package hust.tools.csc.correct;

import java.util.List;

import hust.tools.csc.util.Sentence;
import hust.tools.csc.util.Word;

public abstract class AbstractCorrector implements Corrector {

	protected Sentence sentence;
		
	public AbstractCorrector(Sentence sentence) {
		this.sentence = sentence;
	}
	
	@Override
	public List<Word> getSuggestions(Word word) {
		return null;
	}
	
	@Override
	public Sentence correct() {
		return null;
	}
}
