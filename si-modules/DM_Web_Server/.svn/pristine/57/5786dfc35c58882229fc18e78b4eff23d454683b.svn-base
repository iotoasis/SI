package net.herit.business.firmware;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import net.herit.business.firmware.service.FirmwareService;
import net.herit.common.conf.HeritProperties;
import net.herit.common.util.PagingUtil;
import net.herit.common.util.StringUtil;
import net.herit.security.dto.GroupAuthorization;


@Controller
@RequestMapping(value="/firmware")
public class FirmwareController {

	@Resource(name = "DeviceService")
	private DeviceService deviceService;
	@Resource(name = "FirmwareService")
	private FirmwareService firmwareService;

    @RequestMapping(value="/list.do")
    public String firmwareList(@ModelAttribute("parameterVO") ParameterVO po,
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
		
/*		String deviceModel = po.getDeviceModel();
		String[] tokens = deviceModel.split("\\|");
		if (tokens.length == 2) {
			po.setOui(tokens[0]);
			po.setModelName(tokens[1]);
		}
*/		

		HashMap param = new HashMap<String, Object>();
		int page = StringUtil.parseInt(request.getParameter("page"), 1);
        PagingUtil resultPagingUtil = firmwareService.getFirmwareListPaging(page, 0, po);
        List deviceModelList = deviceService.getDeviceModelList(null);

		/**
		 * 데이터 셋팅
		 */
		model.addAttribute("page", page);
		model.addAttribute("param", po);
		model.addAttribute("deviceModelList", deviceModelList);
		model.addAttribute("resultPagingUtil", resultPagingUtil);

    	return "/v2/firmware/list";
    }

    /**
     * HERE
     * @param request
     * @param locale
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/detail.do")
    public String firmwareDetail(HttpServletRequest request,
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
		String firmwareId = request.getParameter("id");
		if (firmwareId == null) {
			return "redirect:/firmware/list.do"; 
		}
		
        HashMap<String, String> deviceModelInfo = deviceService.getDeviceModelInfoWithFirmwareId(firmwareId);
        
        HashMap<String, String> firmwareInfo = firmwareService.getFirmwareInfo(firmwareId);
        
        List<HashMap<String,String>> firmwareVersionList = firmwareService.getFirmwareVersionList(firmwareId);
		
        model.addAttribute("deviceModelInfo", deviceModelInfo);
		model.addAttribute("firmwareInfo", firmwareInfo);
		model.addAttribute("firmwareVersionList", firmwareVersionList);	

		ObjectMapper objectMapper = new ObjectMapper();
		
		model.addAttribute("deviceModelInfoJson", objectMapper.writeValueAsString(deviceModelInfo));
		model.addAttribute("firmwareInfoJson", objectMapper.writeValueAsString(firmwareInfo));
		model.addAttribute("firmwareVersionListJson", objectMapper.writeValueAsString(firmwareVersionList));

    	return "/v2/firmware/detail";
    }
    

    @RequestMapping(value="/device/upgrade.do")
    public String deviceUpgrade(HttpServletRequest request,
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

		String[] tokens = paramMap.get("deviceModelId");
		String deviceModelId = "";
		String firmwareId = "";
		List firmwareList = null;
		List versionList = null;
    	if (tokens != null && tokens.length > 0) {
    		deviceModelId = tokens[0];
    		firmwareList = firmwareService.getFirmwareListWithDeviceModelId(deviceModelId);
    	}
		tokens = paramMap.get("firmwareId");
    	if (tokens != null && tokens.length > 0) {    		
    		firmwareId = tokens[0];
    		versionList = firmwareService.getFirmwareVersionList(firmwareId);
    	}
		
		int page = StringUtil.parseInt(request.getParameter("page"), 1);
        PagingUtil resultPagingUtil = deviceService.getDeviceFirmwareListPaging(page, 0, paramMap);
        List deviceModelList = deviceService.getDeviceModelList(null);
        

		/**
		 * 데이터 셋팅
		 */
		model.addAttribute("page", page);
		model.addAttribute("param", paramMap);
		model.addAttribute("firmwareList", firmwareList);
		model.addAttribute("deviceModelList", deviceModelList);
		model.addAttribute("versionList", versionList);
		model.addAttribute("resultPagingUtil", resultPagingUtil);

		ObjectMapper objectMapper = new ObjectMapper();

