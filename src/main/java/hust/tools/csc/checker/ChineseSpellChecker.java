package hust.tools.csc.checker;

import java.util.ArrayList;
import hust.tools.csc.score.NoisyChannelModel;
import hust.tools.csc.util.Sentence;

/**
 *<ul>
 *<li>Description: 中文拼写纠正模型 
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年11月21日
 *</ul>
 */
public class ChineseSpellChecker {
	
	private NoisyChannelModel noisyChannelModel;
	
	public ChineseSpellChecker(NoisyChannelModel noisyChannelModel) {
		this.noisyChannelModel = noisyChannelModel;
	}
	
	public CheckResult check(Sentence sentence) {
		ArrayList<Sentence> candidates = noisyChannelModel.getCorrectSentence(sentence);

		return new CheckResult(sentence, candidates);
	}
	
	/**
	 * 系统自动最佳候选纠正句
	 * @return	最佳纠正句
	 */
	public Sentence correct(Sentence sentence){
		return noisyChannelModel.getCorrectSentence(sentence).get(0);
	}
}
