package net.herit.iot.onem2m.incse.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.onem2m.bind.http.client.AsyncResponseListener;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.AccessPointManager;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.facility.CfgManager;
import net.herit.iot.onem2m.incse.facility.OneM2mUtil;
import net.herit.iot.onem2m.incse.manager.AbsManager;
import net.herit.iot.onem2m.incse.manager.dao.ResourceDAO;
import net.herit.iot.onem2m.resource.AE;
import net.herit.iot.onem2m.resource.AEAnnc;
import net.herit.iot.onem2m.resource.AccessControlPolicy;
import net.herit.iot.onem2m.resource.AccessControlPolicyAnnc;
import net.herit.iot.onem2m.resource.AnnounceableResource;
import net.herit.iot.onem2m.resource.AnnounceableSubordinateResource;
import net.herit.iot.onem2m.resource.AnnouncedResource;
import net.herit.iot.onem2m.resource.AnnouncedSubordinateResource;
import net.herit.iot.onem2m.resource.CSEBase;
import net.herit.iot.onem2m.resource.Container;
import net.herit.iot.onem2m.resource.ContainerAnnc;
import net.herit.iot.onem2m.resource.ContentInstance;
import net.herit.iot.onem2m.resource.ContentInstanceAnnc;
import net.herit.iot.onem2m.resource.FilterCriteria;
import net.herit.iot.onem2m.resource.Group;
import net.herit.iot.onem2m.resource.GroupAnnc;
import net.herit.iot.onem2m.resource.LocationPolicy;
import net.herit.iot.onem2m.resource.LocationPolicyAnnc;
import net.herit.iot.onem2m.resource.Naming;
import net.herit.iot.onem2m.resource.Node;
import net.herit.iot.onem2m.resource.NodeAnnc;
import net.herit.iot.onem2m.resource.PrimitiveContent;
import net.herit.iot.onem2m.resource.RegularResource;
import net.herit.iot.onem2m.resource.RemoteCSE;
import net.herit.iot.onem2m.resource.RemoteCSEAnnc;
import net.herit.iot.onem2m.resource.RequestPrimitive;
import net.herit.iot.onem2m.resource.Resource;
import net.herit.iot.onem2m.resource.Schedule;
import net.herit.iot.onem2m.resource.ScheduleAnnc;


public class AnnounceController extends AbsController implements AsyncResponseListener {

	private static final String TAG = AnnounceController.class.getName();

	private Logger log = LoggerFactory.getLogger(AnnounceController.class);
	
	public AnnounceController(OneM2mContext context) {
		super(context);
	}
	
