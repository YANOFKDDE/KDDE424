package edu.scu.stu.luliu.miningtags.crawler;

import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import edu.scu.stu.luliu.miningtags.util.GetPriceByID;
import edu.scu.stu.luliu.miningtags.util.GetProductID;
import edu.scu.stu.luliu.miningtags.vo.Labels;
import edu.scu.stu.luliu.miningtags.vo.Product;
	
/*
 * author: LuLiu 
 * date:2015年1月12日
 * time:下午4:40:44
 * purpose:
 */
public class Crawler {

	String path = "./";
	int pageCount = 0;
	int pageId = 1;
	
	int commentsNum = 0;
	String imageUrl = "NULL";
	String name = "NULL";
	String price = "NULL";
	String productUrl = "NULL";
	ArrayList<Labels> list = new ArrayList<>();
	
	public Product rP;
	
	public Crawler(String url) {
		int productID = GetProductID.INSTACNE.getID(url);
		this.path = "./data/";
		this.rP = getLabelsByID(productID);
	}

	public Product getLabelsByID(int productID){
		if(productID == -1)
			return null;
		
		
		/* get the labels of one product */
		boolean hasNext = true;
		while(hasNext){
			
			String urlS = "http://club.jd.com/review/"+productID+"-3-"+pageId+"-0.html";
			hasNext = getPage(urlS, list);
			pageId++;
			System.out.println("finish page:"+(pageId-1)+"  get comments:"+list.size());
		}
		
		productUrl = "http://item.jd.com/"+productID+".html";
		price = GetPriceByID.INSTANCE.getPrice(productID);
		Product product = new Product(imageUrl, name, price, ""+commentsNum, list, productUrl);
		System.out.println(product);
		
		/* store the labels of product in disk */
//		storeInDisk(list, productID);
		return product;
	}
	
	/* download one page and extract the labels for each user */
	private boolean getPage(String urlString, ArrayList<Labels> list){
		boolean hasNext = false;
		try {
			URL url = new URL(urlString);
			InputStream is = url.openStream();
			Scanner sc = new Scanner(is);
			while(sc.hasNext()){
				String tmp = getLine(sc);
				
				/* check is there another page */
				if(hasNext == false){
					if(tmp.contains("class=\"user\"")){
						hasNext = true;
					}
				}
				
				/* count the number of comments */
				if(tmp.contains("class=\"u-icon\""))
					commentsNum++;
				
				/* get the imagurl and name */
				if(pageId == 1){
					if(tmp.contains("class=\"p-img ac\"")){
						tmp = sc.nextLine();
						String[] tmps = tmp.split("src=");
						String[] tmpss = tmps[1].split(" ");
						imageUrl = tmpss[0].replace("\"", "");
					}
					
					if(tmp.contains("class=\"p-name\"")){
						tmp = sc.nextLine();
						String tmps = tmp.replace("target=\"_blank\">", "");
						name = tmps.replace("</a></li>", "");
					}
				}
				
				if(tmp.contains("<span class=\"comm-tags\" href=\"#none\"><span>")){
					Labels labels = new Labels();
					labels.addLabel(extractLabel(tmp));
//					System.out.println(extractLabel(tmp));
					tmp = getLine(sc);
					while(tmp.contains("<span class=\"comm-tags\" href=\"#none\"><span>")){
						labels.addLabel(extractLabel(tmp));
//						System.out.println(extractLabel(tmp));
						tmp = getLine(sc);
					}
					
					list.add(labels);
				}
			}
			sc.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hasNext;
	}
	
	/* skip the empty line */
	private String getLine(Scanner sc){
		String tmp = sc.nextLine();
		while(tmp.equals("") && sc.hasNext()){
			tmp = sc.nextLine();
		}
		return tmp;
	}
	
	private String extractLabel(String tmp){
		String s = tmp.replace("<span class=\"comm-tags\" href=\"#none\"><span>","");
		s = s.replace("</span></span>","");
		return s;
	}
	
	private void storeInDisk(ArrayList<Labels> list, int productID){
		try {
			PrintStream out = new PrintStream(new File("./data/"+productID));
			PrintStream sout = System.out;
			System.setOut(out);
			for(Labels label:list){
				System.out.println(label.toString());
			}
			System.setOut(sout);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
