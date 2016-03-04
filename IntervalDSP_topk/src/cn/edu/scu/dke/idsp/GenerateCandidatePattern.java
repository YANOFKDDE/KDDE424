package cn.edu.scu.dke.idsp;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.edu.scu.dke.idsp.util.BitSetUtils;
import cn.edu.scu.dke.idsp.util.PatternListUtil;
import cn.edu.scu.dke.idsp.util.SubBitSetList;
import cn.edu.scu.dke.idsp.vo.Element;
import cn.edu.scu.dke.idsp.vo.ElementsInfo;
import cn.edu.scu.dke.idsp.vo.Pattern;
import cn.edu.scu.dke.idsp.vo.PatternInterval;
import cn.edu.scu.dke.idsp.vo.Results;
import cn.edu.scu.dke.idsp.vo.Window;

public class GenerateCandidatePattern {
	/** ��� */
	private Results results;
	/** ���� */
	private ElementsInfo posEI;
	/** ���� */
	private ElementsInfo negEI;
	/** �ȳ������仮�ֿ��� */
	private int l;
	/** ���������� */
	private double posSize;
	/** ���������� */
	private double negSize;
	/** ����֧�ֶ� */
	private double a;
	/** ����֧�ֶ� */
	private double b;
	/** gap��ʼλ�� */
	private long minGap;
	/** gap����λ�� */
	private long maxGap;
	private Window posWindow;
	private Window negWindow;
	private Map<BitSet, Integer> windowsIndexMap;

	public int trycount = 0;

	public GenerateCandidatePattern(ElementsInfo posEI, ElementsInfo negEI,
			int l, double a, double b, long minGap, long maxGap) {
		this.results = new Results(a, b);
		this.posEI = posEI;
		this.negEI = negEI;
		this.posSize = posEI.getLenList().size();
		this.negSize = negEI.getLenList().size();
		this.a = a;
		this.b = b;
		this.l = l;
		this.minGap = minGap;
		this.maxGap = maxGap;
		posWindow = posEI.getWindow();
		negWindow = negEI.getWindow();
		windowsIndexMap = new HashMap<BitSet, Integer>();
		for (int i = 0; i < posWindow.getWindows().size(); i++) {
			windowsIndexMap.put(posWindow.getWindows().get(i), i);
		}
	}

	/** generate candidate patterns **/
	public void genCP() {

		Set<String> elements = posEI.geteMap().keySet();
		// window.print();
        IntervalClean(elements);
		// for (Pattern pattern : patterns) {
		// pattern.print();
		// }
		for (String e : elements) {
			Pattern pattern = Generate_Candidate_Pattern(e, posEI, negEI);
			/** pruning 4 **/
			if (checkImpossible(e)) {
				continue;
			}
			grows(pattern, elements, 1);
		}
	}

	/** 
	 * @Title: IntervalClean 
	 * @Description: ����element��������֧�ֶ�С��a�����䣬�����Ժ���pattern����element��ʱ������жϿ��õ�pattern
	 * @param elements    �趨�ļ� 
	 * @return void    �������� 
	 * @throws 
	 */ 
	private void IntervalClean(Set<String> elements) {
		Element poselement = null;
		Element negelement = null;
		List<String> posremovelist = new ArrayList<String>();//��������Ҫɾ����element
		List<String> negremovelist = new ArrayList<String>();//��������Ҫɾ����element
		for (String elementStr : elements) {
			poselement = posEI.geteMap().get(elementStr);
			negelement = negEI.geteMap().get(elementStr);
			if (poselement.getpOccurrence().size() / posSize < results.getA()) {
				posremovelist.add(elementStr);
				negremovelist.add(elementStr);
				continue;
			} 
			
		}
		for (int i = 0; i < posremovelist.size(); i++) {
			posEI.geteMap().remove(posremovelist.get(i));
			negEI.geteMap().remove(negremovelist.get(i));
		}
		
	}

