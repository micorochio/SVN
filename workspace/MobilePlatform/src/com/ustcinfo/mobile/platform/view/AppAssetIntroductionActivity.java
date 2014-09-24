package com.ustcinfo.mobile.platform.view;

import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ustcinfo.mobile.AppConstants;
import com.ustcinfo.mobile.ApplicationEx;
import com.ustcinfo.mobile.download.DownloadService;
import com.ustcinfo.R;
import com.ustcinfo.mobile.platform.adapter.GuideGalleryAdapter;
import com.ustcinfo.mobile.platform.data.AppInfo;
import com.ustcinfo.mobile.util.AppMgrUtil;
import com.ustcinfo.mobile.util.AsyncImageLoader;

/**
 * 手机应用简介信息
 */
public class AppAssetIntroductionActivity extends Activity {

	private AppInfo appInfo = null;
	private LinearLayout loading;
	private RelativeLayout gallery;
	private LinearLayout download_status_container;

	private View buttons_spacer_left;
	private Button launchbutton;// 打开
	private Button downloadbutton;//下载
	private Button updatebutton;// 更新
	private Button installbutton;//安装
	private Button dummybutton;
	private Button uninstallbutton;// 卸载
	private Button installingbutton;// 安装中
	private Button canceldownloadbutton;// 暂停下载
	private View buttons_spacer_right;

