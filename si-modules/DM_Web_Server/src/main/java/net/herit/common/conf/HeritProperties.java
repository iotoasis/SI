package net.herit.common.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Properties;

public class HeritProperties {
	
	public static final String PATH_ENCODE_TYPE = "UTF-8";
	
	public static final char FILE_SEPERATOR = File.separatorChar;
	
	public static final String FIRST_PACKAGE_NAME = "net";
	public static final String PROPS_FILE_PACAKAGE_DIR = "herit" + System.getProperty("file.separator") + "props" + System.getProperty("file.separator");
	public static final String RELATIVE_PATH_PREFIX = HeritProperties.class.getResource("").getPath().substring(0, HeritProperties.class.getResource("").getPath().lastIndexOf(FIRST_PACKAGE_NAME));
	public static final String GLOBALS_PROPERTIES_FILE_DIR = RELATIVE_PATH_PREFIX + PROPS_FILE_PACAKAGE_DIR + "globals.properties";
	
	
	public static String getProperty(String keyName) {
		String keyValue = null;
		
		FileInputStream fileInputStream = null;
		try {
			String decodedPropsFilePath = URLDecoder.decode(GLOBALS_PROPERTIES_FILE_DIR, PATH_ENCODE_TYPE);
			
			Properties properties = new Properties();
			fileInputStream = new FileInputStream(decodedPropsFilePath);
			
			properties.load(fileInputStream);
			keyValue = properties.getProperty(keyName).trim();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(fileInputStream != null){
					fileInputStream.close();
				} 
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return keyValue;
	}
	
}
