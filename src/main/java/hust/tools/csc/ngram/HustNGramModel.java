package hust.tools.csc.ngram;

import java.io.File;
import java.io.IOException;

import hust.tools.csc.util.Sentence;
import hust.tools.ngram.io.BinaryFileNGramModelWriter;
import hust.tools.ngram.model.NGramLanguageModel;
import hust.tools.ngram.utils.Gram;
import hust.tools.ngram.utils.StringGram;

/**
 *<ul>
 *<li>Description: 自己的n元模型
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月24日
 *</ul>
 */
public class HustNGramModel implements NGramModel {
	
	private NGramLanguageModel nGramModel;
	
	public HustNGramModel(NGramLanguageModel nGramModel) throws IOException {
		this.nGramModel = nGramModel;
	}

	@Override
	public double getSentenceLogProb(Sentence sentence, int n) {
		Gram[] sequence = new StringGram[sentence.size()];
		for(int i=0; i< sentence.size(); i++)
			sequence[i] = new StringGram(sentence.getToken(i));
		
		return nGramModel.getSequenceLogProbability(sequence, getOrder(), true);
	}

	@Override
	public int getOrder() {
		return nGramModel.getOrder();
	}

	@Override
	public double getNGramLogProb(String[] strs, int order) {
		Gram[] grams = new StringGram[strs.length];
		for(int i=0; i< strs.length; i++)
			grams[i] = new StringGram(strs[i]);
		
		return nGramModel.getSequenceLogProbability(grams, order, false);
	}
	
	/**
	 * 写ngram模型到指定路径
	 * @param path	写入路径
	 * @throws IOException 
	 */
	public void writeLM(String path) throws IOException {
		BinaryFileNGramModelWriter lmWriter = new BinaryFileNGramModelWriter(nGramModel, new File(path));
		lmWriter.persist();
	}
}
