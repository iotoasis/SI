package net.herit.iot.onem2m.incse.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.message.onem2m.format.Enums.CONSISTENCY_STRATEGY;
import net.herit.iot.message.onem2m.format.Enums.MEMBER_TYPE;
import net.herit.iot.onem2m.core.convertor.ConvertorFactory;
import net.herit.iot.onem2m.core.convertor.JSONConvertor;
import net.herit.iot.onem2m.core.convertor.XMLConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.controller.AnnounceController;
import net.herit.iot.onem2m.incse.controller.NotificationController;
import net.herit.iot.onem2m.incse.facility.CfgManager;
import net.herit.iot.onem2m.incse.facility.OneM2mUtil;
import net.herit.iot.onem2m.incse.manager.dao.AccessControlPolicyDAO;
import net.herit.iot.onem2m.incse.manager.dao.DAOInterface;
import net.herit.iot.onem2m.incse.manager.dao.GroupDAO;
import net.herit.iot.onem2m.resource.AccessControlRule;
import net.herit.iot.onem2m.resource.Group;
import net.herit.iot.onem2m.resource.RegularResource;
import net.herit.iot.onem2m.resource.Resource;


public class GroupManager extends AbsManager {
	
	static String ALLOWED_PARENT = "AE,AEAnnc,remoteCSE,remoteCSEAnnc,CSEBase";  
	static RESOURCE_TYPE RES_TYPE = RESOURCE_TYPE.GROUP; 
	
	private Logger log = LoggerFactory.getLogger(GroupManager.class);

	private static final String TAG = GroupManager.class.getName();

	public GroupManager(OneM2mContext context) {
		super(RES_TYPE, ALLOWED_PARENT);
		this.context = context;
	}
	
	@Override
	public OneM2mResponse create(OneM2mRequest reqMessage) throws OneM2MException {
	
		return create(reqMessage, this);
		
	}

	@Override
	public OneM2mResponse retrieve(OneM2mRequest reqMessage) throws OneM2MException {
		
		return retrieve(reqMessage, this);
			
	}

	@Override
	public OneM2mResponse update(OneM2mRequest reqMessage) throws OneM2MException {
		
		return update(reqMessage, this);
		
	}

	@Override
	public OneM2mResponse delete(OneM2mRequest reqMessage) throws OneM2MException {
		
		return delete(reqMessage, this);
	
	}


	@Override
	public OneM2mResponse notify(OneM2mRequest reqMessage) throws OneM2MException {
		// TODO Auto-generated method stub

		return new OneM2mResponse();
	}
	
	@Override
	public void announceTo(OneM2mRequest reqMessage, Resource resource, Resource orgRes) throws OneM2MException {
		new AnnounceController(context).announce(reqMessage, resource, orgRes, this);
	}

	@Override 
	public void notification(Resource parentRes, Resource reqRes, Resource orgRes, OPERATION op) throws OneM2MException {
		Document reqDoc = reqRes == null? null : Document.parse( ((GroupDAO)getDAO()).resourceToJson(reqRes));
		Document orgDoc = orgRes == null? null : Document.parse( ((GroupDAO)getDAO()).resourceToJson(orgRes));
		NotificationController.getInstance().notify(parentRes, reqDoc, reqRes, orgDoc, orgRes, op, this);
	}
	
	
	@Override
	public DAOInterface getDAO() {
		return new GroupDAO(context);
	}

	@Override
	public Class<?> getResourceClass() {
		return Group.class;
	}

	@Override
	public XMLConvertor<?> getXMLConveter() throws OneM2MException {
		return ConvertorFactory.getXMLConvertor(Group.class, Group.SCHEMA_LOCATION);
	}

	@Override
	public JSONConvertor<?> getJSONConveter() throws OneM2MException {
		return ConvertorFactory.getJSONConvertor(Group.class, Group.SCHEMA_LOCATION);
	}
	
