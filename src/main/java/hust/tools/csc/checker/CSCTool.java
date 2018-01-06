package hust.tools.csc.checker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hust.tools.csc.evaluation.CSCEvaluator;
import hust.tools.csc.evaluation.Evaluation;
import hust.tools.csc.ngram.HustNGramModel;
import hust.tools.csc.ngram.NGramModel;
import hust.tools.csc.util.Dictionary;
import hust.tools.csc.util.FileOperator;
import hust.tools.csc.util.Sentence;

/**
 *<ul>
 *<li>Description: 拼写纠错应用程序(train--训练模型, check--拼写纠错, eval--模型评价)
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年11月21日
 *</ul>
 */
public class CSCTool {
	
	/**
	 * 训练并输出拼写纠正模型
	 * @param corpus	训练模型的语料
	 * @param charType  字体类型(简/繁)
	 * @param encoding	语料编码
	 * @param method	训练方法
	 * @param zipPath	输出路径
	 * @throws IOException
	 */
	private static void trainModel(String corpus, String charType, String encoding, String method, String zipPath) throws IOException {
		System.out.println("开始训练拼写纠正模型...");
		ChineseSpellCheckerTrainer trainer = new ChineseSpellCheckerTrainer();
		ArrayList<String> files = new ArrayList<>();
		System.out.println("开始训练语言模型...");
		HustNGramModel nGramModel = trainer.constructLM(corpus, encoding, 3);
		String lm = zipPath + "\\lm.bin";
		nGramModel.writeLM(lm);
		System.out.println("语言模型训练完成。");
		files.add(lm);
		if(!method.equals("bcws")) {
			int n = 2;
			if(method.equals("bcwsc"))
				n = 1;
			System.out.println("开始建立字典...");
			Dictionary dictionary = trainer.constructNGramDict(corpus, encoding, n);
			String dict = zipPath + "\\dict.bin";
			dictionary.writeDict(dict);
			System.out.println("字典建立完成。");
			files.add(dict);
		}
		
		FileOperator.zipFiles(files, zipPath);
		System.out.println("拼写纠正模型建立完成。");
	}
	
