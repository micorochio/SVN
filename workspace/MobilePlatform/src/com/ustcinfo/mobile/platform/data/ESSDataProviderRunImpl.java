package com.ustcinfo.mobile.platform.data;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.ustcinfo.mobile.AppConstants;
import com.ustcinfo.mobile.ApplicationEx;
import com.ustcinfo.mobile.handler.GetListHandler;
import com.ustcinfo.mobile.handler.PostResultHandler;
import com.ustcinfo.mobile.http.HttpConnectionUtil;
import com.ustcinfo.mobile.platform.domain.HttpGetListResult;
import com.ustcinfo.mobile.platform.domain.HttpPostResult;

import android.util.Log;


public class ESSDataProviderRunImpl implements ESSDataProvider {

	@Override
	public List<CommentInfo> getCommentInfo(String appId, Integer count) {
		String url = AppConstants.SERVER_URL+"sap/bz/getCommentInfoByAppId/"+appId;
		List<CommentInfo> listInfo = new LinkedList<CommentInfo>();
		CommentInfo info = null;
		UserInfo us = null;
		HttpGetListResult result = null;
		List<Map<String, String>> list;
		try{
			result  = HttpConnectionUtil.get(url,new GetListHandler("list"));
			if(AppConstants.REST_SUCC.equals(result.getResult())){
				list = result.getListMap();
				for(Map<String,String> map:list){
					info = new CommentInfo();
					us = new UserInfo();
					info.setCOMMENT_ID(Integer.valueOf(map.get("COMMENT_ID")));
					info.setSYSLIST_ID(Integer.valueOf(map.get("SYSLIST_ID")));
					info.setCOMMENT_CONTENT(map.get("COMMENT_CONTENT"));
					us.setUSERINFO_ID((map.get("USERINFO_ID")));
					info.setUserInfo(us);
					info.setCOMMENT_TIME(map.get("COMMENT_TIME"));
					info.setReplyInfoList(this.getReplyInfo(info.getCOMMENT_ID()+""));
					listInfo.add(info);
				}
			}else
			{
				return null;
			}
		}catch (IOException e){
			//e.printStackTrace();
			return null;
		}
		return listInfo;
	}

	@Override
	public List<AppInfo> getListAppInfo(String userId) {
		String url = AppConstants.SERVER_URL+"sap/bz/getAppInfoByUserId/"+userId;
		List<AppInfo> listInfo = new LinkedList<AppInfo>();
		AppInfo info = null;
		HttpGetListResult result = null;
		List<Map<String, String>> list;
		try {
			result = HttpConnectionUtil.get(url,new GetListHandler("list"));
			if(AppConstants.REST_SUCC.equals(result.getResult())) {
				list = result.getListMap();
				for(Map<String,String> map:list) {
					info = new AppInfo();
					info.setAppId(Integer.valueOf(map.get("SYSLIST_ID")));
					info.setAppIconUrl(map.get("SYSLIST_IMAGE"));
					info.setAppSize(map.get("SYSLIST_SIZE"));
					info.setAppType(map.get("SYSLIST_TYPE"));
					info.setDownLoadUrl(map.get("SYSLIST_ADDRESS"));
					info.setPackageName(map.get("SYSLIST_PACKAGE"));
					info.setVersion(Integer.valueOf(map.get("SYSLIST_V_CODE")));
					info.setAppName(map.get("SYSLIST_NAME"));
					info.setVersionName(map.get("SYSLIST_V_CODE_NAME"));
					listInfo.add(info);
				}
			} else {
				return null;
			}
		} catch (IOException e) {
			//e.printStackTrace();
			return null;
		}
		return listInfo;
	}

