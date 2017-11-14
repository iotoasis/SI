package net.herit.business.information;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.herit.common.util.SecurityModule;
import net.herit.business.accountmng.service.AccountService;
import net.herit.business.accountmng.service.AccountVO;
import net.herit.business.device.service.*;
import net.herit.business.history.service.*;
import net.herit.business.firmware.service.*;
import net.herit.common.conf.HeritProperties;
import net.herit.common.exception.BizException;
import net.herit.common.exception.UserSysException;
import net.herit.common.model.ProcessResult;
import net.herit.common.util.PagingUtil;
import net.herit.common.util.StringUtil;
import net.herit.security.dto.GroupAuthorization;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;


@Controller
@RequestMapping(value="/information")
public class InformationController {

	@Resource(name = "DeviceService")
	private DeviceService deviceService;

	@Resource(name = "AccountService")
	private AccountService accountService;

    

    @RequestMapping(value="/deviceModel.do")
    public String deviceModelList(HttpServletRequest request,
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
		
    	return "/v2/information/deviceModel";
    }
    @RequestMapping(value="/deviceModel/detail.do")
    public String deviceModelDetail(HttpServletRequest request,
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
		
		Map<String, String[]> paramMap = request.getParameterMap();

		/**
		 * 데이터 셋팅
		 */
		model.addAttribute("param", paramMap);
		
		ObjectMapper objectMapper = new ObjectMapper();

		model.addAttribute("paramJson", objectMapper.writeValueAsString(paramMap));
		
    	return "/v2/information/deviceModelDetail";
    }


    @RequestMapping(value="/user.do")
    public String userList(@ModelAttribute("accountVO") AccountVO accountVO,
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
		
		/*
		String deviceModel = po.getDeviceModel();
		String[] tokens = deviceModel.split("\\|");
		if (tokens.length == 2) {
			po.setOui(tokens[0]);
			po.setModelName(tokens[1]);
		}
		*/        //조회 페이지의 검색키워드 보안 취약점 보완
        String currPage = StringUtil.nvl(request.getParameter("currPage"), "1");
        int nPage = Integer.parseInt(currPage);
        if (nPage < 1) {
            nPage = 1;
        }
		
        PagingUtil resultPagingUtil = accountService.accountPagingList(nPage, accountVO);
        List mngAccountGroupIdList = accountService.getMngAccountGroupIdList();

		/**
		 * 데이터 셋팅
		 */
		model.addAttribute("accountVO", accountVO);
		model.addAttribute("currPage", currPage);
		model.addAttribute("mngAccountGroupIdList", mngAccountGroupIdList);
		model.addAttribute("resultPagingUtil", resultPagingUtil);
		

		ObjectMapper objectMapper = new ObjectMapper();
		model.addAttribute("resultPagingUtil", objectMapper.writeValueAsString(resultPagingUtil));

		
    	return "/v2/information/user";
    }
}
