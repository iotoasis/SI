/*****************************************************************************
 * 프로그램명  : FileDownload.java
 * 설     명  : 파일 다운로드
 * 참고  사항  : 없음
 *****************************************************************************
 * Date       Author  Version Description
 * ---------- ------- ------- -----------------------------------------------
 * 2012.11.09  LSH    1.0     초기작성
 *****************************************************************************/
package net.herit.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omg.CORBA.UserException;




/**
 * 파일다운로드 객체
 *
 * @author tnc
 * @version 1.0
 */
public class FileDownload {
    /**서블릿Response객체 */
	private HttpServletResponse res;
    /**다운로드할 파일이름 */
    private String filename = "";
    /** 클래스 명칭 */
    protected static final String CLASS_NAME = "FileDownload";

    /**
     * 파일 다운로드에 필요한 기본정보를 설정한다
     * @param res 출력에 필요한 HttpServletResponse객체
     * @param type 출력할 마임타입
     * @throws Exception
     */
    public FileDownload(HttpServletResponse res)  {
        res.reset();
        this.res = res;

        res.setContentType("application/octet-stream");
	}

    /**
     * 파일 다운로드에 필요한 기본정보를 설정한다
     * @param res 출력에 필요한 HttpServletResponse객체
     * @param type 출력할 마임타입
     * @throws Exception
     */
	public FileDownload(HttpServletResponse res, String type) {
		res.reset();
		this.res = res;

        //마임타입이 없으면 기본타입으로 설정한다
		if((type == null) || type.equals("")){
			res.setContentType("application/octet-stream");
		}else{
            res.setContentType(type);
        }
	}

    /**
     * 파일 다운로드를 실행한다
     * @param fileName 풀패스의 파일이름(절대경로)
     * @param downName 다운로드할 설정된 파일이름
     * @throws Exception
     */
	public void download(String fileName, String downName, HttpServletRequest req) throws Exception{
		this.filename = fileName;
		byte[] buffer = new byte[1024];
		int byteData = 0;
		int offset = 0;

        try{
            //파일의 최대길이
//            int maxLength = ConfigMng.getValueInt(IPropertyKey.MAX_FILE_NAME_LENGTH);
//            String tempName = StringUtil.toFileTruncate(downName, maxLength, "");

            //파일이름으로 헤더를 설정한다
//            res.setHeader("Content-Disposition", "attachment; filename=" + LangUtil.encode(tempName));
        	try {
        		//UTF-8로 수정한 이유는 일본어 웹에서 파일을 다운로드할때 파일명이 제대로 나오지 않아서.. (11/11/22 김동준)
//        		downName = new String(downName.getBytes("MS949"), "ISO-8859-1");
            	if(req.getHeader("User-Agent").contains("MSIE")) {
//            		downName = new String(downName.getBytes("MS949"), "ISO-8859-1");
            		downName = URLEncoder.encode(downName, "UTF-8".replace("\\+", "%20"));
            	//}else if(req.getHeader("User-Agent").contains("Mozilla")) {
            	}else{
            		downName = "\"" + new String(downName.getBytes("UTF-8"), "ISO-8859-1") +  "\"";
            	}
        		//한국어 되는것
//        		downName = new String(downName.getBytes("UTF-8"), "UTF-8");
        		//japan -> english
        		//String result = new String(ori.getBytes("ISO-8859-1"), "SHIFT_JIS");
        		//english -> japan
        		//String result = new String(ori.getBytes("SHIFT_JIS"), "ISO-8859-1");
        		//downName = new String(downName.getBytes(ConfigMng.getValue(IPropertyKey.ENCODE)), "UTF-8");
            } catch (UnsupportedEncodingException e) {

            }
        	res.setHeader("Content-Type", "application/octet-stream;");
            res.setHeader("Content-Disposition", "attachment; filename=" + downName + ";");
            res.setHeader("Content-Transfer-Encoding", "binary");
            res.setHeader("Pragma", "no-cache");
            res.setHeader("Expires", "-1");

            //파일객체를 생성한다
            File tempFile = new File(fileName);

            //데이터스트림을 생성한다
            FileInputStream in = new FileInputStream(tempFile);
            //Response객체를 이용하여 출력스트림을 생성
            ServletOutputStream out = res.getOutputStream();

            //모든데이터를 출력한다(버퍼길이만큼나누어서 출력한다)
            while((byteData = in.read(buffer, offset, buffer.length)) != -1){
                out.write(buffer, 0, byteData);
            }
            in.close();
            out.flush();
            out.close();
        }catch(IOException ex){
        	res.setStatus(404);
        	throw new Exception("download", ex);
        }
	}

    /**
     * 다운로드끝난 파일은 필요에 따라 삭제한다
     * @throws UserException
     */
	public void delete() throws Exception{
        //파일을 삭제
        if (!filename.equals("")){
            File file = new File(filename);
            file.delete();
        }
    }

	/**
     * 파일의 사이즈를 반환한다.
     * @param fileName 풀패스의 파일이름(절대경로)
     * @throws Exception
     */
	public static int getFileSize(String fileName) throws Exception{
        int fileSize = 0;
        byte[] buffer = new byte[1024];
		int byteData = 0;
		int offset = 0;

        try{
            //파일객체를 생성한다
            File tempFile = new File(fileName);
            if(tempFile.exists()){
	            //데이터스트림을 생성한다
	            FileInputStream in = new FileInputStream(tempFile);

	            //모든데이터를 출력한다(버퍼길이만큼나누어서 출력한다)
	            while((byteData = in.read(buffer, offset, buffer.length)) != -1){
	               fileSize += byteData;
	            }
	            in.close();
            }
	    }catch(FileNotFoundException e){
            throw new Exception("getFileSize", e);
        }catch(IOException e){
            throw new Exception("getFileSize", e);
        }
        return fileSize;
	}
}