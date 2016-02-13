package net.herit.iot.onem2m.ae.hubiss;

import io.netty.handler.codec.http.HttpVersion;

import java.util.Iterator;
import java.util.List;

import org.eclipse.persistence.oxm.XMLRoot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mRequest.RESULT_CONT;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.message.onem2m.format.Enums.CONTENT_TYPE;
import net.herit.iot.onem2m.ae.lib.AuthControllerInterface;
import net.herit.iot.onem2m.ae.lib.HttpBasicRequest;
import net.herit.iot.onem2m.ae.lib.HttpBasicResponse;
import net.herit.iot.onem2m.ae.lib.NotiHandlerInterface;
import net.herit.iot.onem2m.bind.http.client.HttpClient;
import net.herit.iot.onem2m.bind.http.codec.HttpRequestCodec;
import net.herit.iot.onem2m.bind.http.codec.HttpResponseCodec;
import net.herit.iot.onem2m.core.convertor.ConvertorFactory;
import net.herit.iot.onem2m.core.convertor.JSONConvertor;
import net.herit.iot.onem2m.core.convertor.XMLConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.core.util.Utils;
import net.herit.iot.onem2m.resource.AE;
import net.herit.iot.onem2m.resource.AccessControlPolicy;
import net.herit.iot.onem2m.resource.AccessControlRule;
import net.herit.iot.onem2m.resource.Container;
import net.herit.iot.onem2m.resource.ContentInstance;
import net.herit.iot.onem2m.resource.EventNotificationCriteria;
import net.herit.iot.onem2m.resource.Naming;
import net.herit.iot.onem2m.resource.Notification;
import net.herit.iot.onem2m.resource.Resource;
import net.herit.iot.onem2m.resource.SetOfAcrs;
import net.herit.iot.onem2m.resource.Subscription;
import net.herit.iot.onem2m.resource.UriContent;
import net.herit.iot.onem2m.resource.UriListContent;
import net.herit.iot.onem2m.resource.Notification.NotificationEvent;
import net.herit.iot.onem2m.resource.Notification.NotificationEvent.OperationMonitor;
import net.herit.iot.onem2m.resource.Subscription.NOTIFICATIONCONTENT_TYPE;
import net.herit.iot.onem2m.resource.Subscription.NOTIFICATIONEVENT_TYPE;

/**
 * AE가 IN-CSE와 연동하기 위한 함수를 제공하는 클래스 
 * @version : 1.0
 * @author  : Lee inseuk
 */
public class AEController {

	/**
	  * AE 컨트롤러 생성자
	  * 
	  * @param cseAddr - INCSE 주소 (INCSE 고정값)
	  * @param cseId - INCSE의 cse id (INCSE 고정값)
	  * @param csebaseName - INCSE의 csebase 리소스의 이름 (INCSE 고정값)
	  * @param contentType - 서버 연동에 사용할 Content-Type (XML:CONTENT_TYPE.XML, JSON:CONTENT_TYPE.JSON)
	  * @param authController - 인증 수행 클래스, 입력된 클래스는 AE 등록시 인증을 수행하고, 인증정보를 REQUEST메시지에 주입한다.
	  * 
	  */
	public AEController(String cseAddr, String cseId, String csebaseName, 
						CONTENT_TYPE contentType, AuthControllerInterface authController) {
		
		this.cseAddr = cseAddr;
		this.contentType = contentType;
		this.csebaseUri = cseId +"/"+ csebaseName;
		this.cseId = cseId;
		this.authController = authController;
		
	}
	
	/**
	  * AccessControlPolicy를 등록한다.
	  * 	  * 
	  * @param parentUri - AccessControlPolicy 리소스를 생성할 부모 리소스 URI(structured id).
	  * @param from - AE의 ID.    
	  * @param originIds - 권한을 허용할 AE 또는 CSE의 ID 목록.
	  * @param operation - 허용할 오퍼레이션, CREATE:1, RETRIEVE:2, UPDATE:4, DELETE:8, DISCOVERY:16, NOTIFY:32 (허용할 오퍼레이션의 합)
	  * @param retrieveBeforeCreate - create하기전에 retrieve하여 성공할 경우 retrieve된 값 반환
	  * @return 서버에 생성된 AccessControlPolicy 정보, 이미 생성되어 추가 생성되지 못한 경우에는 null 반환
	  * @throws OneM2MException 
	  */
	public AccessControlPolicy doCreateACP(String parentUri, String from, String acpName, 
											List<String> originIds, int operation, 
											List<String> spOriginIds, int spOperation, 
											boolean retrieveBeforeCreate) throws OneM2MException {

		// 이미 등록된 경우 AE정보 retrieve
		if (retrieveBeforeCreate) {
			try {
				AccessControlPolicy acp = (AccessControlPolicy)this.retrieveACP(parentUri +"/"+acpName, from);
				return acp;
			} catch (OneM2MException e) {
				if (e.getResponseStatusCode() != RESPONSE_STATUS.NOT_FOUND) {
					throw e;
				}
			}
		}

		SetOfAcrs soa = new SetOfAcrs();
		AccessControlRule acr = new AccessControlRule(originIds, operation);
		soa.addAccessControlRule(acr);
		
		SetOfAcrs spSoa = new SetOfAcrs();
		AccessControlRule spAcr = new AccessControlRule(spOriginIds, spOperation);
		spSoa.addAccessControlRule(spAcr);
		
		AccessControlPolicy acp = new AccessControlPolicy();
		acp.setPrivileges(soa);
		acp.setSelfPrivileges(spSoa);
		
		AccessControlPolicy rAcp = createACP(parentUri, from, acp, acpName);
		
		return rAcp;
		
	}

	
	/**
	  * AccessControlPolicy를 조회한다.
	  * 	  * 
	  * @param acpUri - 조회할 AccessControlPolicy 리소스 URI(structured id).
	  * @param from - AE의 ID.    
	  * @return 서버에 저장된 AccessControlPolicy 정보  
	  * @throws OneM2MException 
	  */
	public AccessControlPolicy doRetrieveACP(String acpUri, String from) throws OneM2MException {
		
		return retrieveACP(acpUri, from);
		
	}
	
