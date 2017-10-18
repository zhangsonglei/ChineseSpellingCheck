package hust.tools.csc.detecet;

import hust.tools.csc.util.Sentence;

public interface Detector {
	
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
	String[] wordSegment(Sentence sentence);
	
	/**
	 * 为给定句子进行打分
	 * @param sentence	待打分的句子
	 * @return	给定句子的分数
	 */
	double scoreSentence(Sentence sentence);
	
	/**
	 * 给定句子检测出的错误词的数量
	 * @param sentence	待检测的句子
	 * @return			检测出的错误词数量
	 */
	int getErrorWordCounts(Sentence sentence);
	
	/**
	 * 给定句子检测出的所有错误词的位置
	 * @param sentence	待检测的句子
	 * @return 			所有错误词的位置
	 */
	int[] getErrorWordLocations(Sentence sentence);
	
	/**
	 * 给定句子检测出的所有错误词
	 * @param sentence	待检测的句子
	 * @return			检测出的错误词
	 */
	String[] getErrorWords(Sentence sentence);
}
