package net.herit.business.onem2m.handler;

import java.util.HashMap;
import java.util.Iterator;

public class OneM2MHeaderMaker {
	
	public HashMap<String, String> getBasicHeader(String host, String ri, String origin){
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Accept", "application/json");
        header.put("HOST", host);
        header.put("X-M2M-RI", ri);
        header.put("X-M2M-Origin", origin);

        Iterator<String> it = header.keySet().iterator();
        while(it.hasNext()){
            String key = it.next();
            String value = header.get(key);
            System.out.println("########### "+ key+" : "+value);
        }

        return header;
    }

}
