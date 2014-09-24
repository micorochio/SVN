package com.ustcinfo.mobile.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
	/**
	 * 手机号判断
	 * @param mobiles   手机号
	 * @return
	 */
	public static boolean isMobileNO(String mobiles){
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
}
