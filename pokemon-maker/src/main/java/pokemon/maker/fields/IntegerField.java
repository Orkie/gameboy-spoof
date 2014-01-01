package pokemon.maker.fields;

import pokemon.maker.PokemonData;
import pokemon.maker.PokemonDataField;

public class IntegerField implements PokemonDataField {
	private final int value;
	private final int bytes;
	public IntegerField(final int value, final int bytes) {
		final int max = (int) (Math.pow(2, (8*bytes))-1);
		if(value > max) {
			throw new IllegalArgumentException("Invalid value for "+bytes+" byte field ("+value+")");
		}
		
		this.value = value;
		this.bytes = bytes;
	}
	
	public String[] getHex() {
		final String lowByte = "0x" + Integer.toString(value & 0xFF, 16);
		final String highByte = "0x" + Integer.toString((value >> 8) & 0xFF, 16);
		final String higherByte = "0x" + Integer.toString((value >> 16) & 0xFF, 16);
		
		if(bytes == 3) {
			return PokemonData.hex(higherByte, highByte, lowByte);
		} else if(bytes == 2) {
			return PokemonData.hex(highByte, lowByte);
		} else {
			return PokemonData.hex(lowByte);
		}
	}
	
	public String getName() {
		return Integer.toString(value);
	}
	
	public int getValue() {
		return this.value;
	}
}
