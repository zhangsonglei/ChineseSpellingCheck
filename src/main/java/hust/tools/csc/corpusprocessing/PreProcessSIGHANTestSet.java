package hust.tools.csc.corpusprocessing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import hust.tools.csc.detecet.SpellError;
import hust.tools.csc.util.FileOperator;
import hust.tools.csc.util.Sentence;

/**
 *<ul>
 *<li>Description: 预处理SIGHAN2015拼写纠错测试语料，分别以测试句子集，与纠正后的句子集的形式存储 
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年11月5日
 *</ul>
 */
public class PreProcessSIGHANTestSet {
	
	public static void main(String[] args) throws IOException {
		
		if(args.length != 5) {
			System.out.println("错误的参数个数： "+args.length + "\n示例: 原始测试语料  原始标准结果  文档编码  测试语料输出  标准结果输出");
			System.exit(0);
		}
		
		String testInput = args[0];
		String goldInput = args[1];
		String encoding = args[2];
		String testFile = args[3];
		String goldFile = args[4];
		
		HashMap<String, Sentence> tests = FileOperator.readTestFile(testInput, encoding);
		HashMap<String, ArrayList<SpellError>> res = FileOperator.readGoldFile(goldInput, encoding);
		
		OutputStreamWriter test = new OutputStreamWriter(new FileOutputStream(new File(testFile)), encoding);
		BufferedWriter testwriter = new BufferedWriter(test);
		
		OutputStreamWriter result = new OutputStreamWriter(new FileOutputStream(new File(goldFile)), encoding);
		BufferedWriter resultwriter = new BufferedWriter(result);
		
		for(Entry<String, Sentence> entry : tests.entrySet()) {
			String pid = entry.getKey();
			Sentence sentence = entry.getValue();
			testwriter.write(sentence.toString());
			testwriter.newLine();
			if(res.containsKey(pid)) {
				ArrayList<SpellError> errors = res.get(pid);
				for(int i = 0; i < errors.size(); i++) {
					SpellError error = errors.get(i);
					sentence = sentence.setToken(error.getLocation() - 1, error.getCharacter());
				}
			}
			resultwriter.write(sentence.toString());
			resultwriter.newLine();
		}
		resultwriter.close();
		testwriter.close();
	}
}
