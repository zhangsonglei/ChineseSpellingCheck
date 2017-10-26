package hust.tools.csc.correct;

import hust.tools.csc.detecet.SpellError;
import hust.tools.csc.util.Sentence;

/**
 *<ul>
 *<li>Description: 针对一条detector中标记的错误，给出一个或多个可能的纠正结果 
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月10日
 *</ul>
 */
public interface Corrector {

	/**
	 * 返回所有检测出的错误字的候选字
	 * @return	所有检测出的错误字的候选字
	 */
	public String[][] getSuggestions();

	/**
	 * 返回指定检测出的错误字的候选字
	 * @param spellError	指定的检测出的错误字
	 * @return				指定检测出的错误字的候选字
	 */
	public String[] getSuggestions(SpellError spellError);
	
	/**
	 * 系统自动纠正给定句子中的错字
	 * @param sentence	待纠正的句子
	 * @return			纠正后的句子
	 */
	public CorrectResult correct();
	
	/**
	 * 系统自动返回一个纠正句
	 * @return	
	 */
	public Sentence autoCorrect();
}
