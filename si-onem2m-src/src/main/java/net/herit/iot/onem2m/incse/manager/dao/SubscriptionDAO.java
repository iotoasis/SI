package net.herit.iot.onem2m.incse.manager.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bson.BSON;
import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mRequest.RESULT_CONT;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.onem2m.core.convertor.ConvertorFactory;
import net.herit.iot.onem2m.core.convertor.JSONConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.facility.DatabaseManager;
import net.herit.iot.onem2m.incse.facility.OneM2mUtil;
//import net.herit.iot.onem2m.manager.resource.BasicResource;
//import net.herit.iot.onem2m.manager.resource.AnnouncableResource;
//import net.herit.iot.onem2m.manager.resource.AE;
import net.herit.iot.onem2m.incse.manager.dao.DAOInterface;
import net.herit.iot.onem2m.incse.manager.dao.ResourceDAO;









import net.herit.iot.onem2m.resource.AE;
import net.herit.iot.onem2m.resource.AnnounceableResource;
import net.herit.iot.onem2m.resource.PollingChannel;
import net.herit.iot.onem2m.resource.Resource;
import net.herit.iot.onem2m.resource.Schedule;
import net.herit.iot.onem2m.resource.Subscription;

import com.mongodb.client.MongoCollection;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SubscriptionDAO extends ResourceDAO implements DAOInterface {
	
	private Logger log = LoggerFactory.getLogger(SubscriptionDAO.class);
	
	public SubscriptionDAO(OneM2mContext context) {
		super(context);
	}

	@Override
	public String resourceToJson(Resource res) throws OneM2MException {
		try {
			
			JSONConvertor<Subscription> jc = (JSONConvertor<Subscription>)ConvertorFactory.getJSONConvertor(Subscription.class, Subscription.SCHEMA_LOCATION);
			return jc.marshal((Subscription)res);
			
		} catch (Exception e) {
			e.printStackTrace();			
			throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, "Json generation error:"+res.toString());
		}
	}

	@Override
	public void create(Resource resource) throws OneM2MException {
		
		super.create(resource);
		
//		AE res = (AE)resource;
//		
//		// check if parent is subscribable
//		
//		MongoCollection<Document> collection = context.getDatabaseManager().getCollection("resEntity");
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
//		
//		collection.insertOne(doc);		
		
	}

	@Override
	public void update(Resource resource) throws OneM2MException {
		
		super.update(resource);
		
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
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, "Exception during extract resource field");
//			}
//		}
//		
//	    updateDocument("resourceID", resource.getId(), map);  
		
	}

//	@Override
//	public Resource retrieve(String name, String value) throws OneM2MException {
//
//		try {
//			
//			Document doc = getDocument(name, value);
//			
//			if (doc == null) {
//				return null;
//			}
//			
//			AE ae = new AE();
//			this.setAnnouncableAttributes((AnnounceableResource)ae, doc);
//
//			//ae.setAEID((String)doc.get("AE-ID"));
//			//ae.setAppID((String)doc.get("App-ID"));
//			ae.setAEID((String)doc.get("aeid"));
//			ae.setAppID((String)doc.get("appID"));
//			ae.setAppName((String)doc.get("appName"));
//			ae.setOntologyRef((String)doc.get("ontologyRef"));
//			ae.setNodeLink((String)doc.get("nodeLink"));
//
//			List<String> pois = ae.getPointOfAccess();
//			pois.addAll((ArrayList<String>)doc.get("pointOfAccess"));
//			
//			return ae;
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, "Fail to retrieve AE");
//		}
//		
//	}

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
//
//	@Override
//	public Resource retrieveByUri(String uri, RESULT_CONT rc) throws OneM2MException {
//
//		return retrieve(URI_KEY, uri, new JSONConvertor<Subscription>(Subscription.class), rc);
//		
//	}
//
//	@Override
//	public Resource retrieveByResId(String resId, RESULT_CONT rc) throws OneM2MException {
//
//		return retrieve(RESID_KEY, resId, new JSONConvertor<Subscription>(Subscription.class), rc);
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
		
		return retrieve(OneM2mUtil.isUri(id) ? URI_KEY : RESID_KEY, id, ConvertorFactory.getJSONConvertor(Subscription.class, Subscription.SCHEMA_LOCATION), rc);
		
	}
}
