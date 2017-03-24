package net.herit.business.monitor;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.herit.common.conf.HeritProperties;
import net.herit.security.dto.GroupAuthorization;
import net.herit.business.monitor.service.*;
import net.herit.business.device.service.*;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(value="/v2/monitor/")
public class MonitorController {

	@Resource(name = "MonitorService")
	private MonitorService monitorService;

	@Resource(name = "DeviceService")
	private DeviceService deviceService;

    @RequestMapping(value="/_reload_layout_configuration.do")
    public String reloadLayoutConfiguration(HttpServletRequest request,
    		                   Locale locale,
    		                   ModelMap model)
            throws Exception {

		HttpSession session = request.getSession(false);
		if(session != null){
			//페이지 권한 확인
			GroupAuthorization requestAuth = (GroupAuthorization) session.getAttribute("requestAuth");
			if(requestAuth == null || !requestAuth.getAuthorizationDBRead().equals("1")){
				model.addAttribute("authMessage", "모니터 페이지 접근 권한이 없습니다.");
				return "forward:" + HeritProperties.getProperty("Globals.MainPage");
			}
		}

		MonitorLayout layout = monitorService.getTotalMonitorLayout(true);
		
		model.addAttribute("rows", layout.getRows());
		model.addAttribute("customeJs", layout.getJavascriptFilesCustom());
		model.addAttribute("pluginJs", layout.getJavascriptFilesPlugin());

    	return "/v2/monitor/index";
    }
    
    @RequestMapping(value="/index.do")
    public String totalMonitor(HttpServletRequest request,
    		                   Locale locale,
    		                   ModelMap model)
            throws Exception {

		HttpSession session = request.getSession(false);
		if(session != null){
			//페이지 권한 확인
			GroupAuthorization requestAuth = (GroupAuthorization) session.getAttribute("requestAuth");
			if(requestAuth == null || !requestAuth.getAuthorizationDBRead().equals("1")){
				model.addAttribute("authMessage", "모니터 페이지 접근 권한이 없습니다.");
				return "forward:" + HeritProperties.getProperty("Globals.MainPage");
			}
		}
		
		String reload = request.getParameter("reloadConf");
				
		MonitorLayout layout = monitorService.getTotalMonitorLayout(reload != null ? reload.equalsIgnoreCase("true") : false);

		model.addAttribute("rows", layout.getRows());
		model.addAttribute("customeJs", layout.getJavascriptFilesCustom());
		model.addAttribute("pluginJs", layout.getJavascriptFilesPlugin());

    	return "/v2/monitor/index";
    }


	
    @RequestMapping(value="/device.do")
    public String deviceMonitor(HttpServletRequest request,
    		                   Locale locale,
    		                   ModelMap model)
            throws Exception {

		HttpSession session = request.getSession(false);
		if(session != null){
			//페이지 권한 확인
			GroupAuthorization requestAuth = (GroupAuthorization) session.getAttribute("requestAuth");
			if(!requestAuth.getAuthorizationDBRead().equals("1")){
				model.addAttribute("authMessage", "모니터 페이지 접근 권한이 없습니다.");
				return "forward:" + HeritProperties.getProperty("Globals.MainPage");
			}
		}
		
		String oui = request.getParameter("oui");
		String modelName = request.getParameter("mn");
		String sn = request.getParameter("sn");
		String deviceId = request.getParameter("deviceId");
		String reload = request.getParameter("reloadConf");

		if (deviceId == null) {
			return "redirect:/device/list.do"; 
		}
        HashMap<String, Object> deviceInfoTotal = deviceService.getDeviceInfoTotal(deviceId);
        
        HashMap<String, String> deviceInfo = (HashMap<String, String>)deviceInfoTotal.get("deviceInfo");
        HashMap<String, String> deviceModelInfo = (HashMap<String, String>)deviceInfoTotal.get("deviceModelInfo");
        HashMap<String, Object> moMap = (HashMap<String, Object>) deviceInfoTotal.get("moMap");
        List<HashMap<String,Object>> profileList = (List<HashMap<String,Object>>)deviceInfoTotal.get("profileList");
        //List<HashMap<String, Object>> deviceStatusHistList = (List<HashMap<String, Object>>)deviceInfoTotal.get("deviceStatusHistList");
        
		oui = deviceInfo.get("oui");
		modelName = deviceInfo.get("modelName");
		sn = deviceInfo.get("sn");
		
		MonitorLayout layout;
		layout = monitorService.getDeviceMonitorLayoutByModelInfo(oui, modelName, reload != null ? reload.equalsIgnoreCase("true") : false);
		
		try{
			System.err.println(deviceInfo.toString());
			System.out.println(deviceModelInfo.toString());
			System.out.println(moMap.toString());
			System.out.println(profileList.toString());
			
		}catch(Exception e){
			
		}
		
		model.addAttribute("rows", layout.getRows());
		model.addAttribute("customeJs", layout.getJavascriptFilesCustom());
		model.addAttribute("pluginJs", layout.getJavascriptFilesPlugin());
		
		ObjectMapper objectMapper = new ObjectMapper();
		model.addAttribute("dmType", deviceInfo.get("dmType"));
		model.addAttribute("deviceInfoJson", objectMapper.writeValueAsString(deviceInfo));
		model.addAttribute("deviceModelInfoJson", objectMapper.writeValueAsString(deviceModelInfo));
		model.addAttribute("moMapJson", objectMapper.writeValueAsString(moMap));
		model.addAttribute("profileListJson", objectMapper.writeValueAsString(profileList));
		//model.addAttribute("deviceStatusHistListJson", objectMapper.writeValueAsString(deviceStatusHistList));
		model.addAttribute("statCompMapJson", objectMapper.writeValueAsString(layout.getComponentMap("device.status.")));	
		
    	return "/v2/monitor/device";
    }

}
