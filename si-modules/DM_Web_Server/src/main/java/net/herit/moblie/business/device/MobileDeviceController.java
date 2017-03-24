package net.herit.moblie.business.device;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.herit.business.device.service.DeviceService;
import net.herit.business.device.service.ParameterVO;
import net.herit.common.conf.HeritProperties;
import net.herit.common.exception.UserSysException;
import net.herit.moblie.business.device.serivce.MobileDeviceService;
import net.herit.security.dto.GroupAuthorization;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/mdevice")
public class MobileDeviceController {
	
	@Resource(name="MobileDeviceService")
	private MobileDeviceService mobileDeviceSerivce;
	
	@Resource(name="DeviceService")
	private DeviceService deviceService;

	@RequestMapping(value="/detail.do")
	public String mDeviceDetail(HttpServletRequest request, 
								Locale locale, 
								ModelMap model) 
			throws Exception {
		
		/*HttpSession session = request.getSession(false);
		if(session != null){
			//페이지 권한 확인
			GroupAuthorization requestAuth = (GroupAuthorization) session.getAttribute("requestAuth");
			if(!requestAuth.getAuthorizationDBRead().equals("1")){
				model.addAttribute("authMessage", "사용자관리 메뉴는 읽기 권한이 없습니다.");
				return "forward:" + HeritProperties.getProperty("Globals.mFirstPage");
			}
		}*/
		/*int id = Integer.parseInt(request.getParameter("id"));
		HashMap<String, String> deviceModelInfo = mobileDeviceSerivce.getDeviceModelInfo(id);*/
		
		
		//List deviceModelList = mobileDeviceSerivce.getDeviceModelList(null);
		List deviceModelList = deviceService.getDeviceModelList(null);
		
		/**
		 * 데이터 셋팅
		 */
		model.addAttribute("deviceModelList", deviceModelList);
		//model.addAttribute("deviceModelInfo", deviceModelInfo);
		
		return "/mobile/business/device/detail";
	}
	
	@RequestMapping(value="/status.do")
	public String mDeviceStats(HttpServletRequest request, 
								Locale locale, 
								ModelMap model) 
			throws Exception {
		
		/*HttpSession session = request.getSession(false);
		if(session != null){
			//페이지 권한 확인
			GroupAuthorization requestAuth = (GroupAuthorization) session.getAttribute("requestAuth");
			if(!requestAuth.getAuthorizationDBRead().equals("1")){
				model.addAttribute("authMessage", "사용자관리 메뉴는 읽기 권한이 없습니다.");
				return "forward:" + HeritProperties.getProperty("Globals.mFirstPage");
			}
		}*/
		
		
		//List deviceModelList = mobileDeviceSerivce.getDeviceModelList(null);
		List deviceModelList = deviceService.getDeviceModelList(null);
		
		/**
		 * 데이터 셋팅
		 */
		model.addAttribute("deviceModelList", deviceModelList);
		
		return "/mobile/business/device/status";
	}
	
	@RequestMapping(value="/list.do")
	public String mDeviceList(HttpServletRequest request, 
								Locale locale, 
								ModelMap model) 
			throws Exception {
		
		/*HttpSession session = request.getSession(false);
		if(session != null){
			//페이지 권한 확인
			GroupAuthorization requestAuth = (GroupAuthorization) session.getAttribute("requestAuth");
			if(!requestAuth.getAuthorizationDBRead().equals("1")){
				model.addAttribute("authMessage", "사용자관리 메뉴는 읽기 권한이 없습니다.");
				return "forward:" + HeritProperties.getProperty("Globals.mFirstPage");
			}
		}*/
		
		
		//List deviceModelList = mobileDeviceSerivce.getDeviceModelList(null);
		List deviceModelList = deviceService.getDeviceModelList(null);
		
		/**
		 * 데이터 셋팅
		 */
		model.addAttribute("deviceModelList", deviceModelList);
		
		return "/mobile/business/device/list";
	}
	
	@RequestMapping(value="/setting.do")
	public String mDeviceSetting(HttpServletRequest request, 
								Locale locale, 
								ModelMap model) 
			throws Exception {
		
		/*HttpSession session = request.getSession(false);
		if(session != null){
			//페이지 권한 확인
			GroupAuthorization requestAuth = (GroupAuthorization) session.getAttribute("requestAuth");
			if(!requestAuth.getAuthorizationDBRead().equals("1")){
				model.addAttribute("authMessage", "사용자관리 메뉴는 읽기 권한이 없습니다.");
				return "forward:" + HeritProperties.getProperty("Globals.mFirstPage");
			}
		}*/
		
		
		//List deviceModelList = mobileDeviceSerivce.getDeviceModelList(null);
		List deviceModelList = deviceService.getDeviceModelList(null);
		
		/**
		 * 데이터 셋팅
		 */
		model.addAttribute("deviceModelList", deviceModelList);
		
		return "/mobile/business/device/setting";
	}
}
