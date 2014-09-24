package com.ustcinfo.mobile;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.ustcinfo.mobile.platform.data.ESSDataProvider;
import com.ustcinfo.mobile.platform.data.ESSDataProviderDemoImpl;
import com.ustcinfo.mobile.platform.data.ESSDataProviderRunImpl;
import com.ustcinfo.mobile.util.AsyncImageLoader;

/**
 * 复写Application 提供线程安全的HttpClient
 * 
 * @author WINDFREE
 * 
 */
public class ApplicationEx extends Application {
	public static final String SP_USER = "userInfo";
	private static final String TAG = "ApplicationEx";
	public static String MODE = AppConstants.RUN_MODE;
	public static ESSDataProvider data = AppConstants.RUN_MODE.equals(MODE) ? new ESSDataProviderRunImpl()
			: new ESSDataProviderDemoImpl();

	public static int timeoutSec = 40;

	public static boolean NetWorkStatus = false;
//	public static LoginInfo loginInfo = null;// 缓存用户登录信息

//	public static String phoneNumber = "-1";
	private static HttpClient httpClient;

	private static ApplicationEx singleton;

	public static ApplicationEx getInstance() {
		return singleton;
	}

	/**
	 * 图片异步操作
	 */
	private static AsyncImageLoader asyncImageLoader = new AsyncImageLoader();
	/**
	 * 服务根地址，用来拼装服务地址
	 */
	public static String SERV_ROOT_ADDR = "/platform-service/";

	@Override
	public void onCreate() {
		super.onCreate();
		SharedPreferences preferences = getSharedPreferences(
				ApplicationEx.SP_USER, MODE_PRIVATE);
		Editor editor = preferences.edit();
		httpClient = createHttpClient();
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String phone = telephonyManager.getLine1Number(); // 获取用户电话号码 可能为空
		if (!"".equals(phone) && null != phone && !"null".equals(phone)) {
			editor.putString("phoneNumber", phone);
		}
		editor.commit();
		singleton = this;
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		shutdownHttpClient();
	}
//线程终止时，关闭连接
	@Override
	public void onTerminate() {
		super.onTerminate();
		shutdownHttpClient();
	}

	public static HttpClient getHttpClient() {
		if(httpClient ==null){
			httpClient = createHttpClient();
		}
		return httpClient;
	}

	private void shutdownHttpClient() {
		if (httpClient != null && httpClient.getConnectionManager() != null) {
			httpClient.getConnectionManager().shutdown();
		}
	}

	private static HttpClient createHttpClient() {
		Log.d(TAG, "createHttpClient()...");
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
		HttpProtocolParams.setUseExpectContinue(params, true);
		HttpConnectionParams.setConnectionTimeout(params, timeoutSec * 1000);
		HttpConnectionParams.setSoTimeout(params, timeoutSec * 1000);

		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		schReg.register(new Scheme("https",
				SSLSocketFactory.getSocketFactory(), 443));
		// ClientConnectionManager负责管理HttpClient的HTTP连接
		ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
				params, schReg);
		return new DefaultHttpClient(conMgr, params);
	}

	public static AsyncImageLoader GetAsyncImageLoader() {
		return asyncImageLoader;
	}
	
	public String getUserId(){
		return getSharedPreferences(ApplicationEx.SP_USER, MODE_PRIVATE).getString("userId", null);
	}
	
	public String getUserName(){
		return getSharedPreferences(ApplicationEx.SP_USER, MODE_PRIVATE).getString("userName", null);
	}
	
	public String getAreaId(){
		return getSharedPreferences(ApplicationEx.SP_USER, MODE_PRIVATE).getString("areaId", null);
	}
	
	public String getPhoneNumber(){
		return getSharedPreferences(ApplicationEx.SP_USER, MODE_PRIVATE).getString("phoneNumber", null);
	}
	
	public String getDeviceId(){
		return getSharedPreferences(ApplicationEx.SP_USER, MODE_PRIVATE).getString("deviceId", null);
	}
	
	public String getPwd(){
		return getSharedPreferences(ApplicationEx.SP_USER, MODE_PRIVATE).getString("dynamicPwd", null);
	}
}
