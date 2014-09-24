package com.ustcinfo.mobile.platform.view;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ustcinfo.mobile.AppConstants;
import com.ustcinfo.mobile.ApplicationEx;
import com.ustcinfo.mobile.download.DownloadService;
import com.ustcinfo.R;
import com.ustcinfo.mobile.platform.data.AppInfo;
import com.ustcinfo.mobile.util.AsyncImageLoader;

/**
 * 手机应用商城，应用列表页面
 */
public class MobileMarketActivity extends Activity {

	private final String TAG = "MobileMarketActivity";

	private ListView mListView;
	private LinearLayout loading;
	public  List<AppInfo> applist = new LinkedList<AppInfo>();
	private  Map<String,View> progressView=new HashMap<String,View>();
	private DownloadProgress receiver;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.app_list);

		findViews();
		setListeners();
		new AppListAsyncTask().execute();
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		receiver=new DownloadProgress();
		IntentFilter filter = new IntentFilter();
		filter.addAction(AppConstants.ACTION_DOWNLOADING);
		// 注册
		registerReceiver(receiver, filter);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 不要忘了这一步
		unregisterReceiver(receiver);
	}
	private void findViews() {
		mListView = (ListView) this.findViewById(R.id.listview);
		loading = (LinearLayout) this.findViewById(R.id.center_loading);
	}

	private void initViews() {
		if (applist == null) {
			Toast.makeText(MobileMarketActivity.this,
					R.string.prompt_exception_net, Toast.LENGTH_LONG).show();
			return;
		}
		if (applist.size() == 0) {
			Toast.makeText(this, "没有应用程序", Toast.LENGTH_LONG).show();

		} else {
			System.out.println("list:" + applist.size());
			mListView.setAdapter(new ListViewAdapter(this, applist));
			mListView.invalidateViews();

		}
	}

	private void setListeners() {
		final MobileMarketActivity mma = this;
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int pos,
					long arg3) {
				Intent intent = new Intent();
				intent.setClass(mma, AppAssetActivity.class);
				// 切换到详细信息页面
				// Toast.makeText(MobileMarketActivity.this,
				// "切换到详细信息页面，功能待开发",Toast.LENGTH_LONG).show();
				AppInfo appInfo = (AppInfo) mListView.getAdapter().getItem(pos);
				intent.putExtra("appName", appInfo.getAppName());
				intent.putExtra("appId", appInfo.getAppId().toString());
				AppConstants.appID = appInfo.getAppId().toString().trim();
				intent.putExtra("downLoadUrl", appInfo.getDownLoadUrl());
				intent.putExtra("packageName", appInfo.getPackageName());
				intent.putExtra("version", appInfo.getVersion() + "");
				startActivity(intent);
			}
		});
	}

	private final class ListViewHolder {
		public ImageView imageViewIcon;
		public TextView appName;
		public TextView appType;
		public TextView appSize;
		public TextView appVersion;
		public ImageView imgDownLoad;
		public TextView progress;
	}

	private class ListViewAdapter extends BaseAdapter {

		private LayoutInflater mInflater;
		private Context mContext;
		private List<AppInfo> listMap;

		public ListViewAdapter(Context ctx, List<AppInfo> listMap) {
			this.listMap = listMap;
			this.mContext = ctx;
			this.mInflater = LayoutInflater.from(ctx);
		}

		@Override
		public int getCount() {
			return listMap.size();
		}

		@Override
		public Object getItem(int position) {
			return listMap.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			final ListViewHolder viewHolder;

				convertView = mInflater.inflate(R.layout.app_item_listview,
						null);
				viewHolder = new ListViewHolder();
				viewHolder.imageViewIcon = (ImageView) convertView
						.findViewById(R.id.app_item_icon);// 要视图异步加载
				viewHolder.appName = (TextView) convertView
						.findViewById(R.id.text_item_1);
				viewHolder.appType = (TextView) convertView
						.findViewById(R.id.text_item_2);
				viewHolder.appSize = (TextView) convertView
						.findViewById(R.id.text_item_3);
				viewHolder.imgDownLoad = (ImageView) convertView
						.findViewById(R.id.app_item_down);
				viewHolder.progress=(TextView) convertView
						.findViewById(R.id.tvP);
//				viewHolder.appVersion=(TextView) convertView
//				.findViewById(R.id.text_item_4);
				convertView.setTag(viewHolder);
				viewHolder.imgDownLoad
						.setBackgroundResource(R.drawable.app_down_btn_nor);
			viewHolder.appName.setText(listMap.get(position).getAppName());
			viewHolder.appType.setText(listMap.get(position).getAppType());
			viewHolder.appSize.setText(listMap.get(position).getAppSize());
			//viewHolder.appVersion.setText(listMap.get(position).getVersionName());
			// 异步加载图片
			Drawable cachedImage = ApplicationEx.GetAsyncImageLoader()
					.loadDrawable(
							AppConstants.getImageUrl()+listMap.get(position).getAppIconUrl(),// 换成事件的URL
							viewHolder.imageViewIcon,
							new AsyncImageLoader.ImageCallback() {
								@Override
								public void imageLoaded(Drawable imageDrawable,
										ImageView imageView, String imageUrl) {
									imageView.setImageDrawable(imageDrawable);
								}

							});
			if (cachedImage == null) {
				viewHolder.imageViewIcon
						.setImageResource(R.drawable.ic_launcher);	
			} 
			else{
				viewHolder.imageViewIcon.setBackgroundDrawable(cachedImage);
			}
			viewHolder.imageViewIcon.setTag(listMap.get(position)
					.getPackageName());// 表名称,更新时用到
			viewHolder.imgDownLoad.setTag(position);
			if(null!=listMap.get(position).getProgress() && listMap.get(position).getProgress()>0 && listMap.get(position).getProgress()<100){
				String p=listMap.get(position).getProgress().toString();
				viewHolder.progress.setText(p+"%");
				viewHolder.progress.setVisibility(View.VISIBLE);
				viewHolder.imgDownLoad.setBackgroundResource(R.drawable.app_down_btn_sel);
			}
			else if(null!=listMap.get(position).getProgress() && listMap.get(position).getProgress()>=100){
				viewHolder.imgDownLoad.setBackgroundResource(R.drawable.app_down_btn_nor);
				viewHolder.progress.setVisibility(View.GONE);
				viewHolder.progress.setText("");			
			}
			else if(null!=listMap.get(position).getProgress() && listMap.get(position).getProgress()==0){
				viewHolder.imgDownLoad.setBackgroundResource(R.drawable.app_down_btn_nor);
				viewHolder.progress.setVisibility(View.GONE);
				viewHolder.progress.setText("");
			}
			progressView.put(listMap.get(position)
					.getPackageName(), viewHolder.progress);//进度对象放到map里
			viewHolder.progress.setTag(position);
			final Integer xx=position;
			viewHolder.imgDownLoad.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(null!=listMap.get(xx).getProgress() && listMap.get(xx).getProgress()==0){
						viewHolder.progress.setVisibility(View.VISIBLE);
						viewHolder.imgDownLoad.setBackgroundResource(R.drawable.app_down_btn_sel);
						Intent intent = new Intent();
						intent.setClass(MobileMarketActivity.this,
								DownloadService.class);
						// 这里不再使用bindService,而使用startService
						int pos = Integer.valueOf(v.getTag().toString());
						AppInfo appInfo = (AppInfo) mListView.getAdapter().getItem(
								pos);
						Bundle bundle = new Bundle();
						bundle.putString("appName", appInfo.getAppName());
						bundle.putString("downLoadUrl", appInfo.getDownLoadUrl());
						bundle.putString("packageName", appInfo.getPackageName());
						bundle.putString("appId", appInfo.getAppId().toString());
						bundle.putString("version",
								String.valueOf(appInfo.getVersion()));
						intent.putExtras(bundle);
						startService(intent);
					}
					else{
						Toast.makeText(MobileMarketActivity.this, "已经在下载了，请稍等!", Toast.LENGTH_LONG).show();
					}

				}
			});
			return convertView;
		}

	}
	
	class DownloadProgress extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			String packageName=bundle.getString("packageName");
			final TextView tv=(TextView) progressView.get(packageName);
			if(null!=tv){
				int progress = bundle.getInt("progress");
				if(progress<100){
					Integer  i=Integer.valueOf(tv.getTag().toString());
					applist.get(i).setProgress(progress);
					mListView.invalidateViews();
				}
				else{
					Integer  i=Integer.valueOf(tv.getTag().toString());
					applist.get(i).setProgress(0);
					mListView.invalidateViews();
				}
			}
			
		}
		
	}

	class AppListAsyncTask extends AsyncTask<String, Integer, List<AppInfo>> {
		@Override
		protected List<AppInfo> doInBackground(String... params) {
			try {
				applist = ApplicationEx.data
						.getListAppInfo(((ApplicationEx)getApplication()).getUserId());
			} catch (Exception e) {
			}
			return applist;
		}

		@Override
		protected void onPostExecute(List<AppInfo> result) {
			loading.setVisibility(View.GONE);
			initViews();
		}
		/**
		 * 当任务执行之前开始调用此方法
		 */
		@Override
		protected void onPreExecute() {
			loading.setVisibility(View.VISIBLE);// 显示最下方的进度条
		}
	}

}