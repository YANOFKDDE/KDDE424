package edu.scu.stu.luliu.miningtags.util;
/*
 * author: LuLiu 
 * date:2015年1月12日
 * time:下午4:40:44
 * purpose: Get the product ID in jd by url
 */
public enum GetProductID {
	INSTACNE;
	
	public static int getID(String url){
		Integer i = -1;
		try {
			String id = url.replace("http://item.jd.com/", "").replace(".html", "");
			i = Integer.valueOf(id);
		} catch (Exception e) {
			i = -1;
		}
		return i;
	}
}