	public Pattern Generate_Candidate_Pattern(String element,
			ElementsInfo posEI, ElementsInfo negEI) {
		HashMap<String, Element> posEMap = posEI.geteMap();
		HashMap<String, Element> negEMap = negEI.geteMap();
		HashMap<Integer, BitSet> pos_occurrence = new HashMap<Integer, BitSet>(); //�������д�����ϵĳ�����Ϣ
		HashMap<Integer, BitSet> neg_occurrence = new HashMap<Integer, BitSet>(); //���渺�д�����ϵĳ�����Ϣ
		BitSet upperbound = posEMap.get(element).getUpperboundBitSet();//element��upperbound
		BitSet negupperbound = negEMap.get(element).getUpperboundBitSet();//element��upperbound
		upperbound.or(negupperbound);//�ϲ����к͸������Ͻ죻 
		List<BitSet> ignoreList = new ArrayList<BitSet>();// ��������䲻���㣬��ô����ֱ�Ӻ��������������
		double posSup = posEMap.get(element).getpOccurrence().size();
		if (posSup == 0 || (posSup / posSize) < results.getA()) {
			return null;
		}
		Pattern pattern = new Pattern(this.l);
		pattern.setPattern(element);
		// ���ø���window��window��ϵ�bitsetֵ
		pattern.setRegions(upperbound);
		PatternInterval patternInterval = new PatternInterval();
		patternInterval.setRegions(upperbound);
		patternInterval.setPattern(element);
		pos_occurrence = getOccurrenceByRegion(upperbound, posEMap.get(element).getpOccurrence(), posWindow, true);
		patternInterval.setPosOccurrence(pos_occurrence);
		Element negElement = negEMap.get(element);
		if (negElement != null) {
			neg_occurrence = getOccurrenceByRegion(upperbound, negEMap.get(element).getpOccurrence(), negWindow, false);
			patternInterval.setNegOccurrence(neg_occurrence);
		}
		patternInterval.setPosSup(patternInterval.getPosOccurrence().size()
				/ posSize);
		patternInterval.setNegSup(patternInterval.getNegOccurrence().size()
				/ negSize);
		patternInterval.setcRatio();
		pattern.setUpperboundPI(patternInterval);
		int index = windowsIndexMap.get(upperbound);
		for (; index < windowsIndexMap.size(); index++) {// �������µ�window���,������upperbound�����Ӽ�,���µ������Ҫȥ�����������
			BitSet region = posWindow.getWindows().get(index);
			String regions = region.toString();
			if (!BitSetUtils.checkContinue(region)) {//ֻ��������������
				continue;
			}
			if (isIgnoreListContainRegion(ignoreList, region)) {
				continue;
			}
			pos_occurrence = getOccurrenceByRegion(region, posEMap.get(element).getpOccurrence(), posWindow,
					true);
			double posSup2 = pos_occurrence.size();
			if (posSup2 == 0 || posSup2 / posSize < results.getA()) {
				ignoreList.add(region);
				continue;
			}
			patternInterval = new PatternInterval();
			patternInterval.setRegions(region);
			patternInterval.setPattern(element);
			patternInterval.setPosOccurrence(pos_occurrence);
			negElement = negEMap.get(element);
			if (negElement != null) {
				neg_occurrence = getOccurrenceByRegion(region,
						negEMap.get(element).getpOccurrence(),
						negWindow, false);
				patternInterval.setNegOccurrence(neg_occurrence);
			}
			patternInterval.setPosSup(patternInterval.getPosOccurrence().size()
					/ posSize);
			patternInterval.setNegSup(patternInterval.getNegOccurrence().size()
					/ negSize);
			patternInterval.setcRatio();
			pattern.addPatternInterval(BitSetUtils.BitSet2Index(region),patternInterval);
		}
		//��element���Ͻ������峤��Ϊһ��pattern���Ͻ�
		pattern.setUpperboundBitSet(upperbound);
		// PatternListUtil.sortPatternIntervalList(pattern.getPatternlist());
		return pattern;
	}

