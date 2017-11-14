package net.herit.business.monitor.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.herit.business.device.service.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

public class MonitorLayout {

    protected static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);
    
    protected List<MonitorRow> rows = new ArrayList<MonitorRow>();
    
    public void addRow(MonitorRow row) {
    	rows.add(row);
    }
    
    public List<MonitorRow> getRows() {
    	return rows;
    }
    
    public List<String> getComponentNames(String filter) {
    	List<String> names = new ArrayList<String>();

    	Iterator it = rows.iterator();
    	while (it.hasNext()) {
    		MonitorRow row = (MonitorRow)it.next();
    		names.addAll(row.getComponentNames(filter));
    	}
    	
    	return names;
    }
    
    public Map<String, MonitorComponent> getComponentMap(String filter) {
    	
    	Map<String, MonitorComponent> map = new HashMap<String, MonitorComponent>();

    	Iterator it = rows.iterator();
    	while (it.hasNext()) {
    		MonitorRow row = (MonitorRow)it.next();
    		
    		map.putAll(row.getComponentMap(filter));
    	}
    	
    	return map;
    }
    
    public List<String> getJavascriptFilesCustom() {
    	//Map<String, String> paths = new HashMap<String, String>();
    	List<String> paths = new ArrayList<String>();

    	Iterator it = rows.iterator();
    	while (it.hasNext()) {
    		MonitorRow row = (MonitorRow)it.next();
    		row.fillPathMapCustom(paths);
    	}
    	
    	// 중복 제거    	
    	HashMap<String, String> jsMap = new HashMap<String, String>();
    	List<String> result = new ArrayList<String>();
    	Iterator itj = paths.iterator();
    	while (itj.hasNext()) {
    		String js = (String)itj.next();
    		if (jsMap.get(js) == null) {
    			jsMap.put(js,  js);
    			result.add(js);
    		}
    	}
    	
    	return result;
    }
    
    public List<String> getJavascriptFilesPlugin() {
    	List<String> paths = new ArrayList<String>();

    	Iterator it = rows.iterator();
    	while (it.hasNext()) {
    		MonitorRow row = (MonitorRow)it.next();
    		row.fillPathMapPlugin(paths);
    	}
    	
    	// 중복 제거    	
    	HashMap<String, String> jsMap = new HashMap<String, String>();
    	List<String> result = new ArrayList<String>();
    	Iterator itj = paths.iterator();
    	while (itj.hasNext()) {
    		String js = (String)itj.next();
    		if (jsMap.get(js) == null) {
    			jsMap.put(js,  js);
    			result.add(js);
    		}
    	}
    	
    	return result;
    }
}
