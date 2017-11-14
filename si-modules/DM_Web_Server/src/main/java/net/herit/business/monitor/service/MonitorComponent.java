package net.herit.business.monitor.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.herit.business.device.service.*;
import net.herit.common.conf.HeritProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MonitorComponent{

    protected static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);

    protected int colSize = 1;

    protected String compName = "";
    protected String compShortName = "";
    protected String title = "";
    protected String parameterList = "";
    
    protected List<String> jsFiles = new ArrayList<String>();
    protected List<String> resources = new ArrayList<String>();
    //protected List<String> parameterList = new ArrayList<String>();
    //protected List<HashMap<String,Object>> resourceList = new ArrayList<HashMap<String,Object>>();
    
    public MonitorComponent(String name, String shortName, int size, String files, 
    						String title, String resourceUris, String parameters) {
    	compName = name;
    	compShortName = shortName;
    	colSize = size;
    	parameterList = parameters;
    	
    	if (files != null) {
        	String[] fileArr = files.split(";");
        	for (int i=0; i<fileArr.length && fileArr[i].length() > 0; i++) {    		
        		jsFiles.add(fileArr[i]);
        	}
    	}
    	
    	if (resourceUris != null) {
        	String[] uris = resourceUris.split(";");
        	for (int i=0; i<uris.length && uris[i].length() > 0; i++) {    		
        		resources.add(uris[i]);
        	}
    	}
    	
    	/*if (parameters != null) {
        	String[] params = parameters.split(";");
        	for (int i=0; i<params.length && params[i].length() > 0; i++) {    		
        		parameterList.add(params[i]);
        	}
    	}*/
    	
    	this.title = title;
    }

	public void fillPathMapPlugin(List<String> paths) {
		String jsRootPath = HeritProperties.getProperty("Globals.v2JSRootPath");
		
		Iterator it = jsFiles.iterator();
		while (it.hasNext()) {
			String path = (String)it.next();
			if (path.startsWith("http:") || path.startsWith("https:")) {
				paths.add(path);				
			} else {
				paths.add(jsRootPath +path);				
			}
		}
	}

	public void fillPathMapCustom(List<String> paths) {
		String jsRootPath = HeritProperties.getProperty("Globals.v2JSRootPath");
		paths.add(jsRootPath +"herit/component/" + compName +".js");
	}
    
    public int getColSize() {
    	return colSize;
    }
    
    public void setColSize(int size) {
    	colSize = size;
    }
    
    public String getCompName() {
    	return compName;
    }
    
    public String getCompShortName() {
    	return compShortName;
    }
    
    public String getTitle() {
    	return title;
    }

    public void setCompName(String name) {
    	compName = name;
    }

    public void setCompShortName(String name) {
    	compShortName = name;
    }
    
    public void setTitle(String title) {
    	this.title = title;
    }
    
    public List<String> getJsFiles() {
    	return jsFiles;
    }
    
    public List<String> getResources() {
    	return resources;
    }
    
    public void setJsFiles(List<String> files) {
    	jsFiles = files;
    }

	public void setResources(List<String> uris) {
    	resources = uris;
    }

	public String getParameterList() {
		return parameterList;
	}

	public void setParameterList(String parameterList) {
		this.parameterList = parameterList;
	}
	
}
