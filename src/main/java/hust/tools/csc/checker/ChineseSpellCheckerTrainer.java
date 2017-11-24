package hust.tools.csc.checker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import hust.tools.csc.ngram.HustNGramModel;
import hust.tools.csc.ngram.NGramModel;
import hust.tools.csc.score.AbstractNoisyChannelModel;
import hust.tools.csc.util.ConfusionSet;
import hust.tools.csc.util.Dictionary;
import hust.tools.csc.util.CommonUtils;
import hust.tools.csc.wordseg.AbstractWordSegment;
import hust.tools.csc.wordseg.CKIPWordSegment;
import hust.tools.ngram.model.KneserNeyLanguageModelTrainer;
import hust.tools.ngram.utils.StringGramSentenceStream;

/**
 *<ul>
 *<li>Description: 中国文拼写纠错模型训练器
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年11月21日
 *</ul>
 */
public class ChineseSpellCheckerTrainer {
	
	private AbstractNoisyChannelModel noisyChannelModel;
	private NGramModel nGramModel;
	private ConfusionSet confusionSet;

	public ChineseSpellCheckerTrainer(String corpus, String encoding, String method) throws IOException {
		System.out.println("开始建立ngram模型...");
		nGramModel = constructLM(corpus, encoding, 3);
		System.out.println("ngram模型建立完成\n开始建立混淆集...");
		confusionSet = constructConfusionSet(new File("resources\\pro.txt"));
		System.out.println("混淆集建立完成。proSize = "+confusionSet.getSimilarProCount());
		
		System.out.println("开始构造噪音通道模型...");
		selectNoisyChannelModel(corpus, encoding, method);
		System.out.println("噪音通道模型构造完成。 method = " + method + "\n开始进行拼写检查...");
	}

	/**
	 * 训练中文拼写纠错模型
	 * @return	中文拼写纠错模型
	 */
	public ChineseSpellChecker trainCSCModel() {
		return new ChineseSpellChecker(noisyChannelModel);
	}
	