	private boolean checkPrivilege(OneM2mRequest req, OPERATION op, RegularResource regRes) throws OneM2MException {
		String from = req.getFrom();
		List<String> acpIdList;
		if (regRes.getResourceType() == RESOURCE_TYPE.GROUP.Value()) {
			Group grpRes = (Group)regRes;
			acpIdList = grpRes.getMembersAccessControlPolicyIDs();
		} else {
			acpIdList = regRes.getAccessControlPolicyIDs();
		}
		if (acpIdList == null || acpIdList.size() == 0) {
			return true;
		}
		
		AccessControlPolicyDAO dao = new AccessControlPolicyDAO(context);		
		Iterator<String> it = acpIdList.iterator();
		
		while (it.hasNext()) {
			List<AccessControlRule> acrList = dao.getAccessControlRuleList(it.next());
			if (acrList == null) continue;
			
			return OneM2mUtil.checkAcessControlRule(acrList, op, req.getOriginator(), from);
			
			/*Iterator<AccessControlRule> acpIt = acrList.iterator();
			
			while (acpIt.hasNext()) {
				AccessControlRule acp = acpIt.next();
				int iop = acp.getAccessControlOperations();
				List<String> oris = acp.getAccessControlOriginators();
				List<AccessControlContexts> accList = acp.getAccessControlContexts();
				if(((iop & op.Value()) > 0) && oris.contains(from)) {
					return true;	
				}
			}*/
		}
		return false;
	}
	
	private void validateAccessControlPolicy(OneM2mRequest req, OPERATION op, List<RegularResource> resList) throws OneM2MException {

		String from = req.getFrom();
		
		Iterator<RegularResource>it = resList.iterator();
		while (it.hasNext()) {
			RegularResource regRes = it.next();
			if (!checkPrivilege(req, op, regRes)) {
				throw new OneM2MException(RESPONSE_STATUS.NO_PRIVILEGE, regRes.getUri() + ":" +from);						
			}
		}
	}

	@Override
	public void validateResource(Resource res, OneM2mRequest req, Resource curRes) throws OneM2MException {
		log.debug("GroupManager.validate called: {}, {}", res.getUri(), req.getOperationEnum().Name());
		
		GroupDAO dao;
		Group grp = (Group)res;
		OPERATION op = req.getOperationEnum(); 
		
		super.validateResource(res, req, curRes);
		
		switch (op) {
		case CREATE:
			// compare number of memberID and maxNrOfMember -> MAX_NUMBER_OF_MEMBER_EXCEEDED
			if (grp.getMemberIDs() == null)	{
				throw new OneM2MException(RESPONSE_STATUS.INSUFFICIENT_ARGUMENT, "memberIDs is null");
			}
			if (grp.getMemberIDs().size() > grp.getMaxNrOfMembers()) {
				throw new OneM2MException(RESPONSE_STATUS.MAX_NUMBER_OF_MEMBER_EXCEEDED, "MemberCount:" + 
										Integer.toString(grp.getMemberIDs().size())+", MaxNumberOfMembers:"+
										Integer.toString(grp.getMaxNrOfMembers()));
			}

			dao = (GroupDAO)this.getDAO();
			
			List<RegularResource> resList = new ArrayList<RegularResource>();
			List<String> midList = grp.getMemberIDs();
			Iterator<String> itMid = midList.iterator();
			while (itMid.hasNext()) {
				String memId = itMid.next();
				RegularResource regRes = dao.retrieveRegularResInfo(OneM2mUtil.toUriOrResourceId(memId));
				if (regRes == null) throw new OneM2MException(RESPONSE_STATUS.BAD_REQUEST, "Member resources("+memId+") does not exist!");
				resList.add(regRes);
			}
			
			// check access control policy
			validateAccessControlPolicy(req, OPERATION.RETRIEVE, resList);
			
			// check member type
			validateMemberType(resList, grp);
			// update memberTypeValidated attribute
			grp.setMemberTypeValidated(true);

			validateACPExist(grp.getMembersAccessControlPolicyIDs());
			
			break;
		case RETRIEVE:
			break;
		case UPDATE:

			dao = (GroupDAO)this.getDAO();
			Group curGroup = (Group)curRes;
			if (curGroup == null) {
				throw new OneM2MException(RESPONSE_STATUS.NOT_FOUND, "Resource("+req.getTo()+") does not exist!");
			}
			
			int maxNumberOfMembers = (grp.getMaxNrOfMembers() != null) ? grp.getMaxNrOfMembers() : curGroup.getMaxNrOfMembers(); 
			
			// compare number of memberID and maxNrOfMember -> MAX_NUMBER_OF_MEMBER_EXCEEDED
			midList = grp.getMemberIDs();
			if (midList == null)	{
				break;
			}
			if (grp.getMemberIDs().size() > maxNumberOfMembers) {
				throw new OneM2MException(RESPONSE_STATUS.MAX_NUMBER_OF_MEMBER_EXCEEDED, "MemberCount:" + 
										Integer.toString(grp.getMemberIDs().size())+", MaxNumberOfMembers:"+
										Integer.toString(maxNumberOfMembers));
			}
			
			resList = new ArrayList<RegularResource>();
			itMid = midList.iterator();
			while (itMid.hasNext()) {
				String memId = itMid.next();
				RegularResource regRes = dao.retrieveRegularResInfo(memId);
				if (regRes == null) throw new OneM2MException(RESPONSE_STATUS.BAD_REQUEST, "Member resources("+memId+") does not exist!!!");
				resList.add(regRes);
			}
			
			// check access control policy
			validateAccessControlPolicy(req, OPERATION.RETRIEVE, resList);
			
			// check member type
			validateMemberType(resList, grp);
			// update memberTypeValidated attribute
			grp.setMemberTypeValidated(true);
		
			validateACPExist(grp.getMembersAccessControlPolicyIDs());

			break;
		case DELETE:
			break;
		case DISCOVERY:
			break;
		case NOTIFY:
			break;
		default:
			//break;
			
		}
	}
	
