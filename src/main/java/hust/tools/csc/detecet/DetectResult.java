package hust.tools.csc.detecet;

import java.util.ArrayList;

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
	private ArrayList<SpellError[]> errorList;
		
	public DetectResult(ArrayList<SpellError[]> errorList) {
		this.errorList = errorList;
	}
	
	/**
	 * 添加一个拼写错误
	 * @param error
	 */
	public void add(SpellError[] error) {
		if(!errorList.contains(error))
			errorList.add(error);
	}
	
	/**
	 * 返回拼写错误的个数
	 * @return	拼写错误的个数
	 */
	public int errorCounts(int n) {
		return errorList.get(n).length;
	}
	
	public int errorCounts() {
		return errorList.size();
	}
	
	/**
	 * 返回拼写错误的集合
	 * @return	拼写错误的集合
	 */
	public SpellError[] getErrors(int index) {
		if(index >= 0 && index < errorList.size())
			return null;
		
		return errorList.get(index);
	}
	
	@Override
	public String toString() {
		String string = "{";
		
		for(SpellError[] errors : errorList) {
			string += "[";
			for(SpellError error : errors) {
				string += error+" ";
			}
			string = string.trim();
			string += "]";
		}
		
		string += "}";
		
		return string;
	}
}
