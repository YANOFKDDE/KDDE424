package edu.scu.stu.luliu.miningtags.vo;

import java.util.List;
import java.util.Map;

public class MiningSortedResult {
	private Product posProdect;//��һ����Ʒ
	private Product negProdect;//�ڶ�����Ʒ
	private List<String> wordlist;//����������
	/**
	 * �ھ���Ķ�Ӧ�����Ĺ������ݣ���0���б��������Ϊ1�Ĺ������򣬵�1���б��������Ϊ��������Ϊ2�Ĺ�������
	 */
	private List<List<String>> relevancelist;
	private Map<String, String> highlights;//��ǩ����������
	public MiningSortedResult() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MiningSortedResult(Product posProdect, Product negProdect,
			List<String> wordlist, List<List<String>> relevancelist) {
		super();
		this.posProdect = posProdect;
		this.negProdect = negProdect;
		this.wordlist = wordlist;
		this.relevancelist = relevancelist;
	}
	
	public Product getPosProdect() {
		return posProdect;
	}
	public void setPosProdect(Product posProdect) {
		this.posProdect = posProdect;
	}
	public Product getNegProdect() {
		return negProdect;
	}
	public void setNegProdect(Product negProdect) {
		this.negProdect = negProdect;
	}
	public List<String> getWordlist() {
		return wordlist;
	}
	public void setWordlist(List<String> wordlist) {
		this.wordlist = wordlist;
	}
	public List<List<String>> getRelevancelist() {
		return relevancelist;
	}
	public void setRelevancelist(List<List<String>> relevancelist) {
		this.relevancelist = relevancelist;
	}

	public Map<String, String> getHighlights() {
		return highlights;
	}

	public void setHighlights(Map<String, String> highlights) {
		this.highlights = highlights;
	}
}