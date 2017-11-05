package hust.tools.csc.evaluation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import hust.tools.csc.util.FormatConvert;
import hust.tools.csc.util.Sentence;

public class APP {
	
	public static void main(String[] args) throws IOException {
		String originalFile = "E:\\JOB\\TestData\\testFile.txt";
		String goldFile = "E:\\JOB\\TestData\\goldFile.txt";
		String resultFile = "E:\\JOB\\TestData\\SCAUresult.txt";
		
		ArrayList<Sentence> original = readFile(originalFile, "utf-8");
		ArrayList<Sentence> gold = readFile(goldFile, "utf-8");
		ArrayList<Sentence> result = readFile(resultFile, "utf-8");;
		Evaluation evaluator = new CSCEvaluator(original, gold, result);
		
		System.out.println("FPR = " + evaluator.getFalsePositiveRate());
		System.out.println("DA = " + evaluator.getDetectAccuracy());
		System.out.println("DP = " + evaluator.getDetectPrecision());
		System.out.println("DR = " + evaluator.getDetectRecall());
		System.out.println("DF = " + evaluator.getDetectF());
		System.out.println("CA = " + evaluator.getCorrectAccuracy());
		System.out.println("CP = " + evaluator.getCorrectPrecision());
		System.out.println("CR = " + evaluator.getCorrectRecall());
		System.out.println("CF = " + evaluator.getCorrectF());
	}
	
	private static ArrayList<Sentence> readFile(String path, String encoding) throws IOException {
		ArrayList<Sentence> list = new ArrayList<Sentence>();
		File file = new File(path);
		
		if(file.isFile() && file.exists()) {
			InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), encoding);
			BufferedReader reader = new BufferedReader(inputStreamReader);
			
			String line = "";
			while((line = reader.readLine()) != null) {
				line = FormatConvert.ToDBC(line).trim();
				String[] chs = line.split("");
				
				list.add(new Sentence(chs));
			}
			reader.close();
		}else {
			System.err.println("File:\""+path+"\" read failed!");
		}
		
		return list;	
	}
}
