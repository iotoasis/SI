package net.herit.iot.onem2m.ae.hubiss;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.message.onem2m.format.Enums.CONTENT_TYPE;
import net.herit.iot.message.onem2m.format.Enums.FILTER_USAGE;
import net.herit.iot.onem2m.ae.lib.AuthControllerInterface;
import net.herit.iot.onem2m.ae.lib.NotiHandlerInterface;
import net.herit.iot.onem2m.bind.http.client.HttpClient;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.core.util.Utils;
import net.herit.iot.onem2m.resource.ContentInstance;
import net.herit.iot.onem2m.resource.FilterCriteria;
import net.herit.iot.onem2m.resource.UriListContent;

/**
 * AE가 IN-CSE와 연동하기 위한 함수를 제공하는 클래스 
 * @version : 1.0
 * @author  : Lee inseuk
 */
public class AEControllerEx extends AEController {
	
	private Logger log = LoggerFactory.getLogger(AEControllerEx.class);

	private HubissEmulatorHttpListener httpListener;
	private NotiHandlerInterface notiHandler;
	
	// 제어 요청과 결과를 저장한 클래스
	static class Command {
		Command(ContentInstance ciCommand) {
			this.ciCommand = ciCommand;
		}
		public ContentInstance ciCommand;
		public ContentInstance ciResult;
		public ContentInstance getCiCommand() { return ciCommand; }
		public ContentInstance getCiResult() { return ciResult; }
		public void setCiCommand(ContentInstance ci) { this.ciCommand = ci; }
		public void setCiResult(ContentInstance ci) { this.ciResult = ci; }
	}
	// CommandResult를 저장한 해시맵
	private ConcurrentHashMap<String, Command> commandMap = new ConcurrentHashMap<String, Command>();

	protected void putNWaitCommand(String name, Command cmd, long timeout) throws InterruptedException {
		commandMap.put(name, cmd);
		synchronized (cmd) {
			cmd.wait(timeout);
		}
	}
	protected Command setResultNNotifyCommand(ContentInstance ci) {
		
		Command cmd = commandMap.get(ci.getResourceName());
		if (cmd != null) {
			log.debug("Set command result and notify!!!!");
			cmd.setCiResult(ci);
			synchronized (cmd) {
				cmd.notifyAll();
			}
		}
		return cmd;
	}
	protected void removeCommand(Command cmd) throws InterruptedException {
		commandMap.remove(cmd.getCiCommand().getResourceName());
	}
	protected ConcurrentHashMap<String, Command> getCommandMap() {
		return commandMap;
	}
	
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
	public AEControllerEx(String cseAddr, String cseId, String csebaseName, 
						CONTENT_TYPE contentType, AuthControllerInterface authController) {
		
		super(cseAddr, cseId, csebaseName, contentType, authController);
	}	
	

	/**
	  * HTTP 서버를 시작한다.
	  * 	  * 
	  * @param ip - HTTP 서버 IP
	  * @param port - HTTP 서버 포트
	  * @param notiHandler - Notification 처리 클래스 인스턴스
	  * @return 검색결과 데이터 (Content)
	  * @throws OneM2MException 
	  */
	public void doHttpServerStart(String ip, int port, NotiHandlerInterface notiHandler) throws Exception {
				
		this.notiHandler = notiHandler;
		this.httpListener = new HubissEmulatorHttpListener(ip, port, this, notiHandler);
		this.httpListener.start();
		
	}

