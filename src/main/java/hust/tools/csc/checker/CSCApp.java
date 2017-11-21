package hust.tools.csc.checker;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import hust.tools.csc.ngram.HustNGramModel;
import hust.tools.csc.ngram.NGramModel;
import hust.tools.csc.score.AbstractNoisyChannelModel;
import hust.tools.csc.util.ConfusionSet;
import hust.tools.csc.util.Dictionary;
import hust.tools.csc.util.CommonUtils;
import hust.tools.csc.util.Sentence;
import hust.tools.csc.wordseg.AbstractWordSegment;
import hust.tools.csc.wordseg.CKIPWordSegment;
import hust.tools.ngram.io.ARPATextFileNGramModleReader;
import hust.tools.ngram.io.BinaryFileNGramModelReader;
import hust.tools.ngram.io.TextFileNGramModelReader;
import hust.tools.ngram.model.AbstractNGramModelReader;

public class CSCApp {
	
	private static ConfusionSet confusionSet;
	private static NGramModel nGramModel;
	private static AbstractNoisyChannelModel noisyChannelModel;

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		int len = args.length;
		if(4 != len) {
			System.err.println("错误的参数个数：" + len + "\n示例:ChineseSpellChecker 待纠正文本  文件编码  纠正方法 输出路径");
			System.exit(0);
		}
		
		String testFile = args[0];
		String encoding = args[1];
		String method = args[2];
		String output = args[3];
		
		nGramModel = loadModel("E:\\JOB\\TestData\\smallknLM.bin");
		confusionSet = constructConfusionSet(new File("resources\\pro.txt"));
		selectNoisyChannelModel(method);
		
		List<Sentence> sentences = readFile(testFile, encoding);
		OutputStreamWriter oWriter = new OutputStreamWriter(new FileOutputStream(new File(output)), encoding);
		BufferedWriter writer = new BufferedWriter(oWriter);
		
