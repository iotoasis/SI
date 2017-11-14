package net.herit.business.api.service;

import java.io.Serializable;

import net.herit.common.util.StringUtil;

public class FileUploadVO implements Serializable {

	/**게시물 아이디*/
	private String boardId = "";
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
	
	
	public String getVersion() {
		return StringUtil.nvl(version);
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getFileUrl() {
		return StringUtil.nvl(fileUrl);
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	public String getCheckSum() {
		return StringUtil.nvl(checkSum);
	}
	public void setCheckSum(String checkSum) {
		this.checkSum = checkSum;
	}
	public String getBoardId() {
		return StringUtil.nvl(boardId);
	}
	public void setBoardId(String boardId) {
		this.boardId = boardId;
	}
	public String getFileRealName() {
		return StringUtil.nvl(fileRealName);
	}
	public void setFileRealName(String fileRealName) {
		this.fileRealName = fileRealName;
	}
	public String getFilePhysName() {
		return StringUtil.nvl(filePhysName);
	}
	public void setFilePhysName(String filePhysName) {
		this.filePhysName = filePhysName;
	}
	public String getFilePath() {
		return StringUtil.nvl(filePath);
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileSize() {
		return StringUtil.nvl(fileSize);
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	
}
