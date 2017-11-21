package hust.tools.csc.checker;

import java.util.ArrayList;
import java.util.Iterator;

import hust.tools.csc.correct.CorrectResult;
import hust.tools.csc.correct.Corrector;
import hust.tools.csc.detecet.DetectResult;
import hust.tools.csc.detecet.Detector;
import hust.tools.csc.evaluation.CSCEvaluator;
import hust.tools.csc.evaluation.Evaluation;
import hust.tools.csc.util.Sentence;

public class ChineseSpellCheckModel {
	
	private Detector detector;		//检测器
	private Corrector corrector;	//纠正器

	public ChineseSpellCheckModel(Detector detector, Corrector corrector) {
		this.detector = detector;
		this.corrector = corrector;
	}
	
	/**
	 * 返回给定句子第n个候选结果中错误字索引
	 * @param sentence	待检测的句子
	 * @param n			第n个候选结果
	 * @return			第n个候选结果中错误字索引
	 */
	public int[] getErrorLocation(Sentence sentence) {
		DetectResult result = detector.detect(sentence);
		return result.getErrorLocation(0);
	}
	
	/**
	 * 返回给定位置的建议字迭代器，已经按照字的优先级排好序， 若该处无错，返回null
	 * @param sentence	错句
	 * @param index		错句的第index个字
	 * @return			建议字迭代器，已经按照字的优先级排好序， 若该处无错，返回null
	 */
	public Iterator<String> getSuggestions(Sentence sentence, int index) {
		CorrectResult result = corrector.correct(sentence);
		return result.get(index - 1).iterator();
	}
	
	/**
	 * 返回系统纠正后的句子
	 * @param sentence	待纠正的句子
	 * @return			纠正后的句子
	 */
	public Sentence correct(Sentence sentence) {
		return corrector.autoCorrect(sentence);
	}
	
	/**
	 * 打印系统的系能指标
	 * @param tests	测试语料
	 * @param golds	黄金标准
	 */
	public void evaluation(ArrayList<Sentence> tests, ArrayList<Sentence> golds) {
		ArrayList<Sentence> result = new ArrayList<>();
		
		for(Sentence sentence : tests) {
			result.add(correct(sentence));
		}
		
		Evaluation evaluation = new CSCEvaluator(tests, golds, result);
		evaluation.show();
	}
}
