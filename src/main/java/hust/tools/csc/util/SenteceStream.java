package hust.tools.csc.util;

import java.io.IOException;

/**
 *<ul>
 *<li>Description: 拼写纠错的输入，表示一行待检查的句子 (根据标点和非汉字分割后的句子) 
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月10日
 *</ul>
 * @param <T>
 */
public interface SenteceStream {
	
	/**
	 * <li>返回下一个句子。
	 * <li>重复调用该方法每次将从底层源中返回一个句子，直到返回null。  
	 * @return 下一个句子或者null（读取结束）
	 * @throws IOException
	 */
	Sentence next() throws IOException;

	/**
	 * <li>在开始处重置流，之前读取的句子序列将会完全重新读取。 
	 * <li>该方法的实现是可选的。
	 *
	 * @throws IOException 如果重置流的过程中出错抛出异常
	 */
	void reset() throws IOException, UnsupportedOperationException;

	/**
	 * <li>关闭SenteceStream并释放所有资源。
	 * <li>调用close后，将无法再调用read和reset。
	 * @throws IOException 如果关闭流的过程中出错抛出异常
	 */
	void close() throws IOException;
}
