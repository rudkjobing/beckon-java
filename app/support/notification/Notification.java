package support.notification;

import models.Device;

import java.util.List;

/**
 * Created by Steffen Rudkjøbing on 22-04-2015.
 * © 2014 Steffen Rudkjøbing
 */
public interface Notification {

    public Notification setMessage(String message);
    public Notification setEndpoints(List<Device> endpoints);
    public String getMessage();
    public List<Device> getEndpoints();
    public Notification setBadge(int badge);
    public int getBadge();
    public Notification setSound(String sound);
    public String getSound();
    public String serializeMessage();

}
