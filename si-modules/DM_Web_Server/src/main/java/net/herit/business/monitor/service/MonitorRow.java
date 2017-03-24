package net.herit.business.monitor.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import net.herit.business.device.service.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

public class MonitorRow {

    protected static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);

    protected List<MonitorComponent> components = new ArrayList<MonitorComponent>();
    
    public void addComponent(MonitorComponent component) {
    	components.add(component);
    }
    
    public List<MonitorComponent> getComponents() {
    	return components;
    }
    
    public List<String> getComponentNames(String filter) {
    	List<String> names = new ArrayList<String>();

    	Iterator it = components.iterator();
    	while (it.hasNext()) {
    		MonitorComponent comp = (MonitorComponent)it.next();
    		if (comp.getCompName().startsWith(filter)) {
    			names.add(comp.getCompName());
    		}
    	}
    	
    	return names;
    }
    
    public Map<String, MonitorComponent> getComponentMap(String filter) {
    	Map<String, MonitorComponent> map = new HashMap<String, MonitorComponent>();

    	Iterator it = components.iterator();
    	while (it.hasNext()) {
    		MonitorComponent comp = (MonitorComponent)it.next();
    		if (comp.getCompName().startsWith(filter)) {
    			map.put(comp.getCompShortName(), comp);
    		}
    	}
    	
    	return map;
    }

	public void fillPathMapCustom(List<String> paths) {
		
		Iterator it = components.iterator();
		while (it.hasNext()) {
			MonitorComponent comp = (MonitorComponent)it.next();
			comp.fillPathMapCustom(paths);
		}		
	}

	public void fillPathMapPlugin(List<String> paths) {
		
		Iterator it = components.iterator();
		while (it.hasNext()) {
			MonitorComponent comp = (MonitorComponent)it.next();
			comp.fillPathMapPlugin(paths);
		}		
	}
}
