package net.herit.iot.onem2m.incse.manager.dao;

import java.util.HashMap;
import java.util.List;

import org.bson.Document;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.DBCollection;

import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mRequest.RESULT_CONT;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.onem2m.core.convertor.ConvertorFactory;
import net.herit.iot.onem2m.core.convertor.DaoJSONConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.facility.CfgManager;
import net.herit.iot.onem2m.incse.facility.OneM2mUtil;
import net.herit.iot.onem2m.incse.manager.dao.DAOInterface;
import net.herit.iot.onem2m.incse.manager.dao.ResourceDAO;
import net.herit.iot.onem2m.incse.manager.dao.ResourceDAO.KeyValue;



























import net.herit.iot.onem2m.resource.AE;
import net.herit.iot.onem2m.resource.AnnounceableResource;
import net.herit.iot.onem2m.resource.AnnounceableSubordinateResource;
import net.herit.iot.onem2m.resource.Container;
import net.herit.iot.onem2m.resource.ContentInstance;
import net.herit.iot.onem2m.resource.Resource;

import org.bson.Document;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ContentInstanceDAO extends ResourceDAO implements DAOInterface {
	
	private Logger log = LoggerFactory.getLogger(ContentInstanceDAO.class);

	public ContentInstanceDAO(OneM2mContext context) {
		super(context);
	}

	@Override
	public String resourceToJson(Resource res) throws OneM2MException {
		try {
			
			DaoJSONConvertor<ContentInstance> jc = (DaoJSONConvertor<ContentInstance>)ConvertorFactory.getDaoJSONConvertor(ContentInstance.class, ContentInstance.SCHEMA_LOCATION);
			return jc.marshal((ContentInstance)res);
			
		} catch (Exception e) {
			log.debug("Handled exception", e);			
			throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, "Json generation error:"+res.toString());
		}
	}

	@Override
	public void create(Resource resource) throws OneM2MException {

		
		ContentInstance ciRes = (ContentInstance)resource;
		ciRes.setContentSize(ciRes.getContent().length());
		
		String parentID = ciRes.getParentID();
		
		ContainerDAO cDao = new ContainerDAO(context);
		Container cRes = (Container)cDao.retrieve(parentID, null);
		cRes.getCurrentByteSize();
		
		int deletedCount = 0, deletedSize = 0;
		int maxNrOfInstance = cRes.getMaxNrOfInstances() != null ? cRes.getMaxNrOfInstances() : CfgManager.getInstance().getMaxCIPerContainer();
		int overNrOfInstance = cRes.getCurrentNrOfInstances() - maxNrOfInstance; 
		if (overNrOfInstance > 0) {
			log.debug("MaxNrOfInstance exceeded:"+overNrOfInstance);
			deletedCount = this.deleteOldestNExpired(cRes.getResourceID(), overNrOfInstance);
		}
//		if (maxNrOfInstance < cRes.getCurrentNrOfInstances() + 1) {
//			throw new OneM2MException(RESPONSE_STATUS.MAX_NUMBER_OF_MEMBER_EXCEEDED, "Max Count:"+cRes.getMaxNrOfInstances() +", Current Count:"+ cRes.getCurrentNrOfInstances());
//		}
		
		if(cRes.getMaxByteSize() != null && cRes.getCurrentByteSize() != null && ciRes.getContentSize() != null)
			if (cRes.getMaxByteSize() < cRes.getCurrentByteSize() + ciRes.getContentSize()) {
				throw new OneM2MException(RESPONSE_STATUS.MAX_NUMBER_OF_MEMBER_EXCEEDED, "Max Size:"+cRes.getMaxNrOfInstances() +", Current Size:"+ (cRes.getCurrentByteSize() + ciRes.getContentSize()));
			}
		
		super.create(resource);
		
		ContentInstance res = (ContentInstance)resource;
				
		// update container attributes : currentNrOfInstances, currentByteSize
		updateContainer(parentID, 1-deletedCount, res.getContentSize().intValue()-deletedSize);
        
	}	

	@Override
	public void update(Resource resource) throws OneM2MException {
		
		super.update(resource);
				
	}
	
