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
public class KidswatchEmulatorNotiHandler implements NotiHandlerInterface  {
	
	private LogManager 		logManager = LogManager.getInstacne();	
	private Logger log = LoggerFactory.getLogger(DeviceEmulatorNotiHandler.class);

	//public static AttributeKey<FullHttpRequest>	ATTR_KEY_REQUEST	= AttributeKey.valueOf("httpRequest");

	private AEController aeController = null; 
	
	private Container locationReqCntRes = null;
	private Container locationCntRes = null;
	private Container ringtoneReqCntRes;
	private Container ringtoneCntRes;
	private AE ae = null;
	
	public KidswatchEmulatorNotiHandler(AEController aeController) {
		this.aeController = aeController;
	}
	
	public void setResource(AE ae, Container locationReqCntRes, Container locationCntRes) {
		this.locationReqCntRes = locationReqCntRes;
		this.locationCntRes = locationCntRes;
		this.ae = ae;
	}
	
	public void setResource(Container ringtoneReqCntRes, Container ringtoneCntRes) {
		this.ringtoneReqCntRes = ringtoneReqCntRes;
		this.ringtoneCntRes = ringtoneCntRes;
	}

	@Override
	public RESPONSE_STATUS handleCreateNoti(ContentInstance ci, Notification notification) throws OneM2MException {
	
		log.debug("handleCreateNoti:{}", notification.toString());
	
		if (locationReqCntRes == null || locationCntRes == null || ae == null) return RESPONSE_STATUS.INTERNAL_SERVER_ERROR;
		
		if (locationReqCntRes.getResourceID().equals(ci.getParentID())) {
			// location
			String value = ci.getContent();
			log.debug("location req received!!!!!!");
			log.debug("pi:{}, content:{}", ci.getParentID(), value);
			
			String res = String.format("{\"location\":{\"latitude\":\"37.507904\",\"longitude\":\"126.891526\",\"provider\":\"network\",%s,\"time\":\"2015-12-17 17:54:58\"}}", 
					value.replace("{", "")).replace("}", "");
			
			aeController.doCreateContentInstance(this.locationCntRes, this.ae.getResourceID(), res, null);
			
		} else if(ringtoneReqCntRes.getResourceID().equals(ci.getParentID())) {
			// location
			String value = ci.getContent();
			log.debug("ringtone req received!!!!!!");
			log.debug("pi:{}, content:{}", ci.getParentID(), value);
			aeController.doCreateContentInstance(this.ringtoneCntRes, this.ae.getResourceID(), value, null);
			
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
