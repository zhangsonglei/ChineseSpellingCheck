package hust.tools.csc.checker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hust.tools.csc.ngram.HustNGramModel;
import hust.tools.csc.ngram.NGramModel;
import hust.tools.csc.util.Dictionary;
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
	 * @param model			拼写纠错模型
	 * @param testCorpus	待纠正语料
	 * @param encoding		语料编码
	 * @param method		模型方法
	 * @param output		结果输出路径
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	private static void check(String model, String testCorpus, String encoding, String method, String output) throws IOException, ClassNotFoundException {
		ChineseSpellChecker checkModel = readModel(model, method);
		
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
	
	/**
	 * 输出拼写纠正模型
	 * @param corpus	训练模型的语料
	 * @param encoding	语料编码
	 * @param method	训练方法
	 * @param zipPath	输出路径
	 * @throws IOException
	 */
	private static void writeModel(String corpus, String encoding, String method, String zipPath) throws IOException {
		ChineseSpellCheckerTrainer trainer = new ChineseSpellCheckerTrainer();
		ArrayList<String> files = new ArrayList<>();
		HustNGramModel nGramModel = trainer.constructLM(corpus, encoding, 3);
		nGramModel.writeLM("resource\\lm.bin");
		files.add("resource\\lm.bin");
		if(!method.equals("bcws")) {
			int n = 2;
			if(method.equals("bcwsc"))
				n = 1;
			Dictionary dictionary = trainer.constructNGramDict(corpus, encoding, n);
			dictionary.writeDict("resource\\dict.bin");
			files.add("resource\\dict.bin");
		}
		
		FileOperator.zipFiles(files, zipPath);
	}
	
	/**
	 * 读取拼写纠正模型
	 * @param model		模型路径	
	 * @param method	模型方法
	 * @return			拼写纠正模型
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static ChineseSpellChecker readModel(String model, String method) throws ClassNotFoundException, IOException {
		String path = "resource\\";
		FileOperator.unZipFile(method, path);

		NGramModel nGramModel = FileOperator.loadModel(path+"lm.bin");
		ChineseSpellCheckerTrainer trainer = null;
		if(method.equals("bcws")) {
			trainer = new ChineseSpellCheckerTrainer(nGramModel, method);
			return trainer.trainCSCModel();
		}
		
		Dictionary dictionary = FileOperator.constructDict(path+"dict.bin");
		trainer = new ChineseSpellCheckerTrainer(nGramModel, dictionary, method);
		return trainer.trainCSCModel();
	}
 
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		String[] temp = new String[]{"ds", "dsc", "dsb", "dscb", "bcws", "bcwsc", "bcwsb", "bcwscb", 
				"simd", "simdb", "simdc", "simdcb","hust", "hustc", "hustb", "hustcb"};
		List<String> methods = Arrays.asList(temp);
		
		int len = args.length;
		if(6 != len && 5 != len) {
			show(len);
			System.exit(0);
		}
		String operation = args[0];
		if(operation != "train" && operation != "check") {
			System.err.println("错误的指令：" + operation+"\n--train或check");
			System.exit(0);
		}else if((operation == "train" && len != 5) || (operation == "check" && len != 6)) {
			show(len);
			System.exit(0);
		}else{
			String sourceFile = args[1];
			String encoding = "";
			String method = "";
			String output = "";
			
			if(operation == "train") {
				encoding = args[2];
				method = args[3];
				if(!methods.contains(method.toLowerCase())){
					System.err.println("错误的模型训练方法：" + method + "\n请从列表中选择："+ methods);
					System.exit(0);
				}
				
				output = args[4];
				writeModel(sourceFile, encoding, method, output);
			}else{
				String test = args[2];
				encoding = args[3];
				method = args[4];
				if(!methods.contains(method.toLowerCase())){
					System.err.println("错误的模型训练方法：" + method + "\n请从列表中选择："+ methods);
					System.exit(0);
				}
				
				output = args[5];
				
				check(sourceFile, test, encoding, method, output);
			}
		}
	}
	
	/**
	 * 打印错误信息
	 * @param len	用户输入参数的个数
	 */
	private static void show(int len) {
		System.err.println("错误的参数个数：" + len + "\n示例1:train  训练语料  文件编码  模型方法  输出路径"
				   + "\n示例2：check  模型文件 模型方法  测试语料  语料编码  输出路径");
	}
}