	/**
	  * AE를 IN-CSE에 등록한다.
	  * 모든 AE는 최소 1회이상 실행해야 하며,  
	  * 이미 실행해서 INCSE에 등록되어 있을 경우 등록된 정보를 조회하여 반환한다.
	  * 
	  * @param csebaseUri - IN-CSE의 csebase(root) 리소스 uri.
	  * @param aeId - AE의 ID.    
	  * @param aeName - AE 이름 (예: ae-gaslock-001, LGU+ INCSE연동시 "ae-"로 시작하여야함)
	  * @param appId - AE의 애플리케이션 ID (AE는 애플리케이션의 하나의 instance)
	  * @param appName - AE의 애플리케이션 Name (AE는 애플리케이션의 하나의 instance)
	  * @param poa - IN-CSE에 AE로 REQUEST보낼 접속 주소, 예: http://onem2m.herit.net:8080
	  * @return 서버에 저장된 AE 정보  
	  */
	public AE doCreateAE(String csebaseUri, String aeId, String aeName, String appId, String appName, 
							String poa, boolean retrieveBeforeCreate) throws OneM2MException {
		
		if (authController != null && authController.isAuthorized() == false) {
			authController.doAuth();
			if (authController.getEntityId() != null) {
				aeId = authController.getEntityId();
			}
		}
		
		// 이미 등록된 경우 AE정보 retrieve
		if (retrieveBeforeCreate) {
			try {
				this.ae = (AE)this.retrieveAE(csebaseUri +"/"+aeName, aeId);
				return this.ae;
			} catch (OneM2MException e) {
				if (e.getResponseStatusCode() != RESPONSE_STATUS.NOT_FOUND) {
					throw e;
				}
			}
		}

		
		AE ae1 = new AE();
		//ae1.setAEID(aeId);
		ae1.setAppID(appId);
		ae1.setAppName(appName);
		ae1.addPointOfAccess(poa);
		ae1.setRequestReachability(true);
		
		this.ae = createAE(csebaseUri, aeId, ae1, aeName);
		
		return this.ae;
		
	}
	
	/**
	  * AE를 IN-CSE에 등록한다.
	  * 모든 AE는 최소 1회이상 실행해야 하며,  
	  * 이미 실행해서 INCSE에 등록되어 있을 경우 등록된 정보를 조회하여 반환한다.
	  * 
	  * @param csebaseUri - IN-CSE의 csebase(root) 리소스 uri.
	  * @param aeId - AE의 ID.    
	  * @param aeName - AE 이름 (예: ae-gaslock-001, LGU+ INCSE연동시 "ae-"로 시작하여야함)
	  * @param ae - 등록될 AE정보를 포함한 AE오브젝트 (appId, appName, poa, reachability 필수)
	  * @return 서버에 저장된 AE 정보  
	  */
	public AE doCreateAE(String csebaseUri, String aeId, String aeName, AE ae, boolean retrieveBeforeCreate) throws OneM2MException {
		
		if (authController != null && authController.isAuthorized() == false) {
			authController.doAuth();
			if (authController.getEntityId() != null) {
				aeId = authController.getEntityId();
			}
		}
		
		// 이미 등록된 경우 AE정보 retrieve
		if (retrieveBeforeCreate) {
			try {
				this.ae = (AE)this.retrieveAE(csebaseUri +"/"+aeName, aeId);
				return this.ae;
			} catch (OneM2MException e) {
				if (e.getResponseStatusCode() != RESPONSE_STATUS.NOT_FOUND) {
					throw e;
				}
			}
		}
		
//		AE ae1 = new AE();
//		ae1.setAppID(appId);
//		ae1.setAppName(appName);
//		ae1.addPointOfAccess(poa);
//		ae1.setRequestReachability(true);
		
		this.ae = createAE(csebaseUri, aeId, ae, aeName);
		
		return this.ae;
		
	}
	
	/**
	  * AE정보를 조회한다.
	  * 
	  * @param csebaseUri - IN-CSE의 csebase(root) 리소스 uri.
	  * @param aeName - AE 이름 (예: ae-gaslock-001, LGU+ INCSE연동시 "ae-"로 시작하여야함)
	  * @param from - AE의 ID.    
	  * @return 서버에 저장된 AE 정보  
	  * @throws OneM2MException 
	  */
	public AE doRetrieveAE(String csebaseUri, String aeName, String from) throws OneM2MException {
		
		return retrieveAE(csebaseUri +"/"+ aeName, from);
		
	}
	
	/**
	  * AE정보를 조회한다.
	  * 
	  * @param aeUri - 조회할 AE 리소스 URI(structured id).
	  * @param from - AE의 ID.    
	  * @return 서버에 저장된 AE 정보  
	  * @throws OneM2MException 
	  */
	public AE doRetrieveAE(String aeUri, String from) throws OneM2MException {
		
		return retrieveAE(aeUri, from);
		
	}


	/**
	  * AE의 POA를 수정한다.
	  * AE의 POA가 변경되었을 경우 호출해야 한다.
	  * 
	  * @param aeUri - AE의 Uri. 
	  * @param from - AE의 ID.    
	  * @param poa - 수정될 POA
	  * @return 수정된 AE 정보 오브젝트  
	  */
	public AE doUpdateAEPoa(String aeUri, String from, String poa) throws OneM2MException {

		AE ae1 = new AE();
		ae1.addPointOfAccess(poa);
		
		this.update(aeUri, from, ae1);
		
		if (this.ae == null) {
			return ae1;
		} else {
			if (this.ae.getPointOfAccess() != null) {
				this.ae.getPointOfAccess().clear();
			}
			this.ae.addPointOfAccess(poa);
			
			return this.ae;
		}
		
	}

	/**
	  * Container를 생성한다.
	  * 
	  * @param aeUri - AE의 Uri. 
	  * @param from - AE의 ID.    
	  * @param containerName - 생성될 container이름
	  * @param acpIds - 접근권한을 지정한 AccessControlPolicy ID 리스트
	 * @param acpIds 
	  * @return 생성된 container정보 오브젝트  
	  */
	public Container doCreateContainer(String aeUri, String from, String containerName, List<String> acpIds, 
										boolean retrieveBeforeCreate) throws Exception {
				
		// 이미 등록된 경우 Container정보 retrieve
		if (retrieveBeforeCreate) {
			try {
				Container cntr = (Container)this.retrieveContainer(aeUri +"/"+containerName, from);
				return cntr;
			} catch (OneM2MException e) {
				if (e.getResponseStatusCode() != RESPONSE_STATUS.NOT_FOUND) {
					throw e;
				}
			}
		}
		
		Container cnt1 = new Container();
		cnt1.addLabels(containerName);
		
		if (acpIds != null) {
			Iterator<String> it = acpIds.iterator();
			while (it.hasNext()) {
				cnt1.addAccessControlPolicyIDs(it.next());			
			}
		}
		
		return createContainer(aeUri, from, cnt1, containerName);
		
	}
	
	/**
	  * Container정보를 조회한다.
	  * 
	  * @param containerUri - 조회할 Container 리소스 URI(structured id).
	  * @param from - AE의 ID.    
	  * @return 서버에 저장된 Container 정보  
	  * @throws OneM2MException 
	  */
	public Container doRetrieveContainer(String containerUri, String from) throws OneM2MException {
		
		return retrieveContainer(containerUri, from);
		
	}

