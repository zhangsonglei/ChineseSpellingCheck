package hust.tools.csc.evaluation;

/**
 *<ul>
 *<li>Description: 评价指标接口 
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年11月2日
 *</ul>
 */
public interface Evaluation {

	/**
	 * 
	 * @return
	 */
	public double getFalsePositiveRate();
	
	/**
	 * 检测的准确率
	 * @return	检测的准确率
	 */
	public double getDetectAccuracy();
	
	/**
	 * 检测的精确率
	 * @return	检测的精确率
	 */
	public double getDetectPrecision();
	
	/**
	 * 检测的召回率
	 * @return	检测的召回率
	 */
	public double getDetectRecall();
	
	/**
	 * 检测的F值
	 * @return	检测的F值
	 */
	public double getDetectF();
	
	/**
	 * 纠正的准确率
	 * @return	纠正的准确率
	 */
	public double getCorrectAccuracy();
	
	/**
	 * 纠正的精确率
	 * @return	纠正的精确率
	 */
	public double getCorrectPrecision();
	
	/**
	 * 纠正的召回率
	 * @return	纠正的召回率
	 */
	public double getCorrectRecall();
	
	/**
	 * 纠正的F值
	 * @return	纠正的F值
	 */
	public double getCorrectF();
}
