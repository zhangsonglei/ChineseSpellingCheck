package hust.tools.csc.util;

import java.util.HashMap;
import java.util.HashSet;

/**
 *<ul>
 *<li>Description: 候选词(音同、音近、形近)
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月15日
 *</ul>
 */
public class ConfusionSet {
	
	/**
	 * 字与其所有候选词（音同、音近）的映射
	 */
	private HashMap<String, HashSet<String>> similarityPronunciation;
	
	/**
	 * 字与其所有候选词（形近）的映射
	 */
	private HashMap<String, HashSet<String>> similarityShape;
	
	public ConfusionSet() {
	
	}
	
	/**
	 * 混淆集中只有音近的候选字
	 * @param similarityPronunciation	音近的候选字集
	 */
	public ConfusionSet(HashMap<String, HashSet<String>> similarityPronunciation) {
		this.similarityPronunciation = similarityPronunciation;
		similarityShape = new HashMap<>();
	}
	
	/**
	 * 同时使用音近,形近的候选字
	 * @param similarityPronunciation	音近候选字集
	 * @param similarityShape			形近候选字集
	 */
	public ConfusionSet(HashMap<String, HashSet<String>> similarityPronunciation, HashMap<String, HashSet<String>> similarityShape) {
		this.similarityPronunciation = similarityPronunciation;
		this.similarityShape = similarityShape;
	}
	
	/**
	 * 返回给定字的所有候选字数量（包括音近，形近）
	 * @param charcater	给定的字
	 * @return		给定字的候选字数量
	 */
	public int getConfusionCounts(String charcater) {
		int count = 0;
		
		if(containSimilarityPronunciation(charcater)) 
			count += getSimilarityPronunciations(charcater).size();
		
		if(containSimilarityShape(charcater)) 
			count += getSimilarityShapes(charcater).size();
		
		return count;
	}
	
	/**
	 * 返回给定字的形近候选字数量
	 * @param charcater	给定的字
	 * @return			给定字的形近候选字数量
	 */
	public int getSimilarityShapeCounts(String charcater) {
		if(containSimilarityShape(charcater))
			return getSimilarityShapes(charcater).size();
		
		return 0;
	}
	
	/**
	 * 返回给定字的音近候选字数量
	 * @param charcater	给定的字
	 * @return			给定字的音近候选字数量
	 */
	public int getSimilarityPronunciationCounts(String charcater) {
		if(containSimilarityPronunciation(charcater))
			return getSimilarityPronunciations(charcater).size();
		
		return 0;
	}
	
	/**
	 * 返回给定字的所有音近候选字的集合
	 * @param charcater	给定的字
	 * @return			给定字的所有音近候选字的集合
	 */
	public HashSet<String> getSimilarityPronunciations(String character) {
		if(containSimilarityPronunciation(character))
			return similarityPronunciation.get(character);
		
		return null;
	}
	
	/**
	 * 返回给定字的所有形近候选字的集合
	 * @param charcater	给定的字
	 * @return			给定字的所有形近候选字的集合
	 */
	public HashSet<String> getSimilarityShapes(String character) {
		if(containSimilarityShape(character))
			return similarityShape.get(character);
		
		return null;
	}
	
	/**
	 * 为给定字添加一个音近候选字
	 * @param charcater	待添加的字
	 * @param confusion	待添加的音近候选字
	 */
	public void addSimilarityPronunciations(String charcater, String confusion) {
		if(containSimilarityPronunciation(charcater)) 
			similarityPronunciation.get(charcater).add(confusion);
		else {
			HashSet<String> confusions = new HashSet<>();
			confusions.add(confusion);
			similarityPronunciation.put(charcater, confusions);
		}
	}
	
	/**
	 * 为给定字添加一个形近候选字
	 * @param charcater	待添加的字
	 * @param confusion	待添加的形近候选字
	 */
	public void addSimilarityShapes(String charcater, String confusion) {
		if(containSimilarityShape(charcater)) 
			similarityShape.get(charcater).add(confusion);
		else {
			HashSet<String> confusions = new HashSet<>();
			confusions.add(confusion);
			similarityShape.put(charcater, confusions);
		}
	}
	
	/**
	 * 为给定字添加一组音近候选字
	 * @param charcater	待添加的字
	 * @param confusions待添加的音近候选字
	 */
	public void addSimilarityPronunciations(String charcater, HashSet<String> confusions) {
		if(containSimilarityPronunciation(charcater))
			similarityPronunciation.get(charcater).addAll(confusions);
		else 
			similarityPronunciation.put(charcater, confusions);
	}
	
