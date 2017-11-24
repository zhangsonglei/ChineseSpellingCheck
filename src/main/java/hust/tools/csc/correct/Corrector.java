package hust.tools.csc.correct;

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
	 * 返回所有候选纠正结果，第一维度为候选纠正结果的排名，第二维度为纠正结果
	 * @return	所有候选纠正结果
	 */
	public Correction[][] getCorrects();
	
	/**
	 * 返回排名第order的候选纠正结果
	 * @param order	候选纠正结果的排名
	 * @return		排名第order的候选纠正结果
	 */
	public Correction[] getCorrects(int location);
	
	
	/**
	 * 返回给定位置的纠正词列表，如果该字没错，返回null
	 * @return	给定位置的纠正词列表，如果该字没错，返回null
	 */
	public Suggestions getSuggestions(int location);
}
