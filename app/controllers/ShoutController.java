package controllers;

import classes.ShoutAddRequest;
import classes.ShoutResult;
import classes.ShoutResultFactory;
import classes.ShoutMemberTransition;
import com.avaje.ebean.Expr;
import com.avaje.ebean.annotation.Transactional;
import com.fasterxml.jackson.databind.node.ObjectNode;
import factories.ShoutFactory;
import models.*;
import org.apache.commons.lang3.time.DateUtils;
import play.Logger;
import play.libs.Json;
import play.mvc.*;
import support.misc.BroUtil;
import support.notification.AWSNotification;
import support.notification.AWSNotificationService;
import support.notification.Notification;
import support.notification.NotificationService;
import support.security.AuthenticateCookie;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        if(!member.getUser().equals(user)) {
            return badRequest();
        }

        member.setStatus(transition.status);
        member.save();

        /*Alert the other members*/
        Shout shout = member.getShout();
        NotificationService service = new AWSNotificationService();
        boolean canDelete = true;
        for(ShoutMembership s: shout.getMembers()){
            if(s.status != ShoutMembership.Status.DELETED){
                canDelete = false;
            }
            if(s.getUser().equals(user) || member.status.equals(ShoutMembership.Status.DELETED)){
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
        if(canDelete){
            shout.delete();
        }
        else{
            service.publish();
        }

        return ok(toJson(member));

    }

    @Security.Authenticated(AuthenticateCookie.class)
    public static Result getAll(){

        User user = (User) Http.Context.current().args.get("userObject");

        Date nowMinusHours = DateUtils.addHours(new Date(), -2);

        List<ShoutMembership> shouts = ShoutMembership.find.where()
                .and(
                        Expr.eq("user", user),
                        Expr.and(
                                Expr.gt("shout.begins", nowMinusHours),
                                Expr.ne("status", ShoutMembership.Status.DELETED)
                        )
                )
                .order().asc("shout.begins").findList();

        List<ShoutResult> b = new ArrayList<>();
        for(ShoutMembership m : shouts){
            b.add(ShoutResultFactory.getShoutResult(m));
        }
        return ok(toJson(b));

    }

    @Security.Authenticated(AuthenticateCookie.class)
    public static Result get(Long memberId){

        User user = (User) Http.Context.current().args.get("userObject");

        ShoutMembership shout = ShoutMembership.find.byId(memberId);

        if(!shout.getUser().equals(user)){
            return badRequest();
        }

        return ok(toJson(ShoutResultFactory.getShoutResult(shout)));

    }

    @Transactional
    @Security.Authenticated(AuthenticateCookie.class)
    public static Result add(){

        ShoutAddRequest shoutRequest = fromJson(request().body().asJson(), ShoutAddRequest.class);
        User user = (User) Http.Context.current().args.get("userObject");

        ObjectNode result = Json.newObject();

        try{
            Shout shout = ShoutFactory.getShout(user, shoutRequest);
            NotificationService service = new AWSNotificationService();
            for(ShoutMembership member: shout.getMembers()){
                Notification notification = new AWSNotification()
                        .setEndpoints(member.getUser().getDevices())
                        .setMessage(user.getFirstName() + " " + user.getLastName() + " has invited you to " + shout.getTitle())
                        .setBadge(BroUtil.getPendingFriendships(member.getUser()) + BroUtil.getPendingShouts(member.getUser()));

                service.addNotification(notification);
            }
            service.publish();
        }
        catch (Exception e){
            result.put("success", false);
            result.put("message", e.getMessage());
            return badRequest(result);
        }



        return ok();

    }

}
