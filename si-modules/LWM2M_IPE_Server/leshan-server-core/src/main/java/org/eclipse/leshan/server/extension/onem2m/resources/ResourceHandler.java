package org.eclipse.leshan.server.extension.onem2m.resources;

public class ResourceHandler{
	
	// singleton
	private static ResourceHandler rh;
	public static ResourceHandler getInstance(){
		if( rh == null ){
			rh = new ResourceHandler();
		}
		return rh;
	}
	
	// getBasicResourceWithSet
	public Resource getBasicResourceWithSet(int resourceType){
		Resource result = null;
		switch (resourceType) {
		
		// mixed
		case 0:
			
			break;
			
		// accessControlPolicy
		case 1:
			
			break;
			
		// AE
		case 2:
			AE r = new AE();
			result = r;
			break;
			
		// container
		case 3:
			
			break;
			
		// contentInstance
		case 4:
			
			break;
			
		// CSEBase
		case 5:
			
			break;
			
		// group
		case 9:
			
			break;
			
		// pollingChannel
		case 15:
			
			break;
			
		// subscription
		case 23:
			
			break;
			
		default:
			break;
		}
		
		return result;
	}
}
