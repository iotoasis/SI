package net.herit.iot.onem2m.ae.emul;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.handler.codec.http.HttpVersion;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.onem2m.ae.lib.AEController;
import net.herit.iot.onem2m.ae.lib.NotiHandlerInterface;
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
public class AppEmulatorNotiHandler implements NotiHandlerInterface  {
	
	private final static HttpVersion		httpVersion 		= HttpVersion.HTTP_1_1;
		
	private Logger log = LoggerFactory.getLogger(AppEmulatorNotiHandler.class);

	//public static AttributeKey<FullHttpRequest>	ATTR_KEY_REQUEST	= AttributeKey.valueOf("httpRequest");

	private AEController aeController; 
	private DeviceEmulator emulator;

	
	public AppEmulatorNotiHandler(AEController aeController) {
		
		this.aeController = aeController;
		
	}

	
	@Override
	public RESPONSE_STATUS handleCreateNoti(Resource resource, Notification notification) throws OneM2MException {
	
		log.debug("handleUpdateNoti:{}", notification.toString());
		return RESPONSE_STATUS.OK;
			
	}
	
	@Override
	public RESPONSE_STATUS handleCreateNoti(String resId, Notification notification) throws OneM2MException {
	
		log.debug("handleUpdateNoti:{}, {}", notification.toString(), resId);
		return RESPONSE_STATUS.OK;
			
	}
		
	@Override
	public RESPONSE_STATUS handleUpdateNoti(Resource resource, Notification notification) throws OneM2MException {
	
		log.debug("handleUpdateNoti:{}", notification.toString());
		return RESPONSE_STATUS.OK;
	}
	
	
	@Override
	public RESPONSE_STATUS handleDeleteNoti(Resource resource, Notification notification) throws OneM2MException {
		
	
		log.debug("handleDeleteNoti:{}", notification.toString());
		return RESPONSE_STATUS.OK;
		
	}
	
	
	@Override
	public RESPONSE_STATUS handleCreateNoti(ContentInstance ci,
			Notification notification) throws OneM2MException {
	
		log.debug("Switch status changed pi:{}, content:{}", ci.getParentID(), ci.getContent());
		return RESPONSE_STATUS.OK;
		
	}
	
	
	@Override
	public RESPONSE_STATUS handleCreateNoti(Container ci, Notification notification)
			throws OneM2MException {
	
		log.debug("handleUpdateNoti:{}", notification.toString());
		return RESPONSE_STATUS.OK;
	}
	
	
	@Override
	public RESPONSE_STATUS handleUpdateNoti(AE ae, Notification notification)
			throws OneM2MException {
	
		log.debug("handleUpdateNoti:{}", notification.toString());
		return RESPONSE_STATUS.OK;
	}
	
	
	@Override
	public RESPONSE_STATUS handleUpdateNoti(Container container,
			Notification notification) throws OneM2MException {
	
		log.debug("handleUpdateNoti:{}", notification.toString());
		return RESPONSE_STATUS.OK;
	}
	
	
	@Override
	public RESPONSE_STATUS handleDeleteNoti(Container container,
			Notification notification) throws OneM2MException {
	
		log.debug("handleUpdateNoti:{}", notification.toString());
		return RESPONSE_STATUS.OK;
	}
	
	
	@Override
	public RESPONSE_STATUS handleDeleteNoti(ContentInstance ci,
			Notification notification) throws OneM2MException {
	
		log.debug("handleUpdateNoti:{}", notification.toString());
		return RESPONSE_STATUS.OK;
	}
	
}
