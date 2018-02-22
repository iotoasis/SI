package net.herit.business.firmware.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import net.herit.business.device.service.*;
import net.herit.common.dataaccess.HeritHdpAbstractDAO;
import net.herit.common.exception.UserSysException;

import org.springframework.stereotype.Repository;

import com.ibatis.sqlmap.client.SqlMapException;

@Repository("FirmwareDAO")
public class FirmwareDAO extends HeritHdpAbstractDAO {
	/** 클래스 명칭 */
	private final String CLASS_NAME = getClass().getName();
	/** 메소드명칭 */
	private String METHOD_NAME = "";

	public List<Object> getFirmwareListPaging(HashMap<String, Object> po) throws UserSysException {
		METHOD_NAME = "getFirmwareListPaging";

		List resultList = null;

		try {
			//resultList = getSqlMapClientTemplate().queryForList(
			//		"DeviceDAO.deviceList", po);
			resultList = getSqlMapClientTemplate().queryForList(
					"firmware.info.list", po);

		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return resultList;
	}


	public List<HashMap<String,String>> getFirmwareList(HashMap<String, Object> po) throws UserSysException {
		METHOD_NAME = "getFirmwareList";

		List resultList = null;

		try {
			resultList = getSqlMapClientTemplate().queryForList(
					"firmware.info.list", po);

		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return resultList;
	}
	

	public List<HashMap<String,String>> getFirmwareVersionList(HashMap<String, Object> po) throws UserSysException {
		METHOD_NAME = "getFirmwareVersionList";

		List resultList = null;

		try {
			//resultList = getSqlMapClientTemplate().queryForList(
			//		"DeviceDAO.deviceList", po);
			resultList = getSqlMapClientTemplate().queryForList(
					"firmware.version.list", po);

		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return resultList;
	}
	
//HERE FIRMWAREUPDATELIST 
	public List<HashMap<String, String>> getFirmwareUpdateList(HashMap<String, Object> po) throws UserSysException {
		METHOD_NAME = "getFirmwareUpdateList";
		
		List resultList = null;
		
		try {
			resultList = getSqlMapClientTemplate().queryForList(
					"firmware.firmwareUpdate.list", po);
			
		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return resultList;
	}
	
//HERE FIRMWAREVERSION (FIRMWARE_ID)
	public HashMap<String, String> getFirmwareVersion(String firmwareId, String version) throws UserSysException {
		METHOD_NAME = "getFirmwareVersion";
		
		HashMap<String, String> resultMap = null;
		try {
			HashMap<String, Object> po = new HashMap<String,Object>();
			//po.put("deviceId", Integer.parseInt(deviceModelId,10));
			po.put("firmwareId", firmwareId);
			po.put("version", version);
			
			resultMap = (HashMap<String,String>)getSqlMapClientTemplate().queryForObject(
					"firmware.version.get", po);

		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return resultMap;
		
	}
	
	public int getFirmwareCount(HashMap<String, Object> po) throws UserSysException {
		METHOD_NAME = "getFirmwareCount";

		int count = 0;
		try {
			count = (Integer)getSqlMapClientTemplate().queryForObject(
					"firmware.info.count", po);

		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
				
		return count;
	}
	
	public HashMap<String, String> getFirmware(String firmwareId) throws UserSysException {
		METHOD_NAME = "getFirmware";
		
		HashMap<String, String> resultMap = null;
		try {
			HashMap<String, Object> po = new HashMap<String,Object>();
			//po.put("deviceId", Integer.parseInt(deviceModelId,10));
			po.put("firmwareId", firmwareId);
			
			resultMap = (HashMap<String,String>)getSqlMapClientTemplate().queryForObject(
					"firmware.info.get", po);

		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return resultMap;
		
	}
	
	//HERE5
	public HashMap<String, String> updateFirmware(int firmwareId) throws UserSysException {
		
		METHOD_NAME = "updateFirmware";
		
		HashMap<String, String> resultMap = null;
		
		try {
			HashMap<String, Object> po = new HashMap<String,Object>();
			po.put("firmwareId", firmwareId);
			
			resultMap = (HashMap<String,String>)getSqlMapClientTemplate().queryForObject(
					"firmware.schedule.update", po);
			
		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return resultMap;
	}
	
	//HERE6
	public HashMap<String, String> getFirmwareUpdate(String firmwareId, String groupType) throws UserSysException {
		METHOD_NAME = "getFirmwareUpdate";
		
		HashMap<String, String> resultMap = null;
		try {
			HashMap<String, Object> po = new HashMap<String,Object>();
			po.put("firmwareId", firmwareId);
			po.put("groupType", groupType);
			
			resultMap = (HashMap<String,String>)getSqlMapClientTemplate().queryForObject(
					"firmware.schedule.get", po);

		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return resultMap;
		
	}
	
	// MSH-START TR-069 Firmware upload
	public int getFirmwareCount(int dmId) throws UserSysException {
		
		METHOD_NAME = "getFirmwareCount";
		
		int result = 0;
		System.out.println(dmId);
		try {
			HashMap<String, Object> po = new HashMap<String,Object>();
			po.put("deviceModelId", dmId);
			
			result = (Integer) getSqlMapClientTemplate().queryForObject("firmware.info.count", po);
		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return result;
	}
	
	public int getFirmwareVersionCount(int fwId, String version) throws UserSysException {
		
		METHOD_NAME = "getFirmwareCount";
		
		int result = 0;
		System.out.println(fwId+"/"+version);
		try {
			HashMap<String, Object> po = new HashMap<String,Object>();
			po.put("firmwareId", fwId);
			po.put("version", version);
			
			result = (Integer) getSqlMapClientTemplate().queryForObject("firmware.version.count", po);
		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return result;
	}
	
	public int getFirmwareId(int deviceModelId, int firmwareType) throws UserSysException {
		
		METHOD_NAME = "getFirmwareCount";
		
		int result = 0;
		System.out.println(deviceModelId);
		try {
			HashMap<String, Object> po = new HashMap<String,Object>();
			po.put("deviceModelId", deviceModelId);
			po.put("firmwareType", firmwareType);
			
			result = (Integer) getSqlMapClientTemplate().queryForObject("firmware.id", po);
		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return result;
	}
	
	public int addFirmwareInfo(int dmId, String fileName, int fileType, String description) throws UserSysException {
		
		METHOD_NAME = "addFirmwareInfo";
		
		int result = 0;
		System.out.println(dmId+"/"+fileName+"/"+fileType+"/"+description);
		try {
			HashMap<String, Object> po = new HashMap<String,Object>();
			po.put("device_model_id", dmId);
			po.put("package", fileName);
			po.put("firmware_type", fileType);
			po.put("description", description);
			
			result = (Integer) getSqlMapClientTemplate().insert("firmware.upload", po);
		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return result;
	}
	
	public int addFirmwareVersion(int firmwareId, String version, long fileSize) throws UserSysException {
		
		METHOD_NAME = "addFirmwareVersion";
		
		int result = 0;
		System.out.println(firmwareId+"/"+version+"/"+fileSize);
		try {
			HashMap<String, Object> po = new HashMap<String,Object>();
			
			po.put("firmwareId", firmwareId);
			po.put("version", version);
			po.put("checksum", "checksum_tbd");
			po.put("fileUrl", "");
			po.put("fileSize", fileSize);
			
			getSqlMapClientTemplate().insert("firmware.version.upload", po);
		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return result;
	}
	
	
	// MSH-END
}
