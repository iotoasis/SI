package net.herit.business.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.herit.business.api.service.*;
import net.herit.business.device.service.DeviceModelVO;
import net.herit.business.device.service.DeviceVO;
import net.herit.business.device.service.MoProfileVO;
import net.herit.business.protocol.constant.Type;
import net.herit.common.dataaccess.*;
import net.herit.common.exception.UserSysException;
import net.herit.common.model.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import com.ibatis.sqlmap.client.SqlMapException;

@Repository("ApiHdmDAO")
public class ApiHdmDAO extends HeritHdmAbstractDAO {
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
	
	//추가
	public DeviceVO getDeviceInfo(String deviceId) throws UserSysException {
		METHOD_NAME = "getDeviceInfo";
		
		DeviceVO resultVO = null;
		try {
			HashMap<String, String> po = new HashMap<String, String>();
			po.put("deviceId", deviceId);
			
			System.out.println("11111111111111");
			resultVO = (DeviceVO)getSqlMapClientTemplate().queryForObject("DeviceDAO.device", po);
			System.out.println("22222222222222");
		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME, 
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return resultVO;
	}
	
	/*public HashMap<String, Object> getDeviceInfo(String deviceId) throws UserSysException {
		METHOD_NAME = "getDeviceInfo";
		
		HashMap<String, Object> resultMap = null;
		try {
			HashMap<String, String> po = new HashMap<String, String>();
			po.put("deviceId", deviceId);
			
			resultMap = (HashMap<String, Object>)getSqlMapClientTemplate().queryForObject(
					"DeviceDAO.deviceInfo", po);
			
		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME, 
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return resultMap;
	}*/
	
	
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
	 
	
	/****************************************************
	 * 	ETC Protocol
	 ****************************************************/
	public int getCountByAuthAccount(String deviceId, JSONObject token, Type type) throws UserSysException {
		METHOD_NAME = "getCountByAuthAccount";
		
		int result = 0;
		
		try {
			HashMap<String, String> po = new HashMap<String, String>();
			po.put("deviceId", deviceId);
			switch(type){
			case LWM2M:
				po.put("authId", token.getString("authId"));
				po.put("authPwd", token.getString("authPw"));
				break;
			case TR_069:
				po.put("authId", token.getString("Device.ManagementServer.Username"));
				po.put("authPwd", token.getString("Device.ManagementServer.Password"));
				break;
			}
			
			
			result = (Integer)getSqlMapClientTemplate().queryForObject(
					"device.get.count.by.authAccount", po);
			
		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME, 
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return result;
	}
	
	
	
	/*************************************************
	 *			lwm2m 
	 *************************************************/
	public int getCountByAuthAccount(JSONObject token) throws UserSysException {
		METHOD_NAME = "getCountByAuthAccount";
		
		int result = 0;
		
		try {
			HashMap<String, String> po = new HashMap<String, String>();
			po.put("deviceId", token.getString("deviceId"));
			po.put("authId", token.getString("authId"));
			po.put("authPwd", token.getString("authPw"));
			
			result = (Integer)getSqlMapClientTemplate().queryForObject(
					"device.get.count.by.authAccount", po);
			
		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME, 
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return result;
	}
	public int getCountByAuthAccount(String deviceId, String authId, String authPwd) throws UserSysException {
		METHOD_NAME = "getCountByAuthAccount";
		
		int result = 0;
		
		try {
			HashMap<String, String> po = new HashMap<String, String>();
			po.put("deviceId", deviceId);
			po.put("authId", authId);
			po.put("authPwd", authPwd);
			
			result = (Integer)getSqlMapClientTemplate().queryForObject(
					"device.get.count.by.authAccount", po);
			
		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME, 
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return result;
	}
	
	// device Resource 갯수 파악
	public int getResourceCountByDeviceId(String deviceId) throws UserSysException {
		METHOD_NAME = "getResourceCountByDeviceId";
		
		int result = 0;
		
		try {
			HashMap<String, String> po = new HashMap<String, String>();
			po.put("deviceId", deviceId);
			
			result = (Integer)getSqlMapClientTemplate().queryForObject(
					"deviceResource.get.count.by.deviceId", po);
			
		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME, 
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return result;
	}
	// device Resource 제거
	public boolean removeResourceByDeviceId(String deviceId) throws UserSysException {
		METHOD_NAME = "removeResourceByDeviceId";
		
		boolean result = false;
		
		try {
			HashMap<String, String> po = new HashMap<String, String>();
			po.put("device_id", deviceId);
			
			int rCode = delete("deviceResource.remove.by.deviceId", po);
			
			if(rCode > 0){
				result = true;
			}
			
		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME, 
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return result;
	}
	// hdm_device_mo_data테이블에 데이터 추가
	public boolean insertDeviceResources(String deviceId, List<MoProfileVO> unList) throws UserSysException {
		METHOD_NAME = "insertDeviceResources";
		
		boolean result = false;
		
		try {
			
			int rCode = 0;
			for(int i=0; i<unList.size(); i++){
				HashMap<String,String> po = new HashMap<String,String>();
				po.put("device_id", deviceId);
				po.put("resource_uri", unList.get(i).getResourceUri());
				po.put("resource_name", unList.get(i).getDisplayName());
				po.put("data", unList.get(i).getData());
				System.out.println(po.get("data"));
				rCode += (Integer)insert("device.insert.resource", po);
/*				if(unList.get(i).getData() != null){
					po.put("data", unList.get(i).getData());
					rCode += (Integer)insert("device.insert.resource.withData", po);
				} else {
				}*/
			}
			
			if(rCode == unList.size()){
				result = true;
			}
		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return result;
	}
	
	// hit_dev_inf_tbl테이블 deviceId로 조회한 갯수
	public int getDeviceConnStatusCount(String deviceId) throws UserSysException {
		METHOD_NAME = "getDeviceConnStatusCount";
		
		int result = 0;
		
		try {
			HashMap<String,String> po = new HashMap<String,String>();
			po.put("deviceId", deviceId);
			result = (Integer)getSqlMapClientTemplate().queryForObject("device.get.count.by.deviceId", po);
			
		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return result;
	}
	
	// hit_dev_inf_tbl테이블에 접속 데이터 추가
	public boolean insertDeviceConnStatus(String deviceId, String systemId) throws UserSysException {
		METHOD_NAME = "insertDeviceConnStatus";
		
		boolean result = false;
		
		try {
			
			HashMap<String,String> po = new HashMap<String,String>();
			po.put("deviceId",deviceId);
			po.put("status","1");
			po.put("systemId", systemId);
			
			int rCode = (Integer)insert("device.insert.connStatus", po);
			
			if(rCode == 1){
				result = true;
			}
			
		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return result;
	}
	
	// hit_dev_inf_tbl테이블에 접속 상태 업데이트
	public boolean updateDeviceConnStatus(String deviceId, String status) throws UserSysException {
		METHOD_NAME = "updateDeviceConnStatus";
		
		boolean result = false;
		
		try {
			
			HashMap<String,String> po = new HashMap<String,String>();
			po.put("device_id",deviceId);
			po.put("status",status);
			
			int rCode = (Integer)update("device.update.connStatus", po);
			
			if(rCode == 1){
				result = true;
			}
			
		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return result;
	}

	// hdm_device_mo_data 테이블에 데이터 update
	public boolean updateDeviceResourcesData(String deviceId, HashMap<String,Object> map) throws UserSysException {
		METHOD_NAME = "insertDeviceResources";
		
		boolean result = false;
		
		try {
			
			int rCode = 0;
			JSONArray arr = new JSONArray((String) map.get("body"));
			for(int i=0; i<arr.length(); i++){
				HashMap<String,String> resMap = new HashMap<String,String>();
				
				JSONObject body = arr.getJSONObject(i);
				resMap.put("value", body.getJSONObject("content").getString("value"));
				resMap.put("uri", body.getString("uri").replace("/0/", "/-/"));
				resMap.put("device_id", deviceId);
				rCode += (Integer)update("device.update.data", resMap);
				
				System.out.println(resMap.toString());
			}
			
			//rCode += (Integer)insert("device.insert.resource", po);
			if(rCode == arr.length()){
				result = true;
			}
		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return result;
	}
	
	
	/*********************************************
	 * 	TR-069
	 * ******************************************/
	
	public boolean updateDeviceResourcesData(String deviceId, JSONObject param) throws UserSysException {
		METHOD_NAME = "insertDeviceResources";
		
		boolean result = false;
		
		try {
			int rCode = 0;
			
			Iterator it = param.keys();
			while(it.hasNext()){
				
				HashMap<String,String> resMap = new HashMap<String,String>();
				String uri = (String)it.next();
				String value = param.getString(uri);
				
				if(!uri.equals("command")){
					resMap.put("value", value);
					resMap.put("uri", uri.replace(".", "/"));
					resMap.put("device_id", deviceId);
					update("device.update.data", resMap);
				}
			}
			
			
			//rCode += (Integer)insert("device.insert.resource", po);
			/*if(rCode == ){
				result = true;
			}*/
		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return result;
	}
	
	
	public ArrayList<MoProfileVO> getResources(String deviceId, List<String> paramList) throws UserSysException {
		METHOD_NAME = "getDeviceInfo";
		
		ArrayList<MoProfileVO> result = null;
		try {
			StringBuffer listToString = new StringBuffer();
			for(int i=0; i<paramList.size(); i++){
				System.out.println(paramList.get(i));
				listToString.append("'").append(paramList.get(i)).append("'");
				if(i < paramList.size()-1){
					listToString.append(", ");
				}
			}
			
			HashMap po = new HashMap();
			po.put("deviceId", deviceId);
			po.put("resource_uris", listToString.toString());
			
			result = (ArrayList<MoProfileVO>)getSqlMapClientTemplate().queryForList("device.status.list.by.uris", po);
		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME, 
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return result;
	}
	
	public String getHistoricalOption(String deviceId, String resourceUri, String operation) throws UserSysException {
		METHOD_NAME = "getDeviceInfo";
		
		String result = null;
		try {
			
			HashMap po = new HashMap();
			po.put("operation", operation);
			po.put("deviceId", deviceId);
			po.put("resourceUri", resourceUri);
			
			result = (String)getSqlMapClientTemplate().queryForObject("get.historical.option", po);
		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME, 
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return result;
	}
	
	public HashMap<String, String> getResourceInfoByDeviceId(String deviceId, String resourceUri) throws UserSysException {
		METHOD_NAME = "getDeviceInfo";
		
		HashMap<String, String> result = null;
		try {
			
			HashMap po = new HashMap();
			po.put("deviceId", deviceId);
			po.put("resourceUri", resourceUri);
			
			result = (HashMap<String, String>)getSqlMapClientTemplate().queryForObject("get.resource.info.by.id", po);
		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME, 
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return result;
	}
}
