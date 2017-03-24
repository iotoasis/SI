package net.herit.iot.onem2m.incse.manager;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.core.util.Utils;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.facility.CfgManager;
import net.herit.iot.onem2m.incse.facility.OneM2mUtil;
import net.herit.iot.onem2m.incse.manager.VirtualManager.PROCESS_RESULT;
import net.herit.iot.onem2m.incse.manager.dao.GroupDAO;
import net.herit.iot.onem2m.resource.AggregatedResponse;
import net.herit.iot.onem2m.resource.Group;
import net.herit.iot.onem2m.resource.ResponsePrimitive;

public class VFanOutPointManager implements VirtualManagerInterface {
	
	private Logger log = LoggerFactory.getLogger(VFanOutPointManager.class);
	private String TAG = VFanOutPointManager.class.getName();

	//public static String KEY = Group.SEMANTIC_FANOUTPOINT_SHORTNAME;
	public static String KEY = Group.FANOUTPOINT_SHORTNAME;
	private static VFanOutPointManager INSTANCE = new VFanOutPointManager();

	private PROCESS_RESULT processResult; 
	
	public PROCESS_RESULT getProcessResult() {
		return processResult;
	}
	
	public static VFanOutPointManager getInstance() {
		return INSTANCE;
	}	

	//private static HashMap<String, VFanOutPointManager> map = new HashMap<String, VFanOutPointManager>();
	
	private VFanOutPointManager() {
		//VirtualManager.getInstance().addManager(Group.FANOUTPOINT_SHORTNAME, this);
	}

	private String getParentUri(String uri) {
		return uri.substring(0, uri.indexOf("/"+VFanOutPointManager.KEY));
	}

	private String getChildUri(String uri) {
		return uri.substring(uri.indexOf("/"+VFanOutPointManager.KEY)+VFanOutPointManager.KEY.length()+1);
	}
	
	private void processEx(AggregatedResponse aggrContent, ResourceManager rm, 
							  OneM2mRequest reqMessage, OneM2mContext context,
							  int count) throws OneM2MException {
		
		int allowed_depth = CfgManager.getInstance().getAllowedSubGroupDepth();
		if (count >= allowed_depth) {
			log.debug(TAG, "Not supported {}-depth subGroup.", allowed_depth);
			return;
		}
		
		String parentUri = getParentUri(reqMessage.getTo());
		String childUri = getChildUri(reqMessage.getTo());
		
		GroupDAO dao = new GroupDAO(context);

		try {
			Group grp = (Group)dao.retrieve(parentUri, null);
			
			if (grp == null) {
				OneM2MException e = new OneM2MException(RESPONSE_STATUS.NOT_FOUND, "Not found:"+ parentUri);
				log.error(TAG, e);
				
				if(count == 0) throw e;
				
				OneM2mResponse resMessage = new OneM2mResponse(e.getResponseStatusCode(), reqMessage);
				ResponsePrimitive resPrim = (ResponsePrimitive)(resMessage);
				aggrContent.addResponsePrimitive(resPrim);
				
				return;
			}
			if (Utils.checkIfExpired(grp.getExpirationTime())) { 
				OneM2MException e = new OneM2MException(RESPONSE_STATUS.NOT_FOUND, "Request resource is already expired:"+parentUri+", "+grp.getExpirationTime());
				log.error(TAG, e);
				
				if(count == 0) throw e;
				
				OneM2mResponse resMessage = new OneM2mResponse(e.getResponseStatusCode(), reqMessage);
				ResponsePrimitive resPrim = (ResponsePrimitive)(resMessage);
				aggrContent.addResponsePrimitive(resPrim);
				
				return;
			}
			
			List<String> acpIdList = grp.getMembersAccessControlPolicyIDs();
			if (OneM2mUtil.checkAccessControlPolicy(reqMessage, reqMessage.getOperationEnum(), 
									OneM2mUtil.extractAccessControlPolicies(acpIdList, context)) == false) {
				OneM2MException e = new OneM2MException(RESPONSE_STATUS.NO_PRIVILEGE, 
						"No privilege of resouce("+parentUri+".fopt) operation("+reqMessage.getOperationEnum()+")");
				log.error(TAG, e);
				
				if(count == 0) throw e;
				
				OneM2mResponse resMessage = new OneM2mResponse(e.getResponseStatusCode(), reqMessage);
				ResponsePrimitive resPrim = (ResponsePrimitive)(resMessage);
				aggrContent.addResponsePrimitive(resPrim);
				
				return;
			}

			count++;
			
			Iterator<String> it = grp.getMemberIDs().iterator();
			int success = 0;
			int failure = 0;
			while (it.hasNext()) {
				
				// Annc인 경우 (member Hosting CSE가 아닐 경우) request를 member Hosting CSE로 전송 
				// TBD
				OneM2mResponse resMessage;
				try {
					
					String resId = it.next();
					reqMessage.setTo(resId+childUri);
					reqMessage.setGroupRequestIdentifier(reqMessage.getRequestIdentifier());
					
					if(rm.getResourceTypeWithTo(resId) == RESOURCE_TYPE.GROUP) {
						reqMessage.setTo(resId+"/"+VFanOutPointManager.KEY+childUri);
						processEx(aggrContent, rm, reqMessage, context, count);
						continue;
					}
					
					resMessage = rm.processEx(reqMessage, true);		
					if (resMessage != null) {						
//							ResponsePrimitive resPrim = new ResponsePrimitive(resMessage);
						ResponsePrimitive resPrim = (ResponsePrimitive)(resMessage);
						aggrContent.addResponsePrimitive(resPrim);
					}
					if (resMessage != null && Utils.isSuccessResponse(resMessage)) {
						success ++;
					} else {
						failure ++;
					}
					
				} catch (OneM2MException e) {
					log.error(TAG, reqMessage.getTo(), e);		
					resMessage = new OneM2mResponse(e.getResponseStatusCode(), reqMessage);
//						ResponsePrimitive resPrim = new ResponsePrimitive(resMessage);
					ResponsePrimitive resPrim = (ResponsePrimitive)(resMessage);
					aggrContent.addResponsePrimitive(resPrim);
					failure ++;
				}
			}
		} catch(OneM2MException e) {
			log.error(TAG, e);
			
			if(count == 0) throw e;
			
			OneM2mResponse resMessage = new OneM2mResponse(e.getResponseStatusCode(), reqMessage);
			ResponsePrimitive resPrim = (ResponsePrimitive)(resMessage);
			aggrContent.addResponsePrimitive(resPrim);
		}
	}
	
