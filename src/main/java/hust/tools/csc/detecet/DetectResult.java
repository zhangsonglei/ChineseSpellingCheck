package hust.tools.csc.detecet;

import java.util.ArrayList;
import java.util.Iterator;

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
	 * 拼写错误列表
	 */
	private ArrayList<SpellError[]> errorList;
	
	public DetectResult() {
		this(new ArrayList<>());
	}
	
	public DetectResult(ArrayList<SpellError[]> errorList) {
		this.errorList = errorList;
	}
	
	/**
	 * 返回排名第n的候选检错结果的位置，，数组索引与getErrorCharacter（int n）一一对应
	 * @param n		            检错结果的排列的名次
	 * @return			排名第n的候选检错结果（检错的位置）
	 */
	public int[] getErrorLocation(int n) {
		SpellError[] errors = getErrors(n);
		if(errors == null)
			return null;

		int size = errors.length;
		int[] errorLoactions = new int[size];
		
		for(int i = 0; i < size; i++)
			errorLoactions[i] = errors[i].getLocation();
		
		return errorLoactions;		
	}
	
	/**
	 * 返回排名第n的候选检错结果的错字，数组索引与getErrorLocation（int n）一一对应
	 * @param n		            检错结果的排列的名次
	 * @return			排名第n的候选检错结果（检错的位置）
	 */
	public String[] getErrorCharacter(int n) {
		SpellError[] errors = getErrors(n);
		if(errors == null)
			return null;

		int size = errors.length;
		String[] errorLoactions = new String[size];
		
		for(int i = 0; i < size; i++)
			errorLoactions[i] = errors[i].getCharacter();
		
		return errorLoactions;		
	}
	
	/**
	 * 返回给定句子的所有候选检错结果的位置索引，二维数组的索引与getErrorCharacter（）一一对应
	 * @return	所有候选检错结果的位置索引		
	 */
	public int[][] getErrorLocation() {
		int[][] res = new int[candiateCounts()][];
		
		for(int i = 0; i < res.length; i++) {
			res[i] = getErrorLocation(i);
		}
		return res;
	}
	
	/**
	 * 返回给定句子的所有候选检错的错字，二维数组的索引与getErrorLocation（）一一对应
	 * @return	所有候选检错的错字
	 */
	public String[][] getErrorCharacter() {
		String[][] errors =  new String[candiateCounts()][];
		
		for(int i = 0; i < errors.length; i++) {
			errors[i] = getErrorCharacter(i);
		}
		
		return errors;
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
	 * 返回候选检错的个数
	 * @return	拼写错误的个数
	 */
	public int candiateCounts() {
		return errorList.size();
	}
	
	/**
	 * 返回拼写错误的集合
	 * @return	拼写错误的集合
	 */
	public SpellError[] getErrors(int index) {
		if(index >= 0 && index < errorList.size())
			return errorList.get(index);
		
		return null;
	}
	
	public Iterator<SpellError[]> iterator() {
		return errorList.iterator();
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
