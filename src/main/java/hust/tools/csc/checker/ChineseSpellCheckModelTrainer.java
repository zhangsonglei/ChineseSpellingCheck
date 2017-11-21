package hust.tools.csc.checker;

import hust.tools.csc.correct.Corrector;
import hust.tools.csc.correct.HUSTCorrector;
import hust.tools.csc.detecet.Detector;
import hust.tools.csc.detecet.HUSTDetector;
import hust.tools.csc.score.AbstractNoisyChannelModel;

public class ChineseSpellCheckModelTrainer {
	
	private AbstractNoisyChannelModel noisyChannelModel;

	public ChineseSpellCheckModelTrainer(AbstractNoisyChannelModel noisyChannelModel) {
		this.noisyChannelModel = noisyChannelModel;
	}
	
	public Detector trainDetector() {
		return new HUSTDetector(noisyChannelModel);
	}
	
	public Corrector trainCorrector() {
		return new HUSTCorrector(noisyChannelModel);
	}
	
	public ChineseSpellCheckModel checkModel() {
		Detector detector = trainDetector();
		Corrector corrector = trainCorrector();
		
		return new ChineseSpellCheckModel(detector, corrector);
	}
}