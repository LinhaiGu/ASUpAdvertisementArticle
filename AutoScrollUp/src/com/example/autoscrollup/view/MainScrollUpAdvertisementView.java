package com.example.autoscrollup.view;

import android.content.Context;
import android.util.AttributeSet;

import com.example.autoscrollup.entity.AdvertisementObject;
import com.example.autoscrollup.view.base.BaseAutoScrollUpTextView;

/**
 * <pre>
 * 制作主页的向上广告滚动条
 * AdvertisementObject是主页的数据源，假如通过GSON或FastJson获取的实体类
 * 
 * <pre>
 * @author 顾林海
 * 
 */
public class MainScrollUpAdvertisementView extends
		BaseAutoScrollUpTextView<AdvertisementObject> {

	public MainScrollUpAdvertisementView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public MainScrollUpAdvertisementView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MainScrollUpAdvertisementView(Context context) {
		super(context);
	}

	@Override
	public String getTextTitle(AdvertisementObject data) {
		return data.title;
	}

	@Override
	public String getTextInfo(AdvertisementObject data) {
		return data.info;
	}

	/**
	 * 这里面的高度应该和你的xml里设置的高度一致
	 */
	@Override
	protected int getAdertisementHeight() {
		return 40;
	}

}