	/**
	 * 
	 * @Title: getOccurrenceByRegion
	 * @Description: ����window��window��ϵ� ��ʼλ����Ϣ����ȡ��Ӧ��bitset
	 * @param region
	 *            window��window�����Ϣ
	 * @param occurrence
	 *            ȫ����Ϣ
	 * @param window
	 *            window����
	 * @param pos
	 *            ���л���
	 * @return HashMap<Integer,BitSet> ��������
	 * @throws
	 */
	private HashMap<Integer, BitSet> getOccurrenceByRegion(BitSet region,
			HashMap<Integer, BitSet> occurrence, Window window, boolean pos) {
		HashMap<Integer, BitSet> result_occurrence = new HashMap<Integer, BitSet>();
		Map<Integer, Integer[]> windowScope;// ��Ӧ��window��ʼ��Ϣ
		if (pos) {
			windowScope = window.getWindowScope().get(region.toString());
		} else {
			windowScope = window.getWindowScope().get(region.toString());
		}
		for (Integer key : occurrence.keySet()) {
			BitSet cur = (BitSet) occurrence.get(key).clone();
			Integer[] windex = windowScope.get(key);
			regionClear(cur, windex);
			if (cur.cardinality() > 0) {
				result_occurrence.put(key, cur);
			}
		}
		return result_occurrence;
	}

	private void regionClear(BitSet cur, Integer[] windex) {
		BitSet mask = new BitSet(cur.size());
		int size = windex.length / 2;
		for (int i = 0, index = 0; i < size; i++) {
			if (windex[index] >= 0) {
				mask.set(windex[index++], windex[index++] + 1);
			}else {
				index+=2;
			}
		}
		cur.and(mask);
	}

	/**
	 * if the support of an element in positve data sets is less than the Top-k
	 * threshold in Results set, this element is impossible
	 **/
	private boolean checkImpossible(String e) {
		Element element = posEI.geteMap().get(e);
		if (element.getpOccurrence().size() / posSize < results.getA()) {
			System.out.println("Impossible");
			return true;
		} else
			return false;
	}

	/**
	 * 
	 * @Title: grows
	 * @Description: TODO
	 * @param p
	 * @param elements
	 * @param layer
	 *            �趨�ļ�
	 * @return void ��������
	 * @throws
	 */
	private void grows(Pattern p, Set<String> elements, int layer) {
		/** check the pattern p in different condition of regions **/
		boolean continueFlag = checkPattern(p);
		if (!continueFlag) {
			/** pruning rule 6 **/
			/** this pattern is not existed, so does the super=pattern of it **/
			return;
		}

		/** add a new element to pattern p to generate a new pattern **/
		for (String newE : elements) {
			/** pruning 1 **/
			// TODO pruning rule 1
			trycount++;
			System.out.println(trycount);

			/** pruning 4 **/
			if (checkImpossible(newE)) {
				continue;
			}

			// System.out.println("In? "+layer +
			// " size:"+results.getpList().size());
			/** create new pattern **/
			Pattern newPattern = addNewElement(p.clone(), newE);
			if (newPattern == null) {
				continue;
			}
			grows(newPattern, elements, layer + 1);
		}
	}

