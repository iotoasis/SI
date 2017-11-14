package net.herit.business.accountmng.service;

import java.io.Serializable;

import net.herit.common.util.StringUtil;


public class AccountGroupVO implements Serializable{
	
	/** ID */
	private String id;
	/** 그룹이름 */
	private String groupName;
	/** 그룹코드 */
	private String groupCode;
	/** 설명 */
	private String description;
	
	/** actionType */
	private String actionType = "";
	

	/** 검색조건 */
	private String searchKey;
	/** 검색명 */
	private String searchVal;
	/** 선택삭제리스트 */
	private String[] checkList = null;
	
	
	
	
	public String getId() {
		return StringUtil.nvl(id);
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGroupName() {
		return StringUtil.nvl(groupName);
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupCode() {
		return StringUtil.nvl(groupCode);
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public String getDescription() {
		return StringUtil.nvl(description);
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getActionType() {
		return StringUtil.nvl(actionType);
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}	
	public String getSearchKey() {
		return StringUtil.nvl(searchKey);
	}
	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}
	public String getSearchVal() {
		return StringUtil.nvl(searchVal);
	}
	public void setSearchVal(String searchVal) {
		this.searchVal = searchVal;
	}
	public String[] getCheckList() {
		return checkList;
	}
	public void setCheckList(String[] checkList) {
		this.checkList = checkList;
	}
	
}
