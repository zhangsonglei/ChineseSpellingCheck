package hust.tools.csc.util;

import java.util.HashMap;

/**
 *<ul>
 *<li>Description: 词典，并保存词的频数 
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月13日
 *</ul>
 */
public class Dictionary {
	
	/**
	 * 词与其在训练预料中的出现次数的映射
	 */
	private HashMap<Word, Integer> dict;
	
	public Dictionary(HashMap<Word, Integer> dict) {
		this.dict = dict;
	}
	
	/**
	 * 向词典中添加一个词
	 * @param word	待添加的词
	 */
	public void add(Word word) {
		if(dict.containsKey(word))
			dict.put(word, dict.get(word)+ 1);
		else
			dict.put(word, 1);
	}
	
	/**
	 * 从词典中移除给定的词，并返回其移除前的数量
	 * @param word	待移除的词
	 * @return		给定词的数量
	 */
	public int remove(Word word) {
		int count = 0;
		if(dict.containsKey(word)) {
			count = dict.get(word);
			dict.remove(word);
		}
		
		return count;
	}
	
	/**
	 * 判断词典中是否包含给定的词
	 * @param word	待判断的词
	 * @return		true-包含/false-不包含
	 */
	public boolean contains(Word word) {
		return dict.containsKey(word);
	}
	
	/**
	 * 返回给定词的数量
	 * @param word	待返回数量的词
	 * @return		给定词的数量
	 */
	public int getCount(Word word) {
		int count = 0;
		if(dict.containsKey(word))
			count = dict.get(word);
		
		return count;
	}
	
	/**
	 * 更新给定词的数量为count
	 * @param word	待更新的词
	 * @param count	word更新后的数量
	 */
	public void update(Word word, int count) {
		dict.put(word, count);
	}
}
