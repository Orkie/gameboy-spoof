package pokemon.maker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import pokemon.maker.fields.IntegerField;
import pokemon.maker.fields.Item;
import pokemon.maker.fields.Move;
import pokemon.maker.fields.Species;
import pokemon.maker.fields.StatusAilment;
import pokemon.maker.fields.Type;


public class PokemonData {
	public static String[] hex(final String... values) {
		return values;
	}
	
	private final Species species;
	private final IntegerField currentHp;
	private final IntegerField levelPc;
	private final StatusAilment statusAilment;
	private final Type type1;
	private final Type type2;
	private final Item itemHeld;
	private final Move move1;
	private final Move move2;
	private final Move move3;
	private final Move move4;
	private final IntegerField originalTrainerId;
	private final IntegerField exp;
	private final IntegerField hpEv;
	private final IntegerField attackEv;
	private final IntegerField defenseEv;
	private final IntegerField speedEv;
	private final IntegerField specialEv;
	private final IntegerField iv;
	private final IntegerField move1pp;
	private final IntegerField move2pp;
	private final IntegerField move3pp;
	private final IntegerField move4pp;
	private final IntegerField level;
	private final IntegerField maxHp;
	private final IntegerField attack;
	private final IntegerField defense;
	private final IntegerField speed;
	private final IntegerField special;
	
	private final String nickname;
	private final String originalTrainerName;
	
	public PokemonData(final Species species,
						final int currentHp,
						final int levelPc,
						final StatusAilment statusAilment,
						final Type type1,
						final Type type2,
						final Item itemHeld,
						final Move move1,
						final Move move2,
						final Move move3,
						final Move move4,
						final int originalTrainerId,
						final int exp,
						final int hpEv,
						final int attackEv,
						final int defenseEv,
						final int speedEv,
						final int specialEv,
						final int iv,
						final int move1pp,
						final int move2pp,
						final int move3pp,
						final int move4pp,
						final int level,
						final int maxHp,
						final int attack,
						final int defense,
						final int speed,
						final int special,
						final String nickname,
						final String originalTrainerName) {
		this.species = species;
		this.currentHp = new IntegerField(currentHp, 2);
		this.levelPc = new IntegerField(levelPc, 1);
		this.statusAilment = statusAilment;
		this.type1 = type1;
		this.type2 = type2;
		this.itemHeld = itemHeld;
		this.move1 = move1;
		this.move2 = move2;
		this.move3 = move3;
		this.move4 = move4;
		this.originalTrainerId = new IntegerField(originalTrainerId, 2);
		this.exp = new IntegerField(exp, 3);
		this.hpEv = new IntegerField(hpEv, 2);
		this.attackEv = new IntegerField(attackEv, 2);
		this.defenseEv = new IntegerField(defenseEv, 2);
		this.speedEv = new IntegerField(speedEv, 2);
		this.specialEv = new IntegerField(specialEv, 2);
		this.iv = new IntegerField(iv, 2);
		this.move1pp = new IntegerField(move1pp, 1);
		this.move2pp = new IntegerField(move2pp, 1);
		this.move3pp = new IntegerField(move3pp, 1);
		this.move4pp = new IntegerField(move4pp, 1);
		this.level = new IntegerField(level, 1);
		this.maxHp = new IntegerField(maxHp, 2);
		this.attack = new IntegerField(attack, 2);
		this.defense = new IntegerField(defense, 2);
		this.speed = new IntegerField(speed, 2);
		this.special = new IntegerField(special, 2);
		
		this.nickname = nickname;
		if(nickname.length() > 10)
			throw new IllegalArgumentException("Nickname cannot be more than 10 characters long");
		
		this.originalTrainerName = originalTrainerName;
		if(originalTrainerName.length() > 7)
			throw new IllegalArgumentException("Original trainer name cannot be more than 7 characters long");
		
	}
	
	public List<String> getTerminatedNickname() {
		return TextConverter.padTo(TextConverter.terminate(TextConverter.convert(nickname)), "0x50", 11);
	}
	
	public String getOriginalTrainerName() {
		return this.originalTrainerName;
	}
	
	public List<String> getBytes() {
		final List<String> bytes = new ArrayList<String>();
		
		bytes.addAll(Arrays.asList(species.getHex()));
		bytes.addAll(Arrays.asList(currentHp.getHex()));
		bytes.addAll(Arrays.asList(levelPc.getHex()));
		bytes.addAll(Arrays.asList(statusAilment.getHex()));
		bytes.addAll(Arrays.asList(type1.getHex()));
		bytes.addAll(Arrays.asList(type2.getHex()));
		bytes.addAll(Arrays.asList(itemHeld.getHex()));
		bytes.addAll(Arrays.asList(move1.getHex()));
		bytes.addAll(Arrays.asList(move2.getHex()));
		bytes.addAll(Arrays.asList(move3.getHex()));
		bytes.addAll(Arrays.asList(move4.getHex()));
		bytes.addAll(Arrays.asList(originalTrainerId.getHex()));
		bytes.addAll(Arrays.asList(exp.getHex()));
		bytes.addAll(Arrays.asList(hpEv.getHex()));
		bytes.addAll(Arrays.asList(attackEv.getHex()));
		bytes.addAll(Arrays.asList(defenseEv.getHex()));
		bytes.addAll(Arrays.asList(speedEv.getHex()));
		bytes.addAll(Arrays.asList(specialEv.getHex()));
		bytes.addAll(Arrays.asList(iv.getHex()));
		bytes.addAll(Arrays.asList(move1pp.getHex()));
		bytes.addAll(Arrays.asList(move2pp.getHex()));
		bytes.addAll(Arrays.asList(move3pp.getHex()));
		bytes.addAll(Arrays.asList(move4pp.getHex()));
		bytes.addAll(Arrays.asList(level.getHex()));
		bytes.addAll(Arrays.asList(maxHp.getHex()));
		bytes.addAll(Arrays.asList(attack.getHex()));
		bytes.addAll(Arrays.asList(defense.getHex()));
		bytes.addAll(Arrays.asList(speed.getHex()));
		bytes.addAll(Arrays.asList(special.getHex()));
		
		return Collections.unmodifiableList(bytes);
	}

	public Species getSpecies() {
		return this.species;
	}
}
