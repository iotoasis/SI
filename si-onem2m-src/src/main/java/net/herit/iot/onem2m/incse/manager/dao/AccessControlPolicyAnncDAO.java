package net.herit.iot.onem2m.incse.manager.dao;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mRequest.RESULT_CONT;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.onem2m.core.convertor.ConvertorFactory;
import net.herit.iot.onem2m.core.convertor.JSONConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.facility.OneM2mUtil;
import net.herit.iot.onem2m.incse.manager.dao.DAOInterface;
import net.herit.iot.onem2m.incse.manager.dao.ResourceDAO;
import net.herit.iot.onem2m.resource.AccessControlPolicyAnnc;
import net.herit.iot.onem2m.resource.AccessControlRule;
import net.herit.iot.onem2m.resource.Resource;
import net.herit.iot.onem2m.resource.SetOfAcrs;
import net.herit.iot.onem2m.resource.AccessControlRule.AccessControlContexts;



public class AccessControlPolicyAnncDAO extends ResourceDAO implements DAOInterface {
	
	private Logger log = LoggerFactory.getLogger(AccessControlPolicyAnncDAO.class);

	public AccessControlPolicyAnncDAO(OneM2mContext context) {
		super(context);
	}

	@Override
	public String resourceToJson(Resource res) throws OneM2MException {
		try {
			
			JSONConvertor<AccessControlPolicyAnnc> jc = (JSONConvertor<AccessControlPolicyAnnc>)ConvertorFactory.getJSONConvertor(AccessControlPolicyAnnc.class, null);
			return jc.marshal((AccessControlPolicyAnnc)res);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
			throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, "Json generation error:"+res.toString());
		}
	}

	@Override
	public void create(Resource resource) throws OneM2MException {
		
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
				ConvertorFactory.getJSONConvertor(AccessControlPolicyAnnc.class, AccessControlPolicyAnnc.SCHEMA_LOCATION), rc);
		
	}

	
	public List<AccessControlRule> getAccessControlRuleList(String acpId) {
		
		Document doc = getDocument(acpId);
		
		if (doc == null)	return null;
		
		SetOfAcrs privileges = makePriviligesObject((Document)doc.get(PRIVILEGES_KEY));
		List<AccessControlRule> acpList = privileges.getAccessControlRule();
		
		return acpList;
		
	}

	private SetOfAcrs makePriviligesObject(Document privDoc) {
		
		SetOfAcrs privileges = new SetOfAcrs();		
		List<AccessControlRule> acrList = privileges.getAccessControlRule();
		
		List<Document> acrDocList = (List<Document>)privDoc.get(ACCCTRL_RULE_KEY);	// "accessControlRule" "acr"
		Iterator<Document> it = acrDocList.iterator();
		while (it.hasNext()) {
			Document acrDoc = it.next();

			AccessControlRule acr = new AccessControlRule();
			
			List<String> acoList = (List<String>)acrDoc.get(ACCCTRL_ORIGS_KEY);	// "accessControlOriginators" "acor"
//			List<String> oriList = acr.getAccessControlOriginators();					
//			oriList.addAll(acoList);	
			for(String aco : acoList) {		// 2015.09.14 modified...
				acr.addAccessControlOriginators(aco);
			}
			
			int acop = (int)acrDoc.get(ACCCTRL_OPERS_KEY);	// "accessControlOperations"
			acr.setAccessControlOperations(acop);			
			
			ArrayList<Document> accDocList = (ArrayList<Document>)acrDoc.get(ACCCTRL_CTXS_KEY);	// "accessControlContexts"
			List<AccessControlContexts> accList = acr.getAccessControlContexts();
						
//			AccessControlContexts accs = new AccessControlContexts();
//			
//			
//			Document ipsDoc =  (Document)accDoc.get(ACCCTRL_IPADDRS_KEY);	// "accessControlPolicyIpAddresses"
//			AccessControlIpAddresses acips = new AccessControlIpAddresses();
//			 
//			List<String> ipv4List = (List<String>)ipsDoc.get(IPV4ADDRS_KEY);	// "ipv4"
//			if (ipv4List != null) acips.getIpv4Addresses().addAll(ipv4List);
//			List<String> ipv6List = (List<String>)ipsDoc.get(IPV6ADDRS_KEY);	// "ipv6"
//			if (ipv6List != null) acips.getIpv6Addresses().addAll(ipv6List);
//			accs.setAccessControlIpAddresses(acips);
//			
//			Document aclrDoc = (Document)accDoc.get(ACCCTRL_LOCREG_KEY);	// "accessControlLocationRegion"
//			LocationRegion lr = new LocationRegion();
//			List<String> ccList = (List<String>)aclrDoc.get(COUNTRYCODE_KEY);	// "countryCode"
//			if (ccList != null) lr.getCountryCode().addAll(ccList);
//			List<Double> crList = (List<Double>)aclrDoc.get(CIRCREGION_KEY);	// "circRegion"
//			if (crList != null) lr.getCircRegion().addAll(crList);
//			accs.setAccessControlLocationRegion(lr);
//			
//
//			List<String> acwList = (List<String>)accDoc.get(ACCCTRL_WINDOW_KEY);	// "accessControlWindow"
//			if (acwList != null) accs.getAccessControlWindow().addAll(acwList);
//			
//			accList.add(accs);
			
			acrList.add(acr);
		}
		
		return privileges;
	}
	
}
