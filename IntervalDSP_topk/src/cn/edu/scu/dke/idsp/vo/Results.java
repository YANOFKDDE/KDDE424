/**   
 * @Title: Result.java 
 * @Package cn.edu.scu.dke.vo 
 * @Description: TODO(��һ�仰�������ļ���ʲô) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2015��9��6�� ����10:11:32 
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
 * @date 2015��9��6�� ����10:11:32
 * 
 */

public class Results {
	/** �ھ�����Ҫ���ģʽ�� */
	private ArrayList<PatternInterval> pList;
	/** ����֧�ֶ� */
	private double a;
	/** ����֧�ֶ� */
	private double b;
	public double avgCRatio;//Խ��Խ��
	public double avgPatternCount;//Խ��Խ��
	public double avgWindowSize;//ԽСԽ��
	public double avgPatternSize;//Խ��Խ��
	
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
	 * @Description: TODO     �趨�ļ� 
	 * @return void    �������� 
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
			avgCRatio = 0;//Խ��Խ��
			avgPatternCount = amount;//Խ��Խ��
			avgWindowSize = 0;//ԽСԽ��
			avgPatternSize = 0;//Խ��Խ��
		}else {
			avgCRatio = amountCRatio/amount;//Խ��Խ��
			avgPatternCount = amount;//Խ��Խ��
			avgWindowSize = amountWindowSize/amount;//ԽСԽ��
			avgPatternSize = amountPatternSize/amount;//Խ��Խ��
		}
		
		
	}

	@Override
	public String toString() {
		return "Results [a=" + a + ", b=" + b + ", avgCRatio=" + avgCRatio
				+ ", avgPatternCount=" + avgPatternCount + ", avgWindowSize="
				+ avgWindowSize + ", avgPatternSize=" + avgPatternSize + "]";
	}
}
