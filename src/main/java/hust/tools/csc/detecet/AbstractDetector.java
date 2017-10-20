package hust.tools.csc.detecet;

import hust.tools.csc.util.Sentence;

public abstract class AbstractDetector implements Detector {
	
	protected Sentence sentence;
	
	public AbstractDetector(Sentence sentence) {
		this.sentence = sentence;		
	}
	
	/**
	 * 返回分词结果的
	 * @param sentence
	 * @return
	 */
	public abstract String[] wordSegment(Sentence sentence);
	
	public abstract double scoreSentence(Sentence sentence);
	
	public abstract void sort();
}