	/**
	  * Container의 content를 업데이트한다.
	  * Container하위에 contentInstance를 생성을 통해서 최종 상태값을 업데이트함.
	  * 최종 생성된 값은 [container uri]/la 를 통해서 조회(retrieve) 가능함
	  * 
	  * @param cntUri - Container의 Uri.
	  * @param containerName - container이름 
	  * @param from - AE의 ID.    
	  * @param value - 문자열로 된 Container의 정보 값, 값의 형식은 container별로 정의해서 사용해야 함
	  * @param name - 생성될 contentInstance의 이름
	  * @return 생성된 contentInstance 정보 오브젝트  
	  */
	public ContentInstance doCreateContentInstance(String cntUri, String containerName, String from, String value, String ciName) throws Exception {
		
		ContentInstance ci = new ContentInstance();
		ci.setContentInfo("text/plain:0");
		ci.setContent(value);
		ci.addLabels(containerName);
		
		return createContentInstance(cntUri, from, ci, ciName);	
		
	}
	
	/**
	  * Container의 content를 업데이트한다.
	  * Container하위에 contentInstance를 생성을 통해서 최종 상태값을 업데이트함.
	  * 최종 생성된 값은 [container uri]/la 를 통해서 조회(retrieve) 가능함
	  * 
	  * @param container - 업데이트될 container정보, uri와 resourceName이 반드시 셋팅되어 있어야 함. 
	  * @param from - AE의 ID.    
	  * @param value - 문자열로 된 Container의 정보 값, 값의 형식은 container별로 정의해서 사용해야 함
	  * @param ciName - 생성될 contentInstance 이름. null로 설정할 경우 서버에서 자동 생성
	  * @return 생성된 contentInstance 정보 오브젝트  
	  */
	public ContentInstance doCreateContentInstance(Container container, String from, String value, String ciName) throws OneM2MException {
		
		ContentInstance ci = new ContentInstance();
		ci.setContentInfo("text/plain:0");
		ci.setContent(value);
		ci.addLabels(container.getResourceName());
		
		return createContentInstance(container.getUri(), from, ci, ciName);	
		
	}
	
	/**
	  * Container의 content를 업데이트한다.
	  * Container하위에 contentInstance를 생성을 통해서 최종 상태값을 업데이트함.
	  * 최종 생성된 값은 [container uri]/la 를 통해서 조회(retrieve) 가능함
	  * 
	  * @param containerUri - 업데이트될 container의 uri
	  * @param from - AE의 ID.    
	  * @param value - 문자열로 된 Container의 정보 값, 값의 형식은 container별로 정의해서 사용해야 함
	  * @param ciName - 생성될 contentInstance 이름. null로 설정할 경우 서버에서 자동 생성
	  * @return 생성된 contentInstance 정보 오브젝트  
	  */
	public ContentInstance doCreateContentInstance(String containerUri, String from, String value, String ciName) throws OneM2MException {
		
		ContentInstance ci = new ContentInstance();
		ci.setContentInfo("text/plain:0");
		ci.setContent(value);
		
		return createContentInstance(containerUri, from, ci, ciName);
		
	}

	
	/**
	  * ContentInstance 정보를 조회한다.
	  * 
	  * @param ciUri - 조회할 contentInstance 리소스 URI(structured id).
	  * @param from - AE의 ID.    
	  * @return 서버에 저장된 ContentInstance 정보  
	  * @throws OneM2MException 
	  */
	public ContentInstance doRetrieveContentInstance(String ciUri, String from) throws OneM2MException {
		
		return retrieveContentInstance(ciUri, from);
		
	}

	/**
	  * Container의 최종 정보(contentInstnace)를 조회한다.
	  * 
	  * @param cntUri - 업데이트될 container의 uri 
	  * @param from - AE의 ID.    
	  * @return 생성된 contentInstance 정보 오브젝트  
	  */
	public ContentInstance doGetLatestContent(String cntUri, String from) throws OneM2MException {
		return (ContentInstance)this.retrieveContentInstance(cntUri+"/"+Naming.LATEST_SN, from);
	}
	
	// contentType: 1-resource, 2-modified attribute, 3-resourceId(uri)

	/**
	  * 리소스에 대한 구독을 신청한다.
	  * 지정한 리소스의 하위에 subscription리소스를 생성함
	  * 
	  * @param targetUri - 구독 신청할 리소스 uri 
	  * @param from - AE의 ID.     
	  * @param notificationUri - 컨테이너 리소스 수정, 하위 리소스 생성, 삭제에 대한 통지(notification)을 받을 URI, LGU+서버의 경우 통지를 받을 ae의 resourceID입력     
	  * @param notiContentType - 통지되는 정보의 종류    1:리소스 오브젝트, 2:수정된 속성을 포함한 리소스 오브젝트, 3:리소스 ID정보(이 경우 통지가 오면 해당 리소스 조회를 통해서 정보를 파악함)
	  * @param acpIds - 권한을 지정한 AccessControlPolicy
	  * @return 생성된 subscription 정보 오브젝트  
	  */
	public Subscription doCreateSubscription(String targetUri, String from, String subsName, String notificationUri, 
			int notiContentType, List<String> acpIds, boolean retrieveBeforeCreate) throws Exception {

		// 이미 등록된 경우 Container정보 retrieve
		if (retrieveBeforeCreate) {
			try {
				Subscription subs = (Subscription)this.retrieveSubscription(targetUri +"/"+subsName, from);
				return subs;
			} catch (OneM2MException e) {
				if (e.getResponseStatusCode() != RESPONSE_STATUS.NOT_FOUND) {
					throw e;
				}
			}
		}
		
		Subscription subs = new Subscription();
		subs.addNotificationURI(notificationUri);
		EventNotificationCriteria cri = new EventNotificationCriteria();
		cri.addNotificationEventType(NOTIFICATIONEVENT_TYPE.CREATE_OF_CHILD_RESOURCE.Value());
		
		subs.setEventNotificationCriteria(cri);
		subs.setNotificationContentType(notiContentType == 3 ? NOTIFICATIONCONTENT_TYPE.RESOURCE_ID.Value() : 
															NOTIFICATIONCONTENT_TYPE.ALL_ATRRIBUTES.Value());

		
		if (acpIds != null) {
			Iterator<String> it = acpIds.iterator();
			while (it.hasNext()) {
				subs.addAccessControlPolicyIDs(it.next());			
			}
		}
		
		return createSubscription(targetUri, from, subsName, subs);
		
	}
	
	/**
	  * Subscription 리소스를 조회한다.
	  * 
	  * @param subsUri - 조회할 Subscription 리소스 URI(structured id).
	  * @param from - AE의 ID.    
	  * @return 서버에 저장된 Subscription 정보  
	  * @throws OneM2MException 
	  */
	public Subscription doRetrieveSubscription(String subsUri, String from) throws OneM2MException {
		
		return retrieveSubscription(subsUri, from);
		
	}

