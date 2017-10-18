package hust.tools.csc.wordseg;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import hust.tools.csc.util.FormatConvert;
import hust.tools.csc.util.Sentence;
import tw.cheyingwu.ckip.CKIP;
import tw.cheyingwu.ckip.WordSegmentationService;

/**
 *<ul>
 *<li>Description: 基于Socket的CKIP中文分词 
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月18日
 *</ul>
 */
public class CKIPWordSegment extends AbstractWordSegment {
	
	public CKIPWordSegment(Sentence sentence) {
		super(sentence);
	}

	@Override
	public String[] segment() {
		WordSegmentationService service = new CKIP("140.109.19.104", 1501, "zsl", "zsl");
		service.setRawText(sentence.toString());
		service.send();
		
		return parseToWords(service.getReturnText());
	}
	
	/**
	 * 将CKIP分词标注器返回的文本解析成词组
	 * @param text	CKIP分词标注的返回结果
	 * @return		词组
	 */
	private String[] parseToWords(String xmlText) {
		String[] words = null;
		
		//解析CKIP分词标注器的返回结果
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();   
			DocumentBuilder builder = factory.newDocumentBuilder();   
			Document doc = builder.parse(new InputSource(new StringReader(xmlText)));   
	  
	        Element root = doc.getDocumentElement();
	        NodeList books = root.getChildNodes();  
	        
	        String sen = books.item(1).getChildNodes().item(0).getFirstChild().getNodeValue().trim();
	        sen = FormatConvert.ToDBC(sen).trim();	//将全角格式转为半角格式
	        words = sen.split("\\s+");
	        
	        for(int i = 0; i < words.length; i++) 
	        	words[i] = words[i].split("\\(")[0];
	       
		} catch (Exception e) {   
			e.printStackTrace();   
		}
		
		return words;
	}
}
