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
	 * 返回给定句子检测错误的结果
	 * @param sentence	待检测的句子
	 * @return			检测错误的结果
	 */
	DetectResult detect(Sentence sentence);
}
