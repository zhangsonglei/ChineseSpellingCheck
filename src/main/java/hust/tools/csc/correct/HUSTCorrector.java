package hust.tools.csc.correct;

import java.util.HashMap;
import hust.tools.csc.detecet.DetectResult;
import hust.tools.csc.detecet.Detector;
import hust.tools.csc.detecet.SpellError;
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

	private Detector detector;
	
	public HUSTCorrector(Detector detector) {
		this.detector = detector;
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
		DetectResult result = detector.detect(sentence);
		String[] characters = detector.getErrorCharacter(result, 0);
		int[] locations = detector.getErrorLocation(result, 0);
		
		for(int i=0; i < locations.length; i++) 
			sentence = sentence.setToken(locations[i], characters[i]);
		
		return sentence;
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
