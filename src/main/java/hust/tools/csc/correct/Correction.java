package hust.tools.csc.correct;

import java.util.List;

import hust.tools.csc.util.Sentence;
import hust.tools.csc.util.Word;

/**
 *<ul>
 *<li>Description: 针对一条detection中标记的错误，给出一个或多个可能的纠正结果 
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月10日
 *</ul>
 */
public interface Correction {

	/**
	 * 给定句子的n元模型概率
	 * @param sentence	待计算n元模型概率的句子
	 * @param n	n元模型的最大阶数
	 * @return	n元模型概率	
	 */
	double nGramProbablility(Sentence sentence, int n);
	
	/**
	 * 获取所有检测出错位置的所有可能的纠正词
	 * @return 所有检测出错位置的所有可能的纠正词
	 */
	List<Word> getSuggestions(Word word);
	
//	/**
//	 * 该句子分词后的词串
//	 * @param sentence	待分词的句子
//	 * @return	分词后的词串
//	 */
//	Word[] getSegmentedSequence(Sentence sentence);
}
