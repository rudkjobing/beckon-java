package support.notification;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreatePlatformEndpointRequest;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;
import com.amazonaws.services.sns.model.Endpoint;
import com.amazonaws.services.sns.model.PublishRequest;
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

    public void sendNotification(){

        if(this.message == null || this.endpoints == null){
            return;
        }

        for(Device d : this.endpoints){
            PublishRequest p = new PublishRequest();
            p.setTargetArn(d.getArn());
            p.setMessage(this.message);
            this.service.publish(p);
        }

    }

    public void setMessage(String message){
        this.message = message;
    }

    public void setEndpoints(List<Device> devices){
        this.endpoints = devices;
    }

    public CreatePlatformEndpointResult createEndpoint(String uuid){

        CreatePlatformEndpointRequest p = new CreatePlatformEndpointRequest()
                .withPlatformApplicationArn(this.arn)
                .withToken(uuid);

        return this.service.createPlatformEndpoint(p);

    }

}
