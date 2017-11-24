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
	 * 返回拼写错误的集合
	 * @return	拼写错误的集合
	 */
	public SpellError[] getErrors(int index);
	
	/**
	 * 返回排名第n的候选检错结果的位置，，数组索引与getErrorCharacter（int n）一一对应
	 * @param n		            检错结果的排列的名次
	 * @return			排名第n的候选检错结果（检错的位置）
	 */
	public int[] getErrorLocation(int n);
	
	/**
	 * 返回排名第n的候选检错结果的错字，数组索引与getErrorLocation（int n）一一对应
	 * @param n		            检错结果的排列的名次
	 * @return			排名第n的候选检错结果（检错的位置）
	 */
	public String[] getErrorCharacter(int n);
	
	/**
	 * 返回给定句子的所有候选检错结果的位置索引，二维数组的索引与getErrorCharacter（）一一对应
	 * @return	所有候选检错结果的位置索引		
	 */
	public int[][] getErrorLocation();
	
	/**
	 * 返回给定句子的所有候选检错的错字，二维数组的索引与getErrorLocation（）一一对应
	 * @return	所有候选检错的错字
	 */
	public String[][] getErrorCharacter();
}
