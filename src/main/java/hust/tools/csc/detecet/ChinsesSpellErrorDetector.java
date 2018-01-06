package hust.tools.csc.detecet;

import java.util.ArrayList;

import hust.tools.csc.model.NoisyChannelModel;
import hust.tools.csc.util.Sentence;

public class ChinsesSpellErrorDetector implements Detector {

	private NoisyChannelModel detectModel;
	
	public ChinsesSpellErrorDetector(NoisyChannelModel detectModel) {
		this.detectModel = detectModel;
	}
	
	@Override
	public DetectResult detect(Sentence sentence) {
		return detect(sentence, 1);
	}
	
	@Override
	public DetectResult detect(Sentence sentence, int k) {
		ArrayList<SpellError[]> errorList = new ArrayList<>();
		ArrayList<Sentence> candSentences = detectModel.getBestKSentence(sentence, k);
		
		if(candSentences != null) {
			for(Sentence candSentence : candSentences) {
				int len = candSentence.size();
				if(len != sentence.size() || candSentence.equals(sentence))
					continue;
				
				ArrayList<SpellError> errors = new ArrayList<>();
				for(int i = 0; i < len; i++) {
					String character = candSentence.getToken(i);
					if(!sentence.getToken(i).equals(character)) 
						errors.add(new SpellError(character, i));
				}
				
				if(errors.size() != 0)
					errorList.add(errors.toArray(new SpellError[errors.size()]));
				else
					errorList.add(null);
			}//end for
		}
		
		return new DetectResult(errorList);
	}
}
