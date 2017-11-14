package net.herit.common.util;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.herit.common.util.SecurityModule;
import net.herit.common.model.HeritFormBasedFileVO;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * @Class Name  : HeritFileUploadUtil.java
 * @Description : Spring 기반 File Upload 유틸리티
 * @Modification Information
 *
 *     수정일         수정자                   수정내용
 *     -------          --------        ---------------------------
 *   2009.08.26       한성곤                  최초 생성
 *
 * @author 공통컴포넌트 개발팀 한성곤
 * @since 2009.08.26
 * @version 1.0
 * @see
 */
public class HeritFileUploadUtil extends HeritFormBasedFileUtil {
    /**
     * 파일을 Upload 처리한다.
     *
     * @param request
     * @param where
     * @param maxFileSize
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static List<HeritFormBasedFileVO> uploadFiles(HttpServletRequest request, String where, long maxFileSize) throws Exception {
    	List<HeritFormBasedFileVO> list = new ArrayList<HeritFormBasedFileVO>();

		MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest)request;
		Iterator fileIter = mptRequest.getFileNames();

		while (fileIter.hasNext()) {
		    MultipartFile mFile = mptRequest.getFile((String)fileIter.next());
		    HeritFormBasedFileVO vo = new HeritFormBasedFileVO();

		    String tmp = mFile.getOriginalFilename();

	            if (tmp.lastIndexOf("\\") >= 0) {
	        	tmp = tmp.substring(tmp.lastIndexOf("\\") + 1);
	            }

	            vo.setFieldName(mFile.getName());
	            vo.setFileName(tmp);
	            vo.setContentType(mFile.getContentType());
	            //서브디렉토리 주석처리
//	            vo.setServerSubPath(getTodayString());
	            vo.setPhysicalName(getPhysicalFileName());
	            vo.setSize(mFile.getSize());
	            System.out.println("size : " + mFile.getSize());

	            if (tmp.lastIndexOf(".") >= 0) {
	       	 		vo.setPhysicalName(vo.getPhysicalName());	// 2012.11 KISA 보안조치
	            }

	            if (mFile.getSize() >= 0) {
	            	InputStream is = null;
	            	System.out.println("size : " + mFile.getSize());
	            	try {
	            		is = mFile.getInputStream();
//	            		saveFile(is, new File(HeritWebUtil.filePathBlackList(where+SEPERATOR+vo.getServerSubPath()+SEPERATOR+vo.getPhysicalName())));
	            		saveFile(is, new File(HeritWebUtil.filePathBlackList(where+SEPERATOR+vo.getPhysicalName())));
	            		System.out.println("saveFile Name : "+vo.getPhysicalName());
	            	} finally {
	            		if (is != null) {
	            			is.close();
	            		}
	            	}
	            	list.add(vo);
	            }
		}

		return list;
    }
    
    /**
     * HERE
     * @param request
     * @param where
     * @param maxFileSize
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static List<HeritFormBasedFileVO> filesUpload(HttpServletRequest request, String where, long maxFileSize) throws Exception {
    	List<HeritFormBasedFileVO> list = new ArrayList<HeritFormBasedFileVO>();

		MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest)request;
		Iterator fileIter = mptRequest.getFileNames();

		System.out.println("in");
		
		while (fileIter.hasNext()) {
		    MultipartFile mFile = mptRequest.getFile((String)fileIter.next());
		    //if (mFile.getSize() == 0) {
		    //	continue;
		    //}
		    
		    HeritFormBasedFileVO vo = new HeritFormBasedFileVO();

		    String tmp = mFile.getOriginalFilename();
		    System.out.println("Orifilename : "+tmp);
		    System.out.println("filename : "+mFile.getName());
            if (tmp.lastIndexOf("\\") >= 0) {
            	tmp = tmp.substring(tmp.lastIndexOf("\\") + 1);
            }

            vo.setFieldName(mFile.getName());
            vo.setFileName(tmp);
            vo.setContentType(mFile.getContentType());
            //서브디렉토리 주석처리
//	            vo.setServerSubPath(getTodayString());
            vo.setPhysicalName(getPhysicalFileName());
            vo.setSize(mFile.getSize());
            System.out.println("size : " + mFile.getSize());

            if (tmp.lastIndexOf(".") >= 0) {
       	 		vo.setPhysicalName(vo.getPhysicalName());	// 2012.11 KISA 보안조치
            }

            if (mFile.getSize() > 0) {
            	InputStream is = null;
            	System.out.println("size : " + mFile.getSize());
            	try {
            		is = mFile.getInputStream();
//	            		saveFile(is, new File(HeritWebUtil.filePathBlackList(where+SEPERATOR+vo.getServerSubPath()+SEPERATOR+vo.getPhysicalName())));
            		
            		//저장되는 이름 제어
            		saveFile(is, new File(HeritWebUtil.filePathBlackList(where+SEPERATOR+vo.getFileName())));
            		System.out.println("saveFile Name : "+vo.getFileName());
            	} catch(Exception e){
            		e.printStackTrace();
            	} finally {
            		if (is != null) {
            			is.close();
            		}
            	}
            }
        	list.add(vo);
		}

		return list;
    }



    /**
     * 파일을 Upload 처리한다. (이미지 전용)
     *
     * @param request
     * @param where
     * @param maxFileSize
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static List<HeritFormBasedFileVO> uploadExtensionFiles(HttpServletRequest request, String where, long maxFileSize) throws Exception {
		List<HeritFormBasedFileVO> list = new ArrayList<HeritFormBasedFileVO>();

		MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest)request;
		Iterator fileIter = mptRequest.getFileNames();

		while (fileIter.hasNext()) {
		    MultipartFile mFile = mptRequest.getFile((String)fileIter.next());

		    //LGU+ 보안 취약점 모듈 적용 (img 파일인지 점검)
		    if(SecurityModule.VulnerabilityChek(mFile.getOriginalFilename(), 0, "common", "fileup_img").equals("true")){
			    HeritFormBasedFileVO vo = new HeritFormBasedFileVO();

			    String tmp = mFile.getOriginalFilename();


			    String fileExtension = "";

		            if (tmp.lastIndexOf("\\") >= 0) {
		            	tmp = tmp.substring(tmp.lastIndexOf("\\") + 1);
		            }
		            if (tmp.lastIndexOf(".") >= 0) {
		            	fileExtension = tmp.substring(tmp.lastIndexOf(".") + 1);
		            }

		            vo.setFieldName(mFile.getName());
		            vo.setFileName(tmp);
		            vo.setContentType(mFile.getContentType());
		            //vo.setServerSubPath(getTodayString());
		            vo.setPhysicalName(getPhysicalFileName()+"."+fileExtension);
		            vo.setSize(mFile.getSize());

//		            if (tmp.lastIndexOf(".") >= 0) {
//		       	 	vo.setPhysicalName(vo.getPhysicalName());	// 2012.11 KISA 보안조치
//		            }

		            if (mFile.getSize() > 0) {
		            	InputStream is = null;

		            	try {
		            		is = mFile.getInputStream();
		            		//saveFile(is, new File(HeritWebUtil.filePathBlackList(where+SEPERATOR+vo.getServerSubPath()+SEPERATOR+vo.getPhysicalName())));
		            		saveFile(is, new File(HeritWebUtil.filePathBlackList(where+SEPERATOR+vo.getPhysicalName())));
		            	} finally {
		            		if (is != null) {
		            			is.close();
		            		}
		            	}
		            	list.add(vo);
		            }


		    }// LGU+ 보안 취약점 모듈 적용 (img 파일인지 점검) end


		}

		return list;
    }




}
