package hust.tools.csc.correct;

import java.util.HashSet;
import java.util.Iterator;

import hust.tools.csc.detecet.Detector;
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
	protected Sentence sentence;
	private NoisyChannelModel noisyChannelModel;
	private Detector detector;
	
	public HUSTCorrector(ConfusionSet confusionSet, Dictionary dictionary, Detector detector, Sentence sentence) {
		this.confusionSet = confusionSet;
		this.dictionary = dictionary;
		this.sentence = sentence;
		this.detector = detector;
		this.noisyChannelModel = null;
	}
	
	public HUSTCorrector(NoisyChannelModel noisyChannelModel, Sentence sentence) {
		this.noisyChannelModel = noisyChannelModel;
		this.sentence = sentence;
		this.confusionSet = null;
		this.dictionary = null;
	}
	
	@Override
	public SuggestionQueue[] getSuggestions() {
		int[] locations = detector.getErrorLocation();
		String[] characters = detector.getErrorCharacter();
		
		SuggestionQueue[] res = new SuggestionQueue[locations.length];
		
		for(int i = 0; i < locations.length; i++) {
			HashSet<String> set = confusionSet.getConfusions(characters[i]);
			Iterator<String> iterator = set.iterator();
			
			Suggestion[] suggestions = new Suggestion[set.size()];
			for(int j = 0; j < set.size(); j++) {
				String character = iterator.next();
				suggestions[j] = new Suggestion(character, dictionary.getCount(character), locations[i]);
			}
			
			res[i] =  new SuggestionQueue(suggestions);
		}
		
		return res;
	}
	
	@Override
	public Sentence correct() {
		return noisyChannelModel.getCorrectSentence(sentence);
	}
}
