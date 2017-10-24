package hust.tools.csc.correct;

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 *<ul>
 *<li>Description: 纠正建议字优先队列
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月24日
 *</ul>
 */
public class SuggestionQueue {
	
	private PriorityQueue<Suggestion> queue;
	
	public SuggestionQueue(Suggestion[]  suggestions) {
		queue = new PriorityQueue<>(suggestions.length, comparator);
		constructQueue(suggestions);
	}
	
	private void constructQueue(Suggestion[] suggestions) {
		for(Suggestion suggestion : suggestions) 
			add(suggestion);
	}

	private Comparator<Suggestion> comparator = new Comparator<Suggestion>() {
	
		@Override
		public int compare(Suggestion s1, Suggestion s2) {
			return (int)(s1.getCount() - s2.getCount());
		}
	};
	
	/**
	 * 向队列中添加一个元素
	 * @param suggestion	待添加的元素
	 */
	public void add(Suggestion suggestion) {
		if(!queue.contains(suggestion))
			queue.add(suggestion);
	}
	
	/**
	 * 返回队列迭代器
	 * @return	队列迭代器
	 */
	public Iterator<Suggestion> iterator() {
		return queue.iterator();
	}
	
	/**
	 * 返回队列中的第一个元素
	 * @return	队列中的第一个元素
	 */
	public Suggestion poll() {
		return queue.poll();
	}
}
