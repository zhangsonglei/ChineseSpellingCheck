package hust.tools.csc.checker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;

import hust.tools.csc.ngram.NGramModel;
import hust.tools.csc.score.AbstractNoisyChannelModel;
import hust.tools.csc.util.ConfusionSet;
import hust.tools.csc.util.FormatConvert;
import hust.tools.csc.util.Sentence;
import hust.tools.csc.util.Sequence;
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
	
	private ConfusionSet confusionSet;
	private AbstractWordSegment wordSegment;
	private NGramModel nGramModel;
	private int order;
	private int beamSize = 100;
//	private final Logger log = LogManager.getLogger(SCAUNoisyChannelModel.class);
	
	public SCAUNoisyChannelModel(NGramModel nGramModel, ConfusionSet confusionSet, AbstractWordSegment wordSegment) throws IOException {
		this.nGramModel = nGramModel;
		this.confusionSet = confusionSet;
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
		
		//连续单字词的最大个数小于2，不作处理直接返回原句
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
				if(i == 1) 
					readDeleteLocations.add(locations.get(i - 1));
				else if(i + 1 == locations.size()) 
					readDeleteLocations.add(locations.get(i));
				else if(locations.get(i+1) - locations.get(i) != 1)
					readDeleteLocations.add(locations.get(i));

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
				if(FormatConvert.isHanZi(word))
					locations.add(index);
			}
			
			index += word.length();
		}
//		System.out.println("单字词所在位置：\t"+locations);
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
	    		
	    		if(!FormatConvert.isHanZi(character))
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
	public double getChannelModelLogScore(Sentence sentence) {
		return 1.0;
	}

	@Override
	public double getSourceModelLogScore(Sentence candidate) {
		return nGramModel.getSentenceLogProb(candidate, order);
	}
}
