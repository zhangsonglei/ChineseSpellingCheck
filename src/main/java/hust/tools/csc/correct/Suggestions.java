package hust.tools.csc.correct;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *<ul>
 *<li>Description: 所有候选字，根据可能性从前向后列出 
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年11月2日
 *</ul>
 */
public class Suggestions {
	
	/**
	 * 所有纠正的候选字列表，根据可能性从前向后列出
	 */
	private ArrayList<String> candCharacters;
	
	public Suggestions() {
		candCharacters = new ArrayList<>();
	}
	
	public Suggestions(ArrayList<String> candCharacters) {
		this.candCharacters = candCharacters;
	}
	
	/**
	 * 向列表中添加一个元素
	 * @param candCharacter	待添加的元素
	 */
	public void add(String candCharacter) {
		if(!candCharacters.contains(candCharacter))
			candCharacters.add(candCharacter);
	}
	
	/**
	 * 返回列表迭代器
	 * @return	列表迭代器
	 */
	public Iterator<String> iterator() {
		return candCharacters.iterator();
	}
	
	/**
	 * 返回给定位置的候选字
	 * @param index	候选字的位置索引
	 * @return		给定位置的候选字
	 */
	public String getCandCharacter(int index) {
		return candCharacters.get(index);
	}
	
	/**
	 * 判断是否包含给定元素
	 * @param candCharacter	待判断的元素
	 * @return				true-是/false-否
	 */
	public boolean contains(String candCharacter) {
		if(candCharacters.contains(candCharacter))
			return true;
		
		return false;
	}
	
	/**
	 * 删除指定元素
	 * @param candCharacter	待删除的元素
	 */
	public void remove(String candCharacter) {
		if(contains(candCharacter))
			candCharacters.remove(candCharacter);
	}
}
