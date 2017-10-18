package hust.tools.csc.detecet;

/**
 *<ul>
 *<li>Description: 句子检测器的接口
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月10日
 *</ul>
 */
public interface Detecetion {
	
	/**
	 * 给定句子检测出的所有错误字的位置
	 * @param sentence	待检测的句子
	 * @return 			所有错误字的位置
	 */
	int[] getErrorCharacterLocations();
	
	/**
	 * 给定句子检测出的所有错误字
	 * @param sentence	待检测的句子
	 * @return			检测出的所有错误字
	 */
	String[] getErrorCharacters();
}