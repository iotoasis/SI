/*****************************************************************************
 * 프로그램명  : BizException.java
 * 설     명  : 처리가능한 예외 정의 클래스
 * 참고  사항  : 없음
 *****************************************************************************
 * Date       Author  Version Description
 * ---------- ------- ------- -----------------------------------------------
 * 2013.06.01  LSH    1.0     초기작성
 *****************************************************************************/

package net.herit.common.exception;

import net.herit.common.util.DateTimeUtil;





/**
 * 처리가능한 예외 정의한다  
 * 
 * @author tnc
 * @version 1.0
 */
public class BizException extends Exception {

	private String time = null;
	private String className = null;
	private String methodName = null;
	private int errCod = 0;
	private String errId = null;
	private String errMsg = null;
	private String pageURL = null;
	private Object paramObj = null;
	private String modelKey = null;
	private Object model = null;	
	private java.lang.Exception ex = null;
    
	/**
     * BusinessException
     */
    public BizException() {
        super();
        this.time = DateTimeUtil.getDateTimeByPattern("yyyy-MM-dd-HH:mm");   
    }
    /**
     * BusinessException 
     * @param errId 에러아이디
     */
    public BizException(String errId) {
    	super();
    	this.time = DateTimeUtil.getDateTimeByPattern("yyyy-MM-dd-HH:mm");    	
        this.errId = errId;
    }
    
    public BizException(String errId, String errMsg) {
    	super();
    	this.time = DateTimeUtil.getDateTimeByPattern("yyyy-MM-dd-HH:mm");    	
    	this.errId = errId;
    	this.errMsg = errMsg;
    }
    
    public BizException(String calssName, String methodName, int errCod, String errMsg) {
    	super();
    	this.time = DateTimeUtil.getDateTimeByPattern("yyyy-MM-dd-HH:mm");    	
    	this.className = calssName;
    	this.methodName = methodName;
    	this.errCod = errCod;
    	this.errMsg = errMsg;
    }
    
    /**
     * BusinessException
     * @param errId 에러아이디
     * @param e     예외
     */
    public BizException(String errId, java.lang.Exception e) {
    	super(e);
        this.time = DateTimeUtil.getDateTimeByPattern("yyyy-MM-dd-HH:mm");    	
        this.errId = errId;
        this.ex = e ;
    }
    
    /**
     * BusinessException 
     * @param className 클래스명
     * @param methodName 메소드명
     * @param errId 에러아이디   
     * @param pageURL 페이지 URL
     * @param paramObj 파라미터OBJ   
     */
    public BizException(String className, String methodName, 
    		String errId, String pageURL, Object paramObj) {
    	super();
        this.time = DateTimeUtil.getDateTimeByPattern("yyyy-MM-dd-HH:mm");
        this.className = className;
        this.methodName = methodName;
        this.errId = errId;
        this.pageURL = pageURL;
        this.paramObj = paramObj;
    }    
    
    /**
     * BusinessException 
     * @param className 클래스명
     * @param methodName 메소드명
     * @param errId 에러아이디   
     * @param pageURL 페이지 URL
     * @param modelKey 모델 검색키   
     * @param model 모델 
     */
    public BizException(String className, String methodName, 
    		String errId, String pageURL, String modelKey, Object model) {
    	super();
        this.time = DateTimeUtil.getDateTimeByPattern("yyyy-MM-dd-HH:mm");
        this.className = className;
        this.methodName = methodName;
        this.errId = errId;
        this.pageURL = pageURL;
        this.modelKey = modelKey;
        this.model = model;
    }    
    
    /**
     * BusinessException 
     * @param className 클래스명
     * @param methodName 메소드명
     * @param errId 에러아이디   
     * @param pageURL 페이지 URL
     * @param paramObj 파라미터OBJ   
     * @param e     예외
     */
    public BizException(String className, String methodName, 
    		String errId, String pageURL, Object paramObj, java.lang.Exception e) {
    	super(e);
    	this.time = DateTimeUtil.getDateTimeByPattern("yyyy-MM-dd-HH:mm");   
        this.className = className;
        this.methodName = methodName;
        this.errId = errId;
        this.pageURL = pageURL;
        this.paramObj = paramObj;
        this.ex = e ;
    }    
    
	/**
	 * 예외 취득
	 * 
	 * @return java.lang.Exception 예외
	 */
	public java.lang.Exception getException() {
		return ex;
	}	
	/**
	 * 에러코드 취득
	 * 
	 * @return int 에러코드
	 */
	public int getErrCod() {
		return errCod;
	}
	/**
	 * 에러아이디 취득
	 * 
	 * @return java.lang.String 에러아이디
	 */
	public java.lang.String getErrId() {
		return errId;
	}
	/**
	 * 에러메세지 취득
	 * 
	 * @return java.lang.String 에러메세지
	 */
	public java.lang.String getErrMsg() {
		return errMsg;
	}
	/**
	 * 에러 발생 시간 취득
	 * 
	 * @return java.lang.String 에러 발생 시간
	 */
	public java.lang.String getTime() {
		return time;
	}
	/**
	 * 클래스명 취득
	 * 
	 * @return java.lang.String 클래스명
	 */
	public java.lang.String getClassName() {
		return className;
	}
	/**
	 * 메소드명 취득
	 * 
	 * @return java.lang.String 메소드명
	 */
	public java.lang.String getMethodName() {
		return methodName;
	}
	
	/**
	 * 페이지 URL 취득
	 * 
	 * @return java.lang.String 페이지 URL
	 */
	public java.lang.String getPageURL() {
		return pageURL;
	}
	/**
	 * 파라미터OBJ  취득
	 * 
	 * @return Object 파라미터OBJ
	 */
	public Object getParmObj() {
		return paramObj;
	}	
	/**
	 * 모델검색키 취득
	 * 
	 * @return java.lang.String 페이지 URL
	 */
	public java.lang.String getModelKey() {
		return modelKey;
	}
	/**
	 * 파라미터OBJ  취득
	 * 
	 * @return Object 파라미터OBJ
	 */
	public Object getModel() {
		return model;
	}	
}