//	public Resource retrieve(String name, String value) throws OneM2MException {
//		try {
//			Document doc = getDocument(name, value);
//			
//			if (doc == null) {
//				return null;
//			}
//			
//			ContentInstance res = new ContentInstance();
//			setAnnounceableSubordinateResource((AnnounceableSubordinateResource)res, doc);
//			
//			res.setCreator((String)doc.get("creator"));
//			res.setContentInfo((String)doc.get("contentInfo"));
//			res.setContentSize((int)doc.get("contentSize"));
//			res.setOntologyRef((String)doc.get("ontologyRef"));
//			res.setContent((String)doc.get("content"));
//			
//			return res;
//	
//		} catch (Exception e) {
//			log.debug("Handled exception", e);
//			throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, "Fail to retrieve ContentInstance");
//		}
//	}

	/*public void delete(String uri) throws OneM2MException {

		MongoCollection<Document> collection = context.getDatabaseManager().getCollection("resEntity"); 
		BasicDBObject query = new BasicDBObject(URI_KEY, uri);
		
		Document doc = getDocument(URI_KEY, uri);
		int contentSize = (int)doc.get("contentSize");
		String parentID = (String)doc.get("parentID");
		
		deleteDocument(URI_KEY, uri);
		
		
		// update container attributes : currentNrOfInstances, currentByteSize
	    doc = getDocument("resourceID", parentID);
	    
	    HashMap<String, Object> map = new HashMap<String, Object>();
	    map.put("currentNrOfInstances", (int)doc.get("currentNrOfInstances")-1);
	    map.put("currentByteSize", (int)doc.get("currentByteSize")-contentSize);
	    updateDocument("resourceID", parentID, map);
		
	}*/
	
//	@Override
//	public void deleteByUri(String uri) throws OneM2MException {
//		
//		Document doc = getDocument(URI_KEY, uri);
//		int contentSize = (int)doc.get("contentSize");
//		String parentID = (String)doc.get("parentID");
//		
//		deleteDocument(URI_KEY, uri);
//		
//		updateContainer(parentID, -1, contentSize*-1);
//		
//	}
//
//	@Override
//	public void deleteByResId(String resId) throws OneM2MException {
//		
//		Document doc = getDocument(RESID_KEY, resId);
//		int contentSize = (int)doc.get("contentSize");
//		String parentID = (String)doc.get("parentID");
//		
//		deleteDocument(RESID_KEY, resId);
//		
//		updateContainer(parentID, -1, contentSize*-1);
//		
//	}

