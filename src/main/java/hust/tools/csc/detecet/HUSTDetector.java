package hust.tools.csc.detecet;

import java.util.ArrayList;

import hust.tools.csc.score.NoisyChannelModel;
import hust.tools.csc.util.Sentence;

public class HUSTDetector implements Detector{
	
	private NoisyChannelModel noisyChannelModel;
	
	public HUSTDetector(NoisyChannelModel noisyChannelModel) {
		this.noisyChannelModel = noisyChannelModel;
	}
	
	public DetectResult detect(Sentence sentence) {
		ArrayList<SpellError> errorList = new ArrayList<>();
		
		Sentence correct = noisyChannelModel.getCorrectSentence(sentence);
		if(sentence != null && correct!= null) {
			if(sentence.size() == correct.size()) {
				for(int i = 0; i < sentence.size(); i++) {
					String character = correct.getToken(i);
					if(!sentence.getToken(i).equals(character)) 
						errorList.add(new SpellError(character, i));
				}//end for
			}
		}
		
		return new DetectResult(errorList);
	}
	
	/**
	 * 返回所有检测出的错误的字
	 * @return	所有检测出的错误的字
	 */
	public String[] getErrorCharacter(DetectResult result) {
		int size = result.errorCounts();
		String[] errorCharacters = new String[size];
		
		for(int i = 0; i < size; i++)
			errorCharacters[i] = result.getErrors().get(i).getCharacter();
		
		return errorCharacters;
	}
	
	/**
	 * 返回所有检测出的错误字的位置
	 * @return	所有检测出的错误字的位置
	 */
	public int[] getErrorLocation(DetectResult result) {
		int size = result.errorCounts();
		int[] errorLoactions = new int[size];
		
		for(int i = 0; i < size; i++)
			errorLoactions[i] = result.getErrors().get(i).getLocation();
		
		return errorLoactions;		
	}
}