	ProgressBar download_progress_bar;// 进度条
	TextView download_status;// 进度条状态
	private String packageName;
	private Intent intent;
	private MyReceiver receiver;
	private TextView text;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.asset_info_gallery);
		intent = this.getIntent();
		findView();
		packageName = intent.getStringExtra("packageName");
		//packageName = "com.ustcinfo.mobile.platform.view";
		DownloadProcess();
		new AppListAsyncTask()
				.execute(this.getIntent().getStringExtra("appId"));
	}

	private void findView() {
		gallery = (RelativeLayout) this.findViewById(R.id.asset_info_gallery_ds);
		loading = (LinearLayout) this
				.findViewById(R.id.fullscreen_loading_indicator);
		// 获取底部页面资源
		download_status_container = (LinearLayout) this
				.findViewById(R.id.download_status_container);
		buttons_spacer_left = this.findViewById(R.id.buttons_spacer_left);
		launchbutton = (Button) this.findViewById(R.id.launchbutton);// 打开
		downloadbutton = (Button) this.findViewById(R.id.downloadbutton);// 下载
		updatebutton = (Button) this.findViewById(R.id.updatebutton);// 更新
		installbutton = (Button) this.findViewById(R.id.installbutton);// 安装
		dummybutton = (Button) this.findViewById(R.id.dummybutton);
		uninstallbutton = (Button) this.findViewById(R.id.uninstallbutton);// 卸载
		installingbutton = (Button) this.findViewById(R.id.installingbutton);// 安装中
		canceldownloadbutton = (Button) this
				.findViewById(R.id.canceldownloadbutton);// 暂停下载
		buttons_spacer_right = this.findViewById(R.id.buttons_spacer_right);

		download_progress_bar = (ProgressBar) this
				.findViewById(R.id.download_progress_bar);// 进度条
		download_status = (TextView) this.findViewById(R.id.download_status);// 进度条状态

	}

	/**
	 * @Title: showGalleryInfo
	 * @Description: 显示简介信息
	 * @return void
	 * @throws
	 */
	private void showGalleryInfo() {
		loading.setVisibility(View.GONE);
		gallery.setVisibility(View.VISIBLE);
		if (appInfo == null) {
			TextView nameView = (TextView) this
					.findViewById(R.id.asset_info_title);
			nameView.setText(R.string.prompt_exception_net);
			Toast.makeText(AppAssetIntroductionActivity.this,
					R.string.prompt_exception_net, Toast.LENGTH_LONG).show();
			return;
		}
		showDetailInfo();
		showScreenShort();
		showDescription();
		showBottomBtns();
	}

	/**
	 * @Title: showDetailInfo
	 * @Description: 显示详细信息
	 * @return void
	 * @throws
	 */
	private void showDetailInfo() {
		ImageView logoView = (ImageView) this.findViewById(R.id.thumbnail);
		TextView versionView = (TextView) this.findViewById(R.id.version);
		TextView nameView = (TextView) this.findViewById(R.id.asset_info_title);
		TextView sizeView = (TextView) this.findViewById(R.id.size);
		TextView time = (TextView) this.findViewById(R.id.last_update);
		// 异步的加载图片信息
		Drawable cachedImage = ApplicationEx.GetAsyncImageLoader()
				.loadDrawable(AppConstants.getImageUrl() +appInfo.getAppIconUrl(),// 换成事件的URL
						logoView, new AsyncImageLoader.ImageCallback() {
							@Override
							public void imageLoaded(Drawable imageDrawable,
									ImageView imageView, String imageUrl) {
								imageView.setImageDrawable(imageDrawable);
							}

						});
		if (cachedImage == null) {
			logoView.setImageResource(R.drawable.ic_launcher);
		} else {
			logoView.setImageDrawable(cachedImage);
		}
		versionView.setText("版本：" + appInfo.getVersion());
		sizeView.setText("大小：" + appInfo.getAppSize());
		time.setText("时间：" + appInfo.getUpdateTime());
		nameView.setText(appInfo.getAppName());
		// 获得描述信息
	}

	/**
	 * @Title: showScreenShort
	 * @Description: 显示截图信息
	 * @return void
	 * @throws
	 */
	private void showScreenShort() {
		final List<String> imgURL = appInfo.getImgURL();
		final TextView image_indicator = (TextView) this
				.findViewById(R.id.image_indicator);
		if (imgURL.size() != 0) {
			Gallery screenGallery = (Gallery) this
					.findViewById(R.id.screen_gallery);
			screenGallery
					.setAdapter(new GuideGalleryAdapter(this, imgURL, null));
			screenGallery
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int position, long arg3) {
							image_indicator.setText(((position + 1)
									% imgURL.size() == 0 ? imgURL.size()
									: (position + 1) % imgURL.size())
									+ "/"
									+ imgURL.size());
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {

						}
					});
		} else { // 当没有截图的时候
			image_indicator.setText(R.string.no_screen_shorts);
		}
	}

	/**
	 * @Title: showDescription
	 * @Description: 显示描述信息
	 * @return void
	 * @throws
	 */
	private void showDescription() {
		String description = appInfo.getDescription();
		final TextView descriptionView = (TextView) this
				.findViewById(R.id.description);
		descriptionView.setText(description);
		final ImageButton more = (ImageButton) this
				.findViewById(R.id.detail_more);
		more.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				descriptionView.setMaxLines(10000);
				more.setVisibility(View.GONE);
			}
		});
	}

	/**
	 * @Title: showBottom
	 * @Description: 显示底部按钮
	 * @return void
	 * @throws
	 */
	private void showBottomBtns() {
		LinearLayout layout = (LinearLayout) this
				.findViewById(R.id.info_buttons_bar);
		layout.setVisibility(View.VISIBLE);

		// TODO 根据应用的包名判断应用是否安装
		Context context = this.getApplicationContext();
		Intent intent = this.getIntent();
		final String pakage = intent.getStringExtra("packageName");
		int newVersion = Integer.valueOf(intent.getStringExtra("version"));
		final String downLoadUrl = intent.getStringExtra("downLoadUrl");
		final String appName = intent.getStringExtra("appName");
		if (!AppMgrUtil.isInstall(context, pakage)) {// 判断是否安装了,没有安装进入
			buttons_spacer_left.setVisibility(View.VISIBLE);
			buttons_spacer_right.setVisibility(View.VISIBLE);
			if (AppMgrUtil.isHaveDownloadedAPK(context, appName)) {//如果没有安装，判断是否已经下载好了应用包
				// todo 需要判断安装包是否完整
				installbutton.setVisibility(View.VISIBLE);
			} else {
				downloadbutton.setVisibility(View.VISIBLE);
			}
		} else if (AppMgrUtil.isNeedUpdate(context, pakage, newVersion)) {// 判断是否需要更新
			buttons_spacer_left.setVisibility(View.VISIBLE);
			updatebutton.setVisibility(View.VISIBLE);
			uninstallbutton.setVisibility(View.VISIBLE);
			dummybutton.setVisibility(View.VISIBLE);
			buttons_spacer_right.setVisibility(View.VISIBLE);
		} else {//已经安装了
			buttons_spacer_left.setVisibility(View.VISIBLE);
			launchbutton.setVisibility(View.VISIBLE);
			uninstallbutton.setVisibility(View.VISIBLE);
			buttons_spacer_right.setVisibility(View.VISIBLE);
			dummybutton.setVisibility(View.VISIBLE);
		}

		downloadbutton.setOnClickListener(new OnClickListener() {// 下载
					@Override
					public void onClick(View v) {
						// 展示下载进度条
						download_status_container.setVisibility(View.VISIBLE);
						downloadbutton.setVisibility(View.GONE);
						canceldownloadbutton.setVisibility(View.VISIBLE);
						Intent intent = AppAssetIntroductionActivity.this
								.getIntent();
						synchronized (intent) {
							intent.setClass(AppAssetIntroductionActivity.this,
									DownloadService.class);
							// 这里不再使用bindService,而使用startService
							Bundle bundle = new Bundle();
							bundle.putString("appName",
									intent.getStringExtra("appName"));
							bundle.putString("downLoadUrl",
									intent.getStringExtra("downLoadUrl"));
							bundle.putString("appId",
									intent.getStringExtra("appId"));
							bundle.putString("packageName",
									intent.getStringExtra("packageName"));
							bundle.putString("appNameCn",
									intent.getStringExtra("appNameCn"));
							intent.putExtras(bundle);
							startService(intent);
						}
					}
				});
		canceldownloadbutton.setOnClickListener(new OnClickListener() {// 取消下载
			@Override
			public void onClick(View v) {
			}
		});
		updatebutton.setOnClickListener(new OnClickListener() {// 更新
					@Override
					public void onClick(View v) {
						AppMgrUtil.updateSys(AppAssetIntroductionActivity.this
								.getApplicationContext(),
								DownloadService.DOWNLOAD_SD_PATH + downLoadUrl);
						finish();
					}
				});
		launchbutton.setOnClickListener(new OnClickListener() {// 打开
					@Override
					public void onClick(View v) {
						AppMgrUtil.openSys(AppAssetIntroductionActivity.this
								.getApplicationContext(), pakage);
						finish();
					}
				});
		installbutton.setOnClickListener(new OnClickListener() {// 安装
					@Override
					public void onClick(View v) {
						AppMgrUtil.updateSys(AppAssetIntroductionActivity.this
								.getApplicationContext(),
								DownloadService.DOWNLOAD_SD_PATH + downLoadUrl);
						finish();
					}
				});
		uninstallbutton.setOnClickListener(new OnClickListener() {// 卸载
					@Override
					public void onClick(View v) {
						AppMgrUtil.unInstallApp(
								AppAssetIntroductionActivity.this
										.getApplicationContext(), pakage);
						finish();
					}
				});
	}

	private void DownloadProcess() {
		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(AppConstants.ACTION_DOWNLOADING);
		// 注册
		registerReceiver(receiver, filter);
		text = (TextView) this.findViewById(R.id.download_status);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 不要忘了这一步
		unregisterReceiver(receiver);
	}

	public void cancel(View view) {
//		DownloadService.cancelledURL = this.getIntent().getStringExtra(
//				"appName");
//		download_status_container.setVisibility(View.GONE);
//		downloadbutton.setVisibility(View.VISIBLE);
//		canceldownloadbutton.setVisibility(View.GONE);
	}

	/**
	 * 广播接收器
	 * 
	 * @author MIL
	 * 
	 */
	private class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			if (packageName.equals(bundle.getString("packageName"))) {
				download_status_container.setVisibility(View.VISIBLE);
				buttons_spacer_left.setVisibility(View.VISIBLE);
				canceldownloadbutton.setVisibility(View.VISIBLE);
				buttons_spacer_right.setVisibility(View.VISIBLE);
				downloadbutton.setVisibility(View.GONE);
				int progress = bundle.getInt("progress");
				if (progress >= 100) {
					download_progress_bar.setProgress(100);
					text.setText("下载完成！");
					canceldownloadbutton.setVisibility(View.GONE);
					launchbutton.setVisibility(View.VISIBLE);
					uninstallbutton.setVisibility(View.VISIBLE);
					download_status_container.setVisibility(View.GONE);
					dummybutton.setVisibility(View.VISIBLE);
				} else if (progress == -1) {
					download_status_container.setVisibility(View.GONE);
					canceldownloadbutton.setVisibility(View.GONE);
					buttons_spacer_left.setVisibility(View.VISIBLE);
					downloadbutton.setVisibility(View.VISIBLE);
					buttons_spacer_right.setVisibility(View.VISIBLE);
				} else {
					download_progress_bar.setProgress(progress);
					text.setText("下载中：" + progress + "%");
				}
			}

		}
	}

	class AppListAsyncTask extends AsyncTask<String, Integer, AppInfo> {

		@Override
		protected AppInfo doInBackground(String... params) {
			try {
				loading.setVisibility(View.VISIBLE);
				gallery.setVisibility(View.GONE);
				appInfo = ApplicationEx.data.getAppInfo(params[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return appInfo;
		}

		@Override
		protected void onPostExecute(AppInfo result) {
			showGalleryInfo();
		}
	}
}
