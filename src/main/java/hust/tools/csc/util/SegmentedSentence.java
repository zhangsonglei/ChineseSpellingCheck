package hust.tools.csc.util;

public class SegmentedSentence {
	
	private Word[] sentence;
	
	public SegmentedSentence(Word[] sentence) {
		this.sentence = sentence;
	}

	public Word[] getSentence() {
		return sentence;
	}

	public void setSentence(Word[] sentence) {
		this.sentence = sentence;
	}
}
