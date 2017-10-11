package hust.tools.csc.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class WordTest {

	private Word word;
	private Word copy;
	
	@Before
	public void setup() {
		word = new Word("12345", 3);
		copy = new Word("1234", 2);
	}
	
	@Test
	public void testGetEnd() {
		assertEquals(7, word.getEnd());
	}

	@Test
	public void testSetStart() {
		word.setStart(1);
		assertEquals(1, word.getStart());
		assertEquals(5, word.getEnd());
	}

	@Test
	public void testSetWord() {
		word.setWord(copy.toString());
		assertEquals(3, word.getStart());
		assertEquals(6, word.getEnd());
		assertEquals(copy.toString(), word.getWord());
		assertEquals(4, word.length());
	}

	@Test
	public void testGetWord() {
		assertEquals("12345", word.getWord());
	}

	@Test
	public void testCopy() {
		word.copy(copy);
		assertEquals(copy.getStart(), word.getStart());
		assertEquals(copy.getEnd(), word.getEnd());
		assertEquals(copy.length(), word.length());
		assertEquals(copy.getWord(), word.getWord());
		assertEquals(copy.toString(), word.toString());
	}
	
	@Test
	public void testGetCharacter() {
		Character ch = '3';
		assertEquals(ch, word.getCharacter(5));
		assertEquals(null, word.getCharacter(2));
		assertEquals(null, word.getCharacter(8));
	}
	
	@Test
	public void testCompareTo() {
		Word word1 = new Word("12345", 1);
		Word word3 = new Word("123456", 1);
		assertEquals(0, word.compareTo(word1));
		assertEquals(1, word.compareTo(copy));
		assertEquals(-1, word.compareTo(word3));
	}

	@Test
	public void testToString() {
		assertEquals("12345", word.toString());
	}

}
