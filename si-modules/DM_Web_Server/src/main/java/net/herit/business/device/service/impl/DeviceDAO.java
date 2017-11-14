package net.herit.business.device.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import net.herit.business.device.service.*;
import net.herit.common.dataaccess.HeritHdmAbstractDAO;
import net.herit.common.exception.UserSysException;

import org.springframework.stereotype.Repository;

import com.ibatis.sqlmap.client.SqlMapException;

@Repository("DeviceDAO")
public class DeviceDAO extends HeritHdmAbstractDAO {
	/** 클래스 명칭 */
	private final String CLASS_NAME = getClass().getName();
	/** 메소드명칭 */
	private String METHOD_NAME = "";

	public List<Object> getDeviceListPaging(HashMap<String, Object> po) throws UserSysException {
		METHOD_NAME = "getDeviceListPaging";

		List resultList = null;

		try {
			//resultList = getSqlMapClientTemplate().queryForList(
			//		"DeviceDAO.deviceList", po);
			resultList = getSqlMapClientTemplate().queryForList(
					"device.info.list", po);

		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return resultList;
	}
	
	public int getDeviceCount(HashMap<String, Object> po) throws UserSysException {
		METHOD_NAME = "getDeviceCount";

		int count = 0;
		try {
			count = (Integer)getSqlMapClientTemplate().queryForObject(
					"device.list.count", po);

		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
				
		return count;
	}

	public List<Object> getDeviceFirmwareListPaging(HashMap<String, Object> po) throws UserSysException {
		METHOD_NAME = "getDeviceFirmwareListPaging";

		List resultList = null;

		try {
			//resultList = getSqlMapClientTemplate().queryForList(
			//		"DeviceDAO.deviceList", po);
			resultList = getSqlMapClientTemplate().queryForList(
					"device.firmware.list", po);

		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return resultList;
	}
	
	public int getDeviceFirmwareCount(HashMap<String, Object> po) throws UserSysException {
		METHOD_NAME = "getDeviceFirmwareCount";

		int count = 0;
		try {
			count = (Integer)getSqlMapClientTemplate().queryForObject(
					"device.firmware.count", po);

		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
				
		return count;
	}
	
	public HashMap<String, String> getDevice(String deviceId) throws UserSysException {
		METHOD_NAME = "getDevice";
		
		HashMap<String, String> resultMap = null;
		try {
			HashMap<String, String> po = new HashMap<String,String>();
			po.put("deviceId", deviceId);
			
			resultMap = (HashMap<String,String>)getSqlMapClientTemplate().queryForObject(
					"device.info.get", po);

		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return resultMap;
		
	}
	
	public HashMap<String, Object> getDeviceMOList(String deviceId) throws UserSysException {
		METHOD_NAME = "getDevice";

		List resultList = null;
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			HashMap<String, String> po = new HashMap<String,String>();
			po.put("deviceId", deviceId);
			
			resultList = getSqlMapClientTemplate().queryForList("device.status.list", po);

	        ListIterator<HashMap> it = resultList.listIterator();
	        while(it.hasNext()){
	        	HashMap<String, String> mo = it.next();
	        	
	        	resultMap.put(mo.get("resourceUri"), mo);	        	
	        	
	        }
		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return resultMap;
		
	}
	
//HERE DEVICEMODATALIST
	public HashMap<String, Object> getDeviceMoDataList(HashMap<String, Object> po) throws UserSysException {
		METHOD_NAME = "getDeviceMoDataList";

		List resultList = null;
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			
			resultList = getSqlMapClientTemplate().queryForList("device.moData.list", po);

	        ListIterator<HashMap> it = resultList.listIterator();
	        while(it.hasNext()){
	        	HashMap<String, String> mo = it.next();
	        	
	        	resultMap.put(mo.get("resourceUri"), mo);	        	
	        	
	        }
		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return resultMap;
		
	}
	
//HERE MOBILE DEVICEMODATA UPDATE
	/*public HashMap<String, String> updateDeviceMoData(HashMap<String, Object> po) throws UserSysException {
		METHOD_NAME = "updateDeviceMoData";
		
		HashMap<String, String> resultMap = null;
		
		try {
			
			resultMap = (HashMap<String,String>)getSqlMapClientTemplate().queryForObject(
					"device.deviceMoData.update", po);
			
		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return resultMap;
	}*/
	
	
	
}