	/**
	  * NOT TESTED!!!!
	  * AE에 대한 구독을 신청한다.
	  * AE가 생성될 경우 notification을 받을 수 있도록 csebase하위에 subscription리소스를 생성함
	  * 
	  * @param deviceAppName - 구독 신청할 AE의 애플리케이션명, 해당 애플리케이션에 속한 AE의 생성 삭제에 대해서만 통지됨
	  * @param from - AE의 ID.     
	  * @param notificationUri - AE 생성 삭제에 대한 통지(notification)을 받을 URI, LGU+서버의 경우 통지를 받을 ae의 resourceID입력     
	  * @return 생성된 subscription 정보 오브젝트  
	  */
//	public Subscription doAESubscription(String deviceAppName, String from, String notificationUri) throws Exception {
//		
//		Subscription subs = new Subscription();
//		subs.addNotificationURI(notificationUri);
//		EventNotificationCriteria cri = new EventNotificationCriteria();
//		cri.addNotificationEventType(NOTIFICATIONEVENT_TYPE.CREATE_OF_CHILD_RESOURCE.Value());
//		cri.addAttribute(new Attribute(Naming.RESOURCETYPE_SN, RESOURCE_TYPE.AE.Value()));
//		cri.addAttribute(new Attribute(Naming.APPNAME_SN, deviceAppName));
//		
//		subs.setEventNotificationCriteria(cri);
//		subs.setNotificationContentType(NOTIFICATIONCONTENT_TYPE.ALL_ATRRIBUTES.Value());
//		
//		return createSubscription(csebaseUri, from, subs);
//		
//	}


	/**
	  * NOT TESTED!!!!
	  * AE에 정보를 검색한다.
	  * 
	  * @param deviceAppName - 검색할 AE의 애플리케이션명, 해당 애플리케이션만 검색됨 
	  * @param createdSince - 생성시간조건, 지정된 시간 이후에 생성된 AE만 검색됨          
	  * @return 검색된 AE 목록  
	  */
//	public List<AE> doDiscoverDeviceAE(String deviceAppName, String createdSince) throws Exception {
//
//		OneM2mRequest request = new OneM2mRequest();
//		request.setOperation(OPERATION.DISCOVERY);
//		request.setTo(this.csebaseUri);
//		request.setFrom(ae.getResourceID());
//		request.setRequestId(Utils.createRequestId());
//		
//		FilterCriteria fc = new FilterCriteria();
//		fc.setCreatedAfter(createdSince);
//		fc.setFilterUsage(FILTER_USAGE.DISCOVERY.Value());
//		request.setFilterCriteria(fc);
//		
//		if (this.authController != null) this.authController.setAuthData(request);
//				
//		OneM2mResponse res = new HttpClient().processRequest(cseAddr, request);
//		
//		if (res.getResStatusCode() == RESPONSE_STATUS.OK) {
//			
//			UriListContent ulc = (UriListContent)convertToUriList(new String(res.getContent(), "UTF-8"));
//			
//			List<AE> aeList = new ArrayList<AE>();
//			List<String> aeUriList = ulc.getUriList();
//			Iterator<String> it = aeUriList.iterator();
//			
//			while (it.hasNext()) {
//				String uri = it.next();
//				AE ae = (AE)this.retrieveAE(uri,  this.ae.getResourceID());
//			}
//			
//			return  aeList;
//			
//		} else {
//			log.debug("registration failed. {}", res.toString());
//			return null;
//		}
//		
//	}


	/**
	  * 수신한 OneM2m Http Request에서 Notification메시지를 추출하여 notiHandler로 전달한다. 
	  * Notification 메시지만 처리한다. 
	  * Notification에 의한 서비스 로직은 notiHandler에서 구현해야 함.
	  * 
	  * @param method - 수신 메시지의 HTTP Method 
	  * @param uri - 수신 메시지의 요청 URI 
	  * @param headerMap - 수신 메시지의 HTTP 헤더맵 
	  * @param host - 수신 메시지의 호스트명 
	  * @param content - 수신 메시지의 Body 내용 
	  * @param notiHandler - 수신한 notification 메시지를 처리할 클래스          
	  * @return 처리 결과 응답 메시지
	 * @throws Exception 
	  */
//	public OneM2mResponse doProcessRequest(String method, String uri, HashMap<String, String> headerMap, String host, byte[] content, 
//											NotiHandlerInterface notiHandler) throws Exception {
//		
//		OneM2mRequest reqMessage = RequestCodec.decode(method, uri, headerMap, host, content);
//		
//		return doProcessRequest(reqMessage, notiHandler);	
//	}
	


	/**
	  * 수신한 OneM2m Http Request에서 Notification메시지를 추출하여 notiHandler로 전달한다. 
	  * Notification 메시지만 처리한다. 
	  * Notification에 의한 서비스 로직은 notiHandler에서 구현해야 함.
	  * 
	  * @param reqMessage - 파싱된 OneM2M HTTP 요청 메시지 
	  * @param notiHandler - 수신한 notification 메시지를 처리할 클래스          
	  * @return 처리 결과 응답 메시지
	 * @throws Exception 
	  */
	public HttpBasicResponse doProcessRequest(HttpBasicRequest request, NotiHandlerInterface notiHandler) throws Exception {

		OneM2mRequest reqMessage = HttpRequestCodec.decode(request.getMethod(), request.getUri(), request.getHeaders(), 
				request.getHost(), request.getContent());
		
		OneM2mResponse resMessage = doProcessRequest(reqMessage, notiHandler);
		
		HttpBasicResponse response = HttpResponseCodec.encodeToBasic(resMessage, HttpVersion.HTTP_1_1);
		
		return response;
		
	}

