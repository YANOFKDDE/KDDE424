package cn.edu.scu.dke.idsp;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import cn.edu.scu.dke.idsp.util.BitSetUtils;
import cn.edu.scu.dke.idsp.util.DateUtil;
import cn.edu.scu.dke.idsp.util.SubBitSetList;
import cn.edu.scu.dke.idsp.vo.Element;
import cn.edu.scu.dke.idsp.vo.ElementsInfo;
import cn.edu.scu.dke.idsp.vo.Window;

public enum GenerateCandidateElement {
	INSTANCE;
	public void genCE2(List<String> posData, List<String> negData,
			ElementsInfo posEI, ElementsInfo negEI, int l) {
		long minTime = Long.MAX_VALUE;// 保存数据集中最小的时间
		long maxTime = Long.MIN_VALUE;// 保存数据集中最大的时间
		long interval = 0; // 所有数据所跨过的时间区间
		long remainder = 0; // 余数
		long[][] timelist = new long[l][2];// 时间区间划分小时long，后面会加上每天的开始时间，构成完整的事件
		HashMap<String, Element> pos_eMap = new HashMap<String, Element>();
		HashMap<Integer, List<Long>> pos_timeMap = new HashMap<Integer, List<Long>>();
		HashMap<String, Element> neg_eMap = new HashMap<String, Element>();
		HashMap<Integer, List<Long>> neg_timeMap = new HashMap<Integer, List<Long>>();
		List<Integer> pos_lenList = new ArrayList<Integer>();
		List<Integer> neg_lenList = new ArrayList<Integer>();
		List<String> posdataSeqList = new ArrayList<String>();
		List<String> negdataSeqList = new ArrayList<String>();
		// 时间区间划分
		for (String seq : posData) {
			if (!seq.trim().equals("")) {
				posdataSeqList.add(seq.trim());
				String[] tempArrChar = seq.trim().split(",");
				long min = Long.valueOf(tempArrChar[0].split(":")[1]);
				long max = Long.valueOf(tempArrChar[tempArrChar.length - 1]
						.split(":")[1]);
				min = DateUtil.date2timeFromZero(min);
				max = DateUtil.date2timeFromZero(max);
				if (minTime > min) {
					minTime = min;
				}
				if (maxTime < max) {
					maxTime = max;
				}
			}
		}
		for (String seq : negData) {
			if (!seq.trim().equals("")) {
				negdataSeqList.add(seq.trim());
				String[] tempArrChar = seq.trim().split(",");
				long min = Long.valueOf(tempArrChar[0].split(":")[1]);
				long max = Long.valueOf(tempArrChar[tempArrChar.length - 1]
						.split(":")[1]);
				min = DateUtil.date2timeFromZero(min);
				max = DateUtil.date2timeFromZero(max);
				if (minTime > min) {
					minTime = min;
				}
				if (maxTime < max) {
					maxTime = max;
				}
			}
		}
		interval = (maxTime - minTime + 1) / l;
		remainder = (maxTime - minTime + 1) % l;
		remainder = l - remainder;
		timelist[0][0] = minTime;
		timelist[0][1] = minTime + interval - 1;
		for (int i = 1; i < l; i++) {
			if (i < remainder) {
				timelist[i][0] = timelist[i - 1][1] + 1;
				timelist[i][1] = timelist[i][0] + interval - 1;
			} else {
				timelist[i][0] = timelist[i - 1][1] + 1;
				timelist[i][1] = timelist[i][0] + interval;
			}
		}

		// ******************************正列**********************************
		Window poswindow = GenerateWindows(posdataSeqList, timelist, l);
		/** collect the position occurrence information **/
		for (int seqId = 0; seqId < posdataSeqList.size(); seqId++) {
			String tmpStr = posdataSeqList.get(seqId);
			String[] tempArrChar = tmpStr.split(",");
			List<Long> timelonglist = new ArrayList<>();
			for (int position = 0; position < tempArrChar.length; position++) {
				String value = tempArrChar[position];
				String[] values = value.split(":");
				Element e = pos_eMap.get(values[0]);
				if (e == null) {
					e = new Element(values[0]);
					pos_eMap.put(values[0], e);
				}
				timelonglist.add(Long.valueOf(values[1]));
				e.addPOccurrence(seqId, position);
//				e.addSubintOcur2element(seqId, position, poswindow);

				e.setIntervalsBitSet(seqId, position, poswindow);
			}
			pos_timeMap.put(seqId, timelonglist);
			/** collect the length information **/
			pos_lenList.add(tempArrChar.length);
		}

		posEI.seteMap(pos_eMap);
		posEI.setLenList(pos_lenList);
		posEI.setTimeMap(pos_timeMap);
		posEI.setWindow(poswindow);

		// ******************************负列**********************************
		Window negwindow = GenerateWindows(negdataSeqList, timelist, l);
		/** collect the negition occurrence information **/
		for (int seqId = 0; seqId < negdataSeqList.size(); seqId++) {
			String tmpStr = negdataSeqList.get(seqId);
			String[] tempArrChar = tmpStr.split(",");
			List<Long> timelonglist = new ArrayList<>();
			for (int position = 0; position < tempArrChar.length; position++) {
				String value = tempArrChar[position];
				String[] values = value.split(":");
				Element e = neg_eMap.get(values[0]);
				if (e == null) {
					e = new Element(values[0]);
					neg_eMap.put(values[0], e);
				}
				timelonglist.add(Long.valueOf(values[1]));
				e.addPOccurrence(seqId, position);
//				e.addSubintOcur2element(seqId, position, negwindow);
				e.setIntervalsBitSet(seqId, position, negwindow);
			}
			neg_timeMap.put(seqId, timelonglist);
			/** collect the length information **/
			neg_lenList.add(tempArrChar.length);
		}

		negEI.seteMap(neg_eMap);
		negEI.setLenList(neg_lenList);
		negEI.setTimeMap(neg_timeMap);
		negEI.setWindow(negwindow);
	}

