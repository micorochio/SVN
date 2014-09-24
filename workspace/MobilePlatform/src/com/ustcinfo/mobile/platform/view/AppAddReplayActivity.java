package com.ustcinfo.mobile.platform.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ustcinfo.mobile.ApplicationEx;
import com.ustcinfo.R;

public class AppAddReplayActivity extends Activity{
	private String commentID;
	EditText edtext ;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reply_comment_activity);
		this.Goback();
		TextView tx = (TextView)findViewById(R.id.asset_title);
		tx.setText("回复");
		TextView userId = (TextView)findViewById(R.id.author);
		TextView ti = (TextView)findViewById(R.id.time);
		TextView com =(TextView)findViewById(R.id.comment_body);
		Intent intent = getIntent();
		String a =intent.getStringExtra("userCode");
		String b =intent.getStringExtra("time");
		String c =intent.getStringExtra("comment_body");
		commentID=intent.getStringExtra("commentId");
		userId.setText(a);
		ti.setText(b);
		com.setText(c);
		
		@SuppressWarnings("unused")
		EditText edtext= (EditText)findViewById(R.id.comment_text);
		Button okButton = (Button)findViewById(R.id.ok_button);
		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			Intent intent = getIntent();
				
			EditText edtext= (EditText)findViewById(R.id.comment_text);
				if("".equals(edtext.getText().toString()))
				{
					Toast.makeText(AppAddReplayActivity.this, "回复内容不能为空哦！", Toast.LENGTH_LONG).show();
					return;
				}
				else{
					switch(v.getId()){
					case R.id.ok_button:
						if(ApplicationEx.data.insertReplyInfo(((ApplicationEx)getApplication()).getUserId(),commentID,edtext.getText().toString(),"1")){
							Toast.makeText(AppAddReplayActivity.this,"回复成功！",Toast.LENGTH_LONG).show();
							finish();
						}
						else{
							Toast.makeText(AppAddReplayActivity.this,"回复失败！",Toast.LENGTH_LONG).show();
						}
					}
				}
			}
		});
		TextView t = (TextView)findViewById(R.id.ok_button);
		t.setText("确认");
		Button cancleButton =(Button)findViewById(R.id.cancel_button);
		cancleButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		TextView t2 = (TextView)findViewById(R.id.cancel_button);
		t2.setText("取消");
	}
	@SuppressWarnings("unused")
	private void Goback() {
		//处理返回按钮
        ImageButton imgBtn = (ImageButton) this.findViewById(R.id.back);
        final AppAddReplayActivity currentPage = this;
        imgBtn.setOnClickListener( new  OnClickListener(){
			public void onClick(View v) {
				finish();
			}
        });
	}
	
}