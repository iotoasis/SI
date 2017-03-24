package net.herit.common.error.resolver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.herit.common.conf.HeritProperties;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;



public class CustomExceptionResolver implements HandlerExceptionResolver {
	
	private String view;
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object object, Exception exception) {
		
		//Format 을 맞추기 위한 SimpleDateFormat
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		//오늘의 Date 객체를 구함
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(System.currentTimeMillis()));
		Date todayDate = calendar.getTime();
		//오늘을 날짜를 표현하는 String 객체
		String todayDateString = dateFormat.format(todayDate);
	
		String errorFileOnOff = HeritProperties.getProperty("Globals.ErrorFileOnOff");
		String errorFilePath = HeritProperties.getProperty("Globals.ErrorFilePath");
		
		
		if(errorFileOnOff.equals("ON")){
			//로그인한 유저 정보
			String loginId = "test login user id";
			
//			System.out.println("loginId = " +  loginId);				
//			System.out.println();
//			System.out.println();
//			System.out.println("[Error][" + todayDateString + "]===============================================================================================================================");
//			
//			System.out.println("	occurDate: " + todayDateString);
//			System.out.println("	requestURI: " + request.getRequestURI());
//			System.out.println("	servletPath: " + request.getServletPath());
//			System.out.println("	remoteHost: " + request.getRemoteHost());
//			System.out.println("	remoteAddr: " + request.getRemoteAddr());
//			System.out.println("getStackTrace(): " + ExceptionUtils.getStackTrace(exception));
//			System.out.println("===============================================================================================================================================");
//			System.out.println();
//			System.out.println();
				
				
			String filePath = errorFilePath;
			String fileName = "herit_error."+todayDateString.substring(0, 8);
			String type = ".log";
			
			String content = "[Error][" + todayDateString + "] ";
			content += "occurDate: " + todayDateString + " ";
			content += "loginId: " + loginId + " ";
			content += "parameter: ";
	        Enumeration en = request.getParameterNames();
	        while(en.hasMoreElements()){
				String str = (String)en.nextElement();
				String val = request.getParameter(str);
				content +=  "[" + str + " : " + val + "] ";
			}
	        content += "\r\n";
			content += "requestURI: " + request.getRequestURI() + "\r\n";
			content += "servletPath: " + request.getServletPath() + "\r\n";
			content += "remoteHost: " + request.getRemoteHost() + "\r\n";
			content += "remoteAddr: " + request.getRemoteAddr() + "\r\n";
			content += "getStackTrace(): " + ExceptionUtils.getStackTrace(exception);

			
			String fileFullPath = filePath+fileName+type;
	        File file = new File(fileFullPath);
			
			try{

				if(fileFullPath.length() == 0){
					try{
						FileWriter file_writer = new FileWriter(file);
						BufferedWriter buff_writer = new BufferedWriter(file_writer);
						PrintWriter print_writer = new PrintWriter(buff_writer, true);
						print_writer.println(content);

						if(print_writer.checkError()){
							System.out.println("print_writer error!!");
						}

						file.createNewFile();

						// System.out.println("파일 생성 성공");
					}catch(Exception e){
						// TODO: handle exception
						System.out.println("IO Exceiption!");
						e.printStackTrace();
					}
				}else{
					BufferedWriter buff_writer = new BufferedWriter(new FileWriter(file, true));
					PrintWriter print_writer = new PrintWriter(buff_writer, true);
					print_writer.println(content);

					if(print_writer.checkError()){
						System.out.println("print_writer error!!");
					}
					// System.out.println("이어쓰기 성공");
				}

			}catch(IOException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
		
		ModelAndView view = new ModelAndView();
		view.addObject("exception", exception);
		view.setViewName("/common/error/customError");
		return view;
	}
	

	public String getView() {
		return view;
	}
	public void setView(String view) {
		this.view = view;
	}	
}