	/**
	 * @Title: addNewElement
	 * @Description: ����element�����µ�pattern��������gap�������pattern��λ����Ϣ��
	 * @param p
	 * @param newE
	 * @param posEI2
	 * @param negEI2
	 * @param minGap2
	 * @param maxGap2
	 * @return �趨�ļ�
	 * @return Pattern ��������
	 * @throws
	 */
	private Pattern addNewElement(Pattern p, String newE) {
		String patternStr = p.getPattern() + "," + newE;
		p.setPattern(patternStr);
//		if (patternStr.equals("H,B")) {
//			System.out.println(patternStr);
//		}
		Element posElement = posEI.geteMap().get(newE);
		Element negElement = negEI.geteMap().get(newE);
		PatternInterval[] newPatternIntervalArr = new PatternInterval[p.getPatternInterArr().length];//������pattern���µ�patternInterval 
		int index = 0;//patternInterval ���������
		List<Integer> piList = new ArrayList<Integer>();//����������
		//*******************upperboard generation****************
		BitSet oldUpperbound = (BitSet) p.getUpperboundBitSet().clone();
		BitSet newUpperbound = (BitSet) p.getUpperboundBitSet().clone();
		BitSet tmpbitset = (BitSet) posElement.getUpperboundBitSet().clone();
		int firstTrue = newUpperbound.nextSetBit(0);
		tmpbitset.set(0, firstTrue, false);
		newUpperbound.or(tmpbitset);
		List<BitSet> ignoreList = new ArrayList<BitSet>();// �����ʱ���������и����䲻���㣬��ô����ֱ�Ӻ��Ը��ڵ�������������
		HashMap<Integer, BitSet> pos_last_element = posElement.getpOccurrence(); // ���������һ��element���ֵ�BitSet��
		HashMap<Integer, BitSet> neg_last_element = null; // ���������һ��element���ֵ�BitSet��
		if (negElement != null) {
			neg_last_element = negElement.getpOccurrence();
		}
		/** �����ж���upperboard sequence������ */
		PatternInterval pi = p.getUpperboundPI();
		int posSup = getSupByWindow(pi.getPosOccurrence(), pos_last_element,
				posEI.getTimeMap(), newUpperbound.toString(), true);
		if (posSup == 0) {
			pi.getPosOccurrence().clear();
		}
		/** pruning 2 patternȫ�ּ�֦ */
		if (posSup == 0 || posSup / posSize < results.getA()) {
			return null;
		}
		int negSup = getSupByWindow(pi.getNegOccurrence(), neg_last_element,
				negEI.getTimeMap(), newUpperbound.toString(), false);
		if (negSup == 0) {
			pi.getNegOccurrence().clear();
		}
		pi.setPattern(p.getPattern());
		pi.setPosSup(posSup / posSize);
		pi.setNegSup(negSup / negSize);
		pi.setcRatio();
		pi.setRegions(newUpperbound);
		
		
		// start at upperboard to enumerate the window tree
		int windex = windowsIndexMap.get(newUpperbound);
		for (; windex < windowsIndexMap.size(); windex++) {// �������µ�window���,������upperbound�����Ӽ�,���µ������Ҫȥ�����������
			BitSet newregion = posWindow.getWindows().get(windex);
			if (!BitSetUtils.checkContinue(newregion)) {
				continue;
			}
			if (checkInvalidWindow(newUpperbound,(BitSet)newregion.clone())) {
				continue;
			}
			PatternInterval pitmp = null;
			String newregions = newregion.toString();
//			if (newregions.equals("{1, 3, 7, 8}")) {
//				System.out.println();
//			}
			BitSet region = (BitSet) newregion.clone();//��Ҫ���õĸ��ڵ�Ĵ��ڱ��
			region.and(oldUpperbound);
			/** pruning 4 window�Ӵ�С���㣬��һ��w��Ҫ����������ôw���е����Ӽ�Ҳ������� */
			if (isIgnoreListContainRegion(ignoreList, newregion)) {
				continue;
			}
			if(region.cardinality()==0){//���ڵ���û�б����window��ţ���Ҫ����
				ignoreList.add(newregion);//������Ҫ�����Ľڵ�
				continue;
			}
			pi = p.getPatternInterval(BitSetUtils.BitSet2Index(region));
			if (pi == null) {//��Ϊ���ڵ��и�window<a��û�б����window����Ҫpruning
				ignoreList.add(newregion);//������Ҫ�����Ľڵ�
				continue;
			}
			pitmp = pi.clone();
			posSup = getSupByWindow(pitmp.getPosOccurrence(), pos_last_element,
					posEI.getTimeMap(), newregions, true);
			/** pruning 3 pattern�����֦ */
			if (posSup == 0 || posSup / posSize < results.getA()) {
				ignoreList.add(newregion);//������Ҫ�����Ľڵ�
				continue;
			}
			negSup = getSupByWindow(pitmp.getNegOccurrence(), neg_last_element,
					negEI.getTimeMap(), newregions, false);
			if (negSup == 0) {
				pitmp.getNegOccurrence().clear();
			}
			pitmp.setPattern(p.getPattern());
			pitmp.setPosSup(posSup / posSize);
			pitmp.setNegSup(negSup / negSize);
			pitmp.setcRatio();
			pitmp.setRegions(newregion);
			index = BitSetUtils.BitSet2Index(newregion);
			newPatternIntervalArr[index] = pitmp;//�������Ҫ���pi
			piList.add(index);
			/**
			 * pruning 4 patternInterval
			 * ��С������ĸ���֧�ֶȴ���b��ʱ�򣬰����������Ĵ����䱾�ֿ��Բ��ü����ˣ�����ɾ���ö���
			 */
			// if (pi.getNegSup() > results.getB()) {
			// pList.add(pi);
			// }
		}
		p.setPatternInterArr(newPatternIntervalArr);
		p.setPiIndex(piList);
		//������p���Ͻ�
		p.setUpperboundBitSet(newUpperbound);
		return p;
	}

