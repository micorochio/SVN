package com.ustcinfo.mobile.platform.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.ustcinfo.mobile.download.DownloadService;
import com.ustcinfo.R;

public class FileMgrActivity extends Activity {
	
	private MyReceiver receiver;
	private ProgressBar progressBar;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filemgr);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        
        if ("yes".equals(getIntent().getStringExtra("completed"))) {
        	progressBar.setProgress(100);
        }
        
        receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.MY_RECEIVER");
		//注册
		registerReceiver(receiver, filter);
    }
    
    public void cancel(View view) {
    	Intent intent = new Intent(this, DownloadService.class);
    	stopService(intent);
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	//不要忘了这一步
    	unregisterReceiver(receiver);
    }
    
    /**
	 * 广播接收器
	 * @author user
	 *
	 */
	private class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			int progress = bundle.getInt("progress");
			progressBar.setProgress(progress);
		}
	}
	
}