		model.addAttribute("paramJson", objectMapper.writeValueAsString(paramMap));
		model.addAttribute("firmwareListJson", objectMapper.writeValueAsString(firmwareList));
		model.addAttribute("versionListJson", objectMapper.writeValueAsString(versionList));
		model.addAttribute("deviceModelListJson", objectMapper.writeValueAsString(deviceModelList));
		model.addAttribute("resultPagingUtilJson", objectMapper.writeValueAsString(resultPagingUtil));

    	return "/v2/firmware/upgrade";
    }

    /**
     * HERE
     * @param request
     * @param locale
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/device/schedule.do")
    public String deviceUpgradeSchedule(HttpServletRequest request,
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

		String[] tokens = paramMap.get("deviceModelId");
		String deviceModelId = "";
		String firmwareId = "";
		List firmwareList = null;
		List versionList = null;
    	if (tokens != null && tokens.length > 0) {
    		deviceModelId = tokens[0];
    		firmwareList = firmwareService.getFirmwareListWithDeviceModelId(deviceModelId);
    	}
		tokens = paramMap.get("firmwareId");
    	if (tokens != null && tokens.length > 0) {    		
    		firmwareId = tokens[0];
    		versionList = firmwareService.getFirmwareVersionList(firmwareId);
    	}
		
		int page = StringUtil.parseInt(request.getParameter("page"), 1);
        PagingUtil resultPagingUtil = deviceService.getDeviceFirmwareListPaging(page, 0, paramMap);
        List deviceModelList = deviceService.getDeviceModelList(null);
        List firmwareUpdateList = firmwareService.getFirmwareUpdateList(null);
        

		/**
		 * 데이터 셋팅
		 */
		model.addAttribute("page", page);
		model.addAttribute("param", paramMap);
		model.addAttribute("firmwareList", firmwareList);
		model.addAttribute("deviceModelList", deviceModelList);
		model.addAttribute("versionList", versionList);
		model.addAttribute("resultPagingUtil", resultPagingUtil);
		model.addAttribute("firmwareUpdateList", firmwareUpdateList);

		ObjectMapper objectMapper = new ObjectMapper();

		model.addAttribute("paramJson", objectMapper.writeValueAsString(paramMap));
		model.addAttribute("firmwareListJson", objectMapper.writeValueAsString(firmwareList));
		model.addAttribute("versionListJson", objectMapper.writeValueAsString(versionList));
		model.addAttribute("deviceModelListJson", objectMapper.writeValueAsString(deviceModelList));
		model.addAttribute("resultPagingUtilJson", objectMapper.writeValueAsString(resultPagingUtil));
		model.addAttribute("firmwareUpdateListJson", objectMapper.writeValueAsString(firmwareUpdateList));
		
    	return "/v2/firmware/schedule";
    }
    
    /**
     * Here
     * @param request
     * @param locale
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/device/status.do")
    public String deviceUpgradeScheduleStatus(HttpServletRequest request,
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

		String[] tokens = paramMap.get("deviceModelId");
		String deviceModelId = "";
		String firmwareId = "";
		List firmwareList = null;
		List versionList = null;
    	if (tokens != null && tokens.length > 0) {
    		deviceModelId = tokens[0];
    		firmwareList = firmwareService.getFirmwareListWithDeviceModelId(deviceModelId);
    	}
		tokens = paramMap.get("firmwareId");
    	if (tokens != null && tokens.length > 0) {    		
    		firmwareId = tokens[0];
    		versionList = firmwareService.getFirmwareVersionList(firmwareId);
    	}
		
		int page = StringUtil.parseInt(request.getParameter("page"), 1);
        PagingUtil resultPagingUtil = deviceService.getDeviceFirmwareListPaging(page, 0, paramMap);
        List deviceModelList = deviceService.getDeviceModelList(null);
        

		/**
		 * 데이터 셋팅
		 */
		model.addAttribute("page", page);
		model.addAttribute("param", paramMap);
		model.addAttribute("firmwareList", firmwareList);
		model.addAttribute("deviceModelList", deviceModelList);
		model.addAttribute("versionList", versionList);
		model.addAttribute("resultPagingUtil", resultPagingUtil);

		ObjectMapper objectMapper = new ObjectMapper();

		model.addAttribute("paramJson", objectMapper.writeValueAsString(paramMap));
		model.addAttribute("firmwareListJson", objectMapper.writeValueAsString(firmwareList));
		model.addAttribute("versionListJson", objectMapper.writeValueAsString(versionList));
		model.addAttribute("deviceModelListJson", objectMapper.writeValueAsString(deviceModelList));
		model.addAttribute("resultPagingUtilJson", objectMapper.writeValueAsString(resultPagingUtil));

    	return "/v2/firmware/status";
    }
}
