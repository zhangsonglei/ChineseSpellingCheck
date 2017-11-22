package hust.tools.csc.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hust.tools.csc.detecet.SpellError;
import hust.tools.csc.ngram.HustNGramModel;
import hust.tools.csc.ngram.NGramModel;
import hust.tools.ngram.io.ARPATextFileNGramModleReader;
import hust.tools.ngram.io.BinaryFileNGramModelReader;
import hust.tools.ngram.io.TextFileNGramModelReader;
import hust.tools.ngram.model.AbstractNGramModelReader;

/**
 *<ul>
 *<li>Description: 读取SIGHAN2015拼写纠错测试语料并存入内存 
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年11月5日
 *</ul>
 */
public class FileOperator {
	
	/**
	 * 读取SIGHAN2015拼写纠错测试语料（SIGHAN15_CSC_TestInput.txt）
	 * @param path		语料路径
	 * @param encoding	语料格式
	 * @return			句子pid与句子内容的映射
	 * @throws IOException
	 */
	public static HashMap<String, Sentence> readTestFile(String path, String encoding) throws IOException {
		HashMap<String, Sentence> list = new HashMap<>();
		File file = new File(path);
		
		if(file.isFile() && file.exists()) {
			InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), encoding);
			BufferedReader reader = new BufferedReader(inputStreamReader);
			
			String line = "";
			while((line = reader.readLine()) != null) {
				line = CommonUtils.ToDBC(line).trim();
				if(!line.equals("")) {
					String[] strings = line.split("\\s+");
					String pid = strings[0].trim();
					pid = pid.split("=")[1].replaceAll("\\)", "");
					Sentence sentence =  new Sentence(strings[1].trim().split(""));
					
					list.put(pid, sentence);
				}
			}
			reader.close();
		}else {
			System.err.println("File:\""+path+"\" read failed!");
		}
		
		return list;	
	}
	
	/**
	 * 读取SIGHAN2015拼写纠错测试语料的黄金标准语料（SIGHAN15_CSC_TestTruth.txt）
	 * @param path		语料路径
	 * @param encoding	语料编码
	 * @return			句子pid与该句子所有错字的映射
	 * @throws IOException
	 */
	public static HashMap<String, ArrayList<SpellError>> readGoldFile(String path, String encoding) throws IOException {
		HashMap<String, ArrayList<SpellError>> list = new HashMap<>();
		File file = new File(path);
		int no = 0;
		if(file.isFile() && file.exists()) {
			InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), encoding);
			BufferedReader reader = new BufferedReader(inputStreamReader);
			
			String line = "";
			while((line = reader.readLine()) != null) {
				System.out.println(++no);
				line = CommonUtils.ToDBC(line).trim();
				if(!line.equals("")) {
					String[] strings = line.split(",");
					if(strings.length > 2) {
						ArrayList<SpellError> errors = new ArrayList<>();
						for(int i = 1; i < strings.length - 1; i=i+2) {
							SpellError error = new SpellError(strings[i+1].trim(), Integer.parseInt(strings[i].trim()));
							errors.add(error);
						}
						list.put(strings[0], errors);
					}
					
				}
			}
			reader.close();
		}else {
			System.err.println("File:\""+path+"\" read failed!");
		}
		
		return list;	
	}
	
	

	/**
	 * 读取SIGHAN2015拼写纠错测试语料（SIGHAN15_CSC_TestInput.txt）
	 * @param path		语料路径
	 * @param encoding	语料格式
	 * @return			句子pid与句子内容的映射
	 * @throws IOException
	 */
	public static List<String> readSGMLFile(String path, String encoding) throws IOException {
		List<String> list = new ArrayList<>();
		File file = new File(path);
		
		if(file.isFile() && file.exists()) {
			InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), encoding);
			BufferedReader reader = new BufferedReader(inputStreamReader);
			
			String line = "";
			while((line = reader.readLine()) != null) {
				line = CommonUtils.ToDBC(line).trim();
				if(!line.equals("")) {
					list.add(line);
				}
			}
			reader.close();
		}else {
			System.err.println("File:\""+path+"\" read failed!");
		}
		
		return list;	
	}
	
	/**
	 * 按行读取文本中的句子
	 * @param path		文本的路径
	 * @param encoding	文本的编码
	 * @return			句子列表
	 * @throws IOException
	 */
	public static ArrayList<Sentence> readFile(String path, String encoding) throws IOException {
		ArrayList<Sentence> list = new ArrayList<Sentence>();
		File file = new File(path);
		
		if(file.isFile() && file.exists()) {
			InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), encoding);
			BufferedReader reader = new BufferedReader(inputStreamReader);
			
			String line = "";
			while((line = reader.readLine()) != null) {
				line = CommonUtils.ToDBC(line).trim();
				if(line.equals(""))
					continue;
				
				String[] chs = line.split("");
				list.add(new Sentence(chs));
			}
			reader.close();
		}else {
			System.err.println("File:\""+path+"\" read failed!");
		}
		
		return list;	
	}
	
	/**
	 * 建立字典
	 * @param path	字典文件路径
	 * @return		字典
	 * @throws IOException
	 */
	public static Dictionary constructDict(String path) throws IOException {
		File file = new File(path);
		DataInputStream reader = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
		Dictionary dictionary = new Dictionary();
		int count = reader.readInt();
		for(int i = 0; i < count; i++) {
			String[] entry = reader.readUTF().split("\t");
			if(entry.length == 2) {
				String ngrams = CommonUtils.ToDBC(entry[0]);
				ngrams = ngrams.replaceAll("\t", "").trim();
				if(!ngrams.equals("")) {
					int num = Integer.parseInt(entry[1]);
					dictionary.add(ngrams, num);
				}
			}
		}
		reader.close();
	
		return dictionary;
	}

	/**
	 * 加载n元模型文件
	 * @param modelFile		语言模型文件
	 * @return				语言模型
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static NGramModel loadModel(String modelFile) throws ClassNotFoundException, IOException {
		AbstractNGramModelReader modelReader;
		if(modelFile.endsWith(".arpa"))
			modelReader = new ARPATextFileNGramModleReader(new File(modelFile));
		else if(modelFile.endsWith(".bin"))
			modelReader = new BinaryFileNGramModelReader(new File(modelFile));
		else
			modelReader = new TextFileNGramModelReader(new File(modelFile));
	 
		return new HustNGramModel(modelReader.constructModel());
	}
	
	/**
	 * 写文件
	 * @param outPath	写出路径
	 * @param encoding	文件编码
	 * @param content	写入的内容
	 * @throws IOException
	 */
	public static void writeEvaluation(String outPath, String encoding, String content) throws IOException {
		OutputStreamWriter oWriter = new OutputStreamWriter(new FileOutputStream(new File(outPath)), encoding);
		BufferedWriter writer = new BufferedWriter(oWriter);
		writer.write(content);
		writer.close();
	}
}