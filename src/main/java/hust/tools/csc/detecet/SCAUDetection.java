package hust.tools.csc.detecet;

public class SCAUDetection implements Detecetion {

	private SCAUDetector detector;
	
	public SCAUDetection(SCAUDetector detector) {
		this.detector = detector;
	}
	
	@Override
	public int[] getErrorCharacterLocations() {
		return null;
	}

	@Override
	public String[] getErrorCharacters() {
		return null;
	}
}
