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
 *<li>Description: 组合BCWS与SIMD检错方法(将两者检错位置组合起来作为出错位置)，利用n元模型为句子打分
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年11月10日
 *</ul>
 */
public class HUSTNoisyChannelModel extends AbstractNoisyChannelModel {

	private Dictionary dictionary;
	private AbstractWordSegment wordSegment;
 
	public HUSTNoisyChannelModel(Dictionary dictionary, NGramModel nGramModel, ConfusionSet confusionSet,
			AbstractWordSegment wordSegment) throws IOException {
		super(confusionSet, nGramModel);
		
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
		return 1.0;
	}	
}
