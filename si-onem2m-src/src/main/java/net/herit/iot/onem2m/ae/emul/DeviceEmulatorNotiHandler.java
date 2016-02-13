package net.herit.iot.onem2m.ae.emul;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.onem2m.ae.lib.AEController;
import net.herit.iot.onem2m.ae.lib.NotiHandlerInterface;
import net.herit.iot.onem2m.core.util.LogManager;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.resource.AE;
import net.herit.iot.onem2m.resource.Container;
import net.herit.iot.onem2m.resource.ContentInstance;
import net.herit.iot.onem2m.resource.Notification;
import net.herit.iot.onem2m.resource.Resource;

/**
 * AE컨트롤러 클래스를 이용하여 디바이스 애뮬레이터를 구현한 클래스 
 * @version : 1.0
 * @author  : Lee inseuk
 */
public class DeviceEmulatorNotiHandler implements NotiHandlerInterface  {
	
	private LogManager 		logManager = LogManager.getInstacne();	
	private Logger log = LoggerFactory.getLogger(DeviceEmulatorNotiHandler.class);

	//public static AttributeKey<FullHttpRequest>	ATTR_KEY_REQUEST	= AttributeKey.valueOf("httpRequest");

	private AEController aeController = null; 
	
	private Container switchCmdCntRes = null;
	private Container switchCntRes = null;
	private AE ae = null;
	
	public DeviceEmulatorNotiHandler(AEController aeController) {
		
		this.aeController = aeController;

		this.switchCmdCntRes = null;
		this.switchCntRes = null;
		this.ae = null;
		
	}
	
	public void setResource(AE ae, Container switchCmdCntRes, Container switchCntRes) {
		this.switchCmdCntRes = switchCmdCntRes;
		this.switchCntRes = switchCntRes;
		this.ae = ae;
		
	}

	@Override
	public RESPONSE_STATUS handleCreateNoti(ContentInstance ci, Notification notification) throws OneM2MException {
	
		log.debug("handleCreateNoti:{}", notification.toString());
	
		if (switchCmdCntRes == null || switchCntRes == null || ae == null) return RESPONSE_STATUS.INTERNAL_SERVER_ERROR;
		
		if (switchCmdCntRes.getResourceID().equals(ci.getParentID())) {
			// switch control command
			String value = ci.getContent();
			log.debug("Switch control command received!!!!!!");
			log.debug("pi:{}, content:{}", ci.getParentID(), value);
			
			aeController.doCreateContentInstance(this.switchCntRes, this.ae.getResourceID(), value, null);
		}
		
		return RESPONSE_STATUS.OK;
	}

	@Override
	public RESPONSE_STATUS handleCreateNoti(Resource resource, Notification notification) throws OneM2MException {

		log.debug("handleCreateNoti:{}", notification.toString());
		
		return RESPONSE_STATUS.OK;
		
	}

	@Override
	public RESPONSE_STATUS handleCreateNoti(String resId, Notification notification) throws OneM2MException {

		log.debug("handleCreateNoti:{}, {}", notification.toString(), resId);

		//ContentInstance ci = this.aeController.doRetrieveContentInstance(resId, this.ae.getResourceID());
		//log.debug("CI:"+ci.toString());
		
		return RESPONSE_STATUS.OK;
		
	}

	@Override
	public RESPONSE_STATUS handleCreateNoti(Container ci, Notification notification) throws OneM2MException {

		log.debug("handleCreateNoti:{}", notification.toString());

		return RESPONSE_STATUS.OK;
	}

	@Override
	public RESPONSE_STATUS handleUpdateNoti(AE ae, Notification notification) throws OneM2MException {

		log.debug("handleUpdateNoti:{}", notification.toString());

		return RESPONSE_STATUS.OK;
	}

	@Override
	public RESPONSE_STATUS handleUpdateNoti(Container container, Notification notification) throws OneM2MException {

		log.debug("handleUpdateNoti:{}", notification.toString());

		return RESPONSE_STATUS.OK;
	}
	
	@Override
	public RESPONSE_STATUS handleUpdateNoti(Resource resource, Notification notification) throws OneM2MException {

		log.debug("handleUpdateNoti:{}", notification.toString());
		
		return RESPONSE_STATUS.OK;
	}

	@Override
	public RESPONSE_STATUS handleDeleteNoti(Container container, Notification notification) throws OneM2MException {

		log.debug("handleDeleteNoti:{}", notification.toString());

		return RESPONSE_STATUS.OK;
	}

	@Override
	public RESPONSE_STATUS handleDeleteNoti(ContentInstance ci, Notification notification) throws OneM2MException {

		log.debug("handleDeleteNoti:{}", notification.toString());

		return RESPONSE_STATUS.OK;
	}
	
	@Override
	public RESPONSE_STATUS handleDeleteNoti(Resource resource, Notification notification) throws OneM2MException {
	
		log.debug("handleDeleteNoti:{}", notification.toString());
	
		return RESPONSE_STATUS.OK;
		
	}
}
