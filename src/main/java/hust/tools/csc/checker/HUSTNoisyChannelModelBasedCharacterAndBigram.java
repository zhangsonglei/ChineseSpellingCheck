package hust.tools.csc.checker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import hust.tools.csc.ngram.NGramModel;
import hust.tools.csc.score.AbstractNoisyChannelModel;
import hust.tools.csc.util.ConfusionSet;
import hust.tools.csc.util.Dictionary;
import hust.tools.csc.util.Sentence;
import hust.tools.csc.wordseg.AbstractWordSegment;

/**
 *<ul>
 *<li>Description: 在HUST噪音通道模型的基础上，引入字的概率以及当前字与前后邻居组成的bigram的概率 
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年11月16日
 *</ul>
 */
public class HUSTNoisyChannelModelBasedCharacterAndBigram extends AbstractNoisyChannelModel {
	
	private Dictionary dictionary;
	private AbstractWordSegment wordSegment;
 
	public HUSTNoisyChannelModelBasedCharacterAndBigram(Dictionary dictionary, NGramModel nGramModel, ConfusionSet confusionSet,
			AbstractWordSegment wordSegment) throws IOException {
		super(confusionSet, nGramModel);
		
		this.dictionary = dictionary;
		this.wordSegment = wordSegment;
	}
	
	public HUSTNoisyChannelModelBasedCharacterAndBigram(Dictionary dictionary, NGramModel nGramModel, ConfusionSet confusionSet,
			AbstractWordSegment wordSegment, double magicNumber) throws IOException {
		super(confusionSet, nGramModel, magicNumber);
		
		this.dictionary = dictionary;
		this.wordSegment = wordSegment;
	}
	
	@Override
	public ArrayList<Sentence> getCorrectSentence(Sentence sentence) {
		ArrayList<Sentence> candSens = new ArrayList<>();
		ArrayList<String> words = wordSegment.segment(sentence);
		
		if(words.size() < 2) {//分词后，词的个数小于2的不作处理，不作处理直接返回原句
			candSens.add(sentence);
			return candSens;
		}
		
		ArrayList<Integer> locations = locationsOfSingleWords(words);
		ArrayList<Integer> errorLoations = getErrorLocationsBySIMD(dictionary, sentence);
		for(int index : errorLoations) {
			if(!locations.contains(index))
				locations.add(index);
		}
		Collections.sort(locations);
		
		//连续单字词的最大个数小于2，不作处理直接返回原句
		if(locations.size() > 1) {					
			candSens = beamSearch(confusionSet, beamSize, sentence, locations);
			return candSens;
		}
		
		candSens.add(sentence);
		return candSens;
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
		
		return (count / total) * (prefixBigramCount * suffixBigramCount / totalBigram );
	}	
}
