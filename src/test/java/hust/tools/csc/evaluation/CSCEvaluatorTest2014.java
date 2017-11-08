package hust.tools.csc.evaluation;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import hust.tools.csc.util.Sentence;

/**
 *<ul>
 *<li>Description: 根据SIGHAN2014年指标和例子测试评价程序的各个指标 
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年11月8日
 *</ul>
 */
public class CSCEvaluatorTest2014 {
	
	private ArrayList<Sentence> original;
	private ArrayList<Sentence> gold;
	private ArrayList<Sentence> result;
	
	private CSCEvaluator evaluator;
	
	@Before
	public void setUp() throws Exception {
		original = new ArrayList<>();
		gold = new ArrayList<>();
		result = new ArrayList<>();
				
		Sentence sentence1 = new Sentence("今天是个好天气，对吗？".split(""));
		original.add(sentence1);
		gold.add(sentence1);
		result.add(sentence1);
		
		Sentence sentence2 = new Sentence("你在不走，我就告诉国家你要造饭。".split(""));
		Sentence sentence21 = new Sentence("你再不走，我就告诉国家你要造反。".split(""));
		Sentence sentence22 = new Sentence("你再不走，我就告诉国家你要造返。".split(""));
		original.add(sentence2);
		gold.add(sentence21);
		result.add(sentence22);
		
		Sentence sentence3 = new Sentence("不要放弃，如果你现在放气，你会申败名裂的。".split(""));
		Sentence sentence31 = new Sentence("不要放弃，如果你现在放弃，你会身败名裂的。".split(""));
		Sentence sentence32 = new Sentence("不要放气，如果你现在放气，你会身败名裂德。".split(""));
		original.add(sentence3);
		gold.add(sentence31);
		result.add(sentence32);
		
		Sentence sentence4 = new Sentence("它是我的，你别想得到它。".split(""));
		Sentence sentence41 = new Sentence("它是我的，你别想的到它。".split(""));
		original.add(sentence4);
		gold.add(sentence4);
		result.add(sentence41);

		Sentence sentence5 = new Sentence("远处有一个小船，若隐若线。".split(""));
		Sentence sentence51 = new Sentence("远处有一个小船，若隐若现。".split(""));
		original.add(sentence5);
		gold.add(sentence51);
		result.add(sentence51);
		
		evaluator = new CSCEvaluator(original, gold, result);
	}
	
	@Test
	public void testGetFalsePositiveRate() {
		assertTrue(evaluator.getFalsePositiveRate() == 0.5);
	}
	
	@Test
	public void testGetFP() {
		HashSet<Integer> result = evaluator.getFP();
		HashSet<Integer> set = new HashSet<>();
		set.add(3);
		
		assertTrue(set.equals(result) );
	}
	
	@Test
	public void testGetTN() {
		HashSet<Integer> result = evaluator.getTN();
		HashSet<Integer> set = new HashSet<>();
		set.add(0);
		
		assertTrue(set.equals(result) );
	}
	
	///////////////////////////////////////////////////	
	@Test
	public void testGetDTP() {
		HashSet<Integer> result = evaluator.getDTP();
		HashSet<Integer> set = new HashSet<>();
		set.add(1);
		set.add(4);

		assertTrue(set.equals(result) );
	}

	@Test
	public void testGetDFP() {
		HashSet<Integer> result = evaluator.getDFP();
		HashSet<Integer> set = new HashSet<>();
		set.add(2);
		set.add(3);

		assertTrue(set.equals(result) );
	}

	@Test
	public void testGetDTN() {
		HashSet<Integer> result = evaluator.getDTN();
		HashSet<Integer> set = new HashSet<>();
		set.add(0);

		assertTrue(set.equals(result) );
	}

	@Test
	public void testGetDFN() {
		HashSet<Integer> result = evaluator.getDFN();
		HashSet<Integer> set = new HashSet<>();
		set.add(2);

		assertTrue(set.equals(result) );
	}
	
	///////////////////////////////////////////////////
	@Test
	public void testGetCTP() {
		HashSet<Integer> result = evaluator.getCTP();
		HashSet<Integer> set = new HashSet<>();
		set.add(4);

		assertTrue(set.equals(result) );
	}
	
	@Test
	public void testGetCFP() {
		HashSet<Integer> result = evaluator.getCFP();
		HashSet<Integer> set = new HashSet<>();
		set.add(1);
		set.add(2);
		set.add(3);
		
		assertTrue(set.equals(result) );
	}

	@Test
	public void testGetCTN() {
		HashSet<Integer> result = evaluator.getCTN();
		HashSet<Integer> set = new HashSet<>();
		set.add(0);
		
		assertTrue(set.equals(result) );
	}
	
	@Test
	public void testGetCFN() {
		HashSet<Integer> result = evaluator.getCFN();
		HashSet<Integer> set = new HashSet<>();
		set.add(1);
		set.add(2);

		assertTrue(set.equals(result) );
	}
	
	/////////////////////////////////////////////////////
	
	@Test
	public void testGetDetectAccuracy() {
		assertTrue(evaluator.getDetectAccuracy() == 0.6);
	}
	
	@Test
	public void testGetDetectPrecision() {
		assertTrue(evaluator.getDetectPrecision() == 0.5);
	}

	@Test
	public void testGetDetectRecall() {
		assertTrue(evaluator.getDetectRecall() == 2.0/3);
	}

	@Test
	public void testGetDetectF() {
		assertEquals(4.0/7, evaluator.getDetectF(), 0.000000000000001);
	}
	
	///////////////////////////////////////////////////
	@Test
	public void testGetCorrectAccuracy() {
		assertTrue(evaluator.getCorrectAccuracy() == 0.4);
	}

	@Test
	public void testGetCorrectPrecision() {
		assertTrue(evaluator.getCorrectPrecision() == 0.25);
	}

	@Test
	public void testGetCorrectRecall() {
		assertTrue(evaluator.getCorrectRecall() == 1.0/3);
	}

	@Test
	public void testGetCorrectF() {
		assertEquals(2.0/7, evaluator.getCorrectF(), 0.000000000000001);
	}
}
