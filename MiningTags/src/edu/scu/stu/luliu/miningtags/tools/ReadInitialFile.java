package edu.scu.stu.luliu.miningtags.tools;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scu.stu.luliu.miningtags.vo.Labels;
import edu.scu.stu.luliu.miningtags.vo.Product;
public class ReadInitialFile {
  public Product productPos;//文件名
  public Product productNeg;
  private List<List<String>> posFile=new ArrayList<List<String>>();
  private List<List<String>> negFile=new ArrayList<List<String>>();
  public ReadInitialFile()
  {
	  
  }
  public ReadInitialFile(Product productPos,Product productNeg)
  {
	  this.productPos=productPos;
	  this.productNeg=productNeg;
	  this.posFile=readFile(productPos);
	  this.negFile=readFile(productNeg);
  }
  
  public List<List<String>> getPosFile() {
	return posFile;
}
public void setPosFile(List<List<String>> posFile) {
	this.posFile = posFile;
}
public List<List<String>> getNegFile() {
	return negFile;
}
public void setNegFile(List<List<String>> negFile) {
	this.negFile = negFile;
}
/**
   * 读取文件进行初始化
   */
  public List<List<String>> readFile(Product product)
  {
	  List<Labels> list=product.list;
	  List<List<String>> content=new ArrayList<List<String>>();
	  List<String> itmeList=null;
	  for(Labels labe:list)
	  {
		  itmeList=new ArrayList<String>();
		  Set<String>lableContent=labe.getSet();
		  for(String item:lableContent)
		  {
			  itmeList.add(item);
		  }
		  content.add(itmeList);
	  }
	  return content;
  }
}