	public OneM2mResponse process(OneM2mRequest reqMessage, OneM2mContext context) throws OneM2MException {
		
		processResult = PROCESS_RESULT.PROCESSING;
		
		AggregatedResponse aggrContent = new AggregatedResponse();
		ResourceManager rm = new ResourceManager(context);
		OneM2mResponse aggrResMsg = new OneM2mResponse(RESPONSE_STATUS.OK, reqMessage);
		
		try {

			processEx(aggrContent, rm, reqMessage, context, 0);
			
			if (aggrContent.getResponsePrimitive().size() > 0) {
				RESPONSE_STATUS status = RESPONSE_STATUS.OK;
				switch (reqMessage.getOperationEnum()) {
				case CREATE:	status = RESPONSE_STATUS.CREATED;	break;
				case UPDATE:	status = RESPONSE_STATUS.CHANGED;	break;
				case DELETE:	status = RESPONSE_STATUS.DELETED;	break;
				case RETRIEVE:	status = RESPONSE_STATUS.OK;	break;
				default:
					status = RESPONSE_STATUS.NOT_IMPLEMENTED;
				}
				aggrResMsg.setResponseStatusCodeEnum(status);
				aggrResMsg.setContentObject(aggrContent);
				
				processResult = PROCESS_RESULT.COMPLETED;
				
			} else {
				throw new OneM2MException(RESPONSE_STATUS.BAD_REQUEST, "Response size is zero.");
			}
		} catch(OneM2MException e) {
			throw e;
		} catch(Exception e) {
			e.printStackTrace(); 
			throw new OneM2MException(RESPONSE_STATUS.BAD_REQUEST, e.getMessage());
		} finally {			
			processResult = PROCESS_RESULT.COMPLETED;			
		}

		return aggrResMsg;
	
	};
	
	
//	public OneM2mResponse process(OneM2mRequest reqMessage, OneM2mContext context) throws OneM2MException {
//		
//		processResult = PROCESS_RESULT.PROCESSING;
//		
//		String parentUri = getParentUri(reqMessage.getTo());
//		String childUri = getChildUri(reqMessage.getTo());
//		
//		try {
//			GroupDAO dao = new GroupDAO(context);
//			Group grp = (Group)dao.retrieve(parentUri, null);
//			
//			if (grp == null) {
//				throw new OneM2MException(RESPONSE_STATUS.NOT_FOUND, "Not found:"+ parentUri);
//			}
//			if (Utils.checkIfExpired(grp.getExpirationTime())) { 
//				throw new OneM2MException(RESPONSE_STATUS.NOT_FOUND, "Request resource is already expired:"+parentUri+", "+grp.getExpirationTime());
//			}
//			
//			List<String> acpIdList = grp.getMembersAccessControlPolicyIDs();
//			if (OneM2mUtil.checkAccessControlPolicy(reqMessage, reqMessage.getOperationEnum(), 
//									OneM2mUtil.extractAccessControlPolicies(acpIdList, context)) == false) {
//				throw new OneM2MException(RESPONSE_STATUS.NO_PRIVILEGE, 
//						"No privilege of resouce("+parentUri+".fopt) operation("+reqMessage.getOperationEnum()+")");
//			}
//			
//			AggregatedResponse aggrContent = new AggregatedResponse();
//			ResourceManager rm = new ResourceManager(context);
//			Iterator<String> it = grp.getMemberIDs().iterator();
//			int success = 0;
//			int failure = 0;
//			while (it.hasNext()) {
//				
//				// Annc인 경우 (member Hosting CSE가 아닐 경우) request를 member Hosting CSE로 전송 
//				// TBD
//				OneM2mResponse resMessage;
//				try {
//					
//					String resId = it.next();
//					reqMessage.setTo(resId+childUri);
//					reqMessage.setGroupRequestIdentifier(reqMessage.getRequestIdentifier());
//					
//					resMessage = rm.processEx(reqMessage, true);		
//					if (resMessage != null) {						
////						ResponsePrimitive resPrim = new ResponsePrimitive(resMessage);
//						ResponsePrimitive resPrim = (ResponsePrimitive)(resMessage);
//						aggrContent.addResponsePrimitive(resPrim);
//					}
//					if (resMessage != null && Utils.isSuccessResponse(resMessage)) {
//						success ++;
//					} else {
//						failure ++;
//					}
//					
//				} catch (OneM2MException e) {
//								
//					resMessage = new OneM2mResponse(e.getResponseStatusCode(), reqMessage);
////					ResponsePrimitive resPrim = new ResponsePrimitive(resMessage);
//					ResponsePrimitive resPrim = (ResponsePrimitive)(resMessage);
//					aggrContent.addResponsePrimitive(resPrim);
//					failure ++;
//				}
//				
//			}
//			
//			RESPONSE_STATUS status = RESPONSE_STATUS.OK;
//			switch (reqMessage.getOperationEnum()) {
//			case CREATE:	status = RESPONSE_STATUS.CREATED;	break;
//			case UPDATE:	status = RESPONSE_STATUS.CHANGED;	break;
//			case DELETE:	status = RESPONSE_STATUS.DELETED;	break;
//			case RETRIEVE:	status = RESPONSE_STATUS.OK;	break;
//			default:
//				status = RESPONSE_STATUS.NOT_IMPLEMENTED;
//			}
//		
//			OneM2mResponse aggrResMsg = new OneM2mResponse(status, reqMessage);
//			aggrResMsg.setContentObject(aggrContent);
//			
//			processResult = PROCESS_RESULT.COMPLETED;
//			
//			return aggrResMsg;
//			//context.getNseManager().sendResponseMessage(aggrResMsg);
//			
//			
//			//return PROCESS_RESULT.COMPLETED;
//						
//		} catch(OneM2MException e) {
//			throw e;
//		} catch(Exception e) {
//			e.printStackTrace(); 
//			throw new OneM2MException(RESPONSE_STATUS.BAD_REQUEST, e.getMessage());
//		} finally {			
//			processResult = PROCESS_RESULT.COMPLETED;			
//		}
//		
//		//return PROCESS_RESULT.COMPLETED;
//		
//	};
}
