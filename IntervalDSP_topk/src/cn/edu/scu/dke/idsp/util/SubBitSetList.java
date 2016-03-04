package cn.edu.scu.dke.idsp.util;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;

/**
 * author: Hao 
 * date:2015年3月6日
 * time:下午3:29:59
 * purpose: Get the all sub BitSet of a BitSet
 */
public enum SubBitSetList {
	INSTANCE;
	public ArrayList<BitSet> getSub(BitSet o) {
		ArrayList<BitSet> subList = new ArrayList<BitSet>();
		subList.add(o);
		for (int i = o.nextSetBit(0); i >= 0; i = o.nextSetBit(i + 1)) {
			HashSet<BitSet> newBS = new HashSet<BitSet>();
			for(BitSet copy:subList){
				BitSet clone = (BitSet)copy.clone();
				clone.set(i, false);
				if(clone.cardinality() != 0)
					newBS.add(clone);
			}
			subList.addAll(newBS);
		}
		return subList;
	}
}
