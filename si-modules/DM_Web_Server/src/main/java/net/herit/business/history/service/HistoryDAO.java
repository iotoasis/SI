package net.herit.business.history.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import net.herit.business.history.service.*;
import net.herit.common.dataaccess.HeritHdhAbstractDAO;
import net.herit.common.exception.UserSysException;

import org.springframework.stereotype.Repository;

import com.ibatis.sqlmap.client.SqlMapException;

@Repository("HistoryDAO")
public class HistoryDAO extends HeritHdhAbstractDAO {
	/** 클래스 명칭 */
	private final String CLASS_NAME = getClass().getName();
	/** 메소드명칭 */
	private String METHOD_NAME = "";
	
	public List<HashMap<String,String>> getErrorList(HashMap<String, Object> po) throws UserSysException {
		METHOD_NAME = "getErrorList";

		List resultList = null;

		try {
			//resultList = getSqlMapClientTemplate().queryForList(
			//		"DeviceDAO.deviceList", po);
			resultList = getSqlMapClientTemplate().queryForList(
					"history.error.list", po);

		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return resultList;
	}
	
	
	public int getErrorCount(HashMap<String, Object> po) throws UserSysException {
		METHOD_NAME = "getErrorCount";

		int count = 0;
		try {
			count = (Integer)getSqlMapClientTemplate().queryForObject(
					"history.error.count", po);

		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
				
		return count;
	}
	
	public List<HashMap<String,String>> getControlList(HashMap<String, Object> po) throws UserSysException {
		METHOD_NAME = "getControlList";

		List resultList = null;

		try {
			//resultList = getSqlMapClientTemplate().queryForList(
			//		"DeviceDAO.deviceList", po);
			resultList = getSqlMapClientTemplate().queryForList(
					"history.control.list", po);

		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return resultList;
	}
	
	
	public int getControlCount(HashMap<String, Object> po) throws UserSysException {
		METHOD_NAME = "getControlCount";

		int count = 0;
		try {
			count = (Integer)getSqlMapClientTemplate().queryForObject(
					"history.control.count", po);

		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
				
		return count;
	}
	
	public List<HashMap<String,String>> getFirmwareUpgradeList(HashMap<String, Object> po) throws UserSysException {
		METHOD_NAME = "getFirmwareUpgradeList";

		List resultList = null;

		try {
			//resultList = getSqlMapClientTemplate().queryForList(
			//		"DeviceDAO.deviceList", po);
			resultList = getSqlMapClientTemplate().queryForList(
					"history.firmwareUpgrade.list", po);

		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return resultList;
	}
	
	
	public int getFirmwareUpgradeCount(HashMap<String, Object> po) throws UserSysException {
		METHOD_NAME = "getFirmwareUpgradeCount";

		int count = 0;
		try {
			count = (Integer)getSqlMapClientTemplate().queryForObject(
					"history.firmwareUpgrade.count", po);

		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
				
		return count;
	}
	
	public List<HashMap<String,String>> getDeviceStatusList(HashMap<String, Object> po) throws UserSysException {
		METHOD_NAME = "getDeviceStatusList";

		List resultList = null;

		try {
			//resultList = getSqlMapClientTemplate().queryForList(
			//		"DeviceDAO.deviceList", po);
			resultList = getSqlMapClientTemplate().queryForList(
					"history.deviceStatus.list", po);

		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return resultList;
	}
	
	
	public int getDeviceStatusCount(HashMap<String, Object> po) throws UserSysException {
		METHOD_NAME = "getDeviceStatusCount";

		int count = 0;
		try {
			count = (Integer)getSqlMapClientTemplate().queryForObject(
					"history.deviceStatus.count", po);

		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
				
		return count;
	}

//SK C&C 
	/*public List<HashMap<String, Object>> getDeviceStatusHistList(String resourceUri, String resourceName) throws UserSysException {
		METHOD_NAME = "getDeviceStatusHistList";
		
		List resultList = null;
		//HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			HashMap<String, String> po = new HashMap<String, String>();
			po.put("resourceUri", resourceUri);
			po.put("resourcName", resourceName);
			
			resultList = getSqlMapClientTemplate().queryForList("history.statusData.list", po);
		
		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return resultList;
	}*/
}
