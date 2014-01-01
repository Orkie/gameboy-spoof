package pokemon.maker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextConverter {
	private static final Map<Character,String> TABLE;
	static {
		TABLE = new HashMap<Character,String>();
		TABLE.put(' ', "0x7F");
		TABLE.put('A', "0x80");
		TABLE.put('B', "0x81");
		TABLE.put('C', "0x82");
		TABLE.put('D', "0x83");
		TABLE.put('E', "0x84");
		TABLE.put('F', "0x85");
		TABLE.put('G', "0x86");
		TABLE.put('H', "0x87");
		TABLE.put('I', "0x88");
		TABLE.put('J', "0x89");
		TABLE.put('K', "0x8A");
		TABLE.put('L', "0x8B");
		TABLE.put('M', "0x8C");
		TABLE.put('N', "0x8D");
		TABLE.put('O', "0x8E");
		TABLE.put('P', "0x8F");
		TABLE.put('Q', "0x90");
		TABLE.put('R', "0x91");
		TABLE.put('S', "0x92");
		TABLE.put('T', "0x93");
		TABLE.put('U', "0x94");
		TABLE.put('V', "0x95");
		TABLE.put('W', "0x96");
		TABLE.put('X', "0x97");
		TABLE.put('Y', "0x98");
		TABLE.put('Z', "0x99");
		TABLE.put('a', "0xA0");
		TABLE.put('b', "0xA1");
		TABLE.put('c', "0xA2");
		TABLE.put('d', "0xA3");
		TABLE.put('e', "0xA4");
		TABLE.put('f', "0xA5");
		TABLE.put('g', "0xA6");
		TABLE.put('h', "0xA7");
		TABLE.put('i', "0xA8");
		TABLE.put('j', "0xA9");
		TABLE.put('k', "0xAA");
		TABLE.put('l', "0xAB");
		TABLE.put('m', "0xAC");
		TABLE.put('n', "0xAD");
		TABLE.put('o', "0xAE");
		TABLE.put('p', "0xAF");
		TABLE.put('q', "0xB0");
		TABLE.put('r', "0xB1");
		TABLE.put('s', "0xB2");
		TABLE.put('t', "0xB3");
		TABLE.put('u', "0xB4");
		TABLE.put('v', "0xB5");
		TABLE.put('w', "0xB6");
		TABLE.put('x', "0xB7");
		TABLE.put('y', "0xB8");
		TABLE.put('z', "0xB9");
		TABLE.put('-', "0xE3");
		TABLE.put('?', "0xE6");
		TABLE.put('!', "0xE7");
		TABLE.put('.', "0xE8");
		TABLE.put(',', "0xF4");
		TABLE.put('0', "0xF6");
		TABLE.put('1', "0xF7");
		TABLE.put('2', "0xF8");
		TABLE.put('3', "0xF9");
		TABLE.put('4', "0xFA");
		TABLE.put('5', "0xFB");
		TABLE.put('6', "0xFC");
		TABLE.put('7', "0xFD");
		TABLE.put('8', "0xFE");
		TABLE.put('9', "0xFF");
	}
	
	public static List<String> convert(final String text) {
		final List<String> bytes = new ArrayList<String>();
		
		for(int i = 0 ; i < text.length() ; i++) {
			final char c = text.charAt(i);
			final String converted = TABLE.get(c);
			bytes.add(converted == null ? TABLE.get('.') : converted);
		}
		
		return Collections.unmodifiableList(bytes);
	}
	
	public static List<String> terminate(final List<String> bytes) {
		final List<String> list = new ArrayList<String>(bytes);
		list.add("0x50");
		return Collections.unmodifiableList(list);
	}
	
	public static List<String> padTo(final List<String> bytes, final String with, final int lengthBytes) {
		final List<String> list = new ArrayList<String>(bytes);
		while(list.size() < lengthBytes)
			list.add(with);
		return Collections.unmodifiableList(list);
	}
}
