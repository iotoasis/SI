package net.herit.iot.onem2m.incse.manager;

import java.lang.reflect.Method;
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
import net.herit.iot.onem2m.incse.facility.OneM2mUtil;
import net.herit.iot.onem2m.incse.manager.dao.AccessControlPolicyDAO;
import net.herit.iot.onem2m.resource.ChildResourceRef;
import net.herit.iot.onem2m.resource.RegularResource;
import net.herit.iot.onem2m.resource.Resource;

public abstract class AbsManager extends Manager implements ManagerInterface {
	
	//protected static String RESID_PREFIX = "/";
	
	private static final String TAG = AbsManager.class.getName();
	
	private Logger log = LoggerFactory.getLogger(AbsManager.class);

//	public AbsManager() {
//		
//	}
	
	public AbsManager(RESOURCE_TYPE resourceType, String allowed_parent) {
		this.RES_TYPE = resourceType;
		this.ALLOWED_PARENT = allowed_parent;
	}
	
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
	public boolean checkAccessControlPolicy(OneM2mRequest req, OPERATION op, Resource res, Resource parent) throws OneM2MException {
		if (op == OPERATION.CREATE) {
			return OneM2mUtil.checkAccessControlPolicy(req, op, OneM2mUtil.extractAccessControlPolicies(parent,context));
		} else {
			return OneM2mUtil.checkAccessControlPolicy(req, op, OneM2mUtil.extractAccessControlPolicies(res,context));
		}
	}


	public void updateResource(Resource resource, OneM2mRequest req) throws OneM2MException {
		// TODO Auto-generated method stub		
	}

	public void announceTo(OneM2mRequest reqMessage, Resource reqRes, Resource orgRes) throws OneM2MException {
		
	}
	
	public void notification(Resource parentRes, Resource reqRes, Resource orgRes, OPERATION op) throws OneM2MException {
		
	}
	
	public List<ChildResourceRef> extractChildFromRes(Resource res) {
		
		try {
			Method method = res.getClass().getDeclaredMethod("getChildResource", null);
			return (List<ChildResourceRef>)method.invoke(res, new Object[0]);
		} catch (Exception e) {
			// No getChildResource method resource. no problem!!!
		}
		
		return null;
	}

	public String getAllowedParent() {
		return ALLOWED_PARENT;
	}

	public RESOURCE_TYPE getResourceType() {
		return RES_TYPE;
	}
	
	@Override
	public void validateResource(Resource res, OneM2mRequest req, Resource curRes) throws OneM2MException {
		log.debug("AbsManager.validate called:"+ res.getUri() +","+ req.getOperationEnum().Name());

		// Access Control Policy ID속성이 유효한지 체크
//		validateAcpIDs(res);    /2016.05.12
				
		res.validate(req.getOperationEnum());
	}
	
	protected void validateAcpIDs(Resource res) throws OneM2MException {
		
		if (res instanceof RegularResource) {
			
			RegularResource regRes = (RegularResource)res;
			AccessControlPolicyDAO dao = new AccessControlPolicyDAO(this.context);
			
			List<String> acpIds = regRes.getAccessControlPolicyIDs();
			if (acpIds != null) {
				Iterator<String> it = acpIds.iterator();
				while (it.hasNext()) {
					String acpId = it.next();
					if (!dao.checkIfResourceExist(acpId)) {
						throw new OneM2MException(RESPONSE_STATUS.INVALID_ARGUMENTS, "ACP:" +acpId + " does not exist!!!");
					}
				}				
			}
		}
	}
	
}
	
