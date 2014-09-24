package com.zing.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
/**
 * 网络连接工具
 * @author P_Zing
 *
 */
public class WebUtil {
	/**
	 * 输入地址，获取图片
	 * @param Url
	 * @return
	 * @throws Exception
	 */
	public static Bitmap getPic(String Url) throws Exception{
		URL url = new URL(Url);//获取URL
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();//打开连接
		conn.setConnectTimeout(10000);//设置超时时间
		conn.setRequestMethod("GET");//设置请求方式
		int responseCode = conn.getResponseCode();
		if(responseCode==200){//请求返回200时
			InputStream inputStream = conn.getInputStream();
			Bitmap bitmap = BitmapFactory.decodeStream(inputStream);//将输入流转换成图片
			inputStream.close();
			return bitmap;
			
		}
		return null;
		
	}

}
