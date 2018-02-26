package com.canonical.democlient;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

/**
 * Created by Cesium on 2016-10-08.
 */

public class PageHandler extends AppCompatActivity {

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

    protected String getValue( String xml, String path ) {
        Node node = null;
        String make_xml = Util.combineString(xml.substring(0,xml.indexOf(">")+1),"<root>",xml.substring(xml.indexOf(">")+1),"</root>");
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(make_xml.getBytes()));
            doc.getDocumentElement().normalize();
            XPath xpath = XPathFactory.newInstance().newXPath();
            node = (Node) xpath.evaluate("//pc/sgn/nev/rep/cin/"+path, doc, XPathConstants.NODE);
        } catch( Exception e ) {
            Log.e("GET VALUE >> ", e.toString());
        }
        return node.getTextContent();
    }

    // add row on table
    private TableLayout.LayoutParams tableLayout = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
    private TableRow.LayoutParams tableRowLayout = new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);

    protected void appendRow( String value ){

        // create table row
        TableLayout tb = (TableLayout)findViewById(R.id.control_table_layout);
        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(tableLayout);

        // get current time
        long time = System.currentTimeMillis();
        SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String cur_time = dayTime.format(new Date(time));

        // set Text on TextView
        TextView tv_left = new TextView(this);
        tv_left.setText( cur_time );
        tv_left.setLayoutParams( tableRowLayout );
        tableRow.addView( tv_left );

        TextView tv_right = new TextView(this);
        tv_right.setText( value );
        tv_right.setLayoutParams( tableRowLayout );
        tableRow.addView( tv_right );

        // set table rows on table
        tb.addView(tableRow);
    }

    protected void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e("[PageHandler] : ", e.toString());
        }
    }


}