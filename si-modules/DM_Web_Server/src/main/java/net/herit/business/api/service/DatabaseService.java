package net.herit.business.api.service;

import java.util.List;
import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.herit.business.api.ApiController;
import net.herit.business.api.service.*;
import net.herit.business.device.service.DeviceModelVO;
import net.herit.business.device.service.DeviceService;
import net.herit.business.device.service.MoProfileVO;
import net.herit.business.device.service.impl.DeviceModelDAO;
import net.herit.business.device.service.impl.DeviceDAO;
import net.herit.business.firmware.service.FirmwareDAO;
import net.herit.common.exception.BizException;
import net.herit.common.exception.UserSysException;
import net.herit.common.model.CodeVO;
import net.herit.common.model.ProcessResult;
import net.herit.common.util.PagingUtil;
import net.herit.common.util.SecurityUtil;
import net.herit.common.dataaccess.*;
import net.herit.common.conf.HeritProperties;
import net.herit.common.exception.*;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.apache.taglibs.standard.tag.common.core.ParamParent;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;


@Service("DatabaseService")
public class DatabaseService {

    protected static final Logger LOGGER = LoggerFactory.getLogger(DatabaseService.class);

	@Resource(name = "ApiHdmDAO")
	private ApiHdmDAO hdmDAO;
	@Resource(name = "ApiHdpDAO")
	private ApiHdpDAO hdpDAO;
	@Resource(name = "ApiHdhDAO")
	private ApiHdhDAO hdhDAO;
	@Resource(name = "DeviceModelDAO")
	private DeviceModelDAO deviceModelDAO;
	@Resource(name = "DeviceDAO")
	private DeviceDAO deviceDAO;
	@Resource(name = "FirmwareDAO")
	private FirmwareDAO firmwareDAO;
	
	public Map<String, Object> execute(String system, String domain, String data, String operation, Map<String, Object> param) throws Exception {
		
		Map<String, Object> content = new HashMap<String, Object>();
		
		HeritAbstractDAO dao = getDAO(system);
		
		if (operation.equalsIgnoreCase("list")) {
			if (param.get("limit") != null) {
				param.put("limit", Integer.parseInt((String)param.get("limit"), 10));
			}
			//System.out.println("DDDDDDDDDDDDDDDDDDDDDDDDDDD1");
			content.put("list", (ArrayList)dao.list(domain+"."+data+"."+operation, param));
			//System.out.println("DDDDDDDDDDDDDDDDDDDDDDDDDDD2");
		} else if (operation.equalsIgnoreCase("listPage")) {
			String page = (String)param.get("page");
			if (page == null || page.equals("")) {
				page = "1";
				param.put("page", page);
			}
			String listName = system+"."+domain+"."+data+"."+operation;
			param.put("pageStartIndex", PagingUtil.getPaingStartIndex(Integer.parseInt(page, 10), listName));
			param.put("pageSize", PagingUtil.getPageSize(listName));
			content.put("list", (ArrayList)dao.list(domain+"."+data+"."+operation, param));
			content.put("count", dao.select(domain+"."+data+".count", param));
		} else if (operation.equalsIgnoreCase("get")) {
			content.put("info", (Object)dao.select(domain+"."+data+"."+operation, param));
		} else if (operation.equalsIgnoreCase("set")) {
			if (param.get("loginPwd") != null && ((String)param.get("loginPwd")).length() != 0) {
				param.put("loginPwd", SecurityUtil.encryptWordpass((String)param.get("loginPwd")));
			}
			content.put("return", dao.update(domain+"."+data+"."+operation, param));
		} else if (operation.equalsIgnoreCase("insert")) {
			if (param.get("loginPwd") != null) {
				param.put("loginPwd", SecurityUtil.encryptWordpass((String)param.get("loginPwd")));
			}
			content.put("return", dao.insert(domain+"."+data+"."+operation, param));
		} else if (operation.equalsIgnoreCase("delete")) {
			content.put("return", dao.delete(domain+"."+data+"."+operation, param));
		} else {
			// 정형화 되지 않은 API 처리
			if ((system+"."+domain+"."+data+"."+operation).equalsIgnoreCase("hdp.deviceModel.info.profile")) {

				content = getDeviceModelProfile(content, param);
				
			} else if ((system+"."+domain+"."+data+"."+operation).equalsIgnoreCase("hdm.device.info.register")) {
				
				content = registerDevice(content, param);
				
			} else if ((system+"."+domain+"."+data+"."+operation).equalsIgnoreCase("hdm.device.info.oneM2MRegister")) {
				
				content = oneM2MRegister(content, param);
				
			} else if ((system+"."+domain+"."+data+"."+operation).equalsIgnoreCase("hdm.device.info.registers")) {
				
				content = registersDevice(content, param);
				
			} else if ((system+"."+domain+"."+data+"."+operation).equalsIgnoreCase("hdp.firmware.schedule.register")) {
				
				content = firmwareSchedule(content, param);
			} else if ((system+"."+domain+"."+data+"."+operation).equalsIgnoreCase("hdp.firmware.info.update")) {
				
				content = firmwareDetail(content, param);
			} else if ((system+"."+domain+"."+data+"."+operation).equalsIgnoreCase("hdp.deviceModel.info.deleteAll")) {				
				content = deleteAlldeviceModel(content, param);
			} else if ((system+"."+domain+"."+data+"."+operation).equalsIgnoreCase("hdp.deviceModel.moProfile.listAll")) {				
				content = moProfileList(content, param);
			} else if ((system+"."+domain+"."+data+"."+operation).equalsIgnoreCase("hdp.deviceModel.info.getInfo")) {				
				content = deviceModelInfo(content, param);
			} else if ((system+"."+domain+"."+data+"."+operation).equalsIgnoreCase("hdp.deviceModel.moProfile.deleteAll")) {				
				content = deleteAllMoProfile(content, param);
			} else if ((system+"."+domain+"."+data+"."+operation).equalsIgnoreCase("hdp.deviceModel.moProfile.copyVersionInsert")) {				
				content = insertMoProfileCopyVersion(content, param);
			} else if ((system+"."+domain+"."+data+"."+operation).equalsIgnoreCase("hdm.device.deviceMoData.update")) {				
				content = updateDeviceMoData(content, param);
			} else if ((system+"."+domain+"."+data+"."+operation).equalsIgnoreCase("hdp.deviceModel.info.insertAll")) {
				content = insertAlldeviceModel(content, param);			
			}
			/*else if ((system+"."+domain+"."+data+"."+operation).equalsIgnoreCase("hdp.deviceModel.deviceProfile.list")) {				
				content = getDeviceProfileList(content, param);
			} else if ((system+"."+domain+"."+data+"."+operation).equalsIgnoreCase("hdp.device.moData.list")) {				
				content = getDeviceMoDataList(content, param);
			}*/
		}
		return content;
	}

