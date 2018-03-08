package net.herit.security.dto;

import java.io.Serializable;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

public class Account implements HttpSessionBindingListener, Serializable{

	private int accountId;
	private int groupId;
	private String name;
	private String loginId;
	private String loginWordpass;
	private String createTime;
	private String updateTime;
	private String lastAccessTime;
	private String email;
	private String phone;
	private String mobile;
	private String department;
	private String disabled;
	private String ip;
	private String loginSuccessYN;
	private int failCount;

	private LoginBeanBindingListener listener = null;

	public Account() {

	}

	public Account(String loginId, String loginWordpass, LoginBeanBindingListener listener) {
		this.loginId = loginId;
		this.loginWordpass = loginWordpass;
		this.listener = listener;
	}

	@Override
	public void valueBound(HttpSessionBindingEvent event) {
		// 세션 객체에 바로 이 객체(this)가 추가될 때 자동으로 호출되는 메소드.
		if (listener != null)
            listener.loginPerformed(new LoginBeanBindingEvent(this));
	}

	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		// 세션 객체로부터 바로 이 객체(this)가 제거될 때 자동으로 호출되는 메소드.
		if (listener != null)
            listener.logoutPerformed(new LoginBeanBindingEvent(this));
	}


	public interface LoginBeanBindingListener {
		public void loginPerformed(LoginBeanBindingEvent event);
		public void logoutPerformed(LoginBeanBindingEvent event);
	}

	public class LoginBeanBindingEvent {
		private Account login = null;
		public LoginBeanBindingEvent(Account login) {
			this.login = login;
		}
		public Account getLoginBean() {
			return login;
		}
	}

	public class MemberManager implements LoginBeanBindingListener {
		// 지금까지 MemberManager 클래스에 추가된 코드들은 수정된 부분이 없으므로 생략.

		@Override
		public void loginPerformed(LoginBeanBindingEvent event) {
			// 여기에 로그인과 관련되어 부가적으로 처리할 코드들이 들어간다.
		}
		@Override
		public void logoutPerformed(LoginBeanBindingEvent event) {
			// 여기에 로그아웃과 관련되어 부가적으로 처리할 코드들이 들어간다.
		}
	}


	public int getAccountId() {
		return accountId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getLoginWordpass() {
		return loginWordpass;
	}

	public void setLoginWordpass(String loginWordpass) {
		this.loginWordpass = loginWordpass;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getLastAccessTime() {
		return lastAccessTime;
	}

	public void setLastAccessTime(String lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDisabled() {
		return disabled;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}

	public boolean wordpassMatches(String wordpass) {
		return this.loginWordpass == null ? false : this.loginWordpass == wordpass ? true
				: this.loginWordpass.equals(wordpass);
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getLoginSuccessYN() {
		return loginSuccessYN;
	}

	public void setLoginSuccessYN(String loginSuccessYN) {
		this.loginSuccessYN = loginSuccessYN;
	}

	public int getFailCount() {
		return failCount;
	}

	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}





}
