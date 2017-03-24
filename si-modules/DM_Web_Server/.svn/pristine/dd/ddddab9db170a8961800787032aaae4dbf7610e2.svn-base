/*****************************************************************************
 * 프로그램명  : PagingTable.java
 * 설     명  : ȸ��공통부품/Utillity
 * 참고  사항  : 없음
 *****************************************************************************
 * Date       Author  Version Description
 * ---------- ------- ------- -----------------------------------------------
 * 2013.09.11   LSH     1.0     초기작성
 *****************************************************************************/
package net.herit.common.util;

import java.util.ArrayList;
import java.util.List;

import net.herit.common.conf.HeritProperties;


/**
 * 출력리스트페이지클래스
 * @author herit
 * @version 1.0
 */
public class PagingUtil {
	/** 리스트싸이즈의기본값 */
	private static final int BOARD_DEFAULT_PAGESIZE = 5;
	/** 리스트페이지셋싸이즈의기본값 */
	private static final int BOARD_DEFAULT_BIGPAGESIZE = 5;
    /** 전체리스트싸이즈레코드수 */
	private int totalSize;
    /** 페이징단위(Ibatis용) */
	private int pageSize;
    /** 전체페이지수 (totalSize/pageSize)*/
	private int totalPage;
	/** 현재페이지번호 */
    private int currPage;
    /** 현재페이지에서 취득한 레코드수(Ibatis용) */
    private int currPageRowCount;
    /** 개시페이지 */
    private int sIndex ;
    /** 종료페이지 */
    private int eIndex ;
    /** 페이지셋싸이즈 */
    private int bigPageSize;
    /** 페이지셋수 */
    private int bigTotalPage;
    /** 페이지셋의현재위치 */
    private int bigCurrPage;
    /** 페이지셋의개시페이지*/
    private int sBigIndex ;
    /** 페이지셋의종료페이지 */
    private int eBigIndex ;
    /** 페이징된 리스트*/
    private List currList;
    /** 페이지항번수 */
    private int count ;
    /** 개시번호(Ibatis용) */
    private int startIndex ;
    /** 종료페이지번호(Ibatis용) */
    private int endIndex ;
    /** 검색키 */
    private String searchKey = "";
    /** 검색값 */
    private String searchVal = "";
    /** 검색조건 */
    private String condValue = "";
    /** 검색조건데이터BEAN（업무별검색조건이다른경우에대한 OBJECT객체를 선언) */
    private Object objSearch = null;
    /** 코드종별 */
    private int kind ;

    /** 전체레코드카운트 */
    private int nTotal ;

    /** 페이징된 리스트*/
    private List sumList;

    public int getNTotal() {

		return this.nTotal;
	}

	public void setNTotal() {

		nTotal = currList.size();

	}

	/**
     * 페이지기본값 초기화
     */
	public PagingUtil() {
        setDataZeroValue();
	}

	/**
     * 페이지기본값 초기화
     * @param keyBoard 게시판의종류(IPropertyKey에정의되어있는값을 지정한다
     * @param nTotal 전체레코드수
     * @param cPage 현재페이지위치
     */
	public PagingUtil(String keyBoard, int nTotal, int cPage) {
        setDefaultInformation(keyBoard, nTotal, cPage);
	}

	/**
     * 페이지기본값 초기화
     * @param keyBoard　게시판의종류(IPropertyKey에정의되어있는값을 지정한다
	 * @param nTotal　전체레코드수
	 * @param cPage　현재페이지위치
	 * @param searchInfo 검색조건클래스
     */
	public PagingUtil(String keyBoard, int nTotal, int cPage, Object searchInfo) {
        setDefaultInformation(keyBoard, nTotal, cPage);
        setObjSearch (searchInfo);
	}

	/**
	 * 기본페이지정보를 설정한다　
	 * @param keyBoard　게시판의종류(IPropertyKey에정의되어있는값을 지정한다
	 * @param nTotal　전체레코드수
	 * @param cPage　현재페이지위치
	 */
    private void setDefaultInformation(String keyBoard, int nTotal, int cPage){
    	int cPageSize = Integer.parseInt(HeritProperties.getProperty("Globals.PageSize"));
    	int cBigPageSize = Integer.parseInt(HeritProperties.getProperty("Globals.PagesetSize"));

    	if (cPageSize == 0) {
    		cPageSize = BOARD_DEFAULT_PAGESIZE;
    	}
    	if (cBigPageSize == 0) {
    		cBigPageSize = BOARD_DEFAULT_BIGPAGESIZE;
    	}
    	setPagingTable(nTotal, cPageSize, cPage, cBigPageSize);
    }

