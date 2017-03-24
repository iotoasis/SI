/*****************************************************************************
 * 프로그램명  : UserSysException.java
 * 설     명  : System Exception 
 * 참고  사항  : 없음
 *****************************************************************************
 * Date       Author  Version Description
 * ---------- ------- ------- -----------------------------------------------
 * 2013.06.01  LSH    1.0     초기작성
 *****************************************************************************/

package net.herit.common.exception;

import net.herit.common.model.ErrorVO;
import net.herit.common.util.DateTimeUtil;


/**
 * 시스템 예외
 * 
 * @author tnc
 * @version 1.0
 */
public class UserSysException extends Exception {	
	/** 에러정보 객체  */
	private ErrorVO errorInfo =  new ErrorVO();
	
    /**
     * UserSysException 
     * @param className 클래스명
     * @param methodName 메소드명
     * @param errId 에러아이디   
     */
    public UserSysException(String className, String methodName, String errorCode) {
    	super();
    	errorInfo.setClassName(className);
    	errorInfo.setMethodName(methodName);
    	errorInfo.setErrorCode(errorCode);
//    	errorInfo.setErrorMessage(MessageMng.getMessage(errorCode));
    	errorInfo.setDateTime(DateTimeUtil.getDateTime());
    } 
    
    /**
     * UserSysException 
     * @param className 클래스명
     * @param methodName 메소드명
     * @param errId 에러아이디   
     * @param e 예외 
     */
    public UserSysException(String className, String methodName, 
    		String errorCode, Exception originException) {
    	super(originException);
    	errorInfo.setClassName(className);
    	errorInfo.setMethodName(methodName);
    	errorInfo.setErrorCode(errorCode);
//    	errorInfo.setErrorMessage(MessageMng.getMessage(errorCode));
    	errorInfo.setDateTime(DateTimeUtil.getDateTime());
    	errorInfo.setOriginException(originException);
    } 
        
    /**
     * UserSysException 
     * @param className 클래스명
     * @param methodName 메소드명
     * @param errId 에러아이디   
     * @param paramObj 파라미터OBJ   
     * @param e     예외
     */
    public UserSysException(String className, String methodName, 
    		String errorCode, String errorMsg, Exception originException) {
    	super(originException);
    	errorInfo.setClassName(className);
    	errorInfo.setMethodName(methodName);
    	errorInfo.setErrorCode(errorCode);
    	errorInfo.setErrorMessage(errorMsg);
    	errorInfo.setDateTime(DateTimeUtil.getDateTime());
    	errorInfo.setOriginException(originException);    	
    }    
    
    /**
     * UserSysException 
     * @param className 클래스명
     * @param methodName 메소드명
     * @param errId 에러아이디   
     * @param paramObj 파라미터OBJ   
     * @param e     예외
     */
    public UserSysException(String className, String methodName, 
    		String errorCode, Object paramObj, Exception originException) {
    	super(originException);
    	errorInfo.setClassName(className);
    	errorInfo.setMethodName(methodName);
    	errorInfo.setErrorCode(errorCode);
//    	errorInfo.setErrorMessage(MessageMng.getMessage(errorCode));
    	errorInfo.setDateTime(DateTimeUtil.getDateTime());
    	errorInfo.setParamObj(paramObj);
    	errorInfo.setOriginException(originException);    	
    }    
    
    /**
	 * 에러정보객체 취득
	 * @return ErrorVO 에러정보객체
	 */
	public ErrorVO getErrorVO() {
		return errorInfo;
	}
	
	/**
	 * 에러정보객체 취득
	 * @param errorInfo 에러정보객체
	 */
	public void setErrorVO(ErrorVO errorInfo) {
		this.errorInfo = errorInfo;
	}	
}