	/**
	  * 수신한 OneM2m Http Request에서 Notification메시지를 추출하여 notiHandler로 전달한다. 
	  * Notification 메시지만 처리한다. 
	  * Notification에 의한 서비스 로직은 notiHandler에서 구현해야 함.
	  * 
	  * @param reqMessage - 파싱된 OneM2M HTTP 요청 메시지 
	  * @param notiHandler - 수신한 notification 메시지를 처리할 클래스          
	  * @return 처리 결과 응답 메시지
	 * @throws Exception 
	  */
	public OneM2mResponse doProcessRequest(OneM2mRequest reqMessage, NotiHandlerInterface notiHandler) throws Exception {
		
		Notification noti = null;
		try {
			noti = convertToNotification(new String(reqMessage.getContent(), "UTF-8"));
		} catch (Exception e) {			
			e.printStackTrace();
			throw new OneM2MException(RESPONSE_STATUS.BAD_REQUEST, "Fail to convert to notification obejct:"+e.getMessage());
		}
		
		
		if (noti.isVerificationRequest() != null && noti.isVerificationRequest()) {
			return new OneM2mResponse(RESPONSE_STATUS.OK, reqMessage);	
		}
		
		NotificationEvent ne = noti.getNotificationEvent();
		if (ne == null) {
			OneM2mResponse res = new OneM2mResponse(RESPONSE_STATUS.BAD_REQUEST, reqMessage);
			res.setContent(new String("NotificationEvent should be provided!").getBytes());
			return res;
		}
		
		OperationMonitor om = ne.getOperationMonitor();
				
		RESPONSE_STATUS status = null;
		Resource resource = null;
		String resId = null;
		
		OPERATION op = OPERATION.CREATE;
		if (om != null) {
			op = OPERATION.get(om.getOperation());
		}
		
		Object rep = ne.getRepresentation().getResource();
		
		try {
			if (rep instanceof UriListContent) {
				resId = ((UriListContent)rep).getUriList().get(0);
				//OneM2mResponse response = this.retrieve(resId, this.ae.getResourceID());
				//resource = this.convertToResource(response);
			} else if (rep instanceof String) {
				resId = (String)rep;
				//OneM2mResponse response = this.retrieve(this.cseId+"/"+resId, this.ae.getResourceID());
				//resource = this.convertToResource(response);
			} else if (rep instanceof UriContent) {
				resId = ((UriContent)rep).getUri();
				//OneM2mResponse response = this.retrieve(((UriContent)rep).getUri(), this.ae.getResourceID());
				//resource = this.convertToResource(response);
			} else if (rep instanceof Resource) {
				resource = (Resource)rep;
			} else if (rep instanceof XMLRoot) {
				XMLRoot root = (XMLRoot)rep;			
				resId = (String)root.getObject();
				//OneM2mResponse response = this.retrieve(this.cseId+"/"+resId, this.ae.getResourceID());
				//resource = this.convertToResource(response);
			} else {			
				throw new OneM2MException(RESPONSE_STATUS.BAD_REQUEST, "Unexpected representation:"+ (rep != null ? rep.getClass().getName() : "null"));
			}
		} catch (OneM2MException e) {
			throw e;
		} catch (Exception e) {
			
			e.printStackTrace();
			throw new OneM2MException(RESPONSE_STATUS.BAD_REQUEST, "Unexpected representation:"+e.getMessage());
			
		}
		
		if (resource == null && resId != null) {
			status = notiHandler.handleCreateNoti(resId, noti);
		} else {
		
			switch (op) {
			case CREATE:
				switch (RESOURCE_TYPE.get(resource.getResourceType())) {
				case CONTENT_INST:
					status = notiHandler.handleCreateNoti((ContentInstance)resource, noti);
					break;
				case CONTAINER:
					status = notiHandler.handleCreateNoti((Container)resource, noti);
					break;
				default:
					status = notiHandler.handleCreateNoti(resource, noti);
					break;
				}
			case UPDATE:
				switch (RESOURCE_TYPE.get(resource.getResourceType())) {
				case AE:
					status = notiHandler.handleUpdateNoti((AE)resource, noti);
					break;
				case CONTAINER:
					status = notiHandler.handleUpdateNoti((Container)resource, noti);
					break;
				default:
					status = notiHandler.handleUpdateNoti(resource, noti);
					break;
				}
			case DELETE:
				switch (RESOURCE_TYPE.get(resource.getResourceType())) {
				case CONTENT_INST:
					status = notiHandler.handleDeleteNoti((ContentInstance)resource, noti);
					break;
				case CONTAINER:
					status = notiHandler.handleDeleteNoti((Container)resource, noti);
					break;
				default:
					status = notiHandler.handleDeleteNoti(resource, noti);
					break;
				}
			case RETRIEVE:
			case NOTIFY:
			default:
				break;				
			}
		}
						
		return new OneM2mResponse(status, reqMessage);		
	}
	
	
	
	
	
	
	
	
	
	
	
	private Logger log = LoggerFactory.getLogger(AEController.class);
	
	protected String cseAddr;
	protected CONTENT_TYPE contentType;
	
	protected AE ae = null;
	protected String csebaseUri;
	protected String cseId;

	protected AuthControllerInterface authController = null;
	
	
	protected Resource convertToResource(RESOURCE_TYPE resType, OneM2mResponse res) throws OneM2MException {
		
		try {
			Resource resource = convertToResource(resType, new String(res.getContent(), "UTF-8"));
			resource.setUri(res.getContentLocation());
			return resource;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, "Error during content utf8 encoding:"+e.getMessage());
		}
	
	}
	
	protected Resource convertToResource(OneM2mResponse res) throws OneM2MException {
		
		try {
			String pc = new String(res.getContent(), "UTF-8");
			Resource resource = convertToResource(Utils.getResTypeWithContentString(pc, res.getContentType()), pc);
			resource.setUri(res.getContentLocation());
			return resource;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, "Error during content utf8 encoding:"+e.getMessage());
		}
	
	}
		
	protected Resource convertToResource(RESOURCE_TYPE resType, String content) throws OneM2MException {
		try {
			if (contentType == CONTENT_TYPE.RES_XML) {
				
				XMLConvertor<?> xmlCvt = null;
				switch (resType) {
				case AE:
					xmlCvt = ConvertorFactory.getXMLConvertor(AE.class, AE.SCHEMA_LOCATION);
					break;
				case CONTAINER:
					xmlCvt = ConvertorFactory.getXMLConvertor(Container.class, Container.SCHEMA_LOCATION);
					break;
				case CONTENT_INST:
					xmlCvt = ConvertorFactory.getXMLConvertor(ContentInstance.class, ContentInstance.SCHEMA_LOCATION);
					break;
				case SUBSCRIPTION:
					xmlCvt = ConvertorFactory.getXMLConvertor(Subscription.class, Subscription.SCHEMA_LOCATION);
					break;
				case NOTIFICATION:
					xmlCvt = ConvertorFactory.getXMLConvertor(Notification.class, Notification.SCHEMA_LOCATION);
					break;
				case ACCESS_CTRL_POLICY:
					xmlCvt = ConvertorFactory.getXMLConvertor(AccessControlPolicy.class, AccessControlPolicy.SCHEMA_LOCATION);
					break;
				}
				
				if (xmlCvt == null) return null;
				else
						return (Resource)xmlCvt.unmarshal(content);
				
			} else {
				
				JSONConvertor<?> jsonCvt = null;
				switch (resType) {
				case AE:
					jsonCvt = ConvertorFactory.getJSONConvertor(AE.class, AE.SCHEMA_LOCATION);
					break;
				case CONTAINER:
					jsonCvt = ConvertorFactory.getJSONConvertor(Container.class, Container.SCHEMA_LOCATION);
					break;
				case CONTENT_INST:
					jsonCvt = ConvertorFactory.getJSONConvertor(ContentInstance.class, ContentInstance.SCHEMA_LOCATION);
					break;
				case SUBSCRIPTION:
					jsonCvt = ConvertorFactory.getJSONConvertor(Subscription.class, Subscription.SCHEMA_LOCATION);
					break;
				case NOTIFICATION:
					jsonCvt = ConvertorFactory.getJSONConvertor(Notification.class, Notification.SCHEMA_LOCATION);
					break;
				}
				
				if (jsonCvt == null)	return null;
				else					return (Resource)jsonCvt.unmarshal(content);
				
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, "Exception during contentToResource:"+ e.getMessage());
		}
	}

	
	protected Notification convertToNotification(String content) throws Exception {
		if (contentType == CONTENT_TYPE.RES_XML) {
			
			XMLConvertor<?> xmlCvt = ConvertorFactory.getXMLConvertor(Notification.class, Notification.SCHEMA_LOCATION);
			if (xmlCvt == null) return null;
			else				return (Notification) xmlCvt.unmarshal(content);
			
		} else {
			
			JSONConvertor<?> jsonCvt = ConvertorFactory.getJSONConvertor(Notification.class, Notification.SCHEMA_LOCATION);
			if (jsonCvt == null)	return null;
			else					return (Notification)jsonCvt.unmarshal(content);
			
		}
		
	}

	
	protected UriListContent convertToUriList(String content) throws Exception {
		if (contentType == CONTENT_TYPE.RES_XML) {
			
			XMLConvertor<?> xmlCvt = ConvertorFactory.getXMLConvertor(UriListContent.class, UriListContent.SCHEMA_LOCATION);
			if (xmlCvt == null) return null;
			else				return (UriListContent) xmlCvt.unmarshal(content);
			
		} else {
			
			JSONConvertor<?> jsonCvt = ConvertorFactory.getJSONConvertor(UriListContent.class, UriListContent.SCHEMA_LOCATION);
			if (jsonCvt == null)	return null;
			else					return (UriListContent)jsonCvt.unmarshal(content);
			
		}
		
	}
	
	protected AE retrieveAE(String resUri, String from) throws OneM2MException {

		OneM2mResponse res = retrieve(resUri, from);
		AE ae = (AE)convertToResource(RESOURCE_TYPE.AE, res);
		if (ae.getUri() == null) {
			ae.setUri(resUri);
		}
		return ae;
	}
	
	protected AE updateAE(String resUri, AE ae, String from) throws OneM2MException {

		OneM2mResponse res = update(resUri, from, ae);
		return (AE)convertToResource(RESOURCE_TYPE.AE, res);
		
	}
	
	protected AccessControlPolicy retrieveACP(String resUri, String from) throws OneM2MException {

		OneM2mResponse res = retrieve(resUri, from);
		AccessControlPolicy acp = (AccessControlPolicy)convertToResource(RESOURCE_TYPE.ACCESS_CTRL_POLICY, res);
		if (acp.getUri() == null) {
			acp.setUri(resUri);
		}
		return acp;
		
	}
	
	protected Container retrieveContainer(String resUri, String from) throws OneM2MException {

		OneM2mResponse res = retrieve(resUri, from);
		Container container = (Container)convertToResource(RESOURCE_TYPE.CONTAINER, res);
		if (container.getUri() == null) {
			container.setUri(resUri);
		}
		return container;
	}
	
	protected Subscription retrieveSubscription(String resUri, String from) throws OneM2MException {

		OneM2mResponse res = retrieve(resUri, from);
		Subscription subs = (Subscription)convertToResource(RESOURCE_TYPE.SUBSCRIPTION, res);
		if (subs.getUri() == null) {
			subs.setUri(resUri);
		}
		return subs;
	}
	
	protected ContentInstance retrieveContentInstance(String resUri, String from) throws OneM2MException {

		OneM2mResponse res = retrieve(resUri, from);
		ContentInstance ci = (ContentInstance)convertToResource(RESOURCE_TYPE.CONTENT_INST, res);
		if (ci.getUri() == null) {
			ci.setUri(resUri);
		}
		return ci;
	}

	