    /**
     * 취득하는대상페이지레코드만취득한다
     * @param nTotal 전체레코드수
     * @param cPageSize 취득하는페이지단위
     * @param cPage 현재페이지
     * @param cBigPageSize 페이지셋싸이즈
     */
     public void setPagingTable(int nTotal, int cPageSize, int cPage, int cBigPageSize) {
         if (nTotal == 0){
             setDataZeroValue();
         }else{
             setPageValue(nTotal,cPageSize,cPage);
             setBigValue(cBigPageSize);
         }
 	}

    /**
    * 데이터가존재하지 않는경우 기본설정
    */
    private void setDataZeroValue(){
        totalSize = 0;         //전체싸이즈(전체레코드)
        pageSize = 0;          //페이징단위
        totalPage = 0;         //전체페이지수(totalSize/pageSize)
        currPage = 0;          //현재페이지
        currPageRowCount = 0;	//현재페이지의 취득 데이터 싸이즈
        sIndex = 0;            //개시위치
        eIndex = 0;            //종료위치
        bigPageSize = 0;       //페이지셋싸이즈
        bigTotalPage = 0;      //페이지셋수
        bigCurrPage = 0;       //현재페이지쎗
        sBigIndex = 0;         //페이지셋개시
        eBigIndex = 0;         //페이지셋종료
        startIndex = 0;        //페이지개시번호
        endIndex = 0;          //페이지종료번호
        currList = new ArrayList(); //페이지의레코드
    }


   /**
    * 페이지관리데이터를 설정한다
    * @param vtList 취득대상레코드
    * @param cPageSize 취득하는페이지싸이즈
    * @param cPage 현재페이지번호
    */
    private void setPageValue(int nTotal, int cPageSize, int cPage){
        pageSize  = cPageSize;
        totalSize = nTotal;
        if ((totalSize % pageSize) == 0){
            totalPage = (int)(totalSize / pageSize);
        }else{
            totalPage = (int)(totalSize / pageSize) + 1;
        }
        if (cPage > totalPage) {
        	cPage = totalPage;
        }
        currPage = cPage;
        currPageRowCount = currPage* cPageSize;

        sIndex = pageSize * (currPage - 1) + 1;
        eIndex = pageSize * currPage;

        count = totalSize - sIndex + 1;

        if (eIndex > totalSize){
            eIndex = totalSize;
        }
        startIndex = sIndex;
        endIndex = eIndex;

        //페이징 처리할때 가장 마지막 페이지 갈때 pageSize가 1로 나오는 현상 때문에 주석처리 by 2014.04.11 LSH
//        if(currPage==totalPage && (totalSize%cPageSize)>0){
//        	pageSize = (int)(totalSize%cPageSize);
//        }
    }

   /**
    * 페이지셋관련값설정
    * @param cBigPageSize 현재페이지셋번호
    */
    private void setBigValue(int cBigPageSize){
        bigPageSize = cBigPageSize;

        if ((currPage % bigPageSize) == 0){
            bigCurrPage = (int)(currPage / bigPageSize);
        }else{
            bigCurrPage = (int)(currPage / bigPageSize) + 1;
        }

        if ((totalPage % bigPageSize) == 0){
            bigTotalPage = (int)(totalPage / bigPageSize);
        }else{
            bigTotalPage = (int)(totalPage / bigPageSize) + 1;
        }

        sBigIndex = (bigPageSize * (bigCurrPage - 1)) + 1;
        eBigIndex = bigPageSize * bigCurrPage;

        if (totalPage < eBigIndex){
            eBigIndex = totalPage;
        }
    }

    /*
     * 전체레코드수취득
     * @return int 전체레코드수
     */
    public int getTotalSize() {

		return totalSize;
	}

    /*
     * 페이지단위취득
     * @return int 페이지단위
     */
    public int getPageSize() {
        return pageSize;
	}

    /*
     * 전체페이지수취득
     * @return int 전체페이지수
     */
    public int getTotalPage() {
        return totalPage;
	}

    /*
     * 현재페이지취득
     * @return int 현재페이지
     */
    public int getCurrPage() {
        return currPage;
	}

    /*
     * 페이지의데이터싸이즈취득
     * @return int 페이지의데이터싸이즈
     */
    public int getCurrPageRowCount() {
        return currPageRowCount;
	}

    /*
     * 페이지개시위치취득
     * @return int 페이지개시위치
     */
    public int getSIndex() {
        return sIndex;
	}

