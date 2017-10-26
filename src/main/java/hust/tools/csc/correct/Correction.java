package hust.tools.csc.correct;

/**
 *<ul>
 *<li>Description: 纠正后的字
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月24日
 *</ul>
 */
public class Correction implements Comparable<Correction> {
	
	/**
	 * 纠正后的字
	 */
	private String character;
	
	/**
	 * 该字的得分,优先度
	 */
	private double score;
	
	/**
	 * 该字在句子中的位置索引
	 */
	private int location;
	
	public Correction(String character, int score, int location) {
		this.character = character;
		this.score = score;
		this.location = location;
	}

	public String getCharacter() {
		return character;
	}

	public void setCharacter(String character) {
		this.character = character;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public int getLocation() {
		return location;
	}

	public void setLocation(int location) {
		this.location = location;
	}

	@Override
	public int compareTo(Correction o) {
		if(this.score != o.score)
			return this.score - o.score > 0 ? 1 : -1;
		
		return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((character == null) ? 0 : character.hashCode());
		result = prime * result + location;
		long temp;
		temp = Double.doubleToLongBits(score);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Correction other = (Correction) obj;
		if (character == null) {
			if (other.character != null)
				return false;
		} else if (!character.equals(other.character))
			return false;
		if (location != other.location)
			return false;
		if (Double.doubleToLongBits(score) != Double.doubleToLongBits(other.score))
			return false;
		return true;
	}
}