	/**
	 * 为给定字添加一组形近候选字
	 * @param charcater	待添加的字
	 * @param confusions待添加的形近候选字
	 */
	public void addSimilarityShapes(String charcater, HashSet<String> confusions) {
		if(containSimilarityShape(charcater))
			similarityShape.get(charcater).addAll(confusions);
		else 
			similarityShape.put(charcater, confusions);
	}
	
	/**
	 * 添加一组形近候选字，数组第一个为关键字
	 * @param confusions	待添加的字
	 */
	public void addSimilarityShapes(String... confusions) {
		if(confusions.length > 1) {
			for(int i = 1; i < confusions.length; i++)
				addSimilarityShapes(confusions[0], confusions[i]);
		}		
	}
	
	/**
	 * 添加一组形音候选字，数组第一个为关键字
	 * @param confusions	待添加的字
	 */
	public void addSimilarityPronunciations(String... confusions) {
		if(confusions.length > 1) {
			for(int i = 1; i < confusions.length; i++)
				addSimilarityPronunciations(confusions[0], confusions[i]);
		}		
	}
	
	/**
	 * 从音近候选字集中删除给定字
	 * @param charcater	待删除的字
	 * @return			待删除的词的所有音近候选字
	 */
	public HashSet<String> removeSimilarityPronunciations(String charcater) {
		HashSet<String> confusions = null;
		
		if(containSimilarityPronunciation(charcater)) {
			confusions = getSimilarityPronunciations(charcater);
			similarityPronunciation.remove(charcater);
		}
		
		return confusions;
	}
	
	/**
	 * 从形近候选字集中删除给定字
	 * @param charcater	待删除的字
	 * @return			待删除的词的所有形近候选字
	 */
	public HashSet<String> removeSimilarityShapes(String charcater) {
		HashSet<String> confusions = null;
		
		if(containSimilarityPronunciation(charcater)) {
			confusions = getSimilarityShapes(charcater);
			similarityShape.remove(charcater);
		}
		
		return confusions;
	}
	
	/**
	 * 从给定字的形近候选字集中删除给定的候选字
	 * @param charcater	待删除的字
	 * @param confusion	待删除的字的形近候选字
	 */
	public void removeSimilarityShape(String charcater, String confusion) {
		if(containSimilarityShape(charcater))
			if(getSimilarityShapes(charcater).contains(confusion))
				getSimilarityShapes(charcater).remove(confusion);
	}
	
	/**
	 * 从给定字的音近候选字集中删除给定的候选字
	 * @param charcater	待删除的字
	 * @param confusion	待删除的字的音近候选字
	 */
	public void removeSimilarityPronunciations(String charcater, String confusion) {
		if(containSimilarityShape(charcater)) 
			if(getSimilarityShapes(charcater).contains(confusion))
				getSimilarityPronunciations(charcater).remove(confusion);
	}
	
	/**
	 * 判断音近候选字集中是否包含给定关键字
	 * @param charcater	给定关键字
	 * @return			true-包含/false-不包含
	 */
	public boolean containSimilarityPronunciation(String charcater) {
		if(similarityPronunciation.containsKey(charcater))
			return true;
		
		return false;
	}
	
	/**
	 * 判断形近候选字集中是否包含给定关键字
	 * @param charcater	给定关键字
	 * @return			true-包含/false-不包含
	 */
	public boolean containSimilarityShape(String charcater) {
		if(similarityShape.containsKey(charcater))
			return true;
		
		return false;
	}
	
	/**
	 * 判断给定字的音近候选字集中是否包含给定音近候选字
	 * @param charcater	给定音近关键字
	 * @param confusion	给定音近候选字
	 * @return			true-包含/false-不包含
	 */
	public boolean containSimilarityPronunciation(String charcater, String confusion) {
		if(containSimilarityPronunciation(charcater))
			if(getSimilarityPronunciations(charcater).contains(confusion))
				return true;
		
		return false;
	}
	
	/**
	 * 判断给定字的形近候选字集中是否包含给定形近候选字
	 * @param charcater	给定形近关键字字
	 * @param confusion	给定形近候选字
	 * @return			true-包含/false-不包含
	 */
	public boolean containSimilarityShape(String charcater, String confusion) {
		if(containSimilarityShape(charcater))
			if(getSimilarityShapes(charcater).contains(confusion))
				return true;
		
		return false;
	}
}
