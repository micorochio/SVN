package com.ustcinfo.mobile.platform.widget;

import java.util.ArrayList;

import com.ustcinfo.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class PageIndicator extends LinearLayout {
	private Context mContext;
	private Drawable mCurrentDrawable;
	private Drawable mNormalDrawable;

	private int mCurrentPage = 0;// 当前页
	private int mMaxPage = 0;// 最大也页

	private ArrayList<ImageView> arrList;

	public PageIndicator(Context context) {
		super(context);
		this.mContext = context;
	}

	public PageIndicator(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		this.mContext = paramContext;
		loadDefaultDrawable();
	}

	private void loadDefaultDrawable() {
		Resources localResources = this.mContext.getResources();
		this.mNormalDrawable = localResources.getDrawable(R.drawable.icon_grey);
		this.mCurrentDrawable = localResources
				.getDrawable(R.drawable.icon_green);
	}

	public void setMaxPage(int maxNum) {
		this.mMaxPage = maxNum;
		init();
	}

	private void init() {
		removeAllViews();
		arrList = new ArrayList<ImageView>();
		for (int i = 0; i < this.mMaxPage; i++) {
			ImageView iv = new ImageView(this.mContext);
			iv.setPadding(5, 0, 5, 0);
			addView(iv);
			if (i == 0) {
				iv.setImageDrawable(this.mCurrentDrawable);
			} else {
				iv.setImageDrawable(this.mNormalDrawable);
			}
			arrList.add(iv);
		}

	}

	public void pre() {
		setPage(-1 + this.mCurrentPage);
	}

	public void setPage(int curPage) {
		if (curPage >= this.mMaxPage || curPage < 0
				|| curPage == this.mCurrentPage) {
			return;
		}
		this.arrList.get(curPage).setImageDrawable(this.mCurrentDrawable);
		this.arrList.get(this.mCurrentPage).setImageDrawable(
				this.mNormalDrawable);
		this.mCurrentPage = curPage;
	}

	public void next() {
		setPage(1 + this.mCurrentPage);
	}
}
