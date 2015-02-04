package edu.scu.stu.luliu.miningtags.util;

import java.util.ArrayList;
import java.util.HashSet;

import edu.scu.stu.luliu.miningtags.vo.Labels;
import edu.scu.stu.luliu.miningtags.vo.Product;

public enum Count {
	INSTANCE;
	public static void count(Product product){
		ArrayList<Labels> list = product.list;
		int sum = 0;
		HashSet<String> uniCount = new HashSet<String>();
		for(Labels l:list){
			sum += l.getSet().size();
			uniCount.addAll(l.getSet());
		}
		System.out.println("Number of Tags: "+sum);
		System.out.println("Number of unique Tags: "+uniCount.size());
	}
}
