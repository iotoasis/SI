/**
 *  Class Name : SecurityUtil.java
 *  Description : Base64인코딩/디코딩 방식을 이용한 데이터를 암호화/복호화하는 Business Interface class
 *  Modification Information
 *
 *     수정일         수정자                   수정내용
 *   -------    --------    ---------------------------
 *   2009.02.04    박지욱          최초 생성
 *
 *  @author 공통 서비스 개발팀 박지욱
 *  @since 2009. 02. 04
 *  @version 1.0
 *  @see
 *
 *  Copyright (C) 2009 by MOPAS  All right reserved.
 */
package net.herit.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;

import org.apache.commons.codec.binary.Base64;

public class SecurityUtil {

    // 파일구분자
    static final char FILE_SEPARATOR = File.separatorChar;

    static final int BUFFER_SIZE = 1024;

    /**
     * 파일을 암호화하는 기능
     *
     * @param String source 암호화할 파일
     * @param String target 암호화된 파일
     * @return boolean result 암호화여부 True/False
     * @exception Exception
     */
    public static boolean encryptFile(String source, String target) throws Exception {

		// 암호화 여부
		boolean result = false;

		String sourceFile = source.replace('\\', FILE_SEPARATOR).replace('/', FILE_SEPARATOR);
		String targetFile = target.replace('\\', FILE_SEPARATOR).replace('/', FILE_SEPARATOR);
		File srcFile = new File(sourceFile);

		BufferedInputStream input = null;
		BufferedOutputStream output = null;

		byte[] buffer = new byte[BUFFER_SIZE];

		try {
		    if (srcFile.exists() && srcFile.isFile()) {

			input = new BufferedInputStream(new FileInputStream(srcFile));
			output = new BufferedOutputStream(new FileOutputStream(targetFile));

			int length = 0;
			while ((length = input.read(buffer)) >= 0) {
			    byte[] data = new byte[length];
			    System.arraycopy(buffer, 0, data, 0, length);
			    output.write(encodeBinary(data).getBytes());
			    output.write(System.getProperty("line.separator").getBytes());
			}
			result = true;
		    }
		} catch (Exception ex) {
		    //ex.printStackTrace();
		    throw new RuntimeException(ex);	// 2011.10.10 보안점검 후속조치
		} finally {
		   if (input != null) {
		       try {
			   input.close();
		       } catch (Exception ignore) {
			   // no-op
		           //ignore.printStackTrace();
				   System.out.println("IGNORE: " + ignore);	// 보안점검 후속조치
		       }
		   }
		   if (output != null) {
		       try {
			   output.close();
		       } catch (Exception ignore) {
			   // no-op
		           //ignore.printStackTrace();
		           System.out.println("IGNORE: " + ignore);	// 보안점검 후속조치
		       }
		   }
		}
		return result;
    }

    /**
     * 파일을 복호화하는 기능
     *
     * @param String source 복호화할 파일
     * @param String target 복호화된 파일
     * @return boolean result 복호화여부 True/False
     * @exception Exception
     */
    public static boolean decryptFile(String source, String target) throws Exception {

		// 복호화 여부
		boolean result = false;

		String sourceFile = source.replace('\\', FILE_SEPARATOR).replace('/', FILE_SEPARATOR);
		String targetFile = target.replace('\\', FILE_SEPARATOR).replace('/', FILE_SEPARATOR);
		File srcFile = new File(sourceFile);

		BufferedReader input = null;
		BufferedOutputStream output = null;

		//byte[] buffer = new byte[BUFFER_SIZE];
		String line = null;

		try {
		    if (srcFile.exists() && srcFile.isFile()) {

			input = new BufferedReader(new InputStreamReader(new FileInputStream(srcFile)));
			output = new BufferedOutputStream(new FileOutputStream(targetFile));

			while ((line = input.readLine()) != null) {
			    byte[] data = line.getBytes();
			    output.write(decodeBinary(new String(data)));
			}

			result = true;
		    }
		} catch (Exception ex) {
		    //ex.printStackTrace();
		    throw new RuntimeException(ex);	// 보안점검 후속조치
		} finally {
		   if (input != null) {
		       try {
			   input.close();
		       } catch (Exception ignore) {
			   // no-op
		           //ignore.printStackTrace();
				   System.out.println("IGNORE: " + ignore); // 보안점검 후속조치
		       }
		   }
		   if (output != null) {
		       try {
			   output.close();
		       } catch (Exception ignore) {
			   // no-op
		    	   //ignore.printStackTrace();
				   System.out.println("IGNORE: " + ignore); // 보안점검 후속조치
		       }
		   }
		}
		return result;
    }

    /**
     * 데이터를 암호화하는 기능
     *
     * @param byte[] data 암호화할 데이터
     * @return String result 암호화된 데이터
     * @exception Exception
     */
    public static String encodeBinary(byte[] data) throws Exception {
		if (data == null) {
		    return "";
		}

		return new String(Base64.encodeBase64(data));
    }

    /**
     * 데이터를 암호화하는 기능
     *
     * @param String data 암호화할 데이터
     * @return String result 암호화된 데이터
     * @exception Exception
     */
    @Deprecated
    public static String encode(String data) throws Exception {
    	return encodeBinary(data.getBytes());
    }

    /**
     * 데이터를 복호화하는 기능
     *
     * @param String data 복호화할 데이터
     * @return String result 복호화된 데이터
     * @exception Exception
     */
    public static byte[] decodeBinary(String data) throws Exception {
    	return Base64.decodeBase64(data.getBytes());
    }

