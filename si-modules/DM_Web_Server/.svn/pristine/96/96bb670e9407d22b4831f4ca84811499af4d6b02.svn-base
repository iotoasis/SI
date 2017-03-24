package net.herit.business.monitor.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import javax.annotation.Resource;

import net.herit.business.device.service.*;
import net.herit.common.exception.UserSysException;
import net.herit.business.device.service.impl.DeviceModelDAO;
import net.herit.business.device.service.impl.DeviceDAO;






import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service("MonitorService")
public class MonitorService {

	/** 클래스 명칭 */
	private final String CLASS_NAME = getClass().getName();
	/** 메소드명칭 */
	private String METHOD_NAME = "";
	
    protected static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);

	@Resource(name = "MonitorDAO")
	private MonitorDAO MonitorDAO;
	@Resource(name = "DeviceModelDAO")
	private DeviceModelDAO DeviceModelDAO;
	
	private MonitorLayout totalLayout = null;
	private Map<String, MonitorLayout> modelLayoutMap = new HashMap<String, MonitorLayout>(); 

	public MonitorLayout getTotalMonitorLayout(boolean reload)throws UserSysException {
		
		if (totalLayout == null || reload == true) {
			
			List<HashMap<String,Object>> result = MonitorDAO.getRowListForTotal();
			Iterator it = result.iterator();
			Map<Integer, MonitorRow> rowMap = new HashMap<Integer, MonitorRow>();
			int maxRowNum = 0;
			
			while (it.hasNext()) {
				Map<String, Object> comp = (Map<String,Object>)it.next();
				int rowNum = (Integer)comp.get("rowNum");
				MonitorComponent compObj = new MonitorComponent((String)comp.get("componentName"), 
																(String)comp.get("componentShortName"), 
																(Integer)comp.get("columnSize"), 
																(String)comp.get("jsFiles"), 
																(String)comp.get("title"),
																null, null);
				
				MonitorRow row = rowMap.get((Integer)comp.get("rowNum"));
				if (row == null) {
					row = new MonitorRow();
					rowMap.put(rowNum, row);
				}
				row.addComponent(compObj);
				
				if (maxRowNum < rowNum) {
					maxRowNum = rowNum;
				}
			}
			
			totalLayout = new MonitorLayout();
			
			for (int i=0; i<=maxRowNum; i++) {
				MonitorRow row = rowMap.get(i);
				if (row != null) {
					totalLayout.addRow(row);
				}
			}
		}
		
		return totalLayout;
    }

	public MonitorLayout getDeviceMonitorLayoutByModelInfo(String oui, String modelName, boolean reload) throws UserSysException {

		String modelKey = oui+"_"+modelName;
		MonitorLayout layout = modelLayoutMap.get(modelKey);
		if (layout == null || reload == true) {
			
			long modelId = DeviceModelDAO.getDeviceModelId(oui, modelName);
			
			List<HashMap<String,Object>> result = MonitorDAO.getRowListForModel(modelId);
			Iterator it = result.iterator();
			Map<Integer, MonitorRow> rowMap = new HashMap<Integer, MonitorRow>();
			int maxRowNum = 0;
			
			while (it.hasNext()) {
				Map<String, Object> comp = (Map<String,Object>)it.next();

				List<HashMap<String,Object>> resourceList = null;
		    	String resourceUris = (String)comp.get("resourceUris");		
		    	String parameters = (String) comp.get("parameters");
		    	
		    	if (resourceUris != null) {
		    		resourceList = new ArrayList<HashMap<String,Object>>();
		    		
		        	String[] uris = resourceUris.split(";");
		        	for (int i=0; i<uris.length && uris[i].length() > 0; i++) {    		
		        		//resources.add(uris[i]);
		        		List<HashMap<String,Object>> profiles = DeviceModelDAO.getDeviceModelProfileList(oui, modelName, uris[i]);
		        		
		        		if (profiles.size() > 0) {
			        		HashMap<String,Object> profile = profiles.get(0);
			        		
			        		resourceList.add(profile);
		        		}
		        	}
		    	}
		    	
				MonitorComponent compObj = new MonitorComponent((String)comp.get("componentName"),
																(String)comp.get("componentShortName"),
																(Integer)comp.get("columnSize"),
																(String)comp.get("jsFiles"),
																(String)comp.get("title"),
																resourceUris, parameters);

				int rowNum = (Integer)comp.get("rowNum");
				
				MonitorRow row = rowMap.get(rowNum);
				if (row == null) {
					row = new MonitorRow();
					rowMap.put(rowNum, row);
				}
				row.addComponent(compObj);
				
				if (maxRowNum < rowNum) {
					maxRowNum = rowNum;
				}
			}
			
			layout = new MonitorLayout();
			
			for (int i=0; i<=maxRowNum; i++) {
				MonitorRow row = rowMap.get(i);
				if (row != null) {
					layout.addRow(row);
				}
			}
			
			// layout map에 입력			
			modelLayoutMap.put(modelKey, layout);
		}
		
    	return layout;
	}

}
