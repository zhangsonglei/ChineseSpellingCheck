package hust.tools.scs.score;

import hust.tools.csc.util.Sentence;

/**
 *<ul>
 *<li>Description: 为句子打分的接口 
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月18日
 *</ul>
 */
public interface ScoreSentence {

	public double score(Sentence sentence);
}
