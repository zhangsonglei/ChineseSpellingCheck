package hust.tools.csc.corpusprocessing;

import java.io.IOException;
import java.util.ArrayList;

import hust.tools.csc.util.CommonUtils;
import hust.tools.csc.util.FileOperator;

/**
 *<ul>
 *<li>Description:  
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年11月28日
 *</ul>
 */
public class ConvertConfusionSet {
	
	public static void main(String[] args) throws IOException {
		int len = args.length;
		if(len != 5) {
			System.err.println("错误的参数个数： "+len + "\n示例: 混淆集文件  输入文件编码  转换操作(toSim/toTra)  输出路径  输出文件编码");
			System.exit(0);
		}
		
		String input = args[0];
		String inEncoding = args[1];
		
		String operate = args[2];	//转换操作(toSim——繁体转简体/toTra——繁体转简体)
		if(!operate.equals("toSim") && !operate.equals("toTra")) {
			System.err.println("错误的操作类型： "+ operate + "\n示例: toSim——繁体转简体\ntoTra——繁体转简体)");
			System.exit(0);
		}
		
		String output = args[3];
		String outEncoding = args[4];
		
		ArrayList<String> original = FileOperator.readConfusionSetFile(input, inEncoding);
		ArrayList<String> result = convert(original, operate);
		FileOperator.writeFile(result, output, outEncoding);
	}

	/**
	 * 返回转换后的字符串列表
	 * @param original	待转换的字符串列表
	 * @param operate	转换方式
	 * @return			转换后的字符串列表
	 */
	private static ArrayList<String> convert(ArrayList<String> original, String operate) {
		ArrayList<String> list = new ArrayList<>();
		
		if(operate.equals("toSim")) {
			for(String string : original) 
				list.add(CommonUtils.convertToSimplifiedChinese(string));
		}else {
			for(String string : original) 
				list.add(CommonUtils.convertToTraditionalChinese(string));
		}

		return list;
	}
}
