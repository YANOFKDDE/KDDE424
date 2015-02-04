package edu.scu.stu.luliu.miningtags.FpGroth;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.scu.stu.luliu.miningtags.tools.ReadInitialFile;
import edu.scu.stu.luliu.miningtags.vo.TreeNode;
public class PFTree {
	private List<TreeNode>oneItems=new ArrayList<TreeNode>();
	private ReadInitialFile readInitialFile=null;
	public TreeNode root=null;
	private int k;
	public TreeNode[] result=null;
 PFTree()
 {
	
 }
 public PFTree(int k,ReadInitialFile readInitialFile)
 {
	 this.k=k;
	 result=new TreeNode[k];
	 //对结果集进行初始化
	 for(int i=0;i<k;i++)
	 {
		 TreeNode node=new TreeNode("",0,0);
		 result[i]=node;
	 }
	 this.readInitialFile=readInitialFile;
 }
 
 public List<TreeNode> getOneItems() {
	return oneItems;
}
public void setOneItems(List<TreeNode> oneItems) {
	this.oneItems = oneItems;
}
/**
  * 利用二分查找插入排序维护结果集，以保证结果集中为当前K个正例负例集中的差值最大的项(先用插排实现下)
  * @param array待查找插入的数组
  * @return 插入后的数组
  */
 public TreeNode[] addResult(TreeNode[] array,TreeNode node)
 {
	 System.out.println("add------------------------------>"+node.nodeName+": [ "+node.posCount+" , "+node.negCount+" ] ");
	if(node.nodeName.equals("65 147"))
	{
		System.out.println();
	}
	 int differ=node.posCount-node.negCount;
	 if(array[0].nodeName.equals(""))
		 array[0]=node;
	 else if((array[array.length-1].posCount-array[array.length-1].negCount)>=differ)
	 {
		 return array;
	 }
	 else if((array[0].posCount-array[0].negCount)<=differ)
	 {
		 for(int j=array.length-2;j>=0;j--)
		 {
			 array[j+1]=array[j];
		 }
		 array[0]=node;
	 }
	 else 
	 {
		 for(int i=0;i<array.length-1;i++)
		 {
			 int bewP=(array[i].posCount-array[i].negCount)-differ;
			 int bewQ=(array[i+1].posCount-array[i+1].negCount)-differ;
			 if(bewP>=0&&bewQ<0)
			 {
				 //将以第i项为起点依次后移
				 for(int j=array.length-2;j>=i+1;j--)
				 {
					 array[j+1]=array[j];
				 }
				 array[i+1]=node;
				 break;
			 }
		 }
	 }
	 for(int m=0;m<array.length;m++)
	 {
		 System.out.println(array[m].nodeName+":---- [ "+array[m].posCount+" , "+array[m].negCount+" ]  "+(array[m].posCount-array[m].negCount));
	 }
	 System.out.println();
	 return array;
 }
 /**
  * 利用二分查找插入排序来维持结果集
  * @param array 待排序数组
  * @param node 待插入节点
  * @param left 指向左边的指针
  * @param right 指向右边的指针
  */
// public int binoryFind(TreeNode[]array,int differ,int left,int right)
// {
//	 int mid=(left+right)/2;
//	 if(left<right&&(array[mid].posCount-array[mid].negCount)<differ)
//	 {
//		 binoryFind(array, differ,mid,right);
//	 }
//	 else if(left<right&&(array[mid].posCount-array[mid].negCount)<differ)
//	 {
//		 binoryFind(array, differ,mid,right);
//	 }
//	 else 
//	 {
//		 return mid;
//	 }
//	 return -1;
// }
 
