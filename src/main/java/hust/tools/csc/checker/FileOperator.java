package hust.tools.csc.checker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import hust.tools.csc.util.FormatConvert;
import hust.tools.csc.util.Sentence;

/**
 * <ul>
 *<li>Class: FileOperator
 *<li>Description:  to read file or write file
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年6月20日
 *</ul>
 */
public class FileOperator {
	
	/**
	 * read file by line and save to List<String>
	 * @param path
	 * @return List<String[]>
	 * @throws IOException
	 */
	public static List<Sentence> readFile(String path, String encoding) throws IOException {
		List<Sentence> list = new ArrayList<>();
		File file = new File(path);
		
		if(file.isFile() && file.exists()) {
			InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), encoding);
			BufferedReader reader = new BufferedReader(inputStreamReader);
			
			String line = "";
			while((line = reader.readLine()) != null) {
				line = line.trim();
				if(!line.equals("")) {
					String[] sentence = FormatConvert.ToDBC(line).split("");
					list.add(new Sentence(sentence));
				}
			}
			reader.close();
		}else {
			System.err.println("File:\""+path+"\" read failed!");
		}
		
		return list;	
	}
	
	/**
	 * write to file
	 * @param strings
	 * @param path
	 * @throws IOException
	 */
	public static void writeFile(List<String> strings, String path) throws IOException {
		File file = new File(path);
		
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file), "utf-8");
		BufferedWriter writer = new BufferedWriter(outputStreamWriter);
		
		for(String string : strings) {
			writer.write(string);
			writer.newLine();
		}
		writer.close();
	}
	
	/**
	 * write to file
	 * @param int[][] arr
	 * @param path
	 * @throws IOException
	 */
	public static void writeFile(int[][] arr, String path) throws IOException {
		File file = new File(path);
		
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file));
		BufferedWriter writer = new BufferedWriter(outputStreamWriter);
		
		for(int i = 0; i < arr.length; i++) {
			for(int j = 0; j < arr[i].length; j++) {
				writer.write(arr[i][j] + " ");
			}
			writer.newLine();
		}
		writer.close();
	}
	
	/**
	 *<ul>
	 *<li>Method: writeFile
	 *<li>Description: write to file
	 *<li>@param arr
	 *<li>@param path
	 *<li>@throws IOException
	 *</ul>
	 */
	public static void writeFile(int[] arr, String path) throws IOException {
		File file = new File(path);
		
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file));
		BufferedWriter writer = new BufferedWriter(outputStreamWriter);
		
		for(int i = 0; i < arr.length; i++) {
			writer.write(arr[i] + " ");
			writer.newLine();
		}
		writer.close();
	}
	
	/**
	 *<ul>
	 *<li>Method: writeFile
	 *<li>Description: write to file
	 *<li>@param arr
	 *<li>@param path
	 *<li>@throws IOException
	 *</ul>
	 */
	public static void writeFile(String[] arr, String path) throws IOException {
		File file = new File(path);
		
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file));
		BufferedWriter writer = new BufferedWriter(outputStreamWriter);
		
		for(int i = 0; i < arr.length; i++) {
			writer.write(arr[i] + " ");
			writer.newLine();
		}
		writer.close();
	}
}