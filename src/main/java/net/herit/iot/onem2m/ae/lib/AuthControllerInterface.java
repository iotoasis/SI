package net.herit.iot.onem2m.ae.lib;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.onem2m.core.util.OneM2MException;

/**
 * 인증 컨트롤러 클래스 인터페이스
 *  
 * @version : 1.0
 * @author  : Lee inseuk
 */
public interface AuthControllerInterface {

	/**
	  * 인증을 수행한다. 인증 수행 방법 및 절차는 각 구현에 따른다.
	  * 
	  */
	public void doAuth() throws OneM2MException;

	/**
	  * 인증 성공여부를 반환환다.
	  * 인증을 수행하지 않고 미리 제공된 인증 정보가 있을 경우에도 true를 반환한다.
	  * @return 인증 성공 여부
	  * 
	  */
	public boolean isAuthorized() throws OneM2MException;

	/**
	  * OneM2MRequest에 인증정보를 셋팅한다.
	  * 
	  * @param request - CSE로 전송될 REQUEST 정보 오브젝트  
	  */
	public void setAuthData(OneM2mRequest request) throws OneM2MException;


	/**
	  * 인증 과정을 통해서 인증서버로(MES)부터 획득한 Entity ID(AE ID)를 반환한다. 
	  * 
	  * @return Entity ID(AE ID)
	  */
	public String getEntityId() throws OneM2MException;

}
