package com.ustcinfo.mobile.platform.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * ClassName:ParcelableObject 闇�瀹炵幇涓変釜鏂规硶锛宒escribeContents()杩斿洖0灏卞彲浠�銆�
 * writeToParcel(Parcel parcel, int flag) 鍐欏叆Parcel鍜宲ublic static final
 * Parcelable.Creator<parcelableobject> CREATOR
 * 渚涘閮ㄧ被鍙嶅簭鍒楀寲璇ョ被 涓�畾瑕佹敞鎰忛『搴忓悗闈㈣繖涓袱涓嚱鏁拌鍜屽啓鐨勯『搴忚鐩稿悓锛屼笉鐒舵暟鎹細鍑洪敊
 * 
 * @author liuzhaogang
 * 
 */
public class UserInfoPara implements Parcelable {

	private String id;
	private String areaid;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String name;

	public String getAreaid() {
		return areaid;
	}

	public void setAreaid(String areaid) {
		this.areaid = areaid;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(name);
		dest.writeString(areaid);
	}

	public static final Parcelable.Creator<UserInfoPara> CREATOR = new Creator<UserInfoPara>() {

		@Override
		public UserInfoPara createFromParcel(Parcel source) {
			UserInfoPara userInfo = new UserInfoPara();
			userInfo.id = source.readString();
			userInfo.name = source.readString();
			userInfo.areaid = source.readString();
			return userInfo;
		}

		@Override
		public UserInfoPara[] newArray(int size) {
			return new UserInfoPara[size];
		}

	};
}
