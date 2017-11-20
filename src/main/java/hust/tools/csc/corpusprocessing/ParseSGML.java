package hust.tools.csc.corpusprocessing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import hust.tools.csc.util.FileOperator;
import hust.tools.csc.util.Sentence;

/**
 *<ul>
 *<li>Description: 解析SIGHAN2015 SGML格式语料， 提取有用的文本
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年11月14日
 *</ul>
 */
public class ParseSGML {
	
	private static HashMap<String, Sentence> map;
	
	public static void parse(String inputPath, String encoding, String outPath, boolean hasId) throws IOException {
		List<String> list = FileOperator.readSGMLFile(inputPath, encoding);
		
		map = new HashMap<>();
		String sgml = "";
		for(String line : list) {
			sgml += line;
			if(line.equals("</ESSAY>")) {
				process(sgml);
				sgml = "";
			}
		}
		
		OutputStreamWriter oWriter = new OutputStreamWriter(new FileOutputStream(new File(outPath)), encoding);
		BufferedWriter writer = new BufferedWriter(oWriter);
		
		for(Entry<String, Sentence> entry : map.entrySet()) {
			if(hasId) {
				writer.write(entry.getKey() + "\t" + entry.getValue());
				writer.newLine();
			}else {
				writer.write(entry.getValue().toString());
				writer.newLine();
			}
		}
		writer.close();
	}
	
	/**
	 * 提取文件中的有用文本信息（句子id和句子，错误id和错误词以及对应的纠正词）
	 * @param sgml
	 */
	private static void process(String sgml) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();   
			DocumentBuilder builder = factory.newDocumentBuilder();   
			Document doc = builder.parse(new InputSource(new StringReader(sgml)));   
	  
	        Element root = doc.getDocumentElement();
	        int nodeCounts = root.getChildNodes().getLength();
	        NodeList nodeList = root.getChildNodes().item(0).getChildNodes();
	        NamedNodeMap nodeMap = null;
	        Node node = null;
	        for(int i = 0; i < nodeList.getLength(); i++) {
	        	node = nodeList.item(i);
	        	nodeMap = node.getAttributes();
	        	String sen = node.getFirstChild().getNodeValue();
	        	map.put(nodeMap.getNamedItem("id").toString(), new Sentence(sen.split("")));
	        }
	        
	        for(int i = 1; i < nodeCounts; i++) {
	        	nodeList = root.getChildNodes().item(i).getChildNodes(); 
	        	nodeMap = root.getChildNodes().item(i).getAttributes();
	        	String id = nodeMap.getNamedItem("id").toString();
	        	String location = nodeMap.getNamedItem("location").toString();
	        	
	        	String wrong = nodeList.item(0).getFirstChild().getNodeValue();
	        	String correction = nodeList.item(1).getFirstChild().getNodeValue();

	        	update(id, wrong, correction, location);
	        }
	       
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 * @param id
	 * @param wrong
	 * @param correction
	 * @param location
	 */
	private static void update(String id, String wrong, String correction, String location) {
		if(map.containsKey(id)) {
			Sentence sentence = map.get(id);
			
			int index = Integer.parseInt(location.split("\"")[1].split("\"")[0]) - 1;
			if(index < sentence.size()) {
				String charcter = sentence.getToken(index);
				int temp = wrong.indexOf(charcter);
//				System.out.println(id+"\t"+index);
				charcter = correction.substring(temp, temp+1);
				sentence = sentence.setToken(index, charcter);
			}
			map.put(id, sentence);
		}
	}
	
	public static void main(String[] args) throws IOException {
		String inputPath = "E:\\JOB\\TestData\\B2_Training.sgml";
		String outPath = "E:\\JOB\\TestData\\B2_Training.txt";
		String encoding = "utf-8";
		
		parse(inputPath, encoding, outPath, false);
	}
}
