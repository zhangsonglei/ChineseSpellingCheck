package hust.tools.csc.detecet;

import hust.tools.csc.util.Sentence;

/**
 *<ul>
 *<li>Description: 句子错误检测器接口 
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月20日
 *</ul>
 */
public interface Detector {
	
	/**
	 * 返回给定句子的查错的结果
	 * @param sentence	待查错的句子
	 * @return			查错的结果
	 */
	public DetectResult detect(Sentence sentence);
	
	/**
	 * 返回给定句子的最优的k个查错的结果
	 * @param sentence	待查错的句子
	 * @param k			返回的候选结果数
	 * @return			查错的k个结果
	 */
	public DetectResult detect(Sentence sentence, int k);
}
