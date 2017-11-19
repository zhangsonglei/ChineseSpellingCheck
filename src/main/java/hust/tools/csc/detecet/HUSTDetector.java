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
		DetectResult result = new DetectResult();
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
					
					if(errors.size() != 0)
						result.add(errors.toArray(new SpellError[errors.size()]));
					else
						result.add(null);
				}//end if
			}//end for
		}//end if
		
		return result;
	}
}