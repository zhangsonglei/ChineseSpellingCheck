package hust.tools.csc.score;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;

import hust.tools.csc.ngram.NGramModel;
import hust.tools.csc.util.ConfusionSet;
import hust.tools.csc.util.Dictionary;
import hust.tools.csc.util.CommonUtils;
import hust.tools.csc.util.Sentence;
import hust.tools.csc.util.Sequence;

/**
 *<ul>
 *<li>Description: 噪音通道模型抽象类
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月24日
 *</ul>
 */
public abstract class AbstractNoisyChannelModel implements NoisyChannelModel {
	
	protected int bestSize = 5;				//最佳候选句的个数
	protected int order = 3;				//计算句子的n元模型的最高阶数
	protected int beamSize = 50;			//beamSearch方法的beam大小
	protected ConfusionSet confusionSet;
	protected NGramModel nGramModel;
	
	public AbstractNoisyChannelModel(ConfusionSet confusionSet, NGramModel nGramModel) {
		this.confusionSet = confusionSet;
		this.nGramModel = nGramModel;
	}
	
	/**
	 * 根据给定句子，给出得分最高的前size个候选句子
	 * @param confusionSet	字的混淆字集
	 * @param beamSize		搜索束的大小
	 * @param sentence		待搜索的原始句子
	 * @param locations		错误字的位置
	 * @return				得分最高的前size个候选句子
	 */
	protected ArrayList<Sentence> beamSearch(ConfusionSet confusionSet, int beamSize, Sentence sentence, 
			ArrayList<Integer> locations) {
		Queue<Sequence> prev = new PriorityQueue<>(beamSize);
	    Queue<Sequence> next = new PriorityQueue<>(beamSize);
	    Queue<Sequence> tmp;
	    
	    //原始句子的得分
	    double score = 1.0;
	    prev.add(new Sequence(sentence, score));
	    	
	    for(int index : locations) {//遍历每一个错误位置
	    	String character = sentence.getToken(index);
    		
    		if(character == null || !CommonUtils.isHanZi(character))
    			continue;
    		
    		HashSet<String> tmpCands = confusionSet.getConfusionSet(character);
    		tmpCands.add(character);	//将原始字与混淆字同等对待
	    	
	    	int sz = Math.min(beamSize, prev.size());
	    	for(int sc = 0; prev.size() > 0 && sc < sz; sc++) {
	    		Sequence top = prev.remove();
	    		next.add(top);
	    				
	    		Iterator<String> iterator = tmpCands.iterator();
	    		while(iterator.hasNext()) {	//遍历当前字的所有混淆字，分别替换原始字组成新句子，计算句子得分
	    			String candCharacter = iterator.next();
	    			Sentence candSen = top.getSentence().setToken(index, candCharacter);
	    			score = getSourceModelLogScore(candSen) * getChannelModelLogScore(sentence, index, candCharacter, tmpCands);
	    			next.add(new Sequence(candSen, score));
	    		}
	        }

	        prev.clear();
	        tmp = prev;
	        prev = next;
	        next = tmp;
	      }
	    
	    //选取得分最高的bestSize个候选句子
	    ArrayList<Sentence> result = new ArrayList<>();
	    int num = Math.min(bestSize, prev.size());

	    for (int i = 0; i < num; i++)
	      result.add(prev.remove().getSentence());
		
		return result;
	}
	
	/**
	 * 返回候选句子noisy channel model：p(s|c)*p(c)中的p(c)
	 * @param candidate	候选句子
	 * @return	p(c)
	 */
	public abstract double getSourceModelLogScore(Sentence candidate);
	
	/**
	 * 返回noisy channel model：p(s|c)*p(c)中的p(s|c)
	 * @param candidate	候选句子
	 * @return	p(s|c)
	 */
	public abstract double getChannelModelLogScore(Sentence sentence, int location, String candidate,  HashSet<String> cands);
	
