package net.herit.iot.onem2m.incse.manager;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.onem2m.core.convertor.ConvertorFactory;
import net.herit.iot.onem2m.core.convertor.JSONConvertor;
import net.herit.iot.onem2m.core.convertor.XMLConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.manager.dao.ResourceDAO;
import net.herit.iot.onem2m.resource.AllJoynProperty;
import net.herit.iot.onem2m.resource.FlexContainerResource;
import net.herit.iot.onem2m.resource.Naming;
import net.herit.iot.onem2m.resource.Resource;

public class ManagerFactory {


	static protected ResourceDAO dao = null;
	
	static private RESOURCE_TYPE getResourceTypeWithTo(String to) throws OneM2MException {

		int resType = dao.getResourceType(to);
		return RESOURCE_TYPE.get(resType);
		
	}
	
	static public ManagerInterface create(RESOURCE_TYPE resType, OneM2mContext context) throws OneM2MException {

		ManagerInterface manager = null;
		
		switch (resType) {
		case NONE:
			throw new OneM2MException(RESPONSE_STATUS.NOT_IMPLEMENTED, "No resource type specified");

		case ACCESS_CTRL_POLICY:
			manager = new AccessControlPolicyManager(context);
			break;

		case AE:
			manager = new AEManager(context);
			break;
			
		case CONTAINER:
			manager = new ContainerManager(context);
			break;
			
		case CONTENT_INST:
			manager = new ContentInstanceManager(context);			
			break;
		
		case CSE_BASE:
			manager = new CSEBaseManager(context);
			break;
			
		case DELIVERY:
			manager = new DeliveryManager(context);	
			break;
		
		case EVENT_CONFIT:
			throw new OneM2MException(RESPONSE_STATUS.NOT_IMPLEMENTED, "EVENT_CONFIT not implemented");	
//			break;
			
		case EXEC_INST:
			manager = new ExecInstanceManager(context);			
			break;
			
		case GROUP:
			manager = new GroupManager(context);			
			break;
			
		case LOCAT_POLICY:
			throw new OneM2MException(RESPONSE_STATUS.NOT_IMPLEMENTED, "LOCAT_POLICY not implemented");	
//			break;
			
		case M2M_SVC_SUBSC_PROF:
			throw new OneM2MException(RESPONSE_STATUS.NOT_IMPLEMENTED, "M2M_SVC_SUBSC_PROF not implemented");	
//			break;
		
		case MGMT_CMD:
			manager = new MgmtCmdManager(context);				
			break;
			
		case MGMT_OBJ:
			manager = new MgmtObjManager(context);			
			break;

		case MGMT_FIRMWARE:
			manager = new MgmtFirmwareManager(context);			
			break;
			
		case MGMT_SOFTWARE:
			manager = new MgmtSoftwareManager(context);			
			break;
			
		case MGMT_MEMORY:
			manager = new MgmtMemoryManager(context);			
			break;
			
		case MGMT_AREANWK_INFO:
			manager = new MgmtAreaNwkInfoManager(context);			
			break;
			
		case MGMT_AREANWK_DEVICEINFO:
			manager = new MgmtAreaNwkDeviceInfoManager(context);			
			break;
			
		case MGMT_BATTERY:
			manager = new MgmtBatteryManager(context);			
			break;
			
		case MGMT_DEVICE_INFO:
			manager = new MgmtDeviceInfoManager(context);			
			break;
			
		case MGMT_DEVICE_CAPA:
			manager = new MgmtDeviceCapabilityManager(context);			
			break;
			
		case MGMT_REBOOT:
			manager = new MgmtRebootManager(context);			
			break;
			
		case MGMT_EVENT_LOG:
			manager = new MgmtEventLogManager(context);			
			break;
			
		case NODE:
			manager = new NodeManager(context);			
			break;
			
		case POLLING_CHANN:
			manager = new PollingChannelManager(context);
			//throw new OneM2MException(RESPONSE_STATUS.NOT_IMPLEMENTED, "POLLING_CHANN not implemented");
			break;
			
		case REMOTE_CSE:
			manager = new RemoteCSEManager(context);
			break;
			
		case REQUEST:
			manager = new RequestManager(context);			
			break;
			
		case SCHEDULE:
			manager = new ScheduleManager(context);			
			break;

		case SVC_SUBSC_APP_RULE:
			throw new OneM2MException(RESPONSE_STATUS.NOT_IMPLEMENTED, "SVC_SUBSC_APP_RULE not implemented");			
//			break;
		
		case SVC_SUBSC_NODE:
			throw new OneM2MException(RESPONSE_STATUS.NOT_IMPLEMENTED, "SVC_SUBSC_NODE not implemented");			
//			break;
			
		case STATS_COLLECT:
			throw new OneM2MException(RESPONSE_STATUS.NOT_IMPLEMENTED, "STATS_COLLECT not implemented");			
//			break;
			
		case STATS_CONFIG:
			throw new OneM2MException(RESPONSE_STATUS.NOT_IMPLEMENTED, "STATS_CONFIG not implemented");			
//			break;
			
		case SUBSCRIPTION:
			manager = new SubscriptionManager(context);			
			break;
			
		case SEMANTICDESCRIPTOR:
			manager = new SemanticDescriptorManager(context);			
			break;
		
		case ACCESS_CTRL_POLICY_ANNC:
			manager = new AccessControlPolicyAnncManager(context);			
			break;
		
		case AE_ANNC:
			manager = new AEAnncManager(context);			
			break;
			
		case CONTAINER_ANNC:
			manager = new ContainerAnncManager(context);			
			break;
		
		case CONTENT_INST_ANNC:
			manager = new ContentInstanceAnncManager(context);
			break;
			
		case GROUP_ANNC:
			throw new OneM2MException(RESPONSE_STATUS.NOT_IMPLEMENTED, "GROUP_ANNC not implemented");			
//			break;
			
		case LOCAT_POLICY_ANNC:
			throw new OneM2MException(RESPONSE_STATUS.NOT_IMPLEMENTED, "LOCAT_POLICY_ANNC not implemented");			
//			break;
			
		case MGMT_OBJ_ANNC:
			throw new OneM2MException(RESPONSE_STATUS.NOT_IMPLEMENTED, "MGMT_OBJ_ANNC not implemented");			
//			break;
		
		case NODE_ANNC:
			throw new OneM2MException(RESPONSE_STATUS.NOT_IMPLEMENTED, "NODE_ANNC not implemented");			
//			break;
		
		case REMOTE_CSE_ANNC:
			manager = new RemoteCSEAnncManager(context);
			break;
		
		case SCHEDULE_ANNC:
			throw new OneM2MException(RESPONSE_STATUS.NOT_IMPLEMENTED, "SCHEDULE_ANNC not implemented");			
//			break;
			
		default:
			throw new OneM2MException(RESPONSE_STATUS.NOT_IMPLEMENTED, "No resource type specified");
		}
		
		return manager;
	}
		
