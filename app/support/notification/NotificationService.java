package support.notification;

import models.Device;

import java.util.List;

/**
 * Created by Steffen Rudkjøbing on 22/02/15.
 * © 2014 Steffen Rudkjøbing
 */
public interface NotificationService {

    public void sendNotification();
    public void setMessage(String message);
    public void setEndpoints(List<Device> devices);

}
