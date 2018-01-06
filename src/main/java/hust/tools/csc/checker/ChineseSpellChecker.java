package hust.tools.csc.checker;

import java.util.ArrayList;

import hust.tools.csc.correct.ChineseSpellErrorCorrector;
import hust.tools.csc.correct.CorrectResult;
import hust.tools.csc.detecet.ChinsesSpellErrorDetector;
import hust.tools.csc.detecet.DetectResult;
import hust.tools.csc.model.NoisyChannelModel;
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
	
	private ChinsesSpellErrorDetector detector;
	
	private ChineseSpellErrorCorrector corrector;
	
	public ChineseSpellChecker(NoisyChannelModel noisyChannelModel) {
		this.noisyChannelModel = noisyChannelModel;
		detector = new ChinsesSpellErrorDetector(noisyChannelModel);
		corrector = new ChineseSpellErrorCorrector(noisyChannelModel);
	}
	
	/**
	 * 系统自动最佳候选纠正句
	 * @return	最佳纠正句
	 */
	public Sentence bestSentence(Sentence sentence){
		return noisyChannelModel.getBestSentence(sentence);
	}
	
	public Sentence[] bestKSentences(Sentence sentence, int k) {
		ArrayList<Sentence> cands = noisyChannelModel.getBestKSentence(sentence, k);
		return cands.toArray(new Sentence[cands.size()]);
	}
	
	public DetectResult detect(Sentence sentence) {
		return detect(sentence, 1);
	}
	
	public DetectResult detect(Sentence sentence, int k) {
		return detector.detect(sentence, k);
	}
	
	public CorrectResult correct(Sentence sentence) {
		return correct(sentence, 1);
	}
	
	public CorrectResult correct(Sentence sentence, int k) {
		return corrector.correct(sentence, 1);
	}
}
