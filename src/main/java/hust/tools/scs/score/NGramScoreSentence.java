package hust.tools.scs.score;

import hust.tools.csc.util.Sentence;

/**
 *<ul>
 *<li>Description: 利用n元模型为句子打分 
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月18日
 *</ul>
 */
public class NGramScoreSentence implements ScoreSentence {
	private Sentence sentence;
	private double INIT_Parameter = 0.5; 
	double[][][] dp;
	String[][] V;
	String[] trigrams;
	
	public NGramScoreSentence(Sentence sentence) {
		this.sentence = sentence;
		dp = null;
		V = null;
		trigrams = null;
	}

	@Override
	public double score() {
		trigramDP();
		return 0;
	}
	
	
	private void trigramDP() {
		for(String string : trigrams) {
			String c0 = null;
			String c1 = null;
			for(int k = 0; k < V[0].length; k++) {
				for(int l = 0; l < V[1].length; l++) {
					if(V[0][k] == c0) 
						dp[1][k][l] = INIT_Parameter;
					else
						dp[1][k][l] = 1.0;
					
					if(V[1][l] == c1)
						dp[1][k][l] = dp[1][k][l] * INIT_Parameter; 		
				}
			}
			
	////////////////////		
			
			for(int i = 2; i < string.length(); i++) {
				for(int j = 0; j < V[i - 2].length; j++) {
					for(int k = 0; k < V[i - 1].length; k++) {
						for(int l = 0; l < V[i].length; l++) {
							double score = getScore(V[i - 2][k] + V[i - 1][j] + V[i][l]);
							dp[i][k][l] = Math.max(dp[i][k][l], dp[i][k][l] * score);
						}
					}
				}
			}
		}
	}

	private double getScore(String... strings) {
		return 0;
	}	
}
