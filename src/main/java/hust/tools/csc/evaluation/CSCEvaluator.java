package hust.tools.csc.evaluation;

import java.util.ArrayList;
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
			System.err.println("有空值！\noriginalSize = " + originalSize + "goldSize = " + goldSize + "resultSize = " + resultSize);
			System.exit(0);
		}
	}

	@Override
	protected int getDTP() { 		
		return 0;
	}

	@Override
	protected int getDFP() {
		return 0;
	}

	@Override
	protected int getDTN() {
		return 0;
	}

	@Override
	protected int getDFN() {
		return 0;
	}

	@Override
	protected int getCTP() {
		return 0;
	}

	@Override
	protected int getCFP() {
		return 0;
	}

	@Override
	protected int getCTN() {
		return 0;
	}

	@Override
	protected int getCFN() {
		return 0;
	}
}
