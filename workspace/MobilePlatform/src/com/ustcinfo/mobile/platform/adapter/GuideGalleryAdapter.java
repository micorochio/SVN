package com.ustcinfo.mobile.platform.adapter;

import java.util.List;

import com.ustcinfo.mobile.AppConstants;
import com.ustcinfo.mobile.util.AsyncImageLoader;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class GuideGalleryAdapter extends BaseAdapter {
	Context context = null;
	Gallery gallery = null;
	public List<String> imgURL = null;
	private AsyncImageLoader asyncImageLoader = new AsyncImageLoader();;

	public GuideGalleryAdapter(Context context, List<String> imgURL,
			Gallery currGallery) {
		this.context = context;
		this.imgURL = imgURL;
		this.gallery = currGallery;
		
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int position) {
		return imgURL.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView = new ImageView(context);
		String imageUrl = imgURL.get(position % imgURL.size());
		Drawable cachedImage = asyncImageLoader.loadDrawable(AppConstants.getImageUrl() +imageUrl, imageView, new AsyncImageLoader.ImageCallback() {
			@Override
			public void imageLoaded(Drawable imageDrawable,
					ImageView imageView, String imageUrl) {
				if (imageView != null && imageDrawable != null ) {
					imageView.setImageDrawable(imageDrawable);		
				}
		
			}
		});
		if (cachedImage == null) {
			//imageView.setImageResource(R.drawable.icon);
		} else {
			imageView.setImageDrawable(cachedImage);
		}
		// 设置边界对齐
		imageView.setAdjustViewBounds(true);
		imageView.setLayoutParams(new Gallery.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		// 设置比例类型
		 imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		return imageView;
	}

}
