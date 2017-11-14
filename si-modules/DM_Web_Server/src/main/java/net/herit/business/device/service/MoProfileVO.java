package net.herit.business.device.service;

import net.herit.business.device.service.*;

import java.io.Serializable;
import java.util.List;

import net.herit.common.util.StringUtil;



public class MoProfileVO implements Serializable{

	/** HDP_MO_PROFILE.ID */
	private String id;

	
	/** DEVICE_MODEL_ID */
	private String deviceModelId;
	/** DISPLAY_NAME */
	private String displayName;
	/** PROFILE_VER */
	private String profileVer;	
	/** MODEL_NAME */
	private String modelName;
	/** OUI */
	private String oui;
	/** RESOURCE_URI */
	private String resourceUri;
	/** DATA_TYPE */
	private String dataType;
	/** UNIT */
	private String unit;
	/** IS_HISTORICAL */
	private String isHistorical;
	/** IS_DISPLAY */
	private String isDisplay;
	/** DISPLAY_TYPE */
	private String displayType;
	/** NOTI_TYPE */
	private String notiType;
	/** OPERATION */
	private String operation;
	/** IS_DIAGNOSTIC */
	private String isDiagnostic;
	/** IS_MANDATORY */
	private String isMandatory;
	/** DEFAULT_VALUE */
	private String defaultValue;
	/** DESCRIPTION */
	private String description;
	/** CREATE_TIME */
	private String createTime;
	/** UPDATE_TIME */
	private String updateTime;
	private String data;
	
		
	
	/** IS_ERROR */
	private String isError;
	/** IS_MULTIPLE */
	private String isMultiple;
	/** HAS_OPTION_DATA */
	private String hasOptionData;
	
	/** ERROR_CODE list */
	private List<MoErrorCodeVO> errorCodeList;
	/** OPTION_DATA list */
	private List<MoOptionDataVO> optionDataList;


	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getIsMandatory() {
		return isMandatory;
	}
	public void setIsMandatory(String isMandatory) {
		this.isMandatory = isMandatory;
	}
	public void setResourceUri(String resourceUri) {
		this.resourceUri = resourceUri;
	}
	public String getId() {
		return StringUtil.nvl(id);
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getModelName() {
		return StringUtil.nvl(modelName);
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getOui() {
		return StringUtil.nvl(oui);
	}
	public void setOui(String oui) {
		this.oui = oui;
	}
	public String getResourceUri() {
		return StringUtil.nvl(resourceUri);
	}
	public String getDataType() {
		return StringUtil.nvl(dataType);
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getNotiType() {
		return StringUtil.nvl(notiType);
	}
	public void setNotiType(String notiType) {
		this.notiType = notiType;
	}
	public String getOperation() {
		return StringUtil.nvl(operation);
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getIsError() {
		return StringUtil.nvl(isError);
	}
	public void setIsError(String isError) {
		this.isError = isError;
	}
	public String getIsMultiple() {
		return StringUtil.nvl(isMultiple);
	}
	public void setIsMultiple(String isMultiple) {
		this.isMultiple = isMultiple;
	}
	public String getHasOptionData() {
		return StringUtil.nvl(hasOptionData);
	}
	public void setHasOptionData(String hasOptionData) {
		this.hasOptionData = hasOptionData;
	}
	public String getDefaultValue() {
		return StringUtil.nvl(defaultValue);
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getDescription() {
		return StringUtil.nvl(description);
	}
	public void setDescription(String description) {
		this.description = description;
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

	public List<MoErrorCodeVO> getErrorCodeList() {
		return errorCodeList;
	}
	public void setErrorCodeList(List<MoErrorCodeVO> errorCodeList) {
		this.errorCodeList = errorCodeList;
	}

	public List<MoOptionDataVO> getOptionDataList() {
		return optionDataList;
	}
	public void setOptionDataList(List<MoOptionDataVO> optionDataList) {
		this.optionDataList = optionDataList;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getIsDiagnostic() {
		return isDiagnostic;
	}
	public void setIsDiagnostic(String isDiagnostic) {
		this.isDiagnostic = isDiagnostic;
	}
	public String getIsHistorical() {
		return isHistorical;
	}
	public void setIsHistorical(String isHistorical) {
		this.isHistorical = isHistorical;
	}
	public String getDeviceModelId() {
		return deviceModelId;
	}
	public void setDeviceModelId(String deviceModelId) {
		this.deviceModelId = deviceModelId;
	}
	public String getProfileVer() {
		return profileVer;
	}
	public void setProfileVer(String profileVer) {
		this.profileVer = profileVer;
	}
	public String getIsDisplay() {
		return isDisplay;
	}
	public void setIsDisplay(String isDisplay) {
		this.isDisplay = isDisplay;
	}
	public String getDisplayType() {
		return displayType;
	}
	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}


}
