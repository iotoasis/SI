package net.herit.iot.onem2m.core.convertor;

import java.util.HashMap;

import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.resource.*;


public class ConvertorFactory {

	private static HashMap<Class<?>, JSONConvertor<?>> jsonMap = new HashMap<Class<?>, JSONConvertor<?>>(); 
	private static HashMap<Class<?>, XMLConvertor<?>> xmlMap = new HashMap<Class<?>, XMLConvertor<?>>(); 
	
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
		} else if (t.equals(RestCommandResult.class)) {
			ContInstXC = new JSONConvertor<RestCommandResult>(RestCommandResult.class);
		} else if (t.equals(RestCommandCI.class)) {
			ContInstXC = new JSONConvertor<RestCommandCI>(RestCommandCI.class);
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
		} else {
			throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, "Fail to create XML Convertor for "+ t.getCanonicalName());
		}
		return ContInstXC;	
	}
	
}
