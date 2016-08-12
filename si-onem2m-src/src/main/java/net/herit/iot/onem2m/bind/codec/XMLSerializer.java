package net.herit.iot.onem2m.bind.codec;


import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.onem2m.core.convertor.ConvertorFactory;
import net.herit.iot.onem2m.core.convertor.XMLConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.resource.AE;
import net.herit.iot.onem2m.resource.AEAnnc;
import net.herit.iot.onem2m.resource.AccessControlPolicy;
import net.herit.iot.onem2m.resource.AccessControlPolicyAnnc;
import net.herit.iot.onem2m.resource.AggregatedRequest;
import net.herit.iot.onem2m.resource.AggregatedResponse;
import net.herit.iot.onem2m.resource.AreaNwkDeviceInfo;
import net.herit.iot.onem2m.resource.AreaNwkInfo;
import net.herit.iot.onem2m.resource.Battery;
import net.herit.iot.onem2m.resource.CSEBase;
import net.herit.iot.onem2m.resource.Container;
import net.herit.iot.onem2m.resource.ContainerAnnc;
import net.herit.iot.onem2m.resource.ContentInstance;
import net.herit.iot.onem2m.resource.ContentInstanceAnnc;
import net.herit.iot.onem2m.resource.DeviceCapability;
import net.herit.iot.onem2m.resource.DeviceInfo;
import net.herit.iot.onem2m.resource.EventLog;
import net.herit.iot.onem2m.resource.ExecInstance;
import net.herit.iot.onem2m.resource.Firmware;
import net.herit.iot.onem2m.resource.Group;
import net.herit.iot.onem2m.resource.GroupAnnc;
import net.herit.iot.onem2m.resource.Memory;
import net.herit.iot.onem2m.resource.MgmtCmd;
import net.herit.iot.onem2m.resource.Node;
import net.herit.iot.onem2m.resource.Notification;
import net.herit.iot.onem2m.resource.PollingChannel;
import net.herit.iot.onem2m.resource.Reboot;
import net.herit.iot.onem2m.resource.RemoteCSE;
import net.herit.iot.onem2m.resource.RemoteCSEAnnc;
import net.herit.iot.onem2m.resource.Request;
import net.herit.iot.onem2m.resource.RequestPrimitive;
import net.herit.iot.onem2m.resource.Resource;
import net.herit.iot.onem2m.resource.ResponsePrimitive;
import net.herit.iot.onem2m.resource.Schedule;
import net.herit.iot.onem2m.resource.Software;
import net.herit.iot.onem2m.resource.Subscription;
import net.herit.iot.onem2m.resource.UriContent;
import net.herit.iot.onem2m.resource.UriListContent;

public class XMLSerializer extends AbsSerializer {

