package com.ustcinfo.mobile.platform.data;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.ustcinfo.mobile.AppConstants;
import com.ustcinfo.mobile.handler.GetListHandler;
import com.ustcinfo.mobile.handler.PostResultHandler;
import com.ustcinfo.mobile.http.HttpConnectionUtil;
import com.ustcinfo.mobile.platform.domain.HttpGetListResult;
import com.ustcinfo.mobile.platform.domain.HttpPostResult;

public class ESSDataProviderDemoImpl implements ESSDataProvider {
	@Override
	public AppInfo getAppInfo(String appId) {
		AppInfo appInfo = null;
		List<AppInfo> list= this.getListAppInfo("");
		for(AppInfo info:list) {
			if(info.getAppId().toString().equals(appId)){
				List<String> imgURL = new ArrayList<String>();
				imgURL.add("http://lh1.apkok.com/sort/2010-10-27/e0aa5dd990c916ccad834279fd880bec.png");
				imgURL.add("http://www.apkok.com/attach/201203/27/113217d1pk7k0ki6phe9zk.png");
				imgURL.add("http://lh1.apkok.com/sort/2010-10-27/e0aa5dd990c916ccad834279fd880bec.png");
				imgURL.add("http://www.apkok.com/attach/201203/27/113217d1pk7k0ki6phe9zk.png");
				imgURL.add("http://lh1.apkok.com/sort/2010-10-27/e0aa5dd990c916ccad834279fd880bec.png");
				info.setVersion(1);
				info.setImgURL(imgURL);
				info.setDescription("这里是测试的描述信息，这里是测试的描述信息，这里是测试的描述信息，这里是测试的描述信息，" +
						"这里是测试的描述信息，这里是测试的描述信息，这里是测试的描述信息，这里是测试的描述信息，这里是测试的描述信息，这里是测试的描述信息，" +
						"这里是测试的描述信息，这里是测试的描述信息，这里是测试的描述信息，这里是测试的描述信息。");
				appInfo = info;
				break;
			}
		}
		return appInfo;
	}

