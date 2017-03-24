/*****************************************************************************
 * 프로그램명  : TableTag.java
 * 설     명  : 테이블태그라이브러리 설정
 * 참고  사항  : 없음
 *****************************************************************************
 * Date       Author  Version Description
 * ---------- ------- ------- -----------------------------------------------
 * 2013.09.11  LSH      1.0    초기작성
 *****************************************************************************/
package net.herit.common.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import net.herit.common.util.PagingUtil;


/**
 * 테이블태그라이브러리 설정
 * @author  herit
 * @version 1.0
 */
public class TableTag extends TagSupport {

	/** 페이지 처리를 할 LIST정보와 필요한 정보처리 객체 */
	private PagingUtil table = null;
	/** 페이지 이동처리를 수행할 Form태그 명칭 */
	private String form = "";
	/** 페이지 이동시 이동할 URL */
	private String url = "";
	/** 페이지 이동시 이동할 프레임 */
	private String target = "";
	/** 페이지이동스크립트메소드이름 */
	private String method = "";
	/** 전처리 메소드 */
	private String beforeMethod = "";
	/** Project Context Path */
	private String contextPath = "";

    /**
     * 시작 태그 처리
     * @return String TABLE이동태그문자열
     */
    @Override
	public int doStartTag() throws JspException {
    	
        JspWriter out = pageContext.getOut();
        
        try {
        	StringBuffer sbTable = new StringBuffer();

        	if ((table != null) && (table.getTotalPage() > 0)) {
            	sbTable.append("<ul class='pagination'>");

        		//현재 페이지가 1이면 previous 비활성화. 그렇지 않으면 활성화
        		if (table.getCurrPage() > 1) {
        			sbTable.append("<li class='paginate_button previous'><a href=\"javascript:");
                    if(!beforeMethod.equals("")){
                        sbTable.append(beforeMethod);
                        sbTable.append(";");
                    }
                    sbTable.append(method);
                    sbTable.append("('");
                    sbTable.append(table.getCurrPage() - 1);
                    sbTable.append("'");
                    sbTable.append(getParameter());
                    sbTable.append(");\">Previous</a></li>");
        		}
        		else {
        			sbTable.append("<li class='paginate_button previous disabled'><a href='#'>Previous</a></li>");
        		}


            	for (int i = table.getSBigIndex(); i <= table.getEBigIndex(); i++){
            		sbTable.append("<li class='paginate_button ");
            		if (table.getCurrPage() == i ) {
            			sbTable.append("active");
            		}
            		sbTable.append("'><a href=\"javascript:");
                	if(!beforeMethod.equals("")){
                		sbTable.append(beforeMethod);
                		sbTable.append(";");
                	}
                	sbTable.append(method);
                	sbTable.append("('");
                	sbTable.append(i);
                	sbTable.append("'");
                	sbTable.append(getParameter());
                	sbTable.append(")\">");
                	sbTable.append(i + "</a></li>");
            	}


            	if (table.getCurrPage() == table.getTotalPage()) {
        			sbTable.append("<li class='paginate_button next disabled'><a href='#'>Next</a></li>");
            	}
            	else {
        			sbTable.append("<li class='paginate_button next'><a href=\"javascript:");
                    if(!beforeMethod.equals("")){
                        sbTable.append(beforeMethod);
                        sbTable.append(";");
                    }
                    sbTable.append(method);
                    sbTable.append("('");
                    sbTable.append(table.getCurrPage() + 1);
                    sbTable.append("'");
                    sbTable.append(getParameter());
                    sbTable.append(");\">next</a></li>");
            	}
            	
            	sbTable.append("</ul>");
        	}


            out.print(sbTable.toString());

        } catch (IOException ex) {
        	throw new JspException(ex);
        }
        return SKIP_BODY;
    }

    /**
     * 스크립트 호출 메소드의 페이지번호 이외의 기타 파라메터를 구성한다
     * @return String 기타파라메터문자열
     */
    private String getParameter() {
    	StringBuffer sbTable = new StringBuffer();

    	if ("movePage1".equals(method)) {
    		sbTable.append(", ");
    		sbTable.append(form);
    		sbTable.append(", '");
    		sbTable.append(url);
    		sbTable.append("'");
    	}else if("movePage2".equals(method)){
    		sbTable.append(", ");
    		sbTable.append(form);
    		sbTable.append(", '");
    		sbTable.append(url);
    		sbTable.append("'");
    		sbTable.append(", '");
    		sbTable.append(target);
    		sbTable.append("'");
    	}
    	return sbTable.toString();
    }

    /**
     * 페이지처리객체프로퍼티 취득
     * @return String 페이지처리객체프로퍼티
     */
    public PagingUtil getTable() {
        return table;
    }
    /**
     * 페이지처리객체프로퍼티 설정
     * @param obj 페이지처리객체프로퍼티
     */
    public void setTable(PagingUtil obj) {
    	table = obj;
    }

    /**
     * 페이지이동Form이름프로퍼티 취득
     * @return String 페이지이동Form이름프로퍼티
     */
    public String getForm() {
        return form;
    }
    /**
     * 페이지이동Form이름프로퍼티 설정
     * @param str 페이지이동Form이름프로퍼티
     */
    public void setForm(String str) {
    	form = str;
    }

    /**
     * 페이지이동URL프로퍼티 취득
     * @return String 페이지이동URL프로퍼티
     */
    public String getUrl() {
        return url;
    }
    /**
     * 페이지이동URL프로퍼티 설정
     * @param str 페이지이동URL프로퍼티
     */
    public void setUrl(String str) {
    	url = str;
    }
    /**
     * 페이지이동프레임프로퍼티 취득
     * @return String 페이지이동프레임프로퍼티
     */
    public String getTarget() {
        return target;
    }
    /**
     * 페이지이동프레임프로퍼티 설정
     * @param str 페이지이동프레임프로퍼티
     */
    public void setTarget(String str) {
    	target = str;
    }
    /**
     * 호출할스크립트메소드프로퍼티 취득
     * @return String 호출할스크립트메소드프로퍼티
     */
    public String getMethod() {
        return method;
    }
    /**
     * 호출할스크립트메소드프로퍼티 설정
     * @param str 호출할스크립트메소드프로퍼티
     */
    public void setMethod(String str) {
        method = str;
    }
    /**
     * 전처리 메소드 취득
     * @return String 전처리 메소드
     */
    public String getBeforeMethod() {
        return beforeMethod;
    }
    /**
     * 전처리 메소드 설정
     * @param str 전처리 메소드
     */
    public void setBeforeMethod(String str) {
    	beforeMethod = str;
    }

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

}