	/**
	 * json data execute
	 * @param system
	 * @param domain
	 * @param data
	 * @param operation
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
//	public Map<String, Object> execute(String system, String domain, String data, String operation, Map<String, Object> paramMap) throws Exception {
//
//		Map<String, Object> content = new HashMap<String, Object>();
//		
//		HeritAbstractDAO dao = getDAO(system);
//		
//
//		return content;
//	}
	
//HERE FIRMWARE VERSION FILEUPLOAD INSERT & UPDATE	
	public Map<String, Object> upload(String system, String domain, String data, Map<String, Object> param, List<FileUploadVO> fileUploadList) throws Exception {
		
		Map<String, Object> content = new HashMap<String, Object>();
		
		HeritAbstractDAO dao = getDAO(system);
		
		if ((system+"."+domain+"."+data+"."+"upload").equalsIgnoreCase("hdp.firmware.version.upload") || (system+"."+domain+"."+data+"."+"upload").equalsIgnoreCase("hdp.firmware.versionUpdate.upload")) {
			
			content = firmwareDetailUpload(content, param, fileUploadList);
		} else if ((system+"."+domain+"."+data+"."+"upload").equalsIgnoreCase("hdp.deviceModel.info.upload")) {

			content.put("fileUploadDir", param.get("fileUploadDir"));
			content.put("fileInfo", fileUploadList.get(0).getFilePhysName());
			//content.put("fileInfo", fileUploadList.get(0).getFileRealName());
			//content.put("return", dao.insert(domain+"."+data+"."+"register", param));
		}
		
		return content;
	}

	protected Map<String, Object> getDeviceModelProfile(Map<String, Object> content, Map<String, Object> param) throws Exception {
		
		String oui = (String)param.get("oui");
		String modelName = (String)param.get("modelName");
		if (oui != null && modelName != null) {
			content.put("profile", deviceModelDAO.getDeviceModelProfileList(oui, modelName, null));
		} else {
			throw new UserSysException("DatabaseService", "execute", ApiController.INVALID_PARAM, "Invalid Parameter:oui, modelName", null);
		}
		return content;
	}
	
//HERE DEVICE INSERT 
	protected Map<String, Object> registerDevice(Map<String, Object> content, Map<String, Object> param) throws Exception {
		
		String deviceModelId = (String)param.get("deviceModelId");
		String sn = (String)param.get("sn");
		String dmType = (String)param.get("dmType");
		String authId = (String)param.get("authId");
		String authPwd = (String)param.get("authPwd");
		
		HashMap<String, String> modelInfo = deviceModelDAO.getDeviceModelInfo(Integer.parseInt(deviceModelId, 10));
		if (modelInfo == null) {
			throw new UserSysException("DatabaseService", "execute", "101", "Invalid Parameter:deviceModelId", null);
		}
		String deviceId = modelInfo.get("oui")+"_"+modelInfo.get("modelName")+"_"+sn;
		if(deviceDAO.getDevice(deviceId) != null) {
			throw new UserSysException("DatabaseService", "execute", ApiController.DUPLICATED_DEVICE_ID, "Invalid Parameter:sn (duplicated deviceId)", null);
		}
		
		HashMap<String, String> regParam = new HashMap<String, String>();
		regParam.put("oui", modelInfo.get("oui"));
		regParam.put("manufacturer", modelInfo.get("manufacturer"));
		regParam.put("modelName", modelInfo.get("modelName"));
		regParam.put("deviceType", modelInfo.get("deviceType"));
		regParam.put("sn", sn);
		regParam.put("dmType", dmType);
		regParam.put("authId", authId);
		regParam.put("authPwd", authPwd);
		regParam.put("deviceId", deviceId);
		regParam.put("parentId", deviceId);

		content.put("return", hdmDAO.insert("device.info.register", regParam));
		
		return content;
	}
	
//oneM2M Device Register
	protected Map<String, Object> oneM2MRegister(Map<String, Object> content, Map<String, Object> param) throws Exception {
		
		String deviceModelId = (String)param.get("deviceModelId");
		String sn = (String)param.get("sn");
		String authId = (String)param.get("authId");
		String authPwd = (String)param.get("authPwd");
		String extDeviceId = (String)param.get("extDeviceId");
		
		HashMap<String, String> modelInfo = deviceModelDAO.getDeviceModelInfo(Integer.parseInt(deviceModelId, 10));
		if (modelInfo == null) {
			throw new UserSysException("DatabaseService", "execute", "101", "Invalid Parameter:deviceModelId", null);
		}
		String deviceId = modelInfo.get("oui")+"_"+modelInfo.get("modelName")+"_"+sn;
		if(deviceDAO.getDevice(deviceId) != null) {
			throw new UserSysException("DatabaseService", "execute", ApiController.DUPLICATED_DEVICE_ID, "Invalid Parameter:sn (duplicated deviceId)", null);
		}
		
		HashMap<String, String> regParam = new HashMap<String, String>();
		regParam.put("oui", modelInfo.get("oui"));
		regParam.put("manufacturer", modelInfo.get("manufacturer"));
		regParam.put("modelName", modelInfo.get("modelName"));
		regParam.put("deviceType", modelInfo.get("deviceType"));
		regParam.put("sn", sn);
		regParam.put("authId", authId);
		regParam.put("authPwd", authPwd);
		regParam.put("deviceId", deviceId);
		regParam.put("parentId", deviceId);
		regParam.put("extDeviceId", extDeviceId);

		content.put("return", hdmDAO.insert("device.info.oneM2MRegister", regParam));
		
		return content;
	}	
	
//HERE DEVICE FILE INSERT
	protected Map<String, Object> registersDevice(Map<String, Object> content, Map<String, Object> param) throws Exception {
		
		String oui = "";
		String modelName = "";
		String sn = "";
		String authId = "";
		String authPwd = "";
	
		for (String key : param.keySet()) {
			
			//System.out.println("key="+key+", value="+param.get(key));
		}
		String jsonString = (String) param.get("deviceRegInfo");
		
		ArrayList<HashMap<String, String>> str = new ArrayList<HashMap<String, String>>();
		
		ObjectMapper mapper = new ObjectMapper();
		
		str = mapper.readValue(jsonString, new TypeReference<ArrayList<HashMap<String, String>>>(){});
		
		for (HashMap<String, String> innerValueMap: str) {
			
			oui = innerValueMap.get("oui");
			modelName = innerValueMap.get("modelName");
			sn = innerValueMap.get("sn");
			authId = innerValueMap.get("authId");
			authPwd = innerValueMap.get("authPwd");
			
			HashMap<String, String> modelInfo = deviceModelDAO.getDeviceModelInfo(oui, modelName);
			////System.out.println("modelInfo : " + modelInfo);
			if (modelInfo == null) {
				throw new UserSysException("DatabaseService", "execute", "101", "Invalid Parameter:oui, modelName", null);
			}
			
			String deviceId = modelInfo.get("oui")+"_"+modelInfo.get("modelName")+"_"+sn;
			////System.out.println("deviceId : " + deviceId);
			if(deviceDAO.getDevice(deviceId) != null) {
				throw new UserSysException("DatabaseService", "execute", ApiController.DUPLICATED_DEVICE_ID, "Invalid Parameter:sn (duplicated deviceId)", null);
			}
			
			HashMap<String, String> regParam = new HashMap<String, String>();
			regParam.put("oui", modelInfo.get("oui"));
			regParam.put("manufacturer", modelInfo.get("manufacturer"));
			regParam.put("modelName", modelInfo.get("modelName"));
			regParam.put("deviceType", modelInfo.get("deviceType"));
			regParam.put("sn", sn);
			regParam.put("authId", authId);
			regParam.put("authPwd", authPwd);
			regParam.put("deviceId", deviceId);
			regParam.put("parentId", deviceId);
			
			content.put("return", hdmDAO.insert("device.info.register", regParam));
			content.put("return", hdpDAO.update("deviceModel.info.update.deviceCount", regParam));
		}
		content.clear();
		return null;
	}

//HERE FIRMWARE SCHEDULE INSERT & UPDATE
	protected Map<String, Object> firmwareSchedule(Map<String, Object> content, Map<String, Object> param) throws Exception {
		
		String groupId = (String) param.get("groupId");
		String groupType = (String) param.get("groupType");
		String firmwareId = (String) param.get("firmwareId");
		String packageName = (String) param.get("package");
		String version = (String) param.get("version");
		String scheduleTime = (String) param.get("scheduleTime");
		
		HashMap<String, String> firmwareInfo = firmwareDAO.getFirmwareUpdate(firmwareId, groupType);
		////System.out.println("DB firmwareId : " + String.valueOf(firmwareInfo.get("firmwareId")));
		
		
		HashMap<String, String> regParam = new HashMap<String, String>();
		
		if (firmwareInfo == null) {
			//INSERT
			regParam.put("version", version);
			regParam.put("firmwareId", firmwareId);
			regParam.put("package", packageName);
			regParam.put("groupType", groupType);
			regParam.put("groupId", groupId);
			regParam.put("scheduleTime", scheduleTime);
			
			content.put("return", hdpDAO.insert("firmware.schedule.register", regParam));
			
		} else {
			//UPDATE
			regParam.put("firmwareId", firmwareId);
			regParam.put("groupType", groupType);
			regParam.put("version", version);
			regParam.put("scheduleTime", scheduleTime);
			
			content.put("return", hdpDAO.update("firmware.schedule.update", regParam));
		}
			
		return content;
	}
	
	
	
//HERE FIRMWARE DETAIL BASICINFO & VERSION 
	protected Map<String, Object> firmwareDetail(Map<String, Object> content, Map<String, Object> param) throws Exception {
		
		//모델기본정보업데이트
		String id = (String) param.get("id");
		String packageName = (String) param.get("package");
		String description = (String) param.get("description");
		//System.out.println("id : " + id + " packageName : " + packageName + " description : " + description );
		
		HashMap<String, String> regParam = new HashMap<String, String>();
		
		regParam.put("id", id);
		regParam.put("package", packageName);
		regParam.put("description", description);
		
		content.put("return", hdpDAO.update("firmware.info.update", regParam));
		
		
		//펌웨어버전정보삭제
		String firmwareId = (String) param.get("firmwareId");
		String version = (String) param.get("version");
		
		HashMap<String, String> regParam2 = new HashMap<String, String>();
		
		regParam2.put("firmwareId", firmwareId);
		regParam2.put("version", version);
		
		content.put("return", hdpDAO.delete("firmware.version.delete",regParam2));
		//content.put("return", hdpDAO.select("firmware.version.list", regParam2));
		
		return content;
	}

//HERE MOBILE DEVICEMODATA UPDATE
	protected Map<String, Object> updateDeviceMoData(Map<String, Object> content, Map<String, Object> param) throws Exception {
			
			String deviceId = (String) param.get("deviceId");
			String resourceUri = (String) param.get("resourceUri");
			String resourceName = (String) param.get("resourceName");
			String data = (String)param.get("data");
			//System.out.println("deviceId : " + deviceId + " resourceUri : " + resourceUri + " resourceName : " + resourceName + " data : " + data);
			
			HashMap<String, String> regParam = new HashMap<String, String>();
			
			regParam.put("deviceId", deviceId);
			regParam.put("resourceUri", resourceUri);
			regParam.put("resourceName", resourceName);
			regParam.put("data", data);
			
			content.put("return", hdmDAO.update("device.deviceMoData.update", regParam));
			
			return content;
		}
	
//HERE FIRMWARE DETAIL VERSION UPLOAD (+FILEUPLOAD)
	protected Map<String, Object> firmwareDetailUpload(Map<String, Object> content, Map<String, Object> param, List<FileUploadVO> fileUploadList) throws Exception {
		
		String firmwareId = (String) param.get("firmwareId");
		String packageName = (String) param.get("packageName");
		String version = (String) param.get("version");
		String inversion = (String) param.get("firmware_inversion");
		String inchecksum = (String) param.get("firmware_incheckSum");
		String upversion = (String) param.get("firmware_upversion");
		String upchecksum = (String) param.get("firmware_upcheckSum");
		String fileName = fileUploadList.get(0).getFileRealName();
		String fileUrl = "{file_url}/"+packageName+"/"+fileName;
		String fileSize = fileUploadList.get(0).getFileSize();
		
		//System.out.println("fileName : " + fileName + " fileUrl : " + fileUrl + " fileSize : " + fileSize);
		//System.out.println("firmwareId : " + firmwareId + " packageName : " + packageName + " inversion : " + inversion + " inchecksum : " + inchecksum);
		//System.err.println("packageName : " + packageName + " upversion : " + upversion + " upchecksum : " + upchecksum);
		
		////System.out.println("fileUploadList : " + fileUploadList.get(0).getFileRealName());
		HashMap<String, String> firmwareVersionInfo = firmwareDAO.getFirmwareVersion(firmwareId, version);
		
		HashMap<String, String> regParam = new HashMap<String, String>();
		
		if (firmwareVersionInfo == null) {
			//VERSION INSERT
			regParam.put("firmwareId", firmwareId);
			regParam.put("version", inversion);
			regParam.put("checksum", inchecksum);
			regParam.put("fileUrl", fileUrl);
			regParam.put("fileSize", fileSize);
			
			content.put("return", hdpDAO.insert("firmware.version.upload", regParam));
			//System.out.println("FIRMWAREVERSION INSERT");
		} else {
			
			//System.out.println("myFileName: "+fileName);
			//VERSION UPDATE
			if (!fileName.isEmpty()) {
				//System.out.println("myFileName is NOT Empty");
				regParam.put("firmwareId", firmwareId);
				regParam.put("version", version);
				regParam.put("upversion", upversion);
				regParam.put("checksum", upchecksum);
				regParam.put("fileUrl", fileUrl);
				regParam.put("fileSize", fileSize);
				
				content.put("return", hdpDAO.update("firmware.versionUpdate.upload", regParam));
			} else {
				//System.out.println("myFileName is Empty");
				regParam.put("firmwareId", firmwareId);
				regParam.put("version", version);
				regParam.put("upversion", upversion);
				regParam.put("checksum", upchecksum);
				
				content.put("return", hdpDAO.update("firmware.versionUpdate.upload", regParam));
			}
			
			//System.out.println("FIRMWAREVERSION UPDATE");
		}
		
		return content;
	}
	
	

	protected Map<String, Object> insertAlldeviceModel(Map<String, Object> content, Map<String, Object> paramMap) throws Exception {

		String oui = (String) paramMap.get("oui");

		if (oui != null) {
			content.put("return", deviceModelDAO.insertAlldeviceModel(paramMap));
			
		}else{
			throw new UserSysException("DatabaseService", "execute", ApiController.INVALID_PARAM, "Invalid Parameter:oui", null);
		}	

		return content;
	}
	
//HERE MOBILE DEVICEPROFILELIST (통계부분)	
	/*protected HashMap<String, Object> getDeviceProfileList(HashMap<String, Object> content, HashMap<String, Object> paramMap) throws Exception {
		
		String id = (String)paramMap.get("id");
		String oui = (String)paramMap.get("oui");
		String modelName = (String)paramMap.get("modelName");
		//System.out.println("paramMap data : " + id + oui + modelName);
		
		if (oui != null && modelName != null) {
			content.put("deviceProfileList", deviceModelDAO.getDeviceProfileList(paramMap));
		} else {
			throw new UserSysException("DatabaseService", "execute", ApiController.INVALID_PARAM, "Invalid Parameter:id", null);
		}
		return content;
	}*/

//HERE MOBILE DEVICEMODATALIST & CHECKDEVICECONTROL (리스트부분)
	/*protected HashMap<String, Object> getDeviceMoDataList(HashMap<String, Object> content, HashMap<String, Object> paramMap) throws Exception {
			
			String resourceUri = (String)paramMap.get("resourceUri");
			String resourceName = (String)paramMap.get("resourceName");

			//System.out.println("paramMap data : " + resourceUri + resourceName);
			
			if (resourceUri != null) {
				content.put("deviceProfileList", deviceDAO.getDeviceMoDataList(paramMap));
			} else {
				throw new UserSysException("DatabaseService", "execute", ApiController.INVALID_PARAM, "Invalid Parameter:resourceUri", null);
			}
			return null;
		}*/
	
