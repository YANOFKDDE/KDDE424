/**   
 * @Title: Result.java 
 * @Package cn.edu.scu.dke.vo 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2015年9月6日 下午10:11:32 
 * @version V1.0   
 */
package cn.edu.scu.dke.idsp.vo;

import java.util.Collections;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * @ClassName: Result
 * @Description: TODO
 * @author yanli
 * @date 2015年9月6日 下午10:11:32
 * 
 */

public class Results {
	/** 挖掘满足要求的模式集 */
	private ArrayList<PatternInterval> pList;
	/** 正列支持度 */
	private double a;
	/** 负列支持度 */
	private double b;
	public double avgCRatio;//越大越好
	public double avgPatternCount;//越大越好
	public double avgWindowSize;//越小越好
	public double avgPatternSize;//越大越好
	
	public Integer count=0;

	public Results(double a, double b) {
		this.pList = new ArrayList<PatternInterval>();
		this.a = a;
		this.b = b;
	}

	public boolean addPattern(PatternInterval p) {
		pList.add(p);
		return true;
	}

	/** sort the pList, and find the minimum contrast value **/
	public void sort() {
		/** sort the list, ascend **/
		Collections.sort(pList, new Comparator<PatternInterval>() {

			@Override
			public int compare(PatternInterval o1, PatternInterval o2) {
				if (o1.getcRatio() < o2.getcRatio())
					return -1;
				else if (o1.getcRatio() > o2.getcRatio())
					return 1;
				else
					return 0;
			}
		});
	}

	public ArrayList<PatternInterval> getpList() {
		return pList;
	}

	public int getSize() {
		return pList.size();
	}
	public double getA() {
		return a;
	}
	public double getB() {
		return b;
	}

	/** 
	 * @Title: statistics 
	 * @Description: TODO     设定文件 
	 * @return void    返回类型 
	 * @throws 
	 */ 
	public void statistics() {
		int amount = pList.size();
		double amountCRatio = 0;
		double amountWindowSize = 0;
		double amountPatternSize = 0;
		for (int i = 0; i < pList.size(); i++) {
			PatternInterval pi = pList.get(i);
			amountCRatio+=pi.getcRatio();
			amountWindowSize+=pi.getRegions().toString().split(",").length;
			amountPatternSize+=pi.getPattern().length();
		}
		if (amount == 0) {
			avgCRatio = 0;//越大越好
			avgPatternCount = amount;//越大越好
			avgWindowSize = 0;//越小越好
			avgPatternSize = 0;//越大越好
		}else {
			avgCRatio = amountCRatio/amount;//越大越好
			avgPatternCount = amount;//越大越好
			avgWindowSize = amountWindowSize/amount;//越小越好
			avgPatternSize = amountPatternSize/amount;//越大越好
		}
		
		
	}

	@Override
	public String toString() {
		return "Results [a=" + a + ", b=" + b + ", avgCRatio=" + avgCRatio
				+ ", avgPatternCount=" + avgPatternCount + ", avgWindowSize="
				+ avgWindowSize + ", avgPatternSize=" + avgPatternSize + "]";
	}
}
