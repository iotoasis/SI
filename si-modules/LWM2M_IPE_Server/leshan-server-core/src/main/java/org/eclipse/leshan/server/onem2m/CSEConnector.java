package org.eclipse.leshan.server.onem2m;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.eclipse.leshan.server.Constants;
import org.eclipse.leshan.server.Lwm2mServerConfig;
import org.eclipse.leshan.server.api.Util;
import org.eclipse.leshan.server.onem2m.resources.AE;
import org.eclipse.leshan.server.onem2m.resources.Container;
import org.eclipse.leshan.server.onem2m.resources.PollingChannel;
import org.eclipse.leshan.server.onem2m.resources.SemanticDescriptor;
import org.eclipse.leshan.server.onem2m.resources.Subscription;

public class CSEConnector {
	
	private HttpOneM2MOperation op;
	private Lwm2mServerConfig config = Lwm2mServerConfig.getInstance();
	
	// constructor
	public CSEConnector(){
		op = new HttpOneM2MOperation();
	}
	
	// singleton
	private static CSEConnector instance = null;
	public static CSEConnector getInstance(){
		if(instance == null){
			instance = new CSEConnector();
		}
		return instance;
	}
	
	// AE 존재 확인
	public synchronized boolean checkAEExist(){
		boolean result = false;
		try {
			System.out.println("Check AE exists.");
    		op.init(Constants.BASIC_AE_NAME);
    		JSONObject response = op.operate(new AE().makes(), Constants.AE, Constants.REQUEST_METHOD_TYPE.GET, null);
			
			if( response.isNull("RESPONSE-CODE") ){
				result = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			op.closeConnecton();
		}
		return result;
	}
	
	
	//*
	// repository 존재 확인
	public synchronized boolean hasSameObjects(){
		
		boolean result = false;
		try {
			System.out.println("Check current repository status.");
    		op.init(Constants.BASIC_AE_NAME,"?rcn=5");
    		JSONObject response = op.operate(new AE().makes(), Constants.AE, Constants.REQUEST_METHOD_TYPE.GET, null);
    		
    		if( !response.getJSONObject("ae").isNull("ch") ){
	    		JSONArray chs = response.getJSONObject("ae").getJSONArray("ch");
	    		int containerCount = 0; 
	    		for(int i=0; i<chs.length(); i++){
	    			if( chs.getJSONObject(i).getInt("typ") == 3 ){
	    				containerCount += 1;
	    			}
	    		}
	    		
	    		if( containerCount+3 != Constants.RESOURCES.length ){
	    			result = true;
	    		}
    		}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			op.closeConnecton();
		}
		return result;
	}//*/
	
	/*
	// repository 삭제
	public synchronized void removeRepository(){
		try {
    		op.init();
    		JSONObject response = op.operate(new AE().makes(), Constants.AE, Constants.REQUEST_METHOD_TYPE.DELETE, null);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			op.closeConnecton();
		}
	}
	*/
	
	// repository 생성
	public synchronized void createRepository(JSONObject basicToken){
		try {
			System.out.println("Create new repository.");
			
			// AE 생성
    		op.init("");
    		JSONObject resp_ae = op.operate(new AE().makes(), Constants.AE, Constants.REQUEST_METHOD_TYPE.POST, null);
    		
    		op.init(Constants.BASIC_AE_NAME);
    		JSONObject semantic = op.operate(new SemanticDescriptor().makes(), Constants.SEMANTIC_DESCRIPTOR, Constants.REQUEST_METHOD_TYPE.POST, null);
    		
    		// pollingChannel 생성
    		op.init(Constants.BASIC_AE_NAME);
    		PollingChannel pch_svc = new PollingChannel();
    		pch_svc.setRn("pch_svc");
    		JSONObject resp_pch_svc = op.operate(pch_svc.makes(), Constants.POLLING_CHANNEL, Constants.REQUEST_METHOD_TYPE.POST, null);
    		
    		op.init(Constants.BASIC_AE_NAME);
    		PollingChannel pch_srv = new PollingChannel();
    		pch_srv.setRn("pch_srv");
    		JSONObject resp_pch_srv = op.operate(pch_srv.makes(), Constants.POLLING_CHANNEL, Constants.REQUEST_METHOD_TYPE.POST, null);
    		
    		// container 및 subscription 생성
    		for(int i=0; i<Constants.RESOURCES.length; i++){
    			
    			// resource
    			String uriPath = "__"+Constants.RESOURCES[i][0].replace("/", "-");
    			//basicToken = basicToken.put("sn", basicToken.getString("sn").replace(":", "-"));
    			
	    		op.init(Constants.BASIC_AE_NAME);
	    		Container cnt = new Container();
	    		cnt.setRn(Constants.RESOURCES[i][1]+uriPath);
	    		cnt.addLbl(basicToken.toString());
	    		JSONObject resp_cnt = op.operate(cnt.makes(), Constants.CONTAINER, Constants.REQUEST_METHOD_TYPE.POST, null);
	    		
	    		// resource_write
	    		op.init(Constants.BASIC_AE_NAME, Constants.RESOURCES[i][1]+uriPath);
	    		cnt.setRn(Constants.ORDER_EXECUTE);
	    		JSONObject resp_cnt_w = op.operate(cnt.makes(), Constants.CONTAINER, Constants.REQUEST_METHOD_TYPE.POST, null);
	    		
	    		// resource_read
	    		op.init(Constants.BASIC_AE_NAME, Constants.RESOURCES[i][1]+uriPath);
	    		cnt.setRn(Constants.ORDER_RESULT);
	    		JSONObject resp_cnt_r = op.operate(cnt.makes(), Constants.CONTAINER, Constants.REQUEST_METHOD_TYPE.POST, null);
	    		
	    		
	    		// subscription 생성	    		
	    		op.init(Constants.BASIC_AE_NAME, Constants.RESOURCES[i][1]+uriPath, Constants.ORDER_EXECUTE);
	    		Subscription sub = new Subscription();
	    		sub.setRn("sub_"+Constants.RESOURCES[i][1]+"_w");
	    		sub.addNu(Util.makeUrl(config.getIncseAddress(), Constants.BASIC_AE_NAME, "pch_srv", "pcu"));
	    		JSONObject resp_sub_w = op.operate(sub.makes(), Constants.SUBSCRIPTION, Constants.REQUEST_METHOD_TYPE.POST, null);
	    		
	    		op.init(Constants.BASIC_AE_NAME, Constants.RESOURCES[i][1]+uriPath, Constants.ORDER_RESULT);
	    		sub.resetNu();
	    		sub.setRn("sub_"+Constants.RESOURCES[i][1]+"_r");
	    		sub.addNu(Util.makeUrl(config.getIncseAddress(), Constants.BASIC_AE_NAME, "pch_svc", "pcu"));
	    		JSONObject resp_sub_r = op.operate(sub.makes(), Constants.SUBSCRIPTION, Constants.REQUEST_METHOD_TYPE.POST, null);
    		}
    		
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			op.closeConnecton();
		}
	}
}
