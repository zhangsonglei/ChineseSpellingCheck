package hust.tools.csc.util;

import java.util.ArrayList;

import com.hankcs.hanlp.HanLP;

/**
 *<ul>
 *<li>Description: 工具类，对文本进行判断和转换
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年10月13日
 *</ul>
 */
public class CommonUtils {
	
	/**
	 * 判断一个字符串是不是汉字（汉字：[0x4e00,0x9fa5]（或十进制[19968,40869]））
	 * @param string	待判断的字符串
	 * @return			true-是/false-否
	 */
	public static boolean isHanZi(String string) {
		int num = 0;
		for(int i = 0; i < string.length(); i++) {
			num = (int)string.charAt(i);
			if(!(19968 <= num && num <= 40869)) 
				return false;
		}

	    return true;
	}
	
	/**
	 * 判断一个字符串是不是数字（数字：[0x30,0x39]（或十进制[48, 57]））
	 * @param string	待判断的字符串
	 * @return			true-是/false-否
	 */
	public static boolean isNumber(String string) {
		string = ToDBC(string);
		int num = 0;
		for(int i = 0; i < string.length(); i++) {
			num = (int)string.charAt(i);
			if(!(48 <= num && num <=57)) 
				return false;
		}

	    return true;
	}

	/**
     * 半角转全角
     * @param input String.
     * @return 全角字符串.
     */
    public static String ToSBC(String input) {
    	char c[] = input.toCharArray();
    	for (int i = 0; i < c.length; i++) {
    		if (c[i] == ' ') 
    			c[i] = '\u3000';
    		else if (c[i] < '\177')
    			c[i] = (char) (c[i] + 65248);
    	}
	
    	return new String(c);
    }

    /**
     * 全角转半角
     * @param input String.
     * @return 半角字符串
     */
    public static String ToDBC(String input) {
    	char c[] = input.toCharArray();
    	for (int i = 0; i < c.length; i++) {
    		if (c[i] == '\u3000') 
    			c[i] = ' ';
    		else if (c[i] > '\uFF00' && c[i] < '\uFF5F') 
    			c[i] = (char) (c[i] - 65248);
    	}
         
        return new String(c);
    }
    
    /**
	 * 将给定句子切分成连续的bigrams
	 * @param input	待切分的句子
	 * @return		连续的bigrams
	 */
	public static ArrayList<String> generateNGrams(String[] input, int n)  {
		ArrayList<String> output = new ArrayList<>();
		
		for(int i = 0; i < input.length - n + 1; i++) {
			String[] ngrams = new String[n];
			for(int j = i, index = 0; j < i + n; j++, index++)
				ngrams[index] = input[j];

			String ngram = "";
			for(String ch : ngrams)
				ngram += ch;
			
			output.add(ngram);
		}//end for
		
		return output;
	}
	
	/**
	 * 繁体中文转为简体中文
	 * @param traditionalChinese	待转换的繁体中文
	 * @return						简体中文
	 */
	public static String convertToSimplifiedChinese(String traditionalChinese) {
		return HanLP.convertToSimplifiedChinese(traditionalChinese);
	}
	
	/**
	 * 简体中文转为繁体中文
	 * @param simplifiedChinese	待转换的简体中文
	 * @return					繁体中文
	 */
	public static String convertToTraditionalChinese(String simplifiedChinese) {
		return HanLP.convertToTraditionalChinese(simplifiedChinese);
	}
}
