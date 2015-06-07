package controllers;

import classes.FriendshipAddRequest;
import classes.FriendshipTransition;
import classes.SearchQuery;
import com.avaje.ebean.Expr;
import support.misc.BroUtil;
import support.notification.AWSNotification;
import support.notification.AWSNotificationService;
import support.notification.Notification;
import support.notification.NotificationService;
import support.security.AuthenticateCookie;
import models.Friendship;
import models.User;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

import java.util.ArrayList;
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

        FriendshipAddRequest friendshipAddRequest = fromJson(request().body().asJson(), FriendshipAddRequest.class);
        User me = (User) Http.Context.current().args.get("userObject");
        User them = User.find.byId(friendshipAddRequest.getUserId());

        //Find out if we are already friends
        for(Friendship friendship: me.getFriendships()){
            if(friendship.getFriend().equals(them)){
                return badRequest("You guys are already friends.");
            }
        }

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

        NotificationService service = new AWSNotificationService();

        Notification notification = new AWSNotification()
                .setEndpoints(them.getDevices())
                .setMessage(me.getFirstName() + " " + me.getLastName() + " wants to be friends with you!")
                .setBadge(BroUtil.getPendingFriendships(them) + BroUtil.getPendingShouts(them));

        service.addNotification(notification);

        service.publish();

        return ok();
    }

    @Security.Authenticated(AuthenticateCookie.class)
    public static Result getList(String status){

        User user = (User) Http.Context.current().args.get("userObject");
        List<Friendship> friends;
        if(status == null){
            friends = Friendship.find.where()
                    .and(
                            Expr.eq("owner", user),
                            Expr.or(
                                    Expr.or(
                                            Expr.eq("status", Friendship.Status.INVITED), Expr.eq("status", Friendship.Status.ACCEPTED)
                                    ),
                                    Expr.eq("status", Friendship.Status.PENDING)
                            )
                    ).orderBy("friend.firstName").findList();
        }
        else{
            friends = Friendship.find.where()
                    .and(
                            Expr.eq("owner", user),
                            Expr.eq("status", Friendship.Status.ACCEPTED)
                    ).orderBy("friend.firstName").findList();
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
        
        FriendshipTransition friendshipTransition = fromJson(request().body().asJson(), FriendshipTransition.class);

        Friendship friendship = Friendship.find.byId(friendshipTransition.id);
        if(!friendship.getOwner().equals(user)){
            return badRequest();
        }

        Friendship peer = friendship.getPeer();
        
        friendship.setStatus(Friendship.Status.ACCEPTED);
        peer.setStatus(Friendship.Status.ACCEPTED);
        
        friendship.save();
        peer.save();

        NotificationService service = new AWSNotificationService();

        Notification notification = new AWSNotification()
                .setEndpoints(peer.getOwner().getDevices())
                .setMessage(user.getFirstName() + " " + user.getLastName() + " has accepted your friend request!")
                .setBadge(BroUtil.getPendingFriendships(peer.getOwner()) + BroUtil.getPendingShouts(peer.getOwner()));

        service.addNotification(notification);

        service.publish();

        return ok();
    }

    @Security.Authenticated(AuthenticateCookie.class)
    public static Result decline(){
        User user = (User) Http.Context.current().args.get("userObject");

        FriendshipTransition friendshipTransition = fromJson(request().body().asJson(), FriendshipTransition.class);

        Friendship friendship = Friendship.find.byId(friendshipTransition.id);
        if(!friendship.getOwner().equals(user)){
            return badRequest();
        }

        Friendship peer = friendship.getPeer();

        friendship.setStatus(Friendship.Status.DECLINED);
        peer.setStatus(Friendship.Status.BLOCKED);

        friendship.save();
        peer.save();

        return ok();
    }

    @Security.Authenticated(AuthenticateCookie.class)
    public static Result search(){

        SearchQuery query = fromJson(request().body().asJson(), SearchQuery.class);
        User me = (User) Http.Context.current().args.get("userObject");
        String searchString = "%" + query.queryString + "%";
        List users = User.find.where().ilike("email", searchString).not(Expr.eq("id", me.getId())).findPagingList(10).getAsList();

        for(Friendship f: me.getFriendships()){
            if(users.contains(f.getFriend())){
                users.remove(f.getFriend());
            }
        }

        return ok(toJson(users));

    }

}
