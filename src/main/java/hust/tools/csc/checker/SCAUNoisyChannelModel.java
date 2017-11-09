package hust.tools.csc.checker;

import java.io.IOException;
import java.util.ArrayList;
import hust.tools.csc.ngram.NGramModel;
import hust.tools.csc.score.AbstractNoisyChannelModel;
import hust.tools.csc.util.ConfusionSet;
import hust.tools.csc.util.Dictionary;
import hust.tools.csc.util.FormatConvert;
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
	private int beamSize = 100;
	
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
	
	/**
	 * 返回连续的单字词的最大长度，并将孤立的单字词位置索引剔除
	 * @param words	词组
	 * @return		连续的单字词的最大长度
	 */
	private int maxContinueSingleWordsLength(ArrayList<Integer> locations) {		
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
	 * 返回单个字的词在句子中的索引
	 * @param words	句子分词后的词
	 * @return		单个字的词在句子中的位置
	 */
	private ArrayList<Integer> locationsOfSingleWords(ArrayList<String> words) {
		ArrayList<Integer> locations = new ArrayList<>();
		int index = 0;
		for(String word : words) {
			if(word.length() == 1) {
				if(FormatConvert.isHanZi(word))
					locations.add(index);
			}
			
			index += word.length();
		}

		return locations;
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
