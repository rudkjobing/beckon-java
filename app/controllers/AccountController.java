package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.*;
import play.libs.Json;
import play.mvc.*;
import static play.libs.Json.fromJson;

/**
 * Created by steffen on 02/01/15.
 */
public class AccountController extends Controller{

    public static Result signIn(){
        /* Get the request body and deserialize it */
        SignInRequest request = fromJson(request().body().asJson(), SignInRequest.class);
        User user = User.find.where().eq("email", request.email).findUnique();
        ObjectNode result = Json.newObject();

        /* Check that the supplied password matches the hash from the user object */
        try{
            Password.check(request.password, user.getHash());
            result.put("success", true);
        }
        /* Passwords did not match, return bad request */
        catch(Exception e){
            result.put("success", false);
            result.put("message", e.getMessage());
            return badRequest(result);
        }

        /* Save the users session */
        session("connected", request.email);
        return ok(result);
    }

    public static Result signUp(){
        /* Get the request body and deserialize it */
        SignUpRequest request = fromJson(request().body().asJson(), SignUpRequest.class);
        ObjectNode result = Json.newObject();

        /* Create a new user and fill in the data */
        User newUser = new User();

        try {
            newUser.setEmail(request.email);
            newUser.setPhoneNumber(request.phoneNumber);
            newUser.setFirstName(request.firstName);
            newUser.setHash(Password.getSaltedHash(request.password));

            newUser.save();
            result.put("success", true);
            result.put("message", "User created");
            session("connected", newUser.getEmail());
            return ok(result);
        }
        catch(Exception e){
            result.put("success", false);
            result.put("message", e.getMessage());
            return badRequest(result);
        }
    }

}
