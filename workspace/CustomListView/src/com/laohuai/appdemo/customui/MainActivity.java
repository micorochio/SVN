package com.laohuai.appdemo.customui;

import java.util.ArrayList;
import java.util.List;

import com.laohuai.appdemo.customui.ui.MyListView;
import com.laohuai.appdemo.customui.ui.MyListView.OnRefreshListener;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MainActivity extends Activity {

	private List<String> data;
	private BaseAdapter adapter;
	private MyListView listView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		data = new ArrayList<String>();
		data.add("a");
		data.add("b");
		data.add("c");

		listView = (MyListView) findViewById(R.id.listView);
		adapter = new BaseAdapter() {
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView tv = new TextView(getApplicationContext());
				tv.setText(data.get(position));
				return tv;
			}

			public long getItemId(int position) {
				return 0;
			}

			public Object getItem(int position) {
				return null;
			}

			public int getCount() {
				return data.size();
			}
		};
		listView.setAdapter(adapter);

		listView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				new PullToFresh().execute();
			}
		});
	}

	class PullToFresh extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... arg0) {

			try {
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
			data.add("新item");
			return null;

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			adapter.notifyDataSetChanged();
			listView.onRefreshComplete();
		}
		// @Override
		// protected void onPostExecute(Void result) {
		// adapter.notifyDataSetChanged();
		// listView.onRefreshComplete();
		// }

	}
}