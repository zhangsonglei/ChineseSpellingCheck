package hust.tools.csc.util;

import java.util.HashMap;

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
	
	public Dictionary(HashMap<String, Integer> dict) {
		this.dict = dict;
	}
	
//	public Dictionary(BufferedReader stream) throws IOException {
//		constractDictionary(stream);
//	}
//	
//	/**
//	 * 根据给定的训练语料构建字典
//	 * @param stream	训练语料
//	 * @throws IOException 
//	 */
//	private void constractDictionary(BufferedReader stream) throws IOException {
//		
//		String line = "";
//		while ((line = stream.readLine()) != null) {
//			line = FormatConvert.ToDBC(line).replace("\\s+", "").trim();
//			if(!line.equals("")) {
//				String[] characters = line.split("");
//				
//				for(String character : characters)
//					if(FormatConvert.isHanzi(character))
//						add(character);
//			}
//		}
//	}

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
}
