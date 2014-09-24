package com.ustcinfo.mobile.platform.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 自定义Tab-Menu
 * @author WINDFREE
 *
 */
public class TabMenu extends PopupWindow {
	//两个GridView,第一个GridView就是分页标签,位于PopupWindow的顶部，
	//第二个GridView是菜单，位于PopupWindow的主体。
	private GridView mGridMenu, mGridMenuItem;
	private LinearLayout mLayout;
	private MneuAdapter mMenuAdapter;

	public TabMenu(Context pContext, OnItemClickListener pMenuClick,
			OnItemClickListener pMenuItemClick, MneuAdapter pMenuAdapter,
			int pBgColor, int pAniTabMenu) {
		super(pContext);
		/* layout */
		mLayout = new LinearLayout(pContext);
		mLayout.setOrientation(LinearLayout.VERTICAL);
		
		/*标题选项栏*/
		mGridMenu = new GridView(pContext);
		mGridMenu.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		mGridMenu.setNumColumns(pMenuAdapter.getCount());
		mGridMenu.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		mGridMenu.setVerticalSpacing(1);
		mGridMenu.setHorizontalSpacing(1);
		mGridMenu.setGravity(Gravity.CENTER);
		mGridMenu.setOnItemClickListener(pMenuClick);
		mGridMenu.setAdapter(pMenuAdapter);
		mGridMenu.setSelector(new ColorDrawable(Color.TRANSPARENT));
		this.mMenuAdapter = pMenuAdapter;
		
		  //子选项栏 
		mGridMenuItem = new GridView(pContext);
		mGridMenuItem.setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		mGridMenuItem.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mGridMenuItem.setNumColumns(4); // 4 menu items for each row
		mGridMenuItem.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		mGridMenuItem.setVerticalSpacing(10);
		mGridMenuItem.setHorizontalSpacing(10);
		mGridMenuItem.setPadding(10, 10, 10, 10);
		mGridMenuItem.setGravity(Gravity.CENTER);
		mGridMenuItem.setOnItemClickListener(pMenuItemClick);
		
		/* add the two grids to the layout */
		mLayout.addView(mGridMenu);
		mLayout.addView(mGridMenuItem);
		
		/* other initialization */
		this.setContentView(mLayout);
		this.setWidth(LayoutParams.FILL_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setBackgroundDrawable(new ColorDrawable(pBgColor));
		this.setAnimationStyle(pAniTabMenu);
		this.setOutsideTouchable(true);
		this.setFocusable(true);
	}

	public void setMenuSelected(int pIndex) {
		mGridMenu.setSelection(pIndex);
		this.mMenuAdapter.setFocus(pIndex);
	}

	public void setMenuItemSelected(int pIndex, int pColorSelMenuItem) {
		int lCount = mGridMenuItem.getChildCount();
		for (int i = 0; i < lCount; ++i) {
			if (i != pIndex) {
				((LinearLayout) mGridMenuItem.getChildAt(i))
						.setBackgroundColor(Color.TRANSPARENT);
			}
		}
		((LinearLayout) mGridMenuItem.getChildAt(pIndex))
				.setBackgroundColor(pColorSelMenuItem);
	}

	public void setMenuItemAdapter(MenuItemAdapter pAdapter) {
		mGridMenuItem.setAdapter(pAdapter);
	}
	/**
	 * 自定义Adapter，TabMenu的每个分页的主体 
	 * @author WINDFREE
	 *
	 */
	static public class MenuItemAdapter extends BaseAdapter {
		private Context mContext;
		private int mFontColor, mFontSize;
		private String[] mTexts;
		private int[] mResId;
		/**
		 * 设置TabMenu的分页主体
		 * @param pContext	设置TabMenu的分页主体
		 * @param pTexts	按钮集合的字符串数组 
		 * @param pResId	 按钮集合的图标资源数组 
		 * @param pFontSize	 按钮字体大小 
		 * @param pFontColor	按钮字体颜色
		 */
		public MenuItemAdapter(Context pContext, String[] pTexts, int[] pResId,
				int pFontSize, int pFontColor) {
			this.mContext = pContext;
			this.mFontColor = pFontColor;
			this.mTexts = pTexts;
			this.mFontSize = pFontSize;
			this.mResId = pResId;
		}

		@Override
		public int getCount() {
			return mTexts.length;
		}

		@Override
		public Object getItem(int position) {
			return makeMenuItem(position);
		}

		private LinearLayout makeMenuItem(int position) {
			LinearLayout lLayout = new LinearLayout(this.mContext);
			lLayout.setOrientation(LinearLayout.VERTICAL);
			lLayout.setGravity(Gravity.CENTER_HORIZONTAL
					| Gravity.CENTER_VERTICAL);
			lLayout.setPadding(10, 10, 10, 10);

			TextView lView = new TextView(this.mContext);
			lView.setText(mTexts[position]);
			lView.setTextSize(mFontSize);
			lView.setTextColor(mFontColor);
			lView.setGravity(Gravity.CENTER);
			lView.setPadding(5, 5, 5, 5);

			ImageView lImageView = new ImageView(this.mContext);
			lImageView.setBackgroundResource(this.mResId[position]);

			lLayout.addView(lImageView, new LinearLayout.LayoutParams(
					new LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT)));
			lLayout.addView(lView);

			return lLayout;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return makeMenuItem(position);
		}

	}
	/**
	 * 自定义Adapter,TabMenu的分页标签部分 
	 * @author WINDFREE
	 *
	 */
	static public class MneuAdapter extends BaseAdapter {
		private Context mContext;
		private int mFontColor, mUnselectedColor, mSelectedColor;
		private TextView[] mMenuView;
		/**
		 *  设置TabMenu的title 
		 * @param pContext  调用方的上下文
		 * @param pTitles	分页标签的字符串数组
		 * @param pFontSize	 字体大小
		 * @param pFontColor	 字体颜色 
		 * @param pUnselectedColor 未选中项的背景色
		 * @param pSelectedColor 选中项的背景色
		 */
		public MneuAdapter(Context pContext, String[] pTitles, int pFontSize,
				int pFontColor, int pUnselectedColor, int pSelectedColor) {
			this.mContext = pContext;
			this.mFontColor = pFontColor;
			this.mUnselectedColor = pUnselectedColor;
			this.mSelectedColor = pSelectedColor;
			int lLen = pTitles.length;
			this.mMenuView = new TextView[lLen];
			for (int i = 0; i < lLen; ++i) {
				this.mMenuView[i] = new TextView(mContext);
				this.mMenuView[i].setText(pTitles[i]);
				this.mMenuView[i].setTextSize(pFontSize);
				this.mMenuView[i].setTextColor(pFontColor);
				this.mMenuView[i].setGravity(Gravity.CENTER);
				this.mMenuView[i].setPadding(10, 10, 10, 10);
			}
		}

		@Override
		public int getCount() {
			return this.mMenuView.length;
		}

		@Override
		public Object getItem(int arg0) {
			return this.mMenuView[arg0];
		}

		@Override
		public long getItemId(int arg0) {
			return this.mMenuView[arg0].getId();
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			View v;
			if (arg1 == null) {
				v = this.mMenuView[arg0];
			} else {
				v = arg1;
			}
			return v;
		}

		private void setFocus(int pIndex) {
			int lLen = this.mMenuView.length;
			for (int i = 0; i < lLen; ++i) {
				if (i != pIndex) {
					this.mMenuView[i].setBackgroundDrawable(new ColorDrawable(
							mUnselectedColor));
					this.mMenuView[i].setTextColor(mFontColor);
				} else {
					this.mMenuView[i].setBackgroundColor(0x00);
					this.mMenuView[i].setTextColor(mSelectedColor);
				}
			}
		}

	}
}
