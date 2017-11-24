package hust.tools.csc.checker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hust.tools.csc.util.FileOperator;
import hust.tools.csc.util.Sentence;

/**
 *<ul>
 *<li>Description: 拼写纠错应用程序
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年11月21日
 *</ul>
 */
public class CSCApp {

	/**
	 * 纠正文本
	 * @param checkModel	拼写纠错模型
	 * @param testCorpus	待纠正语料
	 * @param encoding		语料编码
	 * @param output		结果输出路径
	 * @throws IOException
	 */
	private static void check(ChineseSpellChecker checkModel, String testCorpus, String encoding, String output) throws IOException {
		ArrayList<Sentence> sentences = FileOperator.readFile(testCorpus, encoding);
		OutputStreamWriter oWriter = new OutputStreamWriter(new FileOutputStream(new File(output)), encoding);
		BufferedWriter writer = new BufferedWriter(oWriter);
		
		int no = 1;
		Sentence bestSentence = null;
		for(Sentence sentence : sentences) {
			System.out.println(no++);
			bestSentence = checkModel.correct(sentence);
			writer.write(bestSentence.toString());
			writer.newLine();
		}
		writer.close();	
	}
 
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		String[] temp = new String[]{"ds", "dsc", "dsb", "dscb", "bcws", "bcwsc", "bcwsb", "bcwscb", 
				"simd", "simdb", "simdc", "simdcb","hust", "hustc", "hustb", "hustcb"};
		List<String> methods = Arrays.asList(temp);
		
		int len = args.length;
		if(5 != len) {
			System.err.println("错误的参数个数：" + len + "\n示例:ChineseSpellChecker  训练语料  测试语料  文件编码  模型方法  输出路径");
			System.exit(0);
		}
		
		String train = args[0];
		String test = args[1];
		String encoding = args[2];
		String method = args[3];
		if(!methods.contains(method.toLowerCase())){
			System.err.println("错误的模型训练方法：" + method + "\n请从列表中选择："+ methods);
			System.exit(0);
		}
		String output = args[4];
		
		ChineseSpellCheckerTrainer modelTrainer = new ChineseSpellCheckerTrainer(train, encoding, method);
		ChineseSpellChecker checkModel = modelTrainer.trainCSCModel();
		check(checkModel, test, encoding, output);
	}
}
