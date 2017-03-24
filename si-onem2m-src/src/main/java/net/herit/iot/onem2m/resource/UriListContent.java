package net.herit.iot.onem2m.resource;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;


@XmlRootElement(name = "uril")
public class UriListContent {
	
	public static final String SCHEMA_LOCATION = "CDT-responsePrimitive-v1_2_0.xsd";
	
    @XmlValue
    protected List<String> uriList;
    
	/**
	 * @return the uriList
	 */
	public List<String> getUriList() {
		if (uriList == null) {
			uriList = new ArrayList<String>();
		}
		return uriList;
	}
	
	public void addUriList(String uri) {
		if (uriList == null) {
			uriList = new ArrayList<String>();
		}
		if (uri != null) {
			this.uriList.add(uri);
		}
	}
	

	/**
	 * @param uriList the uriList to set
	 */
//	public void setUriList(List<String> uriList) {
//		this.uriList = uriList;
//	}
}
