package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.*;
import play.libs.Json;
import play.mvc.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static play.libs.Json.toJson;

/**
 * Created by steffen on 03/01/15.
 */
public class BeckonController extends Controller{

    public static Result getEverything(){
        User u = User.find.where().eq("email", "slyngel@gmail.com").findUnique();

        return ok(toJson(u.getBeckons()));
    }

    public static Result getAll(){
        String email = session("connected");
        if(email == null){
            return forbidden();
        }
        User user = User.find.where().eq("email", email).findUnique();

        return ok(toJson(user.getBeckons()));

    }

    public static Result add(){

        Beckon b = new Beckon();
        DateFormat format = new SimpleDateFormat("y/m/d H:m:s", Locale.ENGLISH);
        try{
            b.setTitle("Første beckon i verden");
            b.setStarts(format.parse("2015/8/24 23:43:10"));
            b.setEnds(format.parse("2015/8/28 20:43:10"));

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
