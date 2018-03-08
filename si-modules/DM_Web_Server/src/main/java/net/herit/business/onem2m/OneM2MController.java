package net.herit.business.onem2m;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import io.netty.handler.codec.http.HttpResponseStatus;
import net.herit.business.api.service.OneM2MApiService;
import net.herit.business.onem2m.service.ConfigurationService;
import net.herit.business.onem2m.service.ConfigurationVO;
import net.herit.business.onem2m.service.PlatformService;
import net.herit.business.onem2m.service.PlatformVO;
import net.herit.common.conf.HeritProperties;
import net.herit.common.exception.BizException;
import net.herit.common.exception.UserSysException;
import net.herit.common.util.StringUtil;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.security.dto.GroupAuthorization;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value="/onem2m")
public class OneM2MController {
	
	@Resource(name = "ConfigurationService")
	private ConfigurationService configurationService;

	@Resource(name = "PlatformService")
	private PlatformService platformService;
	
	@RequestMapping(value="/manager.do")
	public String deviceMonitor(HttpServletRequest request, Locale locale, ModelMap model) throws Exception {
		
		HttpSession session = request.getSession(false);
		if(session != null){
			//페이지 권한 확인
			GroupAuthorization requestAuth = (GroupAuthorization) session.getAttribute("requestAuth");
			if(!requestAuth.getAuthorizationDBRead().equals("1")){
				model.addAttribute("authMessage", "onem2m 페이지 접근 권한이 없습니다.");
				return "forward:" + HeritProperties.getProperty("Globals.MainPage");
			}
		}
		
		String resourceUri = request.getParameter("uri");
		
		if (resourceUri == null) {
			resourceUri = "/herit-in/herit-cse"; 
		}
		
		model.addAttribute("main_uri", resourceUri);
		
		try { 
			OneM2MApiService onem2mService = OneM2MApiService.getInstance();
			String from = onem2mService.getAppName();
			String response = onem2mService.getResourceString(resourceUri, from); 
			//System.out.println(response);
			
	/*		OneM2MApiService onem2mService = OneM2MApiService.getInstance();
			String content = onem2mService.getResource(resourceUri, "/testOrigin");
			//System.out.println("=====>" + content);
*/			
			model.addAttribute("cse_addr", onem2mService.getCseAddr());
			model.addAttribute("content", response);
			
			
		} catch(Exception ex) {
			//System.out.println("ex :" + ex);
		}
		
		
		return "/v2/onem2m/manager";
	}
	
	@ResponseBody
	@RequestMapping(value="/delete.do")
	public Map<String, Object> deleteResource(HttpServletRequest request) throws Exception {
		
		Map<String, Object> response = new HashMap<String, Object>();
		HttpSession session = request.getSession(false);
		if(session != null){
			
			//페이지 권한 확인
			GroupAuthorization requestAuth = (GroupAuthorization) session.getAttribute("requestAuth");
			if(!requestAuth.getAuthorizationDBRead().equals("1")){
				response.put("result", 1);
				response.put("errorCode", -1);
				response.put("content", "authMessage: onem2m 페이지 접근 권한이 없습니다.");
			} else {
				String resourceUri = request.getParameter("uri");
				
				if (resourceUri == null) {
					resourceUri = "/herit-in/herit-cse"; 
				}
				
				try {
					
					
					OneM2MApiService onem2mService = OneM2MApiService.getInstance();
					String from = onem2mService.getAppName();
					RESPONSE_STATUS result = onem2mService.delete(resourceUri, from);
					int errorCode = 0;
					String strResult = "";
					if(result == RESPONSE_STATUS.DELETED) {
						strResult = RESPONSE_STATUS.DELETED.Name();
					} else {
						errorCode = -1;
						strResult = result.Name();
					}
					
					response.put("result", 0);
					response.put("errorCode", errorCode);
					response.put("content", strResult);
					
				} catch(Exception ex) {
					//System.out.println("ex :" + ex);
				}
			}
		} else {
			response.put("result", 1);
			response.put("errorCode", -1);
			response.put("content", "session is null");
		}
		
		return response;
	}
	