	/**
	 * @Title: GenerateWindows
	 * @Description: 生成window起始位置信息
	 * @param posLenList
	 *            正列组序列长度
	 * @param negLenList
	 *            负列组序列长度
	 * @param l
	 *            window的长度
	 * @return Window 返回类型
	 * @throws
	 */
	public Window GenerateWindows(List<String> dataSeqList, long[][] timearr,
			int l) {
		Window window = new Window();
		window.setL(l);
		BitSet region = new BitSet(l);
		region.set(0, l);
		window.setRegion(region);
		ArrayList<BitSet> regionsList = SubBitSetList.INSTANCE.getSub(region);
		for (BitSet bitSet : regionsList) {
			window.getWindows().add(bitSet);
//			if (BitSetUtils.checkContinue(bitSet)) {
//				window.getContinueWindows().add(bitSet);
//			}
			if (bitSet.cardinality() == 1) {
				window.getWindowlist().add(bitSet);
			}
		}
		window.sortWindowlist();
		window.sortWindows();
//		window.sortContinueWindows();
		computeWindowScope(dataSeqList, timearr, l, window);// 计算正列组的window起始信息
		return window;
	};


	/**
	 * @Title: computeWindowScope
	 * @Description: 根据sequence
	 *               list中sequence的长度，和window数量均匀划分sequence长度，获取window和window组合的起始位置
	 * @param lenList
	 *            序列组的长度列表
	 * @param l
	 *            window的数量
	 * @param window
	 *            保存信息的window对象
	 * @param pos
	 *            正列或负列
	 * @return void 返回类型
	 * @throws
	 */
	private void computeWindowScope(List<String> dataSeqList, long[][] timearr,
			int l, Window window) {
		Map<String, Map<Integer, Integer[]>> windowScope;
		windowScope = window.getWindowScope();
		for (int i = 0; i < dataSeqList.size(); i++) {
			String seq = dataSeqList.get(i);
			String[] tempArrChar = seq.split(",");
			long[][] curTimeArr = DateUtil.time2DateTimeIntervals(timearr,
					Long.valueOf(tempArrChar[0].split(":")[1]));
			// 针对window的组合信息，把window和window的组合起始位置计算出来保存到window对象中
			for (BitSet bitSet : window.getWindows()) {
				int cardinality = bitSet.cardinality();
				if (cardinality == 1) {// 单个window的起始位置信息
					int windex = bitSet.nextSetBit(0);
					int countOfInterval = 0;
					Integer[] scope = { -1, -1 };
					long startTime = curTimeArr[windex][0];
					long endTime = curTimeArr[windex][1];
					int j = 0;
					for (; j < tempArrChar.length; j++) {
						String[] value = tempArrChar[j].split(":");
						long tmpTime = Long.valueOf(value[1]);
						if (startTime <= tmpTime && tmpTime <= endTime) {
							if (countOfInterval == 0) {
								scope[0] = j;
							}
							countOfInterval++;
						} else {
							if (countOfInterval > 0) {
								scope[1] = j - 1;
								break;
							}
						}
					}
					if (countOfInterval > 0 && scope[1] == -1) {
						scope[1] = j - 1;
					}
					Map<Integer, Integer[]> map = windowScope.get(bitSet
							.toString());
					if (map == null) {
						map = new HashMap<Integer, Integer[]>();
						windowScope.put(bitSet.toString(), map);
					}
					map.put(i, scope);
				} else if (cardinality > 1) {// window组合的起始位置信息
					// 合并window组合的信息
					List<Integer> indexlist = new ArrayList<Integer>();// 保存window组合信息合并后的起始位置，以成对方式放入list中
					List<Long> timeIndexlist = new ArrayList<Long>();// 保存window组合信息合并后的起始位置时间信息
					int windex = bitSet.nextSetBit(0);
					int preWindex = bitSet.nextSetBit(0);
					long startTime = curTimeArr[windex][0];
					long endTime = curTimeArr[windex][1];
					timeIndexlist.add(startTime);
					for (int j = 1; j < cardinality; j++) {
						windex = bitSet.nextSetBit(windex + 1);
						if (windex - preWindex == 1) {
							preWindex = windex;
							startTime = curTimeArr[windex][0];
							endTime = curTimeArr[windex][1];
						} else {
							timeIndexlist.add(endTime);
							startTime = curTimeArr[windex][0];
							endTime = curTimeArr[windex][1];
							timeIndexlist.add(startTime);
							preWindex = windex;
						}
					}
					timeIndexlist.add(endTime);
					for (int x = 0; x < timeIndexlist.size(); x++) {
						long start = timeIndexlist.get(x);
						long end = timeIndexlist.get(++x);
						int count = 0;
						int z = 0;
						for (; z < tempArrChar.length; z++) {
							String[] value = tempArrChar[z].split(":");
							long tmpTime = Long.valueOf(value[1]);
							if (start <= tmpTime && tmpTime <= end) {
								if (count == 0) {
									indexlist.add(z);
								}
								count++;
							} else {
								if (count > 0) {
									indexlist.add(z - 1);
									break;
								}
							}
						}
						if (count > 0 && indexlist.size() % 2 != 0) {
							indexlist.add(z - 1);
						}
						if (count == 0) {
							indexlist.add(-1);
							indexlist.add(-1);
						}
					}

					Integer[] scope = (Integer[]) indexlist
							.toArray(new Integer[indexlist.size()]);
					Map<Integer, Integer[]> map = windowScope.get(bitSet
							.toString());
					if (map == null) {
						map = new HashMap<Integer, Integer[]>();
						windowScope.put(bitSet.toString(), map);
					}
					map.put(i, scope);
				}
			}
		}
	}
}
