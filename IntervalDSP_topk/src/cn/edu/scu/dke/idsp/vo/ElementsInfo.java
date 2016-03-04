/**   
* @Title: ElementsInfo.java 
* @Package cn.edu.scu.dke.vo 
* @Description: TODO(用一句话描述该文件做什么) 
* @author A18ccms A18ccms_gmail_com   
* @date 2015年9月6日 下午4:09:21 
* @version V1.0   
*/
package cn.edu.scu.dke.idsp.vo;

import java.util.HashMap;
import java.util.List;

import cn.edu.scu.dke.idsp.util.DateUtil;


/** 
 * @ClassName: ElementsInfo 
 * @Description: TODO
 * @author yanli
 * @date 2015年9月6日 下午4:09:21 
 *  
 */
public class ElementsInfo {

	/**
	 * collection of all elements
	 */
	private HashMap<String, Element> eMap;
	private HashMap<Integer, List<Long>> timeMap;
	/**
	 * collection of length of every sequence
	 */
	private List<Integer> lenList;
	/**
	 * collection of all elements
	 */
	private Window window;
	/** 
	 * <p>Title: constructor</p> 
	 * <p>Description: constructor with all fields</p> 
	 * @param eMap
	 * @param lenList 
	 */ 
	public ElementsInfo(HashMap<String, Element> eMap,
			List<Integer> lenList,HashMap<Integer, List<Long>> timeMap) {
		super();
		this.eMap = eMap;
		this.lenList = lenList;
		this.timeMap = timeMap;
	}
	/** 
	 * <p>Title: </p> 
	 * <p>Description: </p>  
	 */ 
	public ElementsInfo() {
		super();
	}
	
	public void printInfo(){
		System.out.println("pos                       the num. of element"+eMap.size());
		/** print the information of elements **/
		for(String s: eMap.keySet()){
			if (s.equals("A")||s.equals("C")) {
				eMap.get(s).printInfo();
				for (int i = 0; i < timeMap.size(); i++) {
					List<Long> list = timeMap.get(i);
					for (int j = 0; j < list.size(); j++) {
						System.out.print(DateUtil.long2Time(list.get(j))+", ");
					}
					System.out.println();
				}
			}
		}
		window.print();
		/** print the length of sequences in data set **/
		System.out.println("Length: "+lenList);
	}
	public void printInfo2(){
		System.out.println("neg                             the num. of element"+eMap.size());
		/** print the information of elements **/
		for(String s: eMap.keySet()){
			if (s.equals("A")||s.equals("C")||s.equals("B")||s.equals("H")) {
				eMap.get(s).printInfo();
				for (int i = 0; i < timeMap.size(); i++) {
					List<Long> list = timeMap.get(i);
					System.out.print(i+"::::");
					for (int j = 0; j < list.size(); j++) {
						System.out.print(j+":"+DateUtil.long2Time(list.get(j))+", ");
					}
					System.out.println();
				}
			}
		}
		window.print();
		/** print the length of sequences in data set **/
		System.out.println("Length: "+lenList);
	}
	
	public HashMap<String, Element> geteMap() {
		return eMap;
	}
	public void seteMap(HashMap<String, Element> eMap) {
		this.eMap = eMap;
	}
	public List<Integer> getLenList() {
		return lenList;
	}
	public void setLenList(List<Integer> lenList) {
		this.lenList = lenList;
	}
	public Window getWindow() {
		return window;
	}
	public void setWindow(Window window) {
		this.window = window;
	}
	public HashMap<Integer, List<Long>> getTimeMap() {
		return timeMap;
	}
	public void setTimeMap(HashMap<Integer, List<Long>> timeMap) {
		this.timeMap = timeMap;
	}
	
}
