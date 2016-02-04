package net.herit.iot.onem2m.incse.facility;

import org.bson.Document;

import net.herit.iot.db.mongo.MongoPool;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class DatabaseManager {
	
	private static DatabaseManager INSTANCE = new DatabaseManager();
	
	private MongoPool mongoPool = null;
	
	private DatabaseManager() {}
	
	public static DatabaseManager getInstance() {
		return INSTANCE;
	}

	public void initialize(MongoPool mongoPool) {
		this.mongoPool = mongoPool;
	}


	public MongoDatabase getMongoDB() {
		return mongoPool.getDatabase();
	}
	
	public MongoCollection<Document> getCollection(String collectionName) {
		return getMongoDB().getCollection(collectionName);
	}
}
