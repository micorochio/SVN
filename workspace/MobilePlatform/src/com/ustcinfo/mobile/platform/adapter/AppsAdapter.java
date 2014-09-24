package com.ustcinfo.mobile.platform.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ustcinfo.mobile.AppConstants;
import com.ustcinfo.mobile.ApplicationEx;
import com.ustcinfo.mobile.download.DownloadService;
import com.ustcinfo.R;
import com.ustcinfo.mobile.platform.data.AppInfo;
import com.ustcinfo.mobile.platform.domain.AppsModel;
import com.ustcinfo.mobile.platform.view.AppMgrCenterActivity;
import com.ustcinfo.mobile.platform.view.MobileMarketActivity;
import com.ustcinfo.mobile.util.AppMgrUtil;
import com.ustcinfo.mobile.util.AsyncImageLoader.ImageCallback;
import com.ustcinfo.mobile.util.ImageUtil;

public class AppsAdapter extends BaseAdapter {

	private static int APP_PAGE_SIZE = 12;

	private List<AppInfo> mAppsModel = new ArrayList<AppInfo>();

	private int page;

	PackageManager pm;

	private AppInfo info;

	private Context context;

	private boolean isAnim = false;

	public AppsAdapter(Context context, List<AppInfo> info, int page) {
		this.context = context;
		this.page = page;
		int i = page * APP_PAGE_SIZE;
		int iEnd = i + APP_PAGE_SIZE;
		pm = context.getPackageManager();
		while ((i < info.size()) && (i < iEnd)) {
			mAppsModel.add(info.get(i));
			i++;
		}
		AppInfo p = new AppInfo();
		p.setAppId(99999);
		mAppsModel.add(p);

	}

	@Override
	public int getCount() {
		return mAppsModel.size();
	}

