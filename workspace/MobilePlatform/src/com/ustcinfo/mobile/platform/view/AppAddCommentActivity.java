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

import com.ustcinfo.mobile.AppConstants;
import com.ustcinfo.mobile.ApplicationEx;
import com.ustcinfo.R;
public class AppAddCommentActivity extends Activity {
	private EditText edtext;
	AppAssetCommentActivity appcom = new AppAssetCommentActivity();
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_comment_activity);
		this.showHeaderInfo();
		Intent intent  = new Intent();
		edtext = (EditText) findViewById(R.id.comment_text);
		TextView tx = (TextView) findViewById(R.id.asset_title);
		tx.setText("评论");
		Button okButton = (Button) findViewById(R.id.ok_button);
		TextView t = (TextView) findViewById(R.id.ok_button);
		t.setText("确认");
		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText edtext = (EditText) findViewById(R.id.comment_text);
				if("".equals(edtext.getText().toString())){
					Toast.makeText(AppAddCommentActivity.this,"评论内容不能为空哦！",Toast.LENGTH_LONG).show();
					return;
				}else{
					switch(v.getId()){
					case R.id.ok_button:
						if(ApplicationEx.data.insertCommentInfo(((ApplicationEx)getApplication()).getUserId(),AppConstants.appID,edtext.getText().toString(), "1")){
							Toast.makeText(AppAddCommentActivity.this, "评论成功！", Toast.LENGTH_LONG).show();
							finish();
						}
						else{
							Toast.makeText(AppAddCommentActivity.this, "评论失败！", Toast.LENGTH_LONG).show();
						}
					}
				} 
				
			}
 
		});
		Button cancleButton = (Button) findViewById(R.id.cancel_button);
		TextView t2 = (TextView) findViewById(R.id.cancel_button);
		t2.setText("取消");
		cancleButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void showHeaderInfo() {
		// 处理返回按钮
		ImageButton imgBtn = (ImageButton) this.findViewById(R.id.back);
		final AppAddCommentActivity currentPage = this;
		imgBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				currentPage.finish();
			}
		});
	}

}