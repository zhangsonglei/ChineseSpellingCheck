package hust.tools.csc.checker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hust.tools.csc.evaluation.CSCEvaluator;
import hust.tools.csc.evaluation.Evaluation;
import hust.tools.csc.ngram.NGramModel;
import hust.tools.csc.util.Dictionary;
import hust.tools.csc.util.FileOperator;
import hust.tools.csc.util.Sentence;

/**
 *<ul>
 *<li>Description: 模型评价应用程序
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年11月22日
 *</ul>
 */
public class CSCEvaluationAPP {
	
	/**
	 * 纠正文本
	 * @param checkModel	拼写纠错模型
	 * @param testCorpus	待纠正语料
	 * @param encoding		语料编码
	 * @param output		结果输出路径
	 * @throws IOException
	 */
	private static void evaluation(ChineseSpellChecker checkModel, String testCorpus, String goldCorpus, String encoding, String output) throws IOException {
		ArrayList<Sentence> original = FileOperator.readFile(testCorpus, encoding);
		ArrayList<Sentence> gold = FileOperator.readFile(goldCorpus, encoding);
		ArrayList<Sentence> result = new ArrayList<>();
		
		int no = 1;
		Sentence bestSentence = null;
		for(Sentence sentence : original) {
			System.out.println(no++);
			bestSentence = checkModel.correct(sentence);
			result.add(bestSentence);
		}
		
		Evaluation evaluation = new CSCEvaluator(original, gold, result);
		String eval = evaluation.show();
		if(output != null) {
			FileOperator.writeEvaluation(output, encoding, eval);
		}		
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
		if(5 != len && 6 != len) {
			System.err.println("错误的参数个数：" + len + "\n示例1:ChineseSpellChecker  模型文件  模型方法  测试语料  黄金语料  文件编码   输出路径。"
												   + "\n示例2：ChineseSpellChecker  模型文件  模型方法  测试语料  黄金语料  文件编码");
			System.exit(0);
		}
		
		String model = args[0];
		
		String method = args[1];
		if(!methods.contains(method.toLowerCase())){
			System.err.println("错误的模型训练方法：" + method + "\n请从列表中选择："+ methods);
			System.exit(0);
		}
		
		String test = args[2];
		String gold = args[3];
		String encoding = args[4];
		
		String output = null;
		if(len == 6)
			output = args[5];
		
		ChineseSpellChecker checkModel = readModel(model, method);
		evaluation(checkModel, test, gold, encoding, output);
	}
}
