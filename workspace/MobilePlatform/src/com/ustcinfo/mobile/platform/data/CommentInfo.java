package com.ustcinfo.mobile.platform.data;

import java.util.List;

/**
 * 评论信息
 */
public class CommentInfo {
	private Integer COMMENT_ID;
	private UserInfo  userInfo;
	private Integer SYSLIST_ID;
	private String COMMENT_TIME;
	private String COMMENT_CONTENT;
	private Integer COMMENT_STATE;
	private List<ReplyInfo> replyInfoList;
	
	public Integer getCOMMENT_ID() {
		return COMMENT_ID;
	}
	public void setCOMMENT_ID(Integer cOMMENT_ID) {
		COMMENT_ID = cOMMENT_ID;
	}
	public UserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
	public Integer getSYSLIST_ID() {
		return SYSLIST_ID;
	}
	public void setSYSLIST_ID(Integer sYSLIST_ID) {
		SYSLIST_ID = sYSLIST_ID;
	}
	public String getCOMMENT_TIME() {
		return COMMENT_TIME;
	}
	public void setCOMMENT_TIME(String cOMMENT_TIME) {
		COMMENT_TIME = cOMMENT_TIME;
	}
	public String getCOMMENT_CONTENT() {
		return COMMENT_CONTENT;
	}
	public void setCOMMENT_CONTENT(String cOMMENT_CONTENT) {
		COMMENT_CONTENT = cOMMENT_CONTENT;
	}
	public Integer getCOMMENT_STATE() {
		return COMMENT_STATE;
	}
	public void setCOMMENT_STATE(Integer cOMMENT_STATE) {
		COMMENT_STATE = cOMMENT_STATE;
	}
	public List<ReplyInfo> getReplyInfoList() {
		return replyInfoList;
	}
	public void setReplyInfoList(List<ReplyInfo> replyInfoList) {
		this.replyInfoList = replyInfoList;
	}
}
