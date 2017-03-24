package net.herit.iot.onem2m.resource;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
	    "filterList",
	    "resourceUris",
	    "paramUri",
	    "paramDescriptor",
	    "paramContentType"
})
public class RestSemanticCI {

	@XmlElement(name = "filter_list")
	private List<String> filterList;
	
	@XmlElement(name = "uri_list")
	private List<String> resourceUris;
	
	@XmlElement(name = "par_uri")
	private String paramUri;
	
	@XmlElement(name = "par_desc")
	private String paramDescriptor;
	
	@XmlElement(name = "par_contype")
	private String paramContentType;

	public String getParamUri() {
		return paramUri;
	}

	public void setParamUri(String paramUri) {
		this.paramUri = paramUri;
	}

	public String getParamDescriptor() {
		return paramDescriptor;
	}

	public void setParamDescriptor(String paramDescriptor) {
		this.paramDescriptor = paramDescriptor;
	}

	public String getParamContentType() {
		return paramContentType;
	}

	public void setParamContentType(String paramContentType) {
		this.paramContentType = paramContentType;
	}

	public List<String> getFilterList() {
		return filterList;
	}

	public void setFilterList(List<String> filterList) {
		this.filterList = filterList;
	}

	public List<String> getResourceUris() {
		return resourceUris;
	}

	public void setResourceUris(List<String> resourceUris) {
		this.resourceUris = resourceUris;
	}
	
	public void addSparql(String sparql) {
		filterList.add(sparql);
	}
	
	
}
