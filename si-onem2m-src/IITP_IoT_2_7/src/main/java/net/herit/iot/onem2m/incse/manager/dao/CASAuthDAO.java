package net.herit.iot.onem2m.incse.manager.dao;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;

import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.context.RestContext;
import net.herit.iot.onem2m.incse.facility.CfgManager;

public class CASAuthDAO {
	private RestContext context;
	private String collectionName;

	protected static final String AUTH_KEY_NAME = "DEV_ID";
	protected static final String AUTH_VAL_NAME = "DEV_PWD";
	
	private Logger log = LoggerFactory.getLogger(CASAuthDAO.class);
	
	public CASAuthDAO(RestContext context) {
		this.context = context;
		this.collectionName = CfgManager.getInstance().getCASAuthDatabaseName();
	}
	
	public Document getDocument(String keyName, String keyValue) {

		MongoCollection<Document> collection = context.getDatabaseManager()
				.getCollection(collectionName);
		Document doc = collection.find(new BasicDBObject(keyName, keyValue))
				.first();

		return doc;
	}
	
	public Document getAuthInfo(String devId) {
		return this.getDocument(AUTH_KEY_NAME, devId);
	}
	
	public boolean regAuthInfo(String devId) {
		
		MongoCollection<Document> collection = context.getDatabaseManager().getCollection(collectionName);
		
		if(devId == null || devId.equals("")) return false;
		
		Document authDoc = getDocument(AUTH_KEY_NAME, devId);
		if (authDoc != null) {

			return false;
		} else {
			authDoc = new Document();
			authDoc.put(AUTH_KEY_NAME, devId);

			collection.insertOne(authDoc);
			
			return true;
		}
	}

	public boolean updateAuthInfo(String devId, String pwd) {
		
		MongoCollection<Document> collection = context.getDatabaseManager().getCollection(collectionName);
		
		if(devId == null || devId.equals("") || pwd == null || pwd.equals("")) return false;
		
		Document authDoc = getDocument(AUTH_KEY_NAME, devId);
		if (authDoc != null) {
			collection.deleteOne(authDoc);
			
		} 
		
		authDoc = new Document();
		authDoc.put(AUTH_KEY_NAME, devId);
		authDoc.put(AUTH_VAL_NAME, pwd);
		
		collection.insertOne(authDoc);
		
		return true;
	}
	
	public void deleteAuthInfo(String devId) {
		MongoCollection<Document> collection = context.getDatabaseManager().getCollection(collectionName);
		
		Document authDoc = getDocument(AUTH_KEY_NAME, devId);
		if(authDoc != null) {
			collection.deleteOne(authDoc);
		}
		
	}
}
