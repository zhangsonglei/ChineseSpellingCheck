package hust.tools.csc.correct;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import hust.tools.csc.detecet.DetectResult;
import hust.tools.csc.detecet.HUSTDetector;
import hust.tools.csc.detecet.SpellError;
import hust.tools.csc.score.NoisyChannelModel;
import hust.tools.csc.util.ConfusionSet;
import hust.tools.csc.util.Dictionary;
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

	private Dictionary dictionary;
	private ConfusionSet confusionSet;
	private NoisyChannelModel noisyChannelModel;
	private HUSTDetector detector;
	
	public HUSTCorrector(ConfusionSet confusionSet, Dictionary dictionary, HUSTDetector detector) {
		this.confusionSet = confusionSet;
		this.dictionary = dictionary;
		this.detector = detector;
		this.noisyChannelModel = null;
	}
	
	public HUSTCorrector(NoisyChannelModel noisyChannelModel) {
		this.noisyChannelModel = noisyChannelModel;
		this.confusionSet = null;
		this.dictionary = null;
	}
	
	@Override
	public CorrectResult correct(Sentence sentence) {
		DetectResult result = detector.detect(sentence);
		int[][] locations = detector.getErrorLocation(result);
		String[][] characters = detector.getErrorCharacter(result);
		
		HashMap<Integer, Suggestions> map = new HashMap<>();
//		
//		for(int i = 0; i < locations.length; i++) {
//			HashSet<String> set = confusionSet.getConfusions(characters[i]);
//			Iterator<String> iterator = set.iterator();
//			
//			Suggestions suggestions = new Suggestions();
//			for(int j = 0; j < set.size(); j++) {
//				String character = iterator.next();
//				suggestions.add(new Correction(character, dictionary.getCount(character), locations[i]));
//			}
//			
//			map.put(locations[i], suggestions);
//		}
		
		return new CorrectResult(map);
	}
	
	@Override
	public Sentence autoCorrect(Sentence sentence) {
		return noisyChannelModel.getCorrectSentence(sentence).get(0);
	}

	@Override
	public String[][] getSuggestions() {
		return null;
	}

	@Override
	public String[] getSuggestions(SpellError spellError) {
		return null;
	}
}
