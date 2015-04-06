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

/**
 * Created by Steffen Rudkjøbing on 22/02/15.
 * © 2014 Steffen Rudkjøbing
 */
public class AWSNotificationService implements NotificationService{

    AmazonSNSClient service;
    String arn;

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

        PublishRequest p = new PublishRequest();
        p.setTargetArn("arn:aws:sns:us-east-1:477853814738:endpoint/APNS_SANDBOX/Beckon/31dfaa22-69b3-3701-b510-c31ac29c4105");
        p.setMessage("Den er der til den er væk : Augustus Caesar");

        this.service.publish(p);

    }

    public CreatePlatformEndpointResult createEndpoint(String uuid){

        CreatePlatformEndpointRequest p = new CreatePlatformEndpointRequest()
                .withPlatformApplicationArn(this.arn)
                .withToken(uuid);

        return this.service.createPlatformEndpoint(p);

    }

}
