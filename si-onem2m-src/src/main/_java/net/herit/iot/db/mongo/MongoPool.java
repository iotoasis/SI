package net.herit.iot.db.mongo;

import java.util.Arrays;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MongoPool {
	private static MongoPool INSTANCE = new MongoPool();
	
	private MongoClient mongoClient = null;

	private String databaseName;
	
	private MongoPool() {}
	
	public static MongoPool getInstance() {
		return INSTANCE;
	}
	
	public boolean initialize(String serverAddr, int serverPort, String databaseName, 
								String userName, String userPasswd) {
		if(serverAddr == null) serverAddr = "localhost";
		
		if(serverPort <= 0 || databaseName == null) {
			System.out.println("Database Server port or database name invalid..");
			return false;
		}
		
		this.databaseName = databaseName;
		
		
		if(userName != null || userPasswd != null) {
			MongoCredential credential = MongoCredential.createCredential(userName, databaseName, userPasswd.toCharArray());
			mongoClient = new MongoClient(new ServerAddress(serverAddr, serverPort), Arrays.asList(credential));
		} else {
			mongoClient = new MongoClient(serverAddr, serverPort);
		}
		
		Logger mongoLogger = Logger.getLogger( "com.mongodb" ); 
		mongoLogger.setLevel(Level.SEVERE);

		return true;
	}

	public MongoDatabase getDatabase() {
		return mongoClient.getDatabase(databaseName);
	}
	
}