	protected Map<String, Object> deleteAlldeviceModel(Map<String, Object> content, Map<String, Object> param) throws Exception {
		
		String checkList = (String)param.get("checkList");
		if (checkList != null) {
			content.put("return", deviceModelDAO.deleteAlldeviceModel(checkList));
		} else {
			throw new UserSysException("DatabaseService", "execute", ApiController.INVALID_PARAM, "Invalid Parameter:checkList", null);
		}
		return content;
	}

	protected Map<String, Object> moProfileList(Map<String, Object> content, Map<String, Object> paramMap) throws Exception {
		
		String deviceModelId = (String)paramMap.get("deviceModelId");
		if (deviceModelId != null) {
			content.put("moProfileList", deviceModelDAO.moProfileList(paramMap));
		} else {
			throw new UserSysException("DatabaseService", "execute", ApiController.INVALID_PARAM, "Invalid Parameter:deviceModelId", null);
		}
		return content;
	}
	protected Map<String, Object> deviceModelInfo(Map<String, Object> content, Map<String, Object> paramMap) throws Exception {
		
		String id = (String)paramMap.get("id");
		if (id != null) {
			content.put("profileVerCodeList", deviceModelDAO.profileVerCodeList(id));
			content.put("info", deviceModelDAO.deviceModelInfo(paramMap));
		} else {
			throw new UserSysException("DatabaseService", "execute", ApiController.INVALID_PARAM, "Invalid Parameter:id", null);
		}
		return content;
	}
	

	protected Map<String, Object> deleteAllMoProfile(Map<String, Object> content, Map<String, Object> param) throws Exception {
		
		String id = (String)param.get("id");
		if (id != null) {
			content.put("return", deviceModelDAO.deleteAllMoProfile(id));
		} else {
			throw new UserSysException("DatabaseService", "execute", ApiController.INVALID_PARAM, "Invalid Parameter:id", null);
		}
		return content;
	}

	
	protected Map<String, Object> insertMoProfileCopyVersion(Map<String, Object> content, Map<String, Object> param) throws Exception {
		
		String deviceModelId = (String)param.get("deviceModelId");
		if (deviceModelId != null) {
			content.put("return", deviceModelDAO.insertMoProfileCopyVersion(param));
		} else {
			throw new UserSysException("DatabaseService", "execute", ApiController.INVALID_PARAM, "Invalid Parameter:deviceModelId", null);
		}
		return content;
	}

	
	protected HeritAbstractDAO getDAO(String system) {
		
		if (system.equalsIgnoreCase("hdp")) {
			return hdpDAO;
		} else if (system.equalsIgnoreCase("hdm")) {
			return hdmDAO;
		} else {
			return hdhDAO;
		}  
	}
}