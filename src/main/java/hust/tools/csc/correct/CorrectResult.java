package hust.tools.csc.correct;

import java.util.HashMap;
import java.util.Iterator;

/**
 *<ul>
 *<li>Description: 给定位置的纠正候选字列表
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月24日
 *</ul>
 */
public class CorrectResult {
	
	private HashMap<Integer, Suggestions> map;
	
	public CorrectResult(HashMap<Integer, Suggestions> map) {
		this.map = map;
	}
	
	/**
	 * 向队列中添加一个元素
	 * @param suggestion	待添加的元素
	 */
	public void add(int location, Suggestions suggestion) {
		if(!map.containsKey(location)) {
			Iterator<String> iterator = suggestion.iterator();
			while(iterator.hasNext()) {
				map.get(location).add(iterator.next());
			}
		}else
			map.put(location, suggestion);
	}
	
	/**
	 * 返回队列中的第一个元素
	 * @return	队列中的第一个元素
	 */
	public Suggestions get(int location) {
		return map.get(location);
	}
	
	/**
	 * 判断是否含有给定位置的建议列表
	 * @param location	待判断的位置
	 * @return			true-是/false-否
	 */
	public boolean contains(int location) {
		return map.containsKey(location);
	}
}
