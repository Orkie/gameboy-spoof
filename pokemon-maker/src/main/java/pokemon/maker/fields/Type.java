package pokemon.maker.fields;

import pokemon.maker.PokemonData;
import pokemon.maker.PokemonDataField;


public enum Type implements PokemonDataField {
	Normal("0x00", "Normal"),
	Fighting("0x01", "Fighting"),
	Flying("0x02", "Flying"),
	Poison("0x03", "Poison"),
	Ground("0x04", "Ground"),
	Rock("0x05", "Rock"),
	Bug("0x07", "Bug"),
	Ghost("0x08", "Ghost"),
	Fire("0x14", "Fire"),
	Water("0x15", "Water"),
	Grass("0x16", "Grass"),
	Electric("0x17", "Electric"),
	Psychic("0x18", "Psychic"),
	Ice("0x19", "Ice"),
	Dragon("0x1A", "Dragon");

	private final String hexCode;
	private final String name;
	private Type(final String hexCode, final String name) {
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
