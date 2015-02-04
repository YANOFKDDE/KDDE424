package edu.scu.stu.luliu.miningtags.tools;

import java.util.ArrayList;
import java.util.List;

public class Test {

	public static void main(String[] args) {
		String []inputFileName={"./data/a.txt","./data/b.txt"};
		FileHandle fh=new FileHandle(inputFileName);
		//fh.extractKeyWords(fh.fileReader());
		List<String >fileContent=new ArrayList<String>();
		for(int i=0;i<inputFileName.length;i++)
		{
			fileContent.addAll(fh.fileReader(inputFileName[i]));
		}
		fh.writeKeyWordsToFile(fileContent);
	}

}