	/**
	 * 计算混淆字的字频之和
	 * @param cands			混淆字集
	 * @param dictionary	字与字频的映射
	 * @return				所有混淆字的字频之和
	 */
	protected double getTotalCharcterCount(HashSet<String> cands, Dictionary dictionary) {
		double total = 1.0;
		Iterator<String> iterator = cands.iterator();
		while(iterator.hasNext()) {
			String cand = iterator.next();
			if(dictionary.contains(cand))
				total += dictionary.getCount(cand);
		}
		
		return total;
	}
	
	/**
	 * 计算所有混淆字与其在句子中前后邻居组成的bigram的计数的乘积，第一个字/最后一个字只考虑与其后/前一个字组成的bigram
	 * @param sentence		句子，用于确定当前字的前后邻居
	 * @param index			给定混淆字在句子中的位置
	 * @param cands			候选字集
	 * @param dictionary	bigram与其频数的映射
	 * @return				总数之积
	 */
	protected double getTotalPrefixAndSuffixBigramCount(Sentence sentence, int index, HashSet<String> cands, Dictionary dictionary) {
		double totalPre = 1.0;
		double totalNext = 1.0;
		Iterator<String> iterator = cands.iterator();
		while(iterator.hasNext()) {
			String cand = iterator.next();
			String preToken = "";
			String nextToken = "";
			
			if(index > 0)
				preToken = sentence.getToken(index - 1);
			if(index < sentence.size() - 1)
				nextToken = sentence.getToken(index + 1);
			
			String preBigram = preToken + cand;
			String nextBigram = cand + nextToken;
			if(dictionary.contains(preBigram))
				totalPre += dictionary.getCount(preBigram);
			if(dictionary.contains(nextBigram))
				totalNext += dictionary.getCount(nextBigram);
		}
		
		return totalPre * totalNext;
		
	}
	
	/**
	 * 遍历给定的位置索引列表，返回最大的连续索引的长度
	 * @param locations	待遍历的位置索引列表
	 * @return			最大的连续索引的长度
	 */
	protected int maxContinueSingleWordsLength(ArrayList<Integer> locations) {		
		if(locations.size() < 2) 
			return locations.size();
		
		int max = 0;
		int len = 1;
		for(int i = 1; i < locations.size(); i++) {
			if(locations.get(i) - locations.get(i - 1) == 1)
				len++;
			else {
				max = max > len ? max : len;
				len = 1;
			}
		}
		
		max = max > len ? max : len;
		return max;
	}
	
	/**
	 * 遍历给定词列表返回所有单字词的位置索引
	 * @param words	待遍历的词列表
	 * @return		单字词的位置位置
	 */
	protected ArrayList<Integer> locationsOfSingleWords(ArrayList<String> words) {
		ArrayList<Integer> locations = new ArrayList<>();
		int index = 0;
		for(String word : words) {
			if(word.length() == 1) {
				if(CommonUtils.isHanZi(word)) {
					locations.add(index);
				}
			}
			
			index += word.length();
		}
		
		return locations;
	}
	
	/**
	 * 根据bigram匹配方法确定错误字的位置
	 * @param sentence			待确定错误字的句子
	 * @param errorLocations	错误字在句子中的位置索引列表
	 */
	protected ArrayList<Integer> getErrorLocationsBySIMD(Dictionary dictionary, Sentence sentence) {
		ArrayList<Integer> errorLocations = new ArrayList<>();
		ArrayList<String> bigrams = CommonUtils.generateNGrams(sentence.toString().split(""), 2);

		//可能的错误位置， 当前bigram与下一个bigram中有不存在与字典的，设置当前bigram的第二个字为可能出错的字
		for(int index = 0; index < bigrams.size() - 1; index++) {
			String currentBigram = bigrams.get(index);
			String nextBigram = bigrams.get(index + 1);
			
			if(!(dictionary.contains(currentBigram) && dictionary.contains(nextBigram))) {
				String wrong = sentence.getToken(index + 1);
				//非汉字不考虑
				if(CommonUtils.isHanZi(wrong))
					errorLocations.add(index + 1);
			}
		}//end for

		return errorLocations;
	}
}