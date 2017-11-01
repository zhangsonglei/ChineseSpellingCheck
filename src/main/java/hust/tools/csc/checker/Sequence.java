package hust.tools.csc.checker;

import hust.tools.csc.util.Sentence;

/**
 *<ul>
 *<li>Description: 句子及其得分，用于BeamSearch
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月31日
 *</ul>
 */
public class Sequence implements Comparable<Sequence>{
	
	private Sentence sentence;
	private double score;
	
	public Sequence(Sentence sentence, double score) {
		this.sentence = sentence;
		this.score = score;
	}

	@Override
	public int compareTo(Sequence o) {
		if(o.score != score)
			return o.score > score ? 1 : -1;
		return 0;
	}

	public double getScore() {
		return score;
	}
	
	public Sentence getSentence() {
		return sentence;
	}
	
	public String toString() {
		return sentence.toString();
	}
}