	static public ManagerInterface create(OneM2mRequest reqMessage, OneM2mContext context) throws OneM2MException {
		
		if (dao == null) {
			dao = new ResourceDAO(context);
		}
		
		RESOURCE_TYPE resType = null;
		if (reqMessage.getOperationEnum() == OPERATION.CREATE) {
			resType = reqMessage.getResourceTypeEnum();
		} else {
			resType = getResourceTypeWithTo(reqMessage.getTo());
		}
		
		ManagerInterface mgrIf = null;
		
		if( resType == RESOURCE_TYPE.FLEXCONTAINER) {
			FlexContainerResource fcRes = new FlexContainerResource();
			
			 OPERATION op = OPERATION.get(reqMessage.getOperation());
			
			switch(op) {
			case CREATE :
				try {
					String str = new String(reqMessage.getContent(), "UTF-8");
					XMLConvertor<?> xmlCvt;
					JSONConvertor<?> jsonCvt;
					
					switch (reqMessage.getContentType()) {
					case RES_XML:
					case XML:	
						try {
							xmlCvt = ConvertorFactory.getXMLConvertor(FlexContainerResource.class, FlexContainerResource.SCHEMA_LOCATION);
							fcRes = (FlexContainerResource)xmlCvt.unmarshal(str);
							
						} catch(Exception e) {
							e.printStackTrace();
						}
						break;
					case RES_JSON:
					case JSON:
						try {
							jsonCvt = ConvertorFactory.getJSONConvertor(FlexContainerResource.class, FlexContainerResource.SCHEMA_LOCATION);
							fcRes = (FlexContainerResource)jsonCvt.unmarshal(str);
						} catch(Exception e) {
							e.printStackTrace();
						}
						break;
					default:
					}
				} catch (OneM2MException e) {
					throw e;
				} catch (Exception e) {
					e.printStackTrace();
					
					throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS, "Invalid format");
				}
				break;
			case UPDATE :
			case DELETE :
			case RETRIEVE :
				String contDefinition = (String)dao.getAttributeWithUri(reqMessage.getTo(), Naming.CONTAINERDEFINITION_SN );
				fcRes.setContainerDefinition(contDefinition);
				break;
			
			default:
			
			}
			
			
			
			String conDef = fcRes.getContainerDefinition();
			if(conDef.contains("allJoynApp")) {
				mgrIf = new AllJoynAppManager(context);
			} /* else if(conDef.contains("allJoynSvcObject")) {
				mgrIf = new AllJoynSvcObjectManager(context);
			} else if(conDef.contains("allJoynInterface")) {
				mgrIf = new AllJoynInterfaceManager(context);
			} else if(conDef.contains("allJoynMethod")) {
				mgrIf = new AllJoynMethodManager(context);
			} else if(conDef.contains("allJoynMethodCall")) {
				mgrIf = new AllJoynMethodCallManager(context);
			} */else if(conDef.contains("allJoynProperty")) {
				mgrIf = new AllJoynPropertyManager(context);
			} else if(conDef.contains("genericInterworkingService")) {
				mgrIf = new GenericInterworkingServiceManager(context);
			} else if(conDef.contains("genericInterworkingOperationInstance")) {
				mgrIf = new GenericInterworkingOperationInstanceManager(context);
			} else {
				throw new OneM2MException(RESPONSE_STATUS.NOT_IMPLEMENTED, "FlexContainer not implemented");			
			}
		} else {
			mgrIf = ManagerFactory.create(resType, context);
		}
		
		return mgrIf;
		//return ManagerFactory.create(resType, context);

	}	
	
}
