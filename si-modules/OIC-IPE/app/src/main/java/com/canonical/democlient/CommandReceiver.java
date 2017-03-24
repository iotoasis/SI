package com.canonical.democlient;

/**
 * Created by 문선호 on 2017-03-10.
 */
public class CommandReceiver {

    private HttpOneM2MOperation op = new HttpOneM2MOperation();
    private FoundItem foundItem = null;

    public CommandReceiver(FoundItem foundItem){
        this.foundItem = foundItem;
    }

    public String receive(){
        String xml = null;
        try{
            System.out.println("ReceiveThread starts");

            op.init(Util.makeUrl(Constants.INCSE_ADDR, Constants.BASE_AE, Constants.POLLING_CHANNEL_URL[0], "pcu"), foundItem);
            xml = op.retrievePollingChannelPCU();

        } catch(Exception e){
            e.printStackTrace();
        } finally {
            op.closeConnecton();
        }

        return xml;
    }
}

