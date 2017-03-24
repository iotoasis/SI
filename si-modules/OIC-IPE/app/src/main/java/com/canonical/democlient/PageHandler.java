package com.canonical.democlient;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

/**
 * Created by Cesium on 2016-10-08.
 */

public class PageHandler extends AppCompatActivity {

    private ArrayList<String> lblList;
    private ArrayList<String> cmdList;
    private ArrayList<String> obsList;

    public void setCmdList(){
        cmdList = new ArrayList<String>();
        cmdList.add("cmd");
    }
    public void setCmdList( String ... lbl ){
        setCmdList();
        for( String str : lbl ){
            cmdList.add(str);
        }
    }
    public void setObsList(){
        obsList = new ArrayList<String>();
        obsList.add("obs");
    }
    public void setObsList( String ... lbl ){
        setObsList();
        for( String str : lbl ){
            obsList.add(str);
        }
    }
    public ArrayList<String> getCmdList(){
        return cmdList;
    }
    public ArrayList<String> getObsList(){
        return obsList;
    }

    public void setLblList( String ... lbl ){
        lblList = new ArrayList<String>();
        for( String str : lbl ) {
            lblList.add(str);
        }
    }
    public ArrayList<String> getLblList(){
        return lblList;
    }

    // xml parsing
    protected String getValue( String xml, String path ) {
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