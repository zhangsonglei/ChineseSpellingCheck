package hust.tools.csc.wordseg;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import hust.tools.csc.util.Sentence;

public class CKIPWordSegmentTest {

	private AbstractWordSegment wordSeg;
	private Sentence sentence;
	private String[] words;
	
	@Before
	public void setup() {
		words = new String[]{"網路", "交友", "是", "一", "件", "很", "浪費", "時間", "并", "危險", "的", "行為", "。"};
		
		sentence = new Sentence("網路交友是一件很浪費時間并危險的行為。");
		wordSeg = new CKIPWordSegment();
	}
	
	@Test
	public void testSegment() {
		ArrayList<String> segs = wordSeg.segment(sentence);

		for(int i = 0; i< words.length; i++) 
			assertEquals(words[i], segs.get(i));
		
		assertEquals(words.length, segs.size());
	}

}
