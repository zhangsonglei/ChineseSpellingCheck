package hust.tools.csc.detecet;

import java.util.ArrayList;

import hust.tools.csc.score.NoisyChannelModel;
import hust.tools.csc.util.Sentence;

/**
 *<ul>
 *<li>Description: 拼写错误检测器 
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年11月17日
 *</ul>
 */
public class HUSTDetector implements Detector{
	
	private NoisyChannelModel noisyChannelModel;
	
	public HUSTDetector(NoisyChannelModel noisyChannelModel) {
		this.noisyChannelModel = noisyChannelModel;
	}

	@Override
	public DetectResult detect(Sentence sentence) {
		ArrayList<SpellError[]> errorList = new ArrayList<>();
		ArrayList<Sentence> candSentences = noisyChannelModel.getCorrectSentence(sentence);
		
		//将提取候选句子中进行了修改的字与其在句子中的位置
		if(sentence != null && candSentences!= null) {
			for(Sentence cand : candSentences) {
				if(sentence.size() == cand.size()) {
					ArrayList<SpellError> errors = new ArrayList<>();
					for(int i = 0; i < sentence.size(); i++) {
						String character = cand.getToken(i);
						if(!sentence.getToken(i).equals(character)) 
							errors.add(new SpellError(character, i));
					}//end for
					
					errorList.add(errors.toArray(new SpellError[errors.size()]));
				}//end if
			}//end for
		}//end if
		
		return new DetectResult(errorList);
	}
	
	
	@Override
	public int[] getErrorLocation(Sentence sentence, int n) {
		SpellError[] errors = detect(sentence).getErrors(n);
		if(errors == null)
			return null;

		int size = errors.length;
		int[] errorLoactions = new int[size];
		
		for(int i = 0; i < size; i++)
			errorLoactions[i] = errors[i].getLocation();
		
		return errorLoactions;		
	}

	@Override
	public int[][] getErrorLocation(Sentence sentence) {
		DetectResult result = detect(sentence);
		int[][] res = new int[result.candiateCounts()][];
		
		for(int i = 0; i < res.length; i++) {
			SpellError[] errors = result.getErrors(i);
			if(errors == null)
				return null;

			int size = errors.length;
			int[] errorLoactions = new int[size];
			
			for(int j = 0; j < size; j++)
				errorLoactions[i] = errors[i].getLocation();
			
			res[i] = errorLoactions;
		}
		return res;
	}
}