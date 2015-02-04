package edu.scu.stu.luliu.miningtags.FpGroth;
import edu.scu.stu.luliu.miningtags.FpGroth.Mining;
import edu.scu.stu.luliu.miningtags.FpGroth.PFTree;
import edu.scu.stu.luliu.miningtags.tools.ReadInitialFile;
import edu.scu.stu.luliu.miningtags.vo.MiningResult;
import edu.scu.stu.luliu.miningtags.vo.Product;
import edu.scu.stu.luliu.miningtags.vo.TreeNode;
import edu.scu.stu.luliu.miningtags.test.CrawlerTest;
import edu.scu.stu.luliu.miningtags.util.Count;
public class Mining {
	public MiningResult  getMiningResult(String urlPos,String urlNeg) {
//		String urlPos="http://item.jd.com/1235382.html";
//		String urlNeg="http://item.jd.com/1281952.html";
		CrawlerTest crawlerTest=new CrawlerTest();
		Product productPos=crawlerTest.getContent(urlPos);
		Count.INSTANCE.count(productPos);
		Product productNeg=crawlerTest.getContent(urlNeg);
		Count.INSTANCE.count(productNeg);
		int kValue=50;
		ReadInitialFile reader=new ReadInitialFile( productPos, productNeg);
		PFTree FT=new  PFTree(kValue,reader);
		//的到一项集的集合
		FT.findOneItems();
//		for(TreeNode node:oneItems)
//		{
//			System.out.print(node.nodeName+" "+node.posCount+" "+node.negCount+" "+(node.posCount-node.negCount)+";  ");
//		}
//		System.out.println();
//		for(TreeNode node:oneItems)
//		{
//			System.out.print(node.posCount-node.negCount+"  ");
//		}
		FT.initalFPTree();
		MiningResult miningResult=new MiningResult(productPos,productNeg,FT.result,FT.getOneItems());
		StringBuilder sbEasyRes=new StringBuilder();
		for(TreeNode node:FT.result)
		{
			System.out.println(node.nodeName+"( "+node.posCount+" "+node.negCount+") "+(node.posCount-node.negCount)+";  ");
			String oneRes="";
			String []resContent=node.nodeName.split(" ");
			for(String str:resContent)
			{
				oneRes +=str+" ";
			}
			if(oneRes.length()>0)
			  oneRes=oneRes.substring(0, oneRes.length()-1);
			System.out.println(oneRes+" ( "+node.posCount+", "+node.negCount+" ) "+(node.posCount-node.negCount)+";");
			sbEasyRes.append(oneRes+" ( "+node.posCount+", "+node.negCount+" ) "+(node.posCount-node.negCount)+";\n");
		}
		System.out.println("------------------------------------------------------------------------------");
		System.out.println(sbEasyRes.toString());
		return miningResult;
	}
	public static void main(String[] args) {
		Mining obj = new Mining();
		obj.getMiningResult("http://item.jd.com/1022296576.html","http://item.jd.com/701316.html");
	}
}
