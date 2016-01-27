package net.herit.iot.onem2m.ae.hubiss;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.onem2m.ae.lib.AuthControllerInterface;
import net.herit.iot.onem2m.core.util.OneM2MException;

/**
 * LGU+ OneM2M MES서버 연동 기능을 구현한 클래스
 *  
 * @version : 1.0
 * @author  : Lee inseuk
 */
public class HubissAuthController implements AuthControllerInterface {
	
	private Logger log = LoggerFactory.getLogger(HubissAuthController.class);

	
	@Override
	/**
	  * 라이브러리 내부에서 사용함
	  * 
	  */
	public void setAuthData(OneM2mRequest request) throws OneM2MException {
		
		log.debug("setAuthData called but nothing to do.");
		
	} 
	
	@Override
	/**
	  * 라이브러리 내부에서 사용함
	  * 
	  */
	public boolean isAuthorized() {
		
		return true;
		
	}
	
	@Override
	/**
	  * 라이브러리 내부에서 사용함
	  * 
	  */
	public String getEntityId() {
		
		return null;
		
	}
	
	@Override
	/**
	  * 라이브러리 내부에서 사용함
	  * 
	  */
	public void doAuth() throws OneM2MException {
		
	}

}
