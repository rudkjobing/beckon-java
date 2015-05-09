package support.notification;

import models.Device;

import java.util.List;

/**
 * Created by Play on 22-04-2015.
 */
public interface Notification {

    public Notification setMessage(String message);
    public Notification setEndpoints(List<Device> endpoints);
    public String getMessage();
    public List<Device> getEndpoints();
    public void setPublished(boolean published);
    public Notification setBadge(int badge);
    public int getBadge();
    public boolean isPublished();

}
