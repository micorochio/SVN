package com.ustcinfo.mobile;

public class AppConstants {
	public static final String RUN_MODE = "RUN_MODE";
	public static final String DEMO_MODE = "DEMO_MODE";
	public static String appID = "";

	public static final String FIRST_ACTIVITY = "first_activity"; // 每个activity的标识
	public static final String MOBLIE_MARKET_ACTIVITY = "mobile_market_activity";

	public static final String KT_ACTIVITY = "kt_activity";
	public static final String RANK_ACTIVITY = "rank_activity";

	public static final int FISRT_ACTIVITY_INDEX = 0;
	public static final int MARKET_ACTIVITY_INDEX = 1;

	public static final int MORE_ACTIVITY_INDEX = 2;

	public static final String UPDATE_VERJSON = "ver.json";

	// public static String ipPort = "135.161.221.164:9080";// 测试

	public static String ipPort = "113.200.188.210:19999";// 外网地址

	// public static String ipPort = "192.168.200.246:9080";
	/**
	 * 获取最新版本URL
	 * 
	 * @return
	 */
	public static final String getImageUrl() {
		return "http://" + AppConstants.ipPort + ApplicationEx.SERV_ROOT_ADDR
				+ "image/";
	}

	/**
	 * 获取最新版本APK
	 * 
	 * @return
	 */
	public static final String getApkUrl() {
		return "http://" + AppConstants.ipPort + ApplicationEx.SERV_ROOT_ADDR
				+ "apk/";
	}

	public static final String REST_SUCC = "1";
	public static final String REST_FAIL = "0";

	public static final String SERVER_URL = "http://" + AppConstants.ipPort
			+ ApplicationEx.SERV_ROOT_ADDR;

	public static String ACTION_DOWNLOADING = "android.intent.action.DOWNLOAD_DOWNLOADING";
	public static final String DYN_PWD = "000000";

	public static String getUrlVersion() {
		return SERVER_URL + "conf/ver.json";
	}
}
