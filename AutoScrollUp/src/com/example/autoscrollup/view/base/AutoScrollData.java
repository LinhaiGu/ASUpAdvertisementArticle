package com.example.autoscrollup.view.base;

/**
 * 自动滚动TextView的数据
 * 
 * @author 顾林海
 * 
 * @param <T>
 */
public interface AutoScrollData<T> {

	/**
	 * * 获取标题
	 * 
	 * @param data
	 * @return
	 */
	public String getTextTitle(T data);

	/**
	 * 获取内容
	 * 
	 * @param data
	 * @return
	 */
	public String getTextInfo(T data);

}
