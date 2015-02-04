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
	 //�Խ�������г�ʼ��
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
  * ���ö��ֲ��Ҳ�������ά����������Ա�֤�������Ϊ��ǰK�������������еĲ�ֵ������(���ò���ʵ����)
  * @param array�����Ҳ��������
  * @return ����������
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
				 //���Ե�i��Ϊ������κ���
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
  * ���ö��ֲ��Ҳ���������ά�ֽ����
  * @param array ����������
  * @param node ������ڵ�
  * @param left ָ����ߵ�ָ��
  * @param right ָ���ұߵ�ָ��
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
  * �����ļ��е�һ��ڵ㣻
  * @param posFileName�����ļ���
  * @param negFileName�����ļ���
  * @return һ����ļ���
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
			 //һ����û����ͬ����item
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
		  * ��Ҫ�޸�ֻ���������а�������
		  * 
		  * */
		 for(int m=0;m<negFileContent.size();m++)
		 {
			 for(int n=0;n<negFileContent.get(m).size();n++)
			 {
				 //һ����û����ͬ����item
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
  * ���ö��ֲ��Ҳ������� �õ��������������ݼ�֧�ֶȲ�ֵ�Ӵ�С������ļ���
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
  * ���������ʵ��
  * @param left ��߽߱�
  * @param right �ұ߽߱�
  * @return �ָ���
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
  * ��һ�γ�ʼ��FPTree
  * @param posFileName �����ļ���
  * @param negFileName �����ļ���
  * @param headTable ��ͷ
  * @return FPTree�ĸ��ڵ�
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
//	  System.out.println("��һ�γ�ʼ����ͷΪ��");
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
 * ��ʼ��״̬���ݿ�ͱ�ͷ�б�
 * @param conditionDataBase ��Ҫ��ֵ��״̬���ݿ�
 * @param headeTable��Ҫ��ʼ���ı�ͷ�б�
 * @param posFileName�������ݼ��ļ�·��
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
	  System.out.println("��һ�γ�ʼ����ͷΪ��");
	 for(TreeNode node:headTable)
		{
//			System.out.println(node.nodeName+"[ "+node.posCount+", "+node.negCount+" ] ");
		}
	
	 //�ڶ��ζ��ļ���ʼ��״̬���ݿ�
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
  * �õ�ĳ���ļ���״̬���ݿ�
  * @param headTable ��ͷ���
  * @param fileName �ļ���
  * @param index  ��ʾ�ļ�����꣨������߸��ࣩ�������1��ʾ���࣬�����ʾ����
  * @return ״̬���ݿ�
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
  * ����FPTree
  * @param conditionDataBase ��ǰ��״̬���ݿ�
  * @param headerTableNew    ��ͷ��㣻
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
		 //��״̬���ݿ��е�ÿһ�����FPTREE��
		 for(List<TreeNode> list:conditionDataBase)
		 {
			 if(list.size()>0)
			 insertTreeNode(index,list,newheade);			 
		 }
		 //��ͷ���Ǵ�������Ҳ����˵���������ɵĶ�,����nextsame�������ɵ�
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
  * ��FPTREE�в���һ��
  * @param index ͷ���
  * @param list ��ǰҪ������б�
  * @param newheade Ҫ���½��г�ʼ���ı�ͷ���
  */
 public void insertTreeNode(TreeNode index,List<TreeNode>list,List<TreeNode>newheade)
 {
	 for(TreeNode n:list)
	 {
//		 System.out.print(n.nodeName+"--");
	 }
//	 System.out.println("");
	boolean flagInsert=true;//TRUE��ʾ�ڵ�ǰ�������ҵ��˸ýڵ㣬�����������ɣ�false��ʾû���ҵ���ǰ�ڵ���Ҫ��������
	for(TreeNode node:list)
	{
		 //���ɱ�ͷָ��		 
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
  * �õ�״̬���ݿ���µı�ͷ���
  * @param headerNode ��ͷ���
  * @return
  */
 public List<List<TreeNode>> getConditionDataBase(TreeNode headerNode, List<TreeNode> newHeade)
 {	 
	 //����headerNode��ͶӰ���ݿ�
//	 List<TreeNode> newHeade=new ArrayList<TreeNode>();
	 List<List<TreeNode>>conditionDataBase=new ArrayList<List<TreeNode>>();
	 List<List<TreeNode>>sortConditionDataBase=new ArrayList<List<TreeNode>>();
	 //����ͷ���headerNode����Ӧ����ͬ�Ľڵ�
	 TreeNode index=headerNode.nextSameNode;	 
	 while(index!=null)
	 {
		// posCount=index
		 List<TreeNode> item=new ArrayList<TreeNode>();
		 TreeNode preNode=index.parentNode;
		 while(preNode.parentNode!=null)
		 {
			 //����������֧�ֶ�ת��Ϊ���һ���ڵ��֧�ֶ�
			 TreeNode newNode=new TreeNode(preNode.nodeName,index.posCount,index.negCount);
			 item.add(newNode);
			 //���ͷ�������ӽ��
			 addHeadTableNode( newHeade, newNode);			 
			 preNode=preNode.parentNode;			 
		 }
		 conditionDataBase.add(item);		 
		 index=index.nextSameNode;//������һ���ڵ�
	 }	 
	 //��newHeade�е����нڵ�����֧�ֶȴ�С����
	 quickSortList(newHeade,0,newHeade.size()-1);
//	 System.out.println("��n�γ�ʼ����ͷΪ��");
	 for(TreeNode node:newHeade)
	{
//		System.out.println(node.nodeName+"[ "+node.posCount+", "+node.negCount+" ] ");
	}
	 //���������״̬���ݿ�conditionDataBase
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
  * ���ͷ����в���һ���µĽ��
  * @param newHeade
  * @param newNode
  */
 public void addHeadTableNode(List<TreeNode> newHeade,TreeNode newNOde)
 {
	 TreeNode newNode=new TreeNode(newNOde.nodeName);
	 newNode.negCount=newNOde.negCount;
	 newNode.posCount=newNOde.posCount;
	// System.out.println("����"+newNode.nodeName);
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
