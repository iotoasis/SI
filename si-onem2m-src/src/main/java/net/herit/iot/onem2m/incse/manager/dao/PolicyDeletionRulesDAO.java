package net.herit.iot.onem2m.incse.manager.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mRequest.RESULT_CONT;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.onem2m.core.convertor.ConvertorFactory;
import net.herit.iot.onem2m.core.convertor.DaoJSONConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.facility.OneM2mUtil;
import net.herit.iot.onem2m.resource.NotificationTargetMgmtPolicyRef;
import net.herit.iot.onem2m.resource.NotificationTargetPolicy;
import net.herit.iot.onem2m.resource.PolicyDeletionRules;
import net.herit.iot.onem2m.resource.RegularResource;
import net.herit.iot.onem2m.resource.Resource;
import net.herit.iot.onem2m.resource.Subscription;

public class PolicyDeletionRulesDAO extends ResourceDAO implements DAOInterface {

	private Logger log = LoggerFactory.getLogger(PolicyDeletionRulesDAO.class);

	public PolicyDeletionRulesDAO(OneM2mContext context) {
		super(context);
	}
	
	@Override
	public String resourceToJson(Resource res) throws OneM2MException {
		try {
			
			DaoJSONConvertor<PolicyDeletionRules> jc = (DaoJSONConvertor<PolicyDeletionRules>)ConvertorFactory.getDaoJSONConvertor(PolicyDeletionRules.class, PolicyDeletionRules.SCHEMA_LOCATION);
			return jc.marshal((PolicyDeletionRules)res);
			
		} catch (Exception e) {
			log.debug("Handled exception", e);
			throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, "Json generation error:"+res.toString());
		}
	}
	
	@Override
	public void create(Resource resource) throws OneM2MException {
	
		PolicyDeletionRules pdrRes = (PolicyDeletionRules)resource;
		String parentID = pdrRes.getParentID();
		
		NotificationTargetPolicyDAO ntpDao = new NotificationTargetPolicyDAO(context);
		NotificationTargetPolicy ntpRes = (NotificationTargetPolicy)ntpDao.retrieve(parentID, null);
		
		int policyDeletionRuleCount = 0;
		for(RegularResource rsc : ntpRes.getPolicyDeletionRulesOrSubscription()) {
			if(rsc.getResourceType() == RESOURCE_TYPE.POLICYDELETIONRULES.Value()) {
				policyDeletionRuleCount++;
			}
		}
		
		if(policyDeletionRuleCount > 2) {
			throw new OneM2MException(RESPONSE_STATUS.CONFLICT, "more than two existing <policyDeletionRules> resources :"+ policyDeletionRuleCount);
		}
		
		super.create(resource);
	}
	
	@Override
	public void update(Resource resource) throws OneM2MException {

		super.update(resource);			
	}

	@Override
	public void delete(String id) throws OneM2MException {
		String resourceID = (String)this.getAttribute(OneM2mUtil.isUri(id) ? URI_KEY : RESID_KEY,  id, RESID_KEY);	

		deleteChild(resourceID);
		
		deleteDocument(OneM2mUtil.isUri(id) ? URI_KEY : RESID_KEY, id);

	}

	@Override
	public Resource retrieve(String id, RESULT_CONT rc) throws OneM2MException {
		return retrieve(OneM2mUtil.isUri(id) ? URI_KEY : RESID_KEY, id, 
				(DaoJSONConvertor<PolicyDeletionRules>)ConvertorFactory.getDaoJSONConvertor(PolicyDeletionRules.class, PolicyDeletionRules.SCHEMA_LOCATION), rc);
	}


}
