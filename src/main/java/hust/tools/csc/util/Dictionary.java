package hust.tools.csc.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 *<ul>
 *<li>Description: 字典，并保存字的频数 
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月13日
 *</ul>
 */
public class Dictionary {
	
	/**
	 * 字与其在语料中出现频数的映射
	 */
	private HashMap<String, Integer> dict;
	
	public Dictionary() {
		dict = new HashMap<>();
	}
	
	public Dictionary(HashMap<String, Integer> dict) {
		this.dict = dict;
	}

	/**
	 * 向字典中添加一个字
	 * @param character	待添加的词
	 */
	public void add(String character) {
		if(dict.containsKey(character))
			dict.put(character, dict.get(character)+ 1);
		else
			dict.put(character, 1);
	}
	
	/**
	 * 为字典中的字的数量加上给定的值
	 * @param character	待加的字
	 * @param count		所加的值
	 */
	public void add(String character, int count) {
		if(contains(character)) 
			dict.put(character, getCount(character) + count);
		else
			dict.put(character, count);
	}
	
	/**
	 * 从词典中移除给定的词，并返回其移除前的数量
	 * @param character	待移除的词
	 * @return			给定词的数量
	 */
	public int remove(String character) {
		int count = 0;
		if(dict.containsKey(character)) {
			count = dict.get(character);
			dict.remove(character);
		}
		
		return count;
	}
	
	/**
	 * 判断词典中是否包含给定的词
	 * @param character	待判断的词
	 * @return			true-包含/false-不包含
	 */
	public boolean contains(String character) {
		return dict.containsKey(character);
	}
	
	/**
	 * 返回给定词的数量
	 * @param word	待返回数量的词
	 * @return		给定词的数量
	 */
	public int getCount(String character) {
		int count = 0;
		if(dict.containsKey(character))
			count = dict.get(character);
		
		return count;
	}
	
	/**
	 * 更新给定词的数量为count
	 * @param character	待更新的词
	 * @param count		character更新后的数量
	 */
	public void update(String character, int count) {
		dict.put(character, count);
	}
	
	/**
	 * 返回字典大小
	 * @return	字典大小
	 */
	public int size() {
		if(dict != null)
			return dict.size();
		
		return 0;
	}
	
	/**
	 * 写字典到指定路径
	 * @param path	待写入的路径
	 * @throws IOException
	 */
	public void writeDict(String path) throws IOException {
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(new File(path)));
		int size = size();
		dos.writeInt(size);
		
		for(Entry<String, Integer> entry : dict.entrySet()) {
			dos.writeUTF(entry.getKey() + "\t" + entry.getValue());
		}
		
		dos.close();
	}
	
	/**
	 * 返回字典key的迭代器
	 * @return
	 */
	public Iterator<String> iterator() {
		return dict.keySet().iterator();
	}
}