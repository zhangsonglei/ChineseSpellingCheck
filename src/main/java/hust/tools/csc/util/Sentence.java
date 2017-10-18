package hust.tools.csc.util;

/**
 *<ul>
 *<li>Description: 句子抽象类
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月12日
 *</ul>
 */
public class Sentence implements Comparable<Sentence>{
	
	private String sentence;
	
	public Sentence(String sentence) {
		this.sentence = sentence;
	}
	
	/**
	 * 该句子的字符长度
	 * @return	字符长度
	 */
	public int length(){
		return sentence.length();
	}
	
	/**
	 * 返回是否包含子串str
	 * @param str	待检索的子串
	 * @return		true-包含/false-不包含
	 */
	public boolean contains(String str) {
		return sentence.contains(str);
	}
	
	/**
	 * 返回给定索引位置的元素
	 * @param index	待返回的位置索引
	 * @return		给定索引位置的元素
	 */
	public String getStr(int index) {		
		if(index > -1 && index < length())
			return sentence.split("")[index];
		
		return null;
	}
	
	/**
	 * 替换给定索引的元素
	 * @param index	待替换的位置索引
	 */
	public String update(int index, String string) {
		String[] sen = sentence.split("");
		if(index > -1 && index < length())
			sen[index] = string;
		
		sentence = "";
		for(String str : sen)
			sentence += str;
		
		return sentence;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sentence == null) ? 0 : sentence.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sentence other = (Sentence) obj;
		if (sentence == null) {
			if (other.sentence != null)
				return false;
		} else if (!sentence.equals(other.sentence))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return sentence;
	}

	@Override
	public int compareTo(Sentence arg0) {
		int res = sentence.compareTo(arg0.sentence);
		if(res == 0)
			return 0;
		else
			return res > 0 ? 1 : -1;
	}
}
