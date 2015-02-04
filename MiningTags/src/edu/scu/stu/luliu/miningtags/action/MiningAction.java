package edu.scu.stu.luliu.miningtags.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;

import edu.scu.stu.luliu.miningtags.FpGroth.Mining;
import edu.scu.stu.luliu.miningtags.tools.Translator;
import edu.scu.stu.luliu.miningtags.vo.MiningResult;
import edu.scu.stu.luliu.miningtags.vo.MiningSortedResult;
import edu.scu.stu.luliu.miningtags.vo.TreeNode;
/**
 * @ClassName: MiningAction 
 * @Description: TODO
 * @author LuLiu
 * @date 2015��1��05�� ����10:25:33 
 *
 */
public class MiningAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	public String url1;// url1
	public String url2;// url2
	public String message;// message

	@Override
	public String execute() throws Exception {
		if ("admin".equals(url1) && "123".equals(url2))
			message = url1 + " ok";
		else {
			message = url1 + " false";
			return INPUT;
		}
		return SUCCESS;
		// return "success";
	}
	
	/**
	 * @Title: mining 
	 * @Description: �����ھ����
	 * @return void   json
	 * @throws
	 */
	public void mining() throws IOException{
	    HttpServletResponse response=ServletActionContext.getResponse();
	    HttpServletRequest req=ServletActionContext.getRequest();
	    String realPath = req.getSession().getServletContext().getRealPath("/mining-tag.json");
	    /*
	     * �ڵ���getWriter֮ǰδ���ñ���(�ȵ���setContentType����setCharacterEncoding�������ñ���),
	     * HttpServletResponse��᷵��һ����Ĭ�ϵı���(��ISO-8859-1)�����PrintWriterʵ���������ͻ�
	     * ����������롣�������ñ���ʱ�����ڵ���getWriter֮ǰ����,��Ȼ����Ч�ġ�
	     * */
	    response.setContentType("text/html;charset=utf-8");
	    //response.setCharacterEncoding("UTF-8");
	    PrintWriter out = response.getWriter();
	    /*
	     *���������ھ򣬷�������
	     */
	    Mining obj = new Mining();
		MiningResult result = obj.getMiningResult(url1,url2);
	    
		MiningSortedResult sortedResult = sortResult(result);
		translator(sortedResult);
		Gson gson = new Gson(); 
	    String jsonString=gson.toJson(sortedResult);
	    writeIntoFile(jsonString,realPath);
	    out.println(jsonString);
	    System.out.println(jsonString);
	    out.flush();
	    out.close();
	}
	
	private void translator(MiningSortedResult sortedResult) {
		try {
			List<String> wordlist = sortedResult.getWordlist();
			List<String> wordlisttemp = new LinkedList<String>();
			Map<String, String> translatorMap = new HashMap<String, String>();//����ȫ������ı�ǩ
			for (int i = 0; i < wordlist.size(); i++) {//ѭ���������б�ǩ
				String word = wordlist.get(i);
				String[] words = word.split(",");
				String wordtemp = Translator.INSTANCE.translate(words[0]);
				wordlisttemp.add(wordtemp+","+words[1]);
				translatorMap.put(words[0].trim(),wordtemp);
				System.out.println(word+"::::::"+wordtemp);
			}
			sortedResult.setWordlist(wordlisttemp);
////////////////////////////////////////////////////////////////
			List<List<String>> relevancelist = sortedResult.getRelevancelist();
			List<List<String>> relevancelisttemp = new LinkedList<List<String>>();
			for (int i = 0; i < relevancelist.size(); i++) {//���Ѿ�����õı�ǩtranslatorMap��������������е�����
				List<String> list = relevancelist.get(i);
				List<String> listtemp = new LinkedList<>();
				for (int j = 0; j < list.size(); j++) {
					String word = list.get(j);
					String[] words = word.split(",");// �����ж����ǩ�ö��Ÿ��� 
					String wordtemp = "";
					for (int k = 0; k < words.length; k++) {
						String temp = translatorMap.get(words[k].trim());
						wordtemp = wordtemp+temp+",";
					}
					wordtemp = wordtemp.substring(0, wordtemp.length()-1);
					listtemp.add(wordtemp);//����õĹ���������뵽�б���
					System.out.println(word+"::::::"+wordtemp);
				}
				if (listtemp.size()>0) {
					relevancelisttemp.add(listtemp);
				}
			}
			sortedResult.setRelevancelist(relevancelisttemp);
/////////////////////////////////////���Ѿ�����õı�ǩtranslatorMap����������е�����
			Map<String, String> highlights = sortedResult.getHighlights();//��ǩ����������
			Map<String, String> highlightstemp = new HashMap<String, String>();//��ǩ����������
			Iterator<String> iter = highlights.keySet().iterator();
			while (iter.hasNext()) {
			    String key = iter.next();
			    String value = highlights.get(key);
			    String keytemp = translatorMap.get(key.trim());
			    value = value.replace("[", "").replace("]", "");
			    String[] valuearr = value.split(",");
			    String valuetemp = "";
			    for (int i = 0; i < valuearr.length; i++) {
			    	valuetemp += translatorMap.get(valuearr[i].trim())+",";
				}
			    valuetemp.substring(0,valuetemp.length()-1);
			    highlightstemp.put(keytemp, valuetemp);
			    System.out.println(keytemp+"::::::"+valuetemp);
			}
			sortedResult.setHighlights(highlightstemp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void writeIntoFile(String jsonString,String realPath ) {
		FileItemFactory factory = new DiskFileItemFactory();
		//�����ļ���ŵ���ʱ�ļ��У�����ļ���Ҫ��ʵ����
		if(StringUtils.isEmpty(realPath)){
			return;
		}
		File file = new File(realPath);
		 try (
			 FileOutputStream out = new FileOutputStream(file);
			 PrintStream p = new PrintStream(out);){
             p.println(jsonString);
         } catch (FileNotFoundException e){
             e.printStackTrace();
         } catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}

	/**
	 * @Title: sortResult 
	 * @Description: TODO 
	 * @param  miningResult �ھ����ݽ��
	 * @return MiningSortedResult  ����������ݷ��ظ�ҳ����д��� 
	 * @throws
	 */
	public MiningSortedResult sortResult(MiningResult miningResult){
		if (miningResult==null) return null;
		MiningSortedResult result = new MiningSortedResult();
		result.setPosProdect(miningResult.getPosProdect());
		result.setNegProdect(miningResult.getNegProdect());
		TreeNode[] treenode = miningResult.getResult();
		List<TreeNode> oneItem = miningResult.getOneItem();
		List<String> words = new LinkedList<String>();//����������
		String haswords = "";
		List<List<String>> relevancelist = new LinkedList<List<String>>();//�ھ���Ķ�Ӧ�����Ĺ�������
		Map<String, String> highlights = new HashMap<String, String>();//��ǩ����������
		//��������������
		int maxnum = 1; 
		for (int i = 0; i < oneItem.size(); i++) {
			TreeNode node = oneItem.get(i);
			int num = node.posCount;
			if (maxnum<num) {
				maxnum=num;
			}
		}
		Double maxd = (double)(maxnum);
		for (int i = 0; i < oneItem.size(); i++) {
			TreeNode node = oneItem.get(i);
			int num = node.posCount;
			double numd = (double)(num);
			int perscent = (int)Math.floor((numd/maxd)*50);
			if (perscent==0) {
				continue;
			}
			words.add(node.nodeName.trim()+","+perscent);
		}
		//�����ھ���Ķ�Ӧ�����Ĺ�������
		for (int i = 0; i < treenode.length; i++) {
			String nodeNames = treenode[i].nodeName;
			String[] nodeNamearr = nodeNames.split(" ");
			int length = nodeNamearr.length;
			if (relevancelist.size()<length) {
				int count = length - relevancelist.size();
				while (count>0) {
					relevancelist.add(new LinkedList<String>());
					count--;
				}
			}
			String nodeNametemp = "";
			for (int j = 0; j < nodeNamearr.length; j++) {
				nodeNametemp = nodeNametemp+nodeNamearr[j] +",";
				haswords = haswords+nodeNamearr[j] +",";
			}
			nodeNametemp = nodeNametemp.substring(0,nodeNametemp.length()-1);
			relevancelist.get(length-1).add(nodeNametemp);
		}
		List<String> wordstemp = new LinkedList<String>();
		for (int i = 0; i < words.size(); i++) {
			String nodeName = words.get(i).split(",")[0];
			if (haswords.contains(nodeName)) {
				wordstemp.add(words.get(i));
			}
		}
		words = wordstemp;
		//����highligts
		for (int i = 0; i < words.size(); i++) {
			String nodeName = words.get(i).split(",")[0];
			Set<String> highligtset = new HashSet<String>();
			highligtset.add(nodeName.trim());
			for (int j = 0; j < treenode.length; j++) {
				String nodeNames = treenode[j].nodeName;
				if (!nodeNames.contains(nodeName))
					continue;
				String[] nodeNamearr = nodeNames.split(" ");
				for (int k = 0; k < nodeNamearr.length; k++) {
					highligtset.add(nodeNamearr[k].trim());
				}
			}
			highlights.put(nodeName, highligtset.toString());
		}
		
		
		result.setWordlist(words);
		result.setRelevancelist(relevancelist);
		result.setHighlights(highlights);
		return result;
	}

	public String getUrl1() {
		return url1;
	}


	public void setUrl1(String url1) {
		this.url1 = url1;
	}


	public String getUrl2() {
		return url2;
	}


	public void setUrl2(String url2) {
		this.url2 = url2;
	}


	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}