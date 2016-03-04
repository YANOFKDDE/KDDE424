package cn.edu.scu.dke.idsp.junit;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.TimeZone;

import org.junit.Test;

import cn.edu.scu.dke.idsp.util.DateUtil;
import cn.edu.scu.dke.idsp.util.SubBitSetList;

/** 
 * @ClassName: TestBusdataadelaayCleaning 
 * @Description: TODO
 * @author yanli
 * @date 2016Âπ?Êú?Êó?‰∏ãÂçà2:21:06 
 *  
 */
public class TestBusdataadelaayCleaning {
	@Test
	public void  testDate(){
		//TimeZone.setDefault(TimeZone.getTimeZone("Europe/Helsinki"));
		//DateUtil.date2timeFromZero(1445870778026l);
		BitSet b = new BitSet();
		b.set(0);
		b.set(0, 0, false);
		b.set(3);
		b.set(4);
		ArrayList<BitSet> regionsList = SubBitSetList.INSTANCE.getSub(b);
		for (BitSet bitSet : regionsList) {
			System.out.println(bitSet);
		}
	}
}
