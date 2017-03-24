package org.eclipse.leshan.server.onem2m;

import org.eclipse.leshan.server.Constants;
import org.eclipse.leshan.server.Lwm2mServerConfig;
import org.eclipse.leshan.server.api.Util;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.eclipse.leshan.server.onem2m.resources.AE;
import org.eclipse.leshan.server.onem2m.resources.Resource;
import org.eclipse.leshan.server.onem2m.resources.ResourceHandler;

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
	    	StringBuffer address_buf = new StringBuffer(config.getIncseAddress());
	    	
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
	        Date endtime = new Date(today.getTime() + (1000 * 60 * 60 * 24 * Constants.DAYS_FOR_EXP)); // after one year
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
    
    
    /*
    public JSONObject temp(Resource res) throws Exception{

        client.setRequestHeaderForTemp();
        client.sendRequest(res.getResToJson());

        JSONObject response = client.getResponse();
        client.closeConnection();

        return response;
    }

    
    // Create
    public JSONObject create(Resource res, int type, String ...headerInfos) throws Exception{

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
    	
    	client.setRequestHeader(makeHostAddress(), resourceId, resourceName, originId, type);
    	
    	if( type == Constants.AE ){
	        JSONArray poa = new JSONArray();
	        poa.put(client.getAuthority());
	    	res.getResToJson().getJSONObject(res.getNameSpace(res)).put("poa", poa);
    	}
    	client.sendRequest(res.getResToJson());
    	
    	JSONObject response = client.getResponse();
    	return response;
    }
    
    // Retrieve
    public JSONObject retrieve(Resource res, int type, String ...headerInfos) throws Exception{
    	
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
    	
    	client.setRequestHeader(makeHostAddress(), resourceId, resourceName, originId, type);
    	client.sendRequest(res.getResToJson());
    	
    	JSONObject response = client.getResponse();
    	return response;
    }


    /*

    /******************************
     *  # Container
     *******************************/
    /*
    // create
    public JSONObject createContainer(ArrayList<String> labels, FoundItem foundItem) {

        JSONObject jsonResponse = new JSONObject();

        try {
            JSONArray lblArray = new JSONArray();

            for(String label : labels) {
                lblArray.put(label);
            }

            client.openConnection();
            client.setRequestHeader(this.host, this.deviceId, foundItem.getResourceName(), foundItem.getResourceUri(), MemberType.CONTAINER);

            JSONObject body_wrap = new JSONObject();
            JSONObject body = new JSONObject();

            body.put("rn", foundItem.getResourceName());
            body.put("mni", 100);
            body.put("mbs", 1024000);
            body.put("mia", 36000);
            body.put("lbl", lblArray);
            body.put("et", strEndTime);
            body_wrap.put("cnt",body);

            client.sendRequest(body_wrap);

            jsonResponse = client.getResponse();

        }catch(IOException ioe) {
            ioe.printStackTrace();
        }catch(JSONException jsone) {
            jsone.printStackTrace();
        }catch(Exception e) {
            e.printStackTrace();
        }

        return jsonResponse;
    }
    public JSONObject createContainer(ArrayList<String> labels, FoundDeviceInfo.ResourceInfo resourceInfo) {
        String parentId = "OIC-" + this.deviceId;
        JSONObject jsonResponse = new JSONObject();
        String resourceName = resourceInfo.resourceUri.substring(1);
        String resourceUri = resourceInfo.resourceUri;

        try {
            String strOrigin = parentId + resourceUri;
            JSONArray lblArray = new JSONArray();

            for(String label : labels) {
                lblArray.put(label);
            }

            client.openConnection();
            client.setRequestHeader(this.host, this.deviceId, strOrigin, resourceName, CONTAINER);

            JSONObject body_wrap = new JSONObject();
            JSONObject body = new JSONObject();

            body.put("rn",resourceName);
            body.put("mni", 100);
            body.put("mbs", 1024000);
            body.put("mia", 36000);
            body.put("lbl", lblArray);
            body.put("et", strEndTime);
            body_wrap.put("cnt",body);

            client.sendRequest(body_wrap);

            jsonResponse = client.getResponse();

        }catch(IOException ioe) {
            ioe.printStackTrace();
        }catch(JSONException jsone) {
            jsone.printStackTrace();
        }catch(Exception e) {
            e.printStackTrace();
        }

        return jsonResponse;
    }
    public JSONObject createContainer(ArrayList<String> labels, OcResource ocResource) {
        String parentId = "OIC-" + this.deviceId;
        String resourceUri = ocResource.getUri();
        JSONObject jsonResponse = new JSONObject();
        String resourceName = ocResource.getUri().substring(1);

        try {
            JSONObject body = new JSONObject();
            String strOrigin = parentId + resourceUri;
            JSONArray lblArray = new JSONArray();

            for(String label : labels) {
                lblArray.put(label);
            }


            Date endtime = new Date(today.getTime() + (1000 * 60 * 60 * 24 * 365)); // after one year
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddTHHmmss");

            client.openConnection();

            client.setRequestHeader(this.host, this.deviceId, strOrigin, resourceName, CONTAINER);

            body.put("mni", 100);
            body.put("mbs", 1024000);
            body.put("mia", 36000);
            body.put("lbl", lblArray);
            body.put("et", sf.format(endtime));

            client.sendRequest(body);

            jsonResponse = client.getResponse();

        }catch(IOException ioe) {
            ioe.printStackTrace();
        }catch(JSONException jsone) {
            jsone.printStackTrace();
        }catch(Exception e) {
            e.printStackTrace();
        }
        return jsonResponse;
    }
    public JSONObject createContainer(ArrayList<String> labels, OcRepresentation ocRepresentation) {
        String parentId = "OIC-" + this.deviceId;
        String resourceUri = ocRepresentation.getUri();
        JSONObject jsonResponse = new JSONObject();
        String resourceName = ocRepresentation.getUri().substring(1);

        try {
            JSONObject body = new JSONObject();
            String strOrigin = parentId + resourceUri;
            JSONArray lblArray = new JSONArray();

            for(String label : labels) {
                lblArray.put(label);
            }

            Date endtime = new Date(today.getTime() + (1000 * 60 * 60 * 24 * 365)); // after one year
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddTHHmmss");

            client.openConnection();

            client.setRequestHeader(this.host, this.deviceId, strOrigin, resourceName, CONTAINER);

            body.put("mni", 100);
            body.put("mbs", 1024000);
            body.put("mia", 36000);
            body.put("lbl", lblArray);
            body.put("et", sf.format(endtime));

            client.sendRequest(body);

            jsonResponse = client.getResponse();

        }catch(IOException ioe) {
            ioe.printStackTrace();
        }catch(JSONException jsone) {
            jsone.printStackTrace();
        }catch(Exception e) {
            e.printStackTrace();
        }
        return jsonResponse;
    }
    public JSONObject createContentInstance(FoundItem foundItem, ArrayList<String> labels, String content) {
        JSONObject jsonResponse = new JSONObject();

        try {
            JSONObject body_wrap = new JSONObject();
            JSONObject body = new JSONObject();
            JSONArray lblArray = new JSONArray();

            for(String label : labels) {
                lblArray.put(label);
            }

            client.openConnection();
            client.setRequestHeader(this.host, this.deviceId, foundItem.getResourceName(), foundItem.getResourceUri(), MemberType.CONTENT_INSTANCE);

            body.put("cnf", "text/plain:0");
            body.put("lbl", lblArray);
            body.put("et", strEndTime);
            body.put("con", content);
            body_wrap.put("cin",body);

            client.sendRequest(body_wrap);

            jsonResponse = client.getResponse();

        }catch(IOException ioe) {
            ioe.printStackTrace();
        }catch(JSONException jsone) {
            jsone.printStackTrace();
        }catch(Exception e) {
            e.printStackTrace();
        }
        return jsonResponse;
    }
    */

    /******************************
     *  # Polling Channel
     *******************************/
    /*
    // create
    public JSONObject createPollingChannel(ArrayList<String> labels, FoundItem oicDevice) {

        JSONObject jsonResponse = new JSONObject();

        try {
            JSONArray lblArray = new JSONArray();

            for(String label : labels) {
                lblArray.put(label);
            }

            client.openConnection();
            client.setRequestHeader(this.host, this.deviceId, this.deviceId, oicDevice.getResourceUri(), MemberType.POLLING_CHANNEL);

            JSONObject body_wrap = new JSONObject();
            JSONObject body = new JSONObject();

            body.put("rn", oicDevice.getResourceName());
            body.put("lbl", lblArray);
            body.put("et", strEndTime);
            body_wrap.put("pch",body);

            client.sendRequest(body_wrap);

            jsonResponse = client.getResponse();

        }catch(IOException ioe) {
            ioe.printStackTrace();
        }catch(JSONException jsone) {
            jsone.printStackTrace();
        }catch(Exception e) {
            e.printStackTrace();
        }
        return jsonResponse;
    }
    // retrieve
    public String retrievePollingChannelPCU() {

        String result= "";
        try {

            client.openConnection();
            client.setRequestHeaderForPCU(this.host, this.deviceId, "TEMP_ORIGIN");
            result = client.getResponseByXml();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (JSONException jsone) {
            jsone.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public void createGroup() {

    }
    */
    /******************************
     *  # Subscription
     *******************************/
    // create
    /*
    public JSONObject createSubscription(ArrayList<String> labels, FoundItem foundItem) {

        JSONObject jsonResponse = new JSONObject();

        try {
            JSONArray lblArray = new JSONArray();

            for(String label : labels) {
                lblArray.put(label);
            }

            client.openConnection();
            client.setRequestHeader(this.host, this.deviceId, foundItem.getResourceName(), foundItem.getResourceUri(), MemberType.SUBSCRIPTION);

            JSONObject body_wrap = new JSONObject();
            JSONObject body = new JSONObject();

            body.put("rn", foundItem.getResourceName());
            body.put("lbl", lblArray);
            body.put("et", strEndTime);
            body.put("nu", foundItem.getPcuUri());
            body_wrap.put("sub",body);

            client.sendRequest(body_wrap);

            jsonResponse = client.getResponse();

        }catch(IOException ioe) {
            ioe.printStackTrace();
        }catch(JSONException jsone) {
            jsone.printStackTrace();
        }catch(Exception e) {
            e.printStackTrace();
        }

        return jsonResponse;
    }
    */

    public void closeConnecton() {
        this.client.closeConnection();
    }
}
