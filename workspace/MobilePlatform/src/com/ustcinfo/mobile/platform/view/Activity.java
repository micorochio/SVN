package com.ustcinfo.mobile.platform.view;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;

public class Activity extends android.app.Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(android.R.style.Theme_Light_NoTitleBar);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		//阻止软键盘自动弹出
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		super.onCreate(savedInstanceState);
		
	}

//	@Override
//	protected void onStart() {
//		super.onStart();
//	}
//	
//	public String getUserId(){
//		return getSharedPreferences(ApplicationEx.SP_USER, MODE_PRIVATE).getString("userId", null);
//	}
//	
//	public String getUserName(){
//		return getSharedPreferences(ApplicationEx.SP_USER, MODE_PRIVATE).getString("userName", null);
//	}
//	
//	public String getAreaId(){
//		return getSharedPreferences(ApplicationEx.SP_USER, MODE_PRIVATE).getString("areaId", null);
//	}
//	
//	public String getPhoneNumber(){
//		return getSharedPreferences(ApplicationEx.SP_USER, MODE_PRIVATE).getString("phoneNumber", null);
//	}
//	
//	public String getDeviceId(){
//		return getSharedPreferences(ApplicationEx.SP_USER, MODE_PRIVATE).getString("deviceId", null);
//	}

}
