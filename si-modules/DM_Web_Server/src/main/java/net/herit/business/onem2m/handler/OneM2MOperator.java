package net.herit.business.onem2m.handler;

import net.herit.business.protocol.HttpOperator;

public class OneM2MOperator {

	private HttpOperator httpOperator = new HttpOperator();
	private OneM2MHeaderMaker headerMaker = new OneM2MHeaderMaker();

	/*
	public int createAE() throws JSONException {
        int result = 0;
        AE ae = new AE();
        HttpURLConnection conn = httpOperator.sendPost(ServerInfo.getInstance().getCseUri(), ae.toJson(), headerMaker.getResourceCreationHeader(Constants.AE));
        JSONObject response = httpOperator.getResponse(conn);
        if( response != null && !response.has("RESPONSE-CODE") ){
            result = 200;
        } else {
            result = Integer.parseInt(response.getString("RESPONSE-CODE"));
        }
        return result;
    }
    */
}
