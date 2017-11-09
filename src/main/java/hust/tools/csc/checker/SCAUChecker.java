package hust.tools.csc.checker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
import hust.tools.csc.util.FormatConvert;
import hust.tools.csc.util.Sentence;
import hust.tools.csc.wordseg.AbstractWordSegment;
import hust.tools.csc.wordseg.CKIPWordSegment;
import hust.tools.ngram.io.ARPATextFileNGramModleReader;
import hust.tools.ngram.io.BinaryFileNGramModelReader;
import hust.tools.ngram.io.TextFileNGramModelReader;
import hust.tools.ngram.model.AbstractNGramModelReader;

public class SCAUChecker {
	
	private static ConfusionSet confusionSet;
	private static NGramModel nGramModel;
	private static AbstractWordSegment wordSegment;
		
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		String similarityPronunciation = "E:\\JOB\\TestData\\pro.txt";
		String similarityShape = "E:\\JOB\\TestData\\shape.txt";
		String lmFile = "E:\\JOB\\TestData\\knLM.bin";
		String testFile = "E:\\JOB\\TestData\\TestInput.txt";
		String result = "E:\\JOB\\TestData\\SCAUresult.txt";
		
		constructConfusionSet(new File(similarityPronunciation), new File(similarityShape));
		nGramModel = loadModel(lmFile);
		wordSegment = new CKIPWordSegment();
		
		AbstractNoisyChannelModel noisyChannelModel = new SCAUNoisyChannelModel(nGramModel, confusionSet, wordSegment);
		
		List<Sentence> sentences = readFile(testFile, "utf-8");
		OutputStreamWriter oWriter = new OutputStreamWriter(new FileOutputStream(new File(result)), "utf-8");
		BufferedWriter writer = new BufferedWriter(oWriter);
		
		int no = 1;
		for(Sentence sentence : sentences) {
			System.out.println(no++);
			ArrayList<Sentence> cands = noisyChannelModel.getCorrectSentence(sentence);
			Sentence bestSentence = cands.get(0);
			
			writer.write(bestSentence.toString());
			writer.newLine();
		}
		writer.close();
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
		
		InputStreamReader pronunciation = new InputStreamReader(new FileInputStream(similarityPronunciation), "utf-8");
		InputStreamReader shape = new InputStreamReader(new FileInputStream(similarityShape), "utf-8");
		BufferedReader proReader = new BufferedReader(pronunciation);
		BufferedReader shapeReader = new BufferedReader(shape);
		String line = "";
		try {
			while ((line = proReader.readLine())!= null) {
				
				line = FormatConvert.ToDBC(line);		//全角转为半角
				line = line.replace("\t", "").trim();	//去除多于的空格
				if(!line.equals("")) {					//过滤空行
					String[] strings = line.split("");
					confusionSet.addSimilarityPronunciations(strings);
				}
			}
			proReader.close();
			
			while ((line = shapeReader.readLine())!= null) {
				line = FormatConvert.ToDBC(line);		//全角转为半角
				line = line.replace("\\s+", "").trim();	//去除多于的空格
				if(!line.equals("")) {					//过滤空行
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
					String[] sentence = FormatConvert.ToDBC(line).split("");
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
