package com.ustcinfo.mobile.platform.widget;


import com.ustcinfo.R;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;




/**
 * Custom alert dialog
 * @author Ryan
 */
public class AlertDialogUtil {

	private Context context;
	private String title;
	private String content;
	private AlertDialog alertDialog;
	private OnClickListener confrimClickListener;
	private OnClickListener cancelClickListener;
	private boolean isText = true;
	private BaseAdapter baseAdapter;
	private OnItemClickListener onItemClickListener;
	private TextView btn_confirm;
	private TextView btn_cancel;
	private String confrimText;
	private String cancelText;
	private Window window;
	
	/**
	 * Constructor
	 * @param context
	 */
	public AlertDialogUtil(Context context){
		this.context = context;
		alertDialog = new AlertDialog.Builder(this.context).create();
		alertDialog.show();
		window = alertDialog.getWindow();
		window.setContentView(R.layout.alert_dialog);
		btn_confirm = (TextView)window.findViewById(R.id.btn_confirm);
		btn_cancel = (TextView)window.findViewById(R.id.btn_cancel);
	}
	
	/**
	 * set title for alert dialog
	 * @param title
	 */
	public void setTitle(String title){
		this.title = title;
	}
	
	/**
	 * set content for alert dialog
	 * @param content
	 */
	public void setContent(String text){
		this.content = text;
	}
	
	/**
	 * set list view for alert dialog
	 * @param content
	 */
	public void setContent(BaseAdapter listAdapter,OnItemClickListener itemClickListener){
		isText = false;
		this.baseAdapter = listAdapter;
		this.onItemClickListener = itemClickListener;
	}
	
	/**
	 * set title for alert dialog
	 * @param titleId
	 */
	public void setTitle(int titleId){
		this.title = context.getString(titleId);
	}
	
	/**
	 * set content for alert dialog
	 * @param contentId
	 */
	public void setContent(int contentId){
		this.content = context.getString(contentId);
	}
	
	public void show(){
		
		TextView tv_title = (TextView)window.findViewById(R.id.alert_title);
		ListView lv_items = (ListView) window.findViewById(R.id.lv_alert_dialog);
		TextView tv_content = (TextView)window.findViewById(R.id.alert_content);
		
		tv_title.setText(title);
		
		if (isText) {
			tv_content.setVisibility(View.VISIBLE);
			tv_content.setText(content);
		}else {
			View lineView = window.findViewById(R.id.line);
			lineView.setVisibility(View.GONE);
			btn_cancel.setVisibility(View.GONE);
			lv_items.setVisibility(View.VISIBLE);
			lv_items.setAdapter(baseAdapter);
			lv_items.setOnItemClickListener(onItemClickListener);
		}
		
	}
	
	/**
	 * set listener for the confirm button
	 * @param cancelClickListener
	 */
	public void setCancelClickListener(String cancelText,OnClickListener cancelClickListener){
		this.cancelText = cancelText;
//		btn_cancel.setText(this.cancelText);
		this.cancelClickListener = cancelClickListener;
		btn_cancel.setOnClickListener(this.cancelClickListener);
	}
	
	/**
	 * set listener for the cancel button
	 * @param confrimClickListener
	 */
	public void setConfirmClickListener(String confrimText,OnClickListener confrimClickListener){
		this.confrimClickListener = confrimClickListener;
		this.confrimText = confrimText;
//		btn_confirm.setText(this.confrimText);
		btn_confirm.setOnClickListener(this.confrimClickListener);
	}
	
	/**
	 * dismiss the alert dialog
	 */
	public void cancel(){
		alertDialog.cancel();
	}
	
}
