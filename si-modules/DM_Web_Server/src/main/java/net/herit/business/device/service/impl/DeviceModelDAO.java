package net.herit.business.device.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import net.herit.business.device.service.*;
import net.herit.common.dataaccess.*;
import net.herit.common.exception.BizException;
import net.herit.common.exception.UserSysException;
import net.herit.common.model.ProcessResult;

import org.springframework.stereotype.Repository;

import com.ibatis.sqlmap.client.SqlMapException;

@Repository("DeviceModelDAO")
public class DeviceModelDAO extends HeritHdpAbstractDAO {
	/** 클래스 명칭 */
	private final String CLASS_NAME = getClass().getName();
	/** 메소드명칭 */
	private String METHOD_NAME = "";
	
	/** 모델별 MO 정보 - key: <OUI>:<MODEL_NAME>, value:profileList */
	private HashMap<String, List<HashMap<String, Object>>> modelProfileMap = new HashMap<String, List<HashMap<String, Object>>>();


	public List<HashMap<String, String>> getDeviceModelList(HashMap<String, Object> po) throws UserSysException {
		METHOD_NAME = "getDeviceModelList";

		List resultList = null;

		try {
			resultList = getSqlMapClientTemplate().queryForList(
					"deviceModel.model.list", po);

		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return resultList;
	}

	public List<HashMap<String, Object>> getDeviceModelProfileList(String oui, String modelName, String uri) throws UserSysException {
		METHOD_NAME = "getDeviceModelProfileList";

		List<HashMap<String, Object>> profileList = null;

		try {
			profileList = (List)modelProfileMap.get(oui+":"+modelName);
			
			if (profileList == null) {
				HashMap<String, Object> po = new HashMap<String,Object>();
				po.put("oui", oui);
				po.put("modelName", modelName);
				
				List<HashMap<String, Object>> resultList = getSqlMapClientTemplate().queryForList(
						"deviceModel.profile.list", po);
				
		        ListIterator<HashMap<String, Object>> it = resultList.listIterator();
		        while(it.hasNext()){
		        	HashMap<String,Object> mo = it.next();
		        	String isError = (String)mo.get("isError");
		        	String hasOptionData = (String)mo.get("hasOptionData");
		        	String resourceUri = (String)mo.get("resourceUri");
		        	long moProfileId = (Long)mo.get("id");
					po.put("moProfileId", moProfileId);
		        	if (isError.equalsIgnoreCase("Y")) {
		        		mo.put("errorCodeList", getSqlMapClientTemplate().queryForList("deviceModel.errorCode.list", po));
		        	}
		        	if (hasOptionData.equalsIgnoreCase("Y")) {
		        		mo.put("optionDataList", getSqlMapClientTemplate().queryForList("deviceModel.optionData.list", po));
		        	}     	
		        	
		        }
		        profileList = resultList;
				
				modelProfileMap.put(oui+":"+modelName, profileList);
			}
			if (uri != null) {
				profileList = filterProfile(uri, profileList);
			}

		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return profileList;
	}
	
//HERE MOBILE DEVICEPROFILELIST	
	public List<HashMap<String, Object>> getDeviceProfileList(HashMap<String, Object> po) throws UserSysException {
		METHOD_NAME = "getDeviceProfileList";

		List resultList = null;

		try {
			resultList = getSqlMapClientTemplate().queryForList(
					"deviceModel.deviceProfile.list", po);

		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return resultList;
	}
	
	public HashMap<String, String> getDeviceModelInfoWithFirmwareId(String firmwareId) throws UserSysException {
		METHOD_NAME = "getDeviceModelInfoWithFirmwareId";
		
		HashMap<String, String> resultMap = null;
		try {
			HashMap<String, Object> po = new HashMap<String,Object>();
			//po.put("firmwareId", Integer.parseInt(firmwareId, 10));
			po.put("firmwareId", firmwareId);
			
			resultMap = (HashMap<String,String>)getSqlMapClientTemplate().queryForObject(
					"deviceModel.info.get.withFirmwareId", po);

		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return resultMap;
	}
	
	public HashMap<String, String> getDeviceModelInfo(int id) throws UserSysException {
		METHOD_NAME = "getDeviceModelInfo";
		
		HashMap<String, String> resultMap = null;
		try {
			HashMap<String, Object> po = new HashMap<String,Object>();
			//po.put("firmwareId", Integer.parseInt(firmwareId, 10));
			po.put("id", id);
			
			resultMap = (HashMap<String,String>)getSqlMapClientTemplate().queryForObject(
					"deviceModel.info.get", po);

		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return resultMap;
	}
	
	public HashMap<String, String> getDeviceModelInfo(String oui, String modelName) throws UserSysException {
		METHOD_NAME = "getDeviceModelInfo";
		
		HashMap<String, String> resultMap = null;
		try {
			HashMap<String, Object> po = new HashMap<String,Object>();
			po.put("oui", oui);
			po.put("modelName", modelName);
			
			resultMap = (HashMap<String,String>)getSqlMapClientTemplate().queryForObject(
					"deviceModel.info.get.withOuiModelName", po);

		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return resultMap;
	}
	//HERE 
	public HashMap<String, String> updateDeviceCount(String oui, String modelName) throws UserSysException {
		
		METHOD_NAME = "updateDeviceCount";
		
		HashMap<String, String> resultMap = null;
		
		try {
			HashMap<String, Object> po = new HashMap<String,Object>();
			po.put("oui", oui);
			po.put("modelName", modelName);
			
			resultMap = (HashMap<String,String>)getSqlMapClientTemplate().queryForObject(
					"deviceModel.info.update.deviceCount", po);
			
		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return resultMap;
	}
	
	public long getDeviceModelId(String oui, String modelName) throws UserSysException {
		METHOD_NAME = "getDeviceModelId";
		
		long modelId = -1;
		try {
			HashMap<String, Object> resultMap = null;
			
			HashMap<String, Object> po = new HashMap<String,Object>();
			po.put("oui", oui);
			po.put("modelName", modelName);
			
			resultMap = (HashMap<String,Object>)getSqlMapClientTemplate().queryForObject(
					"deviceModel.info.get.withOuiModelName", po);
			
			modelId = (Long)resultMap.get("id");			

		//} catch (SqlMapException ex) {
		} catch (Exception ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"디바이스 모델정보 쿼리 과정에서 에러가 발생했습니다.", ex);
		}
		return modelId;
	}
	

    public ProcessResult insertAlldeviceModel(Map<String, Object> paramMap) throws BizException, UserSysException {
    	METHOD_NAME = "insertAlldeviceModel";

    	ProcessResult result = null;
    	int retCode = -1;

		try {

			String oui = (String) paramMap.get("oui");
			String manufacturer = (String) paramMap.get("manufacturer");
			String modelName = (String) paramMap.get("modelName");
			String deviceType = (String) paramMap.get("deviceType");
			String iconUrl = (String) paramMap.get("iconUrl");
			String profileVer = (String) paramMap.get("profileVer");
			String description = (String) paramMap.get("description");
			String applyYn = (String) paramMap.get("applyYn");
			ArrayList<Map<String, Object>> jsonList = (ArrayList<Map<String, Object>>) paramMap.get("jsonList");

			DeviceModelVO deviceModelVO = new DeviceModelVO();
			deviceModelVO.setOui(oui);
			deviceModelVO.setManufacturer(manufacturer);
			deviceModelVO.setModelName(modelName);
			deviceModelVO.setDeviceType(deviceType);
			deviceModelVO.setIconUrl(iconUrl);
			deviceModelVO.setProfileVer(profileVer);
			deviceModelVO.setDescription(description);
			deviceModelVO.setApplyYn(applyYn);
			
			
			Object value = null;
			/*
			 * 디바이스 모델 등록!!!
			 */
			value = insert("deviceModel.info.register", deviceModelVO);
			if(value == null){
				retCode = 0;
			}
			if(retCode<0){
				throw new BizException(CLASS_NAME, METHOD_NAME, retCode, "디바이스 모델 등록시 에러가 발생했습니다.");
			}else{
				result = new ProcessResult(CLASS_NAME, METHOD_NAME, retCode, "정상적으로 등록 처리되었습니다.");
			}

    		//현재 AUTO_INCREMENT값
    		String getId = (String)getSqlMapClientTemplate().queryForObject("deviceModel.info.getId");
    		//System.out.println("getId = " + getId);

	    	for(int i=0; i<jsonList.size(); i++){
	    		//System.out.println(" i = " + jsonList.get(i));
	    		
	    		ArrayList deviceModelAttributeList = (ArrayList)jsonList.get(i);	    		
	    		MoProfileVO moProfileVO = new MoProfileVO();
	    		for(int j=0; j<deviceModelAttributeList.size(); j++){
	    			//System.out.println(deviceModelAttributeList.get(j));
	    			// ["DISPLAY_NAME", "RESOURCE_URI", "DATA_TYPE", "UNIT", "DEFAULT_VALUE", "OPERATION", "NOTI_TYPE", "IS_ERROR", "IS_DIAGNOSTIC", "IS_HISTORICAL", "IS_MANDATORY"]
	    			moProfileVO.setDeviceModelId(getId);
	    			moProfileVO.setProfileVer(profileVer);
	    			moProfileVO.setDisplayName(deviceModelAttributeList.get(0).toString());
	    			moProfileVO.setResourceUri(deviceModelAttributeList.get(1).toString());
	    			moProfileVO.setDataType(deviceModelAttributeList.get(2).toString());
	    			moProfileVO.setUnit(deviceModelAttributeList.get(3).toString());
	    			moProfileVO.setDefaultValue(deviceModelAttributeList.get(4).toString());
	    			moProfileVO.setOperation(deviceModelAttributeList.get(5).toString());
	    			moProfileVO.setNotiType(deviceModelAttributeList.get(6).toString());
	    			moProfileVO.setIsError(deviceModelAttributeList.get(7).toString());
	    			moProfileVO.setIsDiagnostic(deviceModelAttributeList.get(8).toString());
	    			moProfileVO.setIsHistorical(deviceModelAttributeList.get(9).toString());
	    			moProfileVO.setIsMandatory(deviceModelAttributeList.get(10).toString());
	    		}

	    		/*
	    		 * 관리객체 프로파일 등록!!!
	    		 */
				value = insert("deviceModel.moProfile.register", moProfileVO);
				if(value == null){
					retCode = 0;
				}
				if(retCode<0){
					throw new BizException(CLASS_NAME, METHOD_NAME, retCode, "관리객체 프로파일 등록시 에러가 발생했습니다.");
				}else{
					result = new ProcessResult(CLASS_NAME, METHOD_NAME, retCode, "정상적으로 등록 처리되었습니다.");
				}
	    		
	    	}


		} catch (SqlMapException ex) {
    		throw new UserSysException (CLASS_NAME, METHOD_NAME, "디바이스 모델 등록시 에러가 발생했습니다.", ex);
    	}

		return result;
    }
    
    public ProcessResult deleteAlldeviceModel(String checkList) throws BizException, UserSysException {
    	METHOD_NAME = "deleteOfferDeviceType";

    	ProcessResult result = null;
    	int retCode = -1;

		try {

			Object value = null;
			



	    	List moProfileList = null;
	    	try {
	    		moProfileList = list("deviceModel.moProfile.getMoProfileList", checkList);
	    	} catch (SqlMapException ex) {
	    		throw new UserSysException (CLASS_NAME, METHOD_NAME, "관리객체 프로파일 데이터 취득 처리에서 에러가 발생했습니다.", ex);
	    	}
	    	
	    	for(int i=0; i<moProfileList.size(); i++){
	    		Map<String, Object> mapMoProfileList = (Map<String, Object>)moProfileList.get(i);
	    			    		
				/*
				 * 옵션데이터 삭제
				 */
	    		value = delete("deviceModel.moProfile.optionDataDelete", mapMoProfileList);
				/*
				 * 에러코드 삭제
				 */
	    		value = delete("deviceModel.moProfile.errorDataDelete", mapMoProfileList);
				/*
				 * 노티조건 삭제
				 */
	    		value = delete("deviceModel.moProfile.notiConditionDelete", mapMoProfileList);
	    	}	    	
	    	
	    	
			
			/*
			 * 관리객체 프로파일 삭제
			 */
    		value = delete("deviceModel.moProfile.deleteAll", checkList);
    		
			/*
			 * 디바이스 모델 삭제
			 */
    		value = delete("deviceModel.info.deleteAll", checkList);
			if(Integer.parseInt(value.toString()) > 0){
    			retCode = 0;
    		}
    		if(retCode<0){
    			throw new BizException(CLASS_NAME, METHOD_NAME, retCode, "디바이스 모델 삭제시 에러가 발생했습니다.");
    		}else{
    			result = new ProcessResult(CLASS_NAME, METHOD_NAME, retCode, "정상적으로 삭제 처리되었습니다.");
    		}


		} catch (SqlMapException ex) {
    		throw new UserSysException (CLASS_NAME, METHOD_NAME, "디바이스 모델 삭제시 에러가 발생했습니다.", ex);
    	}

		return result;
    }
    

    public List<MoProfileVO> profileVerCodeList(String deviceModelId) throws UserSysException {
    	METHOD_NAME = "profileVerCodeList";
    	List resultList = null;

    	try {
    		resultList = list("deviceModel.moProfile.profileVerCodeList", deviceModelId);
    	} catch (SqlMapException ex) {
    		throw new UserSysException (CLASS_NAME, METHOD_NAME, "관리객체 프로파일 버전 데이터 취득 처리에서 에러가 발생했습니다.", ex);
    	}
    	return resultList;
    }    

    public List<MoProfileVO> moProfileList(Map<String, Object> paramMap) throws UserSysException {
    	METHOD_NAME = "moProfileList";
    	List resultList = null;

    	try {
    		resultList = list("deviceModel.moProfile.moProfileList", paramMap);
    	} catch (SqlMapException ex) {
    		throw new UserSysException (CLASS_NAME, METHOD_NAME, "관리객체 프로파일 데이터 취득 처리에서 에러가 발생했습니다.", ex);
    	}
    	return resultList;
    }    

	public Object deviceModelInfo(Map<String, Object> paramMap) throws UserSysException {
		METHOD_NAME = "get";

		HashMap<String, Object> result = null;
		try {
			result = (HashMap)getSqlMapClientTemplate().queryForObject("deviceModel.info.getDeviceModelInfo", paramMap);

		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME, "사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
				
		return result;
	}
    
    public ProcessResult deleteAllMoProfile(String id) throws BizException, UserSysException {
    	METHOD_NAME = "deleteOfferDeviceType";

    	ProcessResult result = null;
    	int retCode = -1;

    	String moProfileId = id;
    	
		try {

			Object value = null;
			

			/*
			 * 옵션데이터 삭제
			 */
    		value = delete("deviceModel.moProfile.optionDataDelete", moProfileId);
			/*
			 * 에러코드 삭제
			 */
    		value = delete("deviceModel.moProfile.errorDataDelete", moProfileId);
			/*
			 * 노티조건 삭제
			 */
    		value = delete("deviceModel.moProfile.notiConditionDelete", moProfileId);
			
			/*
			 * 관리객체 프로파일 삭제
			 */
    		value = delete("deviceModel.moProfile.delete", id);
    		


		} catch (SqlMapException ex) {
    		throw new UserSysException (CLASS_NAME, METHOD_NAME, "관리객체 삭제시 에러가 발생했습니다.", ex);
    	}

		return result;
    }
    

    public ProcessResult insertMoProfileCopyVersion(Map<String, Object> paramMap) throws BizException, UserSysException {
    	METHOD_NAME = "deleteOfferDeviceType";

    	ProcessResult result = null;
    	int retCode = -1;

    	
		try {

			Object value = null;
			

	    	List moProfileList = null;
	    	try {
	    		moProfileList = list("deviceModel.moProfile.moProfileList", paramMap);
	    	} catch (SqlMapException ex) {
	    		throw new UserSysException (CLASS_NAME, METHOD_NAME, "관리객체 프로파일 데이터 취득 처리에서 에러가 발생했습니다.", ex);
	    	}
	    	
	    	for(int i=0; i<moProfileList.size(); i++){
	    		Map<String, Object> mapMoProfileList = (Map<String, Object>)moProfileList.get(i);

	    		//new profileVer
	    		mapMoProfileList.put("profileVer", paramMap.get("newVersionSelector"));
	    		
	    		
	    		//관리객체 프로파일 등록
				value = insert("deviceModel.moProfile.insert", mapMoProfileList);
				if(value == null){
					retCode = 0;
				}
				if(retCode<0){
					throw new BizException(CLASS_NAME, METHOD_NAME, retCode, "관리객체 프로파일 등록시 에러가 발생했습니다.");
				}else{
					result = new ProcessResult(CLASS_NAME, METHOD_NAME, retCode, "정상적으로 등록 처리되었습니다.");
				}

	    		//현재 AUTO_INCREMENT값
	    		String getId = (String)getSqlMapClientTemplate().queryForObject("deviceModel.info.getId");
	    		
	    		
	    		//조회
		    	List optionDataList = null;
		    	try {
		    		optionDataList = list("deviceModel.moOptionData.list", mapMoProfileList);
		    	} catch (SqlMapException ex) {
		    		throw new UserSysException (CLASS_NAME, METHOD_NAME, "옵션 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		    	}		    	
		    	List errorCodeList = null;
		    	try {
		    		errorCodeList = list("deviceModel.moErrorCode.list", mapMoProfileList);
		    	} catch (SqlMapException ex) {
		    		throw new UserSysException (CLASS_NAME, METHOD_NAME, "에러코드 취득 처리에서 에러가 발생했습니다.", ex);
		    	}		  
		    	List notiConditionList = null;
		    	try {
		    		notiConditionList = list("deviceModel.moNotiCondition.list", mapMoProfileList);
		    	} catch (SqlMapException ex) {
		    		throw new UserSysException (CLASS_NAME, METHOD_NAME, "노티조건 취득 처리에서 에러가 발생했습니다.", ex);
		    	}	
		    	
		    	
		    	for(int j=0; j<optionDataList.size(); j++){
		    		Map<String, Object> mapOptionDataList = (Map<String, Object>)optionDataList.get(j);
			    	//신규로 등록된 moProfileId
		    		mapOptionDataList.put("moProfileId", getId);

					value = insert("deviceModel.moOptionData.insert", mapOptionDataList);
					if(value == null){
						retCode = 0;
					}
					if(retCode<0){
						throw new BizException(CLASS_NAME, METHOD_NAME, retCode, "옵션데이터 등록시 에러가 발생했습니다.");
					}else{
						result = new ProcessResult(CLASS_NAME, METHOD_NAME, retCode, "정상적으로 등록 처리되었습니다.");
					}
		    	}

	    		//에러코드  조회 후 등록  	
		    	for(int j=0; j<errorCodeList.size(); j++){
		    		Map<String, Object> mapErrorCodeList = (Map<String, Object>)errorCodeList.get(j);
			    	//신규로 등록된 moProfileId
		    		mapErrorCodeList.put("moProfileId", getId);

					value = insert("deviceModel.moErrorCode.insert", mapErrorCodeList);
					if(value == null){
						retCode = 0;
					}
					if(retCode<0){
						throw new BizException(CLASS_NAME, METHOD_NAME, retCode, "에러코드 등록시 에러가 발생했습니다.");
					}else{
						result = new ProcessResult(CLASS_NAME, METHOD_NAME, retCode, "정상적으로 등록 처리되었습니다.");
					}
		    	}

	    		//노티조건  조회 후 등록	    	
		    	for(int j=0; j<notiConditionList.size(); j++){
		    		Map<String, Object> mapNotiConditionList = (Map<String, Object>)notiConditionList.get(j);
			    	//신규로 등록된 moProfileId
		    		mapNotiConditionList.put("moProfileId", getId);

					value = insert("deviceModel.moNotiCondition.insert", mapNotiConditionList);
					if(value == null){
						retCode = 0;
					}
					if(retCode<0){
						throw new BizException(CLASS_NAME, METHOD_NAME, retCode, "노티조건 등록시 에러가 발생했습니다.");
					}else{
						result = new ProcessResult(CLASS_NAME, METHOD_NAME, retCode, "정상적으로 등록 처리되었습니다.");
					}
		    	}
	    		
	    	}
	    		    	
			


		} catch (SqlMapException ex) {
    		throw new UserSysException (CLASS_NAME, METHOD_NAME, "버전 추가시 에러가 발생했습니다.", ex);
    	}

		return result;
    }    
	
	private List<HashMap<String, Object>> filterProfile(String uri, List<HashMap<String, Object>> profileList) {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        ListIterator<HashMap<String, Object>> it = profileList.listIterator();
        while(it.hasNext()){
        	HashMap<String,Object> mo = it.next();
        	String resourceUri = (String)mo.get("resourceUri");
        	if (resourceUri.startsWith(uri)) {
        		list.add(mo);
        	}
        }
		return list;
	}


}


