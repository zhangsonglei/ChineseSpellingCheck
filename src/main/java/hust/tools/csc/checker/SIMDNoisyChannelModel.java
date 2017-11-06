package hust.tools.csc.checker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import hust.tools.csc.ngram.NGramModel;
import hust.tools.csc.score.AbstractNoisyChannelModel;
import hust.tools.csc.util.ConfusionSet;
import hust.tools.csc.util.Dictionary;
import hust.tools.csc.util.FormatConvert;
import hust.tools.csc.util.Sentence;

public class SIMDNoisyChannelModel extends AbstractNoisyChannelModel {
	
	private Dictionary dictionary;
	private ConfusionSet confusionSet;
	private NGramModel nGramModel;
	private int order = 3;
	
	public SIMDNoisyChannelModel(Dictionary dictionary, NGramModel nGramModel, ConfusionSet confusionSet) throws IOException {
		this.dictionary = dictionary;
		this.nGramModel = nGramModel;
		this.confusionSet = confusionSet;
	}
	
	@Override
	public ArrayList<Sentence> getCorrectSentence(Sentence sentence) {
		ArrayList<String> bigrams = generateBigrams(sentence.toString().split(""));
		ArrayList<Integer> tempLocations = new ArrayList<>();
		ArrayList<Integer> errorLocations = new ArrayList<>();
		ArrayList<Sentence> res = new ArrayList<>();
		
		//可能的错误位置， 当前bigram与下一个bigram中有不存在与字典的，设置当前bigram的第二个字为可能出错的字
		for(int index = 0; index < bigrams.size() - 1; index++) {
			String currentBigram = bigrams.get(index);
			String nextBigram = bigrams.get(index + 1);
			
			if(!(dictionary.contains(currentBigram) && dictionary.contains(nextBigram)))
				tempLocations.add(index + 1);
		}
		
		
		//确定出错位置，可能出错位置的字的所有候选字与前后字组成trigram，若存在一个trigram出现在字典中，则当前字为错误字，都不存在，则该字不是错字
		for(int index : tempLocations) {
			String C = sentence.getToken(index);
			
			//非汉字不考虑
			if(FormatConvert.isHanZi(C)) {
//				String C_1 = sentence.getToken(index - 1);
//				String C1 = sentence.getToken(index + 1);
//				
//				HashSet<String> tmpPronCands = confusionSet.getSimilarityPronunciations(C);
//	    		HashSet<String> tmpCands = new HashSet<>();
//	    		if(tmpPronCands != null)
//	    			tmpCands.addAll(tmpPronCands);
//	    		
//	    		boolean isError = false;
//	    		Iterator<String> iterator = tmpCands.iterator();
//	    		while(iterator.hasNext()) {
//	    			C = iterator.next();
//	    			
//	    			String trigram = C_1 + C + C1;
//	    			if(dictionary.contains(trigram)) {
//	    				isError = true;
//	    				break;
//	    			}
//	    		}
//				
//	    		if(isError)
	    			errorLocations.add(index);
			}
		}
		
		
		Sentence tempSentence = sentence;
		for(int i : errorLocations)
			tempSentence = tempSentence.setToken(i, "x");
		
		res.add(tempSentence);
		return res;
	}
	
	/**
	 * 将给定句子切分成bigrams
	 * @param input	待切分的句子
	 * @return		bigrams
	 */
	private ArrayList<String> generateBigrams(String[] input)  {
		ArrayList<String> output = new ArrayList<>();
		
		for(int i = 0; i < input.length - 2 + 1; i++) {
			String[] ngrams = new String[2];
			for(int j = i, index = 0; j < i + 2; j++, index++)
				ngrams[index] = input[j];

			String bigram = "";
			for(String ch : ngrams)
				bigram += ch;
			
			output.add(bigram);
		}//end for
		
		return output;
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
