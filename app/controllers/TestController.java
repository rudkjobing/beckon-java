package controllers;

import classes.AddBeckonRequest;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;
import models.Location;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import support.mail.AWSMail;
import support.mail.AWSMailService;
import support.mail.Mail;
import support.notification.AWSNotificationService;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Callable;

import static play.libs.Json.toJson;

/**
 * Created by Steffen Rudkjøbing on 22/02/15.
 * © 2014 Steffen Rudkjøbing
 */
public class TestController extends Controller {

    public static F.Promise<Result> mail(){
        Mail m = new AWSMail();

        ArrayList<String> to = new ArrayList<>();
        to.add("steffen@beckon.dk");

        m.setTo(to);
        m.setFrom("greeter@beckon.dk");
        m.setSubject("Welcome!");
        m.setTextBody("Welcome to Beckon mate!");
        m.setHtmlBody("<B>Welcome</B> to Beckon mate!");

        AWSMailService mailer = new AWSMailService();

        F.Promise<Integer> promiseOfMail = F.Promise.promise(
                new F.Function0<Integer>() {
                    public Integer apply() {
                        mailer.sendMail(m);
                        return 1;
                    }
                }
        );
        return promiseOfMail.map(
                new F.Function<Integer, Result>() {
                    public Result apply(Integer i) {
                        return ok();
                    }
                }
        );
    }

    public static Result notification(){
        AWSNotificationService s = new AWSNotificationService();
        try{
            s.sendNotification();
        }
        catch(Exception e){
            return ok(e.getMessage());
        }
        return ok("notification sent... aight < (>.<) > ");
    }

    public static Result createEndpoint(){
        AWSNotificationService s = new AWSNotificationService();
        try{
            CreatePlatformEndpointResult r = s.createEndpoint("013adbda7aea59b7fe47f067838e6b1421f3badc3c4ad34b61dbb03a60b779e4");
            return ok(toJson(r));
        }
        catch(Exception e){
            return ok(e.getMessage());
        }
    }

}
