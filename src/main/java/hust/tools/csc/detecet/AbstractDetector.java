package hust.tools.csc.detecet;

import hust.tools.csc.util.Sentence;
import hust.tools.csc.wordseg.AbstractWordSegment;
import hust.tools.csc.wordseg.CKIPWordSegment;
import hust.tools.scs.score.NGramScoreSentence;
import hust.tools.scs.score.ScoreSentence;

public abstract class AbstractDetector implements Detector {
	
	protected Sentence sentence;
	private AbstractWordSegment wordSegment; 
	private ScoreSentence scoreSentence;
	
	public AbstractDetector(Sentence sentence) {
		this.sentence = sentence;
		wordSegment = new CKIPWordSegment(sentence);
		scoreSentence = new NGramScoreSentence(sentence);
	}
	
	@Override
	public int wordCounts(Sentence sentence) {
		return wordSegment.segment().length;
	}
	
	@Override
	public String[] wordSegment(Sentence sentence) {		
		return wordSegment.segment();
	}
	
	@Override
	public double scoreSentence(Sentence sentence) {
		return scoreSentence.score();
	}
	
	public abstract void sort();
}
