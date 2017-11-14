/*
 * Copyright 2008-2009 MOPAS(Ministry of Public Administration and Security).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.herit.common.dataaccess;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * Spring 의 iBatis 연동 지원을 Annotation 형식으로 쉽게 처리하기 위한 공통
 * parent DAO 클래스
 * <p>
 * <b>NOTE</b>: Spring 에서 iBatis 연동을 지원하는
 * org.springframework.orm.ibatis.support.
 * SqlMapClientDaoSupport 을 extends 하고 있으며 CRUD 와 관련한
 * 대표적인 method 를 간단하게 호출할 수 있도록 Wrapping 하고 있어 사용자 DAO
 * 에서 iBatis sqlMapClient 호출을 쉽게 하며 Bean 생성 시
 * Annotation 기반으로 sqlMapClient 을 쉽게 injection 할 수 있는
 * 공통 로직을 포함하고 있다.
 * @author 실행환경 개발팀 우병훈
 * @since 2009.02.25
 * @version 1.0
 * @see <pre>
 *  == 개정이력(Modification Information) ==
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.02.25  우병훈          최초 생성
 * 
 * </pre>
 */
public class HeritAbstractDAO extends SqlMapClientDaoSupport {

    /**
     * HeritAbstractDAO 는 base class 로만 사용되며 해당 인스턴스를 직접
     * 생성할 수 없도록 protected constructor 로 선언하였음.
     */
    protected HeritAbstractDAO() {
        // PMD abstract Rule - If the class is intended
        // to be used as a base class only (not to be
        // instantiated
        // directly)
        // a protected constructor can be provided
        // prevent direct instantiation
    }

    /**
     * Annotation 형식으로 sqlMapClient 를 받아와 이를
     * super(SqlMapClientDaoSupport) 의 setSqlMapClient
     * 메서드를 호출하여 설정해 준다.
     * @param sqlMapClient
     *        - ibatis 의 SQL Map 과의 상호작용을 위한 기본 클래스로
     *        mapped statements(select, insert, update,
     *        delete 등) 의 실행을 지원함.
     */
    @Resource(name = "sqlMapClient")
    public void setSuperSqlMapClient(SqlMapClient sqlMapClient) {
        super.setSqlMapClient(sqlMapClient);
    }

    /**
     * 입력 처리 SQL mapping 을 실행한다.
     * @param queryId
     *        - 입력 처리 SQL mapping 쿼리 ID
     * @param parameterObject
     *        - 입력 처리 SQL mapping 입력 데이터를 세팅한 파라메터
     *        객체(보통 VO 또는 Map)
     * @return 입력 시 selectKey 를 사용하여 key 를 딴 경우 해당 key
     */
    public Object insert(String queryId, Object parameterObject) {
        return getSqlMapClientTemplate().insert(queryId, parameterObject);
    }

    /**
     * 수정 처리 SQL mapping 을 실행한다.
     * @param queryId
     *        - 수정 처리 SQL mapping 쿼리 ID
     * @param parameterObject
     *        - 수정 처리 SQL mapping 입력 데이터(key 조건 및 변경
     *        데이터)를 세팅한 파라메터 객체(보통 VO 또는 Map)
     * @return DBMS가 지원하는 경우 update 적용 결과 count
     */
    public int update(String queryId, Object parameterObject) {
        return getSqlMapClientTemplate().update(queryId, parameterObject);
    }

    /**
     * 삭제 처리 SQL mapping 을 실행한다.
     * @param queryId
     *        - 삭제 처리 SQL mapping 쿼리 ID
     * @param parameterObject
     *        - 삭제 처리 SQL mapping 입력 데이터(일반적으로 key 조건)를
     *        세팅한 파라메터 객체(보통 VO 또는 Map)
     * @return DBMS가 지원하는 경우 delete 적용 결과 count
     */
    public int delete(String queryId, Object parameterObject) {
        return getSqlMapClientTemplate().delete(queryId, parameterObject);
    }

    /**
     * pk 를 조건으로 한 단건조회 처리 SQL mapping 을 실행한다.
     * @param queryId
     *        - 단건 조회 처리 SQL mapping 쿼리 ID
     * @param parameterObject
     *        - 단건 조회 처리 SQL mapping 입력 데이터(key)를 세팅한
     *        파라메터 객체(보통 VO 또는 Map)
     * @return 결과 객체 - SQL mapping 파일에서 지정한
     *         resultClass/resultMap 에 의한 단일 결과 객체(보통
     *         VO 또는 Map)
     */
    public Object selectByPk(String queryId, Object parameterObject) {
        return getSqlMapClientTemplate().queryForObject(queryId,
            parameterObject);
    }

    /**
     * 파라미터의 조건을 이용하여 단건조회 처리 SQL mapping 을 실행한다.
     * @param queryId
     *        - 단건 조회 처리 SQL mapping 쿼리 ID
     * @param parameterObject
     *        - 단건 조회 처리 조건 세팅한
     *        파라메터 객체(보통 VO 또는 Map)
     * @return 결과 객체 - SQL mapping 파일에서 지정한
     *         resultClass/resultMap 에 의한 단일 결과 객체(보통
     *         VO 또는 Map)
     */
    public Object select(String queryId, Object parameterObject) {
        return getSqlMapClientTemplate().queryForObject(queryId,
            parameterObject);
    }

    /**
     * 리스트 조회 처리 SQL mapping 을 실행한다.
     * @param queryId
     *        - 리스트 조회 처리 SQL mapping 쿼리 ID
     * @param parameterObject
     *        - 리스트 조회 처리 SQL mapping 입력 데이터(조회 조건)를
     *        세팅한 파라메터 객체(보통 VO 또는 Map)
     * @return 결과 List 객체 - SQL mapping 파일에서 지정한
     *         resultClass/resultMap 에 의한 결과 객체(보통 VO
     *         또는 Map)의 List
     */
    @SuppressWarnings("unchecked")
    public List list(String queryId, Object parameterObject) {
        return getSqlMapClientTemplate().queryForList(queryId, parameterObject);
    }

    /**
     * 부분 범위 리스트 조회 처리 SQL mapping 을 실행한다. (부분 범위 -
     * pageIndex 와 pageSize 기반으로 현재 부분 범위 조회를 위한
     * skipResults, maxResults 를 계산하여 ibatis 호출)
     * @param queryId
     *        - 리스트 조회 처리 SQL mapping 쿼리 ID
     * @param parameterObject
     *        - 리스트 조회 처리 SQL mapping 입력 데이터(조회 조건)를
     *        세팅한 파라메터 객체(보통 VO 또는 Map)
     * @param pageIndex
     *        - 현재 페이지 번호
     * @param pageSize
     *        - 한 페이지 조회 수(pageSize)
     * @return 부분 범위 결과 List 객체 - SQL mapping 파일에서 지정한
     *         resultClass/resultMap 에 의한 부분 범위 결과
     *         객체(보통 VO 또는 Map) List
     */
    @SuppressWarnings("unchecked")
    public List listWithPaging(String queryId, Object parameterObject,
            int pageIndex, int pageSize) {
        int skipResults = pageIndex * pageSize;
        int maxResults = (pageIndex * pageSize) + pageSize;
        return getSqlMapClientTemplate().queryForList(queryId, parameterObject,
            skipResults, maxResults);
    }

}
