package net.herit.business.onem2m.resource;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OneM2MResource {
	public String nameSpace = "m2m:";

    // label
    private ArrayList<String> labelList = null;
    public ArrayList<String> getLabelList(){
    	return labelList;
    }
    public void resetLabel(){
        labelList = new ArrayList<String>();
    }
    public void addLabel(String label){
        labelList.add(label);
    }

    // date time for et
    public String getExpiredTime(){
    	Date today = new Date();
        Date endtime = new Date(today.getTime() + (1000L * 60L * 60L * 24L * 3L)); // after 3 days
        
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd HHmmss");
        String tmpStr = sf.format(endtime);
        String[] arrDateItem = tmpStr.split(" ");
        String strEndTime = arrDateItem[0] + "T" + arrDateItem[1];
        
        return strEndTime;
    }
}
