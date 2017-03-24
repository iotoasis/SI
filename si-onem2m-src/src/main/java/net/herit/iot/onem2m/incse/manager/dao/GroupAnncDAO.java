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
import net.herit.iot.onem2m.resource.AnnouncedResource;
import net.herit.iot.onem2m.resource.GroupAnnc;
import net.herit.iot.onem2m.resource.Resource;


public class GroupAnncDAO extends ResourceDAO implements DAOInterface {
	
	private Logger log = LoggerFactory.getLogger(GroupAnncDAO.class);

	public GroupAnncDAO(OneM2mContext context) {
		super(context);
	}

	@Override
	public String resourceToJson(Resource res) throws OneM2MException {
		try {
			
			DaoJSONConvertor<GroupAnnc> jc = (DaoJSONConvertor<GroupAnnc>)ConvertorFactory.getDaoJSONConvertor(GroupAnnc.class, GroupAnnc.SCHEMA_LOCATION);
			return jc.marshal((GroupAnnc)res);
			
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
	
	@Override
	public void delete(String id) throws OneM2MException {
		
		String resourceID = (String)this.getAttribute(OneM2mUtil.isUri(id) ? URI_KEY : RESID_KEY,  id, RESID_KEY);	

		deleteChild(resourceID);
		
		deleteDocument(OneM2mUtil.isUri(id) ? URI_KEY : RESID_KEY, id);
		
	}

	@Override
	public Resource retrieve(String id, RESULT_CONT rc) throws OneM2MException {
		
		return retrieve(OneM2mUtil.isUri(id) ? URI_KEY : RESID_KEY, id, 
				ConvertorFactory.getDaoJSONConvertor(GroupAnnc.class, GroupAnnc.SCHEMA_LOCATION), rc);
		
	}
	
	
	private void setAnnouncedAttribute(AnnouncedResource res, Document doc) {
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
	
	public AnnouncedResource retrieveAnnouncedResInfo(String resId) {
		
		Document doc = getDocument(resId);
		
		if (doc == null) {
			return null;
		}
		
		AnnouncedResource regRes;
		if ((int)doc.get(RESTYPE_KEY) == RESOURCE_TYPE.GROUP_ANNC.Value()) {
			GroupAnnc grpRes = new GroupAnnc();
			
			List<String> ids = grpRes.getMembersAccessControlPolicyIDs();
			List<String> docIds = (List<String>)doc.get(ACPIDS_KEY);
			ids.addAll(docIds);
			
			regRes = grpRes;
		} else {
			regRes = new AnnouncedResource();
		}
		setAnnouncedAttribute(regRes, doc);
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
	
	public List<AnnouncedResource> retrieveAnnouncedInfoList(List<String> memIdList) {
		
		List<AnnouncedResource> regResList = new ArrayList<AnnouncedResource>();
		
		Iterator<String> it = memIdList.iterator();
		while (it.hasNext()) {
			Document doc;
			String resId = it.next();
			if (OneM2mUtil.isUri(resId)) {
				doc = this.getDocument(URI_KEY,  resId);
			} else {
				doc = this.getDocument(RESID_KEY, resId);
			}
			AnnouncedResource regRes;
			if ((int)doc.get(RESTYPE_KEY) == RESOURCE_TYPE.GROUP.Value()) {
				GroupAnnc grpRes = new GroupAnnc();
				
				List<String> ids = grpRes.getMembersAccessControlPolicyIDs();
				List<String> docIds = (List<String>)doc.get(ACPIDS_KEY);
				ids.addAll(docIds);
				
				regRes = grpRes;
			} else {
				regRes = new AnnouncedResource();
			}
			setAnnouncedAttribute(regRes, doc);
			regResList.add(regRes);
		}
		
		return regResList;
		
	}
}
