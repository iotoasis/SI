package net.herit.iot.onem2m.incse.manager.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;
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
import net.herit.iot.onem2m.incse.manager.dao.DAOInterface;
import net.herit.iot.onem2m.incse.manager.dao.ResourceDAO;
import net.herit.iot.onem2m.resource.Delivery;
import net.herit.iot.onem2m.resource.Group;
import net.herit.iot.onem2m.resource.RegularResource;
import net.herit.iot.onem2m.resource.Resource;


public class GroupDAO extends ResourceDAO implements DAOInterface {
	
	private Logger log = LoggerFactory.getLogger(GroupDAO.class);

	public GroupDAO(OneM2mContext context) {
		super(context);
	}

	@Override
	public String resourceToJson(Resource res) throws OneM2MException {
		try {
			
			DaoJSONConvertor<Group> jc = (DaoJSONConvertor<Group>)ConvertorFactory.getDaoJSONConvertor(Group.class, Group.SCHEMA_LOCATION);
			return jc.marshal((Group)res);
			
		} catch (Exception e) {
			log.debug("Handled exception", e);		
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
//
//	@Override
//	public Resource retrieveByUri(String uri, RESULT_CONT rc) throws OneM2MException {
//		
//		return this.retrieve(URI_KEY, uri, new DaoJSONConvertor<Group>(Group.class), rc);
//		
//	}
//
//	@Override
//	public Resource retrieveByResId(String id, RESULT_CONT rc) throws OneM2MException {
//		
//		return this.retrieve("resourceID", id, new DaoJSONConvertor<Group>(Group.class), rc);
//		
//	}
//	
//	@Override
//	public void deleteByUri(String uri) throws OneM2MException {
//				
//		deleteDocument(URI_KEY, uri);
//		
//		
//	}
//
//	@Override
//	public void deleteByResId(String resId) throws OneM2MException {	
//
//		deleteDocument(RESID_KEY, resId);
//		
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
				ConvertorFactory.getDaoJSONConvertor(Group.class, Group.SCHEMA_LOCATION), rc);
		
	}
	
	
	private void setRegularAttribute(RegularResource res, Document doc) {
		res.setCreationTime(doc.getString(CREATETIME_KEY));
		res.setExpirationTime(doc.getString(EXPIRETIME_KEY));
		res.setId(doc.get(OID_KEY).toString());
		res.setLastModifiedTime(doc.getString(LASTMODTIME_KEY));
		res.setParentID(doc.getString(PARENTID_KEY));
		res.setResourceID(doc.getString(RESID_KEY));
		res.setResourceName(doc.getString(RESNAME_KEY));
		res.setResourceType(doc.getInteger(RESTYPE_KEY));
		res.setUri(doc.getString(URI_KEY));
		
		List<String> ids = (List<String>)doc.get(ACPIDS_KEY);
		if(ids == null) return;
		
		Iterator<String> it = ids.iterator();
		while (it.hasNext()) {
			res.addAccessControlPolicyIDs(it.next());
		}
		
	}
	
	public RegularResource retrieveRegularResInfo(String resId) {
		
		Document doc = getDocument(resId);
		
		if (doc == null) {
			return null;
		}
		
		RegularResource regRes;
		if ((int)doc.get(RESTYPE_KEY) == RESOURCE_TYPE.GROUP.Value()) {
			Group grpRes = new Group();
			
//			List<String> ids = grpRes.getMembersAccessControlPolicyIDs();
//			List<String> docIds = (List<String>)doc.get(ACPIDS_KEY);
//			ids.addAll(docIds);    // 2016.05.10 removed
			
			List<String> docIds = (List<String>)doc.get(ACPIDS_KEY);  // 2016.05.10 added.
			if(docIds != null) {
				for(String docId : docIds) {
					grpRes.addMembersAccessControlPolicyIDs(docId);
				}
			}
			
			regRes = grpRes;
		} else {
			regRes = new RegularResource();
		}
		setRegularAttribute(regRes, doc);
		return regRes;
		
	}
	
	public Resource retrieveResInfo(String resId) {
		
		Document doc = getDocument(resId);
		
		if (doc == null) {
			return null;
		}
		
		Resource res = new Resource();
		this.setResourceAttributes(res, doc);
		return res;
		
	}
	
//	public List<RegularResource> retrieveRegularInfoList(List<String> memIdList) {
//		
//		List<RegularResource> regResList = new ArrayList<RegularResource>();
//		
//		Iterator<String> it = memIdList.iterator();
//		while (it.hasNext()) {
//			Document doc;
//			String resId = it.next();
//			if (OneM2mUtil.isUri(resId)) {
//				doc = this.getDocument(URI_KEY,  resId);
//			} else {
//				doc = this.getDocument(RESID_KEY, resId);
//			}
//			RegularResource regRes;
//			if ((int)doc.get(RESTYPE_KEY) == RESOURCE_TYPE.GROUP.Value()) {
//				Group grpRes = new Group();
//				
//				List<String> ids = grpRes.getMembersAccessControlPolicyIDs();
//				List<String> docIds = (List<String>)doc.get(ACPIDS_KEY);
//				ids.addAll(docIds);
//				
//				regRes = grpRes;
//			} else {
//				regRes = new RegularResource();
//			}
//			setRegularAttribute(regRes, doc);
//			regResList.add(regRes);
//		}
//		
//		return regResList;
//		
//	}
}