    /*
     * 페이지종료위치취득
     * @return int 페이지종료위치
     */
    public int getEIndex() {
        return eIndex;
	}

    /*
     * 페이지셋싸이즈취득
     * @return int 페이지셋싸이즈
     */
    public int getBigPageSize() {
        return bigPageSize;
	}

    /*
     * 전체페이지셋수취득
     * @return int 전체페이지셋수
     */
    public int getBigTotalPage() {
        return bigTotalPage;
	}

    /*
     * 페이지셋의현재위치취득
     * @return int 페이지셋의현재위치
     */
    public int getBigCurrPage() {
        return bigCurrPage;
	}

    /*
     * 페이지셋의개시위치취득
     * @return int 페이지셋의개시위치
     */
    public int getSBigIndex() {
        return sBigIndex;
	}

    /*
     * 페이지셋의종료위치취득
     * @return int 페이지셋의종료위치
     */
    public int getEBigIndex() {
        return eBigIndex;
	}

    /*
     * 페이지항번의개시번호취득
     * @return int 페이지항번의개시번호
     */
    public int getCount() {
        return count;
	}

    /*
     * 페이징하는대상리스트설정
     * @param currList 페이징하는대상리스트
     */
    public void setCurrList(List currList) {
    	this.currList = currList;
	}

    /*
     * 페이징하는대상리스트취득
     * @return List 페이징하는대상리스트
     */
    public List getCurrList() {
        return currList;
	}

    /*
     * 페이지개시위치취득
     * @return int 페이지개시위치
     */
    public int getStartIndex() {
        return startIndex;
	}

    /*
     * 페이지종료위치취득
     * @return int 페이지종료위치
     */
    public int getEndIndex() {
        return endIndex;
	}

    /*
     * 검색키취득
     * @return String 검색키
     */
    public String getSearchKey() {
        return searchKey;
	}

    /*
     * 검색값취득
     * @return String 검색값
     */
    public String getSearchVal() {
        return searchVal;
	}

    /*
     * 검색조건취득
     * @return String 검색조건
     */
    public String getCondValue() {
        return condValue;
	}

    /*
     * 검색조건（업무별）클래스설정
     * @param objSearch 검색조건（업무별）클래스
     */
    public void setObjSearch(Object objSearch) {
        this.objSearch = objSearch;
	}

    /**
     * 검색조건（업무별）클래스취득
     * @return Object　검색조건（업무별）클래스
     */
    public Object getObjSearch() {
        return objSearch;
	}

    /*
     * 코드종별설정
     * @param kind 코드종별
     */
    public void setKind(int kind) {
        this.kind = kind;
	}

    /**
     * 코드종별취득
     * @return kind　코드종별
     */
    public int getKind() {
        return kind;
	}


	/**
	 * 현재페이지와 게시판의종류를 넘겨 PageSize를 리턴한다.
	 * @param keyBoard	게시판의종류(IPropertyKey에정의되어있는값을 지정한다
	 * @return int 결과PageSize
	 */
	public static int getPageSize(String keyBoard) {
    	int cPageSize = Integer.parseInt(HeritProperties.getProperty("Globals.PageSize"));
    	
    	if (cPageSize == 0) {
    		cPageSize = 10;
    	}
    	return cPageSize;
	}

	/**
	 * 현재페이지와 게시판의종류를 넘겨 시작INDEX를 리턴한다.
	 * @param cPage	현재페이지
	 * @param keyBoard	게시판의종류(IPropertyKey에정의되어있는값을 지정한다
	 * @return int 결과INDEX
	 */
	public static int getPaingStartIndex(int cPage, String keyBoard) {
		//return ( getPageSize(keyBoard) * (cPage - 1) + 1);
		return ( getPageSize(keyBoard) * (cPage - 1));	// MYSQL용으로 수정 by 이인석 20141209
	}
	
	/**
	 * 현재페이지와 게시판의종류를 넘겨 시작INDEX를 리턴한다.
	 * @param cPage	현재페이지
	 * @param keyBoard	게시판의종류(IPropertyKey에정의되어있는값을 지정한다
	 * @return int 결과INDEX
	 */
	public static int getPaingEndIndex(int cPage, String keyBoard) {
		return ( getPageSize(keyBoard) * cPage );
	}

	 /*
     * 통계등의 총계리스트설정
     * @param sumList 통계등의 총계리스트
     */
    public void setSumList(List sumList) {
    	this.sumList = sumList;
	}

    /*
     * 통계등의 총계리스트취득
     * @return List 통계등의 총계리스트
     */
    public List getSumList() {
        return sumList;
	}
}