	/** 
	 * @Title: checkInvalidWindow 
	 * @Description: TODO 
	 * @param newUpperbound
	 * @param clone
	 * @return    �趨�ļ� 
	 * @return boolean    �������� 
	 * @throws 
	 */ 
	private boolean checkInvalidWindow(BitSet newUpperbound, BitSet clone) {
		clone.or(newUpperbound);
		clone.xor(newUpperbound);
		return clone.cardinality()>=1?true:false;
	}

	/**
	 * @Title: isIgnoreMapContainRegion
	 * @Description: ��֤��region�е��������䶼��ignoremap�У�������ڣ���ֱ�ӿ��Ժ��Ը�������ϵļ���
	 * @param ignoreMap
	 * @param regions
	 * @return �趨�ļ�
	 * @return boolean ��������
	 * @throws
	 */
	private boolean isIgnoreListContainRegion(List<BitSet> ignoreList,
			BitSet regions) {
		for (int i = 0; i < ignoreList.size(); i++) {
			BitSet tmp = ignoreList.get(i);
			BitSet tmpRegion = (BitSet)tmp.clone();
			tmpRegion.and(regions);
			if (tmpRegion.cardinality() == regions.cardinality()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @Title: isIgnoreMapContainRegion
	 * @Description: ��֤��region�е��������䶼��ignoremap�У�������ڣ���ֱ�ӿ��Ժ��Ը�������ϵļ���
	 * @param ignoreMap
	 * @param regions
	 * @return �趨�ļ�
	 * @return boolean ��������
	 * @throws
	 */
	private boolean isIgnoreMapContainRegion(List<String> ignoreList,
			String regions) {
		regions = regions.substring(1, regions.length() - 1).trim();
		String[] strarr = regions.split(",");
		int index = 0;// strarr������
		for (int i = 0; i < ignoreList.size(); i++) {
			String[] ignorearr = ignoreList.get(i).split(",");
			for (int j = 0; j < ignorearr.length; j++) {
				String tmp = ignorearr[j];
				if (tmp.equals(strarr[index])) {
					index++;
					if (index > strarr.length-1) {
						return true;
					}
				}
			}
			index = 0;// ÿ�ε���ǰ��������
		}
		return false;
	}

	/**
	 * @Title: addIgnoreInterval
	 * @Description: ��������alpha��������ʱ�����ӿ���ֱ�Ӻ��Ե�time interval
	 * @param ignoreList
	 * @param regions
	 *            �趨�ļ�
	 * @return void ��������
	 * @throws
	 */
	private void addIgnoreInterval(List<String> ignoreMap, String regions) {
		regions = regions.substring(1, regions.length() - 1);
		ignoreMap.add(regions.replaceAll(" ", ""));
	}

	/**
	 * @Title: getSupByWindow
	 * @Description: ����֧�ֶȣ����Ҹ���bitset���ӳ�������windowʱ���Զ�����Pattern����Ӧwindow��bitsetֵ
	 * @param occurrence
	 * @param last_element
	 *            ����һ��Ԫ�ص�bitset����Ϣ
	 * @param pattern
	 *            ����
	 * @param pos
	 *            ���л���
	 * @return int ֧�ֶ�
	 * @throws
	 */
	private int getSupByWindow(HashMap<Integer, BitSet> occurrence,
			HashMap<Integer, BitSet> last_element,
			HashMap<Integer, List<Long>> timeMap, String regions, boolean pos) {
		if (last_element == null) {
			return 0;
		}
		Map<Integer, Integer[]> windowScope;
		if (pos) {
			windowScope = this.posWindow.getWindowScope().get(regions);
		} else {
			windowScope = this.negWindow.getWindowScope().get(regions);
		}
		BitSet temp;
		BitSet origin;
		List<Integer> removelist = new ArrayList<Integer>();// ��Ҫremove��list
		for (Integer seqId : occurrence.keySet()) {
			Integer[] windex = windowScope.get(seqId);
			BitSet b = occurrence.get(seqId);
			BitSet element = last_element.get(seqId);
			List<Long> timelist = timeMap.get(seqId);
			if (element == null) {
				removelist.add(seqId);
				continue;
			}
			temp = (BitSet) b.clone();
			origin = (BitSet) b.clone();
			b.xor(b);
			boolean flag = true;
			do {
				flag = right_shift(origin, temp, minGap, maxGap, 1, timelist);
				b.or(temp);
			} while (flag);
			b.and(element);
			regionClear(b, windex);
			if (b.cardinality() == 0) {
				removelist.add(seqId);
				continue;
			}
		}
		for (Integer key : removelist) {// ɾ������window��bitsetֵ
			occurrence.remove(key);
		}
		return occurrence.size();
	}

	/**
	 * 
	 * @Title: checkPattern
	 * @Description: ���pattern�ڸ���windows�еĶԱȶ�
	 * @param p
	 * @return �趨�ļ�
	 * @return boolean ��������
	 * @throws
	 */
	private boolean checkPattern(Pattern p) {
		// if (p.getPattern().equals("B,I")) {
		// System.out.println();
		// System.out.println(p.getPatternMap().get("{2}").getNegOccurrence().get(14));
		// }
		ArrayList<PatternInterval> pList = new ArrayList<PatternInterval>();// ���ڱ����pattern������windows�еĶԱȶȶ���patterninterval
		PatternInterval pi = p.getUpperboundPI();
//		if(pi == null){
//			 System.out.println();
//		}
		/** pruning 2 patternȫ�ּ�֦ */
		if (pi.getPosSup() == 0 || pi.getPosSup() <= results.getA()) {
			return false;
		}
		if (pi.getPosSup() >= results.getA()
				&& pi.getNegSup() <= results.getB()) {
//			pList.add(pi);
		}
		for (int i = 0; i < p.getPiIndex().size(); i++) {
			int index = p.getPiIndex().get(i);
			pi = p.getPatternInterArr()[index];
			if (pi.getPosSup() >= results.getA()
					&& pi.getNegSup() <= results.getB()) {
				pList.add(pi);
			}
		}
//		for (int i = 1; i < p.getPatternlist().size(); i++) {
//			pi = p.getPatternlist().get(i);
//			if (pi.getPosSup() >= results.getA()
//					&& pi.getNegSup() <= results.getB()) {
//				pList.add(pi);
//			}
//		}
		if (pList.size() == 0) {// ���������Ϊ�գ�˵������patternû������a��b�����������ǿ��Լ���������
			return true;
		}
		/** only add pattern p with the most contrast regions **/
		PatternListUtil.sortPatternIntervalList(pList);
		double maxCR = pList.get(0).getcRatio();
		int index = 0;
		for (int i = 1; i < pList.size(); i++) {
			if (maxCR < pList.get(i).getcRatio()) {
				maxCR = pList.get(i).getcRatio();
				index = i;
			}
		}
		results.addPattern(pList.get(index));
		return false;
	}

	/**
	 * 
	 * @Title: getSup
	 * @Description: ����i-1��BitSet�������һ��element��BitSet��������pattern��֧�ֶ�
	 * @param occurrence
	 *            i-1��BitSet��
	 * @param last_element
	 *            ���һ��element��BitSet��
	 * @return int ֧�ֶ�
	 * @throws
	 */
	private int getSup(HashMap<Integer, BitSet> occurrence,
			HashMap<Integer, BitSet> last_element,
			HashMap<Integer, List<Long>> timeMap) {
		if (last_element == null) {
			return 0;
		}
		BitSet temp;
		BitSet origin;// ԭʼ��bitset�����ڼ���λ�Ƶ���λ�ú�ԭʼλ��֮���ʱ�����
		List<Integer> removelist = new ArrayList<Integer>();
		for (Integer seqId : occurrence.keySet()) {
			BitSet b = occurrence.get(seqId);
			BitSet e = last_element.get(seqId);
			List<Long> timelist = timeMap.get(seqId);
			if (e == null) {
				removelist.add(seqId);
				continue;
			}
			temp = (BitSet) b.clone();
			origin = (BitSet) b.clone();
			b.xor(b);
			boolean flag = true;
			do {
				flag = right_shift(origin, temp, minGap, maxGap, 1, timelist);
				b.or(temp);
			} while (flag);
			b.and(e);
			if (b.cardinality() == 0) {
				removelist.add(seqId);
			}
		}
		for (Integer key : removelist) {
			occurrence.remove(key);
		}
		return occurrence.size();

	}

	/**
	 * 
	 * @Title: support_Region_Count
	 * @Description: TODO
	 * @param p
	 * @param posEI
	 * @param negEI
	 * @param minGap
	 * @param maxGap
	 *            �趨�ļ�
	 * @return void ��������
	 * @throws
	 */
	public void support_Region_Count(PatternInterval pi) {
		/** get the elements from ElementsInfo according the eList **/

		/** compute the support information in the region condition **/
		double posSup = pi.getPosOccurrence().size();
		double negSup = pi.getNegOccurrence().size();
		pi.setPosSup(posSup / posSize);
		pi.setNegSup(negSup / negSize);
		pi.setcRatio();
	}

	/**
	 * 
	 * @Title: right_shift
	 * @Description: ���Ʋ���
	 * @param b
	 *            ���Ƶ�BitSet
	 * @param num
	 *            ���Ʋ���
	 * @return void ��������
	 * @throws
	 */
	public boolean right_shift(BitSet origin, BitSet b, long minGap,
			long maxGap, int num, List<Long> timelist) {
		int count = b.cardinality();
		int length = b.length();
		int index = length - 1;
		int indexOfOrigin = origin.length();
		for (int i = count; i > 0; i--) {
			indexOfOrigin = origin.previousSetBit(indexOfOrigin - 1);
			index = b.previousSetBit(index);
			long mintime = timelist.get(indexOfOrigin);// ��ȡ��ǰλ�õ�ʱ��
			long maxtime = 0;
			if (index + num < timelist.size()) {
				maxtime = timelist.get(index + num);// ��ȡҪ�ƶ�����λ�õ�ʱ��
			}
			long gap = maxtime - mintime;
			if (minGap <= gap && gap <= maxGap) {
				b.set(index + num);
			} else {
				origin.clear(indexOfOrigin);// ���ԭʼ����λ��Ϊ�˱�����temp��λ��һ����
			}
			b.clear(index);
		}
		return b.cardinality() > 0;
	}

	public Results getResults() {
		return results;
	}

}
