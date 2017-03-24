package net.herit.iot.onem2m.core.convertor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.resource.*;


public class ConvertorFactory {

	private static HashMap<Class<?>, DaoJSONConvertor<?>> daoJsonMap = new HashMap<Class<?>, DaoJSONConvertor<?>>(); 
	private static HashMap<Class<?>, JSONConvertor<?>> jsonMap = new HashMap<Class<?>, JSONConvertor<?>>(); 
	private static HashMap<Class<?>, XMLConvertor<?>> xmlMap = new HashMap<Class<?>, XMLConvertor<?>>(); 

	private static int maxCiCount = 10;
	private static AtomicInteger curCiIndex;
	private static ArrayList<XMLConvertor<ContentInstance>> xmlCiCvtArr = new ArrayList<XMLConvertor<ContentInstance>>();
	
	public static void initialize(int maxCnt) throws OneM2MException {
		curCiIndex.set(0);
		maxCiCount = maxCnt;
		for (int i = 0; i< maxCiCount; i++) {
			xmlCiCvtArr.add((XMLConvertor<ContentInstance>)createXMLConvertor(ContentInstance.class, ContentInstance.SCHEMA_LOCATION));
		}
	}
	
	public static DaoJSONConvertor<?> getDaoJSONConvertor(Class<?> t, String schema) throws OneM2MException {
		
		DaoJSONConvertor<?> cvt = daoJsonMap.get(t);
		if (cvt == null) {
			cvt = createDaoJSONConvertor(t, schema);
			daoJsonMap.put(t,  cvt);
		}
		
		return cvt;
	}
	
	public static JSONConvertor<?> getJSONConvertor(Class<?> t, String schema) throws OneM2MException {
		
		JSONConvertor<?> cvt = jsonMap.get(t);
		if (cvt == null) {
			cvt = createJSONConvertor(t, schema);
			jsonMap.put(t,  cvt);
		}
		
		return cvt;
	}
	public static XMLConvertor<?> getXMLConvertor(Class<?> t, String schema) throws OneM2MException {

		XMLConvertor<?> cvt = xmlMap.get(t);
		if (cvt == null) {
			cvt = createXMLConvertor(t, schema);
			xmlMap.put(t,  cvt);
		}
		
		return cvt;
	}
	public static XMLConvertor<?> getXMLConvertorCI() throws OneM2MException {
		
		XMLConvertor<ContentInstance> cvt = xmlCiCvtArr.get(curCiIndex.addAndGet(1) % maxCiCount);
		
		return cvt;
	}