	@Override
	public AppInfo getAppInfo(String appId) {
		String infoUrl = AppConstants.SERVER_URL+"sap/bz/getAppInfoByAppId/"+appId;
		String screenShotUrl = AppConstants.SERVER_URL+"sap/bz/getScreenShotByAppId/"+appId;
		List<AppInfo> listInfo = new LinkedList<AppInfo>();
		AppInfo info = new AppInfo();
		HttpGetListResult result = null;
		List<Map<String, String>> list;
		try {
			result = HttpConnectionUtil.get(infoUrl,new GetListHandler("list"));
			if(result != null &&AppConstants.REST_SUCC.equals(result.getResult())) {
				list = result.getListMap();
				for(Map<String,String> map:list) {
					info.setAppId(Integer.valueOf(map.get("SYSLIST_ID")));
					info.setAppIconUrl(map.get("SYSLIST_IMAGE"));
					info.setAppSize(map.get("SYSLIST_SIZE"));
					info.setAppType(map.get("SYSLIST_TYPE"));
					info.setDownLoadUrl(map.get("SYSLIST_ADDRESS"));
					info.setPackageName(map.get("SYSLIST_PACKAGE"));
					info.setVersion(Integer.valueOf(map.get("SYSLIST_V_CODE")));
					info.setVersionName(map.get("SYSLIST_V_CODE_NAME"));
					info.setAppName(map.get("SYSLIST_NAME"));
					info.setDescription(map.get("SYSLIST_DESC"));
					info.setUpdateTime(map.get("SYSLIST_UP_TIME"));
					listInfo.add(info);
				}
				result = HttpConnectionUtil.get(screenShotUrl,new GetListHandler("list"));	
				if(result != null &&AppConstants.REST_SUCC.equals(result.getResult())) {
					list = result.getListMap(); 
					List<String> imgURL = new LinkedList<String>();
					for(Map<String,String> map:list) {
						imgURL.add(map.get("IMAGE_URL"));
					}
					info.setImgURL(imgURL);
				} else {
					return null;
				}
			}else {
				return null;
			}
		} catch (IOException e) {
			//e.printStackTrace();
			return null;
		}
		return info;
	}

	public List<ReplyInfo> getReplyInfo(String commentId) {
		String comUrl = AppConstants.SERVER_URL +"sap/bz/getReplyInfoByCommentId/"+commentId;
		List<ReplyInfo> listInfo = new LinkedList<ReplyInfo>();
		HttpGetListResult result = null;
		List<Map<String, String>> list;
		try{
			result = HttpConnectionUtil.get(comUrl,new GetListHandler("list"));
			if(AppConstants.REST_SUCC.equals(result.getResult())){
				list = result.getListMap();
				for(Map<String,String> map:list){
					ReplyInfo info = new ReplyInfo();
					UserInfo us = new UserInfo();
					us.setUSERINFO_ID(map.get("USERINFO_ID"));
					info.setREPLY_ID(Integer.valueOf(map.get("REPLY_ID")));
					info.setCOMMENT_ID(Integer.valueOf(map.get("COMMENT_ID")));
					info.setUserInfo(us);
					info.setREPLY_CONTENT(map.get("REPLY_CONTENT"));
					info.setREPLY_TIME(map.get("REPLY_TIME"));
					info.setREPLY_STATE(Integer.valueOf(map.get("REPLY_STATE")));
					listInfo.add(info);
				}
			}
			
		}catch(IOException e){
			 e.printStackTrace();
		}
		return listInfo;
	}

	@Override
	public boolean insertCommentInfo(String userinfoId, String syslistId,String commentContent,  String commentState) {
		String url = AppConstants.SERVER_URL + "sap/bz/insertCommentInfo";
		Map<String, String > map = new HashMap<String, String>();
		 map.put("userinfoId", userinfoId);
		 map.put("syslistId", syslistId);
		 map.put("commentContent",commentContent);
		 map.put("commentState", commentState);
		HttpPostResult result;
		try{
		 result = HttpConnectionUtil.post(url, map, new PostResultHandler());
		 // 请求参数添加
		 if(result != null &&AppConstants.REST_SUCC.equals(result.getResult())){
			 return true;
		 }
		}catch(Exception e){
			//e.printStackTrace();
			return false;
		}
		return false;
		
	}

