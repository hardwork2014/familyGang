package com.jzb.fgang.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

@SuppressWarnings("rawtypes")
public class Page implements Serializable {
	private static final long serialVersionUID = -2724448144309944356L;
	private int pageNo = 1;// 页码，默认是第一页
	private int pageSize = 10;// 默认每页显示的记录数
	private long totalRecord = -1;// 总记录数
	private int totalPage;// 总页数

	private List results;// 对应的当前页记录
	private int currentPage = 1; // 当前页码
	private int liststep = 6; // 最多显示分页页数
	private int listbegin; // 从第几页开始显示分页信息
	private int listend; // 分页信息显示到第几页
	private int recordbegin;// 当前页起始记录
	private int recordend = 0;// 当前页结束记录
	
	
	
	private Map params=new HashMap();

	public Page(int currentPage, int pageSize) {
		this.pageSize = pageSize;
		this.currentPage = currentPage;
		this.recordbegin = (currentPage - 1) * pageSize < 1 ? 0
				: (currentPage - 1) * pageSize;
	}

	public Page(int currentPage) {
		this.currentPage = currentPage;
		this.recordbegin = (currentPage - 1) * pageSize < 1 ? 0
				: (currentPage - 1) * pageSize;
	}

	public Page() {
	}

	public int getListstep() {
		return liststep;
	}

	public int getRecordbegin() {
		return (currentPage - 1) * pageSize < 1 ? 0
				: (currentPage - 1) * pageSize;
	}

	public int getRecordend() {
		return recordend;
	}

	public void setRecordend(int recordend) {
		this.recordend = recordend;
	}

	public void setListstep(int liststep) {
		this.liststep = liststep;
	}

	public int getListbegin() {
		return listbegin;
	}

	public void setListbegin(int listbegin) {
		this.listbegin = listbegin;
	}

	public int getListend() {
		return listend;
	}

	public void setListend(int listend) {
		this.listend = listend;
	}

	// 得到当前页
	public int getCurrentPage() {
		return currentPage;
	}

	// 设置当前页
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(long totalRecord) {
		this.totalRecord = totalRecord;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public List getResults() {
		return results;
	}

	public void setResults(List results) {
		this.results = results;
	}
	
	public Map getParams() {
		return params;
	}

	public void setParams(Map params) {
		this.params = params;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Page [pageNo=").append(pageNo).append(", pageSize=")
				.append(pageSize).append(", results=").append(results)
				.append(", totalPage=").append(totalPage)
				.append(", totalRecord=").append(totalRecord).append("]");
		return builder.toString();
	}

	// 初始化page对象
	public static Page initPage(String temppage, Integer pageSize) {
		int current = (StringUtils.isEmpty(temppage)) ? new Integer(1):Integer.valueOf(temppage); 
		Page page = null;
		if (pageSize != null)
			page = new Page(current, pageSize);
		else
			page = new Page(current);
		return page;
	}

	// 设置page对象属性
	public static Page setValue(Page page, List list) {
		int pagescount = (int) Math.ceil((double) page.getTotalRecord()
				/ page.getPageSize());// 求总页数
		page.setTotalPage(pagescount);

		if (pagescount < page.getCurrentPage()) {
			page.setCurrentPage(pagescount);// 如果分页变量大总页数，则将分页变量设计为总页数
		}
		if (page.getCurrentPage() < 1) {
			page.setCurrentPage(1);// 如果分页变量小于１,则将分页变量设为１
		}
	    // 计算listbegin与listend的值
	    if (pagescount < page.getListstep()) {
	    	page.setListbegin(1);
	    	page.setListend(pagescount);
	    } else {
            if (page.getCurrentPage() <= (page.getListstep() / 2 + 1)) {  
            	page.setListbegin(1);
            	page.setListend(page.getListstep());
            } else {
            	page.setListbegin(page.getCurrentPage()-(page.getListstep() / 2));
            	page.setListend(page.getCurrentPage()+(page.getListstep() / 2));
          	    if (page.getListend() >= pagescount) {
          	    	page.setListend(pagescount);
          	    	page.setListbegin(pagescount - page.getListstep() + 1);
          	    }
          	   } 	    	
	    }
		page.setResults(list);		
        
		return page;
	}
	
	public static <T extends Page> Page setValues(T page, List list) {
		return setValue(page, list);
	}
	

}