 /**
  * 返回文件中的一项节点；
  * @param posFileName正例文件名
  * @param negFileName负例文件名
  * @return 一项结点的集合
  */
 public List<TreeNode> findOneItems()
 {
	 List<List<String>> posFileContent=readInitialFile.getPosFile();
	 List<List<String>> negFileContent=readInitialFile.getNegFile();
	 TreeNode node=null;
	 for(int i=0;i<posFileContent.size();i++)
	 {
		 for(int k=0;k<posFileContent.get(i).size();k++)
		 {
			 //一行中没有相同过的item
			 node=new TreeNode(posFileContent.get(i).get(k)+"",1,0);
			 int j=0;
			 for(;j<oneItems.size();j++)
			 {
				 if(oneItems.get(j).equals(node))
				 {
					 break;
				 }
			 }
			 if(j==oneItems.size())
			 {
				oneItems.add(node); 
			 }
			 else
			 {
				 oneItems.get(j).posCount++;
			 }
		 }
	 }
		 /*
		  * 需要修改只保留正例中包含的项
		  * 
		  * */
		 for(int m=0;m<negFileContent.size();m++)
		 {
			 for(int n=0;n<negFileContent.get(m).size();n++)
			 {
				 //一行中没有相同过的item
				 node=new TreeNode(negFileContent.get(m).get(n)+"",0,1);
				 int p=0;
				 for(;p<oneItems.size();p++)
				 {
					 if(oneItems.get(p).equals(node))
					 {
						 break;
					 }
				 }
				 if(p==oneItems.size())
				 {
					oneItems.add(node); 
				 }
				 else
				 {
					 oneItems.get(p).negCount++;
				 }
			 }
		 }
	 return oneItems;
 }
 /**
  * 利用二分查找插入排序 得到按正例负例数据集支持度差值从大到小的排序的集合
  * @param headerTable
  */
 public void quickSortList(List<TreeNode>list,int left ,int right)
 {
	 int dp=0;
	 
	 if(left<right)
	 {
		 dp=partition(list,left,right);
		 quickSortList(list,left,dp-1);
		 quickSortList(list,dp+1,right);
	 }
 }
 /**
  * 快速排序的实现
  * @param left 左边边界
  * @param right 右边边界
  * @return 分割点儿
  */
 public int partition(List<TreeNode>list,int left,int right)
 {
	 TreeNode node=list.get(left);
	 int pivot=list.get(left).posCount-list.get(left).negCount;
	 while(left<right)
	 {
		 while(left<right&&(list.get(right).posCount-list.get(right).negCount)<=pivot)
			 right--;
		 if(left<right)
		 {
			 list.remove(left);
			 list.add(left++, list.get(right-1));
		 }
			
		 while(left<right&&((list.get(left).posCount-list.get(left).negCount)>=pivot))
		 left++;
		 if(left<right)
		 {
			 list.remove(right);
			 list.add(right--, list.get(left));
		 }
	 }
	 list.remove(left);
	 list.add(left,node);
	 return left;
 }
 /**
  * 第一次初始化FPTree
  * @param posFileName 正例文件名
  * @param negFileName 负例文件名
  * @param headTable 表头
  * @return FPTree的根节点
  */
 public TreeNode initalFPTree()
 {
	 List <TreeNode>headTable=new ArrayList<TreeNode>();
	 TreeNode root=new TreeNode();
	 quickSortList(oneItems,0 ,oneItems.size()-1);
//	 System.out.println();
	 for(TreeNode node:oneItems)
	{
//		System.out.print(node.posCount-node.negCount+"  ");
	}
	 int i=0;
	 for(;i<oneItems.size();i++)
	 {
		 if(oneItems.get(i).posCount-oneItems.get(i).negCount<=0)
		 {
			 break;
		 }
	 }
	 headTable.addAll(oneItems.subList(0, i));
//	  System.out.println("第一次初始化表头为：");
	 for(TreeNode node:headTable)
	{
//		System.out.println(node.nodeName+"[ "+node.posCount+", "+node.negCount+" ] ");
	}
	 List<List<TreeNode>> posConditionDataBase=getConditionDataBase(headTable,1);
	 for(List<TreeNode> list:posConditionDataBase)
		 if(list.size()>0)
		 insertTreeNode(root,list,headTable);
	 List<List<TreeNode>> negConditionDataBase=getConditionDataBase(headTable,0);
	 for(List<TreeNode> list:negConditionDataBase)
		 if(list.size()>0)
		 insertTreeNode(root,list,headTable);
	 for(int j=headTable.size()-1;j>=0;j--)
	 {
		 addResult(this.result,headTable.get(j));
		 String newPreNode=headTable.get(j).nodeName;
		 List<TreeNode> heade=new ArrayList<TreeNode>();
		 List<List<TreeNode>>conditionDataBase1= getConditionDataBase(headTable.get(j),heade);
		 bulidFPTree( newPreNode,conditionDataBase1,heade);
	 }
	 
	 return root;
 }
/**
 * 初始化状态数据库和表头列表
 * @param conditionDataBase 需要赋值的状态数据库
 * @param headeTable需要初始化的表头列表
 * @param posFileName正例数据集文件路径
 */
 public void initalHeaderTableCDB(List<List<TreeNode>> conditionDataBase,List<TreeNode>headTable,String posFileName)
 {
	 quickSortList(oneItems,0 ,oneItems.size()-1);
//	 System.out.println();
	 for(TreeNode node:oneItems)
	{
//		System.out.print(node.posCount-node.negCount+"  ");
	}
	 int i=0;
	 for(;i<oneItems.size();i++)
	 {
		 if(oneItems.get(i).posCount-oneItems.get(i).negCount<=0)
		 {
			 break;
		 }
	 }
	 headTable.addAll(oneItems.subList(0, i));
	  System.out.println("第一次初始化表头为：");
	 for(TreeNode node:headTable)
		{
//			System.out.println(node.nodeName+"[ "+node.posCount+", "+node.negCount+" ] ");
		}
	
	 //第二次读文件初始化状态数据库
	 List<List<String>> posFileContent=readInitialFile.getPosFile();
	 for(int j=0;j<posFileContent.size();j++)
	 {
		 List<TreeNode> line=new ArrayList<TreeNode>();
		 for(int m=0;m<headTable.size();m++)
		 {
			 for(int k=0;k<posFileContent.get(j).size();k++)
			 {
				 if((posFileContent.get(j).get(k)+"").equals(headTable.get(m).nodeName))
				 {
					 line.add(headTable.get(m));
				 }
			 }
		 }
		 conditionDataBase.add(line);
	 }
 }
 /**
  * 得到某个文件的状态数据库
  * @param headTable 表头结点
  * @param fileName 文件名
  * @param index  标示文件的类标（正类或者父类）如果等于1标示正类，否则标示负类
  * @return 状态数据库
  */
 public List<List<TreeNode>> getConditionDataBase(List<TreeNode>headTable,int index)
 {
	 List<List<TreeNode>> conditionDataBase=new ArrayList<List<TreeNode>>();
	 List<List<String>> fileContent=null;
	 if(index==1)
	 {
		 fileContent=readInitialFile.getPosFile();
	 }
	 else
	 {
		 fileContent=readInitialFile.getNegFile();
	 }
	 for(int j=0;j<fileContent.size();j++)
	 {
		 List<TreeNode> line=new ArrayList<TreeNode>();
		 for(int m=0;m<headTable.size();m++)
		 {
			 for(int k=0;k<fileContent.get(j).size();k++)
			 {
				 if((fileContent.get(j).get(k)+"").equals(headTable.get(m).nodeName))
				 {
					 if(index==1)
					 line.add(new TreeNode (headTable.get(m).nodeName,1,0));
					 else
					 line.add(new TreeNode (headTable.get(m).nodeName,0,1)); 
				 }
			 }
		 }
		 conditionDataBase.add(line);
	 }
	 return conditionDataBase;
 }
 /**
  * 构建FPTree
  * @param conditionDataBase 当前的状态数据库
  * @param headerTableNew    表头结点；
  */
 public void bulidFPTree(String preNode,List<List<TreeNode>> conditionDataBase,List<TreeNode>newheade)
 {
	 if(newheade.size()==0)
	 {
		 return;
	 }
	 else
	 {
		 TreeNode root=new TreeNode();
		 TreeNode index=root;
		 //将状态数据库中的每一项加入FPTREE中
		 for(List<TreeNode> list:conditionDataBase)
		 {
			 if(list.size()>0)
			 insertTreeNode(index,list,newheade);			 
		 }
		 //表头项是传过来的也就是说在上面生成的额,但是nextsame不是生成的
		for(int i=newheade.size()-1;i>=0;i--)
		{
			TreeNode hn=newheade.get(i);
//			addResult(this.result,hn);
			String newPreNode="";
			newPreNode=preNode+" "+hn.nodeName;
			addResult(this.result,new TreeNode(newPreNode,hn.posCount,hn.negCount));
			List<TreeNode> heade=new ArrayList<TreeNode>();
			List<List<TreeNode>>conditionDataBase1= getConditionDataBase(hn,heade);
			bulidFPTree( newPreNode,conditionDataBase1,heade);
		}		 	 
	 }	 
 }
 
