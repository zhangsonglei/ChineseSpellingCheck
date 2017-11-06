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
import hust.tools.csc.util.Dictionary;
import hust.tools.csc.util.FormatConvert;
import hust.tools.csc.util.Sentence;
import hust.tools.csc.util.Sequence;

public class HUSTBNoisyChannelModel extends AbstractNoisyChannelModel {
	
	private Dictionary dictionary;
	private ConfusionSet confusionSet;
	private NGramModel nGramModel;
	private final int order = 3;
	private final int beamSize = 100;

	public HUSTBNoisyChannelModel(Dictionary dictionary, NGramModel nGramModel, ConfusionSet confusionSet) throws IOException {
		this.nGramModel = nGramModel;
		this.confusionSet = confusionSet;
		this.dictionary = dictionary;
	}
	
	@Override
	public ArrayList<Sentence> getCorrectSentence(Sentence sentence) {
		Queue<Sequence> prev = new PriorityQueue<>(beamSize);
	    Queue<Sequence> next = new PriorityQueue<>(beamSize);
	    Queue<Sequence> tmp;
	    prev.add(new Sequence(sentence, getSourceModelLogScore(sentence) * getChannelModelLogScore(sentence)));
	    	
	    for(int index = 0; index < sentence.size(); index++) {//遍历每一个单字词
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
	    		HashSet<String> tmpShapeCands = confusionSet.getSimilarityShapes(character);
	    		HashSet<String> tmpCands = new HashSet<>();
	    		if(tmpPronCands != null)
	    			tmpCands.addAll(tmpPronCands);
	    		if(tmpShapeCands != null)
	    			tmpCands.addAll(tmpShapeCands);
	    		
	    		Iterator<String> iterator = tmpCands.iterator();
	    		while(iterator.hasNext()) {
	    			String candCharater = iterator.next();
	    			Sentence candSen = top.getSentence().setToken(index, candCharater);
	    			
	    			double score = getSourceModelLogScore(candSen) * getChannelModelLogScore(candSen);
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
}
