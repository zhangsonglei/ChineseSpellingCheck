package hust.tools.csc.checker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import hust.tools.csc.detecet.HUSTDetector;
import hust.tools.csc.detecet.Detector;
import hust.tools.csc.ngram.HustNGramModel;
import hust.tools.csc.ngram.NGramModel;
import hust.tools.csc.score.AbstractNoisyChannelModel;
import hust.tools.csc.util.ConfusionSet;
import hust.tools.csc.util.Dictionary;
import hust.tools.csc.util.FormatConvert;
import hust.tools.csc.util.Sentence;
import hust.tools.csc.wordseg.AbstractWordSegment;
import hust.tools.csc.wordseg.CKIPWordSegment;
import hust.tools.ngram.io.ARPATextFileNGramModleReader;
import hust.tools.ngram.io.BinaryFileNGramModelReader;
import hust.tools.ngram.io.TextFileNGramModelReader;
import hust.tools.ngram.model.AbstractNGramModelReader;

public class SCAUChecker {

	private static Dictionary dictionary;
	private static ConfusionSet confusionSet;
	private static NGramModel nGramModel;
	private static AbstractWordSegment wordSegment;
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		String dictFile = "";
		String similarityPronunciation = "similarityPronunciation.txt";
		String similarityShape = "similarityShape.txt";
		
		String lmFile = "";
		String testFile = "";
		
		constructDict(new File(dictFile));
		constructConfusionSet(new File(similarityPronunciation), new File(similarityShape));
		nGramModel = loadModel(lmFile);
		wordSegment = new CKIPWordSegment();
		
		AbstractNoisyChannelModel noisyChannelModel = new SCAUNoisyChannelModel(nGramModel, confusionSet, wordSegment);
		Detector detector = new HUSTDetector(noisyChannelModel);
		
		List<Sentence> sentences = FileOperator.readFile(testFile, "utf-8");
		
		
		for(Sentence sentence : sentences) {
			System.out.println(detector.detect(sentence));
		}
	}
	
	/**
	 * 根据训练语料统计字频
	 * @param file			训练语料文件
	 * @return				字频
	 * @throws IOException
	 */
	private static void constructDict(File file) throws IOException {
		HashMap<String, Integer> map = new HashMap<>();
		dictionary = new Dictionary(map);
		
		InputStreamReader in = new InputStreamReader(new FileInputStream(file));
		BufferedReader bReader = new BufferedReader(in);
		
		String line = "";
		while ((line = bReader.readLine()) != null) {
			line = FormatConvert.ToDBC(line).replace("\\s+", "").trim();
			if(!line.equals("")) {
				String[] characters = line.split("");
				
				for(String character : characters)
					if(FormatConvert.isHanzi(character))
						dictionary.add(character);
			}
		}
		bReader.close();
	}
	
	/**
	 * 根据困惑集构造困惑矩阵
	 * @param file			混淆集文件
	 * @return				混淆矩阵
	 * @throws FileNotFoundException 
	 * @throws IOException
	 */
	private static void constructConfusionSet(File similarityPronunciation, File similarityShape) throws IOException {
		HashMap<String, HashSet<String>> Pronunciation = new HashMap<>();
		HashMap<String, HashSet<String>> Shape = new HashMap<>();
		confusionSet = new ConfusionSet(Pronunciation, Shape);
		
		InputStreamReader pronunciation = new InputStreamReader(new FileInputStream(similarityPronunciation));
		InputStreamReader shape = new InputStreamReader(new FileInputStream(similarityPronunciation));
		BufferedReader proReader = new BufferedReader(pronunciation);
		BufferedReader shapeReader = new BufferedReader(shape);
		
		String line = "";
		try {
			while ((line = proReader.readLine())!= null) {
				line = FormatConvert.ToDBC(line).replace("\\s+", "").trim();
				if(!line.equals("")) {
					String[] strings = line.split("");
					confusionSet.addSimilarityPronunciations(strings);
				}
			}
			proReader.close();
			
			while ((line = shapeReader.readLine())!= null) {
				line = FormatConvert.ToDBC(line).replace("\\s+", "").trim();
				if(!line.equals("")) {
					String[] sentence = FormatConvert.ToDBC(line).split(",");
					String key = sentence[0];
					String[] values = sentence[1].split("");
					HashSet<String> set = new HashSet<>();
					for(int i = 0; i < values.length; i++) 
						set.add(values[i]);
					
					confusionSet.addSimilarityShapes(key, set);
				}
			}
			shapeReader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
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
}
