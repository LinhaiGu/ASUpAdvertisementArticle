package com.example.autoscrollup;

import java.util.ArrayList;

import com.example.autoscrollup.entity.AdvertisementObject;
import com.example.autoscrollup.view.MainScrollUpAdvertisementView;
import com.example.autoscrollup.view.base.BaseAutoScrollUpTextView.OnItemClickListener;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends Activity {
	private int position = -1;

	private MainScrollUpAdvertisementView mMainScrollUpAdvertisementView;
	private ArrayList<AdvertisementObject> mDataList = new ArrayList<AdvertisementObject>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initData();
		initViews();
	}

	/**
	 * 模拟网络获取数据
	 */
	private void initData() {
		AdvertisementObject advertisementObject = new AdvertisementObject();
		advertisementObject.title = "爆";
		advertisementObject.info = "踏青零食上京东，百万零食1元秒";
		mDataList.add(advertisementObject);
		advertisementObject = new AdvertisementObject();
		advertisementObject.title = "公告";
		advertisementObject.info = "看老刘中国行，满129减50！";
		mDataList.add(advertisementObject);
		advertisementObject = new AdvertisementObject();
		advertisementObject.title = "爆";
		advertisementObject.info = "高姿CC霜全渠道新品首发，领券199减50，点击查看";
		mDataList.add(advertisementObject);
	}

	private void initViews() {
		mMainScrollUpAdvertisementView = (MainScrollUpAdvertisementView) findViewById(R.id.main_advertisement_view);
		mMainScrollUpAdvertisementView.setData(mDataList);
		mMainScrollUpAdvertisementView.setTextSize(15);
		mMainScrollUpAdvertisementView
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(int position) {
						Toast.makeText(MainActivity.this,
								"点击了第" + position + "个广告条", Toast.LENGTH_SHORT)
								.show();
					}
				});
		mMainScrollUpAdvertisementView.start();
		mMainScrollUpAdvertisementView.setTimer(2000);

	}

}