//	private Resource retrieveResource(String resUri, String from) throws OneM2MException {
//		
//		OneM2mResponse res = retrieve(resUri, from);
//		return convertToResource(res);
//
//		OneM2mRequest request = new OneM2mRequest();
//		request.setOperation(OPERATION.RETRIEVE);
//		request.setTo(resUri);		// "/casebase/SAE_1"
//		request.setFrom(from);	// "/SAE_1"
//		request.setRequestId(OneM2mUtil.createRequestId());
//		request.setContentType(CONTENT_TYPE.RES_XML);
//		request.setResultContent(RESULT_CONT.ATTRIBUTE);
//		
//		OneM2mResponse res = new HttpClient().processRequest(cseAddr, request);	// "http://localhost:8080"
//
//		if (res == null) {
//			log.error("retrieveResource failed.");
//			throw new OneM2MException(RESPONSE_STATUS.UNDEFINED, "Faile to retrieveResource RES:null");
//		}
//				
//		if (res.getResStatusCode() == RESPONSE_STATUS.OK) {
//			return convertToResource(RESOURCE_TYPE.AE, res);
//		} else {
//			log.error("retrieveResource failed. {}", res.toString());
//			throw new OneM2MException(res.getResStatusCode(), "Faile to retrieveResource RES:"+res.toString());
//		}
//		
//	}
	
	protected OneM2mResponse update(String resUri, String from, Resource resource) throws OneM2MException {
		OneM2mRequest request = new OneM2mRequest();
		request.setOperation(OPERATION.UPDATE);
		request.setTo(resUri);		// "/casebase/SAE_1"
		request.setFrom(from);	// "/SAE_1"
		request.setRequestIdentifier(Utils.createRequestId());
		request.setContentType(CONTENT_TYPE.RES_XML);
		request.setResultContent(RESULT_CONT.ATTRIBUTE);
		request.setContentObject(resource);
		
		if (this.authController != null) this.authController.setAuthData(request);
		
//		OneM2mResponse res = new HttpClient().process(cseAddr, request);	// "http://localhost:8080"
		OneM2mResponse res = HttpClient.getInstance().sendRequest(cseAddr, request);	

		if (res == null) {
			log.error("update failed.");
			throw new OneM2MException(RESPONSE_STATUS.NETWORK_FAILURE, "network failure");
		}
				
		if (res.getResponseStatusCodeEnum() == RESPONSE_STATUS.OK || res.getResponseStatusCodeEnum() == RESPONSE_STATUS.CHANGED) {
			return res;
		} else {
			log.error("update failed. {}", res.toString());
			throw new OneM2MException(res.getResponseStatusCodeEnum(), "Faile to uprdate resource, response:"+res.toString());
		}
		
	}
	
	protected OneM2mResponse retrieve(String resUri, String from) throws OneM2MException {
		OneM2mRequest request = new OneM2mRequest();
		request.setOperation(OPERATION.RETRIEVE);
		request.setTo(resUri);		// "/casebase/SAE_1"
		request.setFrom(from);	// "/SAE_1"
		request.setRequestIdentifier(Utils.createRequestId());
		request.setContentType(CONTENT_TYPE.RES_XML);
		request.setResultContent(RESULT_CONT.ATTRIBUTE);
		
		if (this.authController != null) this.authController.setAuthData(request);
		
//		OneM2mResponse res = new HttpClient().process(cseAddr, request);	// "http://localhost:8080"
		OneM2mResponse res = HttpClient.getInstance().sendRequest(cseAddr, request);

		if (res == null) {
			log.error("retrieve failed.");
			throw new OneM2MException(RESPONSE_STATUS.NETWORK_FAILURE, "network failure");
		}
				
		if (res.getResponseStatusCodeEnum() == RESPONSE_STATUS.OK) {
			return res;
		} else {
			log.error("retrieve failed. {}", res.toString());
			throw new OneM2MException(res.getResponseStatusCodeEnum(), "Faile to retrieve RES:"+res.toString());
		}
		
	}
	
	protected Subscription createSubscription(String targetUri, String from, String subsName, Subscription subs) throws OneM2MException {
		
		OneM2mRequest request = new OneM2mRequest();
		request.setOperation(OPERATION.CREATE);
		request.setTo(targetUri);
		request.setFrom(from);
		request.setRequestIdentifier(Utils.createRequestId());
		request.setResourceType(RESOURCE_TYPE.SUBSCRIPTION);
		request.setContentType(CONTENT_TYPE.RES_XML);
		request.setResultContent(RESULT_CONT.ATTRIBUTE);
		request.setContentObject(subs);
		if (subsName == null || subsName.length() == 0) {
			subsName = "sub-"+ae.getResourceName().replace("-", "_");	// uplus 서버 제약사항 (resourcename에 '-' 사용 불가)
		}
		request.setName(subsName);
		
		if (this.authController != null) this.authController.setAuthData(request);
		
//		OneM2mResponse res = new HttpClient().process(cseAddr, request);
		OneM2mResponse res = HttpClient.getInstance().sendRequest(cseAddr, request);
		
		if (res == null) {
			log.error("createSubscription failed.");
			throw new OneM2MException(RESPONSE_STATUS.NETWORK_FAILURE, "network failure");
		}
		
		if (res.getResponseStatusCodeEnum() == RESPONSE_STATUS.CREATED) {
			Subscription subs1 = (Subscription)convertToResource(RESOURCE_TYPE.SUBSCRIPTION, res);
			subs1.setUri(targetUri +"/"+ subs1.getResourceName());
			return subs1;
		} else if (res.getResponseStatusCodeEnum() == RESPONSE_STATUS.CONFLICT || res.getResponseStatusCodeEnum() == RESPONSE_STATUS.ALREADY_EXISTS) {
			Subscription subs1 = this.retrieveSubscription(targetUri +"/"+subsName, from);
			subs1.setUri(targetUri +"/"+ subs1.getResourceName());			
			return subs1; 
		} else {
			log.error("createSubscription failed.");
			throw new OneM2MException(res.getResponseStatusCodeEnum(), "Faile to createSubscription RES:null");
		}
		
	}
	
	protected AE createAE(String parentUri, String from, AE ae, String name) throws OneM2MException {
		
		OneM2mRequest request = new OneM2mRequest();
		request.setOperation(OPERATION.CREATE);
		request.setTo(parentUri);
		request.setFrom(from);
		request.setRequestIdentifier(Utils.createRequestId());
		request.setResourceType(RESOURCE_TYPE.AE);
		request.setContentType(CONTENT_TYPE.RES_XML);
		request.setResultContent(RESULT_CONT.ATTRIBUTE);
		request.setContentObject(ae);
		if (name != null) {
			request.setName(name);
		}
		
		if (this.authController != null) this.authController.setAuthData(request);
		
//		OneM2mResponse res = new HttpClient().process(cseAddr, request);
		
		OneM2mResponse res = HttpClient.getInstance().sendRequest(cseAddr, request);
		
		if (res == null) {
			log.error("createAE failed.");
			throw new OneM2MException(RESPONSE_STATUS.NETWORK_FAILURE, "network failure");
		}		
		
		if (res.getResponseStatusCodeEnum() == RESPONSE_STATUS.CREATED) {			
			AE ae1 = (AE)convertToResource(RESOURCE_TYPE.AE, res);
			ae1.setUri(parentUri +"/" + ae1.getResourceName());
			return ae1;
		} else if (res.getResponseStatusCodeEnum() == RESPONSE_STATUS.CONFLICT || res.getResponseStatusCodeEnum() == RESPONSE_STATUS.ALREADY_EXISTS) {
			// 이미 등록된 경우 AE정보 retrieve
			AE ae1 = (AE)this.retrieveAE(parentUri +"/"+name, from);
			ae1.setUri(parentUri +"/"+name);
			
			// poa가 변경된 경우 AE update
			String curPoa = ae1.getPointOfAccess() != null && ae1.getPointOfAccess().size() > 0 ? ae1.getPointOfAccess().get(0) : null;
			String newPoa = ae.getPointOfAccess() != null && ae.getPointOfAccess().size() > 0 ? ae.getPointOfAccess().get(0) : null;
			if (curPoa != newPoa) {
				AE aeU = new AE();
				aeU.setUri(parentUri +"/" + ae1.getResourceName());
				aeU.addPointOfAccess(newPoa);
				this.updateAE(aeU.getUri(), aeU, from);
				
				if (ae1.getPointOfAccess() != null) {
					ae1.getPointOfAccess().clear();
				}
				ae1.addPointOfAccess(newPoa);
			}			
			
			return ae1;
		} else {
			log.debug("createAE failed. {}", res.toString());
			throw new OneM2MException(res.getResponseStatusCodeEnum(), "Fail to createAE RES:"+res.toString());
		}
		
	}


	protected AccessControlPolicy createACP(String parentUri, String from, AccessControlPolicy acp, String name) throws OneM2MException {
		
		OneM2mRequest request = new OneM2mRequest();
		request.setOperation(OPERATION.CREATE);
		request.setTo(parentUri);
		request.setFrom(from);
		request.setRequestIdentifier(Utils.createRequestId());
		request.setResourceType(RESOURCE_TYPE.ACCESS_CTRL_POLICY);
		request.setContentType(CONTENT_TYPE.RES_XML);
		request.setResultContent(RESULT_CONT.ATTRIBUTE);
		request.setContentObject(acp);
		if (name != null) {
			request.setName(name);
		}
		
		if (this.authController != null) this.authController.setAuthData(request);
		
//		OneM2mResponse res = new HttpClient().process(cseAddr, request);
		
		OneM2mResponse res = HttpClient.getInstance().sendRequest(cseAddr, request);
		
		if (res == null) {
			log.error("createAE failed.");
			throw new OneM2MException(RESPONSE_STATUS.NETWORK_FAILURE, "network failure");
		}		
		
		if (res.getResponseStatusCodeEnum() == RESPONSE_STATUS.CREATED) {			
			AccessControlPolicy acpRes = (AccessControlPolicy)convertToResource(RESOURCE_TYPE.ACCESS_CTRL_POLICY, res);
			acpRes.setUri(parentUri +"/" + acpRes.getResourceName());
			return acpRes;
		} else if (res.getResponseStatusCodeEnum() == RESPONSE_STATUS.CONFLICT || res.getResponseStatusCodeEnum() == RESPONSE_STATUS.ALREADY_EXISTS) {
			return null;
		} else {
			log.debug("createACP failed. {}", res.toString());
			throw new OneM2MException(res.getResponseStatusCodeEnum(), "Fail to createACP RES:"+res.toString());
		}
		
	}
