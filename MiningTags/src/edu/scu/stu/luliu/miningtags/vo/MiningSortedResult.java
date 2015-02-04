package edu.scu.stu.luliu.miningtags.vo;

import java.util.List;
import java.util.Map;

public class MiningSortedResult {
	private Product posProdect;//第一个产品
	private Product negProdect;//第二个产品
	private List<String> wordlist;//文字云数据
	/**
	 * 挖掘出的对应数量的关联数据：第0个列表代表数量为1的关联规则，第1个列表代表数量为关联规则为2的关联规则
	 */
	private List<List<String>> relevancelist;
	private Map<String, String> highlights;//标签高亮的数据
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