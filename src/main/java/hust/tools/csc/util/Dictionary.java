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
	
	private HashMap<String, Integer> dict;
	
	public Dictionary(HashMap<String, Integer> dict) {
		this.dict = dict;
	}
	
	public void add(String word) {
		if(dict.containsKey(word))
			dict.put(word, dict.get(word)+ 1);
		else
			dict.put(word, 1);
	}
	
	public int remove(String word) {
		int count = 0;
		if(dict.containsKey(word)) {
			count = dict.get(word);
			dict.remove(word);
		}
		
		return count;
	}
	
	public boolean contains(String word) {
		return dict.containsKey(word);
	}
	
	public int getCount(String word) {
		int count = 0;
		if(dict.containsKey(word))
			count = dict.get(word);
		
		return count;
	}
	
	public void updata(String word, int count) {
		dict.put(word, count);
	}
}
