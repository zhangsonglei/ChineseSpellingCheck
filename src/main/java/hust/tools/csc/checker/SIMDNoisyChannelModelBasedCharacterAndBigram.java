package hust.tools.csc.checker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import hust.tools.csc.ngram.NGramModel;
import hust.tools.csc.score.AbstractNoisyChannelModel;
import hust.tools.csc.util.ConfusionSet;
import hust.tools.csc.util.Dictionary;
import hust.tools.csc.util.Sentence;

/**
 *<ul>
 *<li>Description: 在SIMD噪音通道模型的基础上，引入字的概率以及当前字与前后邻居组成的bigram的概率 
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年11月16日
 *</ul>
 */
public class SIMDNoisyChannelModelBasedCharacterAndBigram extends AbstractNoisyChannelModel {
	
	private Dictionary dictionary;
	
	public SIMDNoisyChannelModelBasedCharacterAndBigram(Dictionary dictionary, NGramModel nGramModel, ConfusionSet confusionSet) throws IOException {
		super(confusionSet, nGramModel);
		
		this.dictionary = dictionary;
	}

	@Override
	public ArrayList<Sentence> getCorrectSentence(Sentence sentence) {
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
		double total = getTotalCharcterCount(cands, dictionary);
		double totalBigram = getTotalPrefixAndSuffixBigramCount(sentence, location, cands, dictionary);
		
		String preToken = "";
		String nextToken = "";
		if(location > 0)
			preToken = sentence.getToken(location - 1);
		if(location < sentence.size() - 1)
			nextToken = sentence.getToken(location + 1);
		String prefixBigram = preToken + candidate;
		String suffixBigram = candidate + nextToken;
		
		int count = dictionary.getCount(candidate);
		int prefixBigramCount = dictionary.getCount(prefixBigram);
		int suffixBigramCount = dictionary.getCount(suffixBigram);
		
		return (count / total) * (prefixBigramCount * suffixBigramCount / totalBigram);
	}
}
