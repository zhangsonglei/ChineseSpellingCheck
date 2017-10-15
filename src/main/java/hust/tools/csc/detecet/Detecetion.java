package hust.tools.csc.detecet;

import java.util.List;

import hust.tools.csc.util.Sentence;
import hust.tools.csc.util.Word;

/**
 *<ul>
 *<li>Description: 句子检测器的接口
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月10日
 *</ul>
 */
public interface Detecetion {
	
	/**
	 * 返回该句子中的字的数量
	 * @param sentence	待检测的句子
	 * @return	所有字的数量
	 */
	int length(Sentence sentence);
	
	/**
	 * 返回该句子中的词的数量
	 * @param sentence	待检测的句子
	 * @return	所有词的数量
	 */
	int wordCounts(Sentence sentence);
	
	
	/**
	 * 返回组成该句子的所有词
	 * @param sentence	待分词的句子
	 * @return	所有组成该句子的词
	 */
	List<Word> getWords(Sentence sentence);
	
	/**
	 * 给定句子检测出的错误词的数量
	 * @param sentence	待检测的句子
	 * @return			检测出的错误词数量
	 */
	int getErrorCounts(Sentence sentence);
	
	/**
	 * 给定句子检测出的所有错误词的位置
	 * @param sentence	待检测的句子
	 * @return 			所有错误词的位置
	 */
	int[] getErrorLocations(Sentence sentence);
	
	/**
	 * 给定句子检测出的所有错误词
	 * @param sentence	待检测的句子
	 * @return			检测出的错误词
	 */
	Word[] getErrorWords(Sentence sentence);
}