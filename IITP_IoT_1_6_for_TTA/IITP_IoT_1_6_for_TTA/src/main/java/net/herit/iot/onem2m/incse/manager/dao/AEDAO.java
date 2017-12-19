package net.herit.iot.onem2m.incse.manager.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bson.BSON;
import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.Cursor;
import com.mongodb.DBCursor;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.RESULT_CONT;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.onem2m.core.convertor.ConvertorFactory;
import net.herit.iot.onem2m.core.convertor.DaoJSONConvertor;
import net.herit.iot.onem2m.core.convertor.XMLConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.facility.CfgManager;
import net.herit.iot.onem2m.incse.facility.DatabaseManager;
import net.herit.iot.onem2m.incse.facility.OneM2mUtil;
import net.herit.iot.onem2m.incse.manager.ManagerInterface;
//import net.herit.iot.onem2m.manager.resource.BasicResource;
//import net.herit.iot.onem2m.manager.resource.AnnouncableResource;
//import net.herit.iot.onem2m.manager.resource.AE;
import net.herit.iot.onem2m.incse.manager.dao.DAOInterface;
import net.herit.iot.onem2m.incse.manager.dao.ResourceDAO;




















import net.herit.iot.onem2m.resource.AE;
import net.herit.iot.onem2m.resource.AnnounceableResource;
import net.herit.iot.onem2m.resource.Resource;

import com.mongodb.client.MongoCollection;
import com.mongodb.util.JSON;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AEDAO extends ResourceDAO implements DAOInterface {
	
	private Logger log = LoggerFactory.getLogger(AEDAO.class);
	
	public AEDAO(OneM2mContext context) {
		super(context);
	}
	
	@Override
	public String resourceToJson(Resource res) throws OneM2MException {
		try {
			
			DaoJSONConvertor<AE> jc = (DaoJSONConvertor<AE>)ConvertorFactory.getDaoJSONConvertor(AE.class, AE.SCHEMA_LOCATION);
			return jc.marshal((AE)res);
			
		} catch (Exception e) {
			log.debug("Handled exception", e);			
			throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, "Json generation error:"+res.toString());
		}
	}
	
	@Override
	public void create(Resource resource) throws OneM2MException {

		Document curDoc = getDocument(resource.getResourceID());
		if (curDoc != null) {			
			throw new OneM2MException(RESPONSE_STATUS.CONFLICT, "Resource already exist!!! :"+resource.getResourceID());
		}
		
		if(((AE)resource).getExpirationTime() == null) {	// added in 2017-10-25
			((AE)resource).setExpirationTime(CfgManager.getInstance().getDefaultExpirationTime());
		}
		
		super.create(resource);
		
//		AE res = (AE)resource;
//		
//		String strJson;
//		try {
//			
//			JSONConvertor<AE> jc = new JSONConvertor<AE>(AE.class);
//			strJson = jc.marshal(res);
//			context.getLogManager().debug("AE json: " + strJson);
//						
//		} catch (Exception e) {
//			log.debug("Handled exception", e);
//			throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, "Exception in converting:"+res.toString());
//		}
//		
//		Document doc = Document.parse(strJson);
//		doc.append(ResourceDAO.URI_KEY, res.getUri());
//		
//		MongoCollection<Document> collection = context.getDatabaseManager().getCollection(COLLECTION_NAME);
//		
//		collection.insertOne(doc);
		
//		Document doc = new Document("appName", res.getAppName())
//		    //.append("App-ID", res.getAppID())
//		    //.append("AE-ID", res.getAEID())
//		    .append("appID", res.getAppID())
//		    .append("aeid", res.getAEID())
//		    .append("pointOfAccess", res.getPointOfAccess())
//		    .append("ontologyRef", res.getOntologyRef())
//		    .append("nodeLink", res.getNodeLink());
//		
//		this.appendAnnounceableAttributes(doc, res);
		
	}

	@Override
	public void update(Resource resource) throws OneM2MException {
		
		super.update(resource);

//		AE res = (AE)resource;
//		
//		JSONConvertor<AE> jc = new JSONConvertor<AE>(AE.class);
//		
//		BasicDBObject dbObj = null;
//		try {
//			String strJson = jc.marshal(res);
//			context.getLogManager().debug("AE Json: " + strJson);
//		
//			dbObj = (BasicDBObject) JSON.parse(strJson);
//		} catch (Exception e) {
//			log.debug("Handled exception", e);
//			throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, "Exception in converting:"+res.toString());
//		}
//		
//		BasicDBObject update = new BasicDBObject(UPDATE_SPECIFIC_FIELD, dbObj);
//		BasicDBObject query = new BasicDBObject(URI_KEY, resource.getUri());
//		MongoCollection<Document> collection = context.getDatabaseManager().getCollection(COLLECTION_NAME);
//		Document doc = collection.findOneAndUpdate(query, update);
//
//		context.getLogManager().debug(doc.toString());
		
		
		
//		AE ae = (AE)resource;
//
//		HashMap<String, Object> map = new HashMap<String, Object>();		
//		
//		for (Field f: ae.getClass().getDeclaredFields()) {
//			f.getName();
//			Object val;
//			try {
//				val = f.get(ae);
//				if (val != null) {
//					map.put(f.getName(), val);
//				}
//			} catch (IllegalArgumentException | IllegalAccessException e) {
//				log.debug("Handled exception", e);
//				throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, "Exception during extract resource field");
//			}
//		}
//		
//	    updateDocument("resourceID", resource.getId(), map); 
		
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

//	@Override
//	public Resource retrieveByUri(String uri, RESULT_CONT rc) throws OneM2MException {
//		
//		return retrieve(URI_KEY, uri, new JSONConvertor<AE>(AE.class), rc);
//	
//	}

	//	@Override
//	public Resource retrieveByUri(String uri, RESULT_CONT rc) throws OneM2MException {
//		
//		return retrieve(URI_KEY, uri, new JSONConvertor<AE>(AE.class), rc);
//			
//	}
//
//	@Override
//	public Resource retrieveByResId(String resId, RESULT_CONT rc) throws OneM2MException {
//		
//		return retrieve(RESID_KEY, resId, new JSONConvertor<AE>(AE.class), rc);
//		
//	}


	@Override
	public Resource retrieve(String id, RESULT_CONT rc) throws OneM2MException {
		
		return retrieve(OneM2mUtil.isUri(id) ? URI_KEY : RESID_KEY, id, ConvertorFactory.getDaoJSONConvertor(AE.class, AE.SCHEMA_LOCATION), rc);
		
	}

	public int getCount() {

		MongoCollection<Document> collection = context.getDatabaseManager().getCollection(collectionName);
		long count = collection.count(new BasicDBObject(RESTYPE_KEY, RESOURCE_TYPE.AE.Value()));
		return (int)count;
		
	}
}
