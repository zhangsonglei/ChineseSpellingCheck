package hust.tools.csc.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SentenceTest {

	private Sentence sentence;
	Sentence sentence1 = new Sentence("我爱中国。");
	Sentence sentence2 = new Sentence("我爱中国人。");
	Sentence sentence3 = new Sentence("我爱中国");
	
	@Before
	public void setup() {
		sentence = new Sentence("我爱中国。");
	}

	@Test
	public void testLength() {
		assertEquals(5, sentence.length());
	}

	@Test
	public void testContains() {
		assertTrue(sentence.contains("中国"));
		assertFalse(sentence.contains("中华"));
	}

	@Test
	public void testValueOf() {
		assertEquals("我", sentence.getStr(0));
		assertEquals("。", sentence.getStr(4));
		assertEquals(null, sentence.getStr(5));
		assertEquals(null, sentence.getStr(-1));
	}

	@Test
	public void testUpdate() {
		assertEquals("我爱中国人。", sentence.update(3, "国人"));
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
