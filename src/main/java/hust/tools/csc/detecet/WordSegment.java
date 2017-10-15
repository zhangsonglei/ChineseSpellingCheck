package hust.tools.csc.detecet;

import tw.cheyingwu.ckip.CKIP;
import tw.cheyingwu.ckip.WordSegmentationService;

public class WordSegment {

	private static WordSegmentationService service;
	
	public static void main(String[] args) {
		service = new CKIP("140.109.19.104", 1501, "zsl", "zsl");
		String testString = "台新金控12月3日將召開股東臨時會進行董監改選。";
		service.setRawText(testString);
		
		service.send();
		System.out.println(service.getReturnText());
	}
}
