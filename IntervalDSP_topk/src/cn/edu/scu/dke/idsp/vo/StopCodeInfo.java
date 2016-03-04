package cn.edu.scu.dke.idsp.vo;

/** 
 * @ClassName: StopCodeInfo 
 * @Description: TODO
 * @author yanli
 * @date 2016年1月7日 下午3:44:48 
 *  
 */
public class StopCodeInfo implements Comparable<StopCodeInfo> {
	private String stopcode;
	private double avgCRatio;//越大越好
	private double avgPatternCount;//越大越好
	private double avgWindowSize;//越小越好
	private double avgPatternSize;//越大越好
	
	/** 
	 * <p>Title: </p> 
	 * <p>Description: </p>  
	 */ 
	public StopCodeInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	/** 
	 * <p>Title: </p> 
	 * <p>Description: </p> 
	 * @param stopcode
	 * @param avgCRatio
	 * @param avgPatternCount
	 * @param avgWindowSize
	 * @param avgPatternSize 
	 */ 
	public StopCodeInfo(String stopcode, double avgCRatio,
			double avgPatternCount, double avgWindowSize, double avgPatternSize) {
		super();
		this.stopcode = stopcode;
		this.avgCRatio = avgCRatio;
		this.avgPatternCount = avgPatternCount;
		this.avgWindowSize = avgWindowSize;
		this.avgPatternSize = avgPatternSize;
	}
	
	 @Override
	 public int compareTo(StopCodeInfo b) {
		double avgcratio =  this.avgCRatio - b.avgCRatio;
		if (avgcratio!=0) {
			return avgcratio>0?1:-1;
		}
		double avgpatternCount =  this.avgPatternCount - b.avgPatternCount;
		if (avgpatternCount!=0) {
			return avgpatternCount>0?1:-1;
		}
		double avgwindowSize =  this.avgWindowSize - b.avgWindowSize;
		if (avgwindowSize!=0) {
			return avgwindowSize>0?1:-1;
		}
		double avgpatternSize =  this.avgPatternSize - b.avgPatternSize;
		return avgpatternSize>0?1:-1;
	 }
	public String getStopcode() {
		return stopcode;
	}
	public void setStopcode(String stopcode) {
		this.stopcode = stopcode;
	}
	public double getAvgCRatio() {
		return avgCRatio;
	}
	public void setAvgCRatio(double avgCRatio) {
		this.avgCRatio = avgCRatio;
	}
	public double getAvgPatternCount() {
		return avgPatternCount;
	}
	public void setAvgPatternCount(double avgPatternCount) {
		this.avgPatternCount = avgPatternCount;
	}
	public double getAvgWindowSize() {
		return avgWindowSize;
	}
	public void setAvgWindowSize(double avgWindowSize) {
		this.avgWindowSize = avgWindowSize;
	}
	public double getAvgPatternSize() {
		return avgPatternSize;
	}
	public void Pattern(double avgPatternSize) {
		this.avgPatternSize = avgPatternSize;
	}
	public void setAvgPatternSize(double avgPatternSize) {
		this.avgPatternSize = avgPatternSize;
	}
	@Override
	public String toString() {
		return "StopCodeInfo [stopcode=" + stopcode + ", avgCRatio="
				+ avgCRatio + ", avgPatternCount=" + avgPatternCount
				+ ", avgWindowSize=" + avgWindowSize + ", avgPatternSize="
				+ avgPatternSize + "]";
	}
	
}
