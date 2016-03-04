package cn.edu.scu.dke.idsp.vo;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName: Window
 * @Description: TODO
 * @author yanli
 * @date 2015年9月29日 下午3:28:33
 * 
 */
public class Window {
	/** num. of window */
	private int l;
	/** window的region信息 */
	private BitSet region;
	/** window和window组合的信息 */
	private List<BitSet> windows = new ArrayList<BitSet>();
	/** window的信息 */
	private List<BitSet> windowlist = new ArrayList<BitSet>();
	/** window和window组合的起始位置信息，map的key值代表的是window 的bitset的String信息 */
	private Map<String, Map<Integer, Integer[]>> windowScope;

	/** 
	 * <p>Title: </p> 
	 * <p>Description: </p>  
	 */ 
	public Window() {
		super();
		windowScope = new HashMap<String, Map<Integer,Integer[]>>();
	}

	public void print() {
//		for (BitSet bitSet : windows) {
//			System.out.println(bitSet);
//		}
		for (Map.Entry<String, Map<Integer,Integer[]>> entry : windowScope.entrySet()) {
			if (entry.getKey().equals("{7}") ||entry.getKey().equals("{9}") || entry.getKey().equals("{8}")) {
				System.out.print(entry.getKey()+"::::");
				for (Map.Entry<Integer, Integer[]> entrysub : entry.getValue().entrySet()) {
					Integer [] values =  entrysub.getValue();
					System.out.print("Key = " + entrysub.getKey()+":");
					for (Integer integer : values) {
						System.out.print(integer+",");
					}
					System.out.println();
				}
			}
		}
//		for (Map.Entry<String, Map<Integer,Integer[]>> entry : negWindowScope.entrySet()) {
//			System.out.print(entry.getKey()+"::::");
//			for (Map.Entry<Integer, Integer[]> entrysub : entry.getValue().entrySet()) {
//				Integer [] values =  entrysub.getValue();
//				 System.out.print("Key = " + entrysub.getKey()+":");
//				 for (Integer integer : values) {
//					 System.out.print(integer+",");
//				}
//				 System.out.println();
//			}
//		}
	}
	
	public int getL() {
		return l;
	}

	public void setL(int l) {
		this.l = l;
	}

	public List<BitSet> getWindows() {
		return windows;
	}

	public void setWindows(List<BitSet> windows) {
		this.windows = windows;
	}

	public BitSet getRegion() {
		return region;
	}

	public void setRegion(BitSet region) {
		this.region = region;
	}

	public List<BitSet> getWindowlist() {
		return windowlist;
	}

	public void setWindowlist(List<BitSet> windowlist) {
		this.windowlist = windowlist;
	}

	/** 
	 * @Title: sortWindowlist 
	 * @Description: 从大到小
	 * @return void    返回类型 
	 * @throws 
	 */ 
	public void sortWindows() {
		for (int i = 1; i < windows.size(); i++) {
			BitSet tmp = windows.get(i);
			int j = i-1;
			for (; j >=0 ; j--) {
				BitSet tmp1 = windows.get(j);
				if (compare(tmp,tmp1)) {
					break;
				}else {
					windows.set(j+1, tmp1);
				}
			}
			windows.set(j+1, tmp);
		}
	}

	/** 
	 * @Title: compare 
	 * @Description: 大于 返回false
	 * @param tmp
	 * @param tmp1
	 * @return    设定文件 
	 * @return boolean    返回类型 
	 * @throws 
	 */ 
	private boolean compare(BitSet tmp, BitSet tmp1) {
		String str1 = tmp.toString();
		String str2 = tmp1.toString();
		String[] str1s = str1.split(",");
		String[] str2s = str2.split(",");
		if (str1s.length>str2s.length) {
			return false;
		}
		if (str1s.length<str2s.length) {
			return true;
		}
		if (str1s.length==str2s.length) {
			return str1.compareTo(str2) >0?false:true;
		}
		return false;
	}

	/** 
	 * @Title: compare 
	 * @Description:  从小到大 
	 * 非组合区间的排序，不同于所有窗口区间的排序 tmp < tmp1 返回true
	 * @param tmp
	 * @param tmp1
	 * @return    设定文件 
	 * @return boolean    返回类型 
	 * @throws 
	 */ 
	private boolean compare2(BitSet tmp, BitSet tmp1) {
		String tmpstr = tmp.toString();
		String tmpstr1 = tmp1.toString();
		int tmpint = Integer.valueOf(tmpstr.substring(1,tmpstr.length()-1));
		int tmpint1 = Integer.valueOf(tmpstr1.toString().substring(1,tmpstr1.length()-1));
		return tmpint > tmpint1;
	}
	/** 
	 * @Title: sortWindowlist 
	 * @Description: 从小到大
	 * @return void    返回类型 
	 * @throws 
	 */ 
	public void sortWindowlist() {
		for (int i = 1; i < windowlist.size(); i++) {
			BitSet tmp = windowlist.get(i);
			int j = i-1;
			for (; j >=0 ; j--) {
				BitSet tmp1 = windowlist.get(j);
				if (compare2(tmp,tmp1)) {
					break;
				}else {
					windowlist.set(j+1, tmp1);
				}
			}
			windowlist.set(j+1, tmp);
		}
	}
	public Map<String, Map<Integer, Integer[]>> getWindowScope() {
		return windowScope;
	}

	public void setWindowScope(Map<String, Map<Integer, Integer[]>> windowScope) {
		this.windowScope = windowScope;
	}

}
