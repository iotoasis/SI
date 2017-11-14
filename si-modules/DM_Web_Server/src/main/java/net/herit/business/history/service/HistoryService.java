package net.herit.business.history.service;

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


@Service("HistoryService")
public class HistoryService {

    protected static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);

	@Resource(name = "HistoryDAO")
	private HistoryDAO historyDAO;

    public PagingUtil getErrorListPaging(int nPage, int nSize, String deviceId) throws UserSysException {

    	HashMap<String, Object> param = new HashMap<String, Object>();
    	param.put("page", nPage);
    	//param.put("pageSize", nSize == 0 ? 10 : nSize);
    	//param.put("pageStartIndex", nSize == 0 ? 10 : nSize);

    	param.put("pageStartIndex", PagingUtil.getPaingStartIndex(nPage, ""));
    	param.put("pageSize", PagingUtil.getPageSize(""));
    	
    	//List<Object> list = (List<Object>)historyDAO.getErrorListPaging(param);

    	//int nTotal = (Integer)firmwareDAO.getFirmwareCount(null);
    	
    	PagingUtil pageTable = null;
    	/*if(nTotal>0){
    		pageTable = new PagingUtil("", nTotal, nPage, deviceId);
    		pageTable.setCurrList(list);
    	}*/

    	return pageTable;
    }




}
