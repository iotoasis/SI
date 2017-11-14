package net.herit.business.device.service;

import java.io.Serializable;

import net.herit.common.util.StringUtil;


public class MoOptionDataVO implements Serializable{

	/** DATA */
	private String data;
	/** DISPLAY_DATA */
	private String displayData;
	/** ORDER */
	private int order;

	public String getData() {
		return StringUtil.nvl(data);
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getDisplayData() {
		return StringUtil.nvl(displayData);
	}
	public void setDisplayData(String displayData) {
		this.displayData = displayData;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}

}
