package com.canonical.democlient;

import org.iotivity.base.OcRepresentation;
import org.iotivity.base.OcResource;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by seung-wanmun on 2016. 8. 24..
 */
public class HttpOneM2MOperation {

    private final static String TAG = HttpOneM2MOperation.class.getSimpleName();
    private HttpOneM2MClient client;

    private static final int AE = 2;
    private static final int CONTAINER = 3;
    private static final int CONTENTINSTANCE = 4;
    private static final int GROUP = 9;
    private static final int SUBSCRIPTION = 23;

    private Date today;
    private String host;
    private String deviceId;

    private String strEndTime;

    public void init(String addr, FoundItem oicDevice) {
        this.today = new Date();
        this.client = new HttpOneM2MClient(addr);
        this.host = oicDevice.getHost();
        this.deviceId = oicDevice.getDeviceId();

        Date endtime = new Date(today.getTime() + (1000 * 60 * 60 * 24 * 365)); // after one year
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd HHmmss");
        String tmpStr = sf.format(endtime);
        String[] arrDateItem = tmpStr.split(" ");
        strEndTime = arrDateItem[0] + "T" + arrDateItem[1];
    }

    /******************************
    *  # AE
    *******************************/
    // create
    public JSONObject createAE(ArrayList<String> labels, FoundItem oicDevice) {

        JSONObject jsonResponse = new JSONObject();

        try {
            JSONObject body_wrap = new JSONObject();
            JSONObject body = new JSONObject();
            JSONArray lblArray = new JSONArray();

            for(String label : labels) {
                lblArray.put(label);
            }

            client.openConnection();
            client.setRequestHeader(this.host, this.deviceId, this.deviceId, oicDevice.getResourceUri(), AE);

            body.put("rn", oicDevice.getResourceName());
            body.put("apn", oicDevice.getDeviceName());
            body.put("api", this.deviceId);
            body.put("rr", false);
            body.put("lbl", lblArray);
            body.put("et", strEndTime);

            lblArray = new JSONArray();
            lblArray.put(client.getAuthority());   // poa
            body.put("poa", lblArray);
            body_wrap.put("ae",body);

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
    public JSONObject retrieveAE(FoundItem oicDevice) {

        JSONObject jsonResponse = new JSONObject();

        try {
            String strOrigin = "OIC-" + this.deviceId;

            client.openConnection();
            client.setRequestHeader(this.host, this.deviceId, this.deviceId, strOrigin, MemberType.AE, Constants.REQUEST_METHOD_TYPE.GET.Value());
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
    // delete
    public JSONObject deleteAE(FoundItem oicDevice) {

        JSONObject jsonResponse = new JSONObject();

        try {
            client.openConnection();
            client.setRequestHeader(this.host, this.deviceId, this.deviceId, oicDevice.getResourceUri(), MemberType.AE, Constants.REQUEST_METHOD_TYPE.DELETE.Value());

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

    /******************************
     *  # Container
     *******************************/
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

    /******************************
     *  # Polling Channel
     *******************************/
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

    /******************************
     *  # Subscription
     *******************************/
    // create
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

    public void closeConnecton() {
        this.client.closeConnection();
    }
}
