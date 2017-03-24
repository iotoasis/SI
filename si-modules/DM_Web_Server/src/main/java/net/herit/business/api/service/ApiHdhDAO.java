package net.herit.business.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.herit.business.api.service.*;
import net.herit.common.dataaccess.*;
import net.herit.common.exception.UserSysException;
import net.herit.common.model.*;

import org.springframework.stereotype.Repository;

import com.ibatis.sqlmap.client.SqlMapException;

@Repository("ApiHdhDAO")
public class ApiHdhDAO extends HeritHdhAbstractDAO {
	/** 클래스 명칭 */
	private final String CLASS_NAME = getClass().getName();
	/** 메소드명칭 */
	private String METHOD_NAME = "";
	
	public List<Object> getList(String queryId, HashMap<String, Object> po) throws UserSysException {
		METHOD_NAME = "getList";
		List resultList = null;

		try {
			resultList = getSqlMapClientTemplate().queryForList(
					"DeviceDAO.deviceList", po);

		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return resultList;
	}
	
	public Object get(String queryId, HashMap<String, Object> po) throws UserSysException {
		METHOD_NAME = "get";

		HashMap<String, Object> result = null;
		try {
			result = (HashMap)getSqlMapClientTemplate().queryForObject(
					"DeviceDAO.deviceCount", po);

		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
				
		return result;
	}
	
	public ProcessResult update(String queryId, HashMap<String, Object> po) throws UserSysException {
		METHOD_NAME = "update";

		ProcessResult result = null;

				
		return result;
	}
	
	public ProcessResult insert(String queryId, HashMap<String, Object> po) throws UserSysException {
		METHOD_NAME = "insert";

		ProcessResult result = null;

				
		return result;
	}
	
	public ProcessResult delete(String queryId, HashMap<String, Object> po) throws UserSysException {
		METHOD_NAME = "delete";

		ProcessResult result = null;

				
		return result;
	}
	
	/***********************************************
	  		LWM2M
	 ***********************************************/
	
	// 제어 이력 남기기
	public int insertControlHistory(HashMap<String, String> map) throws UserSysException {
		METHOD_NAME = "insertControlHistory";
		int result = 0;
		try{
			result = (Integer) insert("insert.control.history", map);
		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME, 
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return result;
	}
		 
	
	// 상태 이력 남기기
	public int insertStatusHistory(HashMap<String, String> map) throws UserSysException {
		METHOD_NAME = "insertControlHistory";
		int result = 0;
		try{
			result = (Integer) insert("insert.status.history", map);
		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME, 
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return result;
	}
	
}
