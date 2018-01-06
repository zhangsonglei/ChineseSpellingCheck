package hust.tools.csc.correct;

import java.util.ArrayList;

import hust.tools.csc.model.NoisyChannelModel;
import hust.tools.csc.util.Sentence;

public class ChineseSpellErrorCorrector implements Corrector {

	private NoisyChannelModel correctModel;
	
	public ChineseSpellErrorCorrector(NoisyChannelModel correctModel) {
		this.correctModel = correctModel;
	}
	
	@Override
	public CorrectResult correct(Sentence sentence) {
		return correct(sentence, 1);
	}
	
	@Override
	public CorrectResult correct(Sentence sentence, int k) {
		CorrectResult result = new CorrectResult();
		ArrayList<Sentence> candSentences = correctModel.getBestKSentence(sentence, k);
		
		if(candSentences != null) {
			for(Sentence candSentence : candSentences) {
				int len = candSentence.size();
				if(len != sentence.size() || candSentence.equals(sentence))
					continue;
				
				ArrayList<Correction> corrections = new ArrayList<>();
				for(int i = 0; i < len; i++) {
					if(candSentence.getToken(i).equals(sentence.getToken(i)))
						corrections.add(new Correction(candSentence.getToken(i), i));
				}
				
				if(corrections.size() != 0)
					result.add(corrections.toArray(new Correction[corrections.size()]));
				else
					result.add(null);
			}
		}
		
		return result;
	}
}
