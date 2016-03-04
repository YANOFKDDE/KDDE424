package cn.edu.scu.dke.idsp.util;


/**
 * author: Hao 
 * date:2015年3月5日
 * time:下午2:44:52
 * purpose:
 */
public enum IntervalHelper {
	INSTANCE;
	public boolean isInIthInterval(int loc, int iTh, int seqLength, int l){
		boolean isIn = false;
		if(loc >= seqLength || loc < 0)
			return isIn;
		int inLen = seqLength / l;
		int plusOne = seqLength % l;
		int start;
		int end;
		if(iTh < plusOne){
			start = (inLen + 1) * iTh;
			end = start + (inLen + 1);
		}
		else{
			start = plusOne * (inLen + 1) + (iTh - plusOne) * inLen;
			end = start + inLen;
		}
		if(loc >= start && loc < end)
		     isIn = true;
//		System.out.println("     i:"+iTh+" "+isIn+" len:"+seqLength+" l:"+l+" loc:"+loc);
		return isIn;
	}
}