	@ResponseBody
	@RequestMapping(value="/create.do")
	public Map<String, Object> createResource(HttpServletRequest request) throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();
		HttpSession session = request.getSession(false);
		if(session != null){
			
			//페이지 권한 확인
			GroupAuthorization requestAuth = (GroupAuthorization) session.getAttribute("requestAuth");
			if(!requestAuth.getAuthorizationDBRead().equals("1")){
				response.put("result", 1);
				response.put("errorCode", -1);
				response.put("content", "authMessage: onem2m 페이지 접근 권한이 없습니다.");
			} else {
				String resourceUri = request.getParameter("uri");
				String resourceType = request.getParameter("rt");
				String body = request.getParameter("con");
				String origin = request.getParameter("from");
				String resourceName = request.getParameter("rn");
				
				if (resourceUri == null) {
					resourceUri = "/herit-in/herit-cse"; 
				}
				
				try {
					
					OneM2MApiService onem2mService = OneM2MApiService.getInstance();
					
					int result;
					int rt = Integer.parseInt(resourceType); 
					if(rt == RESOURCE_TYPE.AE.Value()) {
						result = onem2mService.createResourceAE(resourceUri, origin, body, resourceName, rt);
					} else {
						result = onem2mService.createResourceOther(resourceUri, origin, body, rt);
					}

					int errorCode = 0;
					String strResult = "";
					if(result == HttpResponseStatus.CREATED.code()) {
						strResult = HttpResponseStatus.CREATED.reasonPhrase();
					} else {
						errorCode = -1;
						strResult = HttpResponseStatus.valueOf(result).reasonPhrase();
					}
					
					response.put("result", 0);
					response.put("errorCode", errorCode);
					response.put("content", strResult);
					
				} catch(Exception ex) {
					//System.out.println("ex :" + ex);
				}
			}
		} else {
			response.put("result", 1);
			response.put("errorCode", -1);
			response.put("content", "session is null");
		}
		
