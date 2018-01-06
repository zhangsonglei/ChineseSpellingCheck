package hust.tools.csc.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import hust.tools.csc.ngram.NGramModel;
import hust.tools.csc.util.ConfusionSet;
import hust.tools.csc.util.Dictionary;
import hust.tools.csc.util.Sentence;

/**
 *<ul>
 *<li>Description: 基于字串模型的检测方法（SIMD-> Based on Simple Dictionary ），并利用ngram模型计算句子得分
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年11月1日
 *</ul>
 */
public class SIMDNoisyChannelModel extends AbstractNoisyChannelModel {
	
	private Dictionary dictionary;
	
	public SIMDNoisyChannelModel(Dictionary dictionary, NGramModel nGramModel, ConfusionSet confusionSet) throws IOException {
		super(confusionSet, nGramModel);
		
		this.dictionary = dictionary;
	}
	
	public SIMDNoisyChannelModel(Dictionary dictionary, NGramModel nGramModel, ConfusionSet confusionSet,
			double magicNumber) throws IOException {
		super(confusionSet, nGramModel, magicNumber);
		
		this.dictionary = dictionary;
	}
	
	@Override
	public Sentence getBestSentence(Sentence sentence) {
		return getBestKSentence(sentence, 1).get(0);
	}
	
	@Override
	public ArrayList<Sentence> getBestKSentence(Sentence sentence, int k) {
		if(k < 1)
			throw new IllegalArgumentException("返回候选句子数目不能小于1");
		beamSize = k;
		ArrayList<Integer> tempLocations = getErrorLocationsBySIMD(dictionary, sentence);
//		ArrayList<Integer> errorLocations = new ArrayList<>();
		ArrayList<Sentence> res = new ArrayList<>();
		
//		System.out.println(tempLocations);
//		//确定出错位置，可能出错位置的字的所有候选字与前后字组成trigram，若存在一个trigram出现在字典中，则当前字为错误字，都不存在，则该字不是错字
//		for(int index : tempLocations) {
//			String C = sentence.getToken(index);
//	
//			String C_1 = sentence.getToken(index - 1);
//			String C1 = sentence.getToken(index + 1);
//			
//			HashSet<String> tmpPronCands = confusionSet.getSimilarityPronunciations(C);
//			HashSet<String> tmpCands = new HashSet<>();
//			if(tmpPronCands != null)
//				tmpCands.addAll(tmpPronCands);
//	    		
//			boolean isError = false;
//			Iterator<String> iterator = tmpCands.iterator();
//			while(iterator.hasNext()) {
//				C = iterator.next();
//	    			
//				String trigram = C_1 + C + C1;
//				if(dictionary.contains(trigram)) {
//					isError = true;
//					break;
//				}
//			}
//				
//			if(isError)
//				errorLocations.add(index);
//		}
//		System.out.println(errorLocations);

		//连续单字词的最大个数小于2，不作处理直接返回原句
		if(tempLocations.size() > 1) {
			res = beamSearch(confusionSet, beamSize, sentence, tempLocations);
			return res;
		}
		
		res.add(sentence);
		return res;
	}

	@Override
	public double getSourceModelLogScore(Sentence candidate) {
		return nGramModel.getSentenceLogProb(candidate, order);
	}

	@Override
	public double getChannelModelLogScore(Sentence sentence, int location, String candidate, HashSet<String> cands) {
		return 1.0;
	}
}
