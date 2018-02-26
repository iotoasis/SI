package com.canonical.democlient;

import android.app.Application;

import org.iotivity.base.OcRepresentation;
import org.iotivity.base.OcResource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by seung-wanmun on 2016. 8. 18..
 */
public class FoundDeviceInfo extends Application implements Serializable {
    private ArrayList<FoundItem> deviceList;
    private ArrayList<FoundPlatform > platformList;
    private Map<String, ArrayList<ResourceInfo>> resourceList;
    private ArrayList<OcRepresentation> ctrlResourceList;
    private ArrayList<OcResource> defaultResourceList;

    public FoundDeviceInfo() {
        deviceList = new ArrayList<FoundItem>();
        platformList = new ArrayList<>();
        ctrlResourceList = new ArrayList<>();
        resourceList = new HashMap<String, ArrayList<ResourceInfo>>();
        defaultResourceList = new ArrayList<>();
    }

    public void addFoundPlatform(FoundPlatform foundPlatform) {
        platformList.add(foundPlatform);
    }

    public ArrayList<FoundPlatform> getPlatformList() {
        return this.platformList;
    }

    public void addCtrlResource(OcRepresentation ocRepresentation) {
        ctrlResourceList.add(ocRepresentation);
    }

    public ArrayList<OcRepresentation> getCtrlResourceList() {
        return this.ctrlResourceList;
    }

    public void addDefaultResource(OcResource ocResource) {
        defaultResourceList.add(ocResource);
    }

    public ArrayList<OcResource> getDefaultResourceList() {
        return this.defaultResourceList;
    }

    public void addFoundItem(FoundItem foundItem) {
        deviceList.add(foundItem);
    }

    public ArrayList<FoundItem> getDeviceList() {
        return this.deviceList;
    }

    public void addResource(String host, String resourceUri, List<String> resourceTypes) {

        ResourceInfo resourceInfo = new ResourceInfo();

        resourceInfo.resourceUri = resourceUri;
        resourceInfo.resourceTypes = resourceTypes;

        ArrayList<ResourceInfo> element = resourceList.get(host);

        if(element == null) {
            element = new ArrayList<ResourceInfo>();
        }
        element.add(resourceInfo);
        resourceList.put(host, element);

    }

    public ArrayList<ResourceInfo> getResource(String host) {
        return this.resourceList.get(host);
    }

    static class ResourceInfo implements Serializable{
        String resourceUri;
        List<String> resourceTypes;
    }
}