	private Document createAnncDoc(ResourceDAO dao, Resource reqRes) throws Exception {
		
		if(reqRes instanceof AnnounceableResource == false && reqRes instanceof AnnounceableSubordinateResource == false) {
			log.debug("resource is not AnnounceableResource or AnnounceableSubordinateResource");
			return null;
		}
		
		Document doc = Document.parse(dao.resourceToJson(reqRes));
		Document anncDoc = new Document(); //Document.parse(dao.resourceToJson(anncRes));
		List<String> announcedAttrs = null;
		Set<String> optionalAnnounced = null;

		if (reqRes instanceof AccessControlPolicy) {
			announcedAttrs = AccessControlPolicyAnnc.MA;
			optionalAnnounced = AccessControlPolicyAnnc.OA;
			anncDoc.append(Naming.RESOURCETYPE_SN, RESOURCE_TYPE.ACCESS_CTRL_POLICY_ANNC.Value());
			
		} else if (reqRes instanceof AE) {
			log.debug("Attr: {}", AEAnnc.MA);
			announcedAttrs = AEAnnc.MA;
			optionalAnnounced = AEAnnc.OA;
			
			anncDoc.append(Naming.RESOURCETYPE_SN, RESOURCE_TYPE.AE_ANNC.Value());
			
		} else if (reqRes instanceof Container) {
			announcedAttrs = ContainerAnnc.MA;
			optionalAnnounced = ContainerAnnc.OA;
			
			anncDoc.append(Naming.RESOURCETYPE_SN, RESOURCE_TYPE.CONTAINER_ANNC.Value());
		
		} else if (reqRes instanceof ContentInstance) {
			announcedAttrs = ContentInstanceAnnc.MA;
			optionalAnnounced = ContentInstanceAnnc.OA;
			
			anncDoc.append(Naming.RESOURCETYPE_SN, RESOURCE_TYPE.CONTENT_INST_ANNC.Value());
			
		} else if (reqRes instanceof Group) {
			announcedAttrs = GroupAnnc.MA;
			optionalAnnounced = GroupAnnc.OA;
			
			anncDoc.append(Naming.RESOURCETYPE_SN, RESOURCE_TYPE.GROUP_ANNC.Value());
		
		} else if (reqRes instanceof LocationPolicy) {
			announcedAttrs = LocationPolicyAnnc.MA;
			optionalAnnounced = LocationPolicyAnnc.OA;
			
			anncDoc.append(Naming.RESOURCETYPE_SN, RESOURCE_TYPE.LOCAT_POLICY_ANNC.Value());
			
		//} else if (resource instanceof MgmtObj) { }
		
		} else if (reqRes instanceof Node) {
			announcedAttrs = NodeAnnc.MA;
			optionalAnnounced = NodeAnnc.OA;
			
			anncDoc.append(Naming.RESOURCETYPE_SN, RESOURCE_TYPE.NODE_ANNC.Value());
			
		} else if (reqRes instanceof RemoteCSE) {
			announcedAttrs = RemoteCSEAnnc.MA;
			optionalAnnounced = RemoteCSEAnnc.OA;
			
			anncDoc.append(Naming.RESOURCETYPE_SN, RESOURCE_TYPE.REMOTE_CSE_ANNC.Value());
			
		} else if (reqRes instanceof Schedule) {
			announcedAttrs = ScheduleAnnc.MA;
			optionalAnnounced = ScheduleAnnc.OA;
			
			anncDoc.append(Naming.RESOURCETYPE_SN, RESOURCE_TYPE.SCHEDULE_ANNC.Value());
				
		} else {
			log.error("resource is not AnnounceableResource instance");
			return null;
		}
		
		List<String> annAttribute = null;
		
		if(reqRes instanceof AnnounceableResource) {
			annAttribute = ((AnnounceableResource)reqRes).getAnnouncedAttribute();	
		} else if(reqRes instanceof AnnounceableSubordinateResource) {
			annAttribute = ((AnnounceableSubordinateResource)reqRes).getAnnouncedAttribute();
		}

		if (annAttribute != null) {
			for (String oa : annAttribute ) { // Optional Attributes checking.
				if (optionalAnnounced.contains(oa)) {
					announcedAttrs.add(oa);
				} else {
					log.error("not exist {} attribute in resource's Optional announced.", oa);
					// return null. TODO: failed..
				}
			}
		}
		
		log.debug("announce All Atrributes: {}", announcedAttrs);
		
		for (String key : announcedAttrs) {
			Object obj = doc.get(key);
			anncDoc.append(key, obj);
		}

		log.debug("res: {}, \r\n annc: {}", doc, anncDoc);
		
		return anncDoc;
	}
	
//	private boolean sendAnnounce(OneM2mRequest reqMessage, Resource resAnnc, List<Object> accessPoints, OPERATION op) {
	private boolean sendAnnounce(OneM2mRequest reqMessage, Resource resAnnc, String addr, OPERATION op) throws Exception {

		boolean result = false;

		OneM2mRequest request = new OneM2mRequest();
		request.setOperation(op);
		request.setTo(reqMessage.getTo());
		request.setFrom(reqMessage.getFrom());
		request.setRequestIdentifier(OneM2mUtil.createRequestId());
		request.setResourceType(RESOURCE_TYPE.get(resAnnc.getResourceType()));
		request.setContentType(reqMessage.getContentType());
		request.setResultContent(reqMessage.getResultContentEnum());
		request.setContentObject(resAnnc);
		
		OneM2mResponse annRes;
//		for (Object obj : accessPoints) {
//			for(String addr : (List<String>)obj) {
				log.debug(addr);
//				URI uri = new URI(addr);

				annRes = this.context.getNseManager().sendRequestMessage(addr, request);
				
//				if(annRes == null) continue;
				if (annRes == null) return result;
				
				log.debug("Announce Responsed {}", annRes.toString());
				switch(annRes.getResponseStatusCodeEnum()) {
				case ACCEPTED:
				case OK:
				case CREATED:
				case DELETED:
				case CHANGED:
					result = true;
					break;
				default:
					log.debug("announce is failed. {}", annRes.getResponseStatusCodeEnum().Name());
				}
//			}					
//		}
		
		return result;
	}
	
	
//	private void sendAnnounceWithPollingChannel(OneM2mRequest reqMessage, Resource resAnnc, List<String> pollingChannels, OPERATION op) {
//		if(pollingChannels.size() == 0) return;
	private void sendAnnounceWithPollingChannel(OneM2mRequest reqMessage, Resource resAnnc, String pcu, OPERATION op) {
		
		RequestPrimitive request = new RequestPrimitive();
		request.setOperation(op.Value());
		request.setTo(reqMessage.getTo());
		request.setFrom(reqMessage.getFrom());
		request.setRequestIdentifier(OneM2mUtil.createRequestId());
		request.setResourceType(resAnnc.getResourceType());
		request.setResultContent(reqMessage.getResultContentEnum().Value());
		PrimitiveContent content = new PrimitiveContent();
		content.getAny().add(resAnnc);
		request.setPrimitiveContent(content);
		
		request.setOriginatingTimestamp(reqMessage.getOriginatingTimestamp());
		request.setRequestExpirationTimestamp(reqMessage.getRequestExpirationTimestamp());
		request.setResultExpirationTimestamp(reqMessage.getResultExpirationTimestamp());
		request.setOperationExecutionTime(reqMessage.getOperationExecutionTime());
		//request.setResponseType(ResponseTypeInfo value);
		//request.setResultPersistence(Duration duration);
		request.setResultContent(reqMessage.getResultContentEnum().Value());
		request.setEventCategory(reqMessage.getEventCategory());
		request.setDeliveryAggregation(reqMessage.isDeliveryAggregation());
		request.setGroupRequestIdentifier(reqMessage.getGroupRequestIdentifier());
		
		if(reqMessage.getFilterCriteria() != null) {
			FilterCriteria filterC = new FilterCriteria();
			filterC.setCreatedAfter(reqMessage.getFilterCriteria().getCreatedAfter());
			filterC.setCreatedBefore(reqMessage.getFilterCriteria().getCreatedBefore());
			filterC.setExpireAfter(reqMessage.getFilterCriteria().getExpireAfter());
			filterC.setExpireBefore(reqMessage.getFilterCriteria().getExpireBefore());
			filterC.setFilterUsage(reqMessage.getFilterCriteria().getFilterUsage());
			filterC.setLimit(reqMessage.getFilterCriteria().getLimit());
			filterC.setModifiedSince(reqMessage.getFilterCriteria().getModifiedSince());
			filterC.setResourceType(reqMessage.getResourceTypeEnum().Value());
			filterC.setSizeAbove(reqMessage.getFilterCriteria().getSizeAbove());
			filterC.setSizeBelow(reqMessage.getFilterCriteria().getSizeBelow());
			filterC.setStateTagBigger(reqMessage.getFilterCriteria().getStateTagBigger());
			filterC.setStateTagSmaller(reqMessage.getFilterCriteria().getStateTagSmaller());
			filterC.setUnmodifiedSince(reqMessage.getFilterCriteria().getUnmodifiedSince());
			request.setFilterCriteria(filterC);
		}
		request.setDiscoveryResultType(reqMessage.getDiscoveryResultTypeEnum().Value());

//		for(String pcu : pollingChannels) {
			try {
				AccessPointManager.getInstance().sendRequest(pcu, request);
			} catch (OneM2MException e) {
				log.debug("AccessPointManager.sendRequest failed. {}", e);
			}
//		}
	}
	
	
	private List<String> sendAnnounce(List<String> to_list, Resource resAnnc, OneM2mRequest reqMessage, ResourceDAO dao, OPERATION op
			) throws Exception {
		
		List<String> successAnnounceTo = new ArrayList<String>();
		
		for(String to : to_list) {
			
			List<String> targetURI = new ArrayList<String>();
			
			if(to.startsWith("http")) {
				log.debug("AnnouncedAttribute is Address, sendAnnonce to ===> {}", to);
//				if(sendAnnounce(reqMessage, resAnnc, to, op)) {
//					successAnnounceTo.add(to);
//				}
//				continue;
				targetURI.add(to);
			} else {
			
			
				// TODO: requestReachability checking for POA..
				String id = OneM2mUtil.toUriOrResourceId(to);
				List<Object> accessPoints = 
						dao.getAttributesWithUri(OneM2mUtil.isUri(id) ? Naming.URI_KEY : Naming.RESID_KEY, id, Naming.POINTOFACCESS_SN);
				log.debug("to: {}, address size: {}", to, accessPoints.size());
				log.debug("to: {}, address size: {}", to, accessPoints.size());
	//			if(accessPoints.size() > 0) {
	//				if(sendAnnounce(reqMessage, resAnnc, accessPoints, op)) {
	//					successAnnounceTo.add(to);
	//				}
	//			} else {
	//				List<String> pollingChannels = dao.getPollingChannelUriWithUri(to);
	//				log.debug("PollingChannelUri: {}", pollingChannels);
	//				
	//				sendAnnounceWithPollingChannel(reqMessage, resAnnc, pollingChannels, op);
	//			}
				
				for (Object obj : accessPoints) {
					for(String addr : (List<String>)obj) {
						targetURI.add(addr);
					}
					
				}
			}
		
			for(String addr : targetURI) {
				try {
					URI _uri_ = new URI(addr);
					if( !_uri_.getHost().equals(CfgManager.getInstance().getHostname())) {
						if(sendAnnounce(reqMessage, resAnnc, addr, op)) {
							successAnnounceTo.add(to);
						}
					} else {
						if(dao.isVaildPollingChannelUri(_uri_.getPath())) {
							log.debug("PollingChannelUri: {} is exist.", addr);
							
							sendAnnounceWithPollingChannel(reqMessage, resAnnc, _uri_.getPath(), op);	
						} else {
							log.warn("PollingChannel not exist. {}", addr);
						}
					}
				} catch (Exception e) {
					log.error("sendAnnounce exceptione. ", e);
					continue;
				}
			}
		}
		
		return successAnnounceTo;
	}
//
//	private void sendUpdateAnnounce(List<String> to, Resource resAnnc, OneM2mRequest reqMessage, ResourceDAO dao) throws Exception {
//		
//	}
//	
//	private void sendDeleteAnnounce(List<String> to, Resource resAnnc, OneM2mRequest reqMessage, ResourceDAO dao) throws Exception {
//		
//	}

