package hust.tools.csc.detecet;

/**
 *<ul>
 *<li>Description: 句子中的一个拼写错误信息的类 
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月20日
 *</ul>
 */
public class SpellError {

	/**
	 * 拼写错误的字
	 */
	private String character;

	/**
	 * 拼写错误的类
	 */
	private int location;

	public SpellError(String character, int location) {
		this.character = character;
		this.location = location;
	}
	
	public int getLocation() {
		return location;
	}

	public String getCharacter() {
		return character;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((character == null) ? 0 : character.hashCode());
		result = prime * result + location;
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
		SpellError other = (SpellError) obj;
		if (character == null) {
			if (other.character != null)
				return false;
		} else if (!character.equals(other.character))
			return false;
		if (location != other.location)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return location + "[" + character + "]";
	}
}
