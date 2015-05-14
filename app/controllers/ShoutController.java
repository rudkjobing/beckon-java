package controllers;

import classes.ShoutAddRequest;
import classes.ShoutList;
import classes.ShoutMemberTransition;
import com.avaje.ebean.Expr;
import com.avaje.ebean.annotation.Transactional;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.*;
import org.apache.commons.lang3.time.DateUtils;
import play.Logger;
import play.libs.Json;
import play.mvc.*;
import support.misc.BroUtil;
import support.notification.AWSNotification;
import support.notification.AWSNotificationService;
import support.notification.Notification;
import support.security.AuthenticateCookie;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;

/**
 * Created by steffen on 03/01/15.
 */
public class ShoutController extends Controller{

    @Security.Authenticated(AuthenticateCookie.class)
    public static Result updateMemberStatus(){

        User user = (User) Http.Context.current().args.get("userObject");

        ShoutMemberTransition transition = fromJson(request().body().asJson(), ShoutMemberTransition.class);

        ShoutMembership member = ShoutMembership.find.byId(transition.memberId);
        if(member.getUser().equals(user)){
            member.setStatus(transition.status);
            member.save();

            /*Alert the other members*/
            Shout shout = member.getShout();
            AWSNotificationService service = new AWSNotificationService();
            for(ShoutMembership s: shout.getMembers()){
                if(s.getUser().equals(user)){
                    continue;
                }
                Notification notification = new AWSNotification()
                        .setEndpoints(s.getUser().getDevices())
                        .setBadge(BroUtil.getPendingFriendships(member.getUser()) + BroUtil.getPendingShouts(member.getUser()));
                if(transition.status.equals(ShoutMembership.Status.ACCEPTED)){
                    notification.setMessage(user.getFirstName() + " is attending " + shout.getTitle());
                }
                else if(transition.status.equals(ShoutMembership.Status.DECLINED)){
                    notification.setMessage(user.getFirstName() + " wont attend " + shout.getTitle());
                }
                else if(transition.status.equals(ShoutMembership.Status.MAYBE)){
                    notification.setMessage(user.getFirstName() + " might attend " + shout.getTitle());
                }
                service.addNotification(notification);
            }

            service.publish();

            return ok(toJson(member));
        }

        return badRequest();

    }

    @Security.Authenticated(AuthenticateCookie.class)
    public static Result getAll(){

        User user = (User) Http.Context.current().args.get("userObject");
        ShoutList b = new ShoutList();

        Date nowMinusHours = DateUtils.addHours(new Date(), -2);

        List<ShoutMembership> shouts = ShoutMembership.find.where().and(
                Expr.eq("user", user), Expr.gt("shout.begins", nowMinusHours)
        ).order().asc("shout.begins").findList();

        for(ShoutMembership m : shouts){
            b.addShout(m);
        }
        return ok(toJson(b.shouts));

    }

    @Transactional
    @Security.Authenticated(AuthenticateCookie.class)
    public static Result add(){

        ShoutAddRequest shoutRequest = fromJson(request().body().asJson(), ShoutAddRequest.class);
        User user = (User) Http.Context.current().args.get("userObject");

        ObjectNode result = Json.newObject();

        if(shoutRequest.title.equals("")){

            result.put("success", false);
            result.put("message", "Title can not be empty");
            return badRequest(result);
        }

        Shout newShout = new Shout();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

        try {

            Date date = formatter.parse(shoutRequest.begins);
            if(date.before(DateUtils.addHours(new Date(), -2))){
                throw new Exception("Date is too far in the past");
            }
            newShout.setBegins(date);

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Invalid date");
            return badRequest(result);
        }

        newShout.setTitle(shoutRequest.title.trim());

        if (shoutRequest.location.name == null){
            shoutRequest.location.name = "";
        }
        else{
            shoutRequest.location.name = shoutRequest.location.name.trim();
        }

        newShout.setLocation(shoutRequest.location);

        ShoutMembership member = new ShoutMembership();
        member.setUser(user);
        member.setShout(newShout);
        newShout.getMembers().add(member);
        member.setRole(ShoutMembership.Role.CREATOR);
        member.setStatus(ShoutMembership.Status.ACCEPTED);
        user.getBeckons().add(member);
        member.save();

        AWSNotificationService service = new AWSNotificationService();

        for(Friendship friend : shoutRequest.members) {
            friend.refresh();
            Logger.debug(friend.getNickname());
            Logger.debug(friend.getFriend().getEmail());
            member = new ShoutMembership();
            member.setUser(friend.getFriend());
            member.setShout(newShout);
            newShout.getMembers().add(member);
            member.setRole(ShoutMembership.Role.MEMBER);
            member.setStatus(ShoutMembership.Status.INVITED);
            friend.getOwner().getBeckons().add(member);
            member.save();

            Notification notification = new AWSNotification()
                    .setEndpoints(member.getUser().getDevices())
                    .setMessage(user.getFirstName() + " " + user.getLastName() + " has invited you to " + newShout.getTitle())
                    .setBadge(BroUtil.getPendingFriendships(member.getUser()) + BroUtil.getPendingShouts(member.getUser()));

            service.addNotification(notification);

        }

        service.publish();

        newShout.save();

        return ok();

    }

}
