package net.herit.iot.onem2m.incse.manager.dao;

import java.util.HashMap;
import java.util.Set;

import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.facility.CfgManager;
import net.herit.iot.onem2m.incse.manager.dao.ResourceDAO;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;


public class SeqNumDAO {
	
	private OneM2mContext context;
	private String collectionName;

	protected static final String CONFIG_KEY_NAME = "CONFIGURATION_NAME";
	protected static final String SEQUENCE_DOC_KEY = "SEQUENCE";
	
	private Logger log = LoggerFactory.getLogger(SeqNumDAO.class);

	public SeqNumDAO(OneM2mContext context) {
		this.context = context;
		this.collectionName = CfgManager.getInstance().getConfigurationDatabaseName();
	}

	public HashMap<String, Integer> getAllSequenceNumber() {
		
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		Document seqDoc = getDocument(CONFIG_KEY_NAME, SEQUENCE_DOC_KEY);
		if (seqDoc == null) {
			
			seqDoc = new Document();
			seqDoc.put(CONFIG_KEY_NAME, SEQUENCE_DOC_KEY);

			MongoCollection<Document> collection = context.getDatabaseManager().getCollection(collectionName);
			collection.insertOne(seqDoc);
		}
		
		Set<String> vals = seqDoc.keySet();
		for (String key: vals) {
			try {
				map.put(key,  (int)seqDoc.get(key));
			} catch (Exception e) {
				// pass non integer key-value set
				log.debug("Pass non integer sequence:"+key);
			}
		}
		
		return map;
	}
	
	public void setSequence(String key, int value) {

		MongoCollection<Document> collection = context.getDatabaseManager().getCollection(collectionName);
		Document seqDoc = getDocument(CONFIG_KEY_NAME, SEQUENCE_DOC_KEY);
		if (seqDoc == null) {
			
			seqDoc = new Document();
			seqDoc.put(CONFIG_KEY_NAME, SEQUENCE_DOC_KEY);

			collection.insertOne(seqDoc);
		}
		
		seqDoc.put(key, value);
		
		BasicDBObject update = new BasicDBObject(ResourceDAO.UPDATE_SPECIFIC_FIELD, seqDoc);
		BasicDBObject query = new BasicDBObject(CONFIG_KEY_NAME, SEQUENCE_DOC_KEY);
		Document doc = collection.findOneAndUpdate(query, update);

		log.debug(doc.toString());	
		
	}

	private Document getDocument(String key, String value) {

		MongoCollection<Document> collection = context.getDatabaseManager()
				.getCollection(collectionName);
		Document doc = collection.find(new BasicDBObject(key, value))
				.first();

		return doc;
	}

}
