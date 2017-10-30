package hust.tools.csc.score;

import java.util.ArrayList;

import hust.tools.csc.util.Sentence;

/**
 *<ul>
 *<li>Description: 噪音通道模型接口
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月18日
 *</ul>
 */
public interface NoisyChannelModel {

	/**
	 * 返回给定原始句子的正确句子
	 * @param sentence	原始句子	
	 * @return			原始句子的正确结果
	 */
	public ArrayList<Sentence> getCorrectSentence(Sentence sentence);
}
