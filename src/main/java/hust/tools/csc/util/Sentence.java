package hust.tools.csc.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *<ul>
 *<li>Description: 句子抽象类
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月12日
 *</ul>
 */
public class Sentence implements Iterable<String>, Comparable<Sentence> {

	private String[] tokens;
	
	public Sentence(String singleToken) {
		tokens = new String[] {singleToken.intern()};
	}

	public Sentence(String... tokens) {
		if (tokens == null || tokens.length == 0) 
			throw new IllegalArgumentException("句子不能为空");
		
		this.tokens = new String[tokens.length];

		for (int i = 0; i < tokens.length; i++) 
			this.tokens[i] = tokens[i].intern();
	}

	/**
	 * 返回给定位置索引处的字
	 * @param index	给定的位置索引
	 * @return		该位置索引处的字
	 */
	public String getToken(int index) {
		if(index > -1 && index < size())
			return tokens[index];
		
		return null;
	}

	/**
	 * 返回该句子中字的数量
	 * @return	该句子中字的数量
	 */
	public int size() {
		  return tokens.length;
	}

	/**
	 * 返回该句子中所有字的迭代器
	 * @return	所有字的迭代器
	 */
	public Iterator<String> iterator() {
		return new Iterator<String>() {
			
			private int index;

			public boolean hasNext() {
				return index < size();
			}

			public String next() {
				if (hasNext())
					return getToken(index++);
				else 
					throw new NoSuchElementException();
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}

		};
	}
	
	/**
	 * 返回是否包含给定字
	 * @param token	待检索的字
	 * @return		true-包含/false-不包含
	 */
	public boolean contains(String token) {
		for(int i = 0; i < tokens.length; i++) 
			if (token.equals(tokens[i]))
				return true;

	    return false;
	}
	
	/**
	 * 替换给定索引的元素
	 * @param index	待替换的位置索引
	 */
	public Sentence setToken(int index, String token) {
		if(index > -1 && index < size())
			tokens[index] = token;
		
		return new Sentence(tokens);
	}
	
	@Override
	public int compareTo(Sentence tokens) {
		int len1 = this.size();
		int len2 = tokens.size();
		
		int len = Math.min(len1, len2);
		for(int i = 0; i < len; i++) {
			int res = this.tokens[i].compareTo(tokens.tokens[i]);
			if(res != 0)
				return res > 0 ? 1 : -1;
		}
		
		if(len1 == len2)
			return 0;
		else
			return len1 - len2 > 0 ? 1 : -1;		
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(tokens);
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
		if (!Arrays.equals(tokens, other.tokens))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
		
		string.append("[");
		for (int i = 0; i < size(); i++) {
			string.append(getToken(i));
			if (i < size() - 1)
				string.append(",");
		}
		string.append("]");

		return string.toString();
	}
}
