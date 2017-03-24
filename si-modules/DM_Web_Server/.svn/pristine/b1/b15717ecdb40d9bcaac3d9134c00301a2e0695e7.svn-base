/*****************************************************************************
 * 프로그램명  : StringUtil.java
 * 설     명  : 문자 관련 클래스 util class.
 * 참고  사항  : 없음
 *****************************************************************************
 * Date       Author  Version Description
 * ---------- ------- ------- -----------------------------------------------
 * 2008.09.23  LYS    1.0     초기작성
 *****************************************************************************/
package net.herit.common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;

import net.herit.common.exception.UserSysException;
import net.herit.common.model.ErrorVO;


/**
 * 문자관련 클래스 util class.
 * @author  tnc
 * @version 1.0
 */
public class StringUtil {
	/** 로그처리 객체 */
//    protected static Log logger = LogFactory.getLog(LangUtil.class);

	/**
	 * 예외를 HTML형식으로 출력한다
	 * @param ex 예외
	 * @return String 변환된 String
	 */
	public static String stackTraceToString(Throwable ex) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		PrintStream p = new PrintStream(b);
		ex.printStackTrace(p);
		p.close();
		String stackTrace = b.toString();
		try {
			b.close();
		} catch (IOException e) {
//			logger.error(MessageMng.getMessage("예외출력 처리에 사용된 스트림 관련 리소스를 해제처리에 예외가 발생했습니다."));
		}