	private Resource createResourceWithDoc(ResourceDAO dao, Resource reqRes, Document resAnncDoc, String parentId) throws Exception {
		Resource resAnnc = dao.createResourceWithDoc(resAnncDoc);
		if(resAnnc == null) {
			log.error("Announceable Resource creation failed.");
			return resAnnc;
		}
		if(reqRes instanceof AnnounceableResource) {
			((AnnouncedResource)resAnnc).setLink(reqRes.getUri());
			
			log.debug("parentId={}", parentId);
			List<String> accessControlPolicyIDs = ((AnnounceableResource)reqRes).getAccessControlPolicyIDs();
			if(accessControlPolicyIDs == null || (accessControlPolicyIDs.size() == 0) ) {
				Resource parent = dao.getResourceWithID(parentId);
				/*if(parent instanceof AnnouncedResource) {
					accessControlPolicyIDs = ((AnnouncedResource)parent).getAccessControlPolicyIDs();
				} else */ if(parent instanceof CSEBase) {
					accessControlPolicyIDs = ((CSEBase)parent).getAccessControlPolicyIDs();
				} else if(parent instanceof RegularResource) {
					accessControlPolicyIDs = ((RegularResource)parent).getAccessControlPolicyIDs();
				} else {
					log.debug("Unsupported parent Resource.");
					//return null;
				}
			}
			
			// TODO: if accessControlPolicyIDs.size is 0 then default accessControlPolicyIDs set.
			if(accessControlPolicyIDs == null || accessControlPolicyIDs.size() == 0) {
				// TODO: accessControlPolicyIDs = default AccessControlPolicyIDs...
				return resAnnc;
			}
			for(String accCtlPol : accessControlPolicyIDs) {
				((AnnouncedResource)resAnnc).addAccessControlPolicyIDs(accCtlPol); // .getAccessControlPolicyIDs().add(accCtlPol);
			}
			
		} else if(reqRes instanceof AnnounceableSubordinateResource) {
			((AnnouncedSubordinateResource)resAnnc).setLink(reqRes.getUri());
		}

		return resAnnc;
	}
	

