package com.canonical.democlient;

import java.io.Serializable;

/**
 * Created by seung-wanmun on 2016. 8. 11..
 */
public class FoundItem implements Serializable{
    private String deviceName;
    private String host;
    private String deviceId;
    private int content = 0;

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

    @Override
    public String toString() {
        return "[ deviceName=" + deviceName + ", host=" +
                host + " , deviceId=" + deviceId + " , content=" + content + " ]";
    }

}
