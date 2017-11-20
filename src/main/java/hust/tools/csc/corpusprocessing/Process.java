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
public class Process {
	
	public static void main(String[] args) throws IOException {
		String testFile = "E:\\FinalTest_SubTask2.txt";
		String goldFile = "E:\\FinalTest_SubTask2_Truth.txt";
		HashMap<String, Sentence> tests = FileOperator.readTestFile(testFile, "utf-8");
		HashMap<String, ArrayList<SpellError>> res = FileOperator.readGoldFile(goldFile, "utf-8");
		
		OutputStreamWriter test = new OutputStreamWriter(new FileOutputStream(new File("E:\\testFile2.txt")), "utf-8");
		BufferedWriter testwriter = new BufferedWriter(test);
		
		OutputStreamWriter result = new OutputStreamWriter(new FileOutputStream(new File("E:\\goldFile2.txt")), "utf-8");
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
