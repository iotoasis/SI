package org.eclipse.leshan.server.extension.onem2m.handler;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.eclipse.leshan.server.Lwm2mServerConfig;
import org.eclipse.leshan.server.client.Client;
import org.eclipse.leshan.server.extension.Constants;
import org.eclipse.leshan.server.extension.Tokenization;
import org.eclipse.leshan.server.extension.Util;
import org.eclipse.leshan.server.extension.onem2m.HttpOneM2MOperation;
import org.eclipse.leshan.server.extension.onem2m.resources.AE;
import org.eclipse.leshan.server.extension.onem2m.resources.Container;
import org.eclipse.leshan.server.extension.onem2m.resources.ContentInstance;
import org.eclipse.leshan.server.extension.onem2m.resources.PollingChannel;
import org.eclipse.leshan.server.extension.onem2m.resources.SemanticDescriptor;
import org.eclipse.leshan.server.extension.onem2m.resources.Subscription;

public class IncseOperator {
	
	private HttpOneM2MOperation op;
	private Lwm2mServerConfig config = Lwm2mServerConfig.getInstance();
	
	// constructor
	public IncseOperator(){
		op = new HttpOneM2MOperation();
	}
	
	// AE 존재 확인
	public synchronized boolean AeExist(){
		boolean result = false;
		try {
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
	    		
	    		if( containerCount+3 != Constants.RESOURCE.length ){
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
	
	
	// repository 생성
	public synchronized void createRepository(String authId){
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
    		for(int i=0; i<Constants.RESOURCE.length; i++){
    			
	    		op.init(Constants.BASIC_AE_NAME);
	    		Container cnt = new Container();
	    		cnt.setRn(Constants.RESOURCE[i][1]);
	    		//cnt.addLbl(Tokenization.getDeviceInfoEncoded(authId));
	    		JSONObject resp_cnt = op.operate(cnt.makes(), Constants.CONTAINER, Constants.REQUEST_METHOD_TYPE.POST, null);
	    		
	    		// resource_write 
	    		if(Constants.RESOURCE[i][0].equals("control")){
		    		op.init(Constants.BASIC_AE_NAME, Constants.RESOURCE[i][1]);
		    		cnt.setRn(Constants.CONTAINER_NAME_CONTROL);
		    		JSONObject resp_cnt_w = op.operate(cnt.makes(), Constants.CONTAINER, Constants.REQUEST_METHOD_TYPE.POST, null);
	    		}
	    		// resource_read
	    		if(Constants.RESOURCE[i][0].equals("control")){
		    		op.init(Constants.BASIC_AE_NAME, Constants.RESOURCE[i][1]);
		    		cnt.setRn(Constants.CONTAINER_NAME_RESULT);
		    		JSONObject resp_cnt_r = op.operate(cnt.makes(), Constants.CONTAINER, Constants.REQUEST_METHOD_TYPE.POST, null);
	    		} else if(Constants.RESOURCE[i][0].equals("report")) {
	    			op.init(Constants.BASIC_AE_NAME, Constants.RESOURCE[i][1]);
		    		cnt.setRn(Constants.CONTAINER_NAME_STATUS);
		    		JSONObject resp_cnt_r = op.operate(cnt.makes(), Constants.CONTAINER, Constants.REQUEST_METHOD_TYPE.POST, null);
	    		}
	    		
	    		// subscription 생성
	    		Subscription sub = new Subscription();
	    		if(Constants.RESOURCE[i][0].equals("control")){
		    		op.init(Constants.BASIC_AE_NAME, Constants.RESOURCE[i][1], Constants.CONTAINER_NAME_CONTROL);
		    		sub.setRn("sub_"+Constants.RESOURCE[i][1]+"_execute");
		    		sub.addNu(Util.makeUrl(config.getSiUri(), Constants.BASIC_AE_NAME, "pch_srv", "pcu"));
		    		JSONObject resp_sub_w = op.operate(sub.makes(), Constants.SUBSCRIPTION, Constants.REQUEST_METHOD_TYPE.POST, null);
	    		}
	    		if(Constants.RESOURCE[i][0].equals("control")){
		    		op.init(Constants.BASIC_AE_NAME, Constants.RESOURCE[i][1], Constants.CONTAINER_NAME_RESULT);
		    		sub.resetNu();
		    		sub.setRn("sub_"+Constants.RESOURCE[i][1]+"_"+Constants.CONTAINER_NAME_RESULT);
		    		sub.addNu(Util.makeUrl(config.getSiUri(), Constants.BASIC_AE_NAME, "pch_svc", "pcu"));
		    		JSONObject resp_sub_r = op.operate(sub.makes(), Constants.SUBSCRIPTION, Constants.REQUEST_METHOD_TYPE.POST, null);
	    		} else if(Constants.RESOURCE[i][0].equals("report")) {
	    			op.init(Constants.BASIC_AE_NAME, Constants.RESOURCE[i][1], Constants.CONTAINER_NAME_STATUS);
		    		sub.resetNu();
		    		sub.setRn("sub_"+Constants.RESOURCE[i][1]+"_"+Constants.CONTAINER_NAME_STATUS);
		    		sub.addNu(Util.makeUrl(config.getSiUri(), Constants.BASIC_AE_NAME, "pch_svc", "pcu"));
		    		JSONObject resp_sub_r = op.operate(sub.makes(), Constants.SUBSCRIPTION, Constants.REQUEST_METHOD_TYPE.POST, null);
	    		}
    		}
    		
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			op.closeConnecton();
		}
	}
	
	// repository 생성
	public synchronized void createContentInstance(JSONObject item){
		try {
			String resourceName = Constants.getResourceByUri(item.getString("resourceUri"));
			String repo = Constants.CONTAINER_NAME_RESULT;
			
			op.init(Constants.BASIC_AE_NAME, resourceName, repo);
			ContentInstance cin = new ContentInstance();
			cin.setCon(item.getString("sv"));
    		JSONObject resp_cin = op.operate(cin.makes(), Constants.CONTENTINSTANCE, Constants.REQUEST_METHOD_TYPE.POST, null);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			op.closeConnecton();
		}
	}
}
