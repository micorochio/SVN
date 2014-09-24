package com.ustcinfo.mobile.platform.view;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ustcinfo.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class LogoActivity extends Activity {
	private static final String TAG = "LogoActivity";
	private Thread mSplashThread;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Splash screen view
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.logo);
		// 跳转
		mSplashThread = new Thread() {
			@Override
			public void run() {
				try {
					synchronized (this) {
						// Wait given period of time or exit on touch
						wait(3000);
					}
				} catch (InterruptedException ex) {
				}
				finish();
				Intent intent = new Intent(LogoActivity.this,
						ESSLoginActivity.class);
				startActivity(intent);
			}
		};
		ExecutorService pool = Executors.newSingleThreadExecutor();
		pool.execute(mSplashThread);
		pool.shutdown();
	}

	/**
	 * Processes splash screen touch events
	 */
	@Override
	public boolean onTouchEvent(MotionEvent evt) {
		if (evt.getAction() == MotionEvent.ACTION_DOWN) {
			synchronized (mSplashThread) {
				mSplashThread.notifyAll();
			}
		}
		return true;
	}

}
