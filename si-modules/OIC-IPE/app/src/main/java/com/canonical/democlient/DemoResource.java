package com.canonical.democlient;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.iotivity.base.ErrorCode;
import org.iotivity.base.ObserveType;
import org.iotivity.base.OcConnectivityType;
import org.iotivity.base.OcException;
import org.iotivity.base.OcHeaderOption;
import org.iotivity.base.OcPlatform;
import org.iotivity.base.OcRepresentation;
import org.iotivity.base.OcResource;
import org.iotivity.base.OcResourceIdentifier;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gerald on 2015/11/13.
 */
public class DemoResource implements
        OcPlatform.OnResourceFoundListener,
        OcResource.OnGetListener,
        OcResource.OnPutListener,
        OcResource.OnPostListener,
        OcResource.OnObserveListener {

    protected String TAG = this.getClass().getName();

    private FoundItem item;
    private FoundDeviceInfo deviceInfo = new FoundDeviceInfo();

    protected Activity main_activity;
    protected Context main_context;
    protected OcPlatform.OnResourceFoundListener resource_found_listener;
    protected ArrayList<String> main_list_item;
    protected ArrayAdapter<String> main_list_adapter;
    protected Map<OcResourceIdentifier, OcResource> mFoundResources = new HashMap<>();
    protected OcResource mResource = null;
    protected Thread find_thread = null;
    protected boolean find_thread_running = true;
    protected Thread update_thread = null;
    protected boolean update_thread_running = true;

    protected String resource_type = "";
    protected String resource_uri = "";

    protected boolean inObserve = false;

    public final static String msg_type_found = "msg_found_resource";
    public String msg_content_found = "";

    public String msg_type_put_done = "";
    public final static String msg_content_put_done = "put_done";

    public String msg_type_get_done = "";
    public final static String msg_content_get_done = "get_done";

    public String msg_type_set = "";

    public String server_addr = "";

    public DemoResource() {}

    public DemoResource(Activity main, Context c, ArrayList<String> list_item,
                        ArrayAdapter<String> list_adapter) {
        main_activity = main;
        main_context = c;
        resource_found_listener = this;

        main_list_item = list_item;
        main_list_adapter = list_adapter;
    }

    protected void setup_found_listener(OcPlatform.OnResourceFoundListener found_listener) {
        resource_found_listener = found_listener;
    }

    public void find_resource() {
        start_find_thread();
    }

    protected void find_thread() {
        Log.e(TAG, "Start find thread");
        String requestUri;

        Log.e(TAG, "Finding resources of type: " + resource_type);
        requestUri = OcPlatform.WELL_KNOWN_QUERY + "?rt=" + resource_type;

        while(mResource == null && !Thread.interrupted() && find_thread_running) {
            try {
                OcPlatform.findResource("",
                        requestUri,
                        EnumSet.of(OcConnectivityType.CT_DEFAULT),
                        resource_found_listener
                );
            } catch (OcException e) {
                Log.e(TAG, e.toString());
                Log.e(TAG, "Failed to invoke find resource API");
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Log.e(TAG, "InterruptedException");
                return;
            }
        }
    }

    protected void update_thread() {
        Log.e(TAG, "Need to implement this function");
    }

    protected void start_find_thread() {
        find_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                find_thread();
            }
        });
        find_thread.start();
    }

    protected void stop_find_thread() {
        if(find_thread != null) {
            find_thread_running = false;
            find_thread.interrupt();
        }
    }

    public void start_update_thread() {
        update_thread = new Thread(new Runnable(){
            @Override
            public void run() {
                update_thread();
            }
        });
        update_thread.start();
    }

    public void stop_update_thread() {
        if(update_thread != null) {
            update_thread_running = false;
            update_thread.interrupt();
        }
    }

    protected void update_list() {
        Log.e(TAG, "Need to implement this function");
    }

    protected void setFromIntent(Intent intent) {
        Log.e(TAG, "Need to implement this function");
    }

    protected BroadcastReceiver setReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            setFromIntent(intent);
        }
    };

    protected void sendBroadcastMessage(String type, String key, boolean b) {
        Intent intent = new Intent(type);

        intent.putExtra(key, b);
        Log.e(TAG, "Send message: " + type + ", " + key + ", " + String.valueOf(b));
        LocalBroadcastManager.getInstance(main_context).sendBroadcast(intent);
    }

    protected void sendGetCompleteMessage() {
        Log.e(TAG, "Need to implement this function");
    }

    protected void sendPutCompleteMessage() {
        Log.e(TAG, "Need to implement this function");
    }

    protected void post_reset() {

    }

    public void reset() {
        stop_find_thread();
        stop_update_thread();

        if(inObserve) {
            observeCancelResource();
        }

        post_reset();

        mFoundResources.clear();
    }



    public void setOcRepresentation(OcRepresentation rep) throws OcException {
        Log.e(TAG, "Need to implement this function");
    }

    public OcRepresentation getOcRepresentation() throws OcException {
        OcRepresentation rep = new OcRepresentation();
        Log.e(TAG, "Need to implement this function");
        return rep;
    }

    public void getRep() {
        getResourceRepresentation();
    }

    public void putRep() {

    }

    protected void getResourceRepresentation() {
        Log.e(TAG, "Getting Representation...");

        Map<String, String> queryParams = new HashMap<>();
        try {
            // Invoke resource's "get" API with a OcResource.OnGetListener event
            // listener implementation
            mResource.get(queryParams, this);
        } catch (OcException e) {
            Log.e(TAG, e.toString());
            Log.e(TAG, "Error occurred while invoking \"get\" API");
        }
    }

    protected void putResourceRepresentation() {
        Log.e(TAG, "Putting representation...");

        OcRepresentation representation = null;
        try {
            representation = getOcRepresentation();
        } catch (OcException e) {
            Log.e(TAG, e.toString());
            Log.e(TAG, "Failed to set OcRepresentation");
        }

        Map<String, String> queryParams = new HashMap<>();

        try {
            // Invoke resource's "put" API with a new representation, query parameters and
            // OcResource.OnPutListener event listener implementation
            mResource.put(representation, queryParams, this);
        } catch (OcException e) {
            Log.e(TAG, e.toString());
            Log.e(TAG, "Error occurred while invoking \"put\" API");
        }
    }

    public void observeFoundResource() {
        try {
            // Invoke resource's "observe" API with a observe type, query parameters and
            // OcResource.OnObserveListener event listener implementation
            mResource.observe(ObserveType.OBSERVE, new HashMap<String, String>(), this);
        } catch (OcException e) {
            Log.e(TAG, e.toString());
            Log.e(TAG, "Error occurred while invoking \"observe\" API");
        }
    }

    public void observeCancelResource() {
        Log.e(TAG, "Cancel observe");
        try {
            mResource.cancelObserve();
         } catch (OcException e) {
            Log.e(TAG, e.toString());
            Log.e(TAG, "Error occurred while invoking \"cancelObserve\" API");
        }

    }


    @Override
    public synchronized void onResourceFound(OcResource ocResource) {

        if (null == ocResource) {
            Log.e(TAG, "Found resource is invalid");
            return;
        }

        if (mFoundResources.containsKey(ocResource.getUniqueIdentifier())) {
            Log.e(TAG, "Found a previously seen resource again!");
        } else {

            if( !Constants.old_host.equals(ocResource.getHost()) ) {
                // item 기본정보 등록
                item = new FoundItem();
                item.setHost(ocResource.getHost());
                item.setDeviceId(ocResource.getServerId());
                item.setDeviceName("(Arduino)");

                // FoundDeviceInfo에 넣어 MainActivity로 반환
                deviceInfo.addFoundItem(item);
                deviceInfo.addResource(ocResource.getHost(), ocResource.getUri(), ocResource.getResourceTypes());
                Constants.old_host = ocResource.getHost();
                Constants.deviceInfo = deviceInfo;
                Log.i(">>>>>>>>>>>>>>>", "                               FOUND IT !!!  ");
            } else {
                Log.i(">>>>>>>>>>>>>>>", "                               ALREADY REGISTERED !!!  ");
            }

            Log.e(TAG, "Found resource for the first time on server with ID: " + ocResource.getServerId());
            mFoundResources.put(ocResource.getUniqueIdentifier(), ocResource);
        }

        // Get the resource URI
        String resourceUri = ocResource.getUri();
        // Get the resource host address
        String hostAddress = ocResource.getHost();
        Log.e(TAG, "\tURI of the resource: " + resourceUri);
        Log.e(TAG, "\tHost address of the resource: " + hostAddress);
        server_addr = hostAddress;
        // Get the resource types
        Log.e(TAG, "\tList of resource types: ");
        for (String resourceType : ocResource.getResourceTypes()) {
            Log.e(TAG, "\t\t" + resourceType);
        }
        Log.e(TAG, "\tList of resource interfaces:");
        for (String resourceInterface : ocResource.getResourceInterfaces()) {
            Log.e(TAG, "\t\t" + resourceInterface);
        }
        Log.e(TAG, "\tList of resource connectivity types:");
        for (OcConnectivityType connectivityType : ocResource.getConnectivityTypeSet()) {
            Log.e(TAG, "\t\t" + connectivityType);
        }

        if (resourceUri.equals(resource_uri)) {
            if (mResource != null) {
                Log.e(TAG, "Found another resource, ignoring");
                return;
            }

            //Assign resource reference to a global variable to keep it from being
            //destroyed by the GC when it is out of scope.
            mResource = ocResource;

            sendBroadcastMessage(msg_type_found, msg_content_found, true);
        }
    }

    @Override
    public synchronized void onGetCompleted(List<OcHeaderOption> list,
                                            OcRepresentation ocRepresentation) {
        Log.e(TAG, "GET request was successful");
        Log.e(TAG, "Resource URI: " + ocRepresentation.getUri());

        try {
            setOcRepresentation(ocRepresentation);
            sendGetCompleteMessage();
            update_list();
        } catch (OcException e) {
            Log.e(TAG, e.toString());
            Log.e(TAG, "Failed to set LED representation");
        }
    }

    @Override
    public synchronized void onGetFailed(Throwable throwable) {
        if (throwable instanceof OcException) {
            OcException ocEx = (OcException) throwable;
            Log.e(TAG, ocEx.toString());
            ErrorCode errCode = ocEx.getErrorCode();
            //do something based on errorCode
            Log.e(TAG, "Error code: " + errCode);
        }

        Log.e(TAG, "Failed to get representation of a found LED resource");
    }

    @Override
    public synchronized void onPutCompleted(List<OcHeaderOption> list, OcRepresentation ocRepresentation) {
        Log.e(TAG, "PUT request was successful");
        sendPutCompleteMessage();
        update_list();
    }

    @Override
    public synchronized void onPutFailed(Throwable throwable) {
        if (throwable instanceof OcException) {
            OcException ocEx = (OcException) throwable;
            Log.e(TAG, ocEx.toString());
            ErrorCode errCode = ocEx.getErrorCode();
            //do something based on errorCode
            Log.e(TAG, "Error code: " + errCode);
        }
        Log.e(TAG, "Failed to \"put\" a new representation");
    }

    @Override
    public synchronized void onPostCompleted(List<OcHeaderOption> list,
                                             OcRepresentation ocRepresentation) {
        Log.e(TAG, "POST request was successful");
        try {
            if (ocRepresentation.hasAttribute(OcResource.CREATED_URI_KEY)) {
                Log.e(TAG, "\tUri of the created resource: " +
                        ocRepresentation.getValue(OcResource.CREATED_URI_KEY));
            } else {
                setOcRepresentation(ocRepresentation);
            }
        } catch (OcException e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public synchronized void onPostFailed(Throwable throwable) {
        if (throwable instanceof OcException) {
            OcException ocEx = (OcException) throwable;
            Log.e(TAG, ocEx.toString());
            ErrorCode errCode = ocEx.getErrorCode();
            //do something based on errorCode
            Log.e(TAG, "Error code: " + errCode);
        }
        Log.e(TAG, "Failed to \"post\" a new representation");
    }

    @Override
    public synchronized void onObserveCompleted(List<OcHeaderOption> list,
                                                OcRepresentation ocRepresentation,
                                                int sequenceNumber) {
        if (OcResource.OnObserveListener.REGISTER == sequenceNumber) {
            Log.e(TAG, "Observe registration action is successful:");
            inObserve = true;
        } else if (OcResource.OnObserveListener.DEREGISTER == sequenceNumber) {
            Log.e(TAG, "Observe De-registration action is successful");
            inObserve = false;
        } else if (OcResource.OnObserveListener.NO_OPTION == sequenceNumber) {
            inObserve = false;
            Log.e(TAG, "Observe registration or de-registration action is failed");
        }

        Log.e(TAG, "OBSERVE Result:");
        Log.e(TAG, "\tSequenceNumber:" + sequenceNumber);

        try {
            setOcRepresentation(ocRepresentation);
            update_list();
        } catch (OcException e) {
            Log.e(TAG, e.toString());
            Log.e(TAG, "Failed to set representation");
        }
    }

    @Override
    public synchronized void onObserveFailed(Throwable throwable) {
        if (throwable instanceof OcException) {
            OcException ocEx = (OcException) throwable;
            Log.e(TAG, ocEx.toString());
            ErrorCode errCode = ocEx.getErrorCode();
            //do something based on errorCode
            Log.e(TAG, "Error code: " + errCode);
        }
        Log.e(TAG, "Observation of the found LED resource has failed");
    }
}
