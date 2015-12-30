package net.herit.iot.onem2m.incse.manager.dao;

import net.herit.iot.onem2m.incse.facility.CfgManager;
import net.herit.iot.onem2m.incse.facility.DatabaseManager;
import net.herit.iot.onem2m.incse.manager.dao.ResourceDAO;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;

public class RestSubscriptionDAO {
	
	private DatabaseManager dbManager;
	private String collectionName;

	protected static final String URI_KEY = "uri";
	protected static final String NOTI_URI_KEY = "notiUri";
	
	private Logger log = LoggerFactory.getLogger(RestSubscriptionDAO.class);

	public RestSubscriptionDAO(DatabaseManager dbManager) {
		this.dbManager = dbManager;
		this.collectionName = CfgManager.getInstance().getRestSubscriptionDatabaseName();
	}
	
	
	public String getNotificationUri(String uri) {

		String notiUri = null;
		
		MongoCollection<Document> collection = dbManager.getCollection(collectionName);
		Document seqDoc = getDocument(URI_KEY, uri);
		if (seqDoc != null) {
			
			notiUri = (String)seqDoc.get(NOTI_URI_KEY);
			
		}

		log.debug("getNotificationUri("+notiUri+")");
		
		return notiUri;
		
	}
	
	public void addSubscription(String uri, String notificationUri) {

		MongoCollection<Document> collection = dbManager.getCollection(collectionName);
		Document seqDoc = getDocument(URI_KEY, uri);
		if (seqDoc == null) {
			
			seqDoc = new Document();
			seqDoc.put(URI_KEY, uri);
			seqDoc.put(NOTI_URI_KEY, notificationUri);

			collection.insertOne(seqDoc);
		} else {
			
			seqDoc.put(NOTI_URI_KEY, notificationUri);
			
			BasicDBObject update = new BasicDBObject(ResourceDAO.UPDATE_SPECIFIC_FIELD, seqDoc);
			BasicDBObject query = new BasicDBObject(URI_KEY, uri);
			Document doc = collection.findOneAndUpdate(query, update);
			
		}

		log.debug("addSubscription("+uri+","+notificationUri+")");
		
	}

	private Document getDocument(String key, String value) {

		MongoCollection<Document> collection = dbManager.getCollection(collectionName);
		Document doc = collection.find(new BasicDBObject(key, value))
				.first();

		return doc;
	}

}
