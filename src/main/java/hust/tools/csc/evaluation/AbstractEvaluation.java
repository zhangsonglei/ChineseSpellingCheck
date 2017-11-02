package hust.tools.csc.evaluation;

/**
 *<ul>
 *<li>Description: 评价器的抽象类 
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年11月2日
 *</ul>
 */
public abstract class AbstractEvaluation implements Evaluation {
	
	@Override
	public double getFalsePositiveRate() {
		return 1.0 * getDFP() / (getDFP() + getDTN());
	}
	
	//////////////////////////////////////////////////////检测指标
	@Override
	public double getDetectAccuracy() {
		return 1.0 * (getDTP() + getDTN()) / (getDTP() + getDTN() + getDFP() + getDFN());
	}
	
	@Override
	public double getDetectPrecision() {
		return 1.0 * getDTP() / (getDFP() + getDTP());
	}
	
	@Override
	public double getDetectRecall() {
		return 1.0 * getDTP() / (getDFN() + getDTP());
	}
	
	@Override
	public double getDetectF() {
		return 2.0 * getDetectPrecision() * getDetectRecall() / (getDetectPrecision() + getDetectRecall());
	}
	
	//////////////////////////////////////////////////////纠正指标
	@Override
	public double getCorrectAccuracy() {
		return 1.0 * (getCTP() + getCTN()) / (getCTP() + getCTN() + getCFP() + getCFN());
	}
	
	@Override
	public double getCorrectPrecision() {
		return 1.0 * getCTP() / (getCFP() + getCTP());
	}
	
	@Override
	public double getCorrectRecall() {
		return 1.0 * getCTP() / (getCFN() + getCTP());
	}
	
	@Override
	public double getCorrectF() {
		return 2.0 * getCorrectPrecision() * getCorrectRecall() / (getCorrectPrecision() + getCorrectRecall());
	}
	
	//////////////////////////////////////////////////////检测混淆矩阵
	/**
	 * 返回错字的被正确标记的句子个数
	 * @return	错字的被正确标记的句子个数
	 */
	protected abstract int getDTP();
	
	/**
	 * 返回无错字被标记为错误的句子个数
	 * @return	无错字被标记为错误的句子个数
	 */
	protected abstract int getDFP();
	
	/**
	 * 返回无错的字被标记为无错的句子个数
	 * @return	无错的字被标记为无错的句子个数
	 */
	protected abstract int getDTN();
	
	/**
	 * 返回错字被标记为无错的句子个数
	 * @return	错字被标记为无错的句子个数
	 */
	protected abstract int getDFN();
	
	//////////////////////////////////////////////////////纠正混淆矩阵
	/**
	 * 返回有错的句子被正确纠正的个数
	 * @return	有错的句子被正确纠正的个数
	 */
	protected abstract int getCTP();
	
	/**
	 * 返回无错的句子被修改的个数
	 * @return	返回无错的句子被修改的个数
	 */
	protected abstract int getCFP();
	
	/**
	 * 返回无错的句子没有被修改的个数
	 * @return	无错的句子没有被修改的个数
	 */
	protected abstract int getCTN();
	
	/**
	 * 返回有错的句子被纠正错误的个数
	 * @return	有错的句子被纠正错误的个数
	 */
	protected abstract int getCFN();
}