		return response;
	}
	
	@ResponseBody
	@RequestMapping(value="/update.do")
	public Map<String, Object> updateResource(HttpServletRequest request) throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();
		HttpSession session = request.getSession(false);
		if(session != null){
			
			//페이지 권한 확인
			GroupAuthorization requestAuth = (GroupAuthorization) session.getAttribute("requestAuth");
			if(!requestAuth.getAuthorizationDBRead().equals("1")){
				response.put("result", 1);
				response.put("errorCode", -1);
				response.put("content", "authMessage: onem2m 페이지 접근 권한이 없습니다.");
			} else {
				String resourceUri = request.getParameter("ed_uri");
				String body = request.getParameter("ed_con");
				String origin = request.getParameter("ed_rn");
				
				if (resourceUri == null) {
					resourceUri = "/herit-in/herit-cse"; 
				}
				
				if(origin == null) {
					origin = "/dmserver" ;
				}
				
				try {
					
					OneM2MApiService onem2mService = OneM2MApiService.getInstance();
					
					int result;
					
					result = onem2mService.updateResource(resourceUri, origin, body);
					
					int errorCode = 0;
					String strResult = "";
					if(result == HttpResponseStatus.OK.code()) {
						strResult = HttpResponseStatus.OK.reasonPhrase();
					} else {
						errorCode = -1;
						strResult = HttpResponseStatus.valueOf(result).reasonPhrase();
					}
					
					response.put("result", 0);
					response.put("errorCode", errorCode);
					response.put("content", strResult);
					
				} catch(Exception ex) {
					//System.out.println("ex :" + ex);
				}
			}
		} else {
			response.put("result", 1);
			response.put("errorCode", -1);
			response.put("content", "session is null");
		}
		
		return response;
	}
	
	
    @RequestMapping(value="/qos/info.do")
    public String qosInfo(ModelMap model) throws Exception {
    	
    	ConfigurationVO configurationVO = new ConfigurationVO();
    	configurationVO.setCONFIGURATION_NAME("QOS");
    	configurationVO = configurationService.getConfiguration(configurationVO);
    	
    	
		model.addAttribute("configurationVO", configurationVO);
    	
    	return "/v2/onem2m/qosInfo";
    }
    
    
    @RequestMapping(value="/qos/update.do")
    public String updateQos(@ModelAttribute("configurationVO") ConfigurationVO configurationVO, ModelMap model) throws Exception {

    	configurationVO = configurationService.updateConfiguration(configurationVO);


    	return "redirect:/onem2m/qos/info.do";
    }    

    
    @RequestMapping(value="/platform/list.do")
    public String listPlatform(ModelMap model) throws Exception {
    	
		model.addAttribute("resultList", platformService.listPlatform());
    	
    	return "/v2/onem2m/platformList";
    }

    
    @RequestMapping(value="/platform/new.do")
    public String newPlatform(ModelMap model) throws Exception {
    	
    	return "/v2/onem2m/platformNew";
    }

    
    @RequestMapping(value="/platform/insert.do")
    @ResponseBody
    public Map<String, Object> insertPlatform(@ModelAttribute("platformVO") PlatformVO platformVO) throws Exception {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("result", false);

		
		// check spId
		if (null != platformService.getPlatformBySpId(platformVO)) {
    		dataMap.put("message", "이미 등록된 SP ID입니다!");

    		return dataMap;
		}

		
		// insert platform
		platformService.insert(platformVO);
		dataMap.put("result", true);

		
		return dataMap;
    }
    
    
    @RequestMapping(value="/platform/info.do")
    public String infoPlatform(@ModelAttribute("platformVO") PlatformVO platformVO, ModelMap model) throws Exception {
    	
		model.addAttribute("platformVO", platformService.getPlatform(platformVO));
    	
    	return "/v2/onem2m/platformInfo";
    }

    
    @RequestMapping(value="/platform/update.do")
    @ResponseBody
    public Map<String, Object> updatePlatform(@ModelAttribute("platformVO") PlatformVO platformVO) throws Exception {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("result", false);

		// update platform
		platformService.updatePlatform(platformVO);
		dataMap.put("result", true);

		
		return dataMap;
    }

    
    @RequestMapping(value="/platform/delete.do")
    @ResponseBody
    public Map<String, Object> deletePlatform(@ModelAttribute("platformVO") PlatformVO platformVO) throws Exception {

		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("result", true);
    	
		platformService.deletePlatform(platformVO);

    	return dataMap;
    }

    
    @RequestMapping(value="/resource.do")
    public String resource(ModelMap model) throws Exception {


    	return "/v2/onem2m/resource";
    }    
    
    
	@RequestMapping(value = "/getResource.do")
	@ResponseBody
	public Map<String, Object> getResource(HttpServletRequest request, @RequestParam Map<String,String> allRequestParams) throws BizException, UserSysException {

		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("result", true);
		

		String url = HeritProperties.getProperty("Globals.oneM2MHostUrl") + StringUtil.nvl(request.getParameter("url"));
			
    	// make headers
    	Map<String, String> hadersMap = new HashMap<String, String>();
    	hadersMap.put("X-M2M-Origin", "C-INCSE-Admin");
    	hadersMap.put("X-M2M-RI", "1234");
    	hadersMap.put("Authorization", "Basic VVNFUklJRC1YMTIxMzEyMTIxOkl2MkZfQnZORHZKZ0l1RnhoQ0IwUjV2MjVGUQ==");
    	
    	// send message
    	try {
    		HttpResponse<String> response = Unirest.get(url).headers(hadersMap).asString();
    		
			dataMap.put("status", response.getStatus());
			dataMap.put("statusText", response.getStatusText());
			dataMap.put("headers", response.getHeaders());
			dataMap.put("body", response.getBody());
    	}
    	catch(UnirestException ex) {
			dataMap.put("result", false);
			dataMap.put("message", ex.toString());
    	}
    	
    	
		return dataMap;
	}
    
}