	/**
	 * 训练噪音通道模型
	 * @param trainCorpus	训练语料
	 * @param encoding		语料编码
	 * @param method		噪音通道模型训练方法
	 * @throws IOException
	 */
	private void selectNoisyChannelModel(String trainCorpus, String encoding, String method) throws IOException {
		Dictionary dictionary = null;
		AbstractWordSegment wordSegment = null;
		
		File file = new File(trainCorpus);
		InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), encoding);
		BufferedReader corpus = new BufferedReader(inputStreamReader);
		
		switch (method.toLowerCase()) {
		case "ds":
			dictionary = constructNGramDict(corpus, 2);
			wordSegment = new CKIPWordSegment();
			noisyChannelModel = new DoubleStageNoisyChannelModel(dictionary, nGramModel, confusionSet, wordSegment);
			break;
		case "dsc":
			dictionary = constructNGramDict(corpus, 2);
			wordSegment = new CKIPWordSegment();
			noisyChannelModel = new DoubleStageNoisyChannelModelBasedCharacter(dictionary, nGramModel, confusionSet, wordSegment);
			break;
		case "dsb":
			dictionary = constructNGramDict(corpus, 2);
			wordSegment = new CKIPWordSegment();
			noisyChannelModel = new DoubleStageNoisyChannelModelBasedCharacterAndBigram(dictionary, nGramModel, confusionSet, wordSegment);
			break;
		case "dscb":
			dictionary = constructNGramDict(corpus, 2);
			wordSegment = new CKIPWordSegment();
			noisyChannelModel = new DoubleStageNoisyChannelModelBasedBigram(dictionary, nGramModel, confusionSet, wordSegment);
			break;
		case "hust":
			dictionary = constructNGramDict(corpus, 2);
			wordSegment = new CKIPWordSegment();
			noisyChannelModel = new HUSTNoisyChannelModel(dictionary, nGramModel, confusionSet, wordSegment);
			break;
		case "hustc":
			dictionary = constructNGramDict(corpus, 2);
			wordSegment = new CKIPWordSegment();
			noisyChannelModel = new HUSTNoisyChannelModelBasedCharacter(dictionary, nGramModel, confusionSet, wordSegment);
			break;
		case "hustb":
			dictionary = constructNGramDict(corpus, 2);
			wordSegment = new CKIPWordSegment();
			noisyChannelModel = new HUSTNoisyChannelModelBasedBigram(dictionary, nGramModel, confusionSet, wordSegment);
			break;
		case "hustbc":
			dictionary = constructNGramDict(corpus, 2);
			wordSegment = new CKIPWordSegment();
			noisyChannelModel = new HUSTNoisyChannelModelBasedCharacterAndBigram(dictionary, nGramModel, confusionSet, wordSegment);
			break;
		case "bcws":
			wordSegment = new CKIPWordSegment();
			noisyChannelModel = new BCWSNoisyChannelModel(nGramModel, confusionSet, wordSegment);
			break;
		case "bcwsc":
			dictionary = constructNGramDict(corpus, 1);
			wordSegment = new CKIPWordSegment();
			noisyChannelModel = new BCWSNoisyChannelModelBasedCharacter(dictionary, nGramModel, confusionSet, wordSegment);
			break;
		case "bcwsb":
			dictionary = constructNGramDict(corpus, 2);
			wordSegment = new CKIPWordSegment();
			noisyChannelModel = new BCWSNoisyChannelModelBasedBigram(dictionary, nGramModel, confusionSet, wordSegment);
			break;
		case "bcwscb":
			dictionary = constructNGramDict(corpus, 2);
			wordSegment = new CKIPWordSegment();
			noisyChannelModel = new BCWSNoisyChannelModelBasedCharacterAndBigram(dictionary, nGramModel, confusionSet, wordSegment);
			break;
		case "simd":
			dictionary = constructNGramDict(corpus, 2);
			noisyChannelModel = new SIMDNoisyChannelModel(dictionary, nGramModel, confusionSet);
			break;
		case "simdc":
			dictionary = constructNGramDict(corpus, 2);
			noisyChannelModel = new SIMDNoisyChannelModelBasedCharacter(dictionary, nGramModel, confusionSet);
			break;
		case "simdb":
			dictionary = constructNGramDict(corpus, 2);
			noisyChannelModel = new SIMDNoisyChannelModelBasedBigram(dictionary, nGramModel, confusionSet);
			break;
		case "simdcb":
			dictionary = constructNGramDict(corpus, 2);
			noisyChannelModel = new SIMDNoisyChannelModelBasedCharacterAndBigram(dictionary, nGramModel, confusionSet);
			break;
		default:
			break;
		}		
	}
	
	/**
	 * 构造基于ngram频数的字典
	 * @param corpus	ngram语料
	 * @param n			ngram的最大长度
	 * @return			基于ngram频数的字典
	 * @throws IOException 
	 */
	private Dictionary constructNGramDict(BufferedReader corpus, int n) throws IOException {
		Dictionary dictionary = new Dictionary();
		
		System.out.println("开始建立字典");
		String line = "";
		while ((line = corpus.readLine()) != null) {
			line = CommonUtils.ToDBC(line.replaceAll("\\s+", "")).trim();
			ArrayList<String> ngrams = new ArrayList<>();
			
			for(int i = 1; i <= n; i++) {
				ngrams = CommonUtils.generateNGrams(line.split(""), i);
				for(String ngram : ngrams)
					dictionary.add(ngram);
			}			
		}
		corpus.close();

		System.out.println("字典建立完成，size = " + dictionary.size());
		return dictionary;
	}
	
	/**
	 * 训练n元模型
	 * @param corpus	n元模型语料
	 * @param encoding	语料格式
	 * @param n			ngram的最大长度
	 * @throws IOException	
	 */
	private NGramModel constructLM(String corpus, String encoding, int n) throws IOException {
		StringGramSentenceStream gramSentenceStream = new StringGramSentenceStream(corpus, encoding);
		KneserNeyLanguageModelTrainer trainer = new KneserNeyLanguageModelTrainer(gramSentenceStream, n);

		return new HustNGramModel(trainer.trainModel());
	}

	/**
	 * 根据困惑集构造困惑矩阵
	 * @param file			混淆集文件
	 * @return				混淆矩阵
	 * @throws IOException
	 */
	private ConfusionSet constructConfusionSet(File similarityPronunciation) throws IOException {
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
}