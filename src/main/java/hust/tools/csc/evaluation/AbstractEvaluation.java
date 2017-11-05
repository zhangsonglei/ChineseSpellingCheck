package hust.tools.csc.evaluation;

import java.util.HashSet;

/**
 *<ul>
 *<li>Description: 评价器的抽象类 
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年11月2日
 *</ul>
 */
public abstract class AbstractEvaluation implements Evaluation {
	
	//////////////////////////////////////////////////////错误率指标
	@Override
	public double getFalsePositiveRate() {
		HashSet<Integer> numerator = getFP();	//分子
		
		HashSet<Integer> denominator = new HashSet<>();	//分母
		denominator.addAll(numerator);
		denominator.addAll(getTN());
		
		return 1.0 * numerator.size() / denominator.size();
	}

	//////////////////////////////////////////////////////检测指标
	@Override
	public double getDetectAccuracy() {
		HashSet<Integer> numerator = getDTN();	//分子
		numerator.addAll(getDTP());
		
		HashSet<Integer> denominator = new HashSet<>();	//分母
		denominator.addAll(numerator);
		denominator.addAll(getDFN());
		denominator.addAll(getDFP());
				
		return 1.0 * numerator.size() / denominator.size();
	}
	
	@Override
	public double getDetectPrecision() {
		HashSet<Integer> numerator = getDTP();	//分子
		
		HashSet<Integer> denominator = new HashSet<>();	//分母
		denominator.addAll(numerator);
		denominator.addAll(getDFP());
		
		return 1.0 * numerator.size() / denominator.size();
	}
	
	@Override
	public double getDetectRecall() {
		HashSet<Integer> numerator = getDTP();	//分子
		
		HashSet<Integer> denominator = new HashSet<>();	//分母
		denominator.addAll(numerator);
		denominator.addAll(getDFN());
		
		return 1.0 * numerator.size() / denominator.size();
	}
	
	@Override
	public double getDetectF() {
		return 2.0 * getDetectPrecision() * getDetectRecall() / (getDetectPrecision() + getDetectRecall());
	}
	
	//////////////////////////////////////////////////////纠正指标
	@Override
	public double getCorrectAccuracy() {
		HashSet<Integer> numerator = getCTP();	//分子
		numerator.addAll(getCTN());
		
		HashSet<Integer> denominator = new HashSet<>();	//分母
		denominator.addAll(numerator);
		denominator.addAll(getCFN());
		denominator.addAll(getCFP());
		
		return 1.0 * numerator.size() / denominator.size();
	}
	
	@Override
	public double getCorrectPrecision() {
		HashSet<Integer> numerator = getCTP();	//分子
		
		HashSet<Integer> denominator = new HashSet<>();	//分母
		denominator.addAll(numerator);
		denominator.addAll(getCFP());
		
		return 1.0 * numerator.size() / denominator.size();
	}
	
	@Override
	public double getCorrectRecall() {
		HashSet<Integer> numerator = getCTP();	//分子
		HashSet<Integer> denominator = getCFN();	//分母
		denominator.addAll(getCTP());
		
		return 1.0 * numerator.size() / denominator.size();
	}
	
	@Override
	public double getCorrectF() {
		return 2.0 * getCorrectPrecision() * getCorrectRecall() / (getCorrectPrecision() + getCorrectRecall());
	}
	
	//////////////////////////////////////////////////////错误率指标混淆矩阵
	
	/**
	 * 返回黄金标准没有纠正的句子中，系统也没有纠正的句子位置索引（等同于CTN）
	 * @return	黄金标准没有纠正的句子中，系统也没有纠正的句子位置索引
	 */	
	protected abstract HashSet<Integer> getTN();

	/**
	 * 返回系统进行纠正的句子中，黄金标准没有进行纠正的句子位置索引
	 * @return	系统进行纠正的句子中，黄金标准没有进行纠正的句子位置索引
	 */
	protected abstract HashSet<Integer> getFP();
	
	//////////////////////////////////////////////////////检测指标混淆矩阵
	/**
	 * 返回所有有错句子中错字被全部正确标记的句子位置索引
	 * @return	所有有错句子中错字被全部正确标记的句子位置索引
	 */
	protected abstract HashSet<Integer> getDTP();
	
	/**
	 * 返回所有句子中无错被标记为错误的句子位置索引
	 * @return	所有句子中无错被标记为错误的句子位置索引
	 */
	protected abstract HashSet<Integer> getDFP();
	
	/**
	 * 返回所有无错句子中被标记为无错的句子位置索引
	 * @return	所有无错句子中被标记为无错的句子位置索引
	 */
	protected abstract HashSet<Integer> getDTN();
	
	/**
	 * 返回所有有错句子中的错字被标记为无错的句子位置索引
	 * @return	所有有错句子中的错字被标记为无错的句子位置索引
	 */
	protected abstract HashSet<Integer> getDFN();
	
	//////////////////////////////////////////////////////纠正指标混淆矩阵
	/**
	 * 返回系统进行了纠正的句子中与黄金标准一样的句子位置索引
	 * @return	系统进行了纠正的句子中与黄金标准一样的句子位置索引
	 */
	protected abstract HashSet<Integer> getCTP();
	
	/**
	 * 返回系统进行了纠正的句子中与黄金标准不一样的句子位置索引
	 * @return	系统进行了纠正的句子中与黄金标准不一样的句子位置索引
	 */
	protected abstract HashSet<Integer> getCFP();
	
	/**
	 * 返回黄金标准进行了纠正的句子中与系统不一样的句子位置索引
	 * @return	黄金标准进行了纠正的句子中与系统不一样的句子位置索引
	 */
	protected abstract HashSet<Integer> getCFN();
	
	/**
	 * 返回黄金标准没有纠正的句子中系统同样没有纠正的的句子位置索引
	 * @return	黄金标准没有纠正的句子中系统同样没有纠正的的句子位置索引
	 */
	protected abstract HashSet<Integer> getCTN();
}
