package net.herit.business.accountmng.service;

import java.io.Serializable;

import net.herit.common.util.StringUtil;


public class MenuMasterVO implements Serializable{
	
	/** 메뉴ID */
	private String menuId;
	/** 메뉴명 */
	private String menuName;
	/** 메뉴URL */
	private String urlPath;
	/** 정렬순서 */
	private String orderBy;
	/** 메뉴설명 */
	private String description;
	/** 사용여부 */
	private String disabled;
	/** 등록일자 */
	private String createTime;
	/** 수정일자 */
	private String updateTime;
	

	/** 권한그룹관리 ID */
	private String mngAccountGroupId;
	/** 생성권한 */
	private String rightC;
	/** 읽기권한 */
	private String rightR;
	/** 수정권한 */
	private String rightU;
	/** 삭제권한 */
	private String rightD;
	

	/** 메뉴ID */
	private String[] checkMenuIdList = null;
	/** 생성권한 체크리스트 */
	private String[] checkRightCList = null;
	/** 읽기권한 체크리스트 */
	private String[] checkRightRList = null;
	/** 수정권한 체크리스트 */
	private String[] checkRightUList = null;
	/** 삭제권한 체크리스트 */
	private String[] checkRightDList = null;
	
	
	
	public String getMenuId() {
		return StringUtil.nvl(menuId);
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getMenuName() {
		return StringUtil.nvl(menuName);
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getUrlPath() {
		return StringUtil.nvl(urlPath);
	}
	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}
	public String getOrderBy() {
		return StringUtil.nvl(orderBy);
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public String getDescription() {
		return StringUtil.nvl(description);
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDisabled() {
		return StringUtil.nvl(disabled);
	}
	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}
	public String getCreateTime() {
		return StringUtil.nvl(createTime);
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return StringUtil.nvl(updateTime);
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String[] getCheckRightCList() {
		return checkRightCList;
	}
	public void setCheckRightCList(String[] checkRightCList) {
		this.checkRightCList = checkRightCList;
	}
	public String[] getCheckRightRList() {
		return checkRightRList;
	}
	public void setCheckRightRList(String[] checkRightRList) {
		this.checkRightRList = checkRightRList;
	}
	public String[] getCheckRightUList() {
		return checkRightUList;
	}
	public void setCheckRightUList(String[] checkRightUList) {
		this.checkRightUList = checkRightUList;
	}
	public String[] getCheckRightDList() {
		return checkRightDList;
	}
	public void setCheckRightDList(String[] checkRightDList) {
		this.checkRightDList = checkRightDList;
	}
	public String[] getCheckMenuIdList() {
		return checkMenuIdList;
	}
	public void setCheckMenuIdList(String[] checkMenuIdList) {
		this.checkMenuIdList = checkMenuIdList;
	}
	public String getRightC() {
		return StringUtil.nvl(rightC);
	}
	public void setRightC(String rightC) {
		this.rightC = rightC;
	}
	public String getRightR() {
		return StringUtil.nvl(rightR);
	}
	public void setRightR(String rightR) {
		this.rightR = rightR;
	}
	public String getRightU() {
		return StringUtil.nvl(rightU);
	}
	public void setRightU(String rightU) {
		this.rightU = rightU;
	}
	public String getRightD() {
		return StringUtil.nvl(rightD);
	}
	public void setRightD(String rightD) {
		this.rightD = rightD;
	}
	public String getMngAccountGroupId() {
		return StringUtil.nvl(mngAccountGroupId);
	}
	public void setMngAccountGroupId(String mngAccountGroupId) {
		this.mngAccountGroupId = mngAccountGroupId;
	}
	


}
