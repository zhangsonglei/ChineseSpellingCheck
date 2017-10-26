package hust.tools.csc.correct;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Suggestions {
	
	private ArrayList<Correction> corrections;
	
	public Suggestions() {
		corrections = new ArrayList<>();
	}
	
	public Suggestions(ArrayList<Correction> corrections) {
		this.corrections = corrections;
		Collections.sort(corrections);
	}
	
	/**
	 * 向队列中添加一个元素
	 * @param suggestion	待添加的元素
	 */
	public void add(Correction correction) {
		if(!corrections.contains(correction))
			corrections.add(correction);
		
		Collections.sort(corrections);
	}
	
	/**
	 * 返回队列迭代器
	 * @return	队列迭代器
	 */
	public Iterator<Correction> iterator() {
		return corrections.iterator();
	}
	
	/**
	 * 返回队列中的第一个元素
	 * @return	队列中的第一个元素
	 */
	public Correction get(int index) {
		return corrections.get(index);
	}
	
	/**
	 * 判断是否包含给定元素
	 * @param suggestion	待判断的元素
	 * @return				true-是/false-否
	 */
	public boolean contains(Correction correction) {
		if(corrections.contains(correction))
			return true;
		
		return false;
	}
	
	/**
	 * 删除指定元素
	 * @param suggestion	待删除的元素
	 */
	public void remove(Correction correction) {
		if(contains(correction))
			corrections.remove(correction);
	}
}