	public void announce(OneM2mRequest reqMessage, Resource reqRes, Resource orgRes, AbsManager manager) {

		try {
			
			ResourceDAO dao = (ResourceDAO)manager.getDAO();
			OPERATION reqOP = reqMessage.getOperationEnum();
			
			if ( reqOP != OPERATION.DELETE &&
					((reqRes instanceof AnnounceableResource) == false && (reqRes instanceof AnnounceableSubordinateResource) == false) ) {
				log.debug("resource is not AnnounceableResource instance");
				return;
			}
			
			log.debug("reqResource: {}, orgResource: {}", reqRes, orgRes);
			Document resAnncDoc = createAnncDoc(dao, reqRes);
			
			if(reqOP != OPERATION.DELETE && resAnncDoc == null) {
				log.error("Announceable Resource's document creation failed.");
				return;
			}

			List<String> createTOs = new ArrayList<String>();
			List<String> updateTOs = new ArrayList<String>();
			List<String> deleteTOs = new ArrayList<String>();
			
			List<String> reqAnncTOs = null;
			List<String> orgAnncTOs = null;
			if (reqRes != null) {
				if(reqRes instanceof AnnounceableResource) {
					reqAnncTOs = ((AnnounceableResource)reqRes).getAnnounceTo();
				} else if (reqRes instanceof AnnounceableSubordinateResource) {
					reqAnncTOs = ((AnnounceableSubordinateResource)reqRes).getAnnounceTo();
				}
			}
			
			if(orgRes != null) {
				if(orgRes instanceof AnnounceableResource) {
					orgAnncTOs = ((AnnounceableResource)orgRes).getAnnounceTo();
				} else if (orgRes instanceof AnnounceableSubordinateResource) {
					orgAnncTOs = ((AnnounceableSubordinateResource)orgRes).getAnnounceTo();
				}
			}
			
			switch (reqMessage.getOperationEnum()) {
			case CREATE:
				if(reqAnncTOs != null) {
					createTOs.addAll(reqAnncTOs);
				}
				break;
			case UPDATE:
				if(orgRes == null) {
					log.error("Original Resource is null.");
					return;
				}
				
//				Set<String> reqTOs = null;
//				Set<String> orgTOs = null;
//				if (reqAnncTOs != null) {
//					reqTOs = new HashSet<String>(((AnnounceableResource)reqRes).getAnnounceTo());
//				}
//				if(((AnnounceableResource)orgRes).getAnnounceTo() != null) {
//					orgTOs = new HashSet<String>(((AnnounceableResource)orgRes).getAnnounceTo());
//				}
				
				if(reqAnncTOs != null) {
					for (String to : reqAnncTOs) {
						if(orgAnncTOs != null && orgAnncTOs.contains(to)) {
							updateTOs.add(to);
							reqAnncTOs.remove(to);
							orgAnncTOs.remove(to);
						}
					}
				}
				
				if(reqAnncTOs != null) createTOs.addAll(reqAnncTOs);
				if(orgAnncTOs != null) deleteTOs.addAll(orgAnncTOs);
				
				break;
			case DELETE:
				if(orgRes == null) {
					log.error("Original Resource is null.");
					return;
				}
				///// 2015.8.19
				if(orgAnncTOs != null) {
					deleteTOs.addAll(orgAnncTOs);
				}
//				if(orgRes instanceof AnnounceableResource) {
//					if(((AnnounceableResource)orgRes).getAnnounceTo() != null)
//						deleteTOs.addAll(((AnnounceableResource)orgRes).getAnnounceTo());	
//				} else if(orgRes instanceof AnnounceableSubordinateResource) {
//					if(((AnnounceableSubordinateResource)orgRes).getAnnounceTo() != null)
//						deleteTOs.addAll(((AnnounceableSubordinateResource)orgRes).getAnnounceTo());
//				}
				
				
				break;
			default:
				log.debug("AnnounceContoller] unsupported operation: {}", reqMessage.getOperationEnum());
				return;
			}

	
//			if (resAnnc instanceof AEAnnc) {
//				log.debug("AE: {}", ((AEAnnc)resAnnc).toString());
//			}

			List<String> resultAnncTo = new ArrayList<String>();
			List<String> resultAttrs = new ArrayList<String>();
			
			if(createTOs.size() > 0) {
				Resource resAnnc = createResourceWithDoc(dao, reqRes, resAnncDoc, reqRes.getParentID());
				resultAnncTo.addAll(sendAnnounce(createTOs, resAnnc, reqMessage, dao, OPERATION.CREATE));
			}
			
			if(deleteTOs.size() > 0) {
				//Resource resAnnc = createResourceWithDoc(dao, reqRes, resAnncDoc);
				resultAnncTo.addAll(sendAnnounce(deleteTOs, null, reqMessage, dao, OPERATION.DELETE));
			}
			
			if(updateTOs.size() > 0) {
				
				Set<String> deletedAttrs = null;
				List<String> orgAnnAttribute = null;
				List<String> reqAnnAttribute = null;
				if(orgRes instanceof AnnounceableResource) {
					orgAnnAttribute = ((AnnounceableResource)orgRes).getAnnouncedAttribute();
					reqAnnAttribute = ((AnnounceableResource)reqRes).getAnnouncedAttribute();
				} else if(orgRes instanceof AnnounceableSubordinateResource) {
					orgAnnAttribute = ((AnnounceableSubordinateResource)orgRes).getAnnouncedAttribute();
					reqAnnAttribute = ((AnnounceableSubordinateResource)reqRes).getAnnouncedAttribute();
				}
				
				if(orgAnnAttribute != null) {
					deletedAttrs = new HashSet<String>(orgAnnAttribute);
				} else {
					deletedAttrs = new HashSet<String>();
				}
				
				if(reqAnnAttribute != null) {
					for(String attr : reqAnnAttribute) {
						deletedAttrs.remove(attr);
					}
				}
				
				if(deletedAttrs.size() > 0) {
					for(String key : deletedAttrs) {
//					TODO:			resAnncDoc.put(key, --NULL--);
					}
				}
				
				Resource resAnnc = createResourceWithDoc(dao, reqRes, resAnncDoc, orgRes.getParentID());
				if(sendAnnounce(updateTOs, resAnnc, reqMessage, dao, OPERATION.UPDATE).size() > 0) {
					if(reqRes instanceof AnnounceableResource)
						resultAttrs.addAll(((AnnounceableResource)reqRes).getAnnouncedAttribute());
					else if(reqRes instanceof AnnounceableSubordinateResource)
						resultAttrs.addAll(((AnnounceableSubordinateResource)reqRes).getAnnouncedAttribute());
				}
				
				
//				Set<String> deletedAttrs = new HashSet<String>(((AnnounceableResource)orgRes).getAnnouncedAttribute());
//				for(String attr : ((AnnounceableResource)reqRes).getAnnouncedAttribute()) {
//					deletedAttrs.remove(attr);
//				}
//				
//				if(deletedAttrs.size() > 0) {
//					for(String key : deletedAttrs) {
////			TODO:			resAnncDoc.put(key, --NULL--);
//					}
//				}
//				Resource resAnnc = createResourceWithDoc(dao, reqRes, resAnncDoc, orgRes.getParentID());
//				if(sendAnnounce(updateTOs, resAnnc, reqMessage, dao, OPERATION.UPDATE).size() > 0) {
//					resultAttrs.addAll(((AnnounceableResource)reqRes).getAnnouncedAttribute());
//				}
			}
			
			if(resultAnncTo.size() > 0 || resultAttrs.size() > 0) {
				
			}

		} catch(Exception e) {
			log.debug(TAG, "Exception {}", e);
		}
	}
	
	@Override
	public OneM2mResponse processRequest(OneM2mRequest reqMessage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean processAsyncRequest(OneM2mRequest reqMessage) {
		// TODO Auto-generated method stub
		
		return false;
	}

	@Override
	public void asyncResponse(OneM2mResponse resMessage) {
		// TODO Auto-generated method stub
		
	}
}
