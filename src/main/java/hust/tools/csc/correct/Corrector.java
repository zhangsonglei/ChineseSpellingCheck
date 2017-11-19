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
	 * 返回系统纠正的所有候选结果
	 * @param sentence	待纠正的句子
	 * @return			所有候选结果
	 */
	public CorrectResult correct(Sentence sentence);
	
	/**
	 * 系统自动最佳候选纠正句
	 * @return	最佳纠正句
	 */
	public Sentence autoCorrect(Sentence sentence);
}
