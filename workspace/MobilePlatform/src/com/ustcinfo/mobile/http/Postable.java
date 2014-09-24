/**
 * 工程名: 	SasMobile
 * 文件名: 	Postable.java
 * 创建人:  	ycxiong
 * 创建时间: 	2011-7-14 下午06:03:17
 * 版权所有：	Copyright (c) 2011 苏州科大恒星信息技术有限公司  
 * 文件描述: 描述该文件的作用
 * -----------------------------变更记录 ----------------------------- 
 * 日期        		变更人      		版本号  		变更描述  
 * ------------------------------------------------------------------  
 * 2011-7-14     ycxiong   	1.0.0       	first created  
 */
package com.ustcinfo.mobile.http;

import java.util.Map;

/**
 * 所有需要进行post提交信息的activity都需要实现此接口
 * 
 * @since SasMobile 1.0.0
 * @version 1.0 2011-7-14
 * @author ycxiong
 */
public interface  Postable {
	/**
	 * 获取post提交参数
	 * 
	 * @return
	 */
	public abstract Map<String, String> getPostParams();;

	/**
	 * 获取post全路径目录
	 * 
	 * @return
	 */
	public abstract String getPostUrl();

	/**
	 * 校验提交的post参数是否合法
	 * 
	 * @return ""：合法； 非空字符串： 非法描述
	 */
	public abstract Boolean checkParams();

	/**
	 * 模板方法
	 */
	public void commmit();

	//boolean ischeck();
}
