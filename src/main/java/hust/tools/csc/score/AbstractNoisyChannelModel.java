package hust.tools.csc.score;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;

import hust.tools.csc.util.ConfusionSet;
import hust.tools.csc.util.Dictionary;
import hust.tools.csc.util.FormatConvert;
import hust.tools.csc.util.Sentence;
import hust.tools.csc.util.Sequence;

/**
 *<ul>
 *<li>Description: 噪音通道模型抽象类
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月24日
 *</ul>
 */
public abstract class AbstractNoisyChannelModel implements NoisyChannelModel {
		
	public AbstractNoisyChannelModel() {

	}
	
	/**
	 * 根据给定句子，给出得分最高的前size个候选句子
	 * @param sentence	待搜索的原始句子
	 * @param size		搜索束的大小
	 * @param locations	错误字的位置
	 * @return			给出得分最高的前size个候选句子
	 */
	protected ArrayList<Sentence> beamSearch(Dictionary dictionary, ConfusionSet confusionSet, int beamSize, 
			Sentence sentence, ArrayList<Integer> locations) {
		
		Queue<Sequence> prev = new PriorityQueue<>(beamSize);
	    Queue<Sequence> next = new PriorityQueue<>(beamSize);
	    Queue<Sequence> tmp;
	    prev.add(new Sequence(sentence, getSourceModelLogScore(sentence) * getChannelModelLogScore(sentence)));
	    	
	    for(int index : locations) {//遍历每一个单字词
	    	String character = sentence.getToken(index);
    		
    		if(character == null || !FormatConvert.isHanZi(character))
    			continue;
    		
    		HashSet<String> tmpPronCands = confusionSet.getSimilarityPronunciations(character);
//    		HashSet<String> tmpShapeCands = confusionSet.getSimilarityShapes(character);
    		HashSet<String> tmpCands = new HashSet<>();
    		if(tmpPronCands != null)
    			tmpCands.addAll(tmpPronCands);
//    		if(tmpShapeCands != null)
//    			tmpCands.addAll(tmpShapeCands);
	    	
    		double total = 0.0;
    		Iterator<String> iterator = tmpCands.iterator();
    		while(iterator.hasNext()) {
    			String cand = iterator.next();
    			int count = 1;
    			if(dictionary.contains(cand))
    				count += dictionary.getCount(cand);
    			
    			total += count;
    		}
    		
	    	int sz = Math.min(beamSize, prev.size());
	    	for(int sc = 0; prev.size() > 0 && sc < sz; sc++) {
	    		Sequence top = prev.remove();
	    		next.add(top);
//	    		log.info(top.getSentence()+"'Score = " + top.getScore());
	    		//音近、形近候选字获取并合并
	    		
	    		
	    		iterator = tmpCands.iterator();
	    		while(iterator.hasNext()) {
	    			String candCharacter = iterator.next();
	    			
	    			double count = 1.0;
	    			if(dictionary.contains(candCharacter))
	    				count += dictionary.getCount(candCharacter);

	    			Sentence candSen = top.getSentence().setToken(index, candCharacter);
	    			
	    			double score = getSourceModelLogScore(candSen) * getChannelModelLogScore(candSen) * count / total;
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
	
	
	/**
	 * 返回候选句子noisy channel model：p(s|c)*p(c)中的p(c)
	 * @param candidate	候选句子
	 * @return	p(c)
	 */
	public abstract double getSourceModelLogScore(Sentence candidate);
	
	/**
	 * 返回noisy channel model：p(s|c)*p(c)中的p(s|c)
	 * @param candidate	候选句子
	 * @return	p(s|c)
	 */
	public abstract double getChannelModelLogScore(Sentence candidate);
}
