package hust.tools.csc.score;

import java.io.IOException;

import hust.tools.csc.ngram.HustNGramModel;
import hust.tools.csc.ngram.NGramModel;
import hust.tools.csc.util.ConfusionSet;
import hust.tools.csc.util.Dictionary;
import hust.tools.csc.util.Sentence;
import hust.tools.ngram.utils.GramSentenceStream;

/**
 *<ul>
 *<li>Description: 由SCAU提出的利用n元模型为句子打分 
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月18日
 *</ul>
 */
public class HustNoisyChannelModel extends AbstractNoisyChannelModel {
	private ConfusionSet confusionSet;
	private double INIT_Parameter = 35;
	private Dictionary dictionary;
	private double[][][] dp;
	private String[][] V;
	private String[] trigrams;
	
	public HustNoisyChannelModel(NGramModel nGramModel, ConfusionSet confusionSet, Dictionary dictionary) throws IOException {
		this.nGramModel = nGramModel;
		this.confusionSet = confusionSet;
		this.dictionary = dictionary;
	}
	
	@Override
	public Sentence getCorrectSentence(Sentence sentence) {
		dp = null;
		V = null;
		trigrams = null;
		Sentence candidate = null;
		
		trigramDP(sentence);
		getNoisyChannelModelLogScore(candidate);
		return candidate;
	}
	
	/**
	 * 返回给定候选句子的Noisy Channel　Model概率
	 * @param candidate	原始句子的候选句子
	 * @return			Noisy Channel　Model概率
	 */
	private double getNoisyChannelModelLogScore(Sentence candidate) {
		return getChannelModelLogScore(candidate)*getSourceModelLogScore(candidate);
	}
	


	@Override
	public double getChannelModelLogScore(Sentence sentence) {
		return 1;
	}
	
	/**
	 * 返回给定n元的概率
	 * @param strings	待返回概率的n元
	 * @return			n元的概率
	 */
	private double getLogScore(String... strings) {
		return nGramModel.getNGramLogProb(strings);
	}
	
	/**
	 * 动态规划
	 */
	private void trigramDP(Sentence sentence) {
			String c0 = sentence.getToken(0);
			String c1 = sentence.getToken(1);

			//初始化得分数组
			for(int i = 0; i < V[0].length; i++) {		//遍历句子第一个字个所有候选字
				for(int j = 0; j < V[1].length; j++) {	//遍历句子第二个字个所有候选字
					if(V[0][i] == c0) 
						dp[1][i][j] = INIT_Parameter;
					else
						dp[1][i][j] = 1.0;
					
					if(V[1][j] == c1)
						dp[1][i][j] = dp[1][i][j] * INIT_Parameter;
				}
			}
			
			//动态规划计算最佳得分
			for(int i = 2; i < sentence.size(); i++) {	//遍历句子的每一个字
				for(int j = 0; j < V[i - 2].length; j++) {
					for(int k = 0; k < V[i - 1].length; k++) {
						for(int l = 0; l < V[i].length; l++) {
							double score = getLogScore(V[i - 2][k] + V[i - 1][j] + V[i][l]);
							dp[i][k][l] = Math.max(dp[i][k][l], dp[i-1][j][k] * score);
						}//end for(l)
					}//end for(k)
				}//end for(j)
			}//end for(i)
	}
}
