package hust.tools.csc.correct;

import hust.tools.csc.util.Sentence;

/**
 *<ul>
 *<li>Description: 句子纠正器接口 
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月10日
 *</ul>
 */
public interface Corrector {
	
	/**
	 * 返回给定句子的纠正的结果
	 * @param sentence	待纠正的句子
	 * @return			纠正的结果
	 */
	public CorrectResult correct(Sentence sentence);
	
	/**
	 * 返回给定句子的最优的k个纠正的结果
	 * @param sentence	待纠正的句子
	 * @param k			返回的候选结果数
	 * @return			纠正的k个结果
	 */
	public CorrectResult correct(Sentence sentence, int k);
}
