package org.eclipse.leshan.server.extension.onem2m;

import org.eclipse.leshan.server.Lwm2mServerConfig;
import org.eclipse.leshan.server.extension.Constants;
import org.eclipse.leshan.server.extension.Util;
import org.eclipse.leshan.server.extension.onem2m.resources.AE;
import org.eclipse.leshan.server.extension.onem2m.resources.Resource;
import org.eclipse.leshan.server.extension.onem2m.resources.ResourceHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by seung-wanmun on 2016. 8. 24..
 */
public class HttpOneM2MOperation {

	private Lwm2mServerConfig config = Lwm2mServerConfig.getInstance();
    private final static String TAG = HttpOneM2MOperation.class.getSimpleName();
    private HttpOneM2MClient client;
    private Date today;
    private String address;
    
    // request header info
    private String originId;
    private String resourceName;
    private String resourceId;
    
    private String strEndTime;
    
    public void init(String ...address) {
    	try {
	    	StringBuffer address_buf = new StringBuffer(config.getSiUri());
	    	
	    	if( !(address.length == 1 && address[0].equals("")) ){
	    		for (int i = 0; i < address.length; i++) {
	    			if( address[i].indexOf("?") == -1 ){
	    				address_buf.append("/");
	    			}
	    			address_buf.append(address[i]);
	    		}
	    	}
	    	this.address = address_buf.toString();
	        today = new Date();
	        client = HttpOneM2MClient.getInstance();
			client.init(this.address);
			System.out.println("addr : " + this.address);
			
			// set Date
	        Date endtime = new Date(today.getTime() + (1000 * 60 * 60 * 24 * Constants.DAYS_FOR_EXPIRED)); // after one year
	        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd HHmmss");
	        String tmpStr = sf.format(endtime);
	        String[] arrDateItem = tmpStr.split(" ");
	        strEndTime = arrDateItem[0] + "T" + arrDateItem[1];
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public String getAddress(){
    	return address;
    }
    public String makeHostAddress(){
    	String resultHost = null;
    	int idx = address.lastIndexOf("/");
    	if( idx == address.length() ){
    		resultHost = address.substring(0, address.substring(0,idx-1).lastIndexOf("/"));
    	}
    	return resultHost;
    }
    
    // set request header info    
    public void setRequestHeaderInfoForAE(String resName){
    	resourceName = resName;
    	resourceId = null;
    	originId = "S";
    }
    public void setRequestHeaderInfo(String resName, String resId, String from){
    	resourceName = resName;
    	resourceId = resId;
    	originId = from; 
    }
    
    public JSONObject operate(Resource res, int type, Constants.REQUEST_METHOD_TYPE req_type, String ...headerInfos) throws Exception{
    	
    	// set header
    	if( headerInfos != null && headerInfos.length != 0 ){
	    	if( headerInfos.length != 1 && headerInfos.length != 3 ){
	    		throw new Exception("header 정보가 올바르지 않습니다.");
	    	} else {
	    		switch (headerInfos.length){
		    	case 1:
		    		setRequestHeaderInfoForAE(headerInfos[0]);
		    		break;
		    	case 3:
		    		setRequestHeaderInfo(headerInfos[0], headerInfos[1], headerInfos[2]);
		    		break;
		    	}
	    	}
    	} else {
    		setRequestHeaderInfo(null, null, null);
    	}
    	
    	// set body
    	if( req_type == Constants.REQUEST_METHOD_TYPE.POST ){
    		client.setRequestHeader(makeHostAddress(), resourceId, resourceName, originId, type);
	    	if( type == Constants.AE ){
		        JSONArray poa = new JSONArray();
		        poa.put(client.getAuthority());
		    	res.getResToJson().getJSONObject(res.getNameSpace(res)).put("poa", poa);
	    	}
	    	client.sendRequest(res.getResToJson());
    	} else {
    		client.setRequestHeader(makeHostAddress(), resourceId, resourceName, originId, type, req_type.Value());
    	}
    	
    	JSONObject response = client.getResponse();
    	client.closeConnection();
    	
    	return response;
    }
    
    public String retrievePCU(int type) throws Exception{

    	client.setRequestHeaderForPCU("unnamed","unnamed","unnamed");
    	String response = client.getResponseByXml();
    	client.closeConnection();
    	
    	return response;
    }

    public void closeConnecton() {
        this.client.closeConnection();
    }
}
