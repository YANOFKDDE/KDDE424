package edu.scu.stu.luliu.miningtags.util;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

/*
 * author: LuLiu 
 * date:2015年1月12日
 * time:下午4:40:44
 * purpose:
 */
public enum GetPriceByID {
	INSTANCE;
	public String getPrice(int productID){
		String urlS = getPriceUrl(productID);
		try{
			URL url = new URL(urlS);
			InputStream is = url.openStream();
			Scanner sc = new Scanner(is);
			String tmp = sc.nextLine();
			String[] tmps = tmp.split(",");
			String[] tmpss = tmps[1].split(":");
			String price = tmpss[1].replace("\"", "");
			sc.close();
			is.close();
			
			return price;
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
	
	private String getPriceUrl(int productID){
		String url = "http://p.3.cn/prices/mgets?callback=jsonp"
				+System.currentTimeMillis()
				+"&_="
				+(System.currentTimeMillis()+200)
				+"&skuids=J_"
				+productID
				+"type=1";
		
		return url;
	}
}
