package hust.tools.csc.checker;

import java.io.IOException;
import java.util.ArrayList;
import hust.tools.csc.evaluation.CSCEvaluator;
import hust.tools.csc.evaluation.Evaluation;
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
	private static void evaluation(ChineseSpellCheckModel checkModel, String testCorpus, String goldCorpus, String encoding, String output) throws IOException {
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
 
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		int len = args.length;
		if(5 != len || 6 != len) {
			System.err.println("错误的参数个数：" + len + "\n示例1:ChineseSpellChecker  训练语料  模型方法  测试语料  黄金语料  文件编码   输出路径。"
												   + "\n示例2：ChineseSpellChecker  训练语料  模型方法  测试语料  黄金语料  文件编码");
			System.exit(0);
		}
		
		String train = args[0];
		String method = args[1];
		String test = args[2];
		String gold = args[3];
		String encoding = args[4];
		
		String output = null;
		if(len == 6)
			output = args[5];
		
		ChineseSpellCheckModelTrainer modelTrainer = new ChineseSpellCheckModelTrainer(train, encoding, method);
		ChineseSpellCheckModel checkModel = modelTrainer.trainCSCModel();
		evaluation(checkModel, test, gold, encoding, output);
	}
}
