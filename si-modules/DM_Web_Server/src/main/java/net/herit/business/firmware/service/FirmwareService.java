package net.herit.business.firmware.service;

import java.util.List;

import javax.annotation.Resource;

import net.herit.business.device.service.*;
import net.herit.business.device.service.impl.*;
import net.herit.common.exception.BizException;
import net.herit.common.exception.UserSysException;
import net.herit.common.model.CodeVO;
import net.herit.common.model.ProcessResult;
import net.herit.common.util.PagingUtil;
import net.herit.common.util.SecurityUtil;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service("FirmwareService")
public class FirmwareService {

    protected static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);

	@Resource(name = "FirmwareDAO")
	private FirmwareDAO firmwareDAO;

	public List<HashMap<String,String>> getFirmwareListWithDeviceModelId(String deviceModelId)throws UserSysException {

    	HashMap<String, Object> param = new HashMap<String, Object>();
    	param.put("deviceModelId", deviceModelId);
    	param.put("pageStartIndex", 0);
    	param.put("pageSize", 100);
    	
    	List<HashMap<String,String>> list = firmwareDAO.getFirmwareList(param);

    	return list;
    }

    public PagingUtil getFirmwareListPaging(int nPage, int nSize, ParameterVO po) throws UserSysException {

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
    		param.put("deviceModelId", deviceModelId);
    	}
    	if (oui != null && oui.length() >0) {
    		param.put("oui", oui);
    	}
    	if (modelName != null && modelName.length() >0) {
    		param.put("modelName", modelName);
    	}
    	
    	List<Object> list = (List<Object>)firmwareDAO.getFirmwareListPaging(param);

    	int nTotal = (Integer)firmwareDAO.getFirmwareCount(param);
    	
    	PagingUtil pageTable = null;
    	if(nTotal>0){
    		pageTable = new PagingUtil("", nTotal, nPage, po);
    		pageTable.setCurrList(list);
    	}

    	return pageTable;
    }

    public List<HashMap<String,String>> getFirmwareVersionList(String firmwareId) throws UserSysException {

    	HashMap<String, Object> param = new HashMap<String, Object>();

    	//param.put("firmwareId", Integer.parseInt(firmwareId,10));
    	param.put("firmwareId", firmwareId);
    	
    	List<HashMap<String,String>> verList = firmwareDAO.getFirmwareVersionList(param);
    	
    	return verList;
    }
    
    public HashMap<String, String> getFirmwareInfo(String firmwareId) throws UserSysException {
    	
    	HashMap<String, String> info = firmwareDAO.getFirmware(firmwareId);
    	
    	return info;
    }

//HERE FIRMWAREUPDATELIST
    public List<HashMap<String, String>> getFirmwareUpdateList(ParameterVO po) throws UserSysException {
    	
    	HashMap<String, Object> param = new HashMap<String, Object>();
    	
    	List<HashMap<String,String>> list = firmwareDAO.getFirmwareUpdateList(param);
    	
    	return list;
    }

}
