package pokemon.maker;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import pokemon.maker.fields.Item;
import pokemon.maker.fields.Move;
import pokemon.maker.fields.Species;
import pokemon.maker.fields.StatusAilment;
import pokemon.maker.fields.Type;


/**
 * Hello world!
 *
 */
public class PokemonGeneratorApp  {
    @SuppressWarnings("unchecked")
	public static void main( String[] args ) throws Exception {
    	final PokemonData data = new PokemonData(Species.Articuno, 
    			300, 
    			74, 
    			StatusAilment.None, 
    			Type.Fire, // THIS IS IGNORED ONCE TRADE COMMENCES
    			Type.Ghost, // THIS IS IGNORED ONCE TRADE COMMENCES
    			Item.Calcium, 
    			Move.FireBlast,
    			Move.HydroPump, 
    			Move.ThunderPunch, 
    			Move.MegaKick, 
    			1234, 
    			200000, 
    			65535, 
    			65535, 
    			65535, 
    			65535, 
    			65535, 
    			65535, 
    			pp(3, 0),
    			pp(3, 0), 
    			pp(3, 0), 
    			pp(3, 0), 
    			74, 
    			300, 
    			150, 
    			151, 
    			152, 
    			153,
    			"Alchemy",
    			"BOBBO");
    	
    	final List<String> dataBlock = concatenate(
    			trainerName("HACKER"),
    			pokemonTypeBlock(data),
    			data.getBytes(), // pokemon1
    			data.getBytes(), // pokemon2
    			data.getBytes(), // pokemon3
    			data.getBytes(), // pokemon4
    			data.getBytes(), // pokemon5
    			data.getBytes(), // pokemon6
    			trainerName(data.getOriginalTrainerName()), // pokemon1
    			trainerName(data.getOriginalTrainerName()), // pokemon2
    			trainerName(data.getOriginalTrainerName()), // pokemon3
    			trainerName(data.getOriginalTrainerName()), // pokemon4
    			trainerName(data.getOriginalTrainerName()), // pokemon5
    			trainerName(data.getOriginalTrainerName()), // pokemon6
    			data.getTerminatedNickname(), // pokemon1
    			data.getTerminatedNickname(), // pokemon2
    			data.getTerminatedNickname(), // pokemon3
    			data.getTerminatedNickname(), // pokemon4
    			data.getTerminatedNickname(), // pokemon5
    			data.getTerminatedNickname()); // pokemon6
    	
    	final PrintWriter pw = new PrintWriter(new File("output.h"));
    	pw.write(makeArray("DATA_BLOCK", dataBlock));
    	pw.close();
    }
    
    public static int pp(final int ppUpUsed, final int currentPp) {
    	if(ppUpUsed > 3)
    		throw new IllegalArgumentException("Can only use PP-Up a maximum of 3 times on any one move");
    	if(currentPp >= 64)
    		throw new IllegalArgumentException("No move can have greater than 63 PP");
    	
    	return ppUpUsed << 6 | currentPp;
    }
    
    public static List<String> trainerName(final String name) {
		if(name.length() > 7)
			throw new IllegalArgumentException("Trainer name cannot be more than 7 characters long");
		return Collections.unmodifiableList(TextConverter.padTo(TextConverter.terminate(TextConverter.convert(name)), "0x00", 11));
    }
    
    public static List<String> concatenate(final List<String>... byteLists) {
    	final List<String> concatenated = new ArrayList<String>();
    	for(List<String> byteList : byteLists) {
    		concatenated.addAll(byteList);
    	}
    	return Collections.unmodifiableList(concatenated);
    }
    
    public static List<String> pokemonTypeBlock(final PokemonData... data) {
    	if(data.length > 6)
    		throw new IllegalArgumentException("Can only have 6 pokemon in a party!");
    	
    	final List<String> out = new ArrayList<String>();
    	
    	out.add("0x"+Integer.toString(data.length, 16));
    	for(int i = 0 ; i < 6 ; i++) {
    		if(i < data.length) {
    			out.add(data[i].getSpecies().getHex()[0]);
    		} else {
    			out.add("0xFF");
    		}
    	}
    	out.add("0xFF");
    	
    	return Collections.unmodifiableList(out);
    }
    
    public static String makeArray(final String name, final List<String> bytes) {
    	return new StringBuilder("unsigned char ")
    		.append(name)
    		.append("[")
    		.append(bytes.size())
    		.append("] = {")
    		.append(StringUtils.join(bytes, ","))
	    	.append("};\n")
	    	.toString();
    }
}
