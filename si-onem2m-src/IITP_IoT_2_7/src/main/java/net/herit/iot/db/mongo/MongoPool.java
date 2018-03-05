package net.herit.iot.db.mongo;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoDatabase;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;

public class MongoPool {
	private final static MongoPool INSTANCE = new MongoPool();
	
	private MongoClient mongoClient = null;
//	private String serverAddr;
//	private int serverPort;
	private String databaseName;
//	private String userName;
//	private String userPasswd;
	
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
		
//		this.serverAddr = serverAddr;
//		this.serverPort = serverPort;
		this.databaseName = databaseName;
//		this.userName = userName;
//		this.userPasswd = userPasswd;
		
		
		if(userName != null || userPasswd != null) {
			//MongoCredential credential = MongoCredential.createCredential(userName, databaseName, userPasswd.toCharArray());		// blocked in 2017-11-10
			MongoCredential credential = MongoCredential.createScramSha1Credential(userName, databaseName, userPasswd.toCharArray());		// added in 2017-11-10 to support Mongodb authentication
			mongoClient = new MongoClient(new ServerAddress(serverAddr, serverPort), Arrays.asList(credential));
			
		} else {
		//	mongoClient = new MongoClient(serverAddr, serverPort);
			MongoClientOptions options = new MongoClientOptions.Builder()
			.threadsAllowedToBlockForConnectionMultiplier(20)
			.maxConnectionIdleTime(60000)
			.connectionsPerHost(512)
		//	.minConnectionsPerHost(64)
			.build();
		//	.socketTimeout(180000)
		//	.socketKeepAlive(true)
			//.writeConcern(WriteConcern.SAFE)
			//.connectTimeout(1000).build();	
			
			mongoClient = new MongoClient(serverAddr + ":" + serverPort, options); 
	
			//MongoClientURI  uri = new MongoClientURI("mongodb://" + serverAddr + ":" + serverPort + "/?minPoolSize=100&maxPoolSize=200");
			//mongoClient = new MongoClient(uri);
		}
		
		Logger mongoLogger = Logger.getLogger( "com.mongodb" ); 
		mongoLogger.setLevel(Level.SEVERE);
		//mongoLogger.setLevel(Level.INFO);

		return true;
	}

	public MongoDatabase getDatabase() {
		return mongoClient.getDatabase(databaseName);
	}
	
	public int getCurrConnection() {
		MongoDatabase db = mongoClient.getDatabase("admin");
		Document serverStatus = db.runCommand(new Document("serverStatus", 1));
		Map connMap = (Map)serverStatus.get("connections");
		
		return (Integer)connMap.get("current");
	}
	
}
