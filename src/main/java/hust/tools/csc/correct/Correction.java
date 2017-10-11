package hust.tools.csc.correct;

/**
 *<ul>
 *<li>Description: 针对一条detection中标记的错误，给出一个或多个可能的纠正结果 
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月10日
 *</ul>
 */
public class Correction {
	
	private String[] candidates;
	
	public Correction(String[] candiates) {
		this.candidates = candiates;
	}

	public String[] getCandidates() {
		return candidates;
	}

	public void setCandidates(String[] candidates) {
		this.candidates = candidates;
	}
}
