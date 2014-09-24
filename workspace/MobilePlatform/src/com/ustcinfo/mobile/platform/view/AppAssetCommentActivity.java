package com.ustcinfo.mobile.platform.view;

import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ustcinfo.mobile.ApplicationEx;
import com.ustcinfo.R;
import com.ustcinfo.mobile.platform.data.CommentInfo;
import com.ustcinfo.mobile.platform.data.ReplyInfo;

/**
 * 手机应用评论信息
 */
public class AppAssetCommentActivity extends ListActivity implements OnScrollListener{
	private AppAssetCommentActivity appAssetCommentActivity= this;
	private static final Integer COUNT = 5;
	private List<CommentInfo> commentInfo = null;
	private List<ReplyInfo> replyInfo;
	private ListView listview;
	private Handler handler;
	private int mLastItem = 5;  
    private int mCount =4;  
	private MyAdapter adapter;
	private LinearLayout mlayout;
	private LinearLayout loading;
	private boolean FLAG = false;
	private String userid;
	private String syslistId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comments_activity);
		initdata();
		
	}
	/**
	 * 
	 * 异步加载数据
	 * 
	 */
	public void initdata(){
		loading = (LinearLayout)findViewById(R.id.fullscreen_loading_indicator);
		new ComListAsyncTask().execute(this.getIntent().getStringExtra("appId"));
		
	}
	/**
	 *判断是否有评论 
	 * 
	 */
	
	public void comment(){
		if (commentInfo != null) {
        	if(commentInfo.size()>0){
        		this.showCommentInfo();
//        		isComment();
        		}else if(commentInfo.size()==0){
        			LinearLayout layout = (LinearLayout) this.findViewById(R.id.shafa);
        			this.showCommentInfo();
        			layout.setVisibility(View.VISIBLE);
        		}
		} else {
				Toast.makeText(AppAssetCommentActivity.this, R.string.prompt_exception_net,
					Toast.LENGTH_LONG).show();}
	}
	/**
	 * 
	 * 
	 * 判断是否评论过
	 */
	public void isComment(){
		if(((ApplicationEx)getApplication()).getUserId().equals(userid)){
			FLAG = true;
		}else{
			
		}
		
	}
	
	/**
	 * @Title：showCommentInfo
	 * @Description：显示评论信息
	 * @return:void
	 * @throws
	 */
	public void showCommentInfo() {
		//判断是否安装此程序
		//若有评论信息，则输出；没有怎输出文字显示
        
		Button addcomment = (Button) findViewById(R.id.btn_add_comment);
		Button remcomment = (Button) findViewById(R.id.btn_remove_comment);
		if(FLAG == true){
			addcomment.setVisibility(View.GONE);
			remcomment.setVisibility(View.VISIBLE);
		}
		addcomment.setOnClickListener(new MyButtonListener());
		remcomment.setOnClickListener(new RemoveButtonListenter());
				adapter = new MyAdapter(this, commentInfo, replyInfo,
				R.layout.comment_list_item, R.layout.reply_comment_list_item);
		setListAdapter(adapter);
		
	}

