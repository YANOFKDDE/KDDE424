package cn.edu.scu.dke.idsp.util;

import java.util.BitSet;

/**
 * author: Hao 
 * date:2015年3月12日
 * time:下午4:59:09
 * purpose: clear the 1 value in the regions that is masked
 */
public enum RegionClear {
	INSTANCE;
	
	public void clear(BitSet pO, int l, BitSet regions, int seqLength){
		for (int loc = pO.nextSetBit(0); loc >= 0; loc = pO.nextSetBit(loc+1)) {
			/** find the loc of TRUE value in the position **/
		    for(int iTh = 0 ; iTh < l ; iTh ++){
		    	if(IntervalHelper.INSTANCE.isInIthInterval(loc, iTh, seqLength, l)){
		    		/** find the region which this TRUE value (loc) belongs to **/
		    		if(!regions.get(iTh)){
		    			/** if the region is masked(value is false), clear it **/
		    			pO.set(loc, false);
		    		}
		    	}
		    }
		 }
	}
}
