package hust.tools.csc.detecet;

import hust.tools.csc.util.Sentence;

/**
 *<ul>
 *<li>Description: 由SCAU提出的基于n元模型的中文拼写检测器 
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月18日
 *</ul>
 */
public class SCAUDetector extends AbstractDetector {

	public SCAUDetector(Sentence sentence) {
		super(sentence);
	}

	@Override
	public int getErrorWordCounts(Sentence sentence) {
		return 0;
	}

	@Override
	public int[] getErrorWordLocations(Sentence sentence) {
		return null;
	}

	@Override
	public String[] getErrorWords(Sentence sentence) {
		return null;
	}

	@Override
	public void sort() {
		
	}
}