/**
 * 
 * 添加评论
 * @author Administrator
 *
 */
	public class MyButtonListener implements OnClickListener {
		public void onClick(View v) {
			// 生成一个intent对象
			Intent intent = appAssetCommentActivity.getIntent();
			intent.putExtra("appId", syslistId);
			intent.setClass(AppAssetCommentActivity.this,
					AppAddCommentActivity.class);
			AppAssetCommentActivity.this.startActivityForResult(intent, 0);
		}
	}
	
	public class RemoveButtonListenter implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.btn_remove_comment:
				if(ApplicationEx.data.deleteCommentInfo(((ApplicationEx)getApplication()).getUserId())){
					Toast.makeText(AppAssetCommentActivity.this,"删除评论成功！" , Toast.LENGTH_LONG).show();
					showCommentInfo();
				}
				else{
					Toast.makeText(AppAssetCommentActivity.this,"删除评论失败！" , Toast.LENGTH_LONG).show();
				}
			} 
		}
		
	}

	public final class ViewHolder {
		public TextView author;
		public TextView time;
		public TextView btn_replay;
		public TextView comment_body;

	}

	private class MyAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private List<CommentInfo> list;
		private List<ReplyInfo> replyInfo;
		private int resource;
		private int resourceReply;

		public MyAdapter(Context context, List<CommentInfo> list,
				List<ReplyInfo> replyInfo, int resource, int resourceReply) {
			this.mInflater = LayoutInflater.from(context);
			this.list = list;
			this.replyInfo = replyInfo;
			this.resource = resource;
			this.resourceReply = resourceReply;
		}

		@Override
		public int getCount() {
			// return count;
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			Log.i("TAG", "getView." + position);
			View rowView = mInflater.inflate(resource, null);

			ViewHolder holder = (ViewHolder) rowView.getTag();
			if (holder == null) {
				holder = new ViewHolder();
				holder.author = (TextView) rowView.findViewById(R.id.author);
				holder.time = (TextView) rowView.findViewById(R.id.time);

				holder.btn_replay = (TextView) rowView
						.findViewById(R.id.btn_replay);
				holder.comment_body = (TextView) rowView
						.findViewById(R.id.comment_body);
				rowView.setTag(holder);
			}
			String a = list.get(position).getUserInfo().getUSERINFO_ID();
			holder.author.setText(list.get(position).getUserInfo()
					.getUSERINFO_ID().toString().trim());
			
			if(a==((ApplicationEx)getApplication()).getUserId()){
				FLAG = true;
			}else{
				FLAG = false;
			}
			holder.time.setText(list.get(position).getCOMMENT_TIME().toString()
					.trim());

			holder.btn_replay.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					String userCode = list.get(position).getUserInfo()
							.getUSERINFO_ID().toString().trim();
					userid = userCode;
					String timestr = list.get(position).getCOMMENT_TIME()
							.toString().trim();
					String comment_bodystr = list.get(position)
							.getCOMMENT_CONTENT().toString().trim();
					 syslistId = list.get(position).getSYSLIST_ID().toString().trim();
					String commentid = list.get(position).getCOMMENT_ID().toString().trim();
					Intent intent = new Intent();
					intent.putExtra("userCode", userCode);
					intent.putExtra("time", timestr);
					intent.putExtra("comment_body", comment_bodystr);
					intent.putExtra("commentId", commentid);
					intent.setClass(AppAssetCommentActivity.this,
							AppAddReplayActivity.class);
					AppAssetCommentActivity.this.startActivityForResult(intent, 0);

				}
			});
			
			holder.comment_body.setText(list.get(position).getCOMMENT_CONTENT()
					.toString().trim());

			LinearLayout line = (LinearLayout) rowView
					.findViewById(R.id.reply_container);
			//

			List<ReplyInfo> listInfo = list.get(position).getReplyInfoList();

			// 循环
			View rowReplyView;
			TextView repauthor;
			TextView reptime;
			TextView repbody;
			TextView repbtn;
			if (listInfo != null) {
				for (ReplyInfo info : listInfo) {
					rowReplyView = mInflater.inflate(resourceReply, null);
					repauthor = (TextView) rowReplyView
							.findViewById(R.id.author);
					repauthor.setText(info.getUserInfo().getUSERINFO_ID());
					reptime = (TextView) rowReplyView.findViewById(R.id.time);
					reptime.setText(info.getREPLY_TIME());
					repbody = (TextView) rowReplyView
							.findViewById(R.id.comment_body);
					repbody.setText(info.getREPLY_CONTENT());
					repbtn = (TextView) rowReplyView
							.findViewById(R.id.btn_replay);
					repbtn.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							String userCode = list.get(position).getUserInfo()
									.getUSERINFO_ID().toString().trim();
							String text2 = list.get(position).getCOMMENT_TIME()
									.toString().trim();
							String text3 = list.get(position)
									.getCOMMENT_CONTENT().toString().trim();
							String text4 = list.get(position).getCOMMENT_ID().toString().trim();
							Intent intent = new Intent();
							intent.putExtra("userCode", userCode);
							intent.putExtra("time", text2);
							intent.putExtra("comment_body", text3);
							intent.putExtra("commentId", text4);
							intent.setClass(AppAssetCommentActivity.this,AppAddReplayActivity.class);
							AppAssetCommentActivity.this.startActivity(intent);
						}
					});
					line.addView(rowReplyView);
				}
			}
			return rowView;
		}

	}
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
			mLastItem = firstVisibleItem + visibleItemCount +5;  
			if (adapter.getCount() > mCount) {  
            listview.removeFooterView(mlayout);  
        }  
						
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		handler = new Handler();
		if (mLastItem == adapter.getCount() 
                && scrollState == OnScrollListener.SCROLL_STATE_IDLE) {  
            if (adapter.getCount() <= mCount) {  
                handler.postDelayed(new Runnable() {  
                    @Override  
                    public void run() {  
                    	int a = adapter.getCount();
                    			a +=5;
                        adapter.notifyDataSetChanged();  
                        listview.setSelection(mLastItem);  
                    }  
                }, 1000);  
            }  
        }  
	}

	class ComListAsyncTask extends AsyncTask<String, Integer, List<CommentInfo>>{

		@Override
		protected List<CommentInfo> doInBackground(String... params) {
		try{
			loading.setVisibility(View.VISIBLE);
			commentInfo = ApplicationEx.data.getCommentInfo(params[0],COUNT);
			
		}catch(Exception e){
			e.printStackTrace();
		}
			return commentInfo;
		}
		
		protected void onPostExecute(List<CommentInfo> result) {
			if (result != null) {
				comment();
			}
			loading.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==0&&resultCode==0){
			initdata();
		}
	}
}

