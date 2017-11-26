package hust.tools.csc.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import hust.tools.csc.detecet.SpellError;
import hust.tools.csc.ngram.HustNGramModel;
import hust.tools.csc.ngram.NGramModel;
import hust.tools.ngram.io.ARPATextFileNGramModleReader;
import hust.tools.ngram.io.BinaryFileNGramModelReader;
import hust.tools.ngram.io.TextFileNGramModelReader;
import hust.tools.ngram.model.AbstractNGramModelReader;

/**
 *<ul>
 *<li>Description: 读取SIGHAN2015拼写纠错测试语料并存入内存 
 *<li>Company: HUST
 *<li>@author Sonly
 *<li>Date: 2017年11月5日
 *</ul>
 */
public class FileOperator {
	
	/**
	 * 读取SIGHAN2015拼写纠错测试语料（SIGHAN15_CSC_TestInput.txt）
	 * @param path		语料路径
	 * @param encoding	语料格式
	 * @return			句子pid与句子内容的映射
	 * @throws IOException
	 */
	public static HashMap<String, Sentence> readTestFile(String path, String encoding) throws IOException {
		HashMap<String, Sentence> list = new HashMap<>();
		File file = new File(path);
		
		if(file.isFile() && file.exists()) {
			InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), encoding);
			BufferedReader reader = new BufferedReader(inputStreamReader);
			
