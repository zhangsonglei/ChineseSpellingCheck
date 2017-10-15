package hust.tools.csc.util;

/**
 *<ul>
 *<li>Description: 句子接口 
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月12日
 *</ul>
 */
public interface Sentence {
	
	/**
	 * 该句子的字符长度
	 * @return	字符长度
	 */
	int length();
	
	/**
	 * 返回是否包含子串str
	 * @param str	待检索的子串
	 * @return		true-包含/false-不包含
	 */
	boolean contains(String str);
	
	/**
	 * 返回是否以子串str为开头
	 * @param str	待检索的子串
	 * @return		true-是/false-不是
	 */
	boolean startWith(String str);
	
	/**
	 * 返回是否以子串str为结尾
	 * @param str	待检索的子串
	 * @return		true-是/false-不是
	 */
	boolean endWith(String str);
	
	/**
	 * 返回第一个与给定元素匹配成功的位置索引
	 * @param element	待匹配的元素
	 * @return			第一个与给定元素匹配成功的位置索引
	 */
	int indexOf(String str);
	
	/**
	 * 返回给定索引位置的元素
	 * @param index	待返回的位置索引
	 * @return		给定索引位置的元素
	 */
	String getContent(int index);
	
	/**
	 * 替换给定索引的元素
	 * @param index	待替换的位置索引
	 */
	void setCharacter(int index, String character);
	
	@Override
	int hashCode();
	
	@Override
	boolean equals(Object obj);
	
	@Override
	String toString();
}