	@Override
	public boolean insertReplyInfo(String userinfoId, String commentId,
			String replyContent, String replyState) {
		String url = AppConstants.SERVER_URL + "sap/bz/insertReplyInfo";
		HttpGetListResult result;
		Map<String, String> map = new HashMap<String, String>();
		map.put("userinfoId",userinfoId);
		map.put("commentId", commentId);
		map.put("replyContent", replyContent);
		map.put("replyState", replyState);
		try{
			
		result = HttpConnectionUtil.post(url, map, new GetListHandler("list"));
			if(result != null && AppConstants.REST_SUCC.equals(result.getResult())){
				return true;
			}
		}catch(Exception e){
			//e.printStackTrace();
			return false;
		}
		return false;
	}
	@Override
	public Map<String,String> getLoginInfo(String deviceId) {
		HttpGetListResult result = null;
		List<Map<String, String>> list;
		String userUrl =AppConstants.SERVER_URL +"sap/bz/getMobileUserInfo/"+deviceId;
		String mapUserUrl =AppConstants.SERVER_URL + "sap/bz/getMobileUserMapInfo/";
		Map<String,String> loginInfo = new HashMap<String, String>();
		try {
			//获得登陆的用户信息
			result = HttpConnectionUtil.get(userUrl,new GetListHandler("list"));
			if(result != null &&AppConstants.REST_SUCC.equals(result.getResult())) {
				list = result.getListMap();
				for(Map<String,String> map:list){
					loginInfo.put("dynamicPwd","000000");
					loginInfo.put("userId",map.get("USERINFO_ID"));
					loginInfo.put("deviceId",deviceId);
				}
			} else {
				return null;
			}
			//获得用户账号映射信息
			mapUserUrl += loginInfo.get("userId");
			result = HttpConnectionUtil.get(mapUserUrl,new GetListHandler("list"));
			List<UserMapInfo> listUserMapInfo = new LinkedList<UserMapInfo>();
			UserMapInfo userMapInfo;
			if(result != null &&AppConstants.REST_SUCC.equals(result.getResult())) {
				list = result.getListMap();
				for(Map<String,String> map:list){
					userMapInfo = new UserMapInfo();
					userMapInfo.setAppId(map.get("SYSLIST_ID"));
					userMapInfo.setMapUserAccout(map.get("MAP_USER_ACCOUNT"));
					userMapInfo.setUserAccount(map.get(loginInfo.get("userId")));
					listUserMapInfo.add(userMapInfo);
				}
//				loginInfo.setListUserMapInfo(listUserMapInfo);
			} else {
				return null;
			}
		} catch (IOException e) {
			//e.printStackTrace();
			return null;
		}
		return loginInfo;
	}

	@Override
	public Map<String,Object> registerUser(String deviceId, String userAccount,String phone,String pwd) {
		Map<String,Object> map = new HashMap<String, Object>();
		String url = AppConstants.SERVER_URL +"sap/bz/registerMobileUser";
		Map<String,String> params = new HashMap<String,String>();
		params.put("deviceId", deviceId);
		params.put("userCode", userAccount);
		params.put("userPhone",phone);
		params.put("userPwd", pwd);
		
		HttpPostResult result;
		try {
			//安徽联通，用户名及密码验证113.200.188.210:19999
			//String loginURL="http://220.248.229.53:18080/ahapp-service/comm/login/"+userAccount+"/"+pwd;
			String loginURL="http://113.200.188.210:19999/ahapp-service/comm/login/"+userAccount+"/"+pwd;
			HttpGetListResult listNewResult = HttpConnectionUtil.get(loginURL,
					new GetListHandler("list"));
			if (AppConstants.REST_SUCC.equals(listNewResult.getResult())) {//用户验证成功
				result= HttpConnectionUtil.post(url,params,new PostResultHandler());
				if(result != null && AppConstants.REST_SUCC.equals(result.getResult())) {//如果注册成功
					map.put("result", true);
					String areaId = listNewResult.getListMap().get(0).get("AREACODE");
					map.put("deviceId", deviceId);
					map.put("dynamicPwd", pwd);
					map.put("userId", userAccount);
					map.put("areaId", (areaId==null||areaId.equals("")||areaId.toUpperCase().equals("NULL"))?"931":areaId);
				}else {
					map.put("result", false);
					map.put("desc", "设备或账号已经被注册！");
				}
			} else {
				map.put("result", false);
				map.put("desc", "用户名或密码输入不正确！");
			}
		} catch (IOException e) {
			//e.printStackTrace();
			return null;
		}
		return map;
	}

	@Override
	public boolean deleteCommentInfo(String userCode) {
		return false;
	}

	@Override
	public boolean unRegisterUser(String deviceId, String userAccount) {
		String url = AppConstants.SERVER_URL +"sap/bz/unRegisterMobileUser";
		Map<String,String> params = new HashMap<String,String>();
		params.put("deviceId", deviceId);
		params.put("userCode", userAccount);
		HttpPostResult result;
		try {
			result= HttpConnectionUtil.post(url,params,new PostResultHandler());
			if(result != null && AppConstants.REST_SUCC.equals(result.getResult())) {//如果注册成功
				return true;
			}else {
				return false; 
			}
		} catch (IOException e) {
			//e.printStackTrace();
			return false;
		}
	}
}