//	@Override
//	public Resource retrieveByUri(String uri, RESULT_CONT rc) throws OneM2MException {
//
//		return retrieve(URI_KEY, uri, new DaoJSONConvertor<ContentInstance>(ContentInstance.class), rc);
//		
//	}
//
//	@Override
//	public Resource retrieveByResId(String resId, RESULT_CONT rc) throws OneM2MException {
//
//		return retrieve(RESID_KEY, resId, new DaoJSONConvertor<ContentInstance>(ContentInstance.class), rc);
//		
//	}
	
	@Override
	public void delete(String id) throws OneM2MException {

		Document doc = getDocument(OneM2mUtil.isUri(id) ? URI_KEY : RESID_KEY, id);
		int contentSize = (int)doc.get(CONTENTSIZE_KEY);
		String parentID = (String)doc.get(PARENTID_KEY);
		
		deleteDocument(OneM2mUtil.isUri(id) ? URI_KEY : RESID_KEY, id);
		
		updateContainer(parentID, -1, contentSize*-1);
		
	}

	@Override
	public Resource retrieve(String id, RESULT_CONT rc) throws OneM2MException {
		
		return retrieve(OneM2mUtil.isUri(id) ? URI_KEY : RESID_KEY, id, 
				(DaoJSONConvertor<ContentInstance>)ConvertorFactory.getDaoJSONConvertor(ContentInstance.class, ContentInstance.SCHEMA_LOCATION), rc);
		
	}
	
	
	private void updateContainer(String resId, int cnt, int size) {
		
		Document doc = getDocument(RESID_KEY, resId);
	    
	    HashMap<String, Object> map = new HashMap<String, Object>();
	    map.put(CUR_NOINST_KEY, (int)doc.get(CUR_NOINST_KEY) + cnt);
	    map.put(CUR_BSIZE_KEY, (int)doc.get(CUR_BSIZE_KEY) + size);
	    updateDocument(RESID_KEY, resId, map);
		
	}
	
	private int deleteOldestNExpired(String containerId, int size) {
		
		log.debug("deleteOldestNExpired containerId:{}, size:{}", containerId, size);
		MongoCollection<Document> collection = context.getDatabaseManager()
				.getCollection(collectionName);
		BasicDBList and = new BasicDBList();
		DeleteResult result;
		/*
		
		String now = LocalDateTime.now().toString(DateTimeFormat.forPattern("yyyyMMdd'T'HHmmss"));
		
		and.add(new BasicDBObject(EXPIRETIME_KEY, new BasicDBObject("$lt", now)));
		and.add(new BasicDBObject(PARENTID_KEY, containerId));
		
		result = collection.deleteMany(new BasicDBObject("$and", and));
		size -= result.getDeletedCount();
		
		log.debug("Deleted expired contentInstance:{}", result.getDeletedCount());
		*/
		
		if (size > 0) {
			and.clear();	
			and.add(new BasicDBObject(PARENTID_KEY, containerId));
			and.add(new BasicDBObject(RESTYPE_KEY, RESOURCE_TYPE.CONTENT_INST.Value()));
			
			MongoCursor<Document> cursor = collection.find(new BasicDBObject("$and", and))
											.sort(new BasicDBObject(CREATETIME_KEY, 1))
											.limit(size).iterator();
			
			int deletedCount = 0;
			if (cursor.hasNext()) {
				Document doc = cursor.next();
//				and.clear();
//				and.add(new BasicDBObject(PARENTID_KEY, containerId));
//				and.add(new BasicDBObject(RESTYPE_KEY, RESOURCE_TYPE.CONTENT_INST.Value()));
//				and.add(new BasicDBObject(CREATETIME_KEY, new BasicDBObject("$lt", doc.get(CREATETIME_KEY))));
//				
				result = collection.deleteOne(new BasicDBObject(RESID_KEY, doc.get(RESID_KEY)));
	
				deletedCount += result.getDeletedCount();
			}
			log.debug("Deleted oldest contentInstance:{}", deletedCount);
			return deletedCount;
		}
		return 0;
		
	}

	public Resource retrieveOldest(String parentUri, RESULT_CONT resultContent) throws OneM2MException {
		
		String now = LocalDateTime.now().toString(DateTimeFormat.forPattern("yyyyMMdd'T'HHmmss"));

		DBObject c1 = new BasicDBObject(EXPIRETIME_KEY, null);
		DBObject c2 = new BasicDBObject(EXPIRETIME_KEY, new BasicDBObject("$gt", now));
		
		BasicDBList or = new BasicDBList();
		or.add(c1);
		or.add(c2);
		
		BasicDBObject query = new BasicDBObject("$or", or).append(PARENTID_KEY,  parentUri);
		
		List<Document>childList = getDocuments(query, RESOURCE_TYPE.CONTENT_INST, CREATETIME_KEY, true, 1);

		if (childList.size() == 0) {
			return null;
		}

		Document doc = childList.get(0);

		try {
			DaoJSONConvertor<ContentInstance> cvt = (DaoJSONConvertor<ContentInstance>)ConvertorFactory.getDaoJSONConvertor(ContentInstance.class, ContentInstance.SCHEMA_LOCATION);
					
			Resource res = (Resource) cvt.unmarshal(doc.toJson());
			res.setUri(doc.getString(URI_KEY));
			
			return res;
		} catch (Exception e) {
			log.debug("Handled exception", e);
			
			throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, "Exception during initialization of resource using document.(retrieveOldest)");
		}
		
	}
	

	public Resource retrieveLatest(String parentUri, RESULT_CONT resultContent) throws OneM2MException {		
		
		String now = LocalDateTime.now().toString(DateTimeFormat.forPattern("yyyyMMdd'T'HHmmss"));

		DBObject c1 = new BasicDBObject(EXPIRETIME_KEY, null);
		DBObject c2 = new BasicDBObject(EXPIRETIME_KEY, new BasicDBObject("$gt", now));
		
		BasicDBList or = new BasicDBList();
		or.add(c1);
		or.add(c2);
		
		BasicDBObject query = new BasicDBObject("$or", or).append(PARENTID_KEY,  parentUri);
		
		List<Document>childList = getDocuments(query, RESOURCE_TYPE.CONTENT_INST, CREATETIME_KEY, false, 1);

		if (childList.size() == 0) {
			return null;
		}

		Document doc = childList.get(0);

		try {
			DaoJSONConvertor<ContentInstance> cvt = (DaoJSONConvertor<ContentInstance>)ConvertorFactory.getDaoJSONConvertor(ContentInstance.class, ContentInstance.SCHEMA_LOCATION);
			//DaoJSONConvertor<ContentInstance> cvt = new DaoJSONConvertor<ContentInstance>(ContentInstance.class);
			Resource res = (Resource) cvt.unmarshal(doc.toJson());
			res.setUri(doc.getString(URI_KEY));
			
			return res;
		} catch (Exception e) {
			log.debug("Handled exception", e);
			
			throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, "Exception during initialization of resource using document.(retrieveOldest)");
		}
		
	}
	
}
