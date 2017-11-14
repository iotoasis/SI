package net.herit.business.device.service;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.annotation.Resource;

import net.herit.business.firmware.service.*;
import net.herit.business.device.service.impl.*;
import net.herit.common.conf.HeritProperties;
import net.herit.common.exception.UserSysException;
import net.herit.common.util.PagingUtil;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service("DeviceService")
public class DeviceService {

    protected static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);

	@Resource(name = "DeviceDAO")
	private DeviceDAO deviceDAO;

	@Resource(name = "DeviceModelDAO")
	private DeviceModelDAO deviceModelDAO;
	
	@Resource(name = "FirmwareDAO")
	private FirmwareDAO firmwareDAO;

    public PagingUtil getDeviceListPaging(int nPage, int nSize, ParameterVO po) throws UserSysException {

    	HashMap<String, Object> param = new HashMap<String, Object>();
    	param.put("page", nPage);
    	//param.put("pageSize", nSize == 0 ? 10 : nSize);
    	//param.put("pageStartIndex", nSize == 0 ? 10 : nSize);

    	param.put("pageStartIndex", PagingUtil.getPaingStartIndex(nPage, ""));
    	param.put("pageSize", PagingUtil.getPageSize(""));
    	String sn = po.getSn();
    	String deviceModelId = po.getDeviceModel();
    	String oui = po.getOui();
    	String modelName = po.getModelName();
    	if (sn != null && sn.length() >0) {
    		param.put("snLike", sn+"%");
    	}
    	if (deviceModelId != null && deviceModelId.length() >0) {
    		param.put("id", deviceModelId);
    	}
    	if (oui != null && oui.length() >0) {
    		param.put("oui", oui);
    	}
    	if (modelName != null && modelName.length() >0) {
    		param.put("modelName", modelName);
    	}
    	
    	List<Object> list = (List<Object>)deviceDAO.getDeviceListPaging(param);

    	int nTotal = (Integer)deviceDAO.getDeviceCount(param);
    	
    	PagingUtil pageTable = null;
    	if(nTotal>0){
    		pageTable = new PagingUtil("", nTotal, nPage, po);
    		pageTable.setCurrList(list);
    	}

    	return pageTable;
    }


    public PagingUtil getDeviceFirmwareListPaging(int nPage, int nSize, Map<String, String[]> paramMap) throws UserSysException {

    	HashMap<String, Object> param = new HashMap<String, Object>();

		/*String deviceModel = po.getDeviceModel();
		String[] tokens = deviceModel.split("\\|");
		if (tokens.length == 2) {
			po.setOui(tokens[0]);
			po.setModelName(tokens[1]);
		}*/
    	String[] tokens;
    	String deviceModelId = "";
    	String firmwareId = "";
		String deviceModel = "";
		String oui = "";
		String modelName = "";
		String sn = "";
		String packageName = "";

		tokens = paramMap.get("firmwareId");
    	if (tokens != null && tokens.length > 0) {
    		firmwareId = tokens[0];
    		HashMap<String, String> resMap;
    		resMap = deviceModelDAO.getDeviceModelInfoWithFirmwareId(firmwareId);
    		oui = resMap.get("oui");
    		modelName = resMap.get("modelName");
    		
    		resMap = firmwareDAO.getFirmware(firmwareId);
    		packageName = resMap.get("package");
    		
    	}
		tokens = paramMap.get("sn");
    	if (tokens != null && tokens.length > 0) {
    		sn = tokens[0];
    	}
		
    	/*
    	tokens = paramMap.get("deviceModel");
    	if (tokens != null && tokens.length > 0) {
    		deviceModel = tokens[0];
    	}
		tokens = deviceModel.split("\\|");
		if (tokens != null && tokens.length == 2) {
			oui = tokens[0];
			modelName = tokens[1];
		}
		tokens = paramMap.get("package");
    	if (tokens != null && tokens.length > 0) {
    		packageName = tokens[0];
    	} 
    	*/
    	
				
    	param.put("page", nPage);
    	param.put("pageStartIndex", PagingUtil.getPaingStartIndex(nPage, ""));
    	param.put("pageSize", PagingUtil.getPageSize(""));
    	param.put("snLike", sn+"%");
    	param.put("oui", oui);
    	param.put("modelName", modelName);
    	param.put("package", packageName);
    	
    	List<Object> list = (List<Object>)deviceDAO.getDeviceFirmwareListPaging(param);

    	int nTotal = (Integer)deviceDAO.getDeviceFirmwareCount(param);
    	
    	PagingUtil pageTable = null;
    	if(nTotal>0){
    		pageTable = new PagingUtil("", nTotal, nPage, param);
    		pageTable.setCurrList(list);
    	}

    	return pageTable;
    }


    public HashMap<String, Object> getDeviceInfoTotal(String deviceId) throws UserSysException {

    	HashMap<String, Object> resultMap = new HashMap<String, Object>();
    	
    	int serviceMoId, serviceMoIdMax;
    	String serviceMo = HeritProperties.getProperty("Globals.ServiceMoId");
    	String serviceMoMax = HeritProperties.getProperty("Globals.ServiceMoIdMax");
    	try {
    		serviceMoId = Integer.parseInt(serviceMo);
    	} catch(Exception ex) {
    		serviceMoId = 99;
    	}
    	try {
    		serviceMoIdMax = Integer.parseInt(serviceMoMax);
    	} catch(Exception ex) {
    		serviceMoIdMax = 101;
    	}
    	
    	HashMap<String, String> deviceInfo = getDeviceInfo(deviceId);
    	HashMap<String, Object> moMap = deviceDAO.getDeviceMOList(deviceId);
    	List<HashMap<String, Object>> profileList = deviceModelDAO.getDeviceModelProfileList(deviceInfo.get("oui"), deviceInfo.get("modelName"), null);
    	List<HashMap<String, Object>> basicProfileList = deviceModelDAO.getDeviceModelProfileList(deviceInfo.get("oui"), deviceInfo.get("modelName"), "/2/");
    	List<HashMap<String, Object>> serviceProfileList = deviceModelDAO.getDeviceModelProfileList(deviceInfo.get("oui"), deviceInfo.get("modelName"), "/"+serviceMoId+"/");
    	List<HashMap<String, Object>> networkProfileList = deviceModelDAO.getDeviceModelProfileList(deviceInfo.get("oui"), deviceInfo.get("modelName"), "/3/");
    	HashMap<String, String> deviceModelInfo = deviceModelDAO.getDeviceModelInfo(deviceInfo.get("oui"), deviceInfo.get("modelName"));
    	//List<HashMap<String, Object>> deviceStatusHistList = historyDAO.getDeviceStatusHistList(moMap.get("resourceUri").toString(), moMap.get("resourceName").toString());

    	// 100~110번 오브젝트도 서비스 리소스로 포함
    	for (int i=serviceMoId+1; i<=serviceMoIdMax; i++) {
    		String moId = "/"+i+"/";
        	List<HashMap<String, Object>> serviceProfileListAdd = deviceModelDAO.getDeviceModelProfileList(deviceInfo.get("oui"), deviceInfo.get("modelName"), moId);
            ListIterator<HashMap<String, Object>> it = serviceProfileListAdd.listIterator();
            while(it.hasNext()){
            	HashMap<String,Object> mo = it.next();
            	serviceProfileList.add(mo);        	
            }    		
    	}
    	/*
        ListIterator<HashMap<String, Object>> it = serviceProfileList99.listIterator();
        while(it.hasNext()){
        	HashMap<String,Object> mo = it.next();
        	serviceProfileList.add(mo);        	
        }
    	 */

    	resultMap.put("deviceInfo", deviceInfo);
    	resultMap.put("moMap", moMap);
    	resultMap.put("profileList", profileList);
    	resultMap.put("basicProfileList", basicProfileList);
    	resultMap.put("serviceProfileList", serviceProfileList);
    	resultMap.put("networkProfileList", networkProfileList);
    	resultMap.put("deviceModelInfo", deviceModelInfo);
    	//resultMap.put("deviceStatusHistList", deviceStatusHistList);
    	
    	return resultMap;
    }
    
    public HashMap<String, String> getDeviceInfo(String deviceId) throws UserSysException {
    	
    	HashMap<String, String> info = deviceDAO.getDevice(deviceId);
    	
    	return info;
    }

    public List<HashMap<String,String>> getDeviceModelList(ParameterVO po) throws UserSysException {

    	HashMap<String, Object> param = new HashMap<String, Object>();
    	
    	List<HashMap<String,String>> list = deviceModelDAO.getDeviceModelList(param);
    	
    	return list;
    }
    
    // MSH-START
    public List<HashMap<String,String>> getDeviceModelListByDeviceType(String deviceType) throws UserSysException {

    	List<HashMap<String,String>> list = deviceModelDAO.getDeviceModelListByDeviceType(deviceType);
    	
    	return list;
    }
    // MSH-END


    public HashMap<String,String> getDeviceModelInfo(int id) throws UserSysException {

    	HashMap<String,String> info = deviceModelDAO.getDeviceModelInfo(id);
    	
    	return info;
    }
    
    public HashMap<String, String> getDeviceModelInfoWithFirmwareId(String firmwareId) throws UserSysException {


    	HashMap<String, Object> param = new HashMap<String, Object>();
    	
    	HashMap<String,String> deviceModelInfo = deviceModelDAO.getDeviceModelInfoWithFirmwareId(firmwareId);
    	
    	return deviceModelInfo;
    	
    }
}