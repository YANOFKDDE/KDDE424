package cn.edu.scu.dke.idsp.util;

import java.util.List;

import cn.edu.scu.dke.idsp.vo.PatternInterval;

/** 
 * @ClassName: PatternListUtil 
 * @Description: TODO
 * @author yanli
 * @date 2016��1��1�� ����1:01:38 
 *  
 */
public class PatternListUtil {
	/** 
	 * @Title: sortWindowlist 
	 * @Description: �������򣬴�С����
	 * @return void    �������� 
	 * @throws 
	 */ 
	public static void sortPatternIntervalList(List<PatternInterval> pList) {
		for (int i = 1; i < pList.size(); i++) {
			PatternInterval tmp = pList.get(i);
			int j = i-1;
			for (; j >=0 ; j--) {
				PatternInterval tmp1 = pList.get(j);
				if (compare(tmp,tmp1)) {
					break;
				}else {
					pList.set(j+1, tmp1);
				}
			}
			pList.set(j+1, tmp);
		}
	}
	/** 
	 * @Title: compare 
	 * @Description: TODO 
	 * @param tmp
	 * @param tmp1
	 * @return    �趨�ļ� 
	 * @return boolean    �������� 
	 * @throws 
	 */ 
	public static boolean compare(PatternInterval tmp, PatternInterval tmp1) {
		String str1 = tmp.getRegions().toString();
		String str2 = tmp1.getRegions().toString();
		String[] str1s = str1.split(",");
		String[] str2s = str2.split(",");
		if (str1s.length>str2s.length) {
			return true;
		}
		if (str1s.length<str2s.length) {
			return false;
		}
		if (str1s.length==str2s.length) {
			return str1.compareTo(str2) >0?true:false;
		}
		return false;
	}
}
