package edu.scu.stu.luliu.miningtags.vo;

import java.util.List;


public class MiningResult {
	private Product posProdect;
	private Product negProdect;
	private TreeNode[] result;
	private List<TreeNode> oneItem;
	public MiningResult(Product posProdect,Product negProdect,TreeNode[] result,List<TreeNode> oneItem)
	{
		this.posProdect=posProdect;
		this.negProdect=negProdect;
		this.result=result;
		this.oneItem=oneItem;
	}
	public List<TreeNode> getOneItem() {
		return oneItem;
	}
	public void setOneItem(List<TreeNode> oneItem) {
		this.oneItem = oneItem;
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
	public TreeNode[] getResult() {
		return result;
	}
	public void setResult(TreeNode[] result) {
		this.result = result;
	}
	
}
