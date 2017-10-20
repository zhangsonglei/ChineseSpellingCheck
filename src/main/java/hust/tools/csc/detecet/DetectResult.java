package hust.tools.csc.detecet;

import java.util.HashSet;

/**
 *<ul>
 *<li>Description: 句子拼写错误检测的结果 
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月20日
 *</ul>
 */
public class DetectResult {
		
	/**
	 * 检测的拼写错误列表
	 */
	private HashSet<SpellError> errorList;
		
	public DetectResult(HashSet<SpellError> errorList) {
		this.errorList = errorList;
	}

	/**
	 * 添加一个拼写错误
	 * @param error
	 */
	public void add(SpellError error) {
		if(!errorList.contains(error))
			errorList.add(error);
	}
	
	/**
	 * 返回拼写错误的个数
	 * @return	拼写错误的个数
	 */
	public int errorCounts() {
		return errorList.size();
	}
	
	/**
	 * 返回拼写错误的集合
	 * @return	拼写错误的集合
	 */
	public HashSet<SpellError> getErrors() {
		return errorList;
	}
}
