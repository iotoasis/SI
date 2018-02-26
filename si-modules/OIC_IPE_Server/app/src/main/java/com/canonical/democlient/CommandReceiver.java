package com.canonical.democlient;

import org.json.JSONObject;

/**
 * Created by 문선호 on 2017-03-10.
 */
public class CommandReceiver {

    private HttpOneM2MOperation op = new HttpOneM2MOperation();

    private JSONObject result;
    private FoundItem foundItem = new FoundItem();
    private SensorResourceA sensor_a = null;

    public CommandReceiver( SensorResourceA sensor_a ){
        this.sensor_a = sensor_a;
    }

    public void init(HttpOneM2MOperation op){
        this.op = op;
    }

    public JSONObject receive(){
        try{
            System.out.println("ReceiveThread starts");

            op.init(Util.makeUrl(Constants.INCSE_ADDR, Constants.BASE_AE, Constants.POLLING_CHANNEL_URL[1], "pcu"), foundItem);
            String xml = op.retrievePollingChannelPCU();
            CommandController cc = new CommandController(xml);

            sensor_a.get
            result = cc.handle();
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            op.closeConnecton();
        }

        return result;
    }
}

