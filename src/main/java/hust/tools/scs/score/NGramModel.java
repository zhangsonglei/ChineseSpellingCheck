package hust.tools.scs.score;

/**
 *<ul>
 *<li>Description: n元模型接口 
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月20日
 *</ul>
 */
public interface NGramModel {
	
	/**
	 * 返回串的n元概率
	 * @param grams	待计算概率的串
	 * @param n		n元的最高阶
	 * @return		串的n元概率
	 */
	public double getProb(String[] grams, int n);
}
