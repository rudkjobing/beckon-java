package support.notification;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import models.Device;
import play.libs.Json;

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
    List<Notification> notifications = new ArrayList<>();

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
            ObjectNode message = Json.newObject();
            ObjectNode apns = Json.newObject();
            ObjectNode aps = Json.newObject();
            aps.put("alert", notification.getMessage());
            aps.put("sound", "default");
            aps.put("badge", notification.getBadge());
            apns.put("aps", aps);
            message.put("APNS", apns);
            for(Device d : notification.getEndpoints()){
                PublishRequest p = new PublishRequest();
                p.setMessageStructure("json");
                p.setTargetArn(d.getArn());
                p.setMessage(message.toString());
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
