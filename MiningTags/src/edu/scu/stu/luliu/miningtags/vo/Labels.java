package edu.scu.stu.luliu.miningtags.vo;

import java.util.HashSet;

import edu.scu.stu.luliu.miningtags.tools.Translator;

/*
 * author: LuLiu 
 * date:2015年1月12日
 * time:下午4:40:44
 * purpose:To store the labels of one user
 */
public class Labels {
	private HashSet<String> set = new HashSet<>();
	
	public void addLabel(String label){
		set.add(label);
	}
	
	public HashSet<String> getSet(){
		return set;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for(String s:set){
			sb.append(s+",");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
}
