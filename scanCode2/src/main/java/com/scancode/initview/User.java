package com.scancode.initview;

/** 
* @ClassName: User 
* @Description: TODO	用户信息
* @author zhaoruquan
* @date 2015-9-13 下午3:42:41 
*  
*/
public class User {

	private String userName;
	
	private String account;//账号
	
	private String password;//密码
	
	private String userId;

	public User() {
		super();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
