package hust.tools.scs.score;

import hust.tools.csc.util.ConfusionSet;
import hust.tools.csc.util.Sentence;

/**
 *<ul>
 *<li>Description: 由SCAU提出的利用n元模型为句子打分 
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月18日
 *</ul>
 */
public class SCAUScoreSentence implements ScoreSentence {
	private ConfusionSet confusionSet;
	private NGramModel nGramModel;
	private double INIT_Parameter = 0.5;
	double[][][] dp;
	String[][] V;
	String[] trigrams;
	
	public SCAUScoreSentence(NGramModel nGramModel, ConfusionSet confusionSet) {
		this.nGramModel = nGramModel;
		this.confusionSet = confusionSet;
		dp = null;
		V = null;
		trigrams = null;
	}
	
	@Override
	public double score(Sentence sentence) {
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
							dp[i][k][l] = Math.max(dp[i][k][l], dp[i-1][j][k] * score);
						}
					}
				}
			}
		}
	}

	private double getScore(String... strings) {
		return nGramModel.getProb(strings, strings.length);
	}
}
