package controllers;

import classes.AddBeckonRequest;
import classes.BeckonResult;
import com.avaje.ebean.annotation.Transactional;
import models.*;
import play.Logger;
import play.mvc.*;
import support.notification.AWSNotificationService;
import support.security.AuthenticateUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;

/**
 * Created by steffen on 03/01/15.
 */
public class BeckonController extends Controller{

    @Security.Authenticated(AuthenticateUser.class)
    public static Result getAll(){

        User user = (User) Http.Context.current().args.get("userObject");
        BeckonResult b = new BeckonResult();

        List<BeckonMembership> beckons = user.getBeckons();

        for(BeckonMembership m : beckons){
            b.addBeckon(m);
        }
        return ok(toJson(b));

    }

    @Transactional
    @Security.Authenticated(AuthenticateUser.class)
    public static Result add(){

        AddBeckonRequest a = fromJson(request().body().asJson(), AddBeckonRequest.class);
        User user = (User) Http.Context.current().args.get("userObject");

        Beckon b = new Beckon();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

        try {

            Date date = formatter.parse(a.begins);
            b.setBegins(date);

        } catch (Exception e) {
            return internalServerError();
        }

        b.setTitle(a.title);
        b.setLocation(a.location);

        BeckonMembership m = new BeckonMembership();
        m.setUser(user);
        m.setBeckon(b);
        b.getMembers().add(m);
        m.setRole(BeckonMembership.Role.CREATOR);
        m.setStatus(BeckonMembership.Status.ACCEPTED);
        user.getBeckons().add(m);
        m.save();

        for(Friendship f : a.members) {
            f.refresh();
            Logger.debug(f.getNickname());
            Logger.debug(f.getFriend().getEmail());
            m = new BeckonMembership();
            m.setUser(f.getFriend());
            m.setBeckon(b);
            b.getMembers().add(m);
            m.setRole(BeckonMembership.Role.MEMBER);
            m.setStatus(BeckonMembership.Status.INVITED);
            f.getOwner().getBeckons().add(m);
            m.save();

            AWSNotificationService service = new AWSNotificationService();

            service.setEndpoints(f.getFriend().getDevices());
            service.setMessage(user.getFirstName() + " " + user.getLastName() + " has invited you to " + b.getTitle());
            service.sendNotification();
        }

        b.save();

        return ok();

    }

}
