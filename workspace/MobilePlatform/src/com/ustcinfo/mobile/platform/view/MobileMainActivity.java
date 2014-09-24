package com.ustcinfo.mobile.platform.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.androidpn.client.ServiceManager;

import android.app.ActivityGroup;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.ustcinfo.mobile.AppConstants;
import com.ustcinfo.mobile.download.DownloadService;
import com.ustcinfo.R;
import com.ustcinfo.mobile.platform.widget.AlertDialogUtil;
import com.ustcinfo.mobile.platform.widget.TextProgressBar;
import com.ustcinfo.mobile.util.NetworkUtil;
import com.ustcinfo.mobile.util.VersionUtil;

public class MobileMainActivity extends ActivityGroup {

	private static Boolean isExit = false; // 正在退出

	private static Boolean hasTask = false; // 是否有任务

	private Timer tExit = new Timer();// 异步任务退出程序

	private ViewFlipper mViewFlipper;
	private GridView menuGridView;
	// 底部菜单的名字
	private String[] menuGridViewNames = null;
	// 未选择时 显示的图片资源id
	private int[] menuGridViewUnSelectedImgs = null;
	// 选择时 显示的图片资源id
	private int[] menuGridViewSelectedImgs = null;
	// 上次点击的位置，当前点击的位置
	private int lastClickPosition, clickPosition;

	int mSelectedMenu = 0;

	// 弹出菜单GridView
	private GridView menuGrid;
	private PopupWindow popupWindow;

	private PackageInfo packInfo;

	private String[] menu_name_array = { "设置", "更新", "搜索", "关于", "退出", "关闭" };
	private int[] menu_image_array = { R.drawable.menu_syssettings,
			R.drawable.menu_bookmark_sync_sync, R.drawable.menu_search,
			R.drawable.menu_about, R.drawable.menu_quit, R.drawable.menu_more };

	private VersionUtil version = new VersionUtil();

	private TextProgressBar pb;
	private DownloadProgress downloadReceiver;