	@Override
	public Object getItem(int position) {
		return mAppsModel.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		final viewHolder holder;
		if (convertView == null) {
			holder = new viewHolder();
			convertView = inflater.inflate(R.layout.grid_row_layout, null);
			holder.app_icon = (ImageView) convertView
					.findViewById(R.id.itemAppIcon);
			holder.layout = (LinearLayout) convertView
					.findViewById(R.id.grid_layout);
			holder.app_name = (TextView) convertView
					.findViewById(R.id.itemName);
			holder.app_temp = (TextView) convertView.findViewById(R.id.folder);
			holder.progressBar = (ProgressBar) convertView
					.findViewById(R.id.progress);
			holder.updateImage = (ImageView) convertView
					.findViewById(R.id.updateImg);
			holder.app_ic_uninstall = (ImageView) convertView
					.findViewById(R.id.itemAppUninstall);
			convertView.setTag(holder);
		} else {
			holder = (viewHolder) convertView.getTag();
		}
		info = mAppsModel.get(position);
		if (info.getAppId() != 99999) {
			holder.app_name.setText(info.getAppName());
			if (!AppMgrUtil.isInstall(context, info.getPackageName())) {// 未安装
				holder.updateImage.setVisibility(View.VISIBLE);
				holder.updateImage
						.setBackgroundResource(R.drawable.ic_uninstall);
			}
			if (AppMgrUtil.isNeedUpdate(context, info.getPackageName(),
					info.getVersion())
					&& -1 != AppMgrUtil.getVerCode(context,
							info.getPackageName())) {// 可升级
				holder.updateImage.setVisibility(View.VISIBLE);
				holder.updateImage.setBackgroundResource(R.drawable.can_update);
			}
			if (AppMgrUtil.isInstall(context, info.getPackageName())) {// 已经安装,使用配置的程序图标
				try {
					ApplicationInfo appInfo = pm
							.getApplicationInfo(info.getPackageName(),
									PackageManager.GET_META_DATA);
					Drawable appIcon = pm.getApplicationIcon(appInfo);
					holder.app_icon.setImageDrawable(ImageUtil.toRoundCorner(
							((BitmapDrawable) appIcon), 20));//  设置图标格式位圆角
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
			} else {// 未安装 根据包名称获取图标
				Drawable cachedImage = ApplicationEx.GetAsyncImageLoader()
						.loadDrawable(
								AppConstants.getImageUrl()
										+ info.getAppIconUrl(),
								holder.app_icon, new ImageCallback() {

									@Override
									public void imageLoaded(
											Drawable imageDrawable,
											ImageView imageView, String imageUrl) {
										imageView
												.setImageDrawable(imageDrawable);

									}
								});
				if (cachedImage == null) {
					// Drawable d=
					// context.getResources().getDrawable(R.drawable.ic_launcher);
					// holder.app_icon.setImageDrawable(d);
				} else {
					holder.app_icon.setImageDrawable(cachedImage);
				}

			}
			holder.layout.setOnClickListener(new OnClickListener() {
				AppInfo resole = info;

				@Override
				public void onClick(View v) {
					Intent intent = null;
					if (!AppMgrUtil.isInstall(context, resole.getPackageName())
							|| AppMgrUtil.isNeedUpdate(context,
									resole.getPackageName(),
									resole.getVersion())) {
						intent = new Intent();
						intent.setClass(context, DownloadService.class);
						// 这里不再使用bindService,而使用startService
						Bundle bundle = new Bundle();
						bundle.putString("appName", resole.getAppName());
						bundle.putString("packageName", resole.getPackageName());
						bundle.putString("downLoadUrl", resole.getDownLoadUrl());
						bundle.putString("appId",
								String.valueOf(resole.getAppId()));
						intent.putExtras(bundle);
						context.startService(intent);
						AppMgrCenterActivity.progressBars.put(
								resole.getPackageName(), holder.progressBar);
					} else {
						AppMgrUtil.openSys(context,resole.getPackageName());
					}

				}
			});
			holder.app_ic_uninstall.setOnClickListener(new OnClickListener() {
				AppInfo resole = info;

				@Override
				public void onClick(View v) {
					if (AppMgrUtil.isInstall(context.getApplicationContext(),
							resole.getPackageName())) {
						boolean flag = AppMgrUtil.unInstallApp(
								context.getApplicationContext(),
								resole.getPackageName());
						if (flag == true) {// 更新试图
							holder.updateImage.setVisibility(View.VISIBLE);
							holder.updateImage
									.setBackgroundResource(R.drawable.ic_uninstall);
						}
					} else {
						Toast.makeText(context, "没有安装应用程序", Toast.LENGTH_LONG)
								.show();
					}

				}
			});
			holder.layout.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					GridView gv = (GridView) v.getParent();
					if (isAnim == false) {
						Animation shake = AnimationUtils.loadAnimation(context,
								R.anim.shake);
						shake.reset();
						shake.setFillAfter(true);
						for (int i = 0; i < gv.getChildCount(); i++) {
							View view = gv.getChildAt(i);
							if (null != ((viewHolder) view.getTag()).app_name
									.getText()
									&& "添加应用".equals(((viewHolder) view
											.getTag()).app_name.getText()))
								continue;
							view.startAnimation(shake);
							((viewHolder) view.getTag()).app_ic_uninstall
									.setVisibility(View.VISIBLE);

						}
						isAnim = true;
					} else {
						for (int i = 0; i < gv.getChildCount(); i++) {
							View view = gv.getChildAt(i);
							view.clearAnimation();
							((viewHolder) view.getTag()).app_ic_uninstall
									.setVisibility(View.GONE);
						}
						isAnim = false;
					}
					return true;
				}
			});
		} else {
			// holder.app_icon.setImageDrawable(ImageUtil.toRoundCorner(
			// ((BitmapDrawable)
			// context.getResources().getDrawable(R.drawable.ic_add_app)),
			// 20));//  设置图标格式位圆角
			holder.app_icon.setImageDrawable(context.getResources()
					.getDrawable(R.drawable.ic_add_app));//  设置图标格式位圆角
			holder.app_name.setText("添加应用");
			holder.layout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context,
							MobileMarketActivity.class);//
					context.startActivity(intent);

				}
			});
		}

		return convertView;
	}

	public class btnClickEvent implements OnClickListener {

		private AppsModel resole;

		public btnClickEvent(AppsModel info) {
			resole = info;
		}

		@Override
		public void onClick(View v) {
			Intent intent = null;
			System.out.println(resole.getPackage_Name());
			if (resole.getPackage_Name().equals("com.android.contacts")) {
				if (resole.getActivity_Name().equals(
						"com.android.contacts.DialtactsActivity")) {
					intent = new Intent(Intent.ACTION_DIAL);
				}
				if (resole.getActivity_Name().equals(
						"com.android.contacts.DialtactsContactsEntryActivity")) {
					intent = new Intent(Intent.ACTION_CALL_BUTTON);
				}
			} else {
				intent = context.getPackageManager().getLaunchIntentForPackage(
						resole.getPackage_Name());
			}
			context.startActivity(intent);
		}

	}

	public class viewHolder {
		private ImageView app_icon;
		private ImageView app_ic_uninstall;
		private TextView app_name, app_temp;
		private ProgressBar progressBar;
		private ImageView updateImage;
		private LinearLayout layout;
	}

}