		return convertHtmlBr(stackTrace);
	}

	/**
	 * 일반 스트링을 HTML표시 형식으로 변경한다
	 * @param comment 변환대상 문자열
	 * @return String 변환된 문장열
	 */
	public static String convertHtmlBr(String comment) {
		if (comment == null)
			return "";
		int length = comment.length();
		StringBuffer buffer = new StringBuffer();

		for (int i = 0; i < length; i++) {
			String tmp = comment.substring(i, i + 1);
			if ("\r".compareTo(tmp) == 0) {
				tmp = comment.substring(++i, i + 1);
				if ("\n".compareTo(tmp) == 0) {
					buffer.append("<br>\r");
				} else {
					buffer.append("\r");
				}
			}
			buffer.append(tmp);
		}

		return buffer.toString();
	}

	/**
	 * 일반 스트링을 FLEX내용표시 형식으로 변경한다(FLEX화면에서 \r,\n이 있을 경우 줄바꿈이 두번적용된다.)
	 * @param comment 변환대상 문자열
	 * @return String 변환된 문장열
	 */
	public static String convertFlexStr(String comment) {
		String result = "";
		if (comment == null)
			return result;
		int length = comment.length();
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < length; i++) {
			String tmp = comment.substring(i, i + 1);
			if ("\r".compareTo(tmp) == 0) {
				tmp = comment.substring(++i, i + 1);
				if ("\n".compareTo(tmp) == 0)
					buffer.append("");
				else
					buffer.append("");
			}
			buffer.append(tmp);
		}
		result = buffer.toString();
		return result;
	}


	/**
	 * 널인경우 공백으로 바꾸어준다
	 * @param  str 변경문자열
	 * @return String 변경된 문자열
	 */
	public static String nvl(String str) {
		return nvl(str, "");
	}

	/**
	 *  널일경우 일정한 형식으로 바꾼다
	 * @param str 변경전 문자열
	 * @param NVLString 변경할 문자열
	 * @return String 변경된 문자열
	 */
	public static String nvl(String str, String NVLString) {
		if( (str == null) || (str.trim().equals(""))
				|| (str.trim().equals("null")) ) {
			return NVLString;
		} else {
			return str;
		}
	}

	/**
	 * 널인경우 공백으로 바꾸어준다
	 * @param  str 변경문자열
	 * @return int 변경된 숫자
	 */
	public static int parseInt(String str) {
		return parseInt(str, 0);
	}

	/**
	 *  널일경우 일정한 형식으로 바꾼다
	 * @param str 변경전 문자열
	 * @param basic 변경할 문자열
	 * @return int 변경된 숫자
	 */
	public static int parseInt(String str, int basic) {
		if( (str == null) || (str.trim().equals(""))
				|| (str.trim().equals("null")) ) {
			return basic;
		} else {
			return Integer.parseInt(str);
		}
	}

    /**
     * 대상 문자열의 strOld문자열을 strNew로 변환해 준다.(게시판등의 내용에 사용)
     * @param target 대상문자열
     * @param strOld 변환될문자
     * @param strNew 변환할문자
     * @return sb 변환된 문자열
     */
    public static String convertString (String target,String strOld,String strNew) {
        StringBuffer sb = new StringBuffer();

        if ((target == null) || target.equals("")) {
            return "";
        }
        int idx = 0;

        while((idx = target.indexOf(strOld)) > -1){
            sb.append(target.substring(0,idx));
            sb.append(strNew);
            target = target.substring(idx+1);
        }
        sb.append(target);
        return sb.toString();
    }

    /**
     * 텍스트 데이터의 내용을 화면에 보여주기 위해서 적절히 변환한다
     * @param target 대상문자열
     * @return result 변환된 결과 문자열
     */
    public static String convertText(String target){
        String result = "";
        //공백을 &nbsp;로 변환
        result = convertString(target," ","&nbsp;");
        //개행문자를 <BR>로변환
        result = convertString(result,"\n","<BR>");

        return result;
    }

    /**
     * 텍스트 데이터의 내용을 textarea에 답변내용으로 보여주기 위해서 적절히 변환한다
     * @param target 대상문자열
     * @return result 변환된 결과 문자열
     */
    public static String convertAnswerText(String target){
        String result = "";
        //매 줄마다 >를 붙인다.
        result = convertString(target,"\n","\n > ");
        return result;
    }

    /**
     * 덧글의 내용을 스크립트로 넘겨주기 위해 특수문자 변환한다
     * @param target 대상문자열
     * @return result 변환된 결과 문자열
     */
    public static String convertLineText(String target){
        String result = "";
        //공백을 &nbsp;로 변환
        result = convertString(target,"\"","&quot;");
        //개행문자를 <BR>로변환
        result = convertString(result,"'","\\'");

        return result;
    }

    /**
     * 글을 HTML태그에 디폴트 표시할때 " 일경우 뒤의 문자가 짤리므로특수분자를 변경한다
     * @param target 대상문자열
     * @return result 변환된 결과 문자열
     */
    public static String convertTagDoubleQuot(String target){
        String result = "";
        //공백을 &nbsp;로 변환
        result = convertString(target,"\"","&quot;");

        return result;
    }

    /**
	 * 일반 스트링을 javascript의 인수에 적합하도록 변경한다(개행문자를 모두 없앤다)
	 * @param comment 변환대상 문자열
	 * @return String 변환된 문장열
	 */
	public static String convertJSArgs(String comment) {
		if (comment == null)
			return "";
		int length = comment.length();
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < length; i++) {
			String tmp = comment.substring(i, i + 1);
			if ("\r".compareTo(tmp) == 0) {
				buffer.append("\\r");
			}else if ("\n".compareTo(tmp) == 0){
				buffer.append("\\n");
			}else if ("'".compareTo(tmp) == 0){
				buffer.append("\\'");
			}else if ("\"".compareTo(tmp) == 0){
				buffer.append("\\\"");
			}else{
				buffer.append(tmp);
			}
		}
		return buffer.toString();
	}

    /**
     * 제목등을 일정길이만 보여주고 뒤는 (...으로 변환한다)
     * @param str 대상문자열
     * @param size 보여줄 최대길이
     * @param end 잘라내고 뒤게 붙일 문자열
     * @return sb 변환된 문자열
     */
    public static String toTruncate(String str, int size, String end){
        StringBuffer sb = new StringBuffer();
        if ((str == null) || str.equals("")){
            return "";
        }
        int tempSize = str.length();
        if(tempSize > size){
            sb.append(str.substring(0, size));
            sb.append(end);
        } else {
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * 제목등을 일정길이만 보여주고 뒤는 (...으로 변환한다)
     * @param str 대상문자열
     * @param size 보여줄 최대길이
     * @param end 잘라내고 뒤게 붙일 문자열
     * @return sb 변환된 문자열
     */
    public static String toFileTruncate(String str, int size, String end){
        StringBuffer sb = new StringBuffer();
        if ((str == null) || str.equals("")){
            return "";
        }
        String startStr = "";
        String endStr = "";
        if (str.indexOf(".") != -1) {
            Vector tempString = split(str, ".");
            startStr = (String)tempString.get(0);
            endStr = (String)tempString.get(1);
        } else {
        	startStr = str;
            endStr = "";
        }
        int tempSize = startStr.length();
        if(tempSize > size){
            sb.append(startStr.substring(0, size));
            sb.append(end);
        } else {
            sb.append(startStr);
        }
        sb.append(".");
        sb.append(endStr);
        return sb.toString();
    }

    /**
     * 유저정의 예외를 한곳에서 출력하기 위해 예외 객체에 담았던 에러 관련 정보를 일정한 형식으로 출력
     * @param ex 예외객체
     * @param message 메세지취득위한 객체
     * @return String 형식화된 문자열
     */
    public static String printUserSysMessage(UserSysException ex) {
    	StringBuffer sbMessage = new StringBuffer();
    	ErrorVO errorInfo = ex.getErrorVO();
    	sbMessage.append("[");
    	sbMessage.append(errorInfo.getClassName());
    	sbMessage.append(".");
    	sbMessage.append(errorInfo.getMethodName());
    	sbMessage.append("]");
    	sbMessage.append("(");
    	sbMessage.append(errorInfo.getErrorCode());
    	sbMessage.append(")");
    	sbMessage.append(errorInfo.getErrorMessage());

    	return sbMessage.toString();
    }

    /**
	 * 특정한 문자열을 다른 문자열로 치환
	 * @param target 작업대상문자열
	 * @param o 바뀔문자열
	 * @param n 바꿀문자열
	 * @return String 결과문자열
	 */
	public static String replace(String target, String o, String n){
        StringBuffer result = new StringBuffer();
        int idx = 0;
        if ((target == null) || target.equals("")){
            return "";
        }
        //문장이 끝나지 않았으면 재귀호출한다
        while ((idx=target.indexOf(o)) != -1) {
            result.append(target.substring(0,idx));
            result.append(n);
            target = target.substring(idx+o.length());
        }
        result.append(target);

        return result.toString();

    }

	/**
	 * 전화번호 형식 데이터를 순차적으로 취득
	 * @param target 변환목표 문자열
	 * @param iCheck 전화번호의 취득 자리수 (0,1,2)
	 * @return String 전화번호의 자리수
	 */
	public static Vector getTelToken(String target, String sep) {
		Vector vtTarget = new Vector();
		vtTarget.add("");
		vtTarget.add("");
		vtTarget.add("");
		if (target != null && !target.equals("")) {
			Vector vtTempTarget = split(target, sep);
			if (vtTempTarget.size() == 3 ) {
				vtTarget = vtTempTarget;
			}
		}
		return vtTarget;
	}

	/**
	 * 메일 형식 데이터를 순차적으로 취득
	 * @param target 변환목표 문자열
	 * @param iCheck 전화번호의 취득 자리수 (0,1)
	 * @return String 전화번호의 자리수
	 */
	public static Vector getMailToken(String target, String sep) {
		Vector vtTarget = new Vector();
		vtTarget.add("");
		vtTarget.add("");
		if (target != null && !target.equals("")) {
			Vector vtTempTarget = split(target, sep);
			if (vtTempTarget.size() == 2 ) {
				vtTarget = vtTempTarget;
			}
		}
		return vtTarget;
	}

	/**
	 * 문자열을 구분자에 의해 벡터로 리턴한다
	 * @param target 변환대상 문자열
	 * @param str 짤라낼 구분자
	 * @return Vector 결과 벡터
	 */
	public static Vector split(String target,String str){
        StringTokenizer token = new StringTokenizer(target,str,true); //읽을라인을 ","로 항목별로 토큰을 만든다

        Vector vtCsvTemp = new Vector();          // 토큰을 분리해낼 벡터

        //널이거나 문자가 없을경우는 size가 0인 벡터를 넘긴다
        if ((target == null) || (target.equals(""))){
            return vtCsvTemp ;
        }

        //토큰이 하나도 없을 경우는 해당문자열을 담은 size가 1인 벡터를 넘긴다
        if (token.hasMoreTokens()==false){
            vtCsvTemp.addElement(target);
            return vtCsvTemp;
        }

        //첫번째토큰값을 구해서 값이있는지 ","인지를 체크한다
        String oldToken = token.nextToken().trim();
        // 첫번째 토큰이","일경우 공백을 넣어준다
        if (oldToken.equals(str)){
            vtCsvTemp.addElement("");
            // 첫번째 토큰이 값이 있을경우 값을 넣어준다
        }else{
            vtCsvTemp.addElement(oldToken);
        }

        //모든토킅에 위의 처리를 반복한다
        while(token.hasMoreTokens()){
            String tempToken = token.nextToken().trim();
            // 현재토큰이 ","이면 앞의 값을 체크해서 앞의값이","이아니면 처리하지않고 앞의 값이 ","이면 널이므로 공백처리
            if (tempToken.equals(str)){
                //앞의 값도 ","이면 그값은 널이다
                if (oldToken.equals(str)){
                    vtCsvTemp.addElement("");
                }
                // 값이 있는경우는 값을 넣는다
            }else{
                vtCsvTemp.addElement(tempToken);
            }
            oldToken = tempToken;  // 이전데이터 비교를 위해 설정
        }
        return vtCsvTemp;  //구분된 항목의 벡터를 리턴
    }

	/**
	 * 길이가 12자리 미만의 경우 공백으로 데이터를 체운
	 * @param str 대상데이터
	 * @return String 12자리가 체워진 데이
	 */
	public static String rightPadding (String str) {
		StringBuffer sbData = new StringBuffer();
		int length = nvl(str).length();
		if (length >= 12) {
			sbData.append(str.substring(0, 12));
		} else {
			sbData.append(str);
		    for (int i = length ; i < 12 ; i++) {
		    	sbData.append(" ");
		    }
		}
		return sbData.toString();
	}

	/**
	 * DB처리에 사용할 공통 IN파라메터를 설정한다
	 * @param userInfo 유저정보
	 * @return String 결과문자열
	 */
//	public static String getCommonPrefix(User userInfo) {
//		StringBuffer sbPrefix = new StringBuffer();
//		//언어설정(KOR | ENG | JPN)
//		sbPrefix.append("LANG=");
//		sbPrefix.append(ConfigMng.getValue(IPropertyKey.LANGUAGE));
//		//로그인아이디설정
//		sbPrefix.append(ConfigMng.getValue(IPropertyKey.PROC_DATA_SEP));
//		sbPrefix.append("LOGINID=");
//		sbPrefix.append(userInfo.getId());
//
//		return sbPrefix.toString();
//	}

	/**
	 * DB처리에 사용할 공통 IN파라메터를 설정한다
	 * @param userInfo 유저정보
	 * @return String 결과문자열
	 */
//	public static String getCommonPrefix() {
//		StringBuffer sbPrefix = new StringBuffer();
//		//언어설정(KOR | ENG | JPN)
//		sbPrefix.append("LANG=");
//		sbPrefix.append(ConfigMng.getValue(IPropertyKey.LANGUAGE));
//
//		return sbPrefix.toString();
//	}

	/**
	 * 메뉴에서 레벨당 들여쓰기를 설정한다
	 * @param level 메뉴레벨
	 * @return String 결과문자열
	 */
	public static String printMenuTitle(String level) {
		StringBuffer sbLevel = new StringBuffer();
		int nlevel = 0;
		if ((level != null) && !"".equals(level)) {
			nlevel = Integer.parseInt(level);
		}
		for (int i = 1; i < nlevel; i++) {
			sbLevel.append("&nbsp;&nbsp;&nbsp;&nbsp;");
		}

		return sbLevel.toString();
	}

	/** 이미지 경로를 설정한다 */
//	private final static String PATH_IMAGE = ConfigMng.getValue(IPropertyKey.PATH_IMAGE);

	/**
	 * 메뉴에서 레벨당 들여쓰기를 설정한다
	 * @param level 메뉴레벨
	 * @return String 결과문자열
	 */
	public static String printMenuTitle1(String level) {
		StringBuffer sbLevel = new StringBuffer();
		level = level.substring(0,1);
		int nlevel = 0;
		if ((level != null) && !"".equals(level)) {
			nlevel = Integer.parseInt(level);
		}
		for (int i = 0; i < nlevel; i++) {
			sbLevel.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		}
		/*
		 * 하위 접는 기능 연구중....2011/11/15   ...   Once
		 *
			sbLevel.append("<img src=\"");
			sbLevel.append("/images/dot.gif");
			sbLevel.append("\"");
			sbLevel.append(" id=\"");
			sbLevel.append("target"+nlevel+"");
			sbLevel.append("\" align=\"absmiddle\"");
			sbLevel.append(" onclick=\"hideshow("+nlevel+");\"");
			sbLevel.append(">");
		 */
		return sbLevel.toString();
	}

	/**
	 * 메뉴에서 레벨당 들여쓰기를 설정한다
	 * @param level 메뉴레벨
	 * @return String 결과문자열
	 */
	public static String printMenuTitleTab(String level) {
		StringBuffer sbLevel = new StringBuffer();
		int nlevel = 0;
		if ((level != null) && !"".equals(level)) {
			nlevel = Integer.parseInt(level);
		}
		for (int i = 1; i < nlevel; i++) {
			sbLevel.append("&tab;&tab;&tab;");
		}

		return sbLevel.toString();
	}


	/**
	 * 숫자를 세자리마다 콤마를 찍는다.
	 * @param level 메뉴레벨
	 * @return String 결과문자열
	 */
	public static String toComma(String inValue) {

		if(inValue!=null && inValue.length()>0){
			int idx = inValue.indexOf("."); //소수점 처리
			String str = inValue;
			String rightVal = "";
			if(idx>0){
				str = inValue.substring(0,idx);
				rightVal = inValue.substring(idx+1, inValue.length());
				//소수점자리 이하가 0이면 소수점 아래를 없앤다.
				if(Integer.valueOf(rightVal)>0){
					rightVal = inValue.substring(idx, inValue.length());
				}else{
					rightVal = "";
				}
			}
			str = str + "";
			str = replace(str, ",", "");
			int tmp1, tmp2, tmp3;
			String statValue="", strValue="", modValue="";
			if(str.substring(0,1).equals("-")){
				statValue = str.substring(1,str.length());
			} else {
				statValue = str;
			}
			tmp1 = statValue.length();

			if(tmp1 > 3){
				tmp2 = tmp1 / 3;
				tmp3 = tmp1 % 3;

				if(tmp3>0){
					strValue += statValue.substring(0,tmp3) + ",";
					modValue = statValue.substring(tmp3);
				} else {
					modValue = statValue;
				}

				for ( int i=0; i < tmp2 ; i++ ){
					if(i==(tmp2-1)){
						strValue += modValue.substring(i*3,i*3+3);
					} else {
						strValue += modValue.substring(i*3,i*3+3) + ",";
					}
				}
			} else {
				strValue = statValue;
			}

			String srtValue = "";
			if(str.substring(0,1).equals("-")){
				srtValue = "-"+strValue;
			}else{
				srtValue = strValue;
			}

			return srtValue+rightVal;
		}else{
			return "";
		}
	}
	public static String toComma(double inValue) {
		return toComma(Double.toString(inValue));
	}
	public static String toComma(int inValue) {
		return toComma(Integer.toString(inValue));
	}


	public static String arrayToString(String[] tmp){
		StringBuffer buf = new StringBuffer();
		if(tmp!=null && tmp.length>0){
			for(int i=0;i<tmp.length;i++){
				buf.append(tmp[i]);
				buf.append(";");
			}
		}
		return buf.toString();
	}

    /**
     * 줄바꿈 처리 (파이어폭스 버그 대응)
     * @param sTarget
     * @param iChkByte
     * @return sRetStr String
     */
    public static String chkString(String sTarget, int iChkByte) {
        StringBuffer sRet = new StringBuffer();
        String sRetStr = "";
        String sTemp = "";
        int iLen = 0;
        int j = 0;
        int iSumByte = 0;

        if ((sTarget == null) || (sTarget.equals(""))) {
            return sRetStr;
        } else {
            iLen = sTarget.length();
        }
        for (int i = 0; i < iLen; i++) {
            j = i + 1;
            sTemp = sTarget.substring(i, j);

              try {
                  iSumByte = iSumByte + sTemp.getBytes("UTF-8").length;
              } catch (java.io.UnsupportedEncodingException enc) {

              }

            if (iSumByte >= iChkByte) {
                sRet.append(sTarget.substring(i, j)).append("​&#x200B;");
                iSumByte = 0;
            } else {
                sRet.append(sTarget.substring(i, j));
            }
        }
        sRetStr = sRet.toString();
        return sRetStr;
    }

	public static String round(double target) {
		String pattern = "0.##";
		DecimalFormat df = new DecimalFormat(pattern);

        return df.format(target);

	}

	/**
	 * 널인경우 공백으로 바꾸어준다
	 * @param  str 변경문자열
	 * @return int 변경된 숫자
	 */
	public static double parseDouble(String str) {
		return parseDouble(str, 0);
	}

	/**
	 *  널일경우 일정한 형식으로 바꾼다
	 * @param str 변경전 문자열
	 * @param basic 변경할 문자열
	 * @return int 변경된 숫자
	 */
	public static double parseDouble(String str, int basic) {
		try {
			if( (str == null) || (str.trim().equals(""))
					|| (str.trim().equals("null")) ) {
				return basic;
			} else {
				return Double.parseDouble(str);
			}
		}catch (Exception ex){
			return basic;
		}
	}

	/**
     * 글을 HTML태그에 디폴트 표시할때 " 일경우 뒤의 문자가 짤리므로특수분자를 변경한다
     * @param target 대상문자열
     * @return result 변환된 결과 문자열
     */
    public static String convertTag(String target){
        String result = "";
        //공백을 &nbsp;로 변환<
        result = convertString(target,"<","&lt;");
        result = convertString(result,">","&gt;");
        return result;
    }

    public static String[] lastString(String target) {
//		String[] divide = {"", "", "", "", ""};
		String[] divide = {"", "", "", ""};
		int idx = target.lastIndexOf(System.getProperty("file.separator"));
		String temp = "";
		String temp1 = "";
//		String temp2 = "";
		if (idx !=  -1){
			temp = target.substring(0, idx);
			divide[0] = target.substring(idx+1);
			int idx1 = temp.lastIndexOf(System.getProperty("file.separator"));
			if (idx1 !=  -1){
				temp1 = temp.substring(0, idx1);
				divide[1] = temp.substring(idx1+1);
				int idx2 = temp1.lastIndexOf(System.getProperty("file.separator"));
				if (idx2 !=  -1){
					divide[3] = temp1.substring(0, idx2);
					divide[2] = temp1.substring(idx2+1);

//					temp2 = temp1.substring(0, idx2);
//					divide[2] = temp1.substring(idx2+1);
//					int idx3 = temp2.lastIndexOf(System.getProperty("file.separator"));
//					if (idx3 !=  -1){
//						divide[4] = temp2.substring(0, idx3);
//						divide[3] = temp2.substring(idx3+1);
//
//					}
				}
			}
		}

		return divide;
	}

    /**
     * 문자열을 뒤에서부터 검색해 해당포맷(pattern)이 있으면 해당포맷뒤의 문자열을 삭제한다.
     * @param target 입력문자열
     * @param pattern (-,/)구분형식
     * @return String 취득한문자열
     */
	public static String lastString(String target, String pattern) {

		StringBuffer sbuf = new StringBuffer();

		int idx = target.lastIndexOf(pattern);

		if (idx !=  -1){
			sbuf.append(target.substring(0, idx));
		}else {
			sbuf.append(target);
		}

		return sbuf.toString();
	}

	/**
	 * 0.xxx의 값에 0을 붙인다.
	 * @param target
	 * @return
	 */
	public static String getZeroDotString(String target) {
		String result = "";
		if (!"".equals(StringUtil.nvl(target))) {
			if (".".equals(target.substring(0,1))) {
				StringBuffer sbBuffer = new StringBuffer();
				sbBuffer.append("0");
				sbBuffer.append(target);
				result = sbBuffer.toString();
			} else {
				result = target;
			}
		}

		return result;

	}

	/**
	 * 전화변호 형식의 문자를 만든다. 일반 TEST를 전화번호 형식처럼 "-"이 붙여진 형식으로 바꾼다.
	 * @param str 일반 숫자형 TEXT
	 * @return 전화번호 형식 str
	 */
	public static String toTel(String str){
		/*
		if(str == null || str.equals("")){
			return "";
		}
		str = str.replaceAll("-", "");
		try{
			String szTel1 ="";
			String szTel2 ="";
			String szTel3 ="";

			int len = str.length();

			if(str.substring(0,2).equals("02")){
				szTel1 = str.substring(0,2);
				szTel2 = str.substring(2,len-4);
				szTel3 = str.substring(len-4,len);
			}else{
				szTel1 = str.substring(0,3);
				szTel2 = str.substring(3,len-4);
				szTel3 = str.substring(len-4,len);

			}
			str = szTel1 + "-" + szTel2 + "-" + szTel3;
		}catch(Exception e){

		}

		 */
		return str;
	}

	/**
	 * 주민등록번호 형식의 문자를 만든다. 일반 TEST를 주민등록번호 형식처럼 "-"이 붙여진 형식으로 바꾼다.
	 * @param str 일반 숫자형 TEXT
	 * @return 전화번호 형식 str
	 */
	public static String toSsno(String str){
		if(str == null || str.equals("")){
			return "";
		}
		str = str.replaceAll("-", "");
		try{
			String szSsno1 ="";
			String szSsno2 ="";

			int len = str.length();

			szSsno1 = str.substring(0,6);
			szSsno2 = str.substring(6,len);

			str = szSsno1 + "-" + szSsno2;
		}catch(Exception e){

		}
		return str;
	}



	/**
	 * 사업자 등록번호 형식의 문자를 만든다. 일반 TEST를 전화번호 형식처럼 "-"이 붙여진 형식으로 바꾼다.
	 * @param str 일반 숫자형 TEXT
	 * @return 전화번호 형식 str
	 */
	public static String toVisNum(String str){
		if(str == null || str.equals("")){
			return "";
		}
		str = str.replaceAll("-", "");
		try{
			String szTel1 ="";
			String szTel2 ="";
			String szTel3 ="";

			int len = str.length();

				szTel1 = str.substring(0,3);
				szTel2 = str.substring(3,len-5);
				szTel3 = str.substring(len-5,len);

			str = szTel1 + "-" + szTel2 + "-" + szTel3;
		}catch(Exception e){

		}

		return str;
	}

	/**
	 * 카드번호 형식의 문자를 만든다. 일반 TEXT를 카드번호 형식처럼 "-"이 붙여진 형식으로 바꾼다.
	 * @param str 일반 숫자형 TEXT
	 * @return 전화번호 형식 str
	 */
	public static String toCardNum(String str){
		if(str == null || str.equals("")){
			return "";
		}
		str = str.replaceAll("-", "");
		try{
			String szTel1 ="";
			String szTel2 ="";
			String szTel3 ="";
			String szTel4 ="";

			int len = str.length();

				szTel1 = str.substring(0,4);
				szTel2 = str.substring(4,8);
				szTel3 = str.substring(8,12);
				szTel3 = str.substring(12,16);

			str = szTel1 + "-" + szTel2 + "-" + szTel3 + "-" + szTel4;
		}catch(Exception e){

		}

		return str;
	}


	/**
	 * 모든 문자열의 "-"을 땐다.
	 * @param str 일반 TEXT
	 * @return 전화번호 형식 str
	 */
	public static String toMinerhifun(String str){
		if(str == null || str.equals("")){
			return "";
		}
		str = str.replaceAll("-", "");

		return str;
	}



	/**
	 * substring() 메소드 기능
	 * @param str
	 * @param start
	 * @param end
	 * @return
	 */
	public static String substring(String str, int start, int end){
		if(str == null || str.equals("")){
			return "";
		}

		str = str.substring(start, end);

		return str;
	}



    /**
     * 응용어플리케이션에서 고유값을 사용하기 위해 시스템에서17자리의TIMESTAMP값을 구하는 기능
     *
     * @param
     * @return Timestamp 값
     * @exception MyException
     * @see
     */
    public static String getTimeStamp() {
		String rtnStr = null;

		// 문자열로 변환하기 위한 패턴 설정(년도-월-일 시:분:초:초(자정이후 초))
		String pattern = "yyyyMMddhhmmssSSS";

		try {
		    SimpleDateFormat sdfCurrent = new SimpleDateFormat(pattern, Locale.KOREA);
		    Timestamp ts = new Timestamp(System.currentTimeMillis());

		    rtnStr = sdfCurrent.format(ts.getTime());
		} catch (Exception e) {
		    //e.printStackTrace();
		    throw new RuntimeException(e);	// 2011.10.10 보안점검 후속조치
		}

		return rtnStr;
    }

    /**
     * 문자열 A에서 Z사이의 랜덤 문자열을 구하는 기능을 제공 시작문자열과 종료문자열 사이의 랜덤 문자열을 구하는 기능
     *
     * @param startChr
     *            - 첫 문자
     * @param endChr
     *            - 마지막문자
     * @return 랜덤문자
     * @exception MyException
     * @see
     */
    public static String getRandomStr(char startChr, char endChr) {

		int randomInt;
		String randomStr = null;

		// 시작문자 및 종료문자를 아스키숫자로 변환한다.
		int startInt = Integer.valueOf(startChr);
		int endInt = Integer.valueOf(endChr);

		// 시작문자열이 종료문자열보가 클경우
		if (startInt > endInt) {
		    throw new IllegalArgumentException("Start String: " + startChr + " End String: " + endChr);
		}

		try {
		    // 랜덤 객체 생성
		    SecureRandom rnd = new SecureRandom();

		    do {
			// 시작문자 및 종료문자 중에서 랜덤 숫자를 발생시킨다.
			randomInt = rnd.nextInt(endInt + 1);
		    } while (randomInt < startInt); // 입력받은 문자 'A'(65)보다 작으면 다시 랜덤 숫자 발생.

		    // 랜덤 숫자를 문자로 변환 후 스트링으로 다시 변환
		    randomStr = (char)randomInt + "";
		} catch (Exception e) {
		    //e.printStackTrace();
		    throw new RuntimeException(e);	// 2011.10.10 보안점검 후속조치
		}

		// 랜덤문자열를 리턴
		return randomStr;
    }


}


