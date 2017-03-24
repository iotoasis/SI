package net.herit.business.history;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import net.herit.business.device.service.DeviceService;
import net.herit.business.history.service.HistoryService;
import net.herit.common.conf.HeritProperties;
import net.herit.security.dto.GroupAuthorization;


@Controller
@RequestMapping(value="/history")
public class HistoryController {

	@Resource(name = "DeviceService")
	private DeviceService deviceService;
	@Resource(name = "HistoryService")
	private HistoryService historyService;

    

    @RequestMapping(value="/error.do")
    public String errorList(HttpServletRequest request,
    		                   Locale locale,
    		                   ModelMap model)
            throws Exception {

		HttpSession session = request.getSession(false);
		if(session != null){
			//페이지 권한 확인
			GroupAuthorization requestAuth = (GroupAuthorization) session.getAttribute("requestAuth");
			if(!requestAuth.getAuthorizationDBRead().equals("1")){
				model.addAttribute("authMessage", "사용자관리 메뉴는 읽기 권한이 없습니다.");
				return "forward:" + HeritProperties.getProperty("Globals.MainPage");
			}
		}
		
		/*
		String deviceModel = po.getDeviceModel();
		String[] tokens = deviceModel.split("\\|");
		if (tokens.length == 2) {
			po.setOui(tokens[0]);
			po.setModelName(tokens[1]);
		}
		*/
		
		Map<String, String[]> paramMap = request.getParameterMap();
        List deviceModelList = deviceService.getDeviceModelList(null);
        

		/**
		 * 데이터 셋팅
		 */
		model.addAttribute("param", paramMap);
		model.addAttribute("deviceModelList", deviceModelList);
		
		ObjectMapper objectMapper = new ObjectMapper();

		model.addAttribute("paramJson", objectMapper.writeValueAsString(paramMap));
		model.addAttribute("deviceModelListJson", objectMapper.writeValueAsString(deviceModelList));
		
    	return "/v2/history/error";
    }


    @RequestMapping(value="/control.do")
    public String controlList(HttpServletRequest request,
    		                   Locale locale,
    		                   ModelMap model)
            throws Exception {

		HttpSession session = request.getSession(false);
		if(session != null){
			//페이지 권한 확인
			GroupAuthorization requestAuth = (GroupAuthorization) session.getAttribute("requestAuth");
			if(!requestAuth.getAuthorizationDBRead().equals("1")){
				model.addAttribute("authMessage", "사용자관리 메뉴는 읽기 권한이 없습니다.");
				return "forward:" + HeritProperties.getProperty("Globals.MainPage");
			}
		}
		
		/*
		String deviceModel = po.getDeviceModel();
		String[] tokens = deviceModel.split("\\|");
		if (tokens.length == 2) {
			po.setOui(tokens[0]);
			po.setModelName(tokens[1]);
		}
		*/
		
		Map<String, String[]> paramMap = request.getParameterMap();
        List deviceModelList = deviceService.getDeviceModelList(null);
        

		/**
		 * 데이터 셋팅
		 */
		model.addAttribute("param", paramMap);
		model.addAttribute("deviceModelList", deviceModelList);
		
		ObjectMapper objectMapper = new ObjectMapper();

		model.addAttribute("paramJson", objectMapper.writeValueAsString(paramMap));
		model.addAttribute("deviceModelListJson", objectMapper.writeValueAsString(deviceModelList));
		
    	return "/v2/history/control";
    }

    @RequestMapping(value="/firmware.do")
    public String firmwareList(HttpServletRequest request,
    		                   Locale locale,
    		                   ModelMap model)
            throws Exception {

		HttpSession session = request.getSession(false);
		if(session != null){
			//페이지 권한 확인
			GroupAuthorization requestAuth = (GroupAuthorization) session.getAttribute("requestAuth");
			if(!requestAuth.getAuthorizationDBRead().equals("1")){
				model.addAttribute("authMessage", "사용자관리 메뉴는 읽기 권한이 없습니다.");
				return "forward:" + HeritProperties.getProperty("Globals.MainPage");
			}
		}
		
		/*
		String deviceModel = po.getDeviceModel();
		String[] tokens = deviceModel.split("\\|");
		if (tokens.length == 2) {
			po.setOui(tokens[0]);
			po.setModelName(tokens[1]);
		}
		*/
		
		Map<String, String[]> paramMap = request.getParameterMap();
        List deviceModelList = deviceService.getDeviceModelList(null);
        

		/**
		 * 데이터 셋팅
		 */
		model.addAttribute("param", paramMap);
		model.addAttribute("deviceModelList", deviceModelList);
		
		ObjectMapper objectMapper = new ObjectMapper();

		model.addAttribute("paramJson", objectMapper.writeValueAsString(paramMap));
		model.addAttribute("deviceModelListJson", objectMapper.writeValueAsString(deviceModelList));
		
    	return "/v2/history/firmware";
    }

    @RequestMapping(value="/status.do")
    public String statusList(HttpServletRequest request,
    		                   Locale locale,
    		                   ModelMap model)
            throws Exception {

		HttpSession session = request.getSession(false);
		if(session != null){
			//페이지 권한 확인
			GroupAuthorization requestAuth = (GroupAuthorization) session.getAttribute("requestAuth");
			if(!requestAuth.getAuthorizationDBRead().equals("1")){
				model.addAttribute("authMessage", "사용자관리 메뉴는 읽기 권한이 없습니다.");
				return "forward:" + HeritProperties.getProperty("Globals.MainPage");
			}
		}
		
		/*
		String deviceModel = po.getDeviceModel();
		String[] tokens = deviceModel.split("\\|");
		if (tokens.length == 2) {
			po.setOui(tokens[0]);
			po.setModelName(tokens[1]);
		}
		*/
		
		Map<String, String[]> paramMap = request.getParameterMap();
        List deviceModelList = deviceService.getDeviceModelList(null);
        

		/**
		 * 데이터 셋팅
		 */
		model.addAttribute("param", paramMap);
		model.addAttribute("deviceModelList", deviceModelList);
		
		ObjectMapper objectMapper = new ObjectMapper();

		model.addAttribute("paramJson", objectMapper.writeValueAsString(paramMap));
		model.addAttribute("deviceModelListJson", objectMapper.writeValueAsString(deviceModelList));
		
    	return "/v2/history/status";
    }

}
