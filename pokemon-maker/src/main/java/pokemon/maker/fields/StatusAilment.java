package pokemon.maker.fields;

import pokemon.maker.PokemonData;
import pokemon.maker.PokemonDataField;

public enum StatusAilment implements PokemonDataField {
	
	None("0x00", "None"),
	Asleep("0x04", "Asleep"),
	Poisoned("0x08", "Poisoned"),
	Burned("0x10", "Burned"),
	Frozen("0x20", "Frozen"),
	Paralyzed("0x40", "Paralyzed");
	
	private final String hexCode;
	private final String name;
	private StatusAilment(final String hexCode, final String name) {
		this.hexCode = hexCode;
		this.name = name;
	}
	
	public String[] getHex() {
		return PokemonData.hex(this.hexCode);
	}
	
	public String getName() {
		return this.name;
	}
}
