package support.notification;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.*;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import models.Device;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steffen Rudkjøbing on 22/02/15.
 * © 2014 Steffen Rudkjøbing
 */
public class AWSNotificationService implements NotificationService{

    AmazonSNSClient service;
    String arn;
    List<Notification> notifications = new ArrayList<>();

    public AWSNotificationService(){
        Config config = ConfigFactory.load();
        AWSCredentials credentials = new BasicAWSCredentials(
                config.getString("aws.default.accesKey"),
                config.getString("aws.default.secretKey")
        );

        this.arn = config.getString("aws.default.arn");
        this.service = new AmazonSNSClient(credentials);
        this.service.setRegion(Region.getRegion(Regions.US_EAST_1));
    }

    @Override
    public void addNotification(Notification notification) {
        this.notifications.add(notification);
    }

    @Override
    public void removeNotification(Notification notification) {
        this.notifications.remove(notification);
    }

    @Override
    public void publish() {
        for(Notification notification : this.notifications){
            for(Device device : notification.getEndpoints()){
                PublishRequest publishRequest = new PublishRequest();
                publishRequest.setMessageStructure("json");
                publishRequest.setTargetArn(device.getArn());
                publishRequest.setMessage(notification.serializeMessage());

                try {
                    this.service.publish(publishRequest);
                }
                catch(EndpointDisabledException e){
                    // Endpoint has been disabled, must clean up the device
                    DeleteEndpointRequest deleteEndpointRequest = new DeleteEndpointRequest().withEndpointArn(device.getArn());
                    service.deleteEndpoint(deleteEndpointRequest);
                    device.delete();
                }
            }
        }
    }

    @Override
    public String createEndpoint(String uuid){
        CreatePlatformEndpointRequest p = new CreatePlatformEndpointRequest()
                .withPlatformApplicationArn(this.arn)
                .withToken(uuid);

        return this.service.createPlatformEndpoint(p).getEndpointArn();
    }

}
