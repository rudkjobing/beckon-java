package support.notification;

import models.Device;

import java.util.List;

/**
 * Created by Steffen Rudkjøbing on 22/02/15.
 * © 2014 Steffen Rudkjøbing
 */
public interface NotificationService {

    public void addNotification(Notification notification);
    public void removeNotification(Notification notification);
    public void publish();
    public String createEndpoint(String uuid);

}
