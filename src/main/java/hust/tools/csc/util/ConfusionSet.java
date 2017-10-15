package hust.tools.csc.util;

import java.util.HashMap;
import java.util.HashSet;

/**
 *<ul>
 *<li>Description: 候选词，音同、音近、形近...
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月15日
 *</ul>
 */
public class ConfusionSet {
	
	/**
	 * 词与其所有候选词（音同、音近、形近...的）的映射
	 */
	private HashMap<Word, HashSet<Word>> confusionSet;
	
	public ConfusionSet(HashMap<Word, HashSet<Word>> confusionSet) {
		this.confusionSet = confusionSet;
	}
	
	/**
	 * 返回给定词的候选词数量
	 * @param word	给定的词
	 * @return		给定词的候选词数量
	 */
	public int getConfusionCounts(Word word) {
		if(contains(word))
			return getConfusions(word).size();
		
		return 0;
	}
	
	/**
	 * 返回给定词的所有候选词的列表
	 * @param word	给定的词
	 * @return		给定词的所有候选词的列表
	 */
	public HashSet<Word> getConfusions(Word word) {
		if(contains(word))
			return confusionSet.get(word);
		
		return null;
	}
	
	/**
	 * 为给定词添加一个候选词
	 * @param word		待添加的词
	 * @param confusion	待添加的候选词
	 */
	public void add(Word word, Word confusion) {
		if(contains(word)) 
			confusionSet.get(word).add(confusion);
		else {
			HashSet<Word> confusions = new HashSet<>();
			confusions.add(confusion);
			confusionSet.put(word, confusions);
		}
	}
	
	/**
	 * 为给定词添加一组候选词
	 * @param word		待添加的词
	 * @param confusions待添加的候选词
	 */
	public void add(Word word, HashSet<Word> confusions) {
		if(contains(word))
			confusionSet.get(confusions).addAll(confusions);
		else 
			confusionSet.put(word, confusions);
	}
	
	/**
	 * 从候选词集中删除给定词
	 * @param word	待删除的词
	 * @return		待删除的词的所有候选词
	 */
	public HashSet<Word> remove(Word word) {
		HashSet<Word> rmWords = null;
		
		if(contains(word)) {
			rmWords = getConfusions(word);
			confusionSet.remove(word);
		}
		
		return rmWords;
	}
	
	/**
	 * 从给定词的候选词集中删除给定的候选词
	 * @param word		待删除的词
	 * @param confusion	待删除的词的候选词
	 */
	public void remove(Word word, Word confusion) {
		if(contains(word)) 
			if(getConfusions(word).contains(confusion))
				getConfusions(word).remove(confusion);
	}
	
	/**
	 * 判断候选词集中是否包含给定词
	 * @param word	给定词
	 * @return		true-包含/false-不包含
	 */
	public boolean contains(Word word) {
		if(confusionSet.containsKey(word))
			return true;
		
		return false;
	}
	
	/**
	 * 判断给定词的候选词集中是否包含给定候选词
	 * @param word		给定词
	 * @param confusion	给定候选词
	 * @return			true-包含/false-不包含
	 */
	public boolean contains(Word word, Word confusion) {
		if(contains(word))
			if(getConfusions(word).contains(confusion))
				return true;
		
		return false;
	}
}
