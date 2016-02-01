package net.herit.iot.onem2m.incse.controller.dm;

import java.net.URI;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.message.onem2m.format.Enums.CONTENT_TYPE;
import net.herit.iot.onem2m.bind.http.client.HttpClient;
import net.herit.iot.onem2m.core.convertor.ConvertorFactory;
import net.herit.iot.onem2m.core.convertor.JSONConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.facility.DatabaseManager;
import net.herit.iot.onem2m.incse.manager.AbsManager;
import net.herit.iot.onem2m.incse.manager.dao.RestSubscriptionDAO;
import net.herit.iot.onem2m.resource.AreaNwkDeviceInfo;
import net.herit.iot.onem2m.resource.AreaNwkInfo;
import net.herit.iot.onem2m.resource.Battery;
import net.herit.iot.onem2m.resource.DeviceCapability;
import net.herit.iot.onem2m.resource.DeviceInfo;
import net.herit.iot.onem2m.resource.EventLog;
import net.herit.iot.onem2m.resource.Firmware;
import net.herit.iot.onem2m.resource.Memory;
import net.herit.iot.onem2m.resource.MgmtCmd;
import net.herit.iot.onem2m.resource.MgmtResource;
import net.herit.iot.onem2m.resource.Reboot;
import net.herit.iot.onem2m.resource.Resource;
import net.herit.iot.onem2m.resource.RestSubscription;
import net.herit.iot.onem2m.resource.Software;


public class DMControllerFactory {

	private Logger log = LoggerFactory.getLogger(DMControllerFactory.class);

	public static DMControllerInterface getController(MgmtResource mgmtObj) throws OneM2MException {

		// 
		return new HeritDMController();
		
			
	}

	public static DMControllerInterface getController(MgmtCmd mgmtCmd) throws OneM2MException {

		// 
		return new HeritDMController();
		
			
	}
	
}
