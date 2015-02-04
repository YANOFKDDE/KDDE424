package edu.scu.stu.luliu.miningtags.test;

import edu.scu.stu.luliu.miningtags.util.GetProductID;
import edu.scu.stu.luliu.miningtags.vo.Product;
import edu.scu.stu.luliu.miningtags.crawler.Crawler;

/*
 * author: LuLiu
 * date:2015年1月8日
 * time:下午10:21:05
 * purpose: Test the crawler for JD
 */
public class CrawlerTest {
	public  Product getContent(String url) {
		Crawler crawler = new Crawler(url);
		Product rp = crawler.rP;
		System.out.println(rp);
		return rp;
	}
}
