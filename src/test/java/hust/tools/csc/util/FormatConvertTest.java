package hust.tools.csc.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class FormatConvertTest {
	private String test1;
	private String test2;
	private String test3;
	private String test4;
	private String test5;
	private String test6;
	private String test7;
	
	@Before
	public void setup() {
		test1 = "华科";
		test2 = "华ke";
		test3 = "华12";
		test4 = "huake";
		test5 = "90";
		test6 = "hua12";
		test7 = "９０";
	}
	
	@Test
	public void testIsHanZi() {
		assertTrue(FormatConvert.isHanZi(test1));
		assertFalse(FormatConvert.isHanZi(test2));
		assertFalse(FormatConvert.isHanZi(test3));
		assertFalse(FormatConvert.isHanZi(test4));
		assertFalse(FormatConvert.isHanZi(test5));
		assertFalse(FormatConvert.isHanZi(test6));
		assertFalse(FormatConvert.isHanZi(test7));
	}

	@Test
	public void testIsNumber() {
		assertFalse(FormatConvert.isNumber(test1));
		assertFalse(FormatConvert.isNumber(test2));
		assertFalse(FormatConvert.isNumber(test3));
		assertFalse(FormatConvert.isNumber(test4));
		assertTrue(FormatConvert.isNumber(test5));
		assertFalse(FormatConvert.isNumber(test6));
		assertTrue(FormatConvert.isNumber(test7));
		assertTrue(FormatConvert.isNumber(test7));
		assertTrue(FormatConvert.isNumber(test7));
	}
	
	@Test
	public void testConvert() {
		assertEquals(test5, FormatConvert.ToDBC(test7));
		assertEquals(test7, FormatConvert.ToSBC(test5));
	}
}
