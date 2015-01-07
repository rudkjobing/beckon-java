package controllers;

import classes.AddFriendshipRequest;
import com.avaje.ebean.Expr;
import models.AuthenticateUser;
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

    @Security.Authenticated(AuthenticateUser.class)
    public static Result add(){

        AddFriendshipRequest r = fromJson(request().body().asJson(), AddFriendshipRequest.class);
        User me = (User) Http.Context.current().args.get("userObject");
        User them = User.find.byId(r.getUserId());

        Friendship myFriendship = new Friendship();
        Friendship theirFriendship = new Friendship();

        myFriendship.setFriend(them);
        myFriendship.setNickname(r.getNickname());
        myFriendship.setOwner(me);
        myFriendship.setStatus(Friendship.Status.PENDING);

        theirFriendship.setFriend(me);
        theirFriendship.setNickname(me.getFirstName());
        theirFriendship.setOwner(them);
        theirFriendship.setStatus(Friendship.Status.INVITED);

        myFriendship.setPeer(theirFriendship);
        theirFriendship.setPeer(myFriendship);

        myFriendship.save();
        theirFriendship.save();

        return ok();
    }

    @Security.Authenticated(AuthenticateUser.class)
    public static Result getAll(){

        User user = (User) Http.Context.current().args.get("userObject");

        List<Friendship> friends = Friendship.find.where().and(Expr.eq("owner", user), Expr.eq("status", Friendship.Status.ACCEPTED)).findList();

        return ok(toJson(friends));

    }

    @Security.Authenticated(AuthenticateUser.class)
    public static Result delete(){

        User user = (User) Http.Context.current().args.get("userObject");
        return ok();
    }

}
