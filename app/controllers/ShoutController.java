package controllers;

import classes.ShoutAddRequest;
import classes.ShoutList;
import classes.ShoutMemberTransition;
import com.avaje.ebean.Expr;
import com.avaje.ebean.annotation.Transactional;
import models.*;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import play.Logger;
import play.mvc.*;
import support.notification.AWSNotification;
import support.notification.AWSNotificationService;
import support.notification.Notification;
import support.security.AuthenticateUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;

/**
 * Created by steffen on 03/01/15.
 */
public class ShoutController extends Controller{

    @Security.Authenticated(AuthenticateUser.class)
    public static Result updateMemberStatus(){

        User user = (User) Http.Context.current().args.get("userObject");

        ShoutMemberTransition transition = fromJson(request().body().asJson(), ShoutMemberTransition.class);

        ShoutMembership member = ShoutMembership.find.byId(transition.memberId);
        if(member.getUser().equals(user)){
            member.setStatus(transition.status);
            member.save();
            AWSNotificationService service = new AWSNotificationService();
            if(transition.status.equals(ShoutMembership.Status.ACCEPTED)){

            }
            return ok(toJson(member));
        }

        return badRequest();

    }

    @Security.Authenticated(AuthenticateUser.class)
    public static Result getAll(){

        User user = (User) Http.Context.current().args.get("userObject");
        ShoutList b = new ShoutList();

        Date nowMinusHours = DateUtils.addHours(new Date(), -2);

        List<ShoutMembership> shouts = ShoutMembership.find.where().and(
                Expr.eq("user", user), Expr.gt("shout.begins", nowMinusHours)
        ).findList();

        for(ShoutMembership m : shouts){
            b.addBeckon(m);
        }
        return ok(toJson(b.beckons));

    }

    @Transactional
    @Security.Authenticated(AuthenticateUser.class)
    public static Result add(){

        ShoutAddRequest a = fromJson(request().body().asJson(), ShoutAddRequest.class);
        User user = (User) Http.Context.current().args.get("userObject");

        Shout b = new Shout();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

        try {

            Date date = formatter.parse(a.begins);
            b.setBegins(date);

        } catch (Exception e) {
            return internalServerError();
        }

        b.setTitle(a.title);
        b.setLocation(a.location);

        ShoutMembership m = new ShoutMembership();
        m.setUser(user);
        m.setShout(b);
        b.getMembers().add(m);
        m.setRole(ShoutMembership.Role.CREATOR);
        m.setStatus(ShoutMembership.Status.ACCEPTED);
        user.getBeckons().add(m);
        m.save();

        AWSNotificationService service = new AWSNotificationService();

        for(Friendship f : a.members) {
            f.refresh();
            Logger.debug(f.getNickname());
            Logger.debug(f.getFriend().getEmail());
            m = new ShoutMembership();
            m.setUser(f.getFriend());
            m.setShout(b);
            b.getMembers().add(m);
            m.setRole(ShoutMembership.Role.MEMBER);
            m.setStatus(ShoutMembership.Status.INVITED);
            f.getOwner().getBeckons().add(m);
            m.save();

            Notification notification = new AWSNotification()
                    .setEndpoints(f.getFriend().getDevices())
                    .setMessage(user.getFirstName() + " " + user.getLastName() + " has invited you to " + b.getTitle());

            service.addNotification(notification);

        }

        service.publish();

        b.save();

        return ok();

    }

}
