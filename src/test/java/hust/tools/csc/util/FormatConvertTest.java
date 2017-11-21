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
		assertTrue(CommonUtils.isHanZi(test1));
		assertFalse(CommonUtils.isHanZi(test2));
		assertFalse(CommonUtils.isHanZi(test3));
		assertFalse(CommonUtils.isHanZi(test4));
		assertFalse(CommonUtils.isHanZi(test5));
		assertFalse(CommonUtils.isHanZi(test6));
		assertFalse(CommonUtils.isHanZi(test7));
	}

	@Test
	public void testIsNumber() {
		assertFalse(CommonUtils.isNumber(test1));
		assertFalse(CommonUtils.isNumber(test2));
		assertFalse(CommonUtils.isNumber(test3));
		assertFalse(CommonUtils.isNumber(test4));
		assertTrue(CommonUtils.isNumber(test5));
		assertFalse(CommonUtils.isNumber(test6));
		assertTrue(CommonUtils.isNumber(test7));
		assertTrue(CommonUtils.isNumber(test7));
		assertTrue(CommonUtils.isNumber(test7));
	}
	
	@Test
	public void testConvert() {
		assertEquals(test5, CommonUtils.ToDBC(test7));
		assertEquals(test7, CommonUtils.ToSBC(test5));
	}
}
