package top.zanghongmin.blockchain.core;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 统一分页对象
 */
@ApiModel(value="统一分页对象")
public class PageList<T> implements Serializable {
	public static final long serialVersionUID = 42L;
	@ApiModelProperty(name= "totalCount", value = "页的总大小",example = "0",required = true)
	private int totalCount;
	@ApiModelProperty(name= "pagenum", value = "当前页，第一页为1，第二页为2 ...",example = "1",required = true)
	private int pagenum;
	@ApiModelProperty(name= "pagesize", value = "页大小，默认为20，一页有20个数据",example = "20",required = true)
    private int pagesize;
	@ApiModelProperty(name= "pageList", value = "当前页内容",required = true)
	private T pageList;

    public PageList(int totalCount, int pagenum, int pagesize,T pageList) {
        this.totalCount = totalCount;
        this.pagenum = pagenum;
		this.pagesize = pagesize;
		this.pageList = pageList;
    }

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getPagenum() {
		return pagenum;
	}

	public void setPagenum(int pagenum) {
		this.pagenum = pagenum;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public T getPageList() {
		return pageList;
	}

	public void setPageList(T pageList) {
		this.pageList = pageList;
	}
}
