package hust.tools.csc.correct;

import java.util.List;

import hust.tools.csc.detecet.Detector;
import hust.tools.csc.detecet.SpellError;
import hust.tools.csc.util.Sentence;

/**
 *<ul>
 *<li>Description: 针对一条detection中标记的错误，给出一个或多个可能的纠正结果 
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月10日
 *</ul>
 */
public interface Corrector extends Detector {
	
	/**
	 * 返回检测出错位置的所有可能的纠正词队列，按照优先级别排序
	 * @return 所有检测出错位置的所有可能的纠正词
	 */
	List<String> getSuggestions(SpellError error);
	
	/**
	 * 系统自动纠正给定句子中的错字
	 * @param sentence	待纠正的句子
	 * @return			纠正后的句子
	 */
	Sentence correct();
}
