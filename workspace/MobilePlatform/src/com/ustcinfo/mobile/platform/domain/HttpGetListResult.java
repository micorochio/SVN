/**
 * 工程名: 	SasMobile
 * 文件名: 	ListQuestResult.java
 * 创建人:  	ycxiong
 * 创建时间: 	2011-7-11 下午02:40:53
 * 版权所有：	Copyright (c) 2011 苏州科大恒星信息技术有限公司  
 * 文件描述: 描述该文件的作用
 * -----------------------------变更记录 ----------------------------- 
 * 日期        		变更人      		版本号  		变更描述  
 * ------------------------------------------------------------------  
 * 2011-7-11     ycxiong   	1.0.0       	first created  
 */
package com.ustcinfo.mobile.platform.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 多个记录的查询结果
 * 
 * @since SasMobile 1.0.0
 * @version 1.0 2011-7-11
 * @author ycxiong
 */
public class HttpGetListResult {
	private String result = "1";
	private String desc = "成功";
	private List<Map<String, String>> listMap = new LinkedList<Map<String, String>>();

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<Map<String, String>> getListMap() {
		return listMap;
	}

	public void setListMap(List<Map<String, String>> listMap) {
		this.listMap = listMap;
	}
}