	static DaoJSONConvertor<?> createDaoJSONConvertor(Class<?> t, String schema) throws OneM2MException {
		
		DaoJSONConvertor<?> ContInstXC;
		if (t.equals(UriContent.class)) {
			ContInstXC = new DaoJSONConvertor<UriContent>(UriContent.class);
		} else if (t.equals(UriListContent.class)) {
			ContInstXC = new DaoJSONConvertor<UriListContent>(UriListContent.class);
		} else if (t.equals(Notification.class)) {
			//ContInstXC = new JSONConvertor<Notification>(Notification.class);
			ContInstXC = new DaoJSONConvertor<Notification>(Notification.class, 
					new Class[] {Notification.class,UriListContent.class,AccessControlPolicy.class,AE.class,
					Container.class,ContentInstance.class,CSEBase.class,Delivery.class,
					EventConfig.class,ExecInstance.class,Group.class,LocationPolicy.class,
					MgmtCmd.class,Node.class,PollingChannel.class,RemoteCSE.class,Request.class,
					Schedule.class,ServiceSubscribedAppRule.class,ServiceSubscribedNode.class,
					StatsCollect.class,StatsConfig.class,Subscription.class,AccessControlPolicyAnnc.class,
					AEAnnc.class,ContainerAnnc.class,ContentInstanceAnnc.class,GroupAnnc.class,
					LocationPolicyAnnc.class,NodeAnnc.class,RemoteCSEAnnc.class,ScheduleAnnc.class, String.class});
		} else if (t.equals(AggregatedResponse.class)) {
			ContInstXC = new DaoJSONConvertor<AggregatedResponse>(AggregatedResponse.class);
		} else if (t.equals(AggregatedRequest.class)) {
			ContInstXC = new DaoJSONConvertor<AggregatedRequest>(AggregatedRequest.class);
		} else if (t.equals(RequestPrimitive.class)) {
			ContInstXC = new DaoJSONConvertor<RequestPrimitive>(RequestPrimitive.class);
		} else if (t.equals(ResponsePrimitive.class)) {
			ContInstXC = new DaoJSONConvertor<ResponsePrimitive>(ResponsePrimitive.class);
		} else if (t.equals(AE.class)) {
			ContInstXC = new DaoJSONConvertor<AE>(AE.class);
		} else if (t.equals(AEAnnc.class)) {
			ContInstXC = new DaoJSONConvertor<AEAnnc>(AEAnnc.class);
		} else if (t.equals(Container.class)) {
			ContInstXC = new DaoJSONConvertor<Container>(Container.class);
		} else if (t.equals(ContainerAnnc.class)) {
			ContInstXC = new DaoJSONConvertor<ContainerAnnc>(ContainerAnnc.class);
		} else if (t.equals(ContentInstance.class)) {
			ContInstXC = new DaoJSONConvertor<ContentInstance>(ContentInstance.class);
		} else if (t.equals(ContentInstanceAnnc.class)) {
			ContInstXC = new DaoJSONConvertor<ContentInstanceAnnc>(ContentInstanceAnnc.class);
		} else if (t.equals(AccessControlPolicy.class)) {
			ContInstXC = new DaoJSONConvertor<AccessControlPolicy>(AccessControlPolicy.class);
		} else if (t.equals(AccessControlPolicyAnnc.class)) {
			ContInstXC = new DaoJSONConvertor<AccessControlPolicyAnnc>(AccessControlPolicyAnnc.class);
		} else if (t.equals(CSEBase.class)) {
			ContInstXC = new DaoJSONConvertor<CSEBase>(CSEBase.class);			
		} else if (t.equals(Subscription.class)) {
			ContInstXC = new DaoJSONConvertor<Subscription>(Subscription.class);			
		} else if (t.equals(Group.class)) {
			ContInstXC = new DaoJSONConvertor<Group>(Group.class);			
		} else if (t.equals(GroupAnnc.class)) {
			ContInstXC = new DaoJSONConvertor<GroupAnnc>(GroupAnnc.class);			
		} else if (t.equals(PollingChannel.class)) {
			ContInstXC = new DaoJSONConvertor<PollingChannel>(PollingChannel.class);			
		} else if (t.equals(Request.class)) {
			ContInstXC = new DaoJSONConvertor<Request>(Request.class);			
		} else if (t.equals(RemoteCSE.class)) {
			ContInstXC = new DaoJSONConvertor<RemoteCSE>(RemoteCSE.class);
		} else if (t.equals(RemoteCSEAnnc.class)) {
			ContInstXC = new DaoJSONConvertor<RemoteCSEAnnc>(RemoteCSEAnnc.class);
		} else if (t.equals(Node.class)) {
			ContInstXC = new DaoJSONConvertor<Node>(Node.class);			
		} else if (t.equals(NodeAnnc.class)) {
			ContInstXC = new DaoJSONConvertor<NodeAnnc>(NodeAnnc.class);			
		} else if (t.equals(Schedule.class)) {
			ContInstXC = new DaoJSONConvertor<Schedule>(Schedule.class);			
		} else if (t.equals(ScheduleAnnc.class)) {
			ContInstXC = new DaoJSONConvertor<ScheduleAnnc>(ScheduleAnnc.class);
		} else if (t.equals(RestCommand.class)) {
			ContInstXC = new DaoJSONConvertor<RestCommand>(RestCommand.class);
		} else if (t.equals(RestAuth.class)) {
			ContInstXC = new DaoJSONConvertor<RestAuth>(RestAuth.class);
		} else if (t.equals(RestCommandResult.class)) {
			ContInstXC = new DaoJSONConvertor<RestCommandResult>(RestCommandResult.class);
		} else if (t.equals(RestCommandCI.class)) {
			ContInstXC = new DaoJSONConvertor<RestCommandCI>(RestCommandCI.class);
		} else if (t.equals(RestSemanticCI.class)) {
			ContInstXC = new DaoJSONConvertor<RestSemanticCI>(RestSemanticCI.class);
		} else if (t.equals(RestSubscription.class)) {
			ContInstXC = new DaoJSONConvertor<RestSubscription>(RestSubscription.class);
		} else if (t.equals(MgmtResource.class)) {
			ContInstXC = new DaoJSONConvertor<MgmtResource>(MgmtResource.class);			
		} else if (t.equals(Firmware.class)) {
			ContInstXC = new DaoJSONConvertor<Firmware>(Firmware.class);	
		} else if (t.equals(Software.class)) {
			ContInstXC = new DaoJSONConvertor<Software>(Software.class);
		} else if (t.equals(Memory.class)) {
			ContInstXC = new DaoJSONConvertor<Memory>(Memory.class);
		} else if (t.equals(AreaNwkInfo.class)) {
			ContInstXC = new DaoJSONConvertor<AreaNwkInfo>(AreaNwkInfo.class);
		} else if (t.equals(AreaNwkDeviceInfo.class)) {
			ContInstXC = new DaoJSONConvertor<AreaNwkDeviceInfo>(AreaNwkDeviceInfo.class);
		} else if (t.equals(Battery.class)) {
			ContInstXC = new DaoJSONConvertor<Battery>(Battery.class);
		} else if (t.equals(DeviceInfo.class)) {
			ContInstXC = new DaoJSONConvertor<DeviceInfo>(DeviceInfo.class);
		} else if (t.equals(DeviceCapability.class)) {
			ContInstXC = new DaoJSONConvertor<DeviceCapability>(DeviceCapability.class);
		} else if (t.equals(Reboot.class)) {
			ContInstXC = new DaoJSONConvertor<Reboot>(Reboot.class);
		} else if (t.equals(EventLog.class)) {
			ContInstXC = new DaoJSONConvertor<EventLog>(EventLog.class);
		} else if (t.equals(MgmtCmd.class)) {
			ContInstXC = new DaoJSONConvertor<MgmtCmd>(MgmtCmd.class);
		} else if (t.equals(ExecInstance.class)) {
			ContInstXC = new DaoJSONConvertor<ExecInstance>(ExecInstance.class);			
		} else if (t.equals(SemanticDescriptor.class)) {
			ContInstXC = new DaoJSONConvertor<SemanticDescriptor>(SemanticDescriptor.class);			
		} else if (t.equals(FlexContainerResource.class)) {
			ContInstXC = new DaoJSONConvertor<FlexContainerResource>(FlexContainerResource.class);			
		} else if (t.equals(AllJoynApp.class)) {
			ContInstXC = new DaoJSONConvertor<AllJoynApp>(AllJoynApp.class);			
		} else if (t.equals(AllJoynProperty.class)) {
			ContInstXC = new DaoJSONConvertor<AllJoynProperty>(AllJoynProperty.class);			
		} else if (t.equals(AllJoynSvcObject.class)) {
			ContInstXC = new DaoJSONConvertor<AllJoynSvcObject>(AllJoynSvcObject.class);			
		} else if (t.equals(AllJoynInterface.class)) {
			ContInstXC = new DaoJSONConvertor<AllJoynInterface>(AllJoynInterface.class);			
		} else if (t.equals(AllJoynMethod.class)) {
			ContInstXC = new DaoJSONConvertor<AllJoynMethod>(AllJoynMethod.class);			
		} else if (t.equals(AllJoynMethodCall.class)) {
			ContInstXC = new DaoJSONConvertor<AllJoynMethodCall>(AllJoynMethodCall.class);			
		} else if (t.equals(SvcObjWrapper.class)) {
			ContInstXC = new DaoJSONConvertor<SvcObjWrapper>(SvcObjWrapper.class);			
		} else if (t.equals(SvcFwWrapper.class)) {
			ContInstXC = new DaoJSONConvertor<SvcFwWrapper>(SvcFwWrapper.class);			
		} else if (t.equals(GenericInterworkingService.class)) {
			ContInstXC = new DaoJSONConvertor<GenericInterworkingService>(GenericInterworkingService.class);			
		} else if (t.equals(GenericInterworkingOperationInstance.class)) {
			ContInstXC = new DaoJSONConvertor<GenericInterworkingOperationInstance>(GenericInterworkingOperationInstance.class);			
		} else {
			throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, "Fail to create JSON Convertor for "+ t.getCanonicalName());
		}
		return ContInstXC;	
	}
	
	static JSONConvertor<?> createJSONConvertor(Class<?> t, String schema) throws OneM2MException {
		
		JSONConvertor<?> ContInstXC;
		if (t.equals(UriContent.class)) {
			ContInstXC = new JSONConvertor<UriContent>(UriContent.class);
		} else if (t.equals(UriListContent.class)) {
			ContInstXC = new JSONConvertor<UriListContent>(UriListContent.class);
		} else if (t.equals(Notification.class)) {
			//ContInstXC = new JSONConvertor<Notification>(Notification.class);
			ContInstXC = new JSONConvertor<Notification>(Notification.class, 
					new Class[] {Notification.class,UriListContent.class,AccessControlPolicy.class,AE.class,
					Container.class,ContentInstance.class,CSEBase.class,Delivery.class,
					EventConfig.class,ExecInstance.class,Group.class,LocationPolicy.class,
					MgmtCmd.class,Node.class,PollingChannel.class,RemoteCSE.class,Request.class,
					Schedule.class,ServiceSubscribedAppRule.class,ServiceSubscribedNode.class,
					StatsCollect.class,StatsConfig.class,Subscription.class,AccessControlPolicyAnnc.class,
					AEAnnc.class,ContainerAnnc.class,ContentInstanceAnnc.class,GroupAnnc.class,
					LocationPolicyAnnc.class,NodeAnnc.class,RemoteCSEAnnc.class,ScheduleAnnc.class, String.class});
		} else if (t.equals(AggregatedResponse.class)) {
			ContInstXC = new JSONConvertor<AggregatedResponse>(AggregatedResponse.class);
		} else if (t.equals(AggregatedRequest.class)) {
			ContInstXC = new JSONConvertor<AggregatedRequest>(AggregatedRequest.class);
		} else if (t.equals(RequestPrimitive.class)) {
			ContInstXC = new JSONConvertor<RequestPrimitive>(RequestPrimitive.class);
		} else if (t.equals(ResponsePrimitive.class)) {
			ContInstXC = new JSONConvertor<ResponsePrimitive>(ResponsePrimitive.class);
		} else if (t.equals(AE.class)) {
			ContInstXC = new JSONConvertor<AE>(AE.class);
		} else if (t.equals(AEAnnc.class)) {
			ContInstXC = new JSONConvertor<AEAnnc>(AEAnnc.class);
		} else if (t.equals(Container.class)) {
			ContInstXC = new JSONConvertor<Container>(Container.class);
		} else if (t.equals(ContainerAnnc.class)) {
			ContInstXC = new JSONConvertor<ContainerAnnc>(ContainerAnnc.class);
		} else if (t.equals(ContentInstance.class)) {
			ContInstXC = new JSONConvertor<ContentInstance>(ContentInstance.class);
		} else if (t.equals(ContentInstanceAnnc.class)) {
			ContInstXC = new JSONConvertor<ContentInstanceAnnc>(ContentInstanceAnnc.class);
		} else if (t.equals(AccessControlPolicy.class)) {
			ContInstXC = new JSONConvertor<AccessControlPolicy>(AccessControlPolicy.class);
		} else if (t.equals(AccessControlPolicyAnnc.class)) {
			ContInstXC = new JSONConvertor<AccessControlPolicyAnnc>(AccessControlPolicyAnnc.class);
		} else if (t.equals(CSEBase.class)) {
			ContInstXC = new JSONConvertor<CSEBase>(CSEBase.class);			
		} else if (t.equals(Subscription.class)) {
			ContInstXC = new JSONConvertor<Subscription>(Subscription.class);			
		} else if (t.equals(Group.class)) {
			ContInstXC = new JSONConvertor<Group>(Group.class);			
		} else if (t.equals(GroupAnnc.class)) {
			ContInstXC = new JSONConvertor<GroupAnnc>(GroupAnnc.class);			
		} else if (t.equals(PollingChannel.class)) {
			ContInstXC = new JSONConvertor<PollingChannel>(PollingChannel.class);			
		} else if (t.equals(Request.class)) {
			ContInstXC = new JSONConvertor<Request>(Request.class);			
		} else if (t.equals(RemoteCSE.class)) {
			ContInstXC = new JSONConvertor<RemoteCSE>(RemoteCSE.class);
		} else if (t.equals(RemoteCSEAnnc.class)) {
			ContInstXC = new JSONConvertor<RemoteCSEAnnc>(RemoteCSEAnnc.class);
		} else if (t.equals(Node.class)) {
			ContInstXC = new JSONConvertor<Node>(Node.class);			
		} else if (t.equals(NodeAnnc.class)) {
			ContInstXC = new JSONConvertor<NodeAnnc>(NodeAnnc.class);			
		} else if (t.equals(Schedule.class)) {
			ContInstXC = new JSONConvertor<Schedule>(Schedule.class);			
		} else if (t.equals(ScheduleAnnc.class)) {
			ContInstXC = new JSONConvertor<ScheduleAnnc>(ScheduleAnnc.class);
		} else if (t.equals(RestCommand.class)) {
			ContInstXC = new JSONConvertor<RestCommand>(RestCommand.class);
		} else if (t.equals(RestAuth.class)) {												// added by brianmoon at 2016-09-05 
			ContInstXC = new JSONConvertor<RestAuth>(RestAuth.class);
		} else if (t.equals(RestCommandResult.class)) {
			ContInstXC = new JSONConvertor<RestCommandResult>(RestCommandResult.class);
		} else if (t.equals(RestCommandCI.class)) {
			ContInstXC = new JSONConvertor<RestCommandCI>(RestCommandCI.class);
		} else if (t.equals(RestSemanticCI.class)) {
			ContInstXC = new JSONConvertor<RestSemanticCI>(RestSemanticCI.class);
		} else if (t.equals(RestSubscription.class)) {
			ContInstXC = new JSONConvertor<RestSubscription>(RestSubscription.class);
		} else if (t.equals(MgmtResource.class)) {
			ContInstXC = new JSONConvertor<MgmtResource>(MgmtResource.class);			
		} else if (t.equals(Firmware.class)) {
			ContInstXC = new JSONConvertor<Firmware>(Firmware.class);	
		} else if (t.equals(Software.class)) {
			ContInstXC = new JSONConvertor<Software>(Software.class);
		} else if (t.equals(Memory.class)) {
			ContInstXC = new JSONConvertor<Memory>(Memory.class);
		} else if (t.equals(AreaNwkInfo.class)) {
			ContInstXC = new JSONConvertor<AreaNwkInfo>(AreaNwkInfo.class);
		} else if (t.equals(AreaNwkDeviceInfo.class)) {
			ContInstXC = new JSONConvertor<AreaNwkDeviceInfo>(AreaNwkDeviceInfo.class);
		} else if (t.equals(Battery.class)) {
			ContInstXC = new JSONConvertor<Battery>(Battery.class);
		} else if (t.equals(DeviceInfo.class)) {
			ContInstXC = new JSONConvertor<DeviceInfo>(DeviceInfo.class);
		} else if (t.equals(DeviceCapability.class)) {
			ContInstXC = new JSONConvertor<DeviceCapability>(DeviceCapability.class);
		} else if (t.equals(Reboot.class)) {
			ContInstXC = new JSONConvertor<Reboot>(Reboot.class);
		} else if (t.equals(EventLog.class)) {
			ContInstXC = new JSONConvertor<EventLog>(EventLog.class);
		} else if (t.equals(MgmtCmd.class)) {
			ContInstXC = new JSONConvertor<MgmtCmd>(MgmtCmd.class);
		} else if (t.equals(ExecInstance.class)) {
			ContInstXC = new JSONConvertor<ExecInstance>(ExecInstance.class);			
		} else if (t.equals(SemanticDescriptor.class)) {
			ContInstXC = new JSONConvertor<SemanticDescriptor>(SemanticDescriptor.class);			
		} else if (t.equals(FlexContainerResource.class)) {
			ContInstXC = new JSONConvertor<FlexContainerResource>(FlexContainerResource.class);			
		} else if (t.equals(AllJoynApp.class)) {
			ContInstXC = new JSONConvertor<AllJoynApp>(AllJoynApp.class);			
		} else if (t.equals(AllJoynProperty.class)) {
			ContInstXC = new JSONConvertor<AllJoynProperty>(AllJoynProperty.class);			
		} else if (t.equals(AllJoynSvcObject.class)) {
			ContInstXC = new JSONConvertor<AllJoynSvcObject>(AllJoynSvcObject.class);			
		} else if (t.equals(AllJoynInterface.class)) {
			ContInstXC = new JSONConvertor<AllJoynInterface>(AllJoynInterface.class);			
		} else if (t.equals(AllJoynMethod.class)) {
			ContInstXC = new JSONConvertor<AllJoynMethod>(AllJoynMethod.class);			
		} else if (t.equals(AllJoynMethodCall.class)) {
			ContInstXC = new JSONConvertor<AllJoynMethodCall>(AllJoynMethodCall.class);			
		} else if (t.equals(SvcObjWrapper.class)) {
			ContInstXC = new JSONConvertor<SvcObjWrapper>(SvcObjWrapper.class);			
		} else if (t.equals(SvcFwWrapper.class)) {
			ContInstXC = new JSONConvertor<SvcFwWrapper>(SvcFwWrapper.class);			
		} else if (t.equals(GenericInterworkingService.class)) {
			ContInstXC = new JSONConvertor<GenericInterworkingService>(GenericInterworkingService.class);			
		} else if (t.equals(GenericInterworkingOperationInstance.class)) {
			ContInstXC = new JSONConvertor<GenericInterworkingOperationInstance>(GenericInterworkingOperationInstance.class);			
		} else {
			throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, "Fail to create JSON Convertor for "+ t.getCanonicalName());
		}
		return ContInstXC;	
	}
	

	static XMLConvertor<?> createXMLConvertor(Class<?> t, String schema) throws OneM2MException {
		
		XMLConvertor<?> ContInstXC;
		if (t.equals(UriContent.class)) {
			ContInstXC = new XMLConvertor<UriContent>(UriContent.class, schema);
		} else if (t.equals(UriListContent.class)) {
			ContInstXC = new XMLConvertor<UriListContent>(UriListContent.class, schema);
		} else if (t.equals(Notification.class)) {
			//ContInstXC = new XMLConvertor<Notification>(Notification.class, schema);
			ContInstXC = new XMLConvertor<Notification>(Notification.class, schema, 
					new Class[] {Notification.class,UriListContent.class,AccessControlPolicy.class,AE.class,
					Container.class,ContentInstance.class,CSEBase.class,Delivery.class,
					EventConfig.class,ExecInstance.class,Group.class,LocationPolicy.class,
					MgmtCmd.class,Node.class,PollingChannel.class,RemoteCSE.class,Request.class,
					Schedule.class,ServiceSubscribedAppRule.class,ServiceSubscribedNode.class,
					StatsCollect.class,StatsConfig.class,Subscription.class,AccessControlPolicyAnnc.class,
					AEAnnc.class,ContainerAnnc.class,ContentInstanceAnnc.class,GroupAnnc.class,
					LocationPolicyAnnc.class,NodeAnnc.class,RemoteCSEAnnc.class,ScheduleAnnc.class, String.class});
		} else if (t.equals(AggregatedResponse.class)) {
			ContInstXC = new XMLConvertor<AggregatedResponse>(AggregatedResponse.class, schema);
		} else if (t.equals(AggregatedRequest.class)) {
			ContInstXC = new XMLConvertor<AggregatedRequest>(AggregatedRequest.class, schema);
		} else if (t.equals(ResponsePrimitive.class)) {
			ContInstXC = new XMLConvertor<ResponsePrimitive>(ResponsePrimitive.class, schema);
		} else if (t.equals(AE.class)) {
			ContInstXC = new XMLConvertor<AE>(AE.class, schema);
		} else if (t.equals(AEAnnc.class)) {
			ContInstXC = new XMLConvertor<AEAnnc>(AEAnnc.class, schema);
		} else if (t.equals(Container.class)) {
			ContInstXC = new XMLConvertor<Container>(Container.class, schema);
		} else if (t.equals(ContainerAnnc.class)) {
			ContInstXC = new XMLConvertor<ContainerAnnc>(ContainerAnnc.class, schema);
		} else if (t.equals(ContentInstance.class)) {
			ContInstXC = new XMLConvertor<ContentInstance>(ContentInstance.class, schema);
		} else if (t.equals(ContentInstanceAnnc.class)) {
			ContInstXC = new XMLConvertor<ContentInstanceAnnc>(ContentInstanceAnnc.class, schema);
		} else if (t.equals(AccessControlPolicy.class)) {
			ContInstXC = new XMLConvertor<AccessControlPolicy>(AccessControlPolicy.class, schema);
		} else if (t.equals(AccessControlPolicyAnnc.class)) {
			ContInstXC = new XMLConvertor<AccessControlPolicyAnnc>(AccessControlPolicyAnnc.class, schema);
		} else if (t.equals(CSEBase.class)) {
			ContInstXC = new XMLConvertor<CSEBase>(CSEBase.class, schema);	
		} else if (t.equals(Subscription.class)) {
			ContInstXC = new XMLConvertor<Subscription>(Subscription.class, schema);			
		} else if (t.equals(Group.class)) {
			ContInstXC = new XMLConvertor<Group>(Group.class, schema);			
		} else if (t.equals(GroupAnnc.class)) {
			ContInstXC = new XMLConvertor<GroupAnnc>(GroupAnnc.class, schema);			
		} else if (t.equals(PollingChannel.class)) {
			ContInstXC = new XMLConvertor<PollingChannel>(PollingChannel.class, schema);			
		} else if (t.equals(Request.class)) {
			ContInstXC = new XMLConvertor<Request>(Request.class, schema);			
		} else if (t.equals(RemoteCSE.class)) {
			ContInstXC = new XMLConvertor<RemoteCSE>(RemoteCSE.class, schema);			
		} else if (t.equals(RemoteCSEAnnc.class)) {
			ContInstXC = new XMLConvertor<RemoteCSEAnnc>(RemoteCSEAnnc.class, schema);
		} else if (t.equals(Node.class)) {
			ContInstXC = new XMLConvertor<Node>(Node.class, schema);			
		} else if (t.equals(NodeAnnc.class)) {
			ContInstXC = new XMLConvertor<NodeAnnc>(NodeAnnc.class, schema);			
		} else if (t.equals(Schedule.class)) {
			ContInstXC = new XMLConvertor<Schedule>(Schedule.class, schema);	
		} else if (t.equals(ScheduleAnnc.class)) {
			ContInstXC = new XMLConvertor<ScheduleAnnc>(ScheduleAnnc.class, schema);	
		} else if (t.equals(RequestPrimitive.class)) {
			ContInstXC = new XMLConvertor<RequestPrimitive>(RequestPrimitive.class, schema);
		} else if (t.equals(ResponsePrimitive.class)) {
			ContInstXC = new XMLConvertor<ResponsePrimitive>(ResponsePrimitive.class, schema);
		} else if (t.equals(MgmtResource.class)) {
			ContInstXC = new XMLConvertor<MgmtResource>(MgmtResource.class, schema);
		} else if (t.equals(Firmware.class)) {
			ContInstXC = new XMLConvertor<Firmware>(Firmware.class, schema);
		} else if (t.equals(Software.class)) {
			ContInstXC = new XMLConvertor<Software>(Software.class, schema);
		} else if (t.equals(Memory.class)) {
			ContInstXC = new XMLConvertor<Memory>(Memory.class, schema);
		} else if (t.equals(AreaNwkInfo.class)) {
			ContInstXC = new XMLConvertor<AreaNwkInfo>(AreaNwkInfo.class, schema);
		} else if (t.equals(AreaNwkDeviceInfo.class)) {
			ContInstXC = new XMLConvertor<AreaNwkDeviceInfo>(AreaNwkDeviceInfo.class, schema);
		} else if (t.equals(Battery.class)) {
			ContInstXC = new XMLConvertor<Battery>(Battery.class, schema);
		} else if (t.equals(DeviceInfo.class)) {
			ContInstXC = new XMLConvertor<DeviceInfo>(DeviceInfo.class, schema);
		} else if (t.equals(DeviceCapability.class)) {
			ContInstXC = new XMLConvertor<DeviceCapability>(DeviceCapability.class, schema);
		} else if (t.equals(Reboot.class)) {
			ContInstXC = new XMLConvertor<Reboot>(Reboot.class, schema);
		} else if (t.equals(EventLog.class)) {
			ContInstXC = new XMLConvertor<EventLog>(EventLog.class, schema);
		} else if (t.equals(MgmtCmd.class)) {
			ContInstXC = new XMLConvertor<MgmtCmd>(MgmtCmd.class, schema);
		} else if (t.equals(ExecInstance.class)) {
			ContInstXC = new XMLConvertor<ExecInstance>(ExecInstance.class, schema);
		} else if (t.equals(SemanticDescriptor.class)) {
			ContInstXC = new XMLConvertor<SemanticDescriptor>(SemanticDescriptor.class, schema);
		} else if (t.equals(FlexContainerResource.class)) {
			ContInstXC = new XMLConvertor<FlexContainerResource>(FlexContainerResource.class, schema);
		} else if (t.equals(AllJoynApp.class)) {
			ContInstXC = new XMLConvertor<AllJoynApp>(AllJoynApp.class, schema);
		} else if (t.equals(AllJoynProperty.class)) {
			ContInstXC = new XMLConvertor<AllJoynProperty>(AllJoynProperty.class, schema);
		} else if (t.equals(AllJoynSvcObject.class)) {
			ContInstXC = new XMLConvertor<AllJoynSvcObject>(AllJoynSvcObject.class, schema);
		} else if (t.equals(AllJoynInterface.class)) {
			ContInstXC = new XMLConvertor<AllJoynInterface>(AllJoynInterface.class, schema);
		} else if (t.equals(AllJoynMethod.class)) {
			ContInstXC = new XMLConvertor<AllJoynMethod>(AllJoynMethod.class, schema);
		} else if (t.equals(AllJoynMethodCall.class)) {
			ContInstXC = new XMLConvertor<AllJoynMethodCall>(AllJoynMethodCall.class, schema);
		} else if (t.equals(SvcObjWrapper.class)) {
			ContInstXC = new XMLConvertor<SvcObjWrapper>(SvcObjWrapper.class, schema);
		} else if (t.equals(SvcFwWrapper.class)) {
			ContInstXC = new XMLConvertor<SvcFwWrapper>(SvcFwWrapper.class, schema);
		} else {
			throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, "Fail to create XML Convertor for "+ t.getCanonicalName());
		}
		return ContInstXC;	
	}
	
}
