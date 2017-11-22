package hust.tools.csc.evaluation;

import java.util.ArrayList;
import java.util.HashSet;

import hust.tools.csc.util.Sentence;

/**
 *<ul>
 *<li>Description: 评价器的实现类 
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年11月2日
 *</ul>
 */
public class CSCEvaluator extends AbstractEvaluation {

	private ArrayList<Sentence> original;	//原始测试语料
	private ArrayList<Sentence> gold;		//黄金标准
	private ArrayList<Sentence> result;		//纠正结果
	
	private ArrayList<Integer> errorSentencesLocations;		//存在错误的句子的位置索引
	private ArrayList<Integer> correctedSentencesLocations;	//修改过的句子的位置索引
	
	public CSCEvaluator(ArrayList<Sentence> original, ArrayList<Sentence> gold, ArrayList<Sentence> result) {
		this.original = original;
		this.gold = gold;
		this.result = result;
		
		validate();
	}

	/**
	 * 评测前检测数据的合法性
	 */
	private void validate() {
		int originalSize = original.size();
		int goldSize = gold.size();
		int resultSize = result.size();
		
		if((originalSize * goldSize * resultSize) != 0) {
			if(originalSize == goldSize && originalSize == resultSize && resultSize == goldSize) {
				int index = 0;
				errorSentencesLocations = new ArrayList<>();
				correctedSentencesLocations = new ArrayList<>();
				for(Sentence sentence : original) {
					if(!sentence.equals(gold.get(index)))
						errorSentencesLocations.add(index);
					
					if(!sentence.equals(result.get(index)))
						correctedSentencesLocations.add(index);
					index++;
				}
				
			}else {
				System.err.println("数据量不等！\noriginalSize = " + originalSize + "goldSize = " + goldSize + "resultSize = " + resultSize);
				System.exit(0);
			}
		}else {		
			System.err.println("有空值！\noriginalSize = " + originalSize + "\tgoldSize = " + goldSize + "\tresultSize = " + resultSize);
			System.exit(0);
		}
	}

	@Override
	protected HashSet<Integer> getDTP() {
		HashSet<Integer> temp = new HashSet<>();
		for(int index : errorSentencesLocations) {	//遍历黄金标准中的错误句
			Sentence sentence = original.get(index);
			Sentence errorSentence = gold.get(index);
			Sentence resultSentence = result.get(index);
			
			ArrayList<Integer> errorLocations = new ArrayList<>();	//找出错误字的位置并存储
			for(int i = 0; i < errorSentence.size(); i++) {	//遍历错误句的每一个字
				if(!errorSentence.getToken(i).equals(sentence.getToken(i))) 
					errorLocations.add(i);
			}
			
			int count = 0;
			for(int j : errorLocations) {	//记录该句子所有错误字的位置，被系统检测出个数
				if(!resultSentence.getToken(j).equals(sentence.getToken(j)))
					count++;
			}

			if(count == errorLocations.size())
				temp.add(index);
		}
		
		return temp;
	}

	@Override
	protected HashSet<Integer> getDFP() {
		HashSet<Integer> temp = new HashSet<>();
		for(int index : correctedSentencesLocations) {	//遍历系统检测结果中的错误句
			Sentence sentence = original.get(index);
			Sentence errorSentence = gold.get(index);
			Sentence resultSentence = result.get(index);
			
			ArrayList<Integer> reslutLocations = new ArrayList<>();	//找出错误字的位置并存储
			for(int i = 0; i < resultSentence.size(); i++) {	//遍历错误句的每一个字
				if(!resultSentence.getToken(i).equals(sentence.getToken(i))) 
					reslutLocations.add(i);
			}	
			
			int count = 0;
			for(int j : reslutLocations) {	//记录系统给出的所有错误字的位置，黄金标准是否也认为有错的个数
				if(!errorSentence.getToken(j).equals(sentence.getToken(j)))
				count++;
			}

			if(count != reslutLocations.size())
				temp.add(index);
		}
		
		return temp;
	}

	@Override
	protected HashSet<Integer> getDTN() {
		HashSet<Integer> temp = new HashSet<>();
		for(int index = 0; index < original.size(); index++) {
			if(errorSentencesLocations.contains(index))
				continue;
			
			if(result.get(index).equals(original.get(index)))
				temp.add(index);
		}
		
		return temp;
	}

	@Override
	protected HashSet<Integer> getDFN() {
		HashSet<Integer> temp = new HashSet<>();
		for(int index : errorSentencesLocations) {	//遍历黄金标准中的错误句
			Sentence sentence = original.get(index);
			Sentence errorSentence = gold.get(index);
			Sentence resultSentence = result.get(index);
			
			ArrayList<Integer> errorLocations = new ArrayList<>();	//找出错误字的位置并存储
			for(int i = 0; i < errorSentence.size(); i++) {	//遍历错误句的每一个字
				if(!errorSentence.getToken(i).equals(sentence.getToken(i))) 
					errorLocations.add(i);
			}
			
			int count = 0;
			for(int j : errorLocations) {	//记录该句子所有错误字的位置，被系统检测出个数
				if(resultSentence.getToken(j).equals(sentence.getToken(j)))
					count++;
			}

			if(count > 0)
				temp.add(index);
		}
		
		return temp;
	}

	@Override
	protected HashSet<Integer> getCTP() {
		HashSet<Integer> temp = new HashSet<>();
		for(int index : correctedSentencesLocations) {
			if(result.get(index).equals(gold.get(index)))
				temp.add(index);
		}
		
		return temp;
	}
	
	@Override
	protected HashSet<Integer> getCFP() {
		HashSet<Integer> temp = new HashSet<>();
		for(int index : correctedSentencesLocations) {
			if(!result.get(index).equals(gold.get(index)))
				temp.add(index);
		}
		
		return temp;
	}

	@Override
	protected HashSet<Integer> getCTN() {
		HashSet<Integer> temp = new HashSet<>();
		for(int index = 0; index < original.size(); index++) {
			if(errorSentencesLocations.contains(index))
				continue;
			
			if(original.get(index).equals(result.get(index)))
				temp.add(index);
		}
		
		return temp;
	}

	@Override
	protected HashSet<Integer> getCFN() {
		HashSet<Integer> temp = new HashSet<>();
		for(int index : errorSentencesLocations) {
			if(!gold.get(index).equals(result.get(index)))
				temp.add(index);
		}
	
		return temp;
	}

	@Override
	protected HashSet<Integer> getFP() {
		HashSet<Integer> temp = new HashSet<>();
		for(int index : correctedSentencesLocations) {
			if(gold.get(index).equals(original.get(index)))
				temp.add(index);
		}
		
		return temp;
	}

	@Override
	protected HashSet<Integer> getTN() {
		return getCTN();
	}
	
	@Override
	public String show() {
		String output = "";
		output += "FPR = " + getFalsePositiveRate() +"\n";
		output += "DA = " + getDetectAccuracy() +"\n";
		output += "DP = " + getDetectPrecision() +"\n";
		output += "DR = " + getDetectRecall() +"\n";
		output += "DF = " + getDetectF() +"\n";
		output += "CA = " + getCorrectAccuracy() +"\n";
		output += "CP = " + getCorrectPrecision() +"\n";
		output += "CR = " + getCorrectRecall() +"\n";
		output += "CF = " + getCorrectF() +"\n";
	
		System.out.println(output);
		return output;
	}
}
