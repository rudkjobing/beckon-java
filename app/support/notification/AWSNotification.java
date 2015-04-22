package support.notification;

import models.Device;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Play on 22-04-2015.
 */
public class AWSNotification implements Notification {

    private String message = "";
    private List<Device> endpoints = new ArrayList<Device>();
    private boolean published = false;

    @Override
    public Notification setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public Notification setEndpoints(List<Device> endpoints) {
        this.endpoints = endpoints;
        return this;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public List<Device> getEndpoints() {
        return this.endpoints;
    }

    @Override
    public boolean isPublished() {
        return this.published;
    }

    @Override
    public void setPublished(boolean published) {
        this.published = published;
    }
}
