package net.herit.iot.onem2m.incse.manager.dao;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;

import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mRequest.RESULT_CONT;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.message.onem2m.format.Enums.CSE_TYPE;
import net.herit.iot.onem2m.core.convertor.ConvertorFactory;
import net.herit.iot.onem2m.core.convertor.JSONConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.facility.OneM2mUtil;
import net.herit.iot.onem2m.incse.manager.dao.DAOInterface;
import net.herit.iot.onem2m.incse.manager.dao.ResourceDAO;
import net.herit.iot.onem2m.incse.manager.dao.ResourceDAO.KeyValue;
import net.herit.iot.onem2m.resource.RemoteCSE;
import net.herit.iot.onem2m.resource.RemoteCSEAnnc;
import net.herit.iot.onem2m.resource.Resource;


public class RemoteCSEDAO extends ResourceDAO implements DAOInterface {
	
	private Logger log = LoggerFactory.getLogger(RemoteCSEDAO.class);

	public RemoteCSEDAO(OneM2mContext context) {
		super(context);
	}

	@Override
	public String resourceToJson(Resource res) throws OneM2MException {
		try {
			
			JSONConvertor<RemoteCSE> jc = (JSONConvertor<RemoteCSE>)ConvertorFactory.getJSONConvertor(RemoteCSE.class, RemoteCSE.SCHEMA_LOCATION);
			return jc.marshal((RemoteCSE)res);
			
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

//	@Override
//	public Resource retrieveByUri(String uri, RESULT_CONT rc) throws OneM2MException {
//		
//		return this.retrieve(URI_KEY, uri, new JSONConvertor<RemoteCSE>(RemoteCSE.class), rc);
//		
//	}
//
//	@Override
//	public Resource retrieveByResId(String id, RESULT_CONT rc) throws OneM2MException {
//		
//		return this.retrieve("resourceID", id, new JSONConvertor<RemoteCSE>(RemoteCSE.class), rc);
//		
//	}
//	
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
		
		return retrieve(OneM2mUtil.isUri(id) ? URI_KEY : RESID_KEY, id, ConvertorFactory.getJSONConvertor(RemoteCSE.class, RemoteCSE.SCHEMA_LOCATION), rc);
		
	}
	
	public boolean checkIfRegistered(String cseId, CSE_TYPE cseType) {
		
		List<KeyValue> kv = new ArrayList<KeyValue>();
		kv.add(new KeyValue(CSEID_KEY, cseId));
		kv.add(new KeyValue(RESTYPE_KEY, RESOURCE_TYPE.REMOTE_CSE.Value()));
		if (cseType != null) {
			kv.add(new KeyValue(CSETYPE_KEY, cseType.Value()));
		}
		
		List<Document> doc =  getDocuments(kv, RESOURCE_TYPE.REMOTE_CSE, null, true, 1);
		return doc != null && doc.size() > 0;
	}

	public RemoteCSE retrieveByCseId(String cseId) throws OneM2MException {

		return (RemoteCSE) retrieve(CSEID_KEY, cseId, ConvertorFactory.getJSONConvertor(RemoteCSE.class, RemoteCSE.SCHEMA_LOCATION), RESULT_CONT.ATTRIBUTE);

		
	}


	public int getCount() {

		MongoCollection<Document> collection = context.getDatabaseManager().getCollection(collectionName);
		long count = collection.count(new BasicDBObject(RESTYPE_KEY, RESOURCE_TYPE.REMOTE_CSE.Value()));
		return (int)count;
		
	}
	
}
