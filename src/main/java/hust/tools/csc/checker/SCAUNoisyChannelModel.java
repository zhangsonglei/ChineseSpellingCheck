package hust.tools.csc.checker;

import java.io.IOException;
import java.util.ArrayList;
import hust.tools.csc.ngram.NGramModel;
import hust.tools.csc.score.AbstractNoisyChannelModel;
import hust.tools.csc.util.ConfusionSet;
import hust.tools.csc.util.Dictionary;
import hust.tools.csc.util.Sentence;
import hust.tools.csc.wordseg.AbstractWordSegment;

/**
 *<ul>
 *<li>Description: 由SCAU提出的利用n元模型为句子打分 
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月18日
 *</ul>
 */
public class SCAUNoisyChannelModel extends AbstractNoisyChannelModel {
	
	private Dictionary dictionary;
	private ConfusionSet confusionSet;
	private AbstractWordSegment wordSegment;
	private NGramModel nGramModel;
	private int order;
	private int beamSize = 150;
	
	public SCAUNoisyChannelModel(NGramModel nGramModel, ConfusionSet confusionSet, AbstractWordSegment wordSegment) throws IOException {
		this.nGramModel = nGramModel;
		this.confusionSet = confusionSet;
		this.wordSegment = wordSegment;
		dictionary = new Dictionary();
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
		
		//连续单字词的最大个数小于2，不作处理直接返回原句
		if(locations.size() > 1) {
			//连续单字词的个数最大等于2的使用bigram，大于2的使用trigram
			int maxLength = maxContinueSingleWordsLength(locations);
			if(maxLength <= 2) 
				order = 2;
			else
				order = 3;
			
			candSens = beamSearch(dictionary, confusionSet, beamSize, sentence, locations);
			return candSens;
		}
		
		candSens.add(sentence);
		return candSens;
	}

	@Override
	public double getChannelModelLogScore(Sentence sentence) {
		return 1.0;
	}

	@Override
	public double getSourceModelLogScore(Sentence candidate) {
		return nGramModel.getSentenceLogProb(candidate, order);
	}
}
