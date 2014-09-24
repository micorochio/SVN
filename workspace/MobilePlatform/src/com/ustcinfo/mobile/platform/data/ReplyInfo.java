package com.ustcinfo.mobile.platform.data;


/**
 * 回复信息
 */
public class ReplyInfo {
	private Integer REPLY_ID;
	private Integer COMMENT_ID;
	private UserInfo userInfo;
	private String REPLY_CONTENT;
	private String REPLY_TIME;
	private Integer REPLY_STATE;
	public Integer getREPLY_ID() {
		return REPLY_ID;
	}
	public void setREPLY_ID(Integer rEPLY_ID) {
		REPLY_ID = rEPLY_ID;
	}
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
	public String getREPLY_CONTENT() {
		return REPLY_CONTENT;
	}
	public void setREPLY_CONTENT(String rEPLY_CONTENT) {
		REPLY_CONTENT = rEPLY_CONTENT;
	}
	public String getREPLY_TIME() {
		return REPLY_TIME;
	}
	public void setREPLY_TIME(String rEPLY_TIME) {
		REPLY_TIME = rEPLY_TIME;
	}
	public Integer getREPLY_STATE() {
		return REPLY_STATE;
	}
	public void setREPLY_STATE(Integer rEPLY_STATE) {
		REPLY_STATE = rEPLY_STATE;
	}
}