	/**
	 * 纠正文本
	 * @param model			拼写纠错模型
	 * @param testCorpus	待纠正语料
	 * @param charType  	字体类型(简/繁)
	 * @param encoding		语料编码
	 * @param method		模型方法
	 * @param output		结果输出路径
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	private static void check(String model, String testCorpus, String charType, String encoding, String method, String output) throws IOException, ClassNotFoundException {
		System.out.println("加载模型文件...");
		ChineseSpellChecker checkModel = readModel(model, method, charType);
		System.out.println("模型文件加载完成。\n开始纠正文本..");
		ArrayList<Sentence> sentences = FileOperator.readSentenceFile(testCorpus, encoding);
		OutputStreamWriter oWriter = new OutputStreamWriter(new FileOutputStream(new File(output)), encoding);
		BufferedWriter writer = new BufferedWriter(oWriter);
		
		int no = 1;
		Sentence bestSentence = null;
		for(Sentence sentence : sentences) {
			System.out.println(no++);
			bestSentence = checkModel.bestSentence(sentence);
			writer.write(bestSentence.toString());
			writer.newLine();
		}
		writer.close();	
		System.out.println("纠正结束。");
	}

	/**
	 * 评估拼写纠正模型
	 * @param model			拼写纠正模型
	 * @param method		模型训练方法
	 * @param testCorpus	测试语料文件
	 * @param goldCorpus	标准语料文件
	 * @param charType		字体类型(简/繁)
	 * @param encoding		语料编码
	 * @param output		结果输出路径
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private static void evaluation(String model, String method, String testCorpus, String goldCorpus, String charType, String encoding, String output) throws IOException, ClassNotFoundException {
		System.out.println("加载模型文件...");
		ChineseSpellChecker checkModel = readModel(model, method, charType);
		System.out.println("模型文件加载完成。\n开始预处理测试语料...");
		ArrayList<Sentence> original = FileOperator.readSentenceFile(testCorpus, encoding);
		ArrayList<Sentence> gold = FileOperator.readSentenceFile(goldCorpus, encoding);
		ArrayList<Sentence> result = new ArrayList<>();
		
		Sentence bestSentence = null;
		for(Sentence sentence : original) {
			bestSentence = checkModel.bestSentence(sentence);
			result.add(bestSentence);
		}
		
		System.out.println("语料预处理完成。\n开始评价模型指标...");
		
		Evaluation evaluation = new CSCEvaluator(original, gold, result);
		String eval = evaluation.show();
		if(output != null) {
			FileOperator.writeEvaluation(output, encoding, eval);
		}
		System.out.println("模型指标评价完成。");
	}
	
	/**
	 * 读取拼写纠正模型
	 * @param model		模型路径	
	 * @param method	模型方法
	 * @param charType	字体类型(简/繁)
	 * @return			拼写纠正模型
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static ChineseSpellChecker readModel(String model, String method, String charType) throws ClassNotFoundException, IOException {
		ChineseSpellCheckerTrainer trainer = null;
		String path = model.split("checker.model")[0];
		List<File> files = FileOperator.unZipFile(model, path);
		
		if(files == null) {
			System.err.println("模型文件为空");
			System.exit(0);
		}else {
			System.out.println("开始加载语言模型...");
			File lmFile = new File(path + "lm.bin");
			if(lmFile.exists()) {
				NGramModel nGramModel = FileOperator.loadModel(lmFile);
				System.out.println("语言模型加载完成。");
				if(method.equals("bcws")) {
					trainer = new ChineseSpellCheckerTrainer(nGramModel, method, charType);
					return trainer.trainCSCModel();
				}

				System.out.println("开始加载字典...");
				File dictFile = new File(path + "dict.bin");
				if(dictFile.exists()) {
					Dictionary dictionary = FileOperator.loadDict(dictFile);
					System.out.println("字典加载完成。");
					trainer = new ChineseSpellCheckerTrainer(nGramModel, dictionary, method, charType);
				}else {
					System.err.println("模型缺失字典");
					System.exit(0);
				}
			}else {
				System.err.println("模型缺失语言模型");
				System.exit(0);
			}
		}

		for(File file : files) {
        	file.delete();
		}
		
		return trainer.trainCSCModel();
	}
	

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		long start = System.currentTimeMillis();
		
		String[] temp = new String[]{"ds", "dsc", "dscib", "dsb", "dscb", "bcws", "bcwsc", "bcwscib", "bcwsb", "bcwscb", 
				"simd", "simdb", "simdc", "simdcib", "simdcb", "hust", "hustc", "hustcib", "hustb", "hustcb"};
		String[] charTypes = new String[]{"trad", "simple"};
		List<String> types = Arrays.asList(charTypes);
		List<String> methods = Arrays.asList(temp);
		
		int len = args.length;
		if(6 != len && 7 != len && 8 != len) 
			errParamLen(len);
		
		String operation = args[0];
		if(!operation.equals("train") && !operation.equals("check") && !operation.equals("eval")) {
			System.err.println("错误的指令：" + operation+"\n--train(训练模型)\n--check(拼写纠正)\n--eval(模型评价)");
			System.exit(0);
		}else if((operation.equals("train") && len != 6) || (operation.equals("check") && len != 7) || ((operation.equals("eval") && len != 8))) {
			errParamLen(len);
		}else {
			String sourceFile = args[1];
			String charType = null;
			String encoding = null;
			String method = null;
			String output = null;
			
			if(operation.equals("train")) {
				charType = args[2];
				if(!types.contains(charType.toLowerCase()))
					errCharType(charType, types);
				
				encoding = args[3];
				method = args[4];
				if(!methods.contains(method.toLowerCase()))
					errMethod(method, methods);
				
				output = args[5];
				trainModel(sourceFile, charType, encoding, method, output);
			}else if(operation.equals("check")) {
				method = args[2];
				if(!methods.contains(method.toLowerCase()))
					errMethod(method, methods);
				
				String test = args[3];
				charType = args[4];
				if(!types.contains(charType.toLowerCase()))
					errCharType(charType, types);
				
				encoding = args[5];
				output = args[6];
				
				check(sourceFile, test, charType, encoding, method, output);
			}else {
				method = args[2];
				if(!methods.contains(method.toLowerCase()))
					errMethod(method, methods);
				
				
				String test = args[3];
				String gold = args[4];
				charType = args[5];
				if(!types.contains(charType.toLowerCase()))
					errCharType(charType, types);
				
				encoding = args[6];
				if(len == 8)
					output = args[7];
				
				evaluation(sourceFile, method, test, gold, charType, encoding, output);
			}
		}
		
		long end = System.currentTimeMillis();
		double min = (end - start) / 60000.0;
		System.out.println("Time : " + min);
	}
	
	/**
	 * 打印错误信息
	 * @param len	用户输入参数的个数
	 */
	private static void errParamLen(int len) {
		System.err.println("错误的参数个数：" + len + "\n示例1:train  训练语料  简繁体(trad/simple)  文件编码  模型方法  输出路径"
				   							   + "\n示例2：check  模型文件 模型方法  测试语料  简繁体(trad/simple)  语料编码  输出路径"
											   + "\n示例3:eval  模型文件  模型方法  测试语料  黄金语料  简繁体(trad/simple)  文件编码   输出路径。"
											   + "\n示例4：eval  模型文件  模型方法  测试语料  黄金语料  简繁体(trad/simple)  文件编码");
		System.exit(0);
	}
	
	private static void errCharType(String charType, List<String> types) {
		System.err.println("错误的字体(简繁体)格式：" + charType + "\n请从列表中选择："+ types);
		System.exit(0);
	}
	
	private static void errMethod(String method, List<String> methods) {
		System.err.println("错误的模型训练方法：" + method + "\n请从列表中选择："+ methods);
		System.exit(0);
	}
}
