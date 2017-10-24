package hust.tools.csc.correct;

/**
 *<ul>
 *<li>Description: 纠正建议字
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月24日
 *</ul>
 */
public class Suggestion {
	
	/**
	 * 建议的字
	 */
	private String character;
	
	/**
	 * 该建议字的频数，由字典获取
	 */
	private int count;
	
	/**
	 * 该建议字在句子中的位置索引
	 */
	private int location;
	
	public Suggestion(String character, int count, int location) {
		this.character = character;
		this.count = count;
		this.location = location;
	}

	public String getCharacter() {
		return character;
	}

	public void setCharacter(String character) {
		this.character = character;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getLocation() {
		return location;
	}

	public void setLocation(int location) {
		this.location = location;
	}
}