	private Dialog d;// 更新进度对话框

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.moblie_main);
		initRes();
		findViews();
		setValues();
		setListeners();
		switchActivity(AppConstants.FISRT_ACTIVITY_INDEX);
		// Start the service

		PackageManager packageManager = getPackageManager();
		try {
			packInfo = packageManager.getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	//消息推送暂时注释
//			ServiceManager serviceManager = new ServiceManager(this);
//			serviceManager.setNotificationIcon(R.drawable.notification);
//			serviceManager.startService();

		if (NetworkUtil.getNetWorkState(this)) {// 判断网络状态，更新版本
			Thread VerThread = new Thread() {
				@Override
				public void run() {
					if (version.getServerVerCode() == true) {// 获取最新版本
						int vercode = version
								.getVerCode(MobileMainActivity.this);
						int newVercode = version.getNewVerCode();
						if (newVercode != vercode
								&& -1 != version.getNewVerCode()) {// 需要升级版本
							Message msg = BroadcastHandler.obtainMessage();
							BroadcastHandler.sendEmptyMessage(0);
						}
					}
				}
			};
			ExecutorService pool = Executors.newSingleThreadExecutor();
			pool.execute(VerThread);
			pool.shutdown();
		}

		downloadReceiver = new DownloadProgress();
		IntentFilter filter = new IntentFilter();
		filter.addAction(AppConstants.ACTION_DOWNLOADING);
		// 注册
		registerReceiver(downloadReceiver, filter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 不要忘了这一步
		unregisterReceiver(downloadReceiver);
	}

	private Handler BroadcastHandler = new Handler() {
		public void handleMessage(Message msg) {
			doNewVersionUpdate();
		}
	};

	/**
	 * 版本更新对话框
	 */
	private void doNewVersionUpdate() {
		int verCode = version.getVerCode(this);
		String verName = VersionUtil.getVerName(this);
		StringBuffer sb = new StringBuffer();
		sb.append("当前版本:");
		sb.append(verName);
		sb.append(" Code:");
		sb.append(verCode);
		sb.append(", 发现新版本:");
		sb.append(version.getNewVerName());
		sb.append(" Code:");
		sb.append(version.getNewVerCode());
		sb.append(", 是否更新?");
		final AlertDialogUtil dialog = new AlertDialogUtil(this);
		dialog.setTitle("软件更新");
		dialog.setContent(sb.toString());
		dialog.setConfirmClickListener("OK", new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				beginUpdate();
				dialog.cancel();
			}
		});
		dialog.setCancelClickListener("NO", new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.cancel();
			}
		});
		// 显示对话框
		dialog.show();

	}

	private void beginUpdate() {

		LinearLayout ll = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.layout_loadapk, null);
		pb = (TextProgressBar) ll.findViewById(R.id.down_pb);
		Builder builder = new Builder(this);
		builder.setView(ll);
		builder.setTitle("版本更新进度提示");
		builder.setNegativeButton("后台下载",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		d = builder.show();
		Intent intent = new Intent();
		intent.setClass(this, DownloadService.class);
		Bundle bundle = new Bundle();
		bundle.putString("appName", "ess.apk");
		bundle.putString("downLoadUrl", "ess.apk");
		bundle.putString("packageName", packInfo.packageName);
		bundle.putString("appId", "9009");
		intent.putExtras(bundle);
		startService(intent);

	}

	private void setListeners() {
		menuGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int index, long arg3) {
				lastClickPosition = clickPosition;
				clickPosition = index;
				if (lastClickPosition == clickPosition && index != 2)
					return; // 防止重复点击同一个菜单
				RelativeLayout temp = (RelativeLayout) menuGridView
						.getChildAt(index);
				// temp.setBackgroundResource(R.drawable.product_title_bg);
				temp.getChildAt(0).setBackgroundResource(
						menuGridViewSelectedImgs[index]);
				for (int i = 0; i < menuGridViewNames.length; i++) {
					if (i != index) {
						RelativeLayout temp2 = (RelativeLayout) menuGridView
								.getChildAt(i);
						temp2.setBackgroundDrawable(null);
						temp2.getChildAt(0).setBackgroundResource(
								menuGridViewUnSelectedImgs[i]);
					}
				}
				switchActivity(index);

			}
		});

	}

	/**
	 * 切换到不同的Activity
	 * 
	 * @param index
	 */
	protected void switchActivity(int index) {
		String activityId = null;
		Intent intent = null;
		switch (index) {
		case AppConstants.FISRT_ACTIVITY_INDEX:// 首页
			intent = new Intent(this, AppMgrCenterActivity.class);
			activityId = AppConstants.FIRST_ACTIVITY;
			break;

		case AppConstants.MARKET_ACTIVITY_INDEX:// 应用管理
			intent = new Intent(this, MobileMarketActivity.class);//
			activityId = AppConstants.MOBLIE_MARKET_ACTIVITY;
			break;
		case AppConstants.MORE_ACTIVITY_INDEX:// 跟多
			// openPopupwin();
			intent = new Intent(this, MoreActivity.class);//
			activityId = AppConstants.MOBLIE_MARKET_ACTIVITY;
			break;
		}

		if (activityId != null) {
			toActivity(activityId, intent);
		}

	}

	private void openPopupwin() {
		LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		ViewGroup menuView = (ViewGroup) mLayoutInflater.inflate(
				R.layout.gridview_pop, null, true);
		menuGrid = (GridView) menuView.findViewById(R.id.gridview);
		menuGrid.setAdapter(getMenuAdapter(menu_name_array, menu_image_array));
		menuGrid.requestFocus();
		menuGrid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == 6) {// 关闭
					popupWindow.dismiss();
				}
				if (arg2 == 5) {
					popupWindow.dismiss();
					finish();
				}
			}
		});
		menuGrid.setOnKeyListener(new OnKeyListener() {// 焦点到了gridview上，所以需要监听此处的键盘事件。否则会出现不响应键盘事件的情况
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				switch (keyCode) {
				case KeyEvent.KEYCODE_MENU:
					if (popupWindow != null && popupWindow.isShowing()) {
						popupWindow.dismiss();
					}
					break;
				}
				return true;
			}
		});
		popupWindow = new PopupWindow(menuView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// popupWindow.setAnimationStyle(R.style.PopupAnimation);
		popupWindow.showAtLocation(findViewById(R.id.saLinearLayout),
				Gravity.CENTER | Gravity.CENTER, 0, 0);
		popupWindow.update();

	}

	private ListAdapter getMenuAdapter(String[] menuNameArray,
			int[] menuImageArray) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < menuNameArray.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemImage", menuImageArray[i]);
			map.put("itemText", menuNameArray[i]);
			data.add(map);
		}
		SimpleAdapter simperAdapter = new SimpleAdapter(this, data,
				R.layout.item_menu, new String[] { "itemImage", "itemText" },
				new int[] { R.id.item_image, R.id.item_text });
		return simperAdapter;

	}

	private void toActivity(String activityId, Intent intent) {
		System.out.println("activityId:" + activityId);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		View view = getLocalActivityManager().startActivity(activityId, intent)
				.getDecorView();
		// 切换activity时显示的动画效果
		// view.setAnimation(new WindowAnimation(500));
		mViewFlipper.removeAllViews();
		mViewFlipper.addView(view);
		mViewFlipper.showNext();
	}

	private void setValues() {
		menuGridView.setAdapter(new BottomMenuGridView(this));
		menuGridView.setNumColumns(3);
		menuGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		menuGridView.setGravity(Gravity.CENTER);

	}

	private void findViews() {
		menuGridView = (GridView) this.findViewById(R.id.gridview);
		mViewFlipper = (ViewFlipper) this.findViewById(R.id.content);
	}

	private void initRes() {
		menuGridViewNames = new String[3];
		menuGridViewNames[0] = this.getString(R.string.tab_home);
		menuGridViewNames[1] = this.getString(R.string.tab_app);
		menuGridViewNames[2] = this.getString(R.string.app_more);

		menuGridViewUnSelectedImgs = new int[3];
		menuGridViewUnSelectedImgs[0] = R.drawable.tab_home_normal;
		menuGridViewUnSelectedImgs[1] = R.drawable.tab_app_nor;
		menuGridViewUnSelectedImgs[2] = R.drawable.tab_app_more;

		menuGridViewSelectedImgs = new int[3];
		menuGridViewSelectedImgs[0] = R.drawable.tab_home_selected;
		menuGridViewSelectedImgs[1] = R.drawable.tab_app_sel;
		menuGridViewSelectedImgs[2] = R.drawable.tab_app_more;

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {

		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isExit == false) {
				isExit = true;
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				if (!hasTask) {
					tExit.schedule(task, 2000);
				}
			} else {
				finish();
				System.exit(0);
			}

		}
		return false;
	}

	TimerTask task = new TimerTask() {
		@Override
		public void run() {
			isExit = false;
			hasTask = true;
		}
	};

	private class BottomMenuGridView extends BaseAdapter {

		private LayoutInflater mInflater;

		public BottomMenuGridView(Context ctx) {
			this.mInflater = LayoutInflater.from(ctx);
		}

		@Override
		public int getCount() {
			return menuGridViewUnSelectedImgs.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MenuViewHolder viewHolder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_tab, null);
				viewHolder = new MenuViewHolder();
				viewHolder.imageView = (ImageView) convertView
						.findViewById(R.id.image_item);
				viewHolder.textView = (TextView) convertView
						.findViewById(R.id.text_item);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (MenuViewHolder) convertView.getTag();
			}
			viewHolder.imageView
					.setBackgroundResource(menuGridViewUnSelectedImgs[position]);
			viewHolder.textView.setText(menuGridViewNames[position]);

			return convertView;
		}

	}

	private final class MenuViewHolder {
		public ImageView imageView;
		public TextView textView;
	}
	
	//接收e运维更新通知
	class DownloadProgress extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			String packageName = bundle.getString("packageName");
			if (packageName.equals(packInfo.packageName)) {
				int progress = bundle.getInt("progress");
				if (progress < 100) {
					pb.setProgress(progress);
					pb.setText("已为您加载了：" + progress + "%");
				} else {
					d.dismiss();
				}
			}
		}

	}

}
