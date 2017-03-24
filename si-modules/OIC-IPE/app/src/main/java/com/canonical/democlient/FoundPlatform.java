package com.canonical.democlient;

/**
 * Created by seung-wanmun on 2016. 8. 22..
 */
public class FoundPlatform {
    private String host;
    private String platformId;
    private String manufactureName;
    private String manufactureUrl;
    private String modelNo;
    private String manufactureDate;
    private String platformVersion;
    private String osVersion;
    private String hardwareVersion;
    private String firmwareVersion;
    private String supportUrl;
    private String systemTime;

    public void setHost(String host) {
        this.host = host;
    }
    public String getHost() {
        return this.host;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }
    public String getPlatformId() {
        return this.platformId;
    }

    public void setManufactureName(String manufactureName) {
        this.manufactureName = manufactureName;
    }
    public String getManufactureName() {
        return this.manufactureName;
    }

    public void setManufactureUrl(String url) {
        this.manufactureUrl = url;
    }
    public String getManufactureUrl() {
        return this.manufactureUrl;
    }

    public void setModelNo(String modelNo) {
        this.modelNo = modelNo;
    }
    public String getModelNo() {
        return this.modelNo;
    }

    public void setManufactureDate(String date) {
        this.manufactureDate = date;
    }
    public String getManufactureDate() {
        return this.manufactureDate;
    }

    public void setPlatformVersion(String version) {
        this.platformVersion = version;
    }
    public String getPlatformVersion() {
        return this.platformVersion;
    }

    public void setOsVersion(String version) {
        this.osVersion = version;
    }
    public String getOsVersion() {
        return this.osVersion;
    }

    public void setHardwareVersion(String version) {
        this.hardwareVersion = version;
    }
    public String getHardwareVersion() {
        return this.hardwareVersion;
    }

    public void setFirmwareVersion(String version) {
        this.firmwareVersion = version;
    }
    public String getFirmwareVersion() {
        return this.firmwareVersion;
    }

    public void setSupportUrl(String url) {
        this.supportUrl = url;
    }
    public String getSupportUrl() {
        return this.supportUrl;
    }

    public void setSystemTime(String time) {
        this.systemTime = time;
    }
    public String getSystemTime() {
        return this.systemTime;
    }

    @Override
    public String toString() {
        return "[ platformId=" + platformId + ", manufactureName=" +
                manufactureName + " , modelNo=" + modelNo + " , manufactureDate=" + manufactureDate +
                " , manufactureUrl=" + manufactureUrl +
                " , platformVersion=" + platformVersion + " , osVersion=" + osVersion +
                " , hardwareVersion=" + hardwareVersion + " , firmwareVersion=" + firmwareVersion +
                " , supportUrl=" + supportUrl + " , systemTime=" + systemTime + " ]";
    }
}
