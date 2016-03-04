/**   
 * @Title: IDSP.java 
 * @Package cn.edu.scu.dke.idsp 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2015年9月6日 下午10:16:09 
 * @version V1.0   
 */
package cn.edu.scu.dke.idsp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import cn.edu.scu.dke.idsp.util.FileUtils;
import cn.edu.scu.dke.idsp.vo.ElementsInfo;
import cn.edu.scu.dke.idsp.vo.PatternInterval;
import cn.edu.scu.dke.idsp.vo.Results;

/**
 * @ClassName: IDSP
 * @Description: IDSP挖掘算法
 * @author yanli
 * @date 2015年9月6日 下午10:16:09
 * 
 */
public class DTSP_continuous_test {

	private int l = 3; // 等长度区间划分块数
	private double a = 0.1;//
	private double b = 0.1;//
	private long minGap = 0;// gap起始位置
	private long maxGap = 600;// gap结束位置:以时间为单位，秒
	private String posPath = "./data/DNA/1_posCbi.posCbi";
	private String negPath = "./data/DNA/1_negCbi.negCbi";
	private Results results;// 结果集

	/**
	 * <p>
	 * Title:
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param l
	 * @param k
	 * @param minGap
	 * @param maxGap
	 * @param pos
	 * @param neg
	 */
	public DTSP_continuous_test(int l, double a, double b, int minGap,
			int maxGap, String pos, String neg) {
		super();
		this.l = l;
		this.a = a;
		this.b = b;
		this.minGap = minGap;
		this.maxGap = maxGap;
		this.posPath = pos;
		this.negPath = neg;
	}

	/**
	 * <p>
	 * Title:
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 */
	public DTSP_continuous_test() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @Title: run
	 * @Description: 执行挖掘算法
	 * @param
	 * @return Results 返回类型
	 * @throws
	 */
	public Results run(List<String> posData, List<String> negData) {
		ElementsInfo posEI = new ElementsInfo();
		ElementsInfo negEI = new ElementsInfo();
		GenerateCandidateElement.INSTANCE.genCE2(posData, negData, posEI,
				negEI, l);
		// posEI.printInfo();
		// negEI.printInfo2();
		GenerateCandidatePattern gcp = new GenerateCandidatePattern(posEI,
				negEI, l, a, b, minGap, maxGap);
		gcp.genCP();
		results = gcp.getResults();
		results.count = gcp.trycount;
		return results;
	}

	/**
	 * @Title: main
	 */
	public static void main(String[] args) {

		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Helsinki"));
		//
		// int l = 3; // 等长度区间划分块数
		// double a = 0.6;//
		// double b = 0.4;//
		// int minGap = 0;// gap起始位置
		// int maxGap = 3600000;// gap结束位置:以时间为单位，毫秒
		// String posPath = "E:/YAN LI/研究生/研二上/testData/pos_11111.txt";
		// String negPath = "E:/YAN LI/研究生/研二上/testData/neg_11111.txt";
		int l = 1; // 等长度区间划分块数
		double a = 0.5;//
		double b = 0.4;//
		int minGap = 0;// gap起始位置
		int maxGap = 300000;// gap结束位置:以时间为单位，毫秒
		// String stopcodesPath =
		// "E:/YAN LI/研究生/研二上/Bus Data/data/stopcode/stopcodes.txt";
		String posPath = "E:/YAN LI/研究生/研二上/Bus Data/data/stopcode/tmp222/501_Monday.txt";
		String negPath = "E:/YAN LI/研究生/研二上/Bus Data/data/stopcode/tmp222/501_NOMonday.txt";
		List<String> posData = new ArrayList<String>();
		List<String> negData = new ArrayList<String>();
		try {
			posData = FileUtils.readFile(posPath);
			negData = FileUtils.readFile(negPath);
		} catch (IOException e) {
			System.out.println("Error in data set file!");
			e.printStackTrace();
		}
		long t0 = System.nanoTime();

		DTSP_continuous_test idsp = new DTSP_continuous_test(l, a, b, minGap,
				maxGap, posPath, negPath);
		Results results = idsp.run(posData, negData);
		results.sort();
		long t1 = System.nanoTime();
		long millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
		String timeCost = String.format("time took: %d ms", millis);
		String timeCostM = String.format("time took: %s m",
				String.valueOf(millis / 60000.0));
		List<String> resultlist = new ArrayList<String>();
		resultlist.add(String.valueOf(results.count));
		for (PatternInterval p : results.getpList()) {
			resultlist.add(p.print());
		}
		resultlist.add(String.valueOf(results.getSize()));
		System.out.println(timeCost);
		resultlist.add(timeCost);
		System.out.println(timeCostM);
		resultlist.add(timeCostM);
		results.statistics();
		resultlist.add(results.toString());
		// String filename =
		// "E:/YAN LI/研究生/研二上/Bus Data/data/stopcode/result/mon_nomon/7.333333333_.txt";
		String filename = "E:/YAN LI/研究生/研二上/Bus Data/data/stopcode/newresult/test/8888_00000_"
				+ l
				+ "_"
				+ a
				+ "_"
				+ b
				+ "_"
				+ minGap
				+ "_"
				+ maxGap
				+ new Date().getTime() + ".txt";
		FileUtils.write2file(resultlist, filename);
	}

	public int getL() {
		return l;
	}

	public void setL(int l) {
		this.l = l;
	}

	public void setMaxGap(int maxGap) {
		this.maxGap = maxGap;
	}

	public String getPos() {
		return posPath;
	}

	public void setPos(String pos) {
		this.posPath = pos;
	}

	public String getNeg() {
		return negPath;
	}

	public void setNeg(String neg) {
		this.negPath = neg;
	}

	public Results getResults() {
		return results;
	}

	public void setResults(Results results) {
		this.results = results;
	}

}
