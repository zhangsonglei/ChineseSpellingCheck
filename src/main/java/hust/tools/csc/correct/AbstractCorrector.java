package hust.tools.csc.correct;

import java.util.List;

import hust.tools.csc.detecet.SpellError;
import hust.tools.csc.util.Sentence;

public abstract class AbstractCorrector implements Corrector {

	protected Sentence sentence;
		
	public AbstractCorrector(Sentence sentence) {
		this.sentence = sentence;
	}
	
	@Override
	public List<String> getSuggestions(SpellError error) {
		return null;
	}
	
	@Override
	public Sentence correct() {
		return null;
	}
}
