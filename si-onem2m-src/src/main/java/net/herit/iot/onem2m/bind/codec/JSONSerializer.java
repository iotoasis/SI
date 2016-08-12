package net.herit.iot.onem2m.bind.codec;

import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.onem2m.core.convertor.ConvertorFactory;
import net.herit.iot.onem2m.core.convertor.JSONConvertor;
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

public class JSONSerializer extends AbsSerializer {

	@Override
	public String serialize(Object content) throws Exception {
	
		String json = null;
		
		if (content instanceof UriContent || content instanceof UriListContent) {
			
			if (content.getClass().equals(UriContent.class)) {
				//JSONConvertor<UriContent> ContInstXC = (JSONConvertor<UriContent>)ConvertorFactory.getJSONConvertor(UriContent.class, null);					
				//json = ContInstXC.marshal((UriContent)content);
				json = ((UriContent)content).getUri();
				
			} else if (content.getClass().equals(UriListContent.class)) {
				JSONConvertor<UriListContent> ContInstXC = (JSONConvertor<UriListContent>)ConvertorFactory.getJSONConvertor(UriListContent.class, null);					
				json = ContInstXC.marshal((UriListContent)content);
			}
			
		} else if (content instanceof Notification) {
			
			Notification res = (Notification)content;
			JSONConvertor<Notification> ContInstXC = (JSONConvertor<Notification>)ConvertorFactory.getJSONConvertor(Notification.class, Notification.SCHEMA_LOCATION);
			json = ContInstXC.marshal(res);
			
		} else if (content instanceof AggregatedResponse) {
			
			AggregatedResponse res = (AggregatedResponse)content;
			JSONConvertor<AggregatedResponse> ContInstXC = (JSONConvertor<AggregatedResponse>)ConvertorFactory.getJSONConvertor(AggregatedResponse.class, AggregatedResponse.SCHEMA_LOCATION);
			json = ContInstXC.marshal(res);
			
		} else if (content instanceof AggregatedRequest) {
			
			AggregatedRequest res = (AggregatedRequest)content;
			JSONConvertor<AggregatedRequest> ContInstXC = (JSONConvertor<AggregatedRequest>)ConvertorFactory.getJSONConvertor(AggregatedRequest.class, AggregatedRequest.SCHEMA_LOCATION);
			json = ContInstXC.marshal(res);
			
		} else if (content instanceof ResponsePrimitive) {
			
			ResponsePrimitive res = (ResponsePrimitive)content;
			JSONConvertor<ResponsePrimitive> ContInstXC = (JSONConvertor<ResponsePrimitive>)ConvertorFactory.getJSONConvertor(ResponsePrimitive.class, ResponsePrimitive.SCHEMA_LOCATION);
			json = ContInstXC.marshal(res);
			
		} else if (content instanceof RequestPrimitive) {
			
			RequestPrimitive res = (RequestPrimitive)content;
			JSONConvertor<RequestPrimitive> ContInstXC = (JSONConvertor<RequestPrimitive>)ConvertorFactory.getJSONConvertor(RequestPrimitive.class, RequestPrimitive.SCHEMA_LOCATION);
			json = ContInstXC.marshal(res);
			
		} else if (content instanceof Resource) {
			
			Resource res = (Resource)content;
			
			if (content instanceof AE) {
				JSONConvertor<AE> ContInstXC = (JSONConvertor<AE>)ConvertorFactory.getJSONConvertor(AE.class, AE.SCHEMA_LOCATION);
				json = ContInstXC.marshal((AE)res);

			} else if (content instanceof AEAnnc) {
				JSONConvertor<AEAnnc> ContInstXC = (JSONConvertor<AEAnnc>)ConvertorFactory.getJSONConvertor(AEAnnc.class, AEAnnc.SCHEMA_LOCATION);
				json = ContInstXC.marshal((AEAnnc)res);

			} else if (content instanceof Container) {
				JSONConvertor<Container> ContInstXC = (JSONConvertor<Container>)ConvertorFactory.getJSONConvertor(Container.class, Container.SCHEMA_LOCATION);
				json = ContInstXC.marshal((Container)res);

			} else if (content instanceof ContainerAnnc) {
				JSONConvertor<ContainerAnnc> ContInstXC = (JSONConvertor<ContainerAnnc>)ConvertorFactory.getJSONConvertor(ContainerAnnc.class, ContainerAnnc.SCHEMA_LOCATION);
				json = ContInstXC.marshal((ContainerAnnc)res);
				
			} else if (content instanceof ContentInstance) {
				JSONConvertor<ContentInstance> ContInstXC = (JSONConvertor<ContentInstance>)ConvertorFactory.getJSONConvertor(ContentInstance.class, ContentInstance.SCHEMA_LOCATION);
				json = ContInstXC.marshal((ContentInstance)res);
				
			} else if (content instanceof ContentInstanceAnnc) {
				JSONConvertor<ContentInstanceAnnc> ContInstXC = (JSONConvertor<ContentInstanceAnnc>)ConvertorFactory.getJSONConvertor(ContentInstanceAnnc.class, ContentInstanceAnnc.SCHEMA_LOCATION);
				json = ContInstXC.marshal((ContentInstanceAnnc)res);
				
			} else if (content instanceof AccessControlPolicy) {
				JSONConvertor<AccessControlPolicy> ContInstXC = (JSONConvertor<AccessControlPolicy>)ConvertorFactory.getJSONConvertor(AccessControlPolicy.class, AccessControlPolicy.SCHEMA_LOCATION);
				json = ContInstXC.marshal((AccessControlPolicy)res);
				
			} else if (content instanceof AccessControlPolicyAnnc) {
				JSONConvertor<AccessControlPolicyAnnc> ContInstXC = (JSONConvertor<AccessControlPolicyAnnc>)ConvertorFactory.getJSONConvertor(AccessControlPolicyAnnc.class, AccessControlPolicyAnnc.SCHEMA_LOCATION);
				json = ContInstXC.marshal((AccessControlPolicyAnnc)res);

			} else if (content instanceof CSEBase) {
				//JSONConvertor<CSEBase> ContInstXC = new JSONConvertor<CSEBase>(CSEBase.class);					
				JSONConvertor<CSEBase> ContInstXC = (JSONConvertor<CSEBase>)ConvertorFactory.getJSONConvertor(CSEBase.class, CSEBase.SCHEMA_LOCATION);
				json = ContInstXC.marshal((CSEBase)res);	

			} else if (content instanceof Subscription) {
				JSONConvertor<Subscription> ContInstXC = (JSONConvertor<Subscription>)ConvertorFactory.getJSONConvertor(Subscription.class, Subscription.SCHEMA_LOCATION);
				json = ContInstXC.marshal((Subscription)res);	

			} else if (content instanceof Group) {
				JSONConvertor<Group> ContInstXC = (JSONConvertor<Group>)ConvertorFactory.getJSONConvertor(Group.class, Group.SCHEMA_LOCATION);
				json = ContInstXC.marshal((Group)res);	

			} else if (content instanceof GroupAnnc) {
				JSONConvertor<GroupAnnc> ContInstXC = (JSONConvertor<GroupAnnc>)ConvertorFactory.getJSONConvertor(GroupAnnc.class, GroupAnnc.SCHEMA_LOCATION);
				json = ContInstXC.marshal((GroupAnnc)res);

			} else if (content instanceof PollingChannel) {
				JSONConvertor<PollingChannel> ContInstXC = (JSONConvertor<PollingChannel>)ConvertorFactory.getJSONConvertor(PollingChannel.class, PollingChannel.SCHEMA_LOCATION);
				json = ContInstXC.marshal((PollingChannel)res);	

			} else if (content instanceof Request) {
				JSONConvertor<Request> ContInstXC = (JSONConvertor<Request>)ConvertorFactory.getJSONConvertor(Request.class, Request.SCHEMA_LOCATION);
				json = ContInstXC.marshal((Request)res);	

			} else if (content instanceof RemoteCSE) {
				JSONConvertor<RemoteCSE> ContInstXC = (JSONConvertor<RemoteCSE>)ConvertorFactory.getJSONConvertor(RemoteCSE.class, RemoteCSE.SCHEMA_LOCATION);
				json = ContInstXC.marshal((RemoteCSE)res);	

			} else if (content instanceof RemoteCSEAnnc) {
				JSONConvertor<RemoteCSEAnnc> ContInstXC = (JSONConvertor<RemoteCSEAnnc>)ConvertorFactory.getJSONConvertor(RemoteCSEAnnc.class, RemoteCSEAnnc.SCHEMA_LOCATION);
				json = ContInstXC.marshal((RemoteCSEAnnc)res);	

			} else if (content instanceof Node) {
				JSONConvertor<Node> ContInstXC = (JSONConvertor<Node>)ConvertorFactory.getJSONConvertor(Node.class, Node.SCHEMA_LOCATION);
				json = ContInstXC.marshal((Node)res);	

			} else if (content instanceof Schedule) {
				JSONConvertor<Schedule> ContInstXC = (JSONConvertor<Schedule>)ConvertorFactory.getJSONConvertor(Schedule.class, Schedule.SCHEMA_LOCATION);
				json = ContInstXC.marshal((Schedule)res);	

			} else if (content instanceof Firmware) {
				JSONConvertor<Firmware> ContInstXC = (JSONConvertor<Firmware>)ConvertorFactory.getJSONConvertor(Firmware.class, Firmware.SCHEMA_LOCATION);
				json = ContInstXC.marshal((Firmware)res);	

			} else if (content instanceof Software) {
				JSONConvertor<Software> ContInstXC = (JSONConvertor<Software>)ConvertorFactory.getJSONConvertor(Software.class, Software.SCHEMA_LOCATION);
				json = ContInstXC.marshal((Software)res);	

			} else if (content instanceof Memory) {
				JSONConvertor<Memory> ContInstXC = (JSONConvertor<Memory>)ConvertorFactory.getJSONConvertor(Memory.class, Memory.SCHEMA_LOCATION);
				json = ContInstXC.marshal((Memory)res);	

			} else if (content instanceof AreaNwkInfo) {
				JSONConvertor<AreaNwkInfo> ContInstXC = (JSONConvertor<AreaNwkInfo>)ConvertorFactory.getJSONConvertor(AreaNwkInfo.class, AreaNwkInfo.SCHEMA_LOCATION);
				json = ContInstXC.marshal((AreaNwkInfo)res);	

			} else if (content instanceof AreaNwkDeviceInfo) {
				JSONConvertor<AreaNwkDeviceInfo> ContInstXC = (JSONConvertor<AreaNwkDeviceInfo>)ConvertorFactory.getJSONConvertor(AreaNwkDeviceInfo.class, AreaNwkDeviceInfo.SCHEMA_LOCATION);
				json = ContInstXC.marshal((AreaNwkDeviceInfo)res);	

			} else if (content instanceof Battery) {
				JSONConvertor<Battery> ContInstXC = (JSONConvertor<Battery>)ConvertorFactory.getJSONConvertor(Battery.class, Battery.SCHEMA_LOCATION);
				json = ContInstXC.marshal((Battery)res);	

			} else if (content instanceof DeviceInfo) {
				JSONConvertor<DeviceInfo> ContInstXC = (JSONConvertor<DeviceInfo>)ConvertorFactory.getJSONConvertor(DeviceInfo.class, DeviceInfo.SCHEMA_LOCATION);
				json = ContInstXC.marshal((DeviceInfo)res);	

			} else if (content instanceof DeviceCapability) {
				JSONConvertor<DeviceCapability> ContInstXC = (JSONConvertor<DeviceCapability>)ConvertorFactory.getJSONConvertor(DeviceCapability.class, DeviceCapability.SCHEMA_LOCATION);
				json = ContInstXC.marshal((DeviceCapability)res);	

			} else if (content instanceof Reboot) {
				JSONConvertor<Reboot> ContInstXC = (JSONConvertor<Reboot>)ConvertorFactory.getJSONConvertor(Reboot.class, Reboot.SCHEMA_LOCATION);
				json = ContInstXC.marshal((Reboot)res);	

			} else if (content instanceof EventLog) {
				JSONConvertor<EventLog> ContInstXC = (JSONConvertor<EventLog>)ConvertorFactory.getJSONConvertor(EventLog.class, EventLog.SCHEMA_LOCATION);
				json = ContInstXC.marshal((EventLog)res);	

			} else if (content instanceof MgmtCmd) {
				JSONConvertor<MgmtCmd> ContInstXC = (JSONConvertor<MgmtCmd>)ConvertorFactory.getJSONConvertor(MgmtCmd.class, MgmtCmd.SCHEMA_LOCATION);
				json = ContInstXC.marshal((MgmtCmd)res);	

			} else if (content instanceof ExecInstance) {
				JSONConvertor<ExecInstance> ContInstXC = (JSONConvertor<ExecInstance>)ConvertorFactory.getJSONConvertor(ExecInstance.class, ExecInstance.SCHEMA_LOCATION);
				json = ContInstXC.marshal((ExecInstance)res);	
				
			} else {
				throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, "Not implemented ");
			}
			
			
		}
		
		
		return json;
	}

}
