package edu.scu.stu.luliu.miningtags.vo;
import java.util.ArrayList;
import java.util.List;

import edu.scu.stu.luliu.miningtags.vo.TreeNode;
public class TreeNode implements Comparable{
	 public String nodeName;
	 public Integer posCount=0;
	 public Integer negCount=0;
	 public TreeNode nextSameNode;
	 public TreeNode parentNode;
	 public List<TreeNode>childrenNode=new ArrayList<TreeNode>();
	 public TreeNode()
	 {
		 
	 }
	 public TreeNode(String nodeName)
	 {
		 this.nodeName=nodeName;
	 }
	public boolean equals(Object o)
	{
		TreeNode node=(TreeNode)o;
		if(this.nodeName.compareTo(node.nodeName)==0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public int hashCode()
	{
		return this.nodeName.hashCode();
	}
	 public TreeNode(String nodeName,Integer posCount,Integer negCount)
	 {
		 this.nodeName=nodeName;
		 this.posCount=posCount;
		 this.negCount=negCount;	 
	 }
	 
	 public void addNode(TreeNode o)
	 {
		 if(this.compareTo(o)==0)
		 {
			 this.posCount=this.posCount+o.posCount;
			 this.negCount=this.negCount+o.negCount;	 
		 }
		 else
		 {
			 System.out.println("这两个节点不能相加");
		 }	 
	 }
	@Override
	public int compareTo(Object arg0) {
		TreeNode o=(TreeNode)arg0;
		if(this.nodeName.compareTo(o.nodeName)==0)
		{
			return 0;
		}
		else if(this.nodeName.compareTo(o.nodeName)>0)
		{
			return 1;
		}
		else
		{
			return -1;
		}	
	}
	}
