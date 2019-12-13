package top.zanghongmin.blockchain.core;

/**
 * 数据查询分页转化类
 */
public class PageQuery {
	private int pageoffset = 0;
    private int pagesize = 20;
    //pagenum 第几页 1为第一页 2位第二页
    //pagesize 页daxiao
    public static PageQuery getPageQuery(int pagenum, int pagesize) {
		PageQuery pageQuery = new PageQuery();
		if(pagenum<1){
			pagenum = 1;
		}
		if(pagesize<1){
			pagesize = 20;
		}
		pageQuery.setPageoffset((pagenum-1)*pagesize);
		pageQuery.setPagesize(pagesize);
		return pageQuery;
    }
	public int getPagesize() {
		return pagesize;
	}
    private void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}
	public int getPageoffset() {
		return pageoffset;
	}
    private void setPageoffset(int pageoffset) {
		this.pageoffset = pageoffset;
	}

}
