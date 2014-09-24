package com.ustcinfo.mobile.platform.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ustcinfo.mobile.AppConstants;
import com.ustcinfo.mobile.ApplicationEx;
import com.ustcinfo.R;
import com.ustcinfo.mobile.platform.adapter.AppsAdapter;
import com.ustcinfo.mobile.platform.data.AppInfo;
import com.ustcinfo.mobile.platform.domain.AppsModel;
import com.ustcinfo.mobile.platform.widget.ScrollLayout;
import com.ustcinfo.mobile.util.AppMgrUtil;

/**
 * 应用程序管理中心
 * 
 * @author WINDFREE
 * 
 */
public class AppMgrCenterActivity extends Activity {
	private GridView gridview;
	private List<ResolveInfo> mApps;
	public static List<AppsModel> mAppsModel;
	private AppsAdapter adapter;
	private ScrollLayout curPage;
	private static final float APP_PAGE_SIZE = 12.0f;
	public static Map<String, ProgressBar> progressBars = new HashMap<String, ProgressBar>();
	private InstallReceiver installReceiver;
	private LinearLayout loading;

	public static List<AppInfo> applist = new LinkedList<AppInfo>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_view_center);
		findView();
		setUpListeners();
		installReceiver = new InstallReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(AppConstants.ACTION_DOWNLOADING);
		// 注册
		registerReceiver(installReceiver, filter);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		new AppListAsyncTask().execute(((ApplicationEx)getApplication()).getUserId());	
	}

	private void initViews() {
		if (null == applist) {
			Toast.makeText(AppMgrCenterActivity.this, "请检查网络",
					Toast.LENGTH_LONG).show();
			return;
		}
		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		mApps = getPackageManager().queryIntentActivities(intent, 0);
		final int PageCount = (int) Math.ceil(applist.size() / APP_PAGE_SIZE);
		mAppsModel = new ArrayList<AppsModel>();

		List<AppInfo> apptemp = new LinkedList<AppInfo>();
		for (int i = 0; i < applist.size(); i++) {
			if (!applist.get(i).getPackageName().equals(getPackageName()) && AppMgrUtil.isInstall(this, applist.get(i).getPackageName())) {// 移除自己
				apptemp.add(applist.get(i));
			}
			
		}
		applist=apptemp;
		if (gridview != null) {
			curPage.removeAllViews();
		}
		for (int i = 0; i < PageCount; i++) {
			gridview = new GridView(AppMgrCenterActivity.this);
			gridview.setNumColumns(4);
			gridview.setVerticalSpacing(10);
			gridview.setHorizontalSpacing(10);
			gridview.setColumnWidth(90);
			adapter = new AppsAdapter(this, applist, i);
			gridview.setAdapter(adapter);

			gridview.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					System.out.println(v);
					return false;
				}
			});
			curPage.addView(gridview);
		}

	}

	private void findView() {
		curPage = (ScrollLayout) findViewById(R.id.scr1);
		loading = (LinearLayout) this.findViewById(R.id.center_loading);
	}

	private void setUpListeners() {
		curPage.setPageListener(new ScrollLayout.PageListener() {
			@Override
			public void page(int page) {
				System.out.println(page);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 不要忘了这一步
		unregisterReceiver(installReceiver);
	}

	/**
	 * 接受下载进度
	 * 
	 * @author WINDFREE
	 * 
	 */
	private class InstallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			String packageName = intent.getStringExtra("packageName");
			ProgressBar pb = progressBars.get(packageName);
			if (null != pb) {
				RelativeLayout rl = (RelativeLayout) pb.getParent();
				int progress = bundle.getInt("progress");
				{
					if (progress >= 100 || progress == -1) {
						rl.setVisibility(View.GONE);
						pb.setVisibility(View.GONE);
					} else {
						pb.setVisibility(View.VISIBLE);
						rl.setVisibility(View.VISIBLE);
						pb.setProgress(progress);
					}
				}
			}

		}

	}

	class AppListAsyncTask extends AsyncTask<String, Integer, List<AppInfo>> {
		@Override
		protected List<AppInfo> doInBackground(String... params) {
			try {
				applist = ApplicationEx.data.getListAppInfo(params[0]);
			} catch (Exception e) {
			}
			return applist;
		}

		@Override
		protected void onPostExecute(List<AppInfo> result) {
			initViews();
			loading.setVisibility(View.GONE);// 显示最下方的进度条
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
