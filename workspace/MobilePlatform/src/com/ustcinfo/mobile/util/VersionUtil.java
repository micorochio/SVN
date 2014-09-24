package com.ustcinfo.mobile.util;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.ustcinfo.mobile.AppConstants;
import com.ustcinfo.mobile.handler.CommonJsonHandler;
import com.ustcinfo.mobile.handler.JsonHandler;
import com.ustcinfo.mobile.http.HttpConnectionUtil;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class VersionUtil {
	private int newVerCode = 0;

	private String newVerName = "";

	private static final String TAG = "VersionUtil";
	
	private String  content;//升级内容

	public int getNewVerCode() {
		return newVerCode;
	}

	public void setNewVerCode(int newVerCode) {
		this.newVerCode = newVerCode;
	}

	public String getNewVerName() {
		return newVerName;
	}

	public void setNewVerName(String newVerName) {
		this.newVerName = newVerName;
	}

	/**
	 * 获取版本编码
	 * 
	 * @param context
	 * @return
	 */
	public  int getVerCode(Context context) {
		int verCode = -1;
		try {
			verCode = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
		return verCode;
	}

	/**
	 * 获取版本名称
	 * 
	 * @param context
	 * @return
	 */
	public static String getVerName(Context context) {
		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
		return verName;
	}

	/**
	 * 获取服务器版本
	 * 
	 * @return
	 */
	public boolean getServerVerCode() {
		try {
			Map<String, String> map = HttpConnectionUtil.get(
					AppConstants.getUrlVersion(), new CommonJsonHandler());
			try {
				newVerCode = Integer.parseInt(map.get("verCode"));
				newVerName = map.get("verName");
			} catch (Exception e) {
				newVerCode = -1;
				newVerName = "";
				return false;
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			newVerCode = -1;
			newVerName = "";
			return false;
		}
		return true;
	}

	/**
	 * 暂时不使用
	 * 
	 * @author WINDFREE
	 * 
	 */
	class VersionJsonHandler extends JsonHandler<Boolean> {

		@Override
		protected Boolean extractFromJson(JSONObject jsonObj)
				throws JSONException {
			try {
				newVerCode = Integer.parseInt(jsonObj.getString("verCode"));
				newVerName = jsonObj.getString("verName");
			} catch (Exception e) {
				newVerCode = -1;
				newVerName = "";
				return false;
			}
			return true;
		}

	}
	public  static boolean checkApkExist(Context context, String packageName) {

		if (packageName == null || "".equals(packageName))

			return false;

		try {

			ApplicationInfo info = context.getPackageManager()

			.getApplicationInfo(packageName,

			PackageManager.GET_UNINSTALLED_PACKAGES);

			return true;

		} catch (NameNotFoundException e) {

			return false;

		}

	}

}
