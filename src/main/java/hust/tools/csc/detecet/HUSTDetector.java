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
		ArrayList<SpellError[]> errorList = new ArrayList<>();
		
		ArrayList<Sentence> corrects = noisyChannelModel.getCorrectSentence(sentence);
		if(sentence != null && corrects!= null) {
			for(Sentence correct : corrects) {
				if(sentence.size() == correct.size()) {
					ArrayList<SpellError> errors = new ArrayList<>();
					for(int i = 0; i < sentence.size(); i++) {
						String character = correct.getToken(i);
						if(!sentence.getToken(i).equals(character)) 
							errors.add(new SpellError(character, i));
					}//end for
					
					errorList.add(errors.toArray(new SpellError[errors.size()]));
				}
			}
		}
		
		return new DetectResult(errorList);
	}
	
	/**
	 * 返回所有检测出的错误的字
	 * @return	所有检测出的错误的字
	 */
	public String[] getErrorCharacter(DetectResult result, int n) {
		int size = result.getErrors(n).length;
		String[] errorCharacters = new String[size];
		
		for(int i = 0; i < size; i++)
			errorCharacters[i] = result.getErrors(n)[i].getCharacter();
		
		return errorCharacters;
	}
	
	/**
	 * 返回所有检测出的错误字的位置
	 * @return	所有检测出的错误字的位置
	 */
	public int[] getErrorLocation(DetectResult result, int n) {
		int size = result.getErrors(n).length;
		int[] errorLoactions = new int[size];
		
		for(int i = 0; i < size; i++)
			errorLoactions[i] = result.getErrors(n)[i].getLocation();
		
		return errorLoactions;		
	}

	@Override
	public String[][] getErrorCharacter(DetectResult result) {
		String[][] res = new String[result.errorCounts()][];
		
		for(int i = 0; i < res.length; i++)
			res[i] = getErrorCharacter(result, i);
		
		return res;
	}

	@Override
	public int[][] getErrorLocation(DetectResult result) {
		int[][] res = new int[result.errorCounts()][];
		
		for(int i = 0; i < res.length; i++)
			res[i] = getErrorLocation(result, i);
		
		return res;
	}
}
