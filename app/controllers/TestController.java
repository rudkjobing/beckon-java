package controllers;

import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import support.mail.AWSMail;
import support.mail.AWSMailService;
import support.mail.Mail;

import java.util.ArrayList;
import java.util.concurrent.Callable;

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

}
