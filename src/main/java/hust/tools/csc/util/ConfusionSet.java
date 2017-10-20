package hust.tools.csc.util;

import java.io.BufferedReader;
import java.io.IOException;
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
	private HashMap<String, HashSet<String>> confusionSet;
	
	public ConfusionSet(HashMap<String, HashSet<String>> confusionSet) {
		this.confusionSet = confusionSet;
	}
	
	public ConfusionSet(BufferedReader file) {
		constructConfusionSet(file);
	}
	
	private void constructConfusionSet(BufferedReader stream) {
		confusionSet = new HashMap<String, HashSet<String>>();
		String line = "";
		try {
			while ((line = stream.readLine())!= null) {
				line = FormatConvert.ToDBC(line).replace("\\s+", "").trim();
				if(!line.equals("")) {
					String[] strings = line.split("");
					add(strings);
				}
			}
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 返回给定字的候选字数量
	 * @param charcater	给定的字
	 * @return		给定字的候选字数量
	 */
	public int getConfusionCounts(String charcater) {
		if(contains(charcater))
			return getConfusions(charcater).size();
		
		return 0;
	}
	
	/**
	 * 返回给定字的所有候选字的列表
	 * @param charcater	给定的字
	 * @return		给定字的所有候选字的列表
	 */
	public HashSet<String> getConfusions(String charcater) {
		if(contains(charcater))
			return confusionSet.get(charcater);
		
		return null;
	}
	
	/**
	 * 为给定字添加一个候选字
	 * @param charcater	待添加的字
	 * @param confusion	待添加的候选字
	 */
	public void add(String charcater, String confusion) {
		if(contains(charcater)) 
			confusionSet.get(charcater).add(confusion);
		else {
			HashSet<String> confusions = new HashSet<>();
			confusions.add(confusion);
			confusionSet.put(charcater, confusions);
		}
	}
	
	/**
	 * 为给定字添加一组候选字
	 * @param charcater	待添加的字
	 * @param confusions待添加的候选字
	 */
	public void add(String charcater, HashSet<String> confusions) {
		if(contains(charcater))
			confusionSet.get(confusions).addAll(confusions);
		else 
			confusionSet.put(charcater, confusions);
	}
	
	/**
	 * 添加一组候选字
	 * @param confusions	
	 */
	public void add(String... confusions) {
		if(confusions.length > 1) {
			for(int i = 1; i < confusions.length; i++)
				add(confusions[0], confusions[i]);
		}		
	}
	
	/**
	 * 从候选字集中删除给定字
	 * @param charcater	待删除的字
	 * @return			待删除的词的所有候选字
	 */
	public HashSet<String> remove(String charcater) {
		HashSet<String> confusions = null;
		
		if(contains(charcater)) {
			confusions = getConfusions(charcater);
			confusionSet.remove(charcater);
		}
		
		return confusions;
	}
	
	/**
	 * 从给定字的候选字集中删除给定的候选字
	 * @param charcater	待删除的字
	 * @param confusion	待删除的字的候选字
	 */
	public void remove(String charcater, String confusion) {
		if(contains(charcater)) 
			if(getConfusions(charcater).contains(confusion))
				getConfusions(charcater).remove(confusion);
	}
	
	/**
	 * 判断候选字集中是否包含给定字
	 * @param charcater	给定字
	 * @return			true-包含/false-不包含
	 */
	public boolean contains(String charcater) {
		if(confusionSet.containsKey(charcater))
			return true;
		
		return false;
	}
	
	/**
	 * 判断给定字的候选字集中是否包含给定候选字
	 * @param charcater	给定字
	 * @param confusion	给定候选字
	 * @return			true-包含/false-不包含
	 */
	public boolean contains(String charcater, String confusion) {
		if(contains(charcater))
			if(getConfusions(charcater).contains(confusion))
				return true;
		
		return false;
	}
}
