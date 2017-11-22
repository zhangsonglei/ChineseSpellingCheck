package hust.tools.csc.evaluation;

import java.io.IOException;
import java.util.ArrayList;

import hust.tools.csc.util.FileOperator;
import hust.tools.csc.util.Sentence;

/**
 *<ul>
 *<li>Description: 给定原始文本，标准文本和系统纠错文本，返回各个指标的结果 
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年11月17日
 *</ul>
 */
public class EvaluatorApp {
	
	public static void main(String[] args) throws IOException {
		
		int len = args.length;
		if(4 != len || 5 != len) {
			System.err.println("错误的参数个数：" + len + "\n示例1:(Evaluator 测试语料路径  黄金语料路径  系统结果路径 文件编码  输出路径)"
												   + "\n示例2:(Evaluator 测试语料路径  黄金语料路径  系统结果路径 文件编码)");
			System.exit(0);
		}
		
		String originalFile = args[0];
		String goldFile = args[1];
		String resultFile = args[2];
		String encoding = args[3];
		
		ArrayList<Sentence> original = FileOperator.readFile(originalFile, encoding);
		ArrayList<Sentence> gold = FileOperator.readFile(goldFile, encoding);
		ArrayList<Sentence> result = FileOperator.readFile(resultFile, encoding);;
		
		Evaluation evaluator = new CSCEvaluator(original, gold, result);
		String eval = evaluator.show();
		
		if(len == 5) {
			String output = args[4];
			FileOperator.writeEvaluation(output, encoding, eval);
		}
	}
	
	
}
