package com.ustcinfo.mobile.platform.widget;

import com.ustcinfo.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * 自定义进度条
 * 
 * @author WINDFREE
 * 
 */
public class ProgressBar extends LinearLayout {
    protected TextView mTextView;//显示内容
	public ProgressBar(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		draw();

	}

	private void draw() {
		LayoutInflater.from(getContext()).inflate(R.layout.progress_bar, this);
        mTextView = (TextView) findViewById(R.id.tvProgress);		
	}

	public ProgressBar(Context context) {
		super(context);
	}

}
