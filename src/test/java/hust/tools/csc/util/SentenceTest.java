package hust.tools.csc.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SentenceTest {

	private Sentence sentence;
	Sentence sentence1 = new Sentence("我","爱","中","国","。");
	Sentence sentence2 = new Sentence("我","爱","中","国","人","。");
	Sentence sentence3 = new Sentence("我","爱","中","国");
	
	@Before
	public void setup() {
		sentence = new Sentence("我","爱","中","国","。");
	}

	@Test
	public void testLength() {
		assertEquals(5, sentence.size());
	}

	@Test
	public void testContains() {
		assertTrue(sentence.contains("中"));
		assertFalse(sentence.contains("华"));
	}

	@Test
	public void testValueOf() {
		assertEquals("我", sentence.getToken(0));
		assertEquals("。", sentence.getToken(4));
		assertEquals(null, sentence.getToken(5));
		assertEquals(null, sentence.getToken(-1));
	}

	@Test
	public void testUpdate() {
		sentence.setToken(3, "华");
		assertEquals("[我,爱,中,华,。]", sentence.toString());
	}

	@Test
	public void testEqualsObject() {
		
		assertTrue(sentence.equals(sentence1));
		assertFalse(sentence.equals(sentence2));
	}

	@Test
	public void testCompareTo() {
		assertEquals(0, sentence.compareTo(sentence1));
		assertEquals(-1, sentence.compareTo(sentence2));
		assertEquals(1, sentence.compareTo(sentence3));
	}
}
