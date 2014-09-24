package com.ustcinfo.mobile.platform.view;


import java.util.Map;

import org.androidpn.client.Constants;
import org.androidpn.client.NotificationService;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ustcinfo.mobile.ApplicationEx;
import com.ustcinfo.R;
import com.ustcinfo.mobile.util.AndroidSysUtil;
import com.ustcinfo.mobile.util.AppMgrUtil;

/**
 * 登录页面
 */
public class ESSLoginActivity extends Activity implements OnClickListener {
	// private static final String TAG = "SapLoginActivity";
	String deviceId;
	// 定义界面元素
	Button btnLogin;
	Button btnJieBang;
	EditText etPwd;
	EditText etAccount;
	Intent intent = null;
	private SharedPreferences sharedPrefs;
	private boolean flag = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		sharedPrefs = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,
				Context.MODE_PRIVATE);
		findViewById();
		btnLogin.setOnClickListener(this);
		btnJieBang.setOnClickListener(this);
		new AppListAsyncTask()
				.execute(this.getIntent().getStringExtra("appId"));
	}

	/**
	 * 绑定界面元素
	 */
	private void findViewById() {
		pdshow = new ProgressDialog(this);
		pdshow.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pdshow.setMessage("获取数据中，请稍后...");
		pdshow.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					pdshow.dismiss();
					finish();
				}
				return false;
			}
		});
		pdshow.show();
		etAccount = (EditText) findViewById(R.id.login_edit_account);
		etPwd = (EditText) findViewById(R.id.login_edit_pwd);
		btnLogin = (Button) findViewById(R.id.login_btn_login);
		btnJieBang = (Button) findViewById(R.id.jie_bang_btn);
	}

	@Override
	public void onClick(View v) {
		String userAccount = etAccount.getText().toString().trim();
		String pwd = etPwd.getText().toString().trim();
		if ("".equals(userAccount)) {
			Toast.makeText(ESSLoginActivity.this, "账号不能为空!", Toast.LENGTH_LONG)
					.show();
			return;
		} else if ("".equals(pwd) && etPwd.getVisibility() != View.GONE) {
			Toast.makeText(ESSLoginActivity.this, "密码不能为空!", Toast.LENGTH_LONG)
					.show();
			return;
		} else {
			switch (v.getId()) {
			case R.id.login_btn_login:
				// if(((ApplicationEx)getApplication()).getUserId()==null)
				// {//如果没有注册用户信息
				if (btnLogin.getText().equals("注册")) {
					pdshow.setMessage("注册中，请稍后...");
					pdshow.show();
					new LoginAsyncTask().execute(userAccount, pwd);
				} else {
					if (flag == false) {
						Toast.makeText(ESSLoginActivity.this,
								"发生网络异常，请检查网络设置！", Toast.LENGTH_LONG).show();
					} else {
						// 注册过了，进入应用
						intent = new Intent(this, MobileMainActivity.class);
						startActivity(intent);
						finish();
					}
				}
				break;
			case R.id.jie_bang_btn:
				if (flag == true) {
					pdshow.setMessage("解绑中，请稍后...");
					pdshow.show();
					new JieBangAsyncTask().execute(userAccount);
				} else {
					Toast.makeText(ESSLoginActivity.this, "发生网络异常，请检查网络设置！",
							Toast.LENGTH_LONG).show();
				}
				break;
			default:
				break;
			}
		}
	}

	private void updateAccount(String name, String passwd) {
		Editor editor = sharedPrefs.edit();
		editor.putString(Constants.XMPP_USERNAME, name);
		editor.putString(Constants.XMPP_PASSWORD, passwd);
		editor.commit();
	}

	// 创建菜单，这个菜单我们在XML文件中定义 这里加载进来就OK
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		// 加载我们的菜单文件
		inflater.inflate(R.layout.set_options_menu, menu);
		return true;
	}

	// 菜单选项事件
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_about) {// 取消
			return true;
		} else if (item.getItemId() == R.id.menu_quit) {
			finish();
		}
		return true;

	}

	private ProgressDialog pdshow;

	class JieBangAsyncTask extends AsyncTask<String, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			Boolean result;
			try {
				// TODO 解绑应用
				result = ApplicationEx.data.unRegisterUser(deviceId, params[0]);
			} catch (Exception e) {
				return null;
			}
			return result;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			pdshow.dismiss();
			if (result != null && result) {
				Toast.makeText(ESSLoginActivity.this, "解绑成功！",
						Toast.LENGTH_LONG).show();
				if (AndroidSysUtil.serviceIsRunning(ESSLoginActivity.this,
						"org.androidpn.client.NotificationService")) {
					stopService(new Intent(ESSLoginActivity.this,
							NotificationService.class));
				}
				finish();
				SharedPreferences preferences = getSharedPreferences(
						ApplicationEx.SP_USER, MODE_PRIVATE);
				Editor editor = preferences.edit();
				editor.clear();
				editor.commit();
				AppMgrUtil.openSysHaveIcon(
						ESSLoginActivity.this.getApplicationContext(),
						"com.ustcinfo");
			} else {
				Toast.makeText(ESSLoginActivity.this, "发生网络异常，解绑失败！",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	class LoginAsyncTask extends
			AsyncTask<String, Integer, Map<String, Object>> {

		@Override
		protected Map<String, Object> doInBackground(String... params) {
			Map<String, Object> result = null;
			try {
				// if(((ApplicationEx)getApplication()).getUserId()==null)
				// {//如果没有注册用户信息
				// TODO 注册用户信息
				result = ApplicationEx.data.registerUser(deviceId, params[0],
						"", params[1]);
				// }
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return result;

		}

		@Override
		protected void onPostExecute(Map<String, Object> result) {
			pdshow.dismiss();
			if (result != null) {
				if ((Boolean) result.get("result")) {
					SharedPreferences preferences = getSharedPreferences(
							ApplicationEx.SP_USER, MODE_PRIVATE);
					Editor editor = preferences.edit();
					editor.putString("userId", (String) result.get("userId"));
					editor.putString("areaId", (String) result.get("areaId"));
					editor.putString("dynamicPwd",
							(String) result.get("dynamicPwd"));
					editor.putString("deviceId",
							(String) result.get("deviceId"));
					editor.commit();
					Toast.makeText(ESSLoginActivity.this, "注册成功！",
							Toast.LENGTH_LONG).show();
					intent = new Intent(ESSLoginActivity.this,
							MobileMainActivity.class);
					startActivity(intent);
					finish();
				} else {
					Toast.makeText(ESSLoginActivity.this,
							(String) result.get("desc"), Toast.LENGTH_LONG)
							.show();
				}
			} else {
				Toast.makeText(ESSLoginActivity.this, "发生网络异常，注册失败！",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	class AppListAsyncTask extends
			AsyncTask<String, Integer, Map<String, String>> {

		@Override
		protected Map<String, String> doInBackground(String... params) {
			Map<String, String> map = null;
			try {
				TelephonyManager phoneMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				deviceId = phoneMgr.getDeviceId();
				map = ApplicationEx.data.getLoginInfo(deviceId);
			} catch (Exception e) {
			}
			return map;
		}

		@Override
		protected void onPostExecute(Map<String, String> result) {

			if (result != null) {
				SharedPreferences preferences = getSharedPreferences(
						ApplicationEx.SP_USER, MODE_PRIVATE);
				Editor editor = preferences.edit();
				for (String key : result.keySet()) {
					editor.putString(key, result.get(key));
				}
				editor.commit();
				// if(((ApplicationEx)getApplication()).getUserId() == null)
				// {//表示没有注册
				if (result.size() == 0) {
					btnLogin.setText("注册");
					etPwd.setVisibility(View.VISIBLE);
					btnJieBang.setVisibility(View.INVISIBLE);
				} else {
					btnLogin.setText("登陆");
					etAccount.setText(((ApplicationEx) getApplication())
							.getUserId());
					etAccount.setEnabled(false);
					etAccount.setFocusable(false);
					etPwd.setText("000000");
					etPwd.setFocusable(false);
					etPwd.setEnabled(false);
					btnJieBang.setVisibility(View.VISIBLE);
					updateAccount(
							((ApplicationEx) getApplication()).getUserId(),
							"000000");
				}
				flag = true;
			} else {
				Toast.makeText(ESSLoginActivity.this,
						R.string.prompt_exception_net, Toast.LENGTH_LONG)
						.show();
			}
			if (pdshow != null) {
				pdshow.dismiss();

			}
		}
	}
}