//	private AE retrieveAE(String resUri, String from) throws Exception {
//
//		OneM2mRequest request = new OneM2mRequest();
//		request.setOperation(OPERATION.RETRIEVE);
//		request.setTo(resUri);		// "/casebase/SAE_1"
//		request.setFrom(from);	// "/SAE_1"
//		request.setRequestId(OneM2mUtil.createRequestId());
//		request.setContentType(CONTENT_TYPE.RES_XML);
//		request.setResultContent(RESULT_CONT.ATTRIBUTE);
//		
//		OneM2mResponse res = new HttpClient().processRequest(cseAddr, request);	// "http://localhost:8080"
//		
//		if (res == null) {
//			log.debug("retrieveAE failed.");
//			return null;			
//		}
//		
//		if (res != null && res.getResStatusCode() == RESPONSE_STATUS.OK) {
//			return (AE)convertToResource(RESOURCE_TYPE.AE, new String(res.getContent(), "UTF-8"));
//		} else {
//			log.debug("retrieveAE failed. {}", res.toString());
//			return null;
//		}
//		
//	}
	
	protected Container createContainer(String parentUri, String from, Container container, String name) throws OneM2MException {
		
		OneM2mRequest request = new OneM2mRequest();
		request.setOperation(OPERATION.CREATE);
		request.setTo(parentUri);
		request.setFrom(from);
		request.setRequestIdentifier(Utils.createRequestId());
		request.setResourceType(RESOURCE_TYPE.CONTAINER);
		request.setContentType(CONTENT_TYPE.RES_XML);
		request.setResultContent(RESULT_CONT.ATTRIBUTE);
		request.setContentObject(container);
		if (name != null) {
			request.setName(name);
		}
		
		if (this.authController != null) this.authController.setAuthData(request);
		
//		OneM2mResponse res = new HttpClient().process(cseAddr, request);\
		
		OneM2mResponse res = HttpClient.getInstance().sendRequest(cseAddr, request);
		
		if (res == null) {
			log.error("createContainer failed.");
			throw new OneM2MException(RESPONSE_STATUS.NETWORK_FAILURE, "network failure");
		}
		
		if (res.getResponseStatusCodeEnum() == RESPONSE_STATUS.CREATED) {
			Container cntr = (Container)convertToResource(RESOURCE_TYPE.CONTAINER, res);
			cntr.setUri(parentUri +"/" + cntr.getResourceName());
			return cntr;
		} else if (res.getResponseStatusCodeEnum() == RESPONSE_STATUS.CONFLICT || res.getResponseStatusCodeEnum() == RESPONSE_STATUS.ALREADY_EXISTS) {
			// 이미 등록된 경우 AE정보 retrieve
			Container cntr = (Container)this.retrieveContainer(parentUri +"/"+name, from);
			cntr.setUri(parentUri +"/" + cntr.getResourceName());
			return cntr;
		} else {
			log.error("createContainer failed.");
			throw new OneM2MException(res.getResponseStatusCodeEnum(), "Faile to createContainer RES:"+res.toString());
		}
		
	}
	
	protected ContentInstance createContentInstance(String parentUri, String from, ContentInstance ci, String ciName) throws OneM2MException {
		
		OneM2mRequest request = new OneM2mRequest();
		request.setOperation(OPERATION.CREATE);
		request.setTo(parentUri);
		request.setFrom(from);
		request.setRequestIdentifier(Utils.createRequestId());
		request.setResourceType(RESOURCE_TYPE.CONTENT_INST);
		request.setContentType(CONTENT_TYPE.RES_XML);
		request.setResultContent(RESULT_CONT.ATTRIBUTE);
		request.setContentObject(ci);
		if (ciName != null) {
			request.setName(ciName);
		}
		
		if (this.authController != null) this.authController.setAuthData(request);
		
//		OneM2mResponse res = new HttpClient().process(cseAddr, request);
		
		OneM2mResponse res = HttpClient.getInstance().sendRequest(cseAddr, request);
		
		if (res == null) {
			log.error("createContainer failed.");
			throw new OneM2MException(RESPONSE_STATUS.NETWORK_FAILURE, "network failure");
		}
		
		if (res.getResponseStatusCodeEnum() == RESPONSE_STATUS.CREATED) {
			ContentInstance ci1 = (ContentInstance)convertToResource(RESOURCE_TYPE.CONTENT_INST, res);
			ci1.setUri(parentUri +"/"+ ci1.getResourceName());
			return ci1;
		} else {
			log.error("createContainer failed.");
			throw new OneM2MException(res.getResponseStatusCodeEnum(), "Faile to createContainer RES:null"+res.toString());
		}
		
	}
	
	protected boolean deleteResource(String uri, String from) throws OneM2MException {

		OneM2mRequest request = new OneM2mRequest();
		request.setOperation(OPERATION.RETRIEVE);
		request.setTo(uri);		// "/casebase/SAE_1"
		request.setFrom(from);	// "/SAE_1"
		request.setRequestIdentifier(Utils.createRequestId());
		
		if (this.authController != null) this.authController.setAuthData(request);
		
//		OneM2mResponse res = new HttpClient().process(cseAddr, request);
		
		OneM2mResponse res = HttpClient.getInstance().sendRequest(cseAddr, request);
		
		if (res == null) {
			log.error("deleteResource failed.");
			throw new OneM2MException(RESPONSE_STATUS.NETWORK_FAILURE, "network failure");
		}
		
		if (res.getResponseStatusCodeEnum() == RESPONSE_STATUS.OK) {
			return true;
		} else {
			log.error("deleteResource failed.");
			throw new OneM2MException(res.getResponseStatusCodeEnum(), "Faile to deleteResource RES:"+res.toString());
		}
		
	}
