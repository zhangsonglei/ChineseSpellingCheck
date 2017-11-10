package hust.tools.csc.checker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import hust.tools.csc.ngram.NGramModel;
import hust.tools.csc.score.AbstractNoisyChannelModel;
import hust.tools.csc.util.ConfusionSet;
import hust.tools.csc.util.Dictionary;
import hust.tools.csc.util.Sentence;

public class SIMDNoisyChannelModel extends AbstractNoisyChannelModel {
	
	private Dictionary dictionary;
	private ConfusionSet confusionSet;
	private NGramModel nGramModel;
	private final int order = 3;
	private final int beamSize = 150;
	
	public SIMDNoisyChannelModel(Dictionary dictionary, NGramModel nGramModel, ConfusionSet confusionSet) throws IOException {
		this.dictionary = dictionary;
		this.nGramModel = nGramModel;
		this.confusionSet = confusionSet;
	}
	
	@Override
	public ArrayList<Sentence> getCorrectSentence(Sentence sentence) {
		ArrayList<Integer> tempLocations = getErrorLocationsBySIMD(dictionary, sentence);
		ArrayList<Integer> errorLocations = new ArrayList<>();
		ArrayList<Sentence> res = new ArrayList<>();
		
		//确定出错位置，可能出错位置的字的所有候选字与前后字组成trigram，若存在一个trigram出现在字典中，则当前字为错误字，都不存在，则该字不是错字
		for(int index : tempLocations) {
			String C = sentence.getToken(index);
	
			String C_1 = sentence.getToken(index - 1);
			String C1 = sentence.getToken(index + 1);
				
			HashSet<String> tmpPronCands = confusionSet.getSimilarityPronunciations(C);
			HashSet<String> tmpCands = new HashSet<>();
			if(tmpPronCands != null)
				tmpCands.addAll(tmpPronCands);
	    		
			boolean isError = false;
			Iterator<String> iterator = tmpCands.iterator();
			while(iterator.hasNext()) {
				C = iterator.next();
	    			
				String trigram = C_1 + C + C1;
				if(dictionary.contains(trigram)) {
					isError = true;
					break;
				}
			}
				
			if(isError)
				errorLocations.add(index);
		}
		
		//连续单字词的最大个数小于2，不作处理直接返回原句
		if(errorLocations.size() > 1) {
			res = beamSearch(dictionary, confusionSet, beamSize, sentence, errorLocations);
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
	public double getChannelModelLogScore(Sentence candidate) {
		return 1.0;
	}
}
