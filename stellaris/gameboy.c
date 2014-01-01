#include "inc/hw_ints.h"
#include "inc/hw_types.h"
#include "inc/hw_memmap.h"
#include "driverlib/sysctl.h"
#include "driverlib/gpio.h"
#include "driverlib/fpu.h"
#include "driverlib/uart.h"
#include "driverlib/interrupt.h"
#include "driverlib/ssi.h"
#include "usblib/usblib.h"
#include "usblib/usbcdc.h"
#include "usblib/usb-ids.h"
#include "usblib/device/usbdevice.h"
#include "usblib/device/usbdcdc.h"
#include "driverlib/rom.h"
#include "driverlib/systick.h"
#include "driverlib/timer.h"

#include "uartstdio.h"
#include "pokemon.h"
#include "output.h" // datablock

#define RED_LED   GPIO_PIN_1
#define BLUE_LED  GPIO_PIN_2
#define GREEN_LED GPIO_PIN_3

#define CLK GPIO_PIN_2
#define RX GPIO_PIN_3
#define TX GPIO_PIN_4

#define VALUEOF(val, pin) ((val & pin) != 0)

void enableInterrupts();
byte handleIncomingByte(byte in, byte out);

int main(void) {
	FPULazyStackingEnable();
	SysCtlClockSet(SYSCTL_SYSDIV_4 | SYSCTL_USE_PLL | SYSCTL_OSC_MAIN | SYSCTL_XTAL_16MHZ);

	ROM_SysTickPeriodSet(ROM_SysCtlClockGet());
	ROM_SysTickEnable();

	// set up the GPIO to read gameboy data
	SysCtlPeripheralEnable(SYSCTL_PERIPH_GPIOA);
	GPIOPinTypeGPIOInput(GPIO_PORTA_BASE, CLK|RX);

	GPIOPinTypeGPIOOutput(GPIO_PORTA_BASE, TX);
	GPIOPadConfigSet(GPIO_PORTA_BASE, TX, GPIO_STRENGTH_8MA, GPIO_PIN_TYPE_OD);
	GPIOPinWrite(GPIO_PORTA_BASE, TX, 0);

	// virtual UART
	ROM_SysCtlPeripheralEnable(SYSCTL_PERIPH_GPIOA);
	ROM_GPIOPinConfigure(GPIO_PA0_U0RX);
	ROM_GPIOPinConfigure(GPIO_PA1_U0TX);
	ROM_GPIOPinTypeUART(GPIO_PORTA_BASE, GPIO_PIN_0 | GPIO_PIN_1);
	UARTStdioInit(0);
	UARTprintf("Welcome to POKEMON!\n");

	// turn on green LED
	SysCtlPeripheralEnable(SYSCTL_PERIPH_GPIOF);
	GPIOPinTypeGPIOOutput(GPIO_PORTF_BASE, RED_LED|BLUE_LED|GREEN_LED);
	GPIOPinWrite(GPIO_PORTF_BASE, RED_LED|BLUE_LED|GREEN_LED, RED_LED);
	SysCtlDelay(1000000);

	// set up the GPIO interrupt to read gameboy data
	IntPendClear(INT_GPIOA);
	GPIOPinIntClear(GPIO_PORTA_BASE, CLK|RX);
	GPIOIntTypeSet(SYSCTL_PERIPH_GPIOA, CLK, GPIO_FALLING_EDGE);
	GPIOPinIntEnable(GPIO_PORTA_BASE, CLK);
	enableInterrupts();

	while(1) {
	}
}

void enableInterrupts() {
	ROM_IntMasterEnable();
	ROM_IntEnable(INT_GPIOA);
}

volatile unsigned char interruptCount = 0;
volatile byte gb1Value = 0;
volatile byte gb2Value = 0; // our stuff
volatile byte outputBuffer = 0x00;
void clockInterrupt(void) {

	long val = GPIOPinRead(GPIO_PORTA_BASE, CLK | RX | TX);
	gb1Value |= VALUEOF(val, RX) << (7-interruptCount);
	gb2Value |= VALUEOF(val, TX) << (7-interruptCount);

	if(++interruptCount > 7) {
		outputBuffer = handleIncomingByte(gb1Value, gb2Value);
		gb1Value = gb2Value = 0;
		interruptCount = 0;
	}
	while((GPIOPinRead(GPIO_PORTA_BASE, CLK) & CLK) == 0);// wait for /CLK's rising edge
	GPIOPinWrite(GPIO_PORTA_BASE, TX, outputBuffer & 0x80 ? TX : 0); // write MSB of the output buffer
	outputBuffer = outputBuffer << 1; // shift output buffer along

	// clear this as the last thing we do!
	GPIOPinIntClear(GPIO_PORTA_BASE, CLK);
}

volatile connection_state_t connection_state = NOT_CONNECTED;
volatile trade_centre_state_t trade_centre_state = INIT;
volatile int counter = 0;
byte handleIncomingByte(byte in, byte out) {
	UARTprintf("%x, %x\n", in, out);
	byte send = 0x00;

	switch(connection_state) {
	case NOT_CONNECTED:
		if(in == PKMN_MASTER)
			send = PKMN_SLAVE;
		else if(in == PKMN_BLANK)
			send = PKMN_BLANK;
		else if(in == PKMN_CONNECTED) {
			send = PKMN_CONNECTED;
			connection_state = CONNECTED;
			GPIOPinWrite(GPIO_PORTF_BASE, GREEN_LED | RED_LED, GREEN_LED);
		}
		break;

	case CONNECTED:
		if(in == PKMN_CONNECTED)
			send = PKMN_CONNECTED;
		else if(in == PKMN_TRADE_CENTRE)
			connection_state = TRADE_CENTRE;
		else if(in == PKMN_COLOSSEUM)
			connection_state = COLOSSEUM;
		else if(in == PKMN_BREAK_LINK || in == PKMN_MASTER) {
			connection_state = NOT_CONNECTED;
			send = PKMN_BREAK_LINK;
			GPIOPinWrite(GPIO_PORTF_BASE, GREEN_LED | RED_LED, RED_LED);
		} else {
			send = in;
		}
		break;

	case TRADE_CENTRE:
		if(trade_centre_state == INIT && in == 0x00) {
			if(counter++ == 5) {
				trade_centre_state = READY_TO_GO;
			}
			send = in;
		} else if(trade_centre_state == READY_TO_GO && (in & 0xF0) == 0xF0) {
			trade_centre_state = SEEN_FIRST_WAIT;
			send = in;
		} else if(trade_centre_state == SEEN_FIRST_WAIT && (in & 0xF0) != 0xF0) {
			// TODO - send some random data instead of mirroring
			send = in;

			counter = 0;
			trade_centre_state = SENDING_RANDOM_DATA;
		} else if(trade_centre_state == SENDING_RANDOM_DATA && (in & 0xF0) == 0xF0) {
			if(counter++ == 5) {
				trade_centre_state = WAITING_TO_SEND_DATA;
			}
			send = in;
		} else if(trade_centre_state == WAITING_TO_SEND_DATA && (in & 0xF0) != 0xF0) {
			counter = 0;
			// send first byte
			send = DATA_BLOCK[counter++];
			trade_centre_state = SENDING_DATA;
		} else if(trade_centre_state == SENDING_DATA) {
			send = DATA_BLOCK[counter++];
			if(counter == 415) {
				trade_centre_state = DATA_SENT;
			}
		} else {
			send = in;
		}
		break;

	case COLOSSEUM:
		send = in;
		break;

	default:
		send = in;
		break;
	}

	return send;
}
