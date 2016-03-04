package cn.edu.scu.dke.idsp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.edu.scu.dke.idsp.vo.PatternInterval;

/** 
 * @ClassName: PatternListUtil 
 * @Description: TODO
 * @author yanli
 * @date 2016年1月1日 下午1:01:38 
 *  
 */
public class DateUtil {
	/** 
	 * @Title: sortWindowlist 
	 * @Description: TODO     设定文件 
	 * @return void    返回类型 
	 * @throws 
	 */ 
	public static long date2timeFromZero(long date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date datetime = new Date();
		datetime.setTime(date);
		String datestr = format.format(datetime);
		datestr = datestr.substring(0, 10).trim()+" 00:00:00";
		long starttime = 0;
		try {
			starttime = format.parse(datestr).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date -  starttime;
	}
	
	public static String long2Time(long date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date datetime = new Date();
		datetime.setTime(date);
		String datestr = format.format(datetime);
		return datestr;
	}

	/** 
	 * @Title: time2DateTimeIntervals 
	 * @Description: TODO 
	 * @param timearr
	 * @param time long型的时间
	 * @return    设定文件 
	 * @return long [] []    返回类型 
	 * @throws 
	 */ 
	public static long[][] time2DateTimeIntervals(long[][] timearr,
			long time) {
		Date date = new Date();
		date.setTime(time);
		long[][] curTimeArr = new long[timearr.length][2];
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String datestr = format.format(date).substring(0, 10).trim()+" 00:00:00";
		long starttime = 0;
		try {
			starttime = format.parse(datestr).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < curTimeArr.length; i++) {
			curTimeArr[i][0] = timearr[i][0]+starttime;
			curTimeArr[i][1] = timearr[i][1]+starttime;
		}
		return curTimeArr;
	}
	
}
