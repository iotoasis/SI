package net.herit.business.firmware.service;

import java.io.Serializable;

public class FirmwareVO implements Serializable{
	/** SN */
	private String sn;
	/** DEVICE_MODEL */
	private String deviceModel;
	/** MODEL_NAME */
	private String modelName;
	/** OUI */
	private String oui;
	/** PACKAGE */
	private String packageName;
	
	/**버전*/
	private String version = "";
	/**파일Url*/
	private String fileUrl = "";
	/**CHECKSUM*/
	private String checkSum = "";
	/**파일명*/
	private String fileRealName = "";
	/**물리 파일명*/
	private String filePhysName = "";
	/**파일경로*/
	private String filePath = "";
	/**파일크기(bytes)*/
	private String fileSize = "";
	
	
	
	/** 필드명 */
	private String fieldName = "";
    /** 파일명 */
    private String fileName = "";
    /** ContextType */
    private String contentType = "";
    /** 하위 디렉토리 지정 */
    private String serverSubPath = "";
    /** 물리적 파일명 */
    private String physicalName = "";
    /** 파일 사이즈 */
    private long size = 0L;
	
	
	
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getDeviceModel() {
		return deviceModel;
	}
	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getOui() {
		return oui;
	}
	public void setOui(String oui) {
		this.oui = oui;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	public String getCheckSum() {
		return checkSum;
	}
	public void setCheckSum(String checkSum) {
		this.checkSum = checkSum;
	}
	public String getFileRealName() {
		return fileRealName;
	}
	public void setFileRealName(String fileRealName) {
		this.fileRealName = fileRealName;
	}
	public String getFilePhysName() {
		return filePhysName;
	}
	public void setFilePhysName(String filePhysName) {
		this.filePhysName = filePhysName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getServerSubPath() {
		return serverSubPath;
	}
	public void setServerSubPath(String serverSubPath) {
		this.serverSubPath = serverSubPath;
	}
	public String getPhysicalName() {
		return physicalName;
	}
	public void setPhysicalName(String physicalName) {
		this.physicalName = physicalName;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	
	
	
	
}
