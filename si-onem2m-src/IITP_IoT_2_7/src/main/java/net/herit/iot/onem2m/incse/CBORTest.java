package net.herit.iot.onem2m.incse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.bind.DatatypeConverter;

import net.herit.iot.db.mongo.MongoPool;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.facility.CfgManager;
import net.herit.iot.onem2m.incse.facility.DatabaseManager;
import net.herit.iot.onem2m.incse.facility.NseManager;
import net.herit.iot.onem2m.incse.facility.NseManager.BINDING_TYPE;
import net.herit.iot.onem2m.incse.manager.CSEBaseManager;
import net.herit.iot.onem2m.incse.manager.ManagerFactory;
import net.herit.iot.onem2m.incse.manager.dao.ResourceDAO;

import org.bson.Document;
import org.json.JSONObject;
import org.quartz.CronExpression;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;

import co.nstant.in.cbor.CborBuilder;
import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.model.Array;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.MajorType;
import co.nstant.in.cbor.model.SimpleValue;

public class CBORTest {
	
	public static HashMap getHashMap(DataItem dataItem) {
		HashMap hMap = new HashMap();
		
		co.nstant.in.cbor.model.Map map = (co.nstant.in.cbor.model.Map)dataItem;
		HashMap hashMap = new HashMap();
		Iterator<DataItem> itr = map.getKeys().iterator();
		
		while(itr.hasNext()) {
			
			DataItem key = itr.next();
			//System.out.println(map.get(key).getMajorType().toString());
			if(map.get(key).getMajorType().toString().equals("MAP")) {
				
				hMap.put(key.toString(), getHashMap(map.get(key)));
			} else if(map.get(key).getMajorType().toString().equals("ARRAY")) {
				Array cborArr = (Array)map.get(key);
				
				ArrayList<String> arr = new ArrayList<String>();
				for(int i = 0; i < cborArr.getDataItems().size(); i++) {
					arr.add( cborArr.getDataItems().get(i).toString() );
				}
				
				hMap.put(key.toString(), arr);
			} else {
				String value = map.get(key).toString();
				if(map.get(key).getMajorType().name().contains("INTEGER")) {
					hMap.put(key.toString(),Integer.parseInt(value) );
				} else if(map.get(key).getMajorType().name().contains("SPECIAL")){
					SimpleValue simpleValue = (SimpleValue)map.get(key);
					
					String temp = simpleValue.getSimpleValueType().toString().toLowerCase();
					if(temp.equals("true") || temp.equals("false")) {
						hMap.put(key.toString(), Boolean.getBoolean(temp));
					} else {
						hMap.put(key.toString(), temp);
					}
				}else {
					
					hMap.put(key.toString(), value);
				}
				
			}
		}
			
		return hMap;
	}
	
	public static DataItem getDataItem(HashMap<String, Object> map) {
		DataItem dataItem = null;
		
		co.nstant.in.cbor.model.Map cborMap = new co.nstant.in.cbor.model.Map();
		
		Set<?> set = map.entrySet();
		Iterator<?> iterator = set.iterator();
        String valueClassType="";
        while (iterator.hasNext()) 
        {
            Map.Entry entry = (Entry) iterator.next();
            valueClassType = entry.getValue().getClass().getSimpleName();
            
            DataItem diKey = new co.nstant.in.cbor.model.UnicodeString(entry.getKey().toString());
            DataItem diValue = null;
            System.out.println("#### valueClassType = " + valueClassType);
            if(valueClassType.equals("LinkedHashMap")) {
            	HashMap<String, Object> subMap = (HashMap<String, Object>)entry.getValue();
            	diValue = getDataItem(subMap);
            } else if(valueClassType.equals("ArrayList")) {
            	ArrayList<Object> arrObjs = (ArrayList<Object>)entry.getValue();
            	Array diArr = new Array();
            	DataItem tmpDi;
            	for(Object obj : arrObjs ) {
            		tmpDi = new co.nstant.in.cbor.model.UnicodeString(obj.toString());
            		diArr.add(tmpDi);
            	}
            	diValue = (Array)diArr;
            } else if(valueClassType.equals("Boolean")) {
            	Boolean val = (Boolean)entry.getValue();
            	if(val) {
            		diValue = SimpleValue.TRUE;
            	} else {
            		diValue = SimpleValue.FALSE;
            	}
            } else {
            	if(entry.getValue().getClass().getSimpleName().contains("Integer")) {
            		int nVal = Integer.parseInt(entry.getValue().toString());
            		if(nVal >= 0) {
            			diValue = new co.nstant.in.cbor.model.UnsignedInteger(Long.parseLong(entry.getValue().toString()));
            		} else {
            			diValue = new co.nstant.in.cbor.model.NegativeInteger(Long.parseLong(entry.getValue().toString()));
            		}
            		
            	} else {
            		diValue = new co.nstant.in.cbor.model.UnicodeString(entry.getValue().toString());
            	}
            	
            }
            
            cborMap.put(diKey, diValue);
            
        }
        
		return cborMap;
	}
	