 /**
  * 向FPTREE中插入一行
  * @param index 头结点
  * @param list 当前要插入的列表
  * @param newheade 要从新进行初始化的表头结点
  */
 public void insertTreeNode(TreeNode index,List<TreeNode>list,List<TreeNode>newheade)
 {
	 for(TreeNode n:list)
	 {
//		 System.out.print(n.nodeName+"--");
	 }
//	 System.out.println("");
	boolean flagInsert=true;//TRUE表示在当前的树中找到了该节点，不用重新生成；false标示没有找到当前节点需要重新生成
	for(TreeNode node:list)
	{
		 //生成表头指针		 
		if(flagInsert)
		{
			flagInsert=false;
			for(int j=0;j<index.childrenNode.size();j++)
			 {
				if(index.childrenNode.get(j).nodeName.compareTo(node.nodeName)==0)
				 {
					index.childrenNode.get(j).posCount=index.childrenNode.get(j).posCount+node.posCount;
					index.childrenNode.get(j).negCount=index.childrenNode.get(j).negCount+node.negCount;							 
					index=index.childrenNode.get(j);		
				    flagInsert=true;
					 break;
				}						 
			}
		}
		if(!flagInsert)
		{						
			 index.childrenNode.add(node);
			 node.parentNode=index;
			 
			for(TreeNode tn:newheade)
			{
				if(tn.nodeName.compareTo(node.nodeName)==0)
				{
					TreeNode tableIndex=tn;
					while(tableIndex.nextSameNode!=null)
					{
						tableIndex=tableIndex.nextSameNode;
					}
						tableIndex.nextSameNode=node;
						node.nextSameNode=null;
						break;
				}
			}				
			 index=index.childrenNode.get(index.childrenNode.size()-1);			 
		}
	 }			 
	 return;
 }
 
