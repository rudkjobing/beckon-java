package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.*;
import play.libs.Json;
import play.mvc.*;
import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;

/**
 * Created by steffen on 03/01/15.
 */
public class Beckon extends Controller{

    public static Result getAll(){
        String email = session("connected");
        if(email == null){
            return forbidden();
        }
        User user = User.find.where().eq("email", email).findUnique();

        return ok(toJson(user.getBeckons()));

    }

}
