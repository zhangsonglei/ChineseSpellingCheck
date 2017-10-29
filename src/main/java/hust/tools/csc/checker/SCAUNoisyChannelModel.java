package hust.tools.csc.checker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import hust.tools.csc.ngram.NGramModel;
import hust.tools.csc.score.AbstractNoisyChannelModel;
import hust.tools.csc.util.ConfusionSet;
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
	
	private ConfusionSet confusionSet;
	private double INIT_Parameter = 35;
	private AbstractWordSegment wordSegment;
	private Sentence correctSentence;
	
	public SCAUNoisyChannelModel(NGramModel nGramModel, ConfusionSet confusionSet, AbstractWordSegment wordSegment) throws IOException {
		this.nGramModel = nGramModel;
		this.confusionSet = confusionSet;
		this.wordSegment = wordSegment;
	}

	@Override
	public Sentence getCorrectSentence(Sentence sentence) {
		ArrayList<String> words = wordSegment.segment(sentence);
		
		if(words.size() < 2) {//分词后，词的个数小于2的不作处理，不作处理直接返回原句
			return sentence;
		}
		
		ArrayList<Integer> locations = locationsOfSingleWords(words);
		
		//连续单字词的最大个数小于2，不作处理直接返回原句
		if(locations.size() > 1) {
			String[][] candidateCharacters = initCandidateCharacters(sentence);
			
			/**
			 * 连续单字词的个数最大等于2的使用bigram，大于2的使用trigram
			 */
			int maxLength = maxContinueSingleWordsLength(locations);
			if(maxLength == 2) {
				correctSentence = bigramDP(sentence, candidateCharacters);
			}else {
				correctSentence = trigramDP(sentence, candidateCharacters);
			}			
			
			return correctSentence;
		}
		
		return sentence;
	}

	/**
	 * 根据单字词初始化并建立候选字数组
	 * @param sentence	句子
	 */
	private String[][] initCandidateCharacters(Sentence sentence) {
		String[][] candidateCharacters = new String[sentence.size()][];
		
		for(int i = 0; i < sentence.size(); i++) {
			String character = sentence.getToken(i);
			HashSet<String> set = confusionSet.getConfusions(character);
			
			if(set != null) 
				candidateCharacters[i] = set.toArray(new String[set.size()]);
			else
				candidateCharacters[i] = null;
		}
		
		return candidateCharacters;
	}
	
	/**
	 * 返回连续的单字词的最大长度
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
			if(word.length() == 1) 
				locations.add(index);
			
			index += word.length();
		}
		
		return locations;
	}
	
	/**
	 * 二元动态规划
	 */
	private Sentence bigramDP(Sentence sentence, String[][] candidateCharacters) {
		String c0 = sentence.getToken(0);
		double[][][] dp = null;

		//初始化得分数组
		for(int i = 0; i < candidateCharacters[0].length; i++) {		//遍历句子第一个字个所有候选字
			for(int j = 0; j < candidateCharacters[1].length; j++) {	//遍历句子第二个字个所有候选字
				if(candidateCharacters[0][i] == c0)
					dp[1][i][j] = INIT_Parameter;
				else
					dp[1][i][j] = 1.0;
			}
		}
			
		//动态规划计算最佳得分
		for(int i = 2; i < sentence.size(); i++) {	//遍历句子的每一个字
			for(int j = 0; j < candidateCharacters[i - 2].length; j++) {
				for(int k = 0; k < candidateCharacters[i - 1].length; k++) {
					for(int l = 0; l < candidateCharacters[i].length; l++) {
						double score = getLogScore(candidateCharacters[i - 2][k] + candidateCharacters[i - 1][j] + candidateCharacters[i][l]);
						dp[i][k][l] = Math.max(dp[i][k][l], dp[i-1][j][k] * score);
					}//end for(l)
				}//end for(k)
			}//end for(j)
		}//end for(i)
		
		return null;
	}
	
	/**
	 * 三元动态规划
	 */
	private Sentence trigramDP(Sentence sentence, String[][] candidateCharacters) {
		double[][][] dp = null;
		
		String c0 = sentence.getToken(0);
		String c1 = sentence.getToken(1);
		
		//初始化得分数组
		for(int i = 0; i < candidateCharacters[0].length; i++) {		//遍历句子第一个字个所有候选字
			for(int j = 0; j < candidateCharacters[1].length; j++) {	//遍历句子第二个字个所有候选字
				if(candidateCharacters[0][i] == c0)
					dp[1][i][j] = INIT_Parameter;
				else
					dp[1][i][j] = 1.0;
				
				if(candidateCharacters[1][j] == c1)
					dp[1][i][j] = dp[1][i][j] * INIT_Parameter;
			}
		}
			
		//动态规划计算最佳得分
		for(int i = 2; i < sentence.size(); i++) {	//遍历句子的每一个字
			for(int j = 0; j < candidateCharacters[i - 2].length; j++) {
				for(int k = 0; k < candidateCharacters[i - 1].length; k++) {
					for(int l = 0; l < candidateCharacters[i].length; l++) {
						double score = getLogScore(candidateCharacters[i - 2][k] + candidateCharacters[i - 1][j] + candidateCharacters[i][l]);
						dp[i][k][l] = Math.max(dp[i][k][l], dp[i-1][j][k] * score);
					}//end for(l)
				}//end for(k)
			}//end for(j)
		}//end for(i)
		
		return null;
	}
	
	@Override
	public double getChannelModelLogScore(Sentence sentence) {
		return 1.0;
	}
	
	/**
	 * 返回给定n元的概率
	 * @param strings	待返回概率的n元
	 * @return			n元的概率
	 */
	private double getLogScore(String... strings) {
		return nGramModel.getNGramLogProb(strings);
	}
}