	@Override
	public String serialize(Object content) throws Exception {
		
		String xml = null;

		if (content instanceof UriContent || content instanceof UriListContent) {
			
			if (content.getClass().equals(UriContent.class)) {					
				//XMLConvertor<UriContent> ContInstXC = (XMLConvertor<UriContent>)ConvertorFactory.getXMLConvertor(UriContent.class, null);					
				//xml = ContInstXC.marshal((UriContent)content);	//AE.SCHEMA_LOCATION
				xml = ((UriContent)content).getUri();
				
			} else if (content.getClass().equals(UriListContent.class)) {
				XMLConvertor<UriListContent> ContInstXC = (XMLConvertor<UriListContent>)ConvertorFactory.getXMLConvertor(UriListContent.class, null);	
				xml = ContInstXC.marshal((UriListContent)content);
			}
			
//			if (content.getClass().equals(StringContent.class)) {
//				StringContent sc = (StringContent)content;
//				String name = sc.getName();
//				String value = sc.getValue();
//				xml = "<"+name+">"+value+"</"+name+">";
//				
//			} else if (content.getClass().equals(StringListContent.class)) {
//				StringListContent slc = (StringListContent)content;
//				String name = slc.getName();
//				Iterator<String> i = slc.getIterator();
//				
//				xml = "<"+name+">";
//				while (i.hasNext()) {
//					xml += i.next();
//					if (i.hasNext()) {
//						xml += " ";
//					}
//				}
//				xml += "</"+name+">";
//			}
					
		} else if (content instanceof Notification) {
			
			Notification res = (Notification)content;
			XMLConvertor<Notification> ContInstXC = (XMLConvertor<Notification>)ConvertorFactory.getXMLConvertor(Notification.class, Notification.SCHEMA_LOCATION);
			xml = ContInstXC.marshal(res);
			
		} else if (content instanceof AggregatedResponse) {
			
			AggregatedResponse res = (AggregatedResponse)content;
			XMLConvertor<AggregatedResponse> ContInstXC = (XMLConvertor<AggregatedResponse>)ConvertorFactory.getXMLConvertor(AggregatedResponse.class, AggregatedResponse.SCHEMA_LOCATION);
			xml = ContInstXC.marshal(res);

		} else if (content instanceof AggregatedRequest) {
			
			AggregatedRequest res = (AggregatedRequest)content;
			XMLConvertor<AggregatedRequest> ContInstXC = (XMLConvertor<AggregatedRequest>)ConvertorFactory.getXMLConvertor(AggregatedRequest.class, AggregatedRequest.SCHEMA_LOCATION);
			xml = ContInstXC.marshal(res);

		} else if (content instanceof ResponsePrimitive) {
			
			ResponsePrimitive res = (ResponsePrimitive)content;
			XMLConvertor<ResponsePrimitive> ContInstXC = (XMLConvertor<ResponsePrimitive>)ConvertorFactory.getXMLConvertor(ResponsePrimitive.class, ResponsePrimitive.SCHEMA_LOCATION);
			xml = ContInstXC.marshal(res);

		} else if (content instanceof RequestPrimitive) {
			
			RequestPrimitive res = (RequestPrimitive)content;
			XMLConvertor<RequestPrimitive> ContInstXC = (XMLConvertor<RequestPrimitive>)ConvertorFactory.getXMLConvertor(RequestPrimitive.class, RequestPrimitive.SCHEMA_LOCATION);
			xml = ContInstXC.marshal(res);
			
		} else if (content instanceof Resource) {
			
			Resource res = (Resource)content;
			
			//if (res.getResourceType() == RESOURCE_TYPE.AE.Value()) {
			if (content instanceof AE) {
				XMLConvertor<AE> ContInstXC = (XMLConvertor<AE>)ConvertorFactory.getXMLConvertor(AE.class, AE.SCHEMA_LOCATION);
				xml = ContInstXC.marshal((AE)res);

			} else if (content instanceof AEAnnc) {
				XMLConvertor<AEAnnc> ContInstXC = (XMLConvertor<AEAnnc>)ConvertorFactory.getXMLConvertor(AEAnnc.class, AEAnnc.SCHEMA_LOCATION);
				xml = ContInstXC.marshal((AEAnnc)res);

			} else if (content instanceof Container) {
				XMLConvertor<Container> ContInstXC = (XMLConvertor<Container>)ConvertorFactory.getXMLConvertor(Container.class, Container.SCHEMA_LOCATION);
				xml = ContInstXC.marshal((Container)res);

			} else if (content instanceof ContainerAnnc) {
				XMLConvertor<ContainerAnnc> ContInstXC = (XMLConvertor<ContainerAnnc>)ConvertorFactory.getXMLConvertor(ContainerAnnc.class, ContainerAnnc.SCHEMA_LOCATION);
				xml = ContInstXC.marshal((ContainerAnnc)res);

			} else if (content instanceof ContentInstance) {
				XMLConvertor<ContentInstance> ContInstXC = (XMLConvertor<ContentInstance>)ConvertorFactory.getXMLConvertor(ContentInstance.class, ContentInstance.SCHEMA_LOCATION);
				xml = ContInstXC.marshal((ContentInstance)res);

			} else if (content instanceof ContentInstanceAnnc) {
				XMLConvertor<ContentInstanceAnnc> ContInstXC = (XMLConvertor<ContentInstanceAnnc>)ConvertorFactory.getXMLConvertor(ContentInstanceAnnc.class, ContentInstanceAnnc.SCHEMA_LOCATION);
				xml = ContInstXC.marshal((ContentInstanceAnnc)res);

			} else if (content instanceof AccessControlPolicy) {
				XMLConvertor<AccessControlPolicy> ContInstXC = (XMLConvertor<AccessControlPolicy>)ConvertorFactory.getXMLConvertor(AccessControlPolicy.class, AccessControlPolicy.SCHEMA_LOCATION);
				xml = ContInstXC.marshal((AccessControlPolicy)res);

			} else if (content instanceof AccessControlPolicyAnnc) {
				XMLConvertor<AccessControlPolicyAnnc> ContInstXC = (XMLConvertor<AccessControlPolicyAnnc>)ConvertorFactory.getXMLConvertor(AccessControlPolicyAnnc.class, AccessControlPolicyAnnc.SCHEMA_LOCATION);
				xml = ContInstXC.marshal((AccessControlPolicyAnnc)res);

			} else if (content instanceof CSEBase) {
				XMLConvertor<CSEBase> ContInstXC = (XMLConvertor<CSEBase>)ConvertorFactory.getXMLConvertor(CSEBase.class, CSEBase.SCHEMA_LOCATION);
				xml = ContInstXC.marshal((CSEBase)res);	

			} else if (content instanceof Subscription) {
				XMLConvertor<Subscription> ContInstXC = (XMLConvertor<Subscription>)ConvertorFactory.getXMLConvertor(Subscription.class, Subscription.SCHEMA_LOCATION);
				xml = ContInstXC.marshal((Subscription)res);	

			} else if (content instanceof Group) {
				XMLConvertor<Group> ContInstXC = (XMLConvertor<Group>)ConvertorFactory.getXMLConvertor(Group.class, Group.SCHEMA_LOCATION);
				xml = ContInstXC.marshal((Group)res);			

			} else if (content instanceof GroupAnnc) {
				XMLConvertor<GroupAnnc> ContInstXC = (XMLConvertor<GroupAnnc>)ConvertorFactory.getXMLConvertor(GroupAnnc.class, GroupAnnc.SCHEMA_LOCATION);
				xml = ContInstXC.marshal((GroupAnnc)res);		

			} else if (content instanceof PollingChannel) {
				XMLConvertor<PollingChannel> ContInstXC = (XMLConvertor<PollingChannel>)ConvertorFactory.getXMLConvertor(PollingChannel.class, PollingChannel.SCHEMA_LOCATION);
				xml = ContInstXC.marshal((PollingChannel)res);	

			} else if (content instanceof Request) {
				XMLConvertor<Request> ContInstXC = (XMLConvertor<Request>)ConvertorFactory.getXMLConvertor(Request.class, Request.SCHEMA_LOCATION);
				xml = ContInstXC.marshal((Request)res);	

			} else if (content instanceof RemoteCSE) {
				XMLConvertor<RemoteCSE> ContInstXC = (XMLConvertor<RemoteCSE>)ConvertorFactory.getXMLConvertor(RemoteCSE.class, RemoteCSE.SCHEMA_LOCATION);
				xml = ContInstXC.marshal((RemoteCSE)res);

			} else if (content instanceof RemoteCSEAnnc) {
				XMLConvertor<RemoteCSEAnnc> ContInstXC = (XMLConvertor<RemoteCSEAnnc>)ConvertorFactory.getXMLConvertor(RemoteCSEAnnc.class, RemoteCSEAnnc.SCHEMA_LOCATION);
				xml = ContInstXC.marshal((RemoteCSEAnnc)res);	

			} else if (content instanceof Node) {
				XMLConvertor<Node> ContInstXC = (XMLConvertor<Node>)ConvertorFactory.getXMLConvertor(Node.class, Node.SCHEMA_LOCATION);
				xml = ContInstXC.marshal((Node)res);		

			} else if (content instanceof Schedule) {
				XMLConvertor<Schedule> ContInstXC = (XMLConvertor<Schedule>)ConvertorFactory.getXMLConvertor(Schedule.class, Schedule.SCHEMA_LOCATION);
				xml = ContInstXC.marshal((Schedule)res);	
				
			} else if (content instanceof Firmware) {
				XMLConvertor<Firmware> ContInstXC = (XMLConvertor<Firmware>)ConvertorFactory.getXMLConvertor(Firmware.class, Firmware.SCHEMA_LOCATION);
				xml = ContInstXC.marshal((Firmware)res);	
				
			} else if (content instanceof Software) {
				XMLConvertor<Software> ContInstXC = (XMLConvertor<Software>)ConvertorFactory.getXMLConvertor(Software.class, Software.SCHEMA_LOCATION);
				xml = ContInstXC.marshal((Software)res);	
				
			} else if (content instanceof Memory) {
				XMLConvertor<Memory> ContInstXC = (XMLConvertor<Memory>)ConvertorFactory.getXMLConvertor(Memory.class, Memory.SCHEMA_LOCATION);
				xml = ContInstXC.marshal((Memory)res);	
				
			} else if (content instanceof AreaNwkInfo) {
				XMLConvertor<AreaNwkInfo> ContInstXC = (XMLConvertor<AreaNwkInfo>)ConvertorFactory.getXMLConvertor(AreaNwkInfo.class, AreaNwkInfo.SCHEMA_LOCATION);
				xml = ContInstXC.marshal((AreaNwkInfo)res);	
				
			} else if (content instanceof AreaNwkDeviceInfo) {
				XMLConvertor<AreaNwkDeviceInfo> ContInstXC = (XMLConvertor<AreaNwkDeviceInfo>)ConvertorFactory.getXMLConvertor(AreaNwkDeviceInfo.class, AreaNwkDeviceInfo.SCHEMA_LOCATION);
				xml = ContInstXC.marshal((AreaNwkDeviceInfo)res);	
				
			} else if (content instanceof Battery) {
				XMLConvertor<Battery> ContInstXC = (XMLConvertor<Battery>)ConvertorFactory.getXMLConvertor(Battery.class, Battery.SCHEMA_LOCATION);
				xml = ContInstXC.marshal((Battery)res);	
				
			} else if (content instanceof DeviceInfo) {
				XMLConvertor<DeviceInfo> ContInstXC = (XMLConvertor<DeviceInfo>)ConvertorFactory.getXMLConvertor(DeviceInfo.class, DeviceInfo.SCHEMA_LOCATION);
				xml = ContInstXC.marshal((DeviceInfo)res);	
				
			} else if (content instanceof DeviceCapability) {
				XMLConvertor<DeviceCapability> ContInstXC = (XMLConvertor<DeviceCapability>)ConvertorFactory.getXMLConvertor(DeviceCapability.class, DeviceCapability.SCHEMA_LOCATION);
				xml = ContInstXC.marshal((DeviceCapability)res);	
				
			} else if (content instanceof Reboot) {
				XMLConvertor<Reboot> ContInstXC = (XMLConvertor<Reboot>)ConvertorFactory.getXMLConvertor(Reboot.class, Reboot.SCHEMA_LOCATION);
				xml = ContInstXC.marshal((Reboot)res);	
				
			} else if (content instanceof EventLog) {
				XMLConvertor<EventLog> ContInstXC = (XMLConvertor<EventLog>)ConvertorFactory.getXMLConvertor(EventLog.class, EventLog.SCHEMA_LOCATION);
				xml = ContInstXC.marshal((EventLog)res);	
				
			} else if (content instanceof MgmtCmd) {
				XMLConvertor<MgmtCmd> ContInstXC = (XMLConvertor<MgmtCmd>)ConvertorFactory.getXMLConvertor(MgmtCmd.class, MgmtCmd.SCHEMA_LOCATION);
				xml = ContInstXC.marshal((MgmtCmd)res);	
				
			} else if (content instanceof ExecInstance) {
				XMLConvertor<ExecInstance> ContInstXC = (XMLConvertor<ExecInstance>)ConvertorFactory.getXMLConvertor(ExecInstance.class, ExecInstance.SCHEMA_LOCATION);
				xml = ContInstXC.marshal((ExecInstance)res);	
				
			} else {
				throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, "Not implemented: "+content.getClass().getCanonicalName());
			}
			
		} 
		
		
		return xml;
	}

}
