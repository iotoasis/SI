package net.herit.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.log4j.Logger;

import net.herit.common.model.HeritFormBasedFileVO;
import net.herit.common.util.HeritWebUtil;

/**
 * @Class Name  : HeritFormBasedFileUtil.java
 * @Description : Form-based File Upload 유틸리티
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
public class HeritFormBasedFileUtil {
    /** Buffer size */
    public static final int BUFFER_SIZE = 8192;

    public static final String SEPERATOR = File.separator;

    /**
     * 오늘 날짜 문자열 취득.
     * ex) 20090101
     * @return
     */
    public static String getTodayString() {
	SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

	return format.format(new Date());
    }

    /**
     * 물리적 파일명 생성.
     * @return
     */
    public static String getPhysicalFileName() {
	return HeritFormBasedUUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

    /**
     * 파일명 변환.
     * @param filename String
     * @return
     * @throws Exception
     */
    protected static String convert(String filename) throws Exception {
	//return java.net.URLEncoder.encode(filename, "utf-8");
	return filename;
    }

    /**
     * Stream으로부터 파일을 저장함.
     * @param is InputStream
     * @param file File
     * @throws IOException
     */
    public static long saveFile(InputStream is, File file) throws IOException {
	// 디렉토리 생성
	if (! file.getParentFile().exists()) {
	    file.getParentFile().mkdirs();
	}

	OutputStream os = null;
	long size = 0L;

	try {
	    os = new FileOutputStream(file);
	    int bytesRead = 0;
	    byte[] buffer = new byte[BUFFER_SIZE];

	    while ((bytesRead = is.read(buffer, 0, BUFFER_SIZE)) != -1) {
		size += bytesRead;
		os.write(buffer, 0, bytesRead);
	    }
	} finally {
	    if (os != null) {
		os.close();
	    }
	}

	return size;
    }

    /**
     * 파일을 Upload 처리한다.
     *
     * @param request
     * @param where
     * @param maxFileSize
     * @return
     * @throws Exception
     */
    public static List<HeritFormBasedFileVO> uploadFiles(HttpServletRequest request, String where, long maxFileSize) throws Exception {
	List<HeritFormBasedFileVO> list = new ArrayList<HeritFormBasedFileVO>();

	// Check that we have a file upload request
	boolean isMultipart = ServletFileUpload.isMultipartContent(request);

	if (isMultipart) {
	    // Create a new file upload handler
	    ServletFileUpload upload = new ServletFileUpload();
	    upload.setFileSizeMax(maxFileSize);	// SizeLimitExceededException

	    // Parse the request
	    FileItemIterator iter = upload.getItemIterator(request);
	    while (iter.hasNext()) {
	        FileItemStream item = iter.next();
	        String name = item.getFieldName();
	        InputStream stream = item.openStream();
	        if (item.isFormField()) {
	            //System.out.println("Form field '" + name + "' with value '" + Streams.asString(stream) + "' detected.");
	            Logger.getLogger(HeritFormBasedFileUtil.class).info("Form field '" + name + "' with value '" + Streams.asString(stream) + "' detected.");
	        } else {
	            //System.out.println("File field '" + name + "' with file name '" + item.getName() + "' detected.");
	            Logger.getLogger(HeritFormBasedFileUtil.class).info("File field '" + name + "' with file name '" + item.getName() + "' detected.");

	            if ("".equals(item.getName())) {
	        	continue;
	            }

	            // Process the input stream
	            HeritFormBasedFileVO vo = new HeritFormBasedFileVO();

	            String tmp = item.getName();

	            if (tmp.lastIndexOf("\\") >= 0) {
	        	tmp = tmp.substring(tmp.lastIndexOf("\\") + 1);
	            }

	            vo.setFileName(tmp);
	            vo.setContentType(item.getContentType());
	            vo.setServerSubPath(getTodayString());
	            vo.setPhysicalName(getPhysicalFileName());

	            if (tmp.lastIndexOf(".") >= 0) {
	        	 vo.setPhysicalName(vo.getPhysicalName() + tmp.substring(tmp.lastIndexOf(".")));
	            }

	            long size = saveFile(stream, new File(HeritWebUtil.filePathBlackList(where) + SEPERATOR + vo.getServerSubPath() + SEPERATOR + vo.getPhysicalName()));

	            vo.setSize(size);

	            list.add(vo);
	        }
	    }
	} else {
	    throw new IOException("form's 'enctype' attribute have to be 'multipart/form-data'");
	}

	return list;
    }

    /**
     * 파일을 Download 처리한다.
     *
     * @param response
     * @param where
     * @param serverSubPath
     * @param physicalName
     * @param original
     * @throws Exception
     */
    public static void downloadFile(HttpServletResponse response, String where, String serverSubPath, String physicalName, String original) throws Exception {
	String downFileName = where + SEPERATOR + serverSubPath + SEPERATOR + physicalName;

	File file = new File(HeritWebUtil.filePathBlackList(downFileName));

	if (!file.exists()) {
	    throw new FileNotFoundException(downFileName);
	}

	if (!file.isFile()) {
	    throw new FileNotFoundException(downFileName);
	}

	byte[] b = new byte[BUFFER_SIZE];

	original = original.replaceAll("\r", "").replaceAll("\n", "");
	response.setContentType("application/octet-stream");
	response.setHeader("Content-Disposition", "attachment; filename=\"" + convert(original) + "\";");
	response.setHeader("Content-Transfer-Encoding", "binary");
	response.setHeader("Pragma", "no-cache");
	response.setHeader("Expires", "0");

	BufferedInputStream fin = null;
	BufferedOutputStream outs = null;

		try {
			fin = new BufferedInputStream(new FileInputStream(file));
			outs = new BufferedOutputStream(response.getOutputStream());

			int read = 0;

			while ((read = fin.read(b)) != -1) {
				outs.write(b, 0, read);
			}
		} finally {
			if (outs != null) {
				try {	// 2012.11 KISA 보안조치
					outs.close();
				} catch (Exception ignore) {	
					// no-op
				}
			}

			if (fin != null) {
				try {	// 2012.11 KISA 보안조치
					fin.close();
				} catch (Exception ignore) {
					// no-op
				}
			}
		}
	}

    /**
     * 이미지에 대한 미리보기 기능을 제공한다.
     *
     * mimeType의 경우는 JSP 상에서 다음과 같이 얻을 수 있다.
     * getServletConfig().getServletContext().getMimeType(name);
     *
     * @param response
     * @param where
     * @param serverSubPath
     * @param physicalName
     * @param mimeType
     * @throws Exception
     */
    public static void viewFile(HttpServletResponse response, String where, String serverSubPath, String physicalName, String mimeTypeParam) throws Exception {
	String mimeType = mimeTypeParam;
	String downFileName = where + SEPERATOR + serverSubPath + SEPERATOR + physicalName;

	File file = new File(HeritWebUtil.filePathBlackList(downFileName));

	if (!file.exists()) {
	    throw new FileNotFoundException(downFileName);
	}

	if (!file.isFile()) {
	    throw new FileNotFoundException(downFileName);
	}

	byte[] b = new byte[BUFFER_SIZE];

	if (mimeType == null) {
	    mimeType = "application/octet-stream;";
	}

	response.setContentType(HeritWebUtil.removeCRLF(mimeType));
	response.setHeader("Content-Disposition", "filename=image;");

	BufferedInputStream fin = null;
	BufferedOutputStream outs = null;

	try {
	    fin = new BufferedInputStream(new FileInputStream(file));
	    outs = new BufferedOutputStream(response.getOutputStream());

	    int read = 0;

	    while ((read = fin.read(b)) != -1) {
		outs.write(b, 0, read);
	    }
// 2011.10.10 보안점검 후속조치
	} finally {
	    if (outs != null) {
			try {
			    outs.close();
			} catch (Exception ignore) {
			    System.out.println("IGNORE: " + ignore);
			}
		    }
		    if (fin != null) {
			try {
			    fin.close();
			} catch (Exception ignore) {
			    System.out.println("IGNORE: " + ignore);
			}
		    }
		}
    }
}