	private void validateACPExist(List<String> acpIdList) throws OneM2MException {
		if(acpIdList == null) return;
		
		Iterator<String> it = acpIdList.iterator();
		GroupDAO dao = (GroupDAO)this.getDAO();
		while (it.hasNext()) {
			String resId = it.next();
			Resource res = dao.retrieveResInfo(resId);
			if (res == null) {
				throw new OneM2MException(RESPONSE_STATUS.NOT_FOUND, "Resource("+resId+") does not exist!");
			}
		}
	}
		
	private void validateMemberType(List<RegularResource> resList, Group grp) throws OneM2MException {
		
		List<RegularResource> abandoned = new ArrayList<RegularResource>();
		if(grp.getMemberType() == null) return;  // 2016.05.10
		int memberType = grp.getMemberType();
		if (memberType != MEMBER_TYPE.MIXED.Value()) {

			Iterator<RegularResource>it = resList.iterator();
			while (it.hasNext()) {
				RegularResource regRes = it.next();
				
				if (regRes.getResourceType() != memberType) {
					Integer cs = grp.getConsistencyStrategy();
					if (cs == null)	cs = CONSISTENCY_STRATEGY.SET_MIXED.Value();
					
					switch (CONSISTENCY_STRATEGY.get(cs)) {
					case ABANDON_MEMBER:
						abandoned.add(regRes);
						break;
					case ABANDON_GROUP:
						throw new OneM2MException(RESPONSE_STATUS.MEMBER_TYPE_INCONSISTENT, 
								"resName:"+regRes.getResourceName() +", resType:"+ regRes.getResourceType()+
								", memberType:"+grp.getConsistencyStrategy());
						
					case SET_MIXED:
						grp.setConsistencyStrategy(CONSISTENCY_STRATEGY.SET_MIXED.Value());
						break;
					}
				}					
			}
			
			// remove resource which has different type from memberType
			if (abandoned.size() > 0) {
				Iterator<RegularResource> itAb = abandoned.iterator();
				while (itAb.hasNext()) {
					resList.remove(itAb.next());
				}					
			}
		}
		
	}
	
	@Override
	public void updateResource(Resource resource, OneM2mRequest reqMessage) throws OneM2MException {
		
		Group group = (Group)resource;
		group.setCreator(reqMessage.getFrom());
		
		//// TS-0001-XXX-V1_13_1 - 10.1.1.1	Non-registration related CREATE procedure (ExpirationTime..)
		if(group.getExpirationTime() == null) {
			group.setExpirationTime(CfgManager.getInstance().getDefaultExpirationTime());
		}
		// END - Non-registration related CREATE procedure
	}
}
