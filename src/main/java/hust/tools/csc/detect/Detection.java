package hust.tools.csc.detect;

/**
 *<ul>
 *<li>Description: 检测结果，从一条passage中检测出其中是否有错误，并给出错误的位置
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月10日
 *</ul>
 */
public class Detection {
	
	private int[] locations;
	
	public Detection(int[] locations) {
		this.locations = locations;
	}

	public int[] getLocations() {
		return locations;
	}

	public void setLocations(int[] locations) {
		this.locations = locations;
	}
}