 /**
  * 得到状态数据库和新的表头结点
  * @param headerNode 表头结点
  * @return
  */
 public List<List<TreeNode>> getConditionDataBase(TreeNode headerNode, List<TreeNode> newHeade)
 {	 
	 //保存headerNode的投影数据库
//	 List<TreeNode> newHeade=new ArrayList<TreeNode>();
	 List<List<TreeNode>>conditionDataBase=new ArrayList<List<TreeNode>>();
	 List<List<TreeNode>>sortConditionDataBase=new ArrayList<List<TreeNode>>();
	 //遍历头结点headerNode，对应的相同的节点
	 TreeNode index=headerNode.nextSameNode;	 
	 while(index!=null)
	 {
		// posCount=index
		 List<TreeNode> item=new ArrayList<TreeNode>();
		 TreeNode preNode=index.parentNode;
		 while(preNode.parentNode!=null)
		 {
			 //将各个结点的支持度转化为最后一个节点的支持度
			 TreeNode newNode=new TreeNode(preNode.nodeName,index.posCount,index.negCount);
			 item.add(newNode);
			 //向表头结点中添加结点
			 addHeadTableNode( newHeade, newNode);			 
			 preNode=preNode.parentNode;			 
		 }
		 conditionDataBase.add(item);		 
		 index=index.nextSameNode;//访问下一个节点
	 }	 
	 //对newHeade中的所有节点正项支持度大小排序；
	 quickSortList(newHeade,0,newHeade.size()-1);
//	 System.out.println("第n次初始化表头为：");
	 for(TreeNode node:newHeade)
	{
//		System.out.println(node.nodeName+"[ "+node.posCount+", "+node.negCount+" ] ");
	}
	 //生成排序的状态数据库conditionDataBase
	 for(List<TreeNode> DataBaseList:conditionDataBase)
	 {
		 List<TreeNode> conditionList=new ArrayList<TreeNode>();
		 Iterator<TreeNode> iterhead=newHeade.iterator();
		 while(iterhead.hasNext())
		 {
			 TreeNode T=iterhead.next();			 
			 for(int k=0;k<DataBaseList.size();k++)
			 {
				 if(DataBaseList.get(k).compareTo(T)==0)
				 {
					 conditionList.add(DataBaseList.get(k));
					 break;					 
				 }
			 }
		 }
		sortConditionDataBase.add(conditionList);		 
	 }	 
	 return sortConditionDataBase;
 }
 /**
  * 向表头结点中插入一个新的结点
  * @param newHeade
  * @param newNode
  */
 public void addHeadTableNode(List<TreeNode> newHeade,TreeNode newNOde)
 {
	 TreeNode newNode=new TreeNode(newNOde.nodeName);
	 newNode.negCount=newNOde.negCount;
	 newNode.posCount=newNOde.posCount;
	// System.out.println("加入"+newNode.nodeName);
	 if(newHeade.size()==0)
	 {		 
		 newHeade.add(newNode);
	 }
	 else
	 {
		 int i=0;;
		 for(;i<newHeade.size();i++)
		 {
			if(newHeade.get(i).compareTo(newNode)==0)
			{
				newHeade.get(i).posCount=newHeade.get(i).posCount+newNode.posCount;
				newHeade.get(i).negCount=newHeade.get(i).negCount+newNode.negCount;				 
				break;
			}
		 }
		 if(i==newHeade.size())
		 { 
			   newHeade.add(newNode);	
		 }								 		 		
	 }
	 return;
 }
}
