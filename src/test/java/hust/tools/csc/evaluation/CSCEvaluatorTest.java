package hust.tools.csc.evaluation;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import hust.tools.csc.util.Sentence;

public class CSCEvaluatorTest {
	
	private ArrayList<Sentence> original;
	private ArrayList<Sentence> gold;
	private ArrayList<Sentence> result;
	
	private CSCEvaluator evaluator;
	
	@Before
	public void setUp() throws Exception {
		original = new ArrayList<>();
		gold = new ArrayList<>();
		result = new ArrayList<>();
				
		Sentence sentence1 = new Sentence("今天共进晚餐。".split(""));
		Sentence sentence11 = new Sentence("今天共进玩餐。".split(""));
		original.add(sentence1);
		gold.add(sentence1);
		result.add(sentence11);
		
		Sentence sentence2 = new Sentence("身心减抗才能事半功倍。".split(""));
		Sentence sentence21 = new Sentence("身心健康才能事半功倍。".split(""));
		Sentence sentence22 = new Sentence("身心件康才能事半功倍。".split(""));
		original.add(sentence2);
		gold.add(sentence21);
		result.add(sentence22);
		
		Sentence sentence3 = new Sentence("你的话对他有吴导，我不希望这种晴况再发生。".split(""));
		Sentence sentence31 = new Sentence("你的话对他有误导，我不希望这种情况再发生。".split(""));
		original.add(sentence3);
		gold.add(sentence31);
		result.add(sentence31);
		
		Sentence sentence4 = new Sentence("我爱自然语言处理。".split(""));
		original.add(sentence4);
		gold.add(sentence4);
		result.add(sentence4);

		Sentence sentence5 = new Sentence("做人要时事求是。".split(""));
		Sentence sentence51 = new Sentence("做人要实事求是。".split(""));
		Sentence sentence52 = new Sentence("做人要时实求是。".split(""));
		
		original.add(sentence5);
		gold.add(sentence51);
		result.add(sentence52);
		
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
		set.add(0);
		
		assertTrue(set.equals(result) );
	}
	
	@Test
	public void testGetTN() {
		HashSet<Integer> result = evaluator.getTN();
		HashSet<Integer> set = new HashSet<>();
		set.add(3);
		
		assertTrue(set.equals(result) );
	}
	
	///////////////////////////////////////////////////	
	@Test
	public void testGetDTP() {
		HashSet<Integer> result = evaluator.getDTP();
		HashSet<Integer> set = new HashSet<>();
		set.add(1);
		set.add(2);

		assertTrue(set.equals(result) );
	}

	@Test
	public void testGetDFP() {
		HashSet<Integer> result = evaluator.getDFP();
		HashSet<Integer> set = new HashSet<>();
		set.add(0);
		set.add(4);

		assertTrue(set.equals(result) );
	}

	@Test
	public void testGetDTN() {
		HashSet<Integer> result = evaluator.getDTN();
		HashSet<Integer> set = new HashSet<>();
		set.add(3);

		assertTrue(set.equals(result) );
	}

	@Test
	public void testGetDFN() {
		HashSet<Integer> result = evaluator.getDFN();
		HashSet<Integer> set = new HashSet<>();
		set.add(4);

		assertTrue(set.equals(result) );
	}
	
	///////////////////////////////////////////////////
	@Test
	public void testGetCTP() {
		HashSet<Integer> result = evaluator.getCTP();
		HashSet<Integer> set = new HashSet<>();
		set.add(2);

		assertTrue(set.equals(result) );
	}
	
	@Test
	public void testGetCFP() {
		HashSet<Integer> result = evaluator.getCFP();
		HashSet<Integer> set = new HashSet<>();
		set.add(0);
		set.add(1);
		set.add(4);
		
		assertTrue(set.equals(result) );
	}

	@Test
	public void testGetCTN() {
		HashSet<Integer> result = evaluator.getCTN();
		HashSet<Integer> set = new HashSet<>();
		set.add(3);
		
		assertTrue(set.equals(result) );
	}
	
	@Test
	public void testGetCFN() {
		HashSet<Integer> result = evaluator.getCFN();
		HashSet<Integer> set = new HashSet<>();
		set.add(1);
		set.add(4);

		assertTrue(set.equals(result) );
	}
	
	/////////////////////////////////////////////////////
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
