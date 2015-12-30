package net.herit.iot.onem2m.incse.manager.dao;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;

import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESULT_CONT;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.onem2m.core.convertor.ConvertorFactory;
import net.herit.iot.onem2m.core.convertor.JSONConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.facility.OneM2mUtil;
import net.herit.iot.onem2m.incse.manager.VPollingChannelUriManager;
import net.herit.iot.onem2m.incse.manager.dao.DAOInterface;
import net.herit.iot.onem2m.incse.manager.dao.ResourceDAO;
import net.herit.iot.onem2m.resource.AE;
import net.herit.iot.onem2m.resource.AccessControlPolicy;
import net.herit.iot.onem2m.resource.AccessControlRule;
import net.herit.iot.onem2m.resource.AnnounceableSubordinateResource;
import net.herit.iot.onem2m.resource.LocationRegion;
import net.herit.iot.onem2m.resource.Resource;
import net.herit.iot.onem2m.resource.SetOfAcrs;
import net.herit.iot.onem2m.resource.AccessControlRule.AccessControlContexts;
import net.herit.iot.onem2m.resource.AccessControlRule.AccessControlContexts.AccessControlIpAddresses;



public class AccessControlPolicyDAO extends ResourceDAO implements DAOInterface {
	
	private Logger log = LoggerFactory.getLogger(AccessControlPolicyDAO.class);

	public AccessControlPolicyDAO(OneM2mContext context) {
		super(context);
	}

	@Override
	public String resourceToJson(Resource res) throws OneM2MException {
		try {
			
			JSONConvertor<AccessControlPolicy> jc = (JSONConvertor<AccessControlPolicy>)ConvertorFactory.getJSONConvertor(AccessControlPolicy.class, null);
			return jc.marshal((AccessControlPolicy)res);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
			throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, "Json generation error:"+res.toString());
		}
	}

	@Override
	public void create(Resource resource) throws OneM2MException {
		
		super.create(resource);
		
//		try {
//			
//			AccessControlPolicy res = (AccessControlPolicy)resource;
//
//			MongoCollection<Document> collection = context.getDatabaseManager().getCollection("resEntity");
//			
//			Document doc = new Document();
//
//			doc.append("privileges", makePrivilegesDocument(res.getPrivileges()));
//			doc.append("selfPrivileges", makePrivilegesDocument(res.getSelfPrivileges()));		
//			
//			this.appendAnnounceableSubordinateAttributes(doc, res);
//
//			collection.insertOne(doc);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, "Fail to create AccessControlPolicy");
//		}
		
	}

	@Override
	public void update(Resource resource) throws OneM2MException {
		
		super.update(resource);
				
	}

//	@Override
//	public void deleteByUri(String uri) throws OneM2MException {
//		
//		deleteDocument(URI_KEY, uri);
//		
//	}
//
//	@Override
//	public void deleteByResId(String resId) throws OneM2MException {
//		
//		deleteDocument(RESID_KEY, resId);
//		
//	}

	@Override
	public void delete(String id) throws OneM2MException {
		
		String resourceID = (String)this.getAttribute(OneM2mUtil.isUri(id) ? URI_KEY : RESID_KEY,  id, RESID_KEY);	

		deleteChild(resourceID);
		
		deleteDocument(OneM2mUtil.isUri(id) ? URI_KEY : RESID_KEY, id);
		
	}

	@Override
	public Resource retrieve(String id, RESULT_CONT rc) throws OneM2MException {

		return retrieve(OneM2mUtil.isUri(id) ? URI_KEY : RESID_KEY, id, 
				ConvertorFactory.getJSONConvertor(AccessControlPolicy.class, AccessControlPolicy.SCHEMA_LOCATION), rc);
		
	}

	
	public List<AccessControlRule> getAccessControlRuleList(String acpId) {
		
		Document doc = getDocument(acpId);
		
		if (doc == null)	return null;
		
		SetOfAcrs privileges = makePriviligesObject((Document)doc.get(PRIVILEGES_KEY));
		List<AccessControlRule> acpList = privileges.getAccessControlRule();
		
		return acpList;
		
	}

//	public Resource retrieveSimpleByResId(String resId) throws OneM2MException {
//
//		return retrieveSimple(RESID_KEY, resId);	
//		
//	}
//
//	public Resource retrieveSimpleByUri(String resId) throws OneM2MException {
//
//		return retrieveSimple(URI_KEY, resId);	
//		
//	}
//	
	
//	private Resource retrieveSimple(String name, String value) throws OneM2MException { 4
//
//		try {
//
//			MongoCollection<Document> collection = context.getDatabaseManager().getCollection("resEntity");
//			Document doc = collection.find(new BasicDBObject(name, value)).first();
//			if (doc == null) {
//				return null;
//			}
//			
//			AccessControlPolicy res = new AccessControlPolicy();
//			this.setAnnounceableSubordinateResource(res, doc);
//
//			SetOfAcrs privileges = makePriviligesObject((Document)doc.get("privileges"));
//			SetOfAcrs selfPrivileges = makePriviligesObject((Document)doc.get("selfPrivileges"));
//			
//			res.setPrivileges(privileges);
//			res.setSelfPrivileges(selfPrivileges);
//			
//			return res;
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, "Fail to retrieve AccessControlPolicy");
//		}
//	}
//
//	private Document makePrivilegesDocument(SetOfAcrs privileges) {
//
//		List<Document> acrDocList = new ArrayList<Document>();
//		
//		Iterator<AccessControlRule> it = privileges.getAccessControlRule().iterator();
//		while (it.hasNext()) {
//			Document acrDoc = new Document();
//			AccessControlRule acr = it.next();
//			
//			List<String> acor = acr.getAccessControlOriginators();
//			acrDoc.append("accessControlOriginators", acor);
//			int operation = acr.getAccessControlOperations();
//			acrDoc.append("accessControlOperations", operation);
//			
//			List<AccessControlContexts> ctxList = acr.getAccessControlContexts();
//			Iterator<AccessControlContexts> ia = ctxList.iterator();
//			Document accDoc = new Document();
//			while (ia.hasNext()) {
//				AccessControlContexts accs = ia.next();
//				
//				AccessControlIpAddresses ips = accs.getAccessControlIpAddresses();
//				if (ips != null) {
//					List<String> ipv4s = ips.getIpv4Addresses();
//					List<String> ipv6s = ips.getIpv6Addresses();
//					Document ipsDoc = new Document("ipv4", ipv4s).append("ipv6", ipv6s);
//					accDoc.append("accessControlPolicyIpAddresses", ipsDoc);
//				}
//				
//				LocationRegion aclr = accs.getAccessControlLocationRegion();
//				if (aclr != null) {
//					List<String> cc = aclr.getCountryCode();
//					List<Double> cr = aclr.getCircRegion();
//					Document irDoc = new Document("countryCode", cc).append("circRegion", cr);
//					accDoc.append("accessControlLocationRegion", irDoc);
//				}
//				
//				List<String> acw = accs.getAccessControlWindow();
//				if (acw != null) {
//					accDoc.append("accessControlWindow", acw);					
//				}
//			}
//			acrDoc.append("accessControlContexts", accDoc);
//			
//			acrDocList.add(acrDoc);
//		}
//		
//		Document privDoc = new Document();
//		privDoc.append("accessControlRule",  acrDocList);
//		return privDoc;
//	}
//
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
