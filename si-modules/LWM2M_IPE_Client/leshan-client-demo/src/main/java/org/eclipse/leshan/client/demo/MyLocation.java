package org.eclipse.leshan.client.demo;

import java.util.Date;
import java.util.Random;

import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.response.ReadResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyLocation extends BaseInstanceEnabler {

    private static final Logger LOG = LoggerFactory.getLogger(MyLocation.class);

    private Random random;
    private double latitude;
    private double longitude;
    private Date timestamp;

    public MyLocation() {
        random = new Random();
        //latitude = Float.valueOf(random.nextInt(180));
        latitude = 37.495943;
        longitude = 127.025139;
        
        //longitude = Float.valueOf(random.nextInt(360));
        timestamp = new Date();
    }

    @Override
    public ReadResponse read(int resourceid) {
        LOG.info("Read on Location Resource " + resourceid);
        switch (resourceid) {
        case 0:
            return ReadResponse.success(resourceid, getLatitude());
        case 1:
            return ReadResponse.success(resourceid, getLongitude());
        case 5:
            return ReadResponse.success(resourceid, getTimestamp());
        default:
            return super.read(resourceid);
        }
    }

    public void moveLocation(String nextMove) {
        switch (nextMove.charAt(0)) {
        case 'w':
            moveLatitude(1.0f);
            break;
        case 'a':
            moveLongitude(-1.0f);
            break;
        case 's':
            moveLatitude(-1.0f);
            break;
        case 'd':
            moveLongitude(1.0f);
            break;
        }
    }

    private void moveLatitude(float delta) {
        latitude = latitude + delta;
        timestamp = new Date();
        fireResourcesChange(0, 5);
    }

    private void moveLongitude(float delta) {
        longitude = longitude + delta;
        timestamp = new Date();
        fireResourcesChange(1, 5);
    }

    public String getLatitude() {
        //return Float.toString(latitude - 90.0f);
    	return Double.toString(latitude);
    }

    public String getLongitude() {
        //return Float.toString(longitude - 180.f);
    	return Double.toString(longitude);
    }

    public Date getTimestamp() {
        return timestamp;
    }
}