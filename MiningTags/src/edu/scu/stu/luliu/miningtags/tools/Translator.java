package edu.scu.stu.luliu.miningtags.tools;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Scanner;

public enum Translator {
	INSTANCE;
	
	public static String translate(String content) throws Exception{
		content = URLEncoder.encode(content, "utf-8");
		URL url = new URL("http://translate.google.cn/translate_a/single?client=t&sl=zh-CN&tl=en&hl=zh-CN&dt=bd&dt=ex&dt=ld&dt=md&dt=qc&dt=rw&dt=rm&dt=ss&dt=t&dt=at&ie=UTF-8&oe=UTF-8&otf=1&srcrom=1&ssel=6&tsel=3&tk=517478|504343&q="
				+ content);
		URLConnection conn = url.openConnection();
		conn.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 5.0; Windows XP; DigExt)"); 
		InputStream is = conn.getInputStream(); 
		Scanner sc = new Scanner(is);
		String tmp = sc.nextLine();
		String s = tmp.split("\"")[1];
		sc.close();
		return s;
	}
	public static void main(String[] args) {
		try {
//			String s = new String(, "UTF-8");
			System.out.println(Translator.INSTANCE.translate("张裕特选级雷司令干白葡萄酒 750ml"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
