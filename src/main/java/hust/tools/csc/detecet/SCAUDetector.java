package hust.tools.csc.detecet;

import java.util.ArrayList;

import hust.tools.csc.score.NoisyChannelModel;
import hust.tools.csc.util.Sentence;
import hust.tools.csc.wordseg.AbstractWordSegment;
import hust.tools.csc.wordseg.CKIPWordSegment;

/**
 *<ul>
 *<li>Description: 由SCAU提出的基于n元模型的中文拼写检测器 
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月18日
 *</ul>
 */
public class SCAUDetector implements Detector {
	
	private NoisyChannelModel noisyChannelModel;
	private Sentence sentence;
	public SCAUDetector(Sentence sentence) {
		this.sentence = sentence;
	}

	@Override
	public DetectResult detect() {
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

	@Override
	public String[] getErrorCharacter() {
		DetectResult result = detect();
		int size = result.errorCounts();
		String[] errorCharacters = new String[size];
		
		for(int i = 0; i < size; i++)
			errorCharacters[i] = result.getErrors().get(i).getCharacter();
		
		return errorCharacters;
	}

	@Override
	public int[] getErrorLocation() {
		DetectResult result = detect();
		int size = result.errorCounts();
		int[] errorLoactions = new int[size];
		
		for(int i = 0; i < size; i++)
			errorLoactions[i] = result.getErrors().get(i).getLocation();
		
		return errorLoactions;
	}
	
	/**
	 * 返回句子分词后的词串
	 * @param sentence	待分词的句子
	 * @return			分词后的词串
	 */
	public String[] wordSegment(Sentence sentence) {
		if(sentence != null && sentence.size() > 0) {
			AbstractWordSegment wordSegment = new CKIPWordSegment(sentence);
			return wordSegment.segment();
		}
		
		return null;
	}
}