	@Override
	public List<CommentInfo> getCommentInfo(String appId, Integer count) {
		return this.getListCommentInfo("");
	}
	public List<CommentInfo> getListCommentInfo(String appId){
		String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
		List<CommentInfo> result = new LinkedList<CommentInfo>();
		CommentInfo com;
		UserInfo us;
		com = new CommentInfo();
		us = new UserInfo();
		com.setCOMMENT_ID(1);
		us.setUSERINFO_NAME("tt");
		com.setUserInfo(us);
		com.setCOMMENT_TIME(date);
		com.setCOMMENT_CONTENT("非常好@！");
		com.setReplyInfoList(this.getListCommentContainReplyInfo("1"));
		result.add(com);
		
		com = new CommentInfo();
		us = new UserInfo();
		com.setCOMMENT_ID(2);
		us.setUSERINFO_NAME("ts");
		com.setUserInfo(us);
		com.setCOMMENT_TIME(date);
		com.setReplyInfoList(this.getListCommentContainReplyInfo("2"));
		com.setCOMMENT_CONTENT("h哈哈！");
		result.add(com);
		
		com = new CommentInfo();
		us = new UserInfo();
		com.setCOMMENT_ID(3);
		us.setUSERINFO_NAME("td");
		com.setUserInfo(us);
		com.setCOMMENT_TIME(date);
		com.setCOMMENT_CONTENT("可以！");
		result.add(com);
		
		com = new CommentInfo();
		us = new UserInfo();
		com.setCOMMENT_ID(4);
		us.setUSERINFO_NAME("tc");
		com.setUserInfo(us);
		com.setCOMMENT_TIME(date);
		com.setCOMMENT_CONTENT("是的，不错！");
		result.add(com);
		
		com = new CommentInfo();
		us = new UserInfo();
		com.setCOMMENT_ID(5);
		us.setUSERINFO_NAME("ta");
		com.setUserInfo(us);
		com.setCOMMENT_TIME(date);
		com.setCOMMENT_CONTENT("非常好@！");
		result.add(com);
		
		com = new CommentInfo();
		us = new UserInfo();
		com.setCOMMENT_ID(6);
		us.setUSERINFO_NAME("tx");
		com.setUserInfo(us);
		com.setCOMMENT_TIME(date);
		com.setReplyInfoList(this.getListCommentContainReplyInfo(""));
		com.setCOMMENT_CONTENT("还行啊@！");
		result.add(com);
		
		
		return result;
		
	}
	@Override
	public List<AppInfo> getListAppInfo(String userId) {
		List<AppInfo>  result = new LinkedList<AppInfo>();
		AppInfo app;
		app = new AppInfo();
		app.setAppId(1);
		app.setAppName("SasMobileApp2.3.apk");
		app.setAppType("应用->OSS");
		app.setAppSize("1M");
		app.setVersion(19);
		app.setPackageName("cn.com.starit.mobile.view");
		app.setAppIconUrl("http://u5.mm-img.com/rs/res/21/2012/05/28/a312/294/23294312/logo470x708184565813.png");
		app.setDownLoadUrl("http://192.168.20.130:8080/platform-service/apk/SasMobileApp2.3.apk");
		result.add(app);
		
		app = new AppInfo();
		app.setAppId(2);
		app.setAppName("sqms.apk");
		app.setAppType("应用->OSS");
		app.setAppSize("2M");
		app.setPackageName("cn.com.starit.sqms");
		app.setAppIconUrl("http://u5.mm-img.com/rs/res/21/2012/05/10/a902/170/23170902/logo470x706580250571.png");
		app.setDownLoadUrl("sqms.apk");
		app.setVersion(2);
		result.add(app);
		
		app = new AppInfo();
		app.setAppId(3);
		app.setAppName("yingyonghui.apk");
		app.setAppType("应用->OSS");
		app.setAppSize("3M");
		app.setPackageName("com.yingyonghui.market");
		app.setAppIconUrl("http://u5.mm-img.com/rs/res/22/2012/05/18/a942/229/23229942/logo470x707332318770.png");
		app.setDownLoadUrl("yingyonghui.apk");
		app.setVersion(5);
		result.add(app);
		
		app = new AppInfo();
		app.setAppId(4);
		app.setAppName("yingyonghui.apk");
		app.setAppType("应用->OSS");
		app.setAppSize("3M");
		app.setPackageName("com.yingyonghui.market");
		app.setAppIconUrl("http://u5.mm-img.com/rs/res/21/2012/05/14/a090/196/23196090/logo470x706986149135.png");
		app.setDownLoadUrl("yingyonghui.apk");
		app.setVersion(5);
		result.add(app);
		
		
		app = new AppInfo();
		app.setAppId(5);
		app.setAppName("yingyonghui.apk");
		app.setAppType("应用->OSS");
		app.setAppSize("3M");
		app.setPackageName("com.yingyonghui.market");
		app.setAppIconUrl("http://u5.mm-img.com/rs/res/23/2012/04/11/a674/996/22996674/logo470x704129265490.png");
		app.setDownLoadUrl("yingyonghui.apk");
		app.setVersion(5);
		result.add(app);
		
		
		return result;
	}
	public List<ReplyInfo> getListCommentContainReplyInfo(String commentId){
		String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
		List<ReplyInfo> result = new LinkedList<ReplyInfo>();
		ReplyInfo rep;
		UserInfo us;
		us= new UserInfo();
		rep = new ReplyInfo();
		rep.setREPLY_ID(1);
		rep.setCOMMENT_ID(1);
		us.setUSERINFO_NAME("aaa");
		rep.setUserInfo(us);
		rep.setREPLY_TIME(date);
		rep.setREPLY_CONTENT("哈哈哈");
		result.add(rep);
		
		us= new UserInfo();
		rep = new ReplyInfo();
		rep.setREPLY_ID(3);
		rep.setCOMMENT_ID(1);
		us.setUSERINFO_NAME("ttt");
		rep.setUserInfo(us);
		rep.setREPLY_TIME(date);
		rep.setREPLY_CONTENT("hahidhfohaso ");
		result.add(rep);
		
		us= new UserInfo();
		rep = new ReplyInfo();
		rep.setREPLY_ID(2);
		rep.setCOMMENT_ID(1);
		us.setUSERINFO_NAME("lisi");
		rep.setUserInfo(us);
		rep.setREPLY_TIME(date);
		rep.setREPLY_CONTENT("xxxaaaasssssss");
		result.add(rep);
		
		return result;
		
	}

	@Override
	public List<ReplyInfo> getReplyInfo(String commentId) {
		return null;
	}
	@Override
	public boolean insertCommentInfo(String userinfoId, String syslistId,
			 String commentContent, String commentState) {
		return false;
	}

	@Override
	public boolean insertReplyInfo(String userinfoId, String commentId,
			String replyContent, String replyState) {
		return false;
	}
	@Override
	public Map<String,String> getLoginInfo(String deviceId) {
		Map<String,String> loginInfo = new HashMap<String, String>();
					loginInfo.put("dynamicPwd","000000");
					loginInfo.put("userId","9900009");
					loginInfo.put("deviceId",deviceId);
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
			//甘肃电信，用户名及密码验证
			String loginURL="http://60.164.227.232:9090/SasMobileRest/services/login/"+userAccount+"/"+pwd;
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
				map.put("result", true);
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
		return true;
	}
}
