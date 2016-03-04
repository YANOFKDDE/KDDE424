/**   
* @Title: FileUtils.java 
* @Package cn.edu.scu.dke.idsp.util 
* @Description: TODO(用一句话描述该文件做什么) 
* @author A18ccms A18ccms_gmail_com   
* @date 2015年9月7日 上午9:42:09 
* @version V1.0   
*/
package cn.edu.scu.dke.idsp.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** 
 * @ClassName: FileUtils 
 * @Description: TODO
 * @author yanli
 * @date 2015年9月7日 上午9:42:09 
 *  
 */
public class FileUtils {
	public static List<String> readFile(String path) throws IOException{
		List<String> data = new ArrayList<String>();
		File file=new File(path);
        if(!file.exists()||file.isDirectory())
            throw new FileNotFoundException();
        @SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(new FileReader(file));
        String tmp = br.readLine();
        while (tmp!=null) {
			data.add(tmp);
			tmp = br.readLine();
		}
		return data;
	}
	public static void write2file(List<String> list ,String filename) {
		File file = new File(filename);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for (String seq : list) {
				bw.write(seq + "\n");
			}
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
