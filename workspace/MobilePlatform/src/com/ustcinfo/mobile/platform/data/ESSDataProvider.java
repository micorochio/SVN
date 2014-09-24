package com.ustcinfo.mobile.platform.data;

import java.util.List;
import java.util.Map;

import android.app.Application;

/**
 * ESS数据提供接口
 */
public interface ESSDataProvider {
	/**
	* @Title: getIntroductionInfo
	* @Description: 根据传入的应用ID，获得该应用的详细信息
	* @param appId
	* @return    
	* @return AppInfo 
	* @throws
	 */
	public AppInfo getAppInfo(String appId);
	
	/**
	* @Title: getCommentInfo
	* @Description: count 条数，获得评论信息
	* @param appId
	* @param count 一次取出多少数据
	* @return    
	* @return List<CommentInfo> 
	* @throws
	 */
	public List<CommentInfo> getCommentInfo(String appId,Integer count);
	
	/**
	* @Title: getListAppInfo
	* @Description:根据用户工号获得用户可访问的应用商城的列表
	* @param userId
	* @return    
	* @return List<AppInfo> 
	* @throws
	 */
	public List<AppInfo> getListAppInfo(String userId);
	/**
	 * @Title:getReplyInfo
	 * @Description:根据评论Id，取得用户回复信息
	 * @param commentId
	 * @return 
	 * @return List<ReplyInfo>
	 * @throws
	 */
	public List<ReplyInfo> getReplyInfo(String commentId);
	/**
	 *@Title: insertCommentInfo
	 *@Description:将用户评论信息插入数据库
	 *@param commentId,userinfoId,syslistId,commentTime,commentContent,commentState
	 *@return
	 *@throws
	 */
	
	public boolean insertCommentInfo(String userinfoId,String syslistId,String commentContent,String commentState);
	
	/**
	* @Title: getLoginInfo
	* @Description:根据传入的设备ID，获取登录所需要的信息
	* @param deviceId
	* @return    
	* @return LoginInfo 
	* @throws
	 */
	public Map<String, String> getLoginInfo(String deviceId);
	public boolean insertReplyInfo(String userinfoId,String commentId,String replyContent,String replyState);
	/**
	* @Title: registerUser
	* @Description: 注册用户信息
	* @param userAccount
	* @param deviceId
	* @param pwd
	* @return    
	* @return boolean 
	* @throws
	 */
	public Map<String, Object> registerUser(String deviceId, String userAccount,String phone,String pwd);
	/**
	 *@Title :deleteCommentInfo 
	 *@Description :删除用户评论
	 *@param userCode
	 *@return boolean
	 *@throws
	 * 
	 */
	public boolean deleteCommentInfo(String userCode);

	/**
	* @Title: unRegisterUser
	* @Description:解除用户注册
	* @param deviceId
	* @param userAccount    
	* @return void 
	* @throws
	 */
	public boolean unRegisterUser(String deviceId, String userAccount);
}
