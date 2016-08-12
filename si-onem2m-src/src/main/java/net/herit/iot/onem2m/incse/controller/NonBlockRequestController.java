package net.herit.iot.onem2m.incse.controller;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESPONSE_TYPE;
import net.herit.iot.message.onem2m.format.Enums.*;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.facility.CfgManager;
import net.herit.iot.onem2m.incse.facility.OneM2mUtil;
import net.herit.iot.onem2m.incse.manager.ManagerInterface;
import net.herit.iot.onem2m.incse.manager.RequestManager;
import net.herit.iot.onem2m.resource.OperationResult;
import net.herit.iot.onem2m.resource.Request;
import net.herit.iot.onem2m.resource.Resource;
import net.herit.iot.onem2m.resource.ResponsePrimitive;

public class NonBlockRequestController implements Runnable {

	private OneM2mContext context;
	private RequestManager reqManager;
	private Request reqResource;
	private ManagerInterface resManager;
	private Resource tgtResource;
	private OneM2mRequest request;

	private Logger log = LoggerFactory.getLogger(NonBlockRequestController.class);
	
	public NonBlockRequestController(OneM2mContext context, RequestManager reqManager, Request reqResource,
								ManagerInterface resManager, OneM2mRequest reqMessage) {
		this.context = context;
		this.reqManager = reqManager;
		this.resManager = resManager;
		this.request = reqMessage;
		this.reqResource = reqResource;
		//this.tgtResource = tgtResource;
		
	}

	@Override
	public void run() {

		try {
			OneM2mResponse response = null;
			switch (request.getOperationEnum()) {
			case CREATE:
				response = resManager.create(request);
				break;
			case RETRIEVE:
				response = resManager.retrieve(request);
				break;
			case UPDATE:
				response = resManager.update(request);
				break;
			case DELETE:
				response = resManager.delete(request);
				break;
			case NOTIFY:
				response = resManager.notify(request);
				break;
			default:
				break;
			}
			
			OperationResult or = new OperationResult(response);
			
			reqResource.setRequestStatus(response.getResponseStatusCodeEnum().Value() < 3000 ? 
										REQUEST_STATUS.COMPLETED.Value() :
										REQUEST_STATUS.FAILED.Value());
			reqResource.setOperationResult(or);
			reqManager.getDAO().update(reqResource);
			
			if (request.getResponseTypeEnum() == RESPONSE_TYPE.NBLOCK_REQ_ASYNC) {
				/* TBD - Not tested */ 
				
				// create response content
//				ResponsePrimitive resPrim = new ResponsePrimitive(response);
				ResponsePrimitive resPrim = (ResponsePrimitive)(response);

				// create notification request
				OneM2mRequest notiReq = new OneM2mRequest();
				notiReq.setContentObject(resPrim);
				notiReq.setOperation(OPERATION.NOTIFY);
				notiReq.setContentType(CONTENT_TYPE.RES_XML);	// check ae capa(accept) - TBD
				notiReq.setFrom(CfgManager.getInstance().getCSEBaseName());	// get cse id - from configuration - TBD
				notiReq.setRequestIdentifier(OneM2mUtil.createRequestId());	// create request id

				// send notification in case Non-block async request
				OneM2mResponse notiResp;
				List<String> uriList = request.getNotificationUri();
				if (uriList != null && uriList.size() > 0) {
					Iterator<String> it = uriList.iterator();
					while (it.hasNext()) {
						try {
							notiResp = this.context.getNseManager().sendRequestMessage(it.next(), notiReq);

							if (notiResp != null) {
								break;
							}
						} catch (Exception e) {
							continue;
						}						
					}					
				}				
			}
			
			
		} catch (OneM2MException e) {
			log.error("Handled exception", e);
			
			try {
				OneM2mResponse response = new OneM2mResponse(e.getResponseStatusCode(), request);
				response.setContent(new String(e.getMessage()).getBytes());
				OperationResult or = new OperationResult(response);
				
				reqResource.setRequestStatus(response.getResponseStatusCodeEnum().Value() < 3000 ? 
											REQUEST_STATUS.COMPLETED.Value() :
											REQUEST_STATUS.FAILED.Value());
				reqResource.setOperationResult(or);
				reqManager.getDAO().update(reqResource);

			} catch (OneM2MException e1) {
				log.error("Handled exception", e);
			}
			
			if (request.getResponseTypeEnum() == RESPONSE_TYPE.NBLOCK_REQ_ASYNC) {
				// send notification in case Non-block async request
			
			}
		}
		
	}

}
