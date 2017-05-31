package utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 分页查询返回的实体类型
 * @author yuguitao
 * @param <E>
 * @create at 2010-04-16
 */
@SuppressWarnings("serial")
public class Page<E> implements Serializable, Iterable<E> {
	
	// Extjs　分页
	protected List<E> data;
	
	protected int pageSize;

	protected int pageNumber;

	protected long total = 0;
	
	public Page(long total) {
		this.total = total;
	}

	public Page(int pageNumber,int pageSize,long totalCount) {
		this(pageNumber,pageSize,totalCount,new ArrayList<E>(0));
	}

	public Page(int pageNumber,int pageSize,long total,List<E> result) {
		if(pageSize <= 0) throw new IllegalArgumentException("[pageSize] must great than zero");
		this.pageSize = pageSize;
		this.pageNumber = pageNumber;
		this.total = total;
		setResult(result);
	}

	public void setResult(List<E> elements) {
		if (elements == null)
			throw new IllegalArgumentException("'result' must be not null");
		this.data = elements;
	}
	
	/**
     * 每一页显示的条目数
     *
     * @return 每一页显示的条目数
     */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 当前页包含的数据
	 *
	 * @return 当前页数据源
	 */
	public List<E> getData() {
		return data;
	}

	/**
	 * 得到数据库的第一条记录号
	 * @return
	 */
	public int getFirstResult() {
		return pageNumber * pageSize;
	}

	public long getTotal() {
		return total;
	}

	@SuppressWarnings("unchecked")
	public Iterator<E> iterator() {
		return (Iterator<E>) (data == null ? Collections.emptyList().iterator() : data.iterator());
	}
}
