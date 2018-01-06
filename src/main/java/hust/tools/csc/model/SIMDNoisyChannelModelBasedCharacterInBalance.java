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
 *<li>Description: 在SIMD噪音通道模型的基础上，引入字的概率,字的概率通过平衡语料获取 (pro = count/totalConfusion)
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年11月16日
 *</ul>
 */
public class SIMDNoisyChannelModelBasedCharacterInBalance extends AbstractNoisyChannelModel {
	
	private Dictionary dictionary;
	private Dictionary charDict;
	
	public SIMDNoisyChannelModelBasedCharacterInBalance(Dictionary dictionary, NGramModel nGramModel,
			ConfusionSet confusionSet, String charType) throws IOException {
		super(confusionSet, nGramModel);
		
		this.dictionary = dictionary;
		charDict = buildCharDict(charType);
	}
	
	public SIMDNoisyChannelModelBasedCharacterInBalance(Dictionary dictionary, NGramModel nGramModel, 
			ConfusionSet confusionSet, String charType, double magicNumber) throws IOException {
		super(confusionSet, nGramModel, magicNumber);
		
		this.dictionary = dictionary;
		charDict = buildCharDict(charType);
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
		ArrayList<Integer> errorLocations = getErrorLocationsBySIMD(dictionary, sentence);
		ArrayList<Sentence> res = new ArrayList<>();
		
		//连续单字词的最大个数小于2，不作处理直接返回原句
		if(errorLocations.size() > 1) {
			res = beamSearch(confusionSet, beamSize, sentence, errorLocations);
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
		double total = getTotalCharcterCount(cands, charDict);
		double count = charDict.getCount(candidate);
		
		return count / total;
	}
}