//	public Resource parseContentResource(AbsMessage message, ManagerInterface manager) throws OneM2MException {
//		
//		try {
//			Resource res;
//			String str = new String(message.getContent(), "UTF-8");
//			XMLConvertor<?> xmlCvt;
//			JSONConvertor<?> jsonCvt;
//			switch (message.getContentType()) {
//				case RES_XML:
//					xmlCvt = manager.getXMLConveter();
//					res = (Resource)xmlCvt.unmarshal(str);
//					System.out.println("resourceName: " + res.getResourceName());
//					return res;
//				case RES_JSON:
//					jsonCvt = manager.getJSONConveter();
//					res = (Resource)jsonCvt.unmarshal(str);
//					System.out.println("resourceName: " + res.getResourceName());
//					return res;
//				case XML:	// cannot sure if content is resource or not, so just return null in case exception
//					try {
//						xmlCvt = manager.getXMLConveter();
//						res = (Resource)xmlCvt.unmarshal(str);
//						System.out.println("resourceName: " + res.getResourceName());
//						return res;
//					} catch(Exception e) {
//						e.printStackTrace();
//						return null;
//					}
//				case JSON:	// cannot sure if content is resource or not, so just return null in case exception
//					try {
//						jsonCvt = manager.getJSONConveter();
//						res = (Resource)jsonCvt.unmarshal(str);
//						System.out.println("resourceName: " + res.getResourceName());
//						return res;
//					} catch(Exception e) {
//						e.printStackTrace();
//						return null;
//					}
//					
//				default:
//					return null;
//			}	
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			
//			throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS, "Invalid format");
//		}
//
//	}
}
