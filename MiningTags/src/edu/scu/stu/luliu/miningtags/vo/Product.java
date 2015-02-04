package edu.scu.stu.luliu.miningtags.vo;

import java.util.ArrayList;
/*
 * author: LuLiu 
 * date:2015年1月12日
 * time:下午4:40:44
 * purpose: Store the basic information of a product
 */
public class Product {
	public String imageUrl;
	public String name;
	public String price;
	public String commentsNum;
	public ArrayList<Labels> list;
	public String productUrl;
	
	public Product(String imageUrl, String name, String price,
			String commentsNum, ArrayList<Labels> list, String productUrl) {
		super();
		this.imageUrl = imageUrl;
		this.name = name;
		this.price = price;
		this.commentsNum = commentsNum;
		this.list = list;
		this.productUrl = productUrl;
	}
	@Override
	public String toString() {
		String s = "name: "+name
				+" image Url: "+imageUrl
				+" price: "+price
				+" comments num:"+commentsNum
				+" product Url:"+productUrl;
		return s;
	}
	
	public void print(){
		System.out.println("Product: "+name+" *********************");
		for(Labels l:list){
			System.out.println(l.toString());
		}
	}
}
