package hust.tools.csc.correct;

import java.util.ArrayList;
import java.util.HashMap;

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
	private ArrayList<Correction[]> correctionList;
	
	public CorrectResult() {
		this(new HashMap<>(), new ArrayList<>());
	}
	
	public CorrectResult(HashMap<Integer, Suggestions> map) {
		this(map, new ArrayList<>());
	}
	
	public CorrectResult(ArrayList<Correction[]> correctionList) {
		this(new HashMap<>(), correctionList);
	}
	
	public CorrectResult(HashMap<Integer, Suggestions> map, ArrayList<Correction[]> correctionList) {
		this.correctionList = correctionList;
		this.map = map;
	}
	
	/**
	 * 添加一个候选纠正句的信息（所有纠正的字）
	 * @param corrections	纠正的字组
	 */
	public void add(Correction[] corrections) {
		correctionList.add(corrections);
		
		for(Correction correction : corrections) {
			int location = correction.getLocation();
			String character = correction.getCharacter();
			
			if(map.containsKey(location)) {
				map.get(location).add(character);
			}else {
				Suggestions suggestions = new Suggestions();
				suggestions.add(character);
				
				map.put(location, suggestions);
			}
		}
	}
	
	/**
	 * 返回所有候选纠正结果，第一维度为候选纠正结果的排名，第二维度为纠正结果
	 * @return	所有候选纠正结果
	 */
	public Correction[][] getSuggestions(){
		if(correctionList.size() == 0)
			return null;
		
		Correction[][] corrections = new Correction[correctionList.size()][];
		for(int i = 0; i < correctionList.size(); i++)
			corrections[i] = correctionList.get(0);
		return corrections;
	}

	/**
	 * 返回排名第order的候选纠正结果
	 * @param order	候选纠正结果的排名
	 * @return		排名第order的候选纠正结果
	 */
	public Correction[] getSuggestions(int order) {
		if(order > correctionList.size() || order < 0)
			return null;
		
		return correctionList.get(order);
	}
	
	
	/**
	 * 返回给定位置的纠正词列表，如果该字没错，返回null
	 * @return	给定位置的纠正词列表，如果该字没错，返回null
	 */
	public Suggestions get(int location) {
		if(map.containsKey(location))
			return map.get(location);
		
		return null;
	}
	
	/**
	 * 判断是否含有给定位置的纠正词
	 * @param location	待判断的位置
	 * @return			true-是/false-否
	 */
	public boolean contains(int location) {
		return map.containsKey(location);
	}
}
