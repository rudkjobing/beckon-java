package controllers;

import models.*;
import play.mvc.*;
import support.security.AuthenticateUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static play.libs.Json.toJson;

/**
 * Created by steffen on 03/01/15.
 */
public class BeckonController extends Controller{

    @Security.Authenticated(AuthenticateUser.class)
    public static Result getAll(){

        User user = (User) Http.Context.current().args.get("userObject");
        return ok(toJson(user.getBeckons()));

    }

    @Security.Authenticated(AuthenticateUser.class)
    public static Result add(){

//        JsonNode root = request().body().asJson();
//
//        Beckon newBeckon = fromJson(root.get("beckon"), Beckon.class);
//

        Beckon b = new Beckon();
        DateFormat format = new SimpleDateFormat("y/m/d H:m:s", Locale.ENGLISH);
        try{
            b.setTitle("Første beckon i verden");
            b.setBegins(format.parse("2015/8/24 23:43:10"));

            User u = User.find.where().eq("email", "slyngel@gmail.com").findUnique();

            BeckonMembership m = new BeckonMembership();
            m.setUser(u);
            m.setStatus(BeckonMembership.Status.ACCEPTED);
            m.setRole(BeckonMembership.Role.ADMIN);
            b.getMembers().add(m);
            m.setBeckon(b);

            u.getBeckons().add(m);
//
//            m.save();
            b.save();
//            u.save();

            return ok();
        }
        catch(Exception e){

        }
        return ok();
    }

}
