package net.herit.iot.onem2m.incse.controller;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.onem2m.incse.context.OneM2mContext;

public abstract class AbsController {
	protected OneM2mContext context;
	
	protected AbsController(OneM2mContext context) {
		super();
		this.context = context;
	}
	
	protected AbsController() {	}
	
	protected void setContext(OneM2mContext context) {
		this.context = context;
	}
	
	protected OneM2mContext getContext() {
		return this.context;
	}
	
	public abstract OneM2mResponse processRequest(OneM2mRequest reqMessage);
	
	public abstract boolean processAsyncRequest(OneM2mRequest reqMessage);
	
}
