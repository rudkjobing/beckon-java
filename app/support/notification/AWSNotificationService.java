package support.notification;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Created by Steffen Rudkjøbing on 22/02/15.
 * © 2014 Steffen Rudkjøbing
 */
public class AWSNotificationService implements NotificationService{

    AmazonSNSClient service;

    public AWSNotificationService(){

        Config c = ConfigFactory.load();
        AWSCredentials credentials = new BasicAWSCredentials(
                c.getString("aws.default.accesKey"),
                c.getString("aws.default.secretKey")
        );
        this.service = new AmazonSNSClient(credentials);
        Region region = Region.getRegion(Regions.US_EAST_1);
        this.service.setRegion(region);

    }

    public void sendNotification(){



    }

}
