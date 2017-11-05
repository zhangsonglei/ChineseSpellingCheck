package hust.tools.csc.corpusprocessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import hust.tools.csc.detecet.SpellError;
import hust.tools.csc.util.FormatConvert;
import hust.tools.csc.util.Sentence;

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
				line = FormatConvert.ToDBC(line).trim();
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
	public static HashMap<String, ArrayList<SpellError>> readFile(String path, String encoding) throws IOException {
		HashMap<String, ArrayList<SpellError>> list = new HashMap<>();
		File file = new File(path);
		
		if(file.isFile() && file.exists()) {
			InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), encoding);
			BufferedReader reader = new BufferedReader(inputStreamReader);
			
			String line = "";
			while((line = reader.readLine()) != null) {
				line = FormatConvert.ToDBC(line).trim();
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
}