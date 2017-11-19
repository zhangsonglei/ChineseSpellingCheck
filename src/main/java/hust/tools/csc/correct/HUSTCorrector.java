package hust.tools.csc.correct;

import java.util.ArrayList;
import hust.tools.csc.score.NoisyChannelModel;
import hust.tools.csc.util.Sentence;

/**
 *<ul>
 *<li>Description: 纠正器抽象类 
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月24日
 *</ul>
 */
public class HUSTCorrector implements Corrector {

	private NoisyChannelModel noisyChannelModel;
	
	public HUSTCorrector(NoisyChannelModel noisyChannelModel) {
		this.noisyChannelModel = noisyChannelModel;
	}
	
	@Override
	public CorrectResult correct(Sentence sentence) {
		CorrectResult result = new CorrectResult();
		ArrayList<Sentence> candSentences = noisyChannelModel.getCorrectSentence(sentence);
		if(candSentences != null) {
			for(Sentence candSentence : candSentences) {
				int len = candSentence.size();
				if(len != sentence.size())
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
	
	@Override
	public Sentence autoCorrect(Sentence sentence) {
		CorrectResult result = correct(sentence);
		Correction[] corrections = result.getSuggestions(0);
		
		for(Correction correction :corrections) { 
			int location = correction.getLocation();
			String character = correction.getCharacter();
			sentence = sentence.setToken(location, character);
		}
		
		return sentence;
	}
}
