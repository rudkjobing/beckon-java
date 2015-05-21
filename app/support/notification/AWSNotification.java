package support.notification;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Device;
import play.libs.Json;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steffen Rudkjøbing on 22-04-2015.
 * © 2014 Steffen Rudkjøbing
 */
public class AWSNotification implements Notification {

    private List<Device> endpoints = new ArrayList<Device>();
    private String message = "";
    private int badge = 0;
    private String sound ="default";

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
    public Notification setBadge(int badge) {
        this.badge = badge;
        return this;
    }

    @Override
    public int getBadge() {
        return this.badge;
    }

    @Override
    public Notification setSound(String sound) {
        this.sound = sound;
        return this;
    }

    @Override
    public String getSound() {
        return this.sound;
    }

    @Override
    public String serializeMessage() {
        ObjectNode message = Json.newObject();
        ObjectNode apns = Json.newObject();
        ObjectNode aps = Json.newObject();

        aps.put("alert", this.getMessage());
        aps.put("badge", this.getBadge());
        aps.put("sound", this.getSound());

        apns.put("aps", aps);
        message.put("APNS", apns.toString());
        return message.toString();
    }
}
