/*****************************************************************************
 * 프로그램명  : DateTimeUtil.java
 * 설     명  : ȸ��공통부품/Utillity
 * 참고  사항  : 없음
 *****************************************************************************
 * Date       Author  Version Description
 * ---------- ------- ------- -----------------------------------------------
 * 2012.09.11    LSH    1.0     초기작성
 *****************************************************************************/
package net.herit.common.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * 날짜관련처리클래스.
 * @author  tnc
 * @version 1.0
 */
public class DateTimeUtil {

	/**
	 * 날짜를 파리미터로 넘겨준 형식대로 취득한다。
	 * @param pattern (YYMMDD날짜형식)
	 * @return String 취득한형식화된 날짜
	 */
    public static String getDateTimeByPattern(String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                pattern, Locale.KOREAN);
        String dateString = formatter.format(new Date());

        return dateString;
    }

    /**
     * 날짜를 년월일（８자리）만취득하고구분형식으로 표시한다
     * @param dateTime 8자리이상날짜
     * @param pattern (-,/)구분형식
     * @return String 취득한날짜
     */
    public static String getDateFormat(String dateTime, String pattern){
    	StringTokenizer stringtokenizer = new StringTokenizer(dateTime, pattern);
    	if(stringtokenizer.countTokens()>1){
    		return dateTime.toString();
    	}
    	StringBuffer sbDate = new StringBuffer("");
		if ( dateTime != null && dateTime.length() >= 8){
			sbDate.append(dateTime.substring(0, 4));
			sbDate.append(pattern);
			sbDate.append(dateTime.substring(4, 6));
			sbDate.append(pattern);
			sbDate.append(dateTime.substring(6, 8));
		}
		return sbDate.toString();
    }

    /**
     * 날짜를 년월（6자리）만취득하고구분형식으로 표시한다
     * @param dateTime 6자리이상날짜
     * @param pattern (-,/)구분형식
     * @return String 취득한날짜
     */
    public static String getDateFormatYM(String dateTime, String pattern){
    	StringBuffer sbDate = new StringBuffer("");
		if ( dateTime != null && dateTime.length() >= 6){
			sbDate.append(dateTime.substring(0, 4));
			sbDate.append(pattern);
			sbDate.append(dateTime.substring(4, 6));
		}
		return sbDate.toString();
    }

    /**
     * 날짜를 년월（6자리）만취득하고구분형식으로 표시한다
     * @param dateTime 6자리이상날짜
     * @param pattern (-,/)구분형식
     * @return String 취득한날짜
     */
    public static String getDateFormatYM1(String dateTime, String pattern){
    	StringBuffer sbDate = new StringBuffer("");
		if ( dateTime != null && dateTime.length() >= 6){
			sbDate.append(dateTime.substring(0, 4));
			sbDate.append(pattern);
			sbDate.append(dateTime.substring(4, 7));
		}
		return sbDate.toString();
    }

    /**
     * 날짜를 년월（4자리）만취득하고구분형식으로 표시한다
     * @param dateTime 6자리이상날짜
     * @param pattern (-,/)구분형식
     * @return String 취득한날짜
     */
    public static String getDateFormatYear(String dateTime){
    	StringBuffer sbDate = new StringBuffer("");
		if ( dateTime != null && dateTime.length() >= 4){
			sbDate.append(dateTime.substring(0, 4));
		}
		return sbDate.toString();
    }

    /**
     * 날짜를 년월（4자리）만취득하고구분형식으로 표시한다
     * @param dateTime 6자리이상날짜
     * @param pattern (-,/)구분형식
     * @return String 취득한날짜
     */
    public static String getDateFormatMonth(String dateTime){
    	StringBuffer sbDate = new StringBuffer("");
		if ( dateTime != null && dateTime.length() >= 7){
			sbDate.append(dateTime.substring(5, 7));
		}
		return sbDate.toString();
    }

    /**
     * 형식화한날짜를 년월일(8자리)만취득한다
     * @param  data (YYYY+[]+MM+[]+DD)
     * @return sb.toString() (YYYYMMDD)
     */
    public static String getDeleteFormatYMD(String data) {
        StringBuffer sb = new StringBuffer();
        if (data == null || data.equals("")){
            return "";
        }
        sb.append(data.substring(0,4));
        sb.append(data.substring(5,7));
        sb.append(data.substring(8));

        return sb.toString();
    }

    /**
     * 시간을 시/분(4자리)만취득하여 구분형식으로 취득한다
     * @param dateTime 4자리시간데이터
     * @param pattern (：)구분형식
     * @return String 취득한시간데이터
     */
    public static String getTimeFormat(String dateTime, String pattern){
    	StringBuffer sbDate = new StringBuffer("");
		if ( dateTime != null && dateTime.length() >= 4){
			sbDate.append(dateTime.substring(0, 2));
			sbDate.append(pattern);
			sbDate.append(dateTime.substring(2, 4));
		}
        if ( dateTime != null && dateTime.length() == 6){
        	sbDate.append(pattern);
			sbDate.append(dateTime.substring(4, 6));
		}
		return sbDate.toString();
    }

    /**
     * 날짜＋시간데이터１２자리를날짜시간별로 정형화해서 보여준다
     * @param dateTime 12행날짜+시간데이터
     * @param pattern (-,/)날짜구분형식
     * @return String 취득결과데이터
     */
    public static String getDateTime(String dateTime, String pattern){
    	StringBuffer sbDate = new StringBuffer("");
		if ( dateTime != null && dateTime.length() >= 12){
			sbDate.append(dateTime.substring(2, 4));
			sbDate.append(pattern);
			sbDate.append(dateTime.substring(4, 6));
			sbDate.append(pattern);
			sbDate.append(dateTime.substring(6, 8));
			sbDate.append(" ");
			sbDate.append(dateTime.substring(8, 10));
			sbDate.append(":");
			sbDate.append(dateTime.substring(10, 12));
		}
		if ( dateTime != null && dateTime.length() == 14){
			sbDate.append(":");
			sbDate.append(dateTime.substring(12, 14));
		}
		return sbDate.toString();
    }

    /**
     * 날짜＋시간데이터１0자리를날짜시간별로 정형화해서 보여준다
     * @param dateTime 10행날짜+시간데이터
     * @param pattern (-,/)날짜구분형식
     * @return String 취득결과데이터
     */
    public static String getDateTime2(String dateTime, String pattern){
    	StringBuffer sbDate = new StringBuffer("");
		if ( dateTime != null && dateTime.length() >= 10){
			sbDate.append(dateTime.substring(0, 4));
			sbDate.append(pattern);
			sbDate.append(dateTime.substring(4, 6));
			sbDate.append(pattern);
			sbDate.append(dateTime.substring(6, 8));
			sbDate.append(" ");
			sbDate.append(dateTime.substring(8, 10));
		}
		return sbDate.toString();
    }

    /**
     * 당일날짜취득
     * @return String 당일날짜(8자리)
     */
    public static String getToday(){
    	String msgDate = getDateTimeByPattern("yyyyMMdd");;
        return msgDate;
    }

    /**
     * 현재 시간을 취득한다
     * @return String 현재시간(시간,분,초로 구성된 6자리 문자열)
     */
    public static String getThisTime (){
    	String msgTime = "";

        SimpleDateFormat simpleDate = new SimpleDateFormat("HHmmss");
        msgTime = simpleDate.format(new Date());
        return msgTime;
    }

    /**
     * 현재의 날짜와 시간을 취득한다
     * @return  String 현재날짜날짜(년,월,일 시간,분,초로 구성된 6자리 문자열)
     */
    public static String getDateTime (){
        String msgDate = "";

        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        msgDate = simpleDate.format(new Date());
        return msgDate;
    }

    /**
     * 두날짜를 비교한다.
     * @param date1 날짜1
     * @param date2 날짜2
     * @return  String time values(millisecond ) 0이면 같은 날짜 -이면 cal1이 cal2보다 이전/ +이면 cal1이 cal2보다 이후, "":비교불가
     */
    public static String compareDate(String date1, String date2){
    	if(date1!=null && date1.length()>=8 && date2!=null && date2.length()>=8){

    		try{
	    		int year1 = Integer.parseInt(date1.substring(0, 4));
	    		int month1 = 0;
	    		if(date1.substring(4, 5).equals("0")){
	    			month1 = Integer.parseInt(date1.substring(5, 6));
	    		}else{
	    			month1 = Integer.parseInt(date1.substring(4, 6));
	    		}
	    		month1 = month1-1;
	    		int dayOfMonth1 = 1;
	    		if(date1.substring(6, 7).equals("0")){
	    			dayOfMonth1 = Integer.parseInt(date1.substring(7, 8));
	    		}else{
	    			dayOfMonth1 = Integer.parseInt(date1.substring(6, 8));
	    		}

	    		int year2 = Integer.parseInt(date2.substring(0, 4));
	    		int month2 = 0;
	    		if(date2.substring(4, 5).equals("0")){
	    			month2 = Integer.parseInt(date2.substring(5, 6));
	    		}else{
	    			month2 = Integer.parseInt(date2.substring(4, 6));
	    		}
	    		month2 = month2-1;
	    		int dayOfMonth2 = 1;
	    		if(date2.substring(6, 7).equals("0")){
	    			dayOfMonth2 = Integer.parseInt(date2.substring(7, 8));
	    		}else{
	    			dayOfMonth2 = Integer.parseInt(date2.substring(6, 8));
	    		}

	    		Calendar cal1 = new GregorianCalendar(year1,month1,dayOfMonth1);
	    		Calendar cal2 = new GregorianCalendar(year2,month2,dayOfMonth2);

	    		return Integer.toString(cal1.compareTo(cal2));
    		}catch(Exception e){
    			return "";
    		}
    	}else{
    		return "";
    	}
    }

    /**
     * 기    능 : YYYYMMDD 형식의 날에 일 수를 더해서 날을 얻는다.
     * @param   YYYYMMDD
     * @param   DD
     */
    public static String getDatePlusDay(String YYYYMMDD , String day){
        int iYear	= Integer.parseInt(YYYYMMDD.substring(0,4));
        int iMonth	= Integer.parseInt(YYYYMMDD.substring(4,6))-1;
        int iDays	= Integer.parseInt(YYYYMMDD.substring(6,8));

        int iDD		= Integer.parseInt(day);

        SimpleDateFormat	sdf	=   new SimpleDateFormat("yyyyMMdd");
        Calendar cGivenDay		=	new GregorianCalendar(iYear,iMonth,iDays);

        String	sGivenDay		=   sdf.format(cGivenDay.getTime());

        cGivenDay.add(Calendar.DATE,iDD);

        String 	sDayAfter = sdf.format(cGivenDay.getTime());

        return	sDayAfter;
    }   // End : Util.getYYYYMMDDPlusDD


    /**
     * 일정시간에서 현재시간까지의 시간이 정해진 분까지의 남은 시간을 초단위로 변환
     * @param date1 일정시간(YYYYMMDDHHmmss)
     * @param min 분정보
     * @param long 분까지의 남은 시간
     */
    public static long getTimeToTimeSecond(String date1, String min) {
    	int iYear	= Integer.parseInt(date1.substring(0,4));
        int iMonth	= Integer.parseInt(date1.substring(4,6))-1;
        int iDays	= Integer.parseInt(date1.substring(6,8));
        int iHour	= Integer.parseInt(date1.substring(8,10));
        int iMin	= Integer.parseInt(date1.substring(10,12));
        int iSec	= Integer.parseInt(date1.substring(12,14));

    	Calendar first = new GregorianCalendar(iYear, iMonth, iDays, iHour, iMin, iSec);
    	//정해진시간
    	long lFirst = first.getTimeInMillis();
    	//현재시간
    	long lSecond = System.currentTimeMillis();
    	//정해진시간에서 현재시간까지의 시간차
    	long lTime = (lSecond - lFirst) / 1000 ;

    	//정해진분의 초단위환산
    	long lMin = Long.parseLong(min) * 60;
    	//정해진 분까지의 남은 시간 (초단위)
    	long lTarget = lMin - lTime;
    	return lTarget;
    }

    /**
     * 두날짜간의 간격을 초단위로 변환
     * @param date1 일정시간(YYYYMMDDHHmmss)
     * @param date2 일정시간(YYYYMMDDHHmmss)
     * @param long 분까지의 남은 시간
     */
    public static long getTimeAndTimeToSecond(String date1, String date2) {
    	int iYear	= Integer.parseInt(date1.substring(0,4));
        int iMonth	= Integer.parseInt(date1.substring(4,6))-1;
        int iDays	= Integer.parseInt(date1.substring(6,8));
        int iHour	= Integer.parseInt(date1.substring(8,10));
        int iMin	= Integer.parseInt(date1.substring(10,12));
        int iSec	= Integer.parseInt(date1.substring(12,14));

    	Calendar first = new GregorianCalendar(iYear, iMonth, iDays, iHour, iMin, iSec);
    	//정해진시간
    	long lFirst = first.getTimeInMillis();

    	iYear	= Integer.parseInt(date2.substring(0,4));
        iMonth	= Integer.parseInt(date2.substring(4,6))-1;
        iDays	= Integer.parseInt(date2.substring(6,8));
        iHour	= Integer.parseInt(date2.substring(8,10));
        iMin	= Integer.parseInt(date2.substring(10,12));
        iSec	= Integer.parseInt(date2.substring(12,14));

        Calendar second = new GregorianCalendar(iYear, iMonth, iDays, iHour, iMin, iSec);

    	//현재시간
    	long lSecond = second.getTimeInMillis();

    	//정해진시간에서 현재시간까지의 시간차
    	long lTime = (lSecond - lFirst) / 1000 ;

    	return lTime;
    }

    /**
     * 초데이터를 분데이터로 변환한다
     * @param time 초데이터
     * @return long 분데이터
     */
    public static long getSecToMin(long time) {
    	long min = time/60;

    	return min;
    }

    /**
     * 초데이터를 분으로 구하고 나머지 초를 구한다
     * @param time 초데이터
     * @return long 초데이터
     */
    public static long getSecToMinOtherSec(long time) {
    	long sec = time % 60;

    	return sec;
    }

    /**
     * 날짜 데이터 에서 (10자리이상날짜)시간을 구한다
     * @param date 초데이터
     * @return long 초데이터
     */
    public static String getTimeData(String date) {
    	String time = "00";
    	if (date.length() >= 10) {
    	    time = date.substring(8, 10);
    	}
    	return time;
    }

    /**
     * 날짜 데이터 에서 (12자리이상날짜)분을 구한다
     * @param date 초데이터
     * @return long 초데이터
     */
    public static String getMinData(String date) {
    	String min = "00";
    	if (date.length() >= 12) {
    		String tempMin = date.substring(10, 12);
    		int nMin = Integer.parseInt(tempMin);
    		if (nMin >= 0 && nMin < 10) {
    			min = "00";
    		} else if (nMin >= 10 && nMin < 20) {
    			min = "10";
    		} else if (nMin >= 20 && nMin < 30) {
    			min = "20";
    		} else if (nMin >= 30 && nMin < 40) {
    			min = "30";
    		} else if (nMin >= 40 && nMin < 50) {
    			min = "40";
    		} else if (nMin >= 50 && nMin < 60) {
    			min = "50";
    		}
    	}

    	return min;
    }

    /**
     * 해당하는 주의 시작일
     * @param chooseDate 선택일(YYYYMMDD)
     * @return String 시작일
     */
	public static String getWeekStartDay(String chooseDate) {

		Calendar sDate = getDateCalendarType(chooseDate);
		//요일취득
		int weekVal = sDate.get(Calendar.DAY_OF_WEEK);

		int addCnt = -1*(weekVal-1);
		sDate.add(Calendar.DATE, addCnt);

    	return getDateStringType(sDate);
	}

    /**
     * 해당하는 주의 종료일
     * @param chooseDate 선택일(YYYYMMDD)
     * @return String 종료일
     */
	public static String getWeekEndDay(String chooseDate) {

		Calendar sDate = getDateCalendarType(chooseDate);
		//요일취득
		int weekVal = sDate.get(Calendar.DAY_OF_WEEK);

		int addCnt = 1*(7-weekVal);
		sDate.add(Calendar.DATE, addCnt);

    	return getDateStringType(sDate);
	}

    /**
     * 해당일의 다음주 일자
     * @param chooseDate 선택일(YYYYMMDD)
     * @return String 다음주 일자
     */
	public static String getNextWeekDay(String chooseDate) {

		Calendar sDate = getDateCalendarType(chooseDate);

		sDate.add(Calendar.DATE, 7);

    	return getDateStringType(sDate);
	}

    /**
     * 해당일의 이전주 일자
     * @param chooseDate 선택일(YYYYMMDD)
     * @return String 이전주 일자
     */
	public static String getPrevWeekDay(String chooseDate) {

		Calendar sDate = getDateCalendarType(chooseDate);

		sDate.add(Calendar.DATE, -7);

    	return getDateStringType(sDate);
	}

	 /**
     * 해당일의 일년전 일자
     * @param chooseDate 선택일(YYYYMMDD)
     * @return String 이전주 일자
     */
	public static String getPrevOneYearDay(String chooseDate) {

		Calendar sDate = getDateCalendarType(chooseDate);

		sDate.add(Calendar.DATE, -365);

    	return getDateStringType(sDate);
	}
    /**
     * 기    능 : YYYYMMDD 형식의 스트링을 Calendar형으로 변환한다.
     * @param   YYYYMMDD
     * @return  Calendar
     */
    public static Calendar getDateCalendarType(String dateStr){
    	Calendar calDate = new GregorianCalendar(Integer.parseInt(dateStr.substring(0,4)),
												Integer.parseInt(dateStr.substring(4,6))-1,
												Integer.parseInt(dateStr.substring(6,8)));
    	return calDate;
    }

    /**
     * 기    능 : Calendar를 YYYYMMDD 형식의 스트링으로 변환한다.
     * @param   Calendar
     * @return  YYYYMMDD
     */
    public static String getDateStringType(Calendar calDate){
    	String tmpDate = "";
		int iYear = calDate.get(Calendar.YEAR);
		tmpDate += Integer.toString(iYear);
		int iMonth = calDate.get(Calendar.MONTH)+1;
		if(iMonth<10){
			tmpDate += "0";
		}
		tmpDate += Integer.toString(iMonth);
		int iDate = calDate.get(Calendar.DAY_OF_MONTH);
		if(iDate<10){
			tmpDate += "0";
		}
		tmpDate += Integer.toString(iDate);

    	return tmpDate;
    }

    /**
     * 날짜를 년월일（８자리）만취득하고구분형식으로 표시한다
     * @param dateTime 8자리이상날짜
     * @param pattern (-,/)구분형식
     * @return String 취득한날짜
     */
    public static String getChartDateFormat(String dateTime, String pattern){
    	StringBuffer sbDate = new StringBuffer("");
		if ( dateTime != null && dateTime.length() >= 4){
			sbDate.append(dateTime.substring(0, 4));
		}
		if ( dateTime != null && dateTime.length() >= 6){
			sbDate.append(pattern);
			sbDate.append(dateTime.substring(4, 6));
		}
		if ( dateTime != null && dateTime.length() >= 8){
			sbDate.append(pattern);
			sbDate.append(dateTime.substring(6, 8));
		}
		if ( dateTime != null && dateTime.length() >= 10){
			sbDate.append(" ");
			sbDate.append(dateTime.substring(8, 10));
		}
		if ( dateTime != null && dateTime.length() >= 12){
			sbDate.append(":");
			sbDate.append(dateTime.substring(10, 12));
		}
		return sbDate.toString();
    }

    /**
     * 초단위시간을 몇일몇시간몇분몇초로 구한다.
     * @param total 총초
     * @return
     */
    public static String getSecondToFullTime(long total) {
    	StringBuffer sbBuf = new StringBuffer("");
    	if (total > 0) {
    		long nSec = total % 60;
    		long temp = total / 60;
	    	if (temp <= 0) {
	    		sbBuf.append(nSec);
	    		sbBuf.append("초");
	    		return sbBuf.toString();
	    	}
	    	long nMin = temp % 60;
	    	long temp1 = temp / 60;
	    	if (temp1 <= 0) {
	    		sbBuf.append(nMin);
	    		sbBuf.append("분");
	    		sbBuf.append(nSec);
	    		sbBuf.append("초");
	    		return sbBuf.toString();
	    	}
	    	long nHour = temp1 % 24;
	    	long nDay =  temp1 / 24;
	    	if (nHour <= 0) {
	    		sbBuf.append(nHour);
	    		sbBuf.append("시간");
	    		sbBuf.append(nMin);
	    		sbBuf.append("분");
	    		sbBuf.append(nSec);
	    		sbBuf.append("초");
	    		return sbBuf.toString();
	    	} else {
	    		sbBuf.append(nDay);
	    		sbBuf.append("일");
	    		sbBuf.append(nHour);
	    		sbBuf.append("시간");
	    		sbBuf.append(nMin);
	    		sbBuf.append("분");
	    		sbBuf.append(nSec);
	    		sbBuf.append("초");
	    		return sbBuf.toString();
	    	}

    	} else {
    		sbBuf.append("0");
    		return sbBuf.toString();
    	}
    }
    /**
     * 날짜를 년월일（８자리）만취득하고구분형식으로 표시한다
     * @param dateTime 8자리이상날짜
     * @param pattern (-,/)구분형식
     * @return String 취득한날짜
     */
    public static String getPostgresDateFormat(String dateTime, String pattern){
    	StringBuffer sbDate = new StringBuffer("");
		if ( dateTime != null && dateTime.length() >= 8){
			sbDate.append(dateTime.substring(0, 4));
			sbDate.append(pattern);
			sbDate.append(dateTime.substring(4, 6));
			sbDate.append(pattern);
			sbDate.append(dateTime.substring(6, 8));
		}

		if ( dateTime != null && dateTime.length() >= 10){
			sbDate.append(" ");
			sbDate.append(dateTime.substring(8, 10));
		} else {
			sbDate.append(" 00");
		}
		if ( dateTime != null && dateTime.length() >= 12){
			sbDate.append(":");
			sbDate.append(dateTime.substring(10, 12));
		} else {
			sbDate.append(" 00");
		}
		if ( dateTime != null && dateTime.length() >= 14){
			sbDate.append(":");
			sbDate.append(dateTime.substring(12, 14));
		} else {
			sbDate.append(" 00");
		}

		return sbDate.toString();
    }
    /**
     * 날짜를 년월일（８자리）만취득하고구분형식으로 표시한다
     * @param dateTime 8자리이상날짜
     * @param pattern (-,/)구분형식
     * @return String 취득한날짜
     */
    public static String getTodayHan(){
    	StringBuffer sbDate = new StringBuffer("");
    	String today = getToday();
		sbDate.append(today.substring(0, 4));
		sbDate.append("년 ");
		sbDate.append(today.substring(4, 6));
		sbDate.append("월 ");
		sbDate.append(today.substring(6, 8));
		sbDate.append("일 ");
		return sbDate.toString();
    }
    /**
     * 요일취득
     * @return String 당일날짜(8자리)
     */
    public static String getWeekDay(String date){
    	String lang = "KOR";
    	int cnt = 1;
    	return getWeekDay(date, lang, cnt);
    }
    /**
     * 요일취득
     * @return String 당일날짜(8자리)
     */
    public static String getWeekDay(String date, String lang, int cnt){
    	if(date==null || date.equals("")){
    		return "";

    	}else if(date.length()>7){
	    	if(date.length()>=10){
	    		date = date.substring(0,10);
	    		date.replace(".", "");
	    	}
    		int iYear	= Integer.parseInt(date.substring(0,4));
	        int iMonth	= Integer.parseInt(date.substring(4,6))-1;
	        int iDays	= Integer.parseInt(date.substring(6,8));
	    	Calendar cal = new GregorianCalendar(iYear,iMonth,iDays);
	    	int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
	    	String dayOfWeekStr = "";
	    	if(dayOfWeek==Calendar.SUNDAY){
	    		if(lang.equals("KOR") && cnt==1){
	    			dayOfWeekStr = "(일)";
	    		}else{
	    			dayOfWeekStr = "(Sun)";
	    		}
	    	}else if(dayOfWeek==Calendar.MONDAY){
	    		if(lang.equals("KOR") && cnt==1){
	    			dayOfWeekStr = "(월)";
	    		}else{
	    			dayOfWeekStr = "(Mon)";
	    		}
	    	}else if(dayOfWeek==Calendar.TUESDAY){
	    		if(lang.equals("KOR") && cnt==1){
	    			dayOfWeekStr = "(화)";
	    		}else{
	    			dayOfWeekStr = "(Tue)";
	    		}
	    	}else if(dayOfWeek==Calendar.WEDNESDAY){
	    		if(lang.equals("KOR") && cnt==1){
	    			dayOfWeekStr = "(수)";
	    		}else{
	    			dayOfWeekStr = "(Wed)";
	    		}
	    	}else if(dayOfWeek==Calendar.THURSDAY){
	    		if(lang.equals("KOR") && cnt==1){
	    			dayOfWeekStr = "(목)";
	    		}else{
	    			dayOfWeekStr = "(Thu)";
	    		}
	    	}else if(dayOfWeek==Calendar.FRIDAY){
	    		if(lang.equals("KOR") && cnt==1){
	    			dayOfWeekStr = "(금)";
	    		}else{
	    			dayOfWeekStr = "(Fri)";
	    		}
	    	}else if(dayOfWeek==Calendar.SATURDAY){
	    		if(lang.equals("KOR") && cnt==1){
	    			dayOfWeekStr = "(토)";
	    		}else{
	    			dayOfWeekStr = "(Sat)";
	    		}
	    	}
	        return dayOfWeekStr;
    	}else{
    		return "";
    	}
    }

    /**
     * 날짜를 년월일（８자리）만취득하고구분형식으로 표시한다
     * @param dateTime 8자리이상날짜
     * @param pattern (-,/)구분형식
     * @return String 취득한날짜
     */
    public static String getPostgresDeleteDateFormat(String dateTime){
    	StringBuffer sb = new StringBuffer();
        if (dateTime == null || dateTime.equals("")){
            return "";
        }
        sb.append(dateTime.substring(0,4));
        sb.append(dateTime.substring(5,7));
        sb.append(dateTime.substring(8,10));
        sb.append(dateTime.substring(11,13));
        sb.append(dateTime.substring(14,16));
        sb.append(dateTime.substring(17));
		return sb.toString();
    }


    /**
     * 현재의 날짜와 시간을 취득한다
     * @return  String 현재날짜날짜(년,월,일 시간,분,초로 구성된 6자리 문자열)
     */
    public static String getFullDateTime (){
        String msgDate = "";

        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMddHHmmss");
        msgDate = simpleDate.format(new Date());
        return msgDate;
    }

	/**
     * 2011.08.09
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
			
		    //throw new RuntimeException(e);	// 보안점검 후속조치
		}
		return rtnStr;
    }
	

    /**
     * 해당일의 이전 일자
     * @param chooseDate 선택일(YYYYMMDD)
     * @return String 이전 일자
     */
	public static String getPrevOneDay(String chooseDate) {

		Calendar sDate = getDateCalendarType(chooseDate);

		sDate.add(Calendar.DATE, -1);

    	return getDateStringType(sDate);
	}
}
