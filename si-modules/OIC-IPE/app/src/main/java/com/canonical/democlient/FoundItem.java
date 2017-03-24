package com.canonical.democlient;

/**
 * Created by seung-wanmun on 2016. 8. 11..
 */
public class FoundItem {
    private String deviceName;
    private String host;
    private String deviceId;

    private int content = 0;
    private String resourceName;
    private String resourceUri;
    private String pcuUri;
    private String con;

    public String getDeviceName() {
        return deviceName;
    }
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }

    public String getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getContent() { return content; }
    public void setContent(int content) { this.content = content; }

    public String getResourceName() {
        return resourceName;
    }
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceUri() {
        return resourceUri;
    }
    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }

    public String getPcuUri() {
        return pcuUri;
    }
    public void setPcuUri(String pcuUri) {
        this.pcuUri = pcuUri;
    }

    @Override
    public String toString() {
        return Util.makeUrl("[",deviceName,deviceId,host,String.valueOf(content),resourceName,resourceUri,"]");
    }

    public String getCon() {
        return con;
    }

    public void setCon(String con) {
        this.con = con;
    }
}
