package net.herit.business.device;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import net.herit.business.device.service.DeviceService;
import net.herit.business.device.service.ParameterVO;
import net.herit.common.conf.HeritProperties;
import net.herit.common.util.PagingUtil;
import net.herit.common.util.StringUtil;
import net.herit.security.dto.GroupAuthorization;


@Controller
@RequestMapping(value="/device")
public class DeviceController {

	@Resource(name = "DeviceService")
	private DeviceService deviceService;

	@RequestMapping(value="/test.do")
    public String test(@ModelAttribute("parameterVO") ParameterVO po,
			    		HttpServletRequest request,
			            Locale locale,
			            ModelMap model)
			throws Exception {
		
		
		
		return "/v2/device/test";
	}
	
    @RequestMapping(value="/list.do")
    public String deviceList(@ModelAttribute("parameterVO") ParameterVO po,
    		                   HttpServletRequest request,
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
		
		String deviceModel = po.getDeviceModel();
		String[] tokens = deviceModel.split("\\|");
		if (tokens.length == 2) {
			po.setOui(tokens[0]);
			po.setModelName(tokens[1]);
		}
		

		HashMap param = new HashMap<String, Object>();
		int page = StringUtil.parseInt(request.getParameter("page"), 1);
        PagingUtil resultPagingUtil = deviceService.getDeviceListPaging(page, 0, po);
        List deviceModelList = deviceService.getDeviceModelList(null);

		/**
		 * 데이터 셋팅
		 */
		model.addAttribute("page", page);
		model.addAttribute("param", po);
		model.addAttribute("deviceModelList", deviceModelList);
		model.addAttribute("resultPagingUtil", resultPagingUtil);

    	return "/v2/device/list";
    }



    @RequestMapping(value="/detail.do")
    public String deviceDetail(HttpServletRequest request,
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

		HashMap param = new HashMap<String, Object>();
		String deviceId = request.getParameter("deviceId");
		if (deviceId == null) {
			deviceId = (String)request.getSession().getAttribute("deviceId");
		} else {
			request.getSession().setAttribute("deviceId", deviceId);			
		}
		if (deviceId == null) {
			return "redirect:/device/list.do"; 
		}
        HashMap<String, Object> deviceInfoTotal = deviceService.getDeviceInfoTotal(deviceId);

        HashMap<String, String> deviceInfo = (HashMap<String, String>)deviceInfoTotal.get("deviceInfo");
        HashMap<String, String> deviceModelInfo = (HashMap<String, String>)deviceInfoTotal.get("deviceModelInfo");
        HashMap<String, Object> moMap = (HashMap<String, Object>) deviceInfoTotal.get("moMap");
        List<HashMap<String,Object>> profileList = (List<HashMap<String,Object>>)deviceInfoTotal.get("profileList");
        List<HashMap<String,Object>> basicProfileList = (List<HashMap<String,Object>>)deviceInfoTotal.get("basicProfileList");
        List<HashMap<String,Object>> serviceProfileList = (List<HashMap<String,Object>>)deviceInfoTotal.get("serviceProfileList");
        List<HashMap<String,Object>> networkProfileList = (List<HashMap<String,Object>>)deviceInfoTotal.get("networkProfileList");
		model.addAttribute("deviceInfo", deviceInfo);
		model.addAttribute("deviceModelInfo", deviceModelInfo);
		model.addAttribute("moMap", moMap);
		model.addAttribute("basicProfileList", basicProfileList);
		model.addAttribute("serviceProfileList", serviceProfileList);
		model.addAttribute("fileServerHost", HeritProperties.getProperty("Globals.fileServerHost"));
		model.addAttribute("fileServerPort", HeritProperties.getProperty("Globals.fileServerPort"));

		ObjectMapper objectMapper = new ObjectMapper();

		model.addAttribute("deviceInfoJson", objectMapper.writeValueAsString(deviceInfo));
		model.addAttribute("deviceModelInfoJson", objectMapper.writeValueAsString(deviceModelInfo));
		model.addAttribute("moMapJson", objectMapper.writeValueAsString(moMap));
		model.addAttribute("profileListJson", objectMapper.writeValueAsString(profileList));	
		model.addAttribute("basicProfileListJson", objectMapper.writeValueAsString(basicProfileList));	
		model.addAttribute("serviceProfileListJson", objectMapper.writeValueAsString(serviceProfileList));	
		model.addAttribute("networkProfileListJson", objectMapper.writeValueAsString(networkProfileList));		


    	return "/herit/business/device/detail";
    }



