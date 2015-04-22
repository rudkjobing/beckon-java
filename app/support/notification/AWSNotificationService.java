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
    String message;
    List<Device> endpoints;
    List<Notification> notifications;

    public AWSNotificationService(){

        Config c = ConfigFactory.load();
        AWSCredentials credentials = new BasicAWSCredentials(
                c.getString("aws.default.accesKey"),
                c.getString("aws.default.secretKey")
        );
        this.arn = c.getString("aws.default.arn");
        this.service = new AmazonSNSClient(credentials);
        Region region = Region.getRegion(Regions.US_EAST_1);
        this.service.setRegion(region);

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
            for(Device d : notification.getEndpoints()){
                PublishRequest p = new PublishRequest();
                p.setTargetArn(d.getArn());
                p.setMessage(notification.getMessage());
                this.service.publish(p);
            }
        }

    }

    public CreatePlatformEndpointResult createEndpoint(String uuid){

        CreatePlatformEndpointRequest p = new CreatePlatformEndpointRequest()
                .withPlatformApplicationArn(this.arn)
                .withToken(uuid);

        return this.service.createPlatformEndpoint(p);

    }

}
