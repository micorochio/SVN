package com.ustcinfo.mobile.download;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.ustcinfo.R;
import com.ustcinfo.mobile.AppConstants;

public class DownloadService extends Service {
	private Context mContext = this;
	private NotificationManager mNotificationManager;

	public final static String V_URL = "v_url";// 下载地址
	public final static String V_ID = "v_id";// appID
	public final static String V_NAME = "v_name";// app名称

	public final static String V_RATE = "v_rate";
	public final static String V_HANDLER = "v_handler";
	public final static String V_FILEPATH = "v_filepath";
	// 固定存放下载的音乐的路径：SD卡目录下
	public static final String DOWNLOAD_SD_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ File.separator
			+ "essapk" + File.separator;

	/**
	 * 进度条handler
	 * 
	 * @author liuzhaogang
	 * 
	 */
	class ProcessHandler extends Handler {
		private Notification mNotification;

		/**
		 * 更新系统版本
		 */
		private void updateSys(String filePath) {
			Uri iru = Uri.fromFile(new File(filePath));
			Intent inten = new Intent(Intent.ACTION_VIEW);
			inten.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			inten.setDataAndType(iru, "application/vnd.android.package-archive");
			startActivity(inten);
		}

		public ProcessHandler(Intent intnt) {
			int icon = R.drawable.app_down_btn_sel;
			CharSequence tickerText = "开始下载";
			long when = System.currentTimeMillis();
			mNotification = new Notification(icon, tickerText, when);
			// 放置在"正在运行"栏目中
			mNotification.flags = Notification.FLAG_ONGOING_EVENT;
			RemoteViews contentView = new RemoteViews(
					mContext.getPackageName(),
					R.layout.download_notification_layout);
			// 指定个性化视图
			mNotification.contentView = contentView;
			contentView.setTextViewText(R.id.fileName,
					intnt.getStringExtra("appName"));
			contentView.setTextViewText(R.id.rate, 0 + "%");
			contentView.setProgressBar(R.id.progress, 100, 0, false);
			PendingIntent contentIntent = PendingIntent.getActivity(mContext,
					0, intnt, PendingIntent.FLAG_UPDATE_CURRENT);
			// 指定内容意图
			mNotification.contentIntent = contentIntent;
			mNotificationManager.notify(
					Integer.valueOf(intnt.getStringExtra("appId")),
					mNotification);
		}

		public void handleMessage(android.os.Message msg) {
			Map<String, Object> map = (Map<String, Object>) msg.obj;
			int id = (Integer) map.get(V_ID);
			switch (msg.what) {
			case 1:			
				int rate = (Integer) map.get(V_RATE);
				String appName = (String) map.get(V_NAME);
				if (rate < 100) {
					// 更新进度
					RemoteViews contentView = mNotification.contentView;
					contentView.setTextViewText(R.id.fileName, appName);
					contentView.setTextViewText(R.id.rate, rate + "%");
					contentView.setProgressBar(R.id.progress, 100, rate, false);
					// 最后别忘了通知一下,否则不会更新
					mNotificationManager.notify(id, mNotification);
				} else {
					// 安装下载好的APK包
					String filePath = ((File) map.get(V_FILEPATH))
							.getAbsolutePath();
					mNotificationManager.cancel(id);
					updateSys(filePath);
					stopSelf();
				}
				break;
			case -1:
				// 取消通知
				Toast.makeText(mContext, "下载异常，请重试!", 1).show();
				mNotificationManager.cancel(id);
				stopSelf();
				break;
			}
		};
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		System.out.println(Environment.getExternalStorageState() + "------"
				+ Environment.MEDIA_MOUNTED);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String url = intent.getStringExtra("downLoadUrl");// 下载地址
		int id = Integer.valueOf(intent.getStringExtra("appId"));// apkid
		String appName = intent.getStringExtra("appName");// apk名称
		String packageName = intent.getStringExtra("packageName");// apk名称
		final Map<String, Object> map = new HashMap<String, Object>();
		map.put(V_ID, id);
		map.put(V_NAME, appName);
		map.put(V_FILEPATH, DOWNLOAD_SD_PATH + appName);
		map.put(V_URL, url);
		map.put("packageName", packageName);
		map.put(V_HANDLER, new ProcessHandler(intent));

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			url=AppConstants.getApkUrl()+url;
			download(url, new File(DOWNLOAD_SD_PATH), map);
		} else {

			Toast.makeText(this, "未有SD卡", 1).show();

		}
		return super.onStartCommand(intent, flags, startId);
	}

	private void download(final String url, final File savedir,
			final Map<String, Object> map) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					FileDownloader loader = new FileDownloader(
							DownloadService.this, url, savedir, 1, map);

					loader.download(new DownloadProgressListener() {
						Map<String, Object> m = map;
						Handler handler = (Handler) m.get(V_HANDLER);
						String packageName = (String) map.get("packageName");

						@Override
						public void onDownloadSize(int rate) {// 实时获知文件已经下载的数据长度
							Message msg = new Message();
							msg.what = 1;
							msg.obj = map;
							map.put(V_RATE, rate);
							handler.sendMessage(msg);// 发送消息
							// 发送特定action的广播
							Intent intent = new Intent();
							intent.setAction(AppConstants.ACTION_DOWNLOADING);
							intent.putExtra("progress", rate);
							intent.putExtra("packageName", packageName);
							sendBroadcast(intent);

						}

					});

				} catch (Exception e) {
					Message msg = new Message();
					msg.what = -1;
					msg.obj = map;
					((Handler) map.get(V_HANDLER)).sendMessage(msg);// 发送消息
					Intent intent = new Intent();
					intent.setAction(AppConstants.ACTION_DOWNLOADING);
					intent.putExtra("progress", -1);
					intent.putExtra("packageName", (String) map.get("packageName"));
					sendBroadcast(intent);
				}

			}
		}).start();

	}

	/**
	 * 创建下载目录
	 */
	public static void createPath(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdir();
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
