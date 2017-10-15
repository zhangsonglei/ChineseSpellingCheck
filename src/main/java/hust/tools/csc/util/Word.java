package hust.tools.csc.util;

/**
 *<ul>
 *<li>Description: 句子中的词 
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月10日
 *</ul>
 */
public class Word implements Comparable<Word> {
	
	/**
	 * 词的内容
	 */
	private String word;
	
	/**
	 * 词的开始位置
	 */
	private int start;
	
	/**
	 * 词的结束位置
	 */
	private int end;

	/**
	 * 创建一个新的词对象
	 *
	 * @param text 词串
	 * @param start 词的开始位置索引
	 */
	public Word(String word, int start) {
		this.word = word;
		this.start = start;
		setEnd();
	}

	/**
	 * 复制一个词对象
	 * @param word 待复制的词
	 */
	public Word(Word word) {
		this.copy(word);
	}

	/**
	 * 设置该词的结束位置索引
	 */
	private void setEnd() {
		end = start + word.length() - 1;
	}
	
	/**
	 * 返回该词的结束位置索引.
	 * @return 该词的结束位置索引
	 */
	public int getEnd() {
		return end;
	}

	/**
	 * 设置该词的开始位置索引
	 * @param start 该词的开始位置索引
	 */
	public void setStart(int start) {
		this.start = start;
		setEnd();
	}

	/**
	 * 返回该词的开始位置索引
	 * @return 该词的开始位置索引.
	 */
	public int getStart() {
	    return start;
	}

	/**
	 * 更新该词的词串.
	 * @param word 更新后的词串
	 */
	public void setWord(String word) {
		this.word = word;
	    setEnd();
	}

	/**
	 * 返回该词的词串
	 * @return 该词的词串
	 */
	public String getWord() {
		return word;
	}

	/**
	 * 将该词设置为目标词
	 * @param word 待复制的词
	 */
	public void copy(Word word) {
		this.word = word.toString();
		this.start = word.getStart();
		setEnd();
	}
	
	/**
	 * 获取给定索引位置的字符
	 * @param index	给定的索引
	 * @return		给定索引位置的字符
	 */
	public Character getCharacter(int index) {
		if(index > end || index < start)
			return null;
		
		return word.toCharArray()[index - start];
	}
	
	/**
	 * 返回该词的长度
	 * @return 该词的长度
	 */
	public int length() {	  
		return word.length();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((word == null) ? 0 : word.hashCode());
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
		Word other = (Word) obj;
		if (word == null) {
			if (other.word != null)
				return false;
		} else if (!word.equals(other.word))
			return false;
		return true;
	}
	
	public int compareTo(Word w) {
		if(this.word.compareTo(w.word) > 0) 
			return 1;
		else if(this.word.compareTo(w.word) < 0)
			return -1;
		else 
			return 0;
	}
	
	@Override
	public String toString() {
		return word;
	}
}