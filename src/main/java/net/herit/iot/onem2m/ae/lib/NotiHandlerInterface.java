package net.herit.iot.onem2m.ae.lib;

import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.resource.AE;
import net.herit.iot.onem2m.resource.Container;
import net.herit.iot.onem2m.resource.ContentInstance;
import net.herit.iot.onem2m.resource.Notification;
import net.herit.iot.onem2m.resource.Resource;

/**
 * Notification 처리 클래스 인터페이스
 *  
 * @version : 1.0
 * @author  : Lee inseuk
 */
public interface NotiHandlerInterface {	
	
	/**
	  * ContentInstance 리소스 생성 Notification을 처리한다.
	  * Container에 subscription을 생성하면 contentInstance가 생성되었을때 이 함수가 호출된다. 
	  * @param ci - 생성된 리소스 정보 
	  * @param notification - Notification 메시지 정보
	  * @return 처리 결과 상태 코드, 지정된 상태 코드로 Notification 메시지에 대한 응답을 보낸다.
	  * @throws OneM2MException 
	  * 
	  */
	public RESPONSE_STATUS handleCreateNoti(ContentInstance ci, Notification notification) throws OneM2MException;	
	
	/**
	  * Container 리소스 생성 Notification을 처리한다.
	  * AE에 subscription을 생성하면 container가 생성되었을때 이 함수가 호출된다. 
	  * @param container - 생성된 리소스 정보 
	  * @param notification - Notification 메시지 정보
	  * @return 처리 결과 상태 코드, 지정된 상태 코드로 Notification 메시지에 대한 응답을 보낸다.
	  * @throws OneM2MException 
	  * 
	  */
	public RESPONSE_STATUS handleCreateNoti(Container container, Notification notification) throws OneM2MException;

	
	/**
	  * 리소스 생성 Notification을 처리한다.
	  * AE에 subscription을 생성하면 container가 생성되었을때 이 함수가 호출된다. 
	  * @param resource - 생성된 리소스 정보 
	  * @param notification - Notification 메시지 정보
	  * @return 처리 결과 상태 코드, 지정된 상태 코드로 Notification 메시지에 대한 응답을 보낸다.
	  * @throws OneM2MException 
	  * 
	  */
	public RESPONSE_STATUS handleCreateNoti(Resource resource, Notification notification) throws OneM2MException;


	/**
	  * 리소스 생성 Notification을 처리한다.
	  * Noti를 통해서 전달되는 데이터가 Resource가 아니고 resourceId인 경우 이 함수가 호출된다. 
	  * @param resId - 생성된 리소스 ID
	  * @param notification - Notification 메시지 정보
	  * @return 처리 결과 상태 코드, 지정된 상태 코드로 Notification 메시지에 대한 응답을 보낸다.
	  * @throws OneM2MException 
	  * 
	  */
	public RESPONSE_STATUS handleCreateNoti(String resId, Notification noti) throws OneM2MException;
	
	/**
	  * !!!! NOT TESTED
	  * AE 변경 Notification을 처리한다.
	  * AE에 subscription을 생성하면 AE의 정보가 변경되었을 때 이 함수가 호출된다. 
	  * @param ae - 수정된 AE 정보 
	  * @param notification - Notification 메시지 정보
	  * @return 처리 결과 상태 코드, 지정된 상태 코드로 Notification 메시지에 대한 응답을 보낸다.
	  * @throws OneM2MException 
	  * 
	  */
	public RESPONSE_STATUS handleUpdateNoti(AE ae, Notification notification) throws OneM2MException;


	/**
	  * !!!! NOT TESTED
	  * Container 변경 Notification을 처리한다.
	  * Container에 subscription을 생성하면 Container의 정보가 변경되었을 때 이 함수가 호출된다. 
	  * @param container - 수정된 컨테이너 정보 
	  * @param notification - Notification 메시지 정보
	  * @return 처리 결과 상태 코드, 지정된 상태 코드로 Notification 메시지에 대한 응답을 보낸다.
	  * @throws OneM2MException 
	  * 
	  */
	public RESPONSE_STATUS handleUpdateNoti(Container container, Notification notification) throws OneM2MException;


	/**
	  * !!!! NOT TESTED
	  * 리소스 변경 Notification을 처리한다.
	  * @param resource - 수정된 리소스 정보 
	  * @param notification - Notification 메시지 정보
	  * @return 처리 결과 상태 코드, 지정된 상태 코드로 Notification 메시지에 대한 응답을 보낸다.
	  * @throws OneM2MException 
	  * 
	  */
	public RESPONSE_STATUS handleUpdateNoti(Resource resource, Notification notification) throws OneM2MException;

	/**
	  * !!!! NOT TESTED
	  * Container 삭제 Notification을 처리한다.
	  * AE에 subscription을 생성하면 하위 Container가 삭제되었을때 이 함수가 호출된다. 
	  * @param container - 삭제된 컨테이너 정보 
	  * @param notification - Notification 메시지 정보
	  * @return 처리 결과 상태 코드, 지정된 상태 코드로 Notification 메시지에 대한 응답을 보낸다.
	  * @throws OneM2MException 
	  * 
	  */
	public RESPONSE_STATUS handleDeleteNoti(Container container, Notification notification) throws OneM2MException;	

	/**
	  * !!!! NOT TESTED
	  * ContentInstance 삭제 Notification을 처리한다.
	  * Container에 subscription을 생성하면 하위 CnotentInstance가 삭제되었을때 이 함수가 호출된다. 
	  * @param ci - 삭제된 ContentInstance 정보 
	  * @param notification - Notification 메시지 정보
	  * @return 처리 결과 상태 코드, 지정된 상태 코드로 Notification 메시지에 대한 응답을 보낸다.
	  * @throws OneM2MException 
	  * 
	  */
	public RESPONSE_STATUS handleDeleteNoti(ContentInstance ci, Notification notification) throws OneM2MException;	

	/**
	  * !!!! NOT TESTED
	  * 리소스 삭제 Notification을 처리한다. 
	  * @param resource - 삭제된 리소스 정보 
	  * @param notification - Notification 메시지 정보
	  * @return 처리 결과 상태 코드, 지정된 상태 코드로 Notification 메시지에 대한 응답을 보낸다.
	  * @throws OneM2MException 
	  * 
	  */
	public RESPONSE_STATUS handleDeleteNoti(Resource resource, Notification notification) throws OneM2MException;
}
