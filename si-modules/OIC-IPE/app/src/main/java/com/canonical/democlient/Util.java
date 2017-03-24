package com.canonical.democlient;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

/**
 * Created by 문선호 on 2016-09-28.
 */
public class Util {

    public static String makeUrl(String... str){
        StringBuffer sb = new StringBuffer();
        for( int i=0; i<str.length; i++ ) {
            sb.append(str[i]);
            if( i<str.length-1 ) {
                sb.append("/");
            }
        }
        return sb.toString();
    }

    public static String makeUrl(String baseUrl, Object... objs){
        StringBuffer sb = new StringBuffer(baseUrl);
        sb.append("/");
        try{
            for( int i=0; i<objs.length; i++ ) {
                sb.append( ((JSONObject)objs[i]).getString("rn") );
                if( i<objs.length-1 ) {
                    sb.append("/");
                }
            }
        } catch(JSONException je) {
            Log.i("UTIL",">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> makeUrl error");
        }
        return sb.toString();
    }

    public static String combineString(String... str){
        StringBuffer sb = new StringBuffer();
        for( int i=0; i<str.length; i++ ) {
            sb.append(str[i]);
        }
        return sb.toString();
    }

    // null 이나 empty이면 true 반환
    public static boolean isNoE(String msg){
        boolean result = false;
        if(msg==null || msg.equals("")){
            result = true;
        }
        return result;
    }

    // xml parsing
    public static String getValue( String xml, String path ) {
        Node node = null;
        String make_xml = Util.combineString(xml.substring(0,xml.indexOf(">")+1),"<root>",xml.substring(xml.indexOf(">")+1),"</root>");
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(make_xml.getBytes()));
            doc.getDocumentElement().normalize();
            XPath xpath = XPathFactory.newInstance().newXPath();
            node = (Node) xpath.evaluate("//pc/sgn/nev/rep/cin/"+path, doc, XPathConstants.NODE);
        } catch( Exception e ) {
            Log.i("GET VALUE >> ", e.toString());
        }
        return node.getTextContent();
    }

}