			String line = "";
			while((line = reader.readLine()) != null) {
				line = CommonUtils.ToDBC(line).trim();
				if(!line.equals("")) {
					String[] strings = line.split("\\s+");
					String pid = strings[0].trim();
					pid = pid.split("=")[1].replaceAll("\\)", "");
					Sentence sentence =  new Sentence(strings[1].trim().split(""));
					
					list.put(pid, sentence);
				}
			}
			reader.close();
		}else {
			System.err.println("File:\""+path+"\" read failed!");
		}
		
		return list;	
	}
	
	/**
	 * 读取SIGHAN2015拼写纠错测试语料的黄金标准语料（SIGHAN15_CSC_TestTruth.txt）
	 * @param path		语料路径
	 * @param encoding	语料编码
	 * @return			句子pid与该句子所有错字的映射
	 * @throws IOException
	 */
	public static HashMap<String, ArrayList<SpellError>> readGoldFile(String path, String encoding) throws IOException {
		HashMap<String, ArrayList<SpellError>> list = new HashMap<>();
		File file = new File(path);
		int no = 0;
		if(file.isFile() && file.exists()) {
			InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), encoding);
			BufferedReader reader = new BufferedReader(inputStreamReader);
			
			String line = "";
			while((line = reader.readLine()) != null) {
				System.out.println(++no);
				line = CommonUtils.ToDBC(line).trim();
				if(!line.equals("")) {
					String[] strings = line.split(",");
					if(strings.length > 2) {
						ArrayList<SpellError> errors = new ArrayList<>();
						for(int i = 1; i < strings.length - 1; i=i+2) {
							SpellError error = new SpellError(strings[i+1].trim(), Integer.parseInt(strings[i].trim()));
							errors.add(error);
						}
						list.put(strings[0], errors);
					}
					
				}
			}
			reader.close();
		}else {
			System.err.println("File:\""+path+"\" read failed!");
		}
		
		return list;	
	}
	
	

	/**
	 * 读取SIGHAN2015拼写纠错测试语料（SIGHAN15_CSC_TestInput.txt）
	 * @param path		语料路径
	 * @param encoding	语料格式
	 * @return			句子pid与句子内容的映射
	 * @throws IOException
	 */
	public static List<String> readSGMLFile(String path, String encoding) throws IOException {
		List<String> list = new ArrayList<>();
		File file = new File(path);
		
		if(file.isFile() && file.exists()) {
			InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), encoding);
			BufferedReader reader = new BufferedReader(inputStreamReader);
			
			String line = "";
			while((line = reader.readLine()) != null) {
				line = CommonUtils.ToDBC(line).trim();
				if(!line.equals("")) {
					list.add(line);
				}
			}
			reader.close();
		}else {
			System.err.println("File:\""+path+"\" read failed!");
		}
		
		return list;	
	}
	
	/**
	 * 按行读取文本中的句子
	 * @param path		文本的路径
	 * @param encoding	文本的编码
	 * @return			句子列表
	 * @throws IOException
	 */
	public static ArrayList<Sentence> readFile(String path, String encoding) throws IOException {
		ArrayList<Sentence> list = new ArrayList<Sentence>();
		File file = new File(path);
		
		if(file.isFile() && file.exists()) {
			InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), encoding);
			BufferedReader reader = new BufferedReader(inputStreamReader);
			
			String line = "";
			while((line = reader.readLine()) != null) {
				line = CommonUtils.ToDBC(line).trim();
				if(line.equals(""))
					continue;
				
				String[] chs = line.split("");
				list.add(new Sentence(chs));
			}
			reader.close();
		}else {
			System.err.println("File:\""+path+"\" read failed!");
		}
		
		return list;	
	}
	
	/**
	 * 建立字典
	 * @param path	字典文件路径
	 * @return		字典
	 * @throws IOException
	 */
	public static Dictionary loadDict(String path) throws IOException {
		File file = new File(path);
		DataInputStream reader = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
		Dictionary dictionary = new Dictionary();
		int count = reader.readInt();
		for(int i = 0; i < count; i++) {
			String[] entry = reader.readUTF().split("\t");
			if(entry.length == 2) {
				String ngrams = CommonUtils.ToDBC(entry[0]);
				ngrams = ngrams.replaceAll("\t", "").trim();
				if(!ngrams.equals("")) {
					int num = Integer.parseInt(entry[1]);
					dictionary.add(ngrams, num);
				}
			}
		}
		reader.close();
	
		return dictionary;
	}

	/**
	 * 加载n元模型文件
	 * @param modelFile		语言模型文件
	 * @return				语言模型
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static NGramModel loadModel(String modelFile) throws ClassNotFoundException, IOException {
		AbstractNGramModelReader modelReader;
		if(modelFile.endsWith(".arpa"))
			modelReader = new ARPATextFileNGramModleReader(new File(modelFile));
		else if(modelFile.endsWith(".bin"))
			modelReader = new BinaryFileNGramModelReader(new File(modelFile));
		else
			modelReader = new TextFileNGramModelReader(new File(modelFile));
	 
		return new HustNGramModel(modelReader.constructModel());
	}
	
	/**
	 * 写文件
	 * @param outPath	写出路径
	 * @param encoding	文件编码
	 * @param content	写入的内容
	 * @throws IOException
	 */
	public static void writeEvaluation(String outPath, String encoding, String content) throws IOException {
		OutputStreamWriter oWriter = new OutputStreamWriter(new FileOutputStream(new File(outPath)), encoding);
		BufferedWriter writer = new BufferedWriter(oWriter);
		writer.write(content);
		writer.close();
	}
	
	/**
	 * 将多个文件压缩到制定目录
	 * @param files		待压缩的文件路径
	 * @param zipname	压缩包的路径名
	 * @throws IOException
	 */
	public static void zipFiles(ArrayList<String> files,String zipPath) throws IOException {
        OutputStream os = new BufferedOutputStream(new FileOutputStream(zipPath + "\\checker.model"));
        ZipOutputStream zos = new ZipOutputStream(os);
        byte[] buf = new byte[8192];
        int len;
        for(String filePath: files) {
        	File file = new File(filePath);
            if(!file.isFile())
            	continue;
            ZipEntry ze = new ZipEntry(file.getName());
            zos.putNextEntry(ze);
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            while((len = bis.read(buf)) > 0) {
            	zos.write(buf, 0, len);
            }
            zos.closeEntry();
            bis.close();
        }
        zos.close();
        
        //删除已压缩的文件
        for(String filePath: files) {
        	File file = new File(filePath);
        	file.delete();
        }
	}
	
	/**
	 * 解压压缩文件到指定目录
	 * @param filePath	待解压的文件
	 * @param toPath	解压的路径
	 * @throws IOException
	 */
	public static void unZipFile(String filePath, String toPath) throws IOException {
		//去目录下寻找文件
		File file = new File(filePath);
		ZipFile zipFile = null;
		
		try {
			zipFile = new ZipFile(file);//设置编码格式
        } catch (IOException exception) {
        	exception.printStackTrace();
        	System.out.println("解压文件不存在!");
        }
        
		Enumeration<? extends ZipEntry> e = zipFile.entries(); 
		while(e.hasMoreElements()) { 
			ZipEntry zipEntry = (ZipEntry)e.nextElement(); 
			if(zipEntry.isDirectory()) { 
				continue;
        	}else { 
        		File tempFile = new File(toPath + zipEntry.getName()); 
        		tempFile.getParentFile().mkdirs(); 
        		tempFile.createNewFile();
        		InputStream is = zipFile.getInputStream(zipEntry); 
        		FileOutputStream fos = new FileOutputStream(tempFile); 
        		int length = 0;
        		byte[] b = new byte[1024]; 
        		while((length=is.read(b, 0, 1024)) != -1) { 
        			fos.write(b, 0, length); 
        		} 
        		is.close(); 
        		fos.close(); 
        	} 
        }

//        if (zipFile != null) {
//        	zipFile.close(); 
//        }
//        file.deleteOnExit();//解压完以后将压缩包删除
	}
}