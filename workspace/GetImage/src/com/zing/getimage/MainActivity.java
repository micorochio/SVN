package com.zing.getimage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.zing.util.WebUtil;

public class MainActivity extends Activity {
	private EditText inputUrl;
	private ImageView showPic;
	private Button goButton;
	private Bitmap pic = null;
	public  final  int CHANGE_UI = 1;
	public  Handler handler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			if(msg.what==1){
				Bitmap pic = (Bitmap) msg.obj;
				if (pic != null) {
					showPicture(pic);
				} else {
					Toast.makeText(getApplicationContext(), "图片未取到，请重输地址",
							Toast.LENGTH_LONG).show();
				}
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findView();
		
		
		goButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				new Thread(){
					@Override
					public void run() {
						String url = inputUrl.getText().toString();
						System.out.println(url);
						try {
							
							pic = WebUtil.getPic(url);
							Message msg = new Message();
							msg.what= CHANGE_UI;
							msg.obj = pic;
							handler.sendMessage(msg);
						} catch (Exception e) {
							e.printStackTrace();
							
						}
					};
				}.start();
				
				
				
			}
		});
		
	}

	private boolean showPicture(Bitmap pic) {
		showPic.setImageBitmap(pic);
		return true;
	}

	public void findView() {
		inputUrl = (EditText) findViewById(R.id.text_url);
		showPic = (ImageView) findViewById(R.id.img_show);
		goButton=(Button) findViewById(R.id.btn_go);
	}
}