	/**
	  * HTTP 서버를 종료한다.
	  * 	  * 
	  * @throws Exception 
	  */
	public void doHttpServerStop() throws Exception {
		this.httpListener.stop();
	}

	
	/**
	  * 제어를 수행한다.
	  * 
	  * # 컨테이너 구조
	  * - gas_0001: 제어 대상 디바이스 AE 
	  *     |- switch: 제어 대상 항목 
	  *          |- Execute: 제어 요청 전송용 container (디바이스가 subscribe 해야 함)
	  *          |- Result: 제어 결과 전송용 container (서비스 애플리케이션이 subscribe 해야 함)
	  * 
	  * # 메시지 규칙
	  * - request: command식별자를 이름으로 contentInstance 생성
	  * - response: request와 동일한 이름으로 contentInstance 생성
	  *   
	  * 	  * 
	  * @param resUri - 제어대상 container id (structured id).
	  * @param from - AE의 ID.    
	  * @param ci - 제어를 위해 전송될 데이터를 포함한 contentInstance
	  * @return 검색결과 데이터 (Content)
	  * @throws OneM2MException 
	  */
	public ContentInstance doControlCommand(String resUri, String from, ContentInstance ci, long timeout) throws OneM2MException {
		
		String ciName = Utils.createRequestId();
		ContentInstance ciCommand = this.createContentInstance(resUri +"/Execute", from, ci, ciName);
		
		log.debug("Command sent (Sent RN:"+ciName+", Recv RN:"+ciCommand.getResourceName()+", Recv Uri:"+ciCommand.getUri()+")");
		
		Command cmd = new Command(ciCommand);
		try {
			this.putNWaitCommand(ciCommand.getResourceName(), cmd, timeout);
			ContentInstance ciResult = cmd.getCiResult();
			if (ciResult == null) {
				log.debug("Timeout:"+ciName);
				try {
					ciResult = this.retrieveContentInstance(resUri+"/Result/"+ciCommand.getResourceName(), from);
					if (ciResult != null) {
						return ciResult;
					}
				} catch (OneM2MException ex) {
					System.out.println("doControlCommand - exception on retrieveContentInstance("+resUri+"/Result/"+
											ciCommand.getResourceName()+"):"+ex.getMessage());
					throw new OneM2MException(RESPONSE_STATUS.COMMAND_TIMEOUT, "Command timeout");
				}
				
			} else {
				log.debug("Recv command result:"+ciName);
				this.removeCommand(cmd);
				return ciResult;
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		return null;
		
	}

	
	/**
	  * 등록된 리소스를 전달된 FilterCriteria를 조건으로 검색한다.
	  * 	  * 
	  * @param resUri - 리소스를 검색할 리소스 URI(structured id).
	  * @param from - AE의 ID.    
	  * @param fc - 검색 조건
	  * @return 검색된 리소스 id 목록
	  * @throws OneM2MException 
	  */
	public List<String> doDiscovery(String resUri, String from, FilterCriteria fc) throws OneM2MException {
		
		UriListContent ulc = this.discovery(resUri, from, fc);
		return ulc.getUriList();
		
	}

	
	protected UriListContent discovery(String resUri, String from, FilterCriteria fc) throws OneM2MException {

		OneM2mRequest request = new OneM2mRequest();
		request.setOperation(OPERATION.DISCOVERY);
		request.setTo(resUri);
		request.setFrom(from);
		request.setRequestIdentifier(Utils.createRequestId());
		request.setContentType(CONTENT_TYPE.RES_JSON);
		//request.setResultContent(RESULT_CONT.ATTRIBUTE);
		fc.setFilterUsage(FILTER_USAGE.DISCOVERY.Value());
		request.setFilterCriteria(fc);
		
		if (this.authController != null) this.authController.setAuthData(request);
				
//		OneM2mResponse res = new HttpClient().process(cseAddr, request);

		OneM2mResponse res = HttpClient.getInstance().sendRequest(cseAddr, request);
		
		if (res == null) {
			log.error("retrieve failed.");
			throw new OneM2MException(RESPONSE_STATUS.NETWORK_FAILURE, "network failure");
		}
		
		if (res.getResponseStatusCodeEnum() == RESPONSE_STATUS.OK) {
			
			try {
				return (UriListContent)convertToUriList(new String(res.getContent(), "UTF-8"));
			} catch (Exception e) {
				throw new OneM2MException(RESPONSE_STATUS.BAD_RESPONSE, "Fail to parse RES:"+e.getMessage());
			}
			
			
		} else {
			log.error("Discovery failed. {}", res.toString());
			throw new OneM2MException(res.getResponseStatusCodeEnum(), "Faile to retrieve RES:"+res.toString());
		}
		
	}
}
