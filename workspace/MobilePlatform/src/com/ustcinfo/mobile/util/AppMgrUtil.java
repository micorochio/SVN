package com.ustcinfo.mobile.util;

import java.io.File;
import java.util.List;

import com.ustcinfo.mobile.AppConstants;
import com.ustcinfo.mobile.ApplicationEx;
import com.ustcinfo.mobile.download.DownloadService;
import com.ustcinfo.mobile.platform.domain.DownloadInfo;
import com.ustcinfo.mobile.platform.domain.UserInfoPara;
import com.ustcinfo.mobile.platform.service.FileService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class AppMgrUtil {
	private static final String TAG = "AppMgrUtil";
	public final static String PAR_KEY = "com.ustcinfo.par";

	/**
	 * 获取版本编码
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static int getVerCode(Context context, String packageName) {
		int verCode = -1;
		try {
			verCode = context.getPackageManager()
					.getPackageInfo(packageName, 0).versionCode;
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
		return verCode;
	}

	/**
	 * 获取版本名称
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static String getVerName(Context context, String packageName) {
		String verName = "";
		try {
			verName = context.getPackageManager()
					.getPackageInfo(packageName, 0).versionName;
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
		return verName;
	}

	/**
	 * @Title: unInstallApp
	 * @Description: 卸载应用
	 * @param packageName
	 * @return
	 * @return boolean
	 * @throws
	 */

	public static boolean unInstallApp(Context context, String packageName) {
		try {
			Uri uri = Uri.parse("package:" + packageName);
			Intent intent = new Intent(Intent.ACTION_DELETE, uri);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, "卸载本地应用失败！", Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

	/**
	 * 打开本地应用,传入应用的包名
	 * 
	 * @param pakage
	 * @return
	 */
	public static boolean openSys(Context context, String pakage) {
		//Intent intent = new Intent();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		try {
			if(((ApplicationEx)context.getApplicationContext()).getUserId() == null){
				Toast.makeText(context, "请解绑后重新登陆", Toast.LENGTH_LONG).show();
				return false;
			}
			ComponentName cn = new ComponentName(pakage,pakage+".view."+"SplashScreen" );            
			intent.setComponent(cn);
			intent.setAction(Intent.ACTION_VIEW); 
			//intent.setAction(pakage);// 隐式调用
			Bundle mBundle = new Bundle();

			UserInfoPara u = new UserInfoPara();
			u.setId(((ApplicationEx)context.getApplicationContext()).getUserId());
			u.setName(((ApplicationEx)context.getApplicationContext()).getDeviceId());
			u.setAreaid(((ApplicationEx)context.getApplicationContext()).getAreaId());
			mBundle.putParcelable(PAR_KEY, u); // Parcelable传递对象的方法
			/**
			 * UserInfoPara userInfoPara =
			 * (UserInfoPara)getIntent().getParcelableExtra(PAR_KEY);;
			 */
			intent.putExtras(mBundle);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			//intent.addCategory(Intent.CATEGORY_DEFAULT);
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, "打开本地应用失败！", Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

	/**
	 * 更新系统版本
	 */
	public static void updateSys(Context context, String filePath) {
		Uri iru = Uri.fromFile(new File(filePath));
		Intent inten = new Intent(Intent.ACTION_VIEW);
		inten.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		inten.setDataAndType(iru, "application/vnd.android.package-archive");
		context.startActivity(inten);
	}

	/**
	 * @Title: isHaveDownloadAPK
	 * @Description:判断是否下载了要安装的应用包
	 * @return
	 * @return boolean
	 * @throws
	 */
	public static boolean isHaveDownloadedAPK(Context context, String urlstr) {
		FileService downloadAdapter;// 工具类
		String downUrl = urlstr.startsWith("http:") ? urlstr : AppConstants
				.getApkUrl() + urlstr;
		String localfile = DownloadService.DOWNLOAD_SD_PATH + urlstr;
		List<DownloadInfo> infos;
		downloadAdapter = new FileService(context);
		// downloadAdapter.open();
		// infos = downloadAdapter.getInfos(downUrl);
		// downloadAdapter.close();
		// if(infos.size() ==0 ){//表示已经下载完成
		// File file = new File(localfile);
		// if(file.exists()){
		// return true;
		// }
		// }
		return false;
	}

	/**
	 * @Title: isInstall
	 * @Description: 根据应用的包名判断应用是否安装
	 * @param pakage
	 * @return
	 * @return boolean
	 * @throws
	 */
	public static boolean isInstall(Context context, String pakage) {
		int version;
		if (pakage == null)
			return false;
		// 获取版本和名称。
		version = getVerCode(context, pakage);
		return version == -1 ? false : true;
	}

	/**
	 * @Title: isNeedUpdate
	 * @Description: 判断是否需要更新版本
	 * @param pakage
	 * @param newVersion
	 * @return
	 * @return boolean
	 * @throws
	 */
	public static boolean isNeedUpdate(Context context, String pakage,
			int newVersion) {
		int version;
		if (pakage == null || newVersion == -1)
			return false;
		// 获取版本和名称。
		version = getVerCode(context, pakage);
		return version == newVersion ? false : true;
	}
	
	/**
	 * 打开本地应用,有图标的应用
	 * @param pakage
	 * @return
	 */
	public static boolean openSysHaveIcon(Context context,String pakage) {
		 Intent intent=new Intent(); 
		 try {
			 intent =context.getPackageManager().getLaunchIntentForPackage(pakage); 
			 context.startActivity(intent); 
		 } catch(Exception e) {
			 e.printStackTrace();
			 Toast.makeText(context, "打开本地应用失败！",
						Toast.LENGTH_LONG).show();
			 return false;
		 }
		 return true;
	}
}
