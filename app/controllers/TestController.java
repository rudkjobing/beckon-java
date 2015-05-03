package controllers;

import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import support.mail.AWSMail;
import support.mail.AWSMailService;
import support.mail.Mail;
import support.notification.AWSNotificationService;

import java.util.ArrayList;
import java.util.List;

import static play.libs.Json.toJson;

/**
 * Created by Steffen Rudkjøbing on 22/02/15.
 * © 2014 Steffen Rudkjøbing
 */
public class TestController extends Controller {

    public static F.Promise<Result> mail(){
        Mail m = new AWSMail();

        ArrayList<String> to = new ArrayList<>();
        to.add("steffen@shout.dk");

        m.setTo(to);
        m.setFrom("greeter@shout.dk");
        m.setSubject("Welcome!");
        m.setTextBody("Welcome to Shout mate!");
        m.setHtmlBody("<B>Welcome</B> to Shout mate!");

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

    public static Result createEndpoint(){

        return ok();
    }

}