    @RequestMapping(value="/register.do")
    public String deviceRegister(HttpServletRequest request,
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
		
        List deviceModelList = deviceService.getDeviceModelList(null);

		/**
		 * 데이터 셋팅
		 */
		model.addAttribute("deviceModelList", deviceModelList);

    	return "/v2/device/register";
    }
    
    /**
     * oneM2M서버에서 검색 후 dm서버에 등록
     * @param request
     * @param locale
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/oneM2MRegister.do")
    public String oneM2MDeviceRegister(HttpServletRequest request, Locale locale, ModelMap model) throws Exception {
    	
    	HttpSession session = request.getSession(false);
    	if(session != null){
			//페이지 권한 확인
			GroupAuthorization requestAuth = (GroupAuthorization) session.getAttribute("requestAuth");
			if(!requestAuth.getAuthorizationDBRead().equals("1")){
				model.addAttribute("authMessage", "사용자관리 메뉴는 읽기 권한이 없습니다.");
				return "forward:" + HeritProperties.getProperty("Globals.MainPage");
			}
		}
    	
    	List deviceModelList = deviceService.getDeviceModelList(null);
    	
    	/**
    	 * 데이터 셋팅
    	 */
    	model.addAttribute("deviceModelList", deviceModelList);
    	
    	return "/v2/device/oneM2MRegister";
    }
    
    /**
     * Here
     * @param request
     * @param locale
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/registers.do")
    public String deviceRegisters(HttpServletRequest request, Locale locale, ModelMap model) throws Exception {

		HttpSession session = request.getSession(false);
		if(session != null){
			//페이지 권한 확인
			GroupAuthorization requestAuth = (GroupAuthorization) session.getAttribute("requestAuth");
			if(!requestAuth.getAuthorizationDBRead().equals("1")){
				model.addAttribute("authMessage", "사용자관리 메뉴는 읽기 권한이 없습니다.");
				return "forward:" + HeritProperties.getProperty("Globals.MainPage");
			}
		}
		
        List deviceModelList = deviceService.getDeviceModelList(null);

		/**
		 * 데이터 셋팅
		 */
		model.addAttribute("deviceModelList", deviceModelList);

    	return "/v2/device/registers";
    }

    @RequestMapping(value="/diagnosis.do")
    public String deviceDiagnosis(HttpServletRequest request,
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

		HashMap param = new HashMap<String, Object>();
		String deviceId = request.getParameter("deviceId");
        HashMap<String, Object> deviceInfoTotal = deviceService.getDeviceInfoTotal(deviceId);

        HashMap<String, String> deviceInfo = (HashMap<String, String>)deviceInfoTotal.get("deviceInfo");
        HashMap<String, Object> moMap = (HashMap<String, Object>) deviceInfoTotal.get("moMap");
        List<HashMap<String,Object>> profileList = (List<HashMap<String,Object>>)deviceInfoTotal.get("profileList");
        List<HashMap<String,Object>> basicProfileList = (List<HashMap<String,Object>>)deviceInfoTotal.get("basicProfileList");
        List<HashMap<String,Object>> serviceProfileList = (List<HashMap<String,Object>>)deviceInfoTotal.get("serviceProfileList");
        List<HashMap<String,Object>> networkProfileList = (List<HashMap<String,Object>>)deviceInfoTotal.get("networkProfileList");
		model.addAttribute("deviceInfo", deviceInfo);
		model.addAttribute("moMap", moMap);
		model.addAttribute("basicProfileList", basicProfileList);
		model.addAttribute("serviceProfileList", serviceProfileList);
		model.addAttribute("networkProfileList", networkProfileList);

		ObjectMapper objectMapper = new ObjectMapper();
		
		model.addAttribute("deviceInfoJson", objectMapper.writeValueAsString(deviceInfo));
		model.addAttribute("moMapJson", objectMapper.writeValueAsString(moMap));
		model.addAttribute("profileListJson", objectMapper.writeValueAsString(profileList));	
		model.addAttribute("basicProfileListJson", objectMapper.writeValueAsString(basicProfileList));	
		model.addAttribute("serviceProfileListJson", objectMapper.writeValueAsString(serviceProfileList));	
		model.addAttribute("networkProfileListJson", objectMapper.writeValueAsString(networkProfileList));		


    	return "/herit/business/device/diagnosis";
    }


}
