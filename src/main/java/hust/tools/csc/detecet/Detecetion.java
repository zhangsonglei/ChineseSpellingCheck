package hust.tools.csc.detecet;

import hust.tools.csc.util.Sentence;

public interface Detecetion {
	
	/**
	 * 获取所有检测出错的位置
	 * @return 所有检测出错的位置	
	 */
	int[] getErrorLocations(Sentence sentence);
}