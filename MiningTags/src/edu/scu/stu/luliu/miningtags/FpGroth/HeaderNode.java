package edu.scu.stu.luliu.miningtags.FpGroth;

import edu.scu.stu.luliu.miningtags.vo.TreeNode;

public class HeaderNode {
 public Integer posSup;
 public TreeNode next=null;
 public Integer negSup;
 HeaderNode()
 {
	
 }

public HeaderNode( Integer posSup, TreeNode next) {
	super();
	this.posSup = posSup;	
}
 HeaderNode( Integer posSup,Integer negSup ,TreeNode next) {
	this.posSup = posSup;	
	this.negSup=negSup;
}
 
}
