package hust.tools.csc.detecet;

import hust.tools.csc.util.Sentence;

public abstract class AbstractDetector implements Detector {
	
	protected Sentence sentence;
	
	public AbstractDetector(Sentence sentence) {
		this.sentence = sentence;		
	}
	
	public abstract double scoreSentence(Sentence sentence);
	
	public abstract void sort();
}
