package com.ustcinfo.mobile.util;

import java.util.ArrayList;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class AndroidSysUtil {
	public static boolean serviceIsRunning(Context context, String serviceName) {
		// 获取ActivityManager
		ActivityManager myManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
				.getRunningServices(30);
		// 获取系统当前的所有Service，最多可以获取30个
		for (int i = 0; i < runningService.size(); i++) {
			// 遍历当前运行的Service，判断是否存在指定的Service
			if (runningService.get(i).service.getClassName().toString()
					.equals(serviceName)) {
				return true;
			}

		}
		return false;
	}
}
