package net.herit.moblie.business.device.serivce;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import net.herit.business.device.service.ParameterVO;
import net.herit.business.device.service.impl.DeviceModelDAO;
import net.herit.common.exception.UserSysException;

import org.springframework.stereotype.Service;

@Service("MobileDeviceService")
public class MobileDeviceService {

	@Resource(name = "DeviceModelDAO")
	private DeviceModelDAO deviceModelDAO;
	
	public List<HashMap<String,String>> getDeviceModelList(ParameterVO po) throws UserSysException {

    	HashMap<String, Object> param = new HashMap<String, Object>();
    	
    	List<HashMap<String, String>> list = deviceModelDAO.getDeviceModelList(param);
    	
    	return list;
    }
}
