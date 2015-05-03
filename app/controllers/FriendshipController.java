package controllers;

import classes.FriendshipAddRequest;
import classes.FriendshipTransition;
import com.avaje.ebean.Expr;
import support.notification.AWSNotification;
import support.notification.AWSNotificationService;
import support.notification.Notification;
import support.security.AuthenticateCookie;
import models.Friendship;
import models.User;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

import java.util.List;

import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;

/**
 * Created by Steffen Rudkjøbing on 07/01/15.
 * © 2014 Steffen Rudkjøbing
 */
public class FriendshipController extends Controller {

    @Security.Authenticated(AuthenticateCookie.class)
    public static Result add(){

        FriendshipAddRequest r = fromJson(request().body().asJson(), FriendshipAddRequest.class);
        User me = (User) Http.Context.current().args.get("userObject");
        User them = User.find.byId(r.getUserId());

        if(me.getId().equals(them.getId())){
            return badRequest("You cannot be friends with yourself.");
        }

        Friendship myFriendship = new Friendship();
        Friendship theirFriendship = new Friendship();

        myFriendship.setFriend(them);
        myFriendship.setOwner(me);
        myFriendship.setStatus(Friendship.Status.INVITED);

        theirFriendship.setFriend(me);
        theirFriendship.setOwner(them);
        theirFriendship.setStatus(Friendship.Status.PENDING);

        myFriendship.save();
        theirFriendship.save();

        myFriendship.setPeer(theirFriendship);
        theirFriendship.setPeer(myFriendship);

        myFriendship.save();
        theirFriendship.save();

        AWSNotificationService service = new AWSNotificationService();

        Notification notification = new AWSNotification()
                .setEndpoints(them.getDevices())
                .setMessage(me.getFirstName() + " " + me.getLastName() + " wants to be friends with you!");

        service.publish();

        return ok();
    }

    @Security.Authenticated(AuthenticateCookie.class)
    public static Result getList(Long id, String status){

        User user = (User) Http.Context.current().args.get("userObject");
        List<Friendship> friends;
        if(status == null){
            friends = Friendship.find.where()
                    .and(
                            Expr.and(
                                    Expr.eq("owner", user),
                                    Expr.gt("id", id))
                            ,
                            Expr.or(
                                    Expr.or(
                                            Expr.eq("status", Friendship.Status.INVITED), Expr.eq("status", Friendship.Status.ACCEPTED)
                                    ),
                                    Expr.eq("status", Friendship.Status.PENDING)
                            )
                    ).orderBy("nickname").findList();
        }
        else{
            friends = Friendship.find.where()
                    .and(
                            Expr.and(
                                    Expr.eq("owner", user),
                                    Expr.gt("id", id)
                            )
                            ,
                            Expr.eq("status", Friendship.Status.ACCEPTED)
                    ).orderBy("nickname").findList();
        }


        if(friends.size() == 0 && id != 0L){
            return status(304, "Not modified");
        }

        return ok(toJson(friends));

    }

    @Security.Authenticated(AuthenticateCookie.class)
    public static Result delete(){

        User user = (User) Http.Context.current().args.get("userObject");
        return ok();
    }

    @Security.Authenticated(AuthenticateCookie.class)
    public static Result accept(){
        User user = (User) Http.Context.current().args.get("userObject");
        FriendshipTransition ft = fromJson(request().body().asJson(), FriendshipTransition.class);
        Friendship f = Friendship.find.byId(ft.id);
        Friendship peer = f.getPeer();
        f.setStatus(Friendship.Status.ACCEPTED);
        peer.setStatus(Friendship.Status.ACCEPTED);
        f.save();
        peer.save();

        AWSNotificationService service = new AWSNotificationService();

        Notification notification = new AWSNotification()
                .setEndpoints(peer.getOwner().getDevices())
                .setMessage(user.getFirstName() + " " + user.getLastName() + " has accepted your friend request!");

        service.publish();

        return ok();
    }

    public static Result decline(){
        User user = (User) Http.Context.current().args.get("userObject");
        FriendshipTransition ft = fromJson(request().body().asJson(), FriendshipTransition.class);
        Friendship f = Friendship.find.byId(ft.id);
        Friendship peer = f.getPeer();
        f.setStatus(Friendship.Status.DECLINED);
        peer.setStatus(Friendship.Status.BLOCKED);
        f.save();
        peer.save();
        return ok();
    }

}
