package hust.tools.csc.detecet;

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
	 * 返回所有检测出的错误的字
	 * @return	所有检测出的错误的字
	 */
	String[] getErrorCharacter();
	
	/**
	 * 返回所有检测出的错误字的位置
	 * @return	所有检测出的错误字的位置
	 */
	int[] getErrorLocation();
	
	/**
	 * 返回给定句子检测错误的结果
	 * @param sentence	待检测的句子
	 * @return			检测错误的结果
	 */
	DetectResult detect();
}
