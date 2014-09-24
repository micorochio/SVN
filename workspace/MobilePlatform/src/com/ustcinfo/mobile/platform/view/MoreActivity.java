package com.ustcinfo.mobile.platform.view;

import java.util.HashMap;
import java.util.Map;

import org.androidpn.client.NotificationService;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.ustcinfo.mobile.ApplicationEx;
import com.ustcinfo.mobile.handler.GetListHandler;
import com.ustcinfo.mobile.handler.PostResultHandler;
import com.ustcinfo.mobile.http.HttpConnectionUtil;
import com.ustcinfo.R;
import com.ustcinfo.mobile.platform.domain.HttpGetListResult;
import com.ustcinfo.mobile.platform.domain.HttpPostResult;
import com.ustcinfo.mobile.util.AndroidSysUtil;
import com.ustcinfo.mobile.util.AppMgrUtil;

public class MoreActivity extends Activity {

	private LinearLayout sys_sync;
	private LinearLayout sys_set;
	private LinearLayout sys_unbind;
	private LinearLayout sys_about;
	String version;
	private boolean isAdmin = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_info);
		findView();
		setUpListeners();
		// 获取packagemanager的实例
		PackageManager packageManager = getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		version = packInfo.versionName;
		// new checkCompetenceAsyncTask().execute(); //验证权限
	}

	private void setUpListeners() {
		sys_sync.setOnClickListener(new OnClickListener() {// 检查更新
			@Override
			public void onClick(View v) {
				new CheckAsyncTask().execute();
			}

		});
		sys_set.setOnClickListener(new OnClickListener() {// 系统设置

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		sys_unbind.setOnClickListener(new OnClickListener() {// 解除绑定

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (isAdmin) {
							unRegisteOthers();
						} else
							unRegisteUser();
					}
				});
		sys_about.setOnClickListener(new OnClickListener() {// 关于

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						LayoutInflater inflater = LayoutInflater
								.from(MoreActivity.this);
						View view = inflater.inflate(R.layout.about, null);
						((TextView) view.findViewById(R.id.tv_version))
								.setText(version);
						new AlertDialog.Builder(MoreActivity.this)
								.setTitle("关于").setView(view)
								.setPositiveButton("确定", null).show();
					}
				});

	}

	private void findView() {
		sys_sync = (LinearLayout) findViewById(R.id.sys_sync);
		sys_set = (LinearLayout) findViewById(R.id.sys_set);
		sys_unbind = (LinearLayout) findViewById(R.id.sys_unbind);
		sys_about = (LinearLayout) findViewById(R.id.sys_about);
	}

	private void unRegisteUser() {
		new AlertDialog.Builder(MoreActivity.this)
				.setTitle("解绑")
				.setMessage(
						"确定解绑正在使用的账号："
								+ ((ApplicationEx) getApplication())
										.getUserId() + " ?")
				.setNegativeButton("取消", null)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						new JieBangAsyncTask().execute();
					}
				}).show();
	}

	private void unRegisteOthers() {
		new AlertDialog.Builder(MoreActivity.this).setItems(
//				new String[] { "解绑现用账号", "解绑其他账号" },
				new String[] { "解绑现用账号"},
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						switch (which) {
						case 0:
							unRegisteUser();
							break;
						case 1:
							final EditText et = new EditText(MoreActivity.this);
							et.setHint("请输入需要解绑的账号");
							et.setLayoutParams(new LinearLayout.LayoutParams(
									LayoutParams.FILL_PARENT,
									LayoutParams.WRAP_CONTENT));
							et.setSingleLine(true);
							new AlertDialog.Builder(MoreActivity.this)
									.setTitle("解绑其他账号")
									.setMessage("请输入需要解绑的账号")
									.setView(et)
									.setNegativeButton("取消", null)
									.setPositiveButton(
											"解绑",
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													// TODO Auto-generated
													// method stub
													String account = et
															.getText()
															.toString().trim();
													if (account.equals("")||account.length()!=11) {
														Toast.makeText(MoreActivity.this, "请输入正确解绑号码！", Toast.LENGTH_SHORT).show();
													} else {
														new unRegisteOthersAsyncTask()
																.execute(account);
													}
												}
											}).show();
							break;
						default:
							break;
						}
					}
				}).show();
	}

	class CheckAsyncTask extends AsyncTask<String, Integer, String> {
		ProgressDialog pd = ProgressDialog.show(MoreActivity.this, null,
				"检查更新...");

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pd.dismiss();
			Toast.makeText(MoreActivity.this, "已经是最新版本", Toast.LENGTH_LONG)
					.show();
		}

	}

	class JieBangAsyncTask extends AsyncTask<String, Integer, Boolean> {
		ProgressDialog pd = ProgressDialog.show(MoreActivity.this, null,
				"正在解绑...");

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd.show();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			Boolean result;
			try {
				// TODO 解绑应用
				result = ApplicationEx.data.unRegisterUser(
						((ApplicationEx) getApplication()).getDeviceId(),
						((ApplicationEx) getApplication()).getUserId());
			} catch (Exception e) {
				return null;
			}
			return result;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			pd.dismiss();
			if (result != null && result) {
				Toast.makeText(MoreActivity.this, "解绑成功！", Toast.LENGTH_LONG)
						.show();
				if (AndroidSysUtil.serviceIsRunning(MoreActivity.this,
						"org.androidpn.client.NotificationService")) {
					stopService(new Intent(MoreActivity.this,
							NotificationService.class));
				}
				Intent intent = new Intent(getBaseContext(),
						ESSLoginActivity.class);
				// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
				startActivity(intent);
				finish();
				// AppMgrUtil.openSysHaveIcon(MoreActivity.this.getApplicationContext(),"cn.com.starit.mobile.service.view.gansu");
				SharedPreferences preferences = getSharedPreferences(
						ApplicationEx.SP_USER, MODE_PRIVATE);
				Editor editor = preferences.edit();
				editor.clear();
				editor.commit();
				AppMgrUtil.openSysHaveIcon(
						MoreActivity.this.getApplicationContext(),
						"com.ustcinfo");
			} else {
				Toast.makeText(MoreActivity.this, "发生网络异常，解绑失败！",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	class unRegisteOthersAsyncTask extends
			AsyncTask<String, Integer, HttpPostResult> {
		ProgressDialog pd = ProgressDialog.show(MoreActivity.this, null,
				"正在解绑...");

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd.show();
		}

		@Override
		protected HttpPostResult doInBackground(String... params) {
			String url = "http://220.248.229.53:18080/platform-service/sap/bz/unRegisterMobileUser1";
			Map<String, String> map = new HashMap<String, String>();
			map.put("userCode", params[0]);
			HttpPostResult result;
			try {
				result = HttpConnectionUtil.post(url, map,
						new PostResultHandler());
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return result;
		}

		@Override
		protected void onPostExecute(HttpPostResult result) {
			pd.dismiss();
			if (result != null) {
			  System.out.println(result.getResult());	
				if(result.getResult().equals("1")){
				Toast.makeText(MoreActivity.this, "解绑成功!",
						Toast.LENGTH_LONG).show();}
				else {
					Toast.makeText(MoreActivity.this, "已经解绑!",
							Toast.LENGTH_LONG).show();
				}
			}
			else {
				Toast.makeText(MoreActivity.this, "发生网络异常，解绑失败！",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	class checkCompetenceAsyncTask extends
			AsyncTask<String, Integer, HttpGetListResult> {
		ProgressDialog pd = ProgressDialog.show(MoreActivity.this, null,
				"正在验证权限...");

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd.show();
		}

		@Override
		protected HttpGetListResult doInBackground(String... params) {
			String url = "http://220.248.229.53:18080/platform-service/sap/bz/getIfUserRegister/"
					+ ((ApplicationEx) getApplication()).getUserId();
			HttpGetListResult result;
			try {
				result = HttpConnectionUtil
						.get(url, new GetListHandler("list"));
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return result;
		}

		@Override
		protected void onPostExecute(HttpGetListResult result) {
			pd.dismiss();
			if (result != null) {
				try {
					if (result.getListMap().get(0).get("IfUserRegister")
							.equals("1")) {
						isAdmin = true;
					}
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			} else {
				isAdmin = false;
				Toast.makeText(MoreActivity.this, "发生网络异常，验证失败",
						Toast.LENGTH_LONG).show();
			}
		}
	}
}
