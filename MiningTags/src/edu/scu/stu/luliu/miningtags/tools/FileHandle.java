package edu.scu.stu.luliu.miningtags.tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
public class FileHandle {
	 private String []inputFileNameArray;
	 private String beginFlag="<span>";
	 private String endFlag="</span>";
	 public FileHandle()
	 {
		 
	 }
	 public FileHandle(String []inputFileName)
	 {
		 this.inputFileNameArray=inputFileName;
	 }
	 
	 public String getBeginFlag() {
		return beginFlag;
	}
	public void setBeginFlag(String beginFlag) {
		this.beginFlag = beginFlag;
	}
	public String getEndFlag() {
		return endFlag;
	}
	public void setEndFlag(String endFlag) {
		this.endFlag = endFlag;
	}
	/**
	  * 读取文本文件的内容
	  * @return 文本文件的内容
	  */
	 public List<String> fileReader(String inputFileName)
	 {
		 List<String> result=new ArrayList<String>();
		 try {
			BufferedReader br=new BufferedReader(new FileReader(inputFileName));
			String line="";
			while((line=br.readLine())!=null)
			{
				result.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return result;
	 }
	 /**
	  * 输出文件的关键字
	  * @param fileContent 输入文件的内容
	  * @return
	  */
	 public Set<String> extractKeyWords(List<String> fileContent)
	 {
		 Set<String>keyWords=new HashSet<String>();
		 for(int i=0;i<fileContent.size();i++)
		 {
			 int index=0;
			 while((index=(fileContent.get(i).indexOf(beginFlag, index)))!=-1)
			 {
				 int endIndex=fileContent.get(i).indexOf(endFlag, index);
				 String item=fileContent.get(i).substring(index+6, endIndex);
//				 System.out.println(item);
				 keyWords.add(item);
				 index=endIndex;
			 }
		 }
		 return keyWords;
	 }
	 /**
	  * 向文件中写入文件内容
	  * @param fileContent 文件内容
	  */
	 public void fileWriter(String fileContent,String fileName)
	 {
		 try {
			FileWriter FW=new FileWriter(fileName);
			FW.write(fileContent);
			FW.flush();
			FW.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	 }
	 /**
	  * 向文件中写入keywords
	  * @param fileContent
	  */
	 public void writeKeyWordsToFile(List<String> fileContent)
	 {
		 String fileName="./data/key.txt";
		 Set<String>keyWords=extractKeyWords(fileContent);
		 Iterator<String> iter=keyWords.iterator();
		 StringBuilder sb=new StringBuilder();
		 int k=0;
		 while(iter.hasNext())
		 {
			 sb.append(iter.next()+" "+k+"\n");
			 k++;
		 }
		 System.out.println(sb.toString());
		 fileWriter(sb.toString(),fileName);
	 }
}
