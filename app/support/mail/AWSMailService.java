package support.mail;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import play.Logger;

/**
 * Created by Steffen Rudkjøbing on 22/02/15.
 * © 2014 Steffen Rudkjøbing
 */
public class AWSMailService implements MailService {

    AmazonSimpleEmailServiceClient service;

    public AWSMailService(){
        Config c = ConfigFactory.load();
        AWSCredentials credentials = new BasicAWSCredentials(
                c.getString("aws.default.accesKey"),
                c.getString("aws.default.secretKey")
        );
        this.service = new AmazonSimpleEmailServiceClient(credentials);
        Region region = Region.getRegion(Regions.US_EAST_1);
        this.service.setRegion(region);
    }

    @Override
    public void sendMail(Mail mail){
        try {
            this.service.sendEmail((SendEmailRequest) mail.compile());
        }
        catch(Exception e){
            Logger.error("Unable to send Email " + mail.toString() + e.getMessage());
        }
    }

}