		int no = 1;
		Sentence bestSentence = null;
		for(Sentence sentence : sentences) {
			System.out.println(no++);
			ArrayList<Sentence> cands = noisyChannelModel.getCorrectSentence(sentence);
			if(cands.size() == 0 || cands == null) 
				continue;
			
			bestSentence = cands.get(0);
			writer.write(bestSentence.toString());
			writer.newLine();
		}
		writer.close();		
	}

	private static void selectNoisyChannelModel(String method) throws IOException {
		switch (method.toLowerCase()) {
		case "ds":
			Dictionary dictionary = constructDict("E:\\JOB\\TestData\\trigramCount.bin");
			AbstractWordSegment wordSegment = new CKIPWordSegment();
			noisyChannelModel = new DoubleStageNoisyChannelModel(dictionary, nGramModel, confusionSet, wordSegment);
			break;
		case "dsc":
			Dictionary dictionary1 = constructDict("E:\\JOB\\TestData\\trigramCount.bin");
			AbstractWordSegment wordSegment1 = new CKIPWordSegment();
			noisyChannelModel = new DoubleStageNoisyChannelModelBasedCharacter(dictionary1, nGramModel, confusionSet, wordSegment1);
			break;
		case "dsb":
			Dictionary dictionary2 = constructDict("E:\\JOB\\TestData\\trigramCount.bin");
			AbstractWordSegment wordSegment2 = new CKIPWordSegment();
			noisyChannelModel = new DoubleStageNoisyChannelModelBasedCharacterAndBigram(dictionary2, nGramModel, confusionSet, wordSegment2);
			break;
		case "dscb":
			Dictionary dictionary3 = constructDict("E:\\JOB\\TestData\\trigramCount.bin");
			AbstractWordSegment wordSegment3 = new CKIPWordSegment();
			noisyChannelModel = new DoubleStageNoisyChannelModelBasedBigram(dictionary3, nGramModel, confusionSet, wordSegment3);
			break;
		case "hust":
			Dictionary dictionary4 = constructDict("E:\\JOB\\TestData\\trigramCount.bin");
			AbstractWordSegment wordSegment4 = new CKIPWordSegment();
			noisyChannelModel = new HUSTNoisyChannelModel(dictionary4, nGramModel, confusionSet, wordSegment4);
			break;
		case "hustc":
			Dictionary dictionary5 = constructDict("E:\\JOB\\TestData\\trigramCount.bin");
			AbstractWordSegment wordSegment5 = new CKIPWordSegment();
			noisyChannelModel = new HUSTNoisyChannelModelBasedCharacter(dictionary5, nGramModel, confusionSet, wordSegment5);
			break;
		case "hustb":
			Dictionary dictionary6 = constructDict("E:\\JOB\\TestData\\trigramCount.bin");
			AbstractWordSegment wordSegment6 = new CKIPWordSegment();
			noisyChannelModel = new HUSTNoisyChannelModelBasedBigram(dictionary6, nGramModel, confusionSet, wordSegment6);
			break;
		case "hustbc":
			Dictionary dictionary7 = constructDict("E:\\JOB\\TestData\\trigramCount.bin");
			AbstractWordSegment wordSegment7 = new CKIPWordSegment();
			noisyChannelModel = new HUSTNoisyChannelModelBasedCharacterAndBigram(dictionary7, nGramModel, confusionSet, wordSegment7);
			break;
		case "bcws":
			AbstractWordSegment wordSegment8 = new CKIPWordSegment();
			noisyChannelModel = new BCWSNoisyChannelModel(nGramModel, confusionSet, wordSegment8);
			break;
		case "bcwsc":
			Dictionary dictionary9 = constructDict("E:\\JOB\\TestData\\trigramCount.bin");
			AbstractWordSegment wordSegment9 = new CKIPWordSegment();
			noisyChannelModel = new BCWSNoisyChannelModelBasedCharacter(dictionary9, nGramModel, confusionSet, wordSegment9);
			break;
		case "bcwsb":
			Dictionary dictionary10 = constructDict("E:\\JOB\\TestData\\trigramCount.bin");
			AbstractWordSegment wordSegment10 = new CKIPWordSegment();
			noisyChannelModel = new BCWSNoisyChannelModelBasedBigram(dictionary10, nGramModel, confusionSet, wordSegment10);
			break;
		case "bcwscb":
			Dictionary dictionary11 = constructDict("E:\\JOB\\TestData\\trigramCount.bin");
			AbstractWordSegment wordSegment11 = new CKIPWordSegment();
			noisyChannelModel = new BCWSNoisyChannelModelBasedCharacterAndBigram(dictionary11, nGramModel, confusionSet, wordSegment11);
			break;
		case "simd":
			Dictionary dictionary12 = constructDict("E:\\JOB\\TestData\\trigramCount.bin");
			System.out.println(dictionary12.size());
			noisyChannelModel = new SIMDNoisyChannelModel(dictionary12, nGramModel, confusionSet);
			break;
		case "simdc":
			Dictionary dictionary13 = constructDict("E:\\JOB\\TestData\\trigramCount.bin");
			noisyChannelModel = new SIMDNoisyChannelModelBasedCharacter(dictionary13, nGramModel, confusionSet);
			break;
		case "simdb":
			Dictionary dictionary14 = constructDict("E:\\JOB\\TestData\\trigramCount.bin");
			noisyChannelModel = new SIMDNoisyChannelModelBasedBigram(dictionary14, nGramModel, confusionSet);
			break;
		case "simdcb":
			Dictionary dictionary15 = constructDict("E:\\JOB\\TestData\\trigramCount.bin");
			noisyChannelModel = new SIMDNoisyChannelModelBasedCharacterAndBigram(dictionary15, nGramModel, confusionSet);
			break;
		default:
			break;
		}		
	}

	/**
	 * 建立字典
	 * @param path	字典文件路径
	 * @return		字典
	 * @throws IOException
	 */
	private static Dictionary constructDict(String path) throws IOException {
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
	 * 根据困惑集构造困惑矩阵
	 * @param file			混淆集文件
	 * @return				混淆矩阵
	 * @throws FileNotFoundException 
	 * @throws IOException
	 */
	private static ConfusionSet constructConfusionSet(File similarityPronunciation) throws IOException {
		HashMap<String, HashSet<String>> Pronunciation = new HashMap<>();
		HashMap<String, HashSet<String>> Shape = new HashMap<>();
		ConfusionSet set = new ConfusionSet(Pronunciation, Shape);
	
		InputStreamReader pronunciation = new InputStreamReader(new FileInputStream(similarityPronunciation), "utf-8");
//		InputStreamReader shape = new InputStreamReader(new FileInputStream(similarityShape), "utf-8");
		BufferedReader proReader = new BufferedReader(pronunciation);
//		BufferedReader shapeReader = new BufferedReader(shape);
		String line = "";
		try {
			while ((line = proReader.readLine())!= null) {
				line = CommonUtils.ToDBC(line);		//全角转为半角
				line = line.replace("\t", "").trim();	//去除多于的空格
				if(!line.equals("")) {					//过滤空行
					String[] strings = line.split("");
					set.addSimilarityPronunciations(strings);
				}
			}
			proReader.close();
		
//			while ((line = shapeReader.readLine())!= null) {
//				line = FormatConvert.ToDBC(line);		//全角转为半角
//				line = line.replace("\\s+", "").trim();	//去除多于的空格
//				if(!line.equals("")) {					//过滤空行
//					String[] sentence = FormatConvert.ToDBC(line).split(",");
//					String key = sentence[0];
//					String[] values = sentence[1].split("");
//					HashSet<String> set = new HashSet<>();
//					for(int i = 0; i < values.length; i++) 
//						set.add(values[i]);
//				
//					confusionSet.addSimilarityShapes(key, set);
//				}
//			}
//			shapeReader.close();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		return set;
	}

	/**
	 * 加载n元模型文件
	 * @param modelFile		语言模型文件
	 * @return				语言模型
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private static NGramModel loadModel(String modelFile) throws ClassNotFoundException, IOException {
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
	 * 按行读取文件并以Sentence为元素保存到List中
	 * @param path			文件路径
	 * @param encoding		文件编码
	 * @return				
	 * @throws IOException
	 */
	public static List<Sentence> readFile(String path, String encoding) throws IOException {
		List<Sentence> list = new ArrayList<>();
		File file = new File(path);
	
		if(file.isFile() && file.exists()) {
			InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), encoding);
			BufferedReader reader = new BufferedReader(inputStreamReader);
		
			String line = "";
			while((line = reader.readLine()) != null) {
				line = line.trim();
				if(!line.equals("")) {
					String[] sentence = CommonUtils.ToDBC(line).split("");
					list.add(new Sentence(sentence));
				}
			}
			reader.close();
		}else {
			System.err.println("File:\""+path+"\" read failed!");
		}
	
		return list;	
	}
}