	public static void main(String[] args) throws Exception {
		try {
			//String hexText = "A1676D326D3A727170A6626F700162746F772F2F6578616D706C652E6E65742F6D6E63736531323334637271696541313030306372636E07627063A1666D326D3A6165A362726E74536D617274486F6D654170706C69636174696F6E63617069644E6135366361706E676170703132333462747902";
			String hexText = "A1666D326D3A6165A6636C626C82666875626973736561646D696E6265746E32303137313033543132323332316361706E736F6E656D326D506C6174666F726D41646D696E63706F61817331302E3130312E3130312E3131313A38303830636170696774657374617070627272F4";
			//String hexText = "bf6346756ef563416d7421ff";
			byte[] bytes = new BigInteger(hexText, 16).toByteArray();
			
			System.out.println("CBOR::" + new String(bytes, "UTF-8"));
			
			InputStream inputStream = new ByteArrayInputStream(bytes);
			CborDecoder decoder = new CborDecoder(inputStream);
			List<DataItem> dataItems = decoder.decode();
			
			HashMap hashMap = new HashMap();
			
			hashMap = getHashMap(dataItems.get(1));
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("__message__", hashMap);
			
			jsonObject = (JSONObject)jsonObject.get("__message__");
			
			System.out.println(jsonObject.toString());
			
			
			//String jsonStr = "{\"m2m:rqp\": {\"op\": 1, \"to\": \"//example.net/mncse1234\", \"rqi\": \"A1000\", \"rcn\": 7, \"pc\": {\"m2m:ae\": {\"rn\": \"SmartHomeApplication\", \"api\": \"Na56\", \"apn\": \"app1234\"}}, \"ty\": 2}}";
			String jsonStr = "{\"m2m:ae\":{\"lbl\":[\"hubiss\",\"admin\"],\"poa\":[\"10.101.101.111:8080\"],\"rr\":false,\"api\":\"testapp\",\"et\":\"2017103T122321\",\"apn\":\"onem2mPlatformAdmin\"}}";
			ObjectMapper mapper = new ObjectMapper();
			
			HashMap<String, Object> map = mapper.readValue(jsonStr, new TypeReference<Map<String, Object>>(){});
			
			DataItem dataItem = getDataItem(map);
			
			hashMap = getHashMap(dataItem);
			jsonObject = new JSONObject();
			jsonObject.put("__message__", hashMap);
			
			jsonObject = (JSONObject)jsonObject.get("__message__");
			
			System.out.println(jsonObject.toString());
			
		
			ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
			CborEncoder encoder = new CborEncoder(byteOutputStream);
			encoder.encode(dataItem);
			
			byte[] cborBytes = byteOutputStream.toByteArray();
			
			StringBuffer sb = new StringBuffer();
			
			//String cborEncodedStr = new BigInteger(cborBytes).toString(16);
			for(int i = 0; i < cborBytes.length; i++) {
				sb.append(Integer.toHexString(0x0100 + (cborBytes[i] & 0x00FF)).substring(1));

			}
			
			System.out.println(sb.toString());
	/**		
			CfgManager cfgManager = CfgManager.getInstance();
			
			cfgManager.initialize();
			
			MongoPool.getInstance().initialize(cfgManager.getDatabaseHost(), cfgManager.getDatabasePort(), cfgManager.getDatabaseName(), 
												cfgManager.getDatabaseUser(), cfgManager.getDatabasePassword());
			
			DatabaseManager dbManager = DatabaseManager.getInstance();
			
			dbManager.initialize(MongoPool.getInstance());
			
			MongoCollection<Document> collection = dbManager.getCollection(CfgManager.getInstance().getResourceDatabaseName());
			Document doc = collection.find(new BasicDBObject("ri", "NODE_0"))
					.first();
	
			Date date;
			CronExpression exp;
			
			//String a = "* 3 * * * ? 2017";
			
			try {
				exp = new CronExpression(a);
				date = exp.getNextValidTimeAfter(new Date());
				System.out.println("####### cron expression ######## ");
				
				if(date == null) {
					System.out.println("XXXXXXXXXXXX");
				}
				System.out.println(date);
			
			}catch(ParseException e) {
				e.printStackTrace();
			}
**/			
			org.springframework.util.AntPathMatcher pathMatcher = new org.springframework.util.AntPathMatcher();
			String path = "/text1/value1";
		    ArrayList<String> entries = new ArrayList<String>();
		    entries.add("/text1/*");
		    entries.add("/text*/*");
		    entries.add("/text2/*/*");
		    entries.add("/text2/*/*/*");
		    entries.add("/text1/*/*");
		    entries.add("/text1/value*");
		    entries.add("/*/*");
		    entries.add("/*");

		    String bestMatch = null;
		    // use a pattern comparator to see what pattern best matches this path
		    
		    Comparator<String> comp = pathMatcher.getPatternComparator(path);
		    // go through all our patterns
		    for(String pattern : entries) {
		        // make sure the pattern matches the path
		        if(pathMatcher.match(pattern, path)) {
		            // check if we already have a match
		            System.out.println("=====> pattern=" + pattern);
		        }
		    }
		    System.out.println(bestMatch);	
			
			
		} catch(Exception e) {
			e.printStackTrace();
			
			throw e;
		}
		
	}
}
