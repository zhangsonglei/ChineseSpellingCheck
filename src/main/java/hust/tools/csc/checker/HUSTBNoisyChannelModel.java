package hust.tools.csc.checker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;

import hust.tools.csc.ngram.NGramModel;
import hust.tools.csc.score.AbstractNoisyChannelModel;
import hust.tools.csc.util.ConfusionSet;
import hust.tools.csc.util.Dictionary;
import hust.tools.csc.util.FormatConvert;
import hust.tools.csc.util.Sentence;
import hust.tools.csc.util.Sequence;
import hust.tools.csc.wordseg.AbstractWordSegment;

public class HUSTBNoisyChannelModel extends AbstractNoisyChannelModel {
	
	private Dictionary dictionary;
	private ConfusionSet confusionSet;
	private NGramModel nGramModel;
	private AbstractWordSegment wordSegment;
	private int order;
	private final int beamSize = 130;
	private HashSet<String> ignores; 
 
	public HUSTBNoisyChannelModel(Dictionary dictionary, NGramModel nGramModel, ConfusionSet confusionSet,
			AbstractWordSegment wordSegment) throws IOException {
		this.nGramModel = nGramModel;
		this.confusionSet = confusionSet;
		this.dictionary = dictionary;
		this.wordSegment = wordSegment;
		ignores = new HashSet<>();
		String[] strings = new String[]{"的", "得", "地", "她", "他", "它", "我"};
		for(String string : strings)
			ignores.add(string);
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
		getErrorLocationsBySIMD(sentence, locations);
		Collections.sort(locations);
		
		//单字词的最大个数小于2，不作处理直接返回原句
		if(locations.size() > 1) {
			
			/**
			 * 连续单字词的个数最大等于2的使用bigram，大于2的使用trigram
			 */
			int maxLength = maxContinueSingleWordsLength(locations);

			if(maxLength <= 2) {
				order = 2;
				candSens = beamSearch(sentence, locations);
			}else {
				order = 3;
				candSens = beamSearch(sentence, locations);
			}

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
		
		ArrayList<Integer> readDeleteLocations = new ArrayList<>();
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
		
		//删除孤立的单字词位置索引
		for(int location : readDeleteLocations) {
			int index = locations.indexOf(location);
			locations.remove(index);
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
				if(FormatConvert.isHanZi(word)) {
					if(!ignores.contains(word))
						locations.add(index);
				}
			}
			
			index += word.length();
		}
		
		return locations;
	}
	
	/**
	 * 根据给定句子，给出得分最高的前size个候选句子
	 * @param sentence	待搜索的原始句子
	 * @param size		搜索束的大小
	 * @param locations	错误字的位置
	 * @return			给出得分最高的前size个候选句子
	 */
	private ArrayList<Sentence> beamSearch(Sentence sentence, ArrayList<Integer> locations) {		
		Queue<Sequence> prev = new PriorityQueue<>(beamSize);
	    Queue<Sequence> next = new PriorityQueue<>(beamSize);
	    Queue<Sequence> tmp;
	    prev.add(new Sequence(sentence, getSourceModelLogScore(sentence) * getChannelModelLogScore(sentence)));
	    	
	    for(int index : locations) {//遍历每一个单字词
	    	int sz = Math.min(beamSize, prev.size());

	    	for(int sc = 0; prev.size() > 0 && sc < sz; sc++) {
	    		Sequence top = prev.remove();
	    		next.add(top);
//	    		log.info(top.getSentence()+"'Score = " + top.getScore());
	    		//音近、形近候选字获取并合并
	    		String character = top.getSentence().getToken(index);
	    		
	    		if(character == null || !FormatConvert.isHanZi(character))
	    			continue;
	    		
	    		HashSet<String> tmpPronCands = confusionSet.getSimilarityPronunciations(character);
//	    		HashSet<String> tmpShapeCands = confusionSet.getSimilarityShapes(character);
	    		HashSet<String> tmpCands = new HashSet<>();
	    		if(tmpPronCands != null)
	    			tmpCands.addAll(tmpPronCands);
//	    		if(tmpShapeCands != null)
//	    			tmpCands.addAll(tmpShapeCands);
	    		
	    		Iterator<String> iterator = tmpCands.iterator();
	    		while(iterator.hasNext()) {
	    			String candCharater = iterator.next();
//	    			int count = dictionary.getCount(candCharater);
	    			int count = 1;
	    			Sentence candSen = top.getSentence().setToken(index, candCharater);
	    			
	    			double score = getSourceModelLogScore(candSen) * getChannelModelLogScore(candSen) * count;
//	    			log.info(candSen+"'Score = " + score);
	    			next.add(new Sequence(candSen, score));
	    		}
	        }

	        prev.clear();
	        tmp = prev;
	        prev = next;
	        next = tmp;
	      }
	    
	    
	    ArrayList<Sentence> result = new ArrayList<>();
	    int num = Math.min(5, prev.size());

	    for (int i = 0; i < num; i++)
	      result.add(prev.remove().getSentence());
		
		return result;
	}

	@Override
	public double getSourceModelLogScore(Sentence candidate) {
		return nGramModel.getSentenceLogProb(candidate, order);
	}

	@Override
	public double getChannelModelLogScore(Sentence candidate) {
		return 1.0;
	}	
	
	/**
	 * 根据n元切分匹配方法确定错误字的位置
	 * @param sentence			待处理的句子
	 * @param errorLocations	错误字在句子中的位置索引列表
	 */
	private void getErrorLocationsBySIMD(Sentence sentence, ArrayList<Integer> errorLocations) {
		ArrayList<String> bigrams = generateBigrams(sentence.toString().split(""));
		
		//可能的错误位置， 当前bigram与下一个bigram中有不存在与字典的，设置当前bigram的第二个字为可能出错的字
		for(int index = 0; index < bigrams.size() - 1; index++) {
			String currentBigram = bigrams.get(index);
			String nextBigram = bigrams.get(index + 1);
			
			if(!(dictionary.contains(currentBigram) && dictionary.contains(nextBigram))) {
				if(!errorLocations.contains(index + 1)) {
					String wrong = sentence.getToken(index + 1);
					//非汉字不考虑,
					if(!ignores.contains(wrong) && FormatConvert.isHanZi(wrong))
						errorLocations.add(index + 1);
				}
			}
		}//end for
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
}
