package net.herit.business.dm.service;

import java.io.Serializable;

public class NotificationVO implements Serializable {
	String sur;
	String con;
	String cnf;
	String createTime;
	/**
	 * @return the sur
	 */
	public String getSur() {
		return sur;
	}
	/**
	 * @param sur the sur to set
	 */
	public void setSur(String sur) {
		this.sur = sur;
	}
	/**
	 * @return the con
	 */
	public String getCon() {
		return con;
	}
	/**
	 * @param con the con to set
	 */
	public void setCon(String con) {
		this.con = con;
	}
	/**
	 * @return the cnf
	 */
	public String getCnf() {
		return cnf;
	}
	/**
	 * @param cnf the cnf to set
	 */
	public void setCnf(String cnf) {
		this.cnf = cnf;
	}
	/**
	 * @return the createTime
	 */
	public String getCreateTime() {
		return createTime;
	}
	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	public String toJsonString() {
		String retStr = "{"
				  + "\"sur\": \"" + this.getSur() + "\", "
				  + "\"con\": \"" + this.getCon() + "\", "
				  + "\"cnf\": \"" + this.getCnf() + "\", "
				  + "\"createTime\": \"" + this.getCreateTime() + "\""
				  + "}";
		
		return retStr;
	}
}
