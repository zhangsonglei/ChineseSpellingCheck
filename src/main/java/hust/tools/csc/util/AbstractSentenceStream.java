package hust.tools.csc.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 *<ul>
 *<li>Description: 从训练语料中流式读取sentence的抽象类 
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月11日
 *</ul>
 * @param <T>
 */
public abstract class AbstractSentenceStream implements SenteceStream {
	
	private BufferedReader bufferedReader;
	
	/**
	 * 每次读取的行数
	 */
	private final int  slide_length = 1000;

	/**
	 * 流式读取的迭代器
	 */
	private Iterator<Sentence> sentences = Collections.<Sentence>emptyList().iterator();

	/**
	 * 将训练语料解析成句子序列的抽象方法
	 * @param lines 从训练语料中读取的行
	 * @return 解析后的句子序列
	 */
	protected abstract Iterator<Sentence> createSentences(List<String> lines);

	public AbstractSentenceStream(String pathname, String encoding) throws FileNotFoundException, UnsupportedEncodingException {
		File file = new File(pathname);
		InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), encoding);
		this.bufferedReader = new BufferedReader(inputStreamReader);
	}

	public AbstractSentenceStream(File file, String encoding) throws FileNotFoundException, UnsupportedEncodingException {
		InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), encoding);
		this.bufferedReader = new BufferedReader(inputStreamReader);
	}

	/**
	 * 从流中读取下一个句子，重复调用该方法直到返回null，每次从底层源中返回一个句子
	 * @return 下一个句子
	 * @throws IOException 如果读取过程中出错，抛出异常
	 */
	public final Sentence next() throws IOException {

		if (sentences.hasNext()) {
			return sentences.next();
		}else {
			List<String> lines = new ArrayList<>();
			String line = null;
			int size = 0;
			while (!sentences.hasNext() && (line = bufferedReader.readLine()) != null) {
				line  = line.trim();
				if(!line.equals(""))
					lines.add(line);
			
				if(++size == slide_length)//每次读取1000行
					sentences = createSentences(lines);
			}
			
			//语料中剩余的小于slide_length的所有行
			if(size != 0)
				sentences = createSentences(lines);

			if (sentences.hasNext()) {
				return next();
			}
		}

		return null;
	}
	
	@Override
	public void reset() throws IOException, UnsupportedOperationException {
		sentences = Collections.emptyIterator();
		bufferedReader.reset();
	}

	@Override
	public void close() throws IOException {
		bufferedReader.close();
	}

}