    /**
     * 데이터를 복호화하는 기능
     *
     * @param String data 복호화할 데이터
     * @return String result 복호화된 데이터
     * @exception Exception
     */
    @Deprecated
    public static String decode(String data) throws Exception {
    	return new String(decodeBinary(data));
    }

    /**
     * 비밀번호를 암호화하는 기능(복호화가 되면 안되므로 SHA-256 인코딩 방식 적용)
     *
     * @param String data 암호화할 비밀번호
     * @return String result 암호화된 비밀번호
     * @exception Exception
     */
    public static String encryptOpenApiKey(String data) throws Exception {

		if (data == null) {
		    return "";
		}

		byte[] plainText = null; // 평문
		byte[] hashValue = null; // 해쉬값
		plainText = data.getBytes();

		MessageDigest md = MessageDigest.getInstance("SHA-256");
		hashValue = md.digest(plainText);

		/*
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(hashValue);
		*/
		return new String(Base64.encodeBase64(hashValue));
    }

    /**
     * 비밀번호를 암호화하는 기능(복호화가 되면 안되므로 SHA-256 인코딩 방식 적용)
     *
     * @param String data 암호화할 비밀번호
     * @return String result 암호화된 비밀번호
     * @exception Exception
     */
    public static String encryptPassword(String data) throws Exception {

		if (data == null) {
		    return "";
		}

		byte[] plainText = null; // 평문
		byte[] hashValue = null; // 해쉬값
		plainText = data.getBytes();

		MessageDigest md = MessageDigest.getInstance("SHA-512");
		hashValue = md.digest(plainText);


		/* 기존 주석처리 (원래 주석 처리 되어 있었음)
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(hashValue);
		*/


		return new String(Base64.encodeBase64(hashValue));

    }

    /**
     * 비밀번호를 암호화하는 기능(복호화가 되면 안되므로 SHA-256 인코딩 방식 적용)
     * @param data 암호화할 비밀번호
     * @param salt Salt
     * @return 암호화된 비밀번호
     * @throws Exception
     */
    public static String encryptPassword(String data, byte[] salt) throws Exception {

		if (data == null) {
		    return "";
		}

		byte[] hashValue = null; // 해쉬값

		MessageDigest md = MessageDigest.getInstance("SHA-512");

		md.reset();
		md.update(salt);
		hashValue = md.digest(data.getBytes());

		return new String(Base64.encodeBase64(hashValue));
    }

    /**
     * 비밀번호를 암호화된 패스워드 검증(salt가 사용된 경우만 적용).
     *
     * @param data 원 패스워드
     * @param encoded 해쉬처리된 패스워드(Base64 인코딩)
     * @return
     * @throws Exception
     */
    public static boolean checkPassword(String data, String encoded, byte[] salt) throws Exception {
    	byte[] hashValue = null; // 해쉬값

    	MessageDigest md = MessageDigest.getInstance("SHA-512");

    	md.reset();
    	md.update(salt);
    	hashValue = md.digest(data.getBytes());

    	return MessageDigest.isEqual(hashValue, Base64.decodeBase64(encoded.getBytes()));
    }


    /**
     * 문자열을 지정한 분리자에 의해 배열로 리턴하는 메서드.
     * @param source 원본 문자열
     * @param separator 분리자
     * @return result 분리자로 나뉘어진 문자열 배열
     */
    public static String[] split(String source, String separator) throws NullPointerException {
        String[] returnVal = null;
        int cnt = 1;

        int index = source.indexOf(separator);
        int index0 = 0;
        while (index >= 0) {
            cnt++;
            index = source.indexOf(separator, index + 1);
        }
        returnVal = new String[cnt];
        cnt = 0;
        index = source.indexOf(separator);
        while (index >= 0) {
            returnVal[cnt] = source.substring(index0, index);
            index0 = index + 1;
            index = source.indexOf(separator, index + 1);
            cnt++;
        }
        returnVal[cnt] = source.substring(index0);

        return returnVal;
    }

    /**
     * 문자열을 지정한 분리자에 의해 지정된 길이의 배열로 리턴하는 메서드.
     * @param source 원본 문자열
     * @param separator 분리자
     * @param arraylength 배열 길이
     * @return 분리자로 나뉘어진 문자열 배열
     */
    public static String[] split(String source, String separator, int arraylength) throws NullPointerException {
        String[] returnVal = new String[arraylength];
        int cnt = 0;
        int index0 = 0;
        int index = source.indexOf(separator);
        while (index >= 0 && cnt < (arraylength - 1)) {
            returnVal[cnt] = source.substring(index0, index);
            index0 = index + 1;
            index = source.indexOf(separator, index + 1);
            cnt++;
        }
        returnVal[cnt] = source.substring(index0);
        if (cnt < (arraylength - 1)) {
            for (int i = cnt + 1; i < arraylength; i++) {
                returnVal[i] = "";
            }
        }

        return returnVal;
    }

    /*
    public static void main(String[] args) {
    	try {
    		String password = "abc";
    		String salt = "def";

    		String first = encryptPassword(password, salt.getBytes());
    		String second = encryptPassword(password, salt.getBytes());
			System.out.println(password + " => " + first + " : " + checkPassword(password, first, salt.getBytes()));
			System.out.println(password + " => " + second + " : " + checkPassword(password, second, salt.getBytes()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    }
    */
}