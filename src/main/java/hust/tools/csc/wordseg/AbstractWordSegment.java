package hust.tools.csc.wordseg;

import hust.tools.csc.util.Sentence;

/**
 *<ul>
 *<li>Description: 分词的抽象类，提供公用的抽象方法 
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月18日
 *</ul>
 */
public abstract class AbstractWordSegment {
	
	protected Sentence sentence;
	
	public AbstractWordSegment(Sentence sentence) {
		this.sentence = sentence;
	}
	
	public abstract String[] segment();
}
