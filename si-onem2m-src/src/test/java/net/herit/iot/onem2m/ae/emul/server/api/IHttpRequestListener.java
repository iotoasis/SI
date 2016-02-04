package net.herit.iot.onem2m.ae.emul.server.api;

import net.herit.iot.onem2m.ae.lib.HttpBasicRequest;
import net.herit.iot.onem2m.ae.lib.HttpBasicResponse;

/**
 * Created by otto on 2015. 11. 2..
 */
public interface IHttpRequestListener {
    public HttpBasicResponse handleHttpRequest(HttpBasicRequest request) throws Exception;

}
