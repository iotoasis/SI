package net.herit.business.dm.service;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import net.herit.business.dm.service.impl.DeviceManagerDAO;
import net.herit.common.exception.UserSysException;
import net.herit.common.util.PagingUtil;

@Service("DeviceManagerService")
public class DeviceManagerService {

	protected static final Logger LOGGER = LoggerFactory.getLogger(DeviceManagerService.class);
	
	@Resource(name = "DeviceManagerDAO")
	private DeviceManagerDAO deviceManagerDAO;
	
	public NotificationVO getCurrentNotificationInfo() throws UserSysException {
		return deviceManagerDAO.getCurrentNotificationInfo();
	}
	
}
