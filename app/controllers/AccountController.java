package controllers;

import classes.Password;
import classes.RegisterDeviceRequest;
import classes.SignInRequest;
import classes.SignUpRequest;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;
import com.avaje.ebean.Expr;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.*;
import play.libs.Json;
import play.mvc.*;
import support.notification.AWSNotificationService;
import support.security.AuthenticateUser;

import java.util.Date;

import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;

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
        Session s = new Session(user);
        response().setCookie("uuid", s.getUuid(), 60 * 60 * 24 * 30);
        s.save();
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
            newUser.setLastName(request.lastName);
            newUser.setHash(Password.getSaltedHash(request.password));

            newUser.save();
            result.put("success", true);
            result.put("message", "User created");
            return ok(result);
        }
        catch(Exception e){
            result.put("success", false);
            result.put("message", e.getMessage());
            return badRequest(result);
        }
    }

    @Security.Authenticated(AuthenticateUser.class)
    public static Result registerDevice(){

        RegisterDeviceRequest request = fromJson(request().body().asJson(), RegisterDeviceRequest.class);
        User user = (User) Http.Context.current().args.get("userObject");

        Device device = Device.find.where()
                .and(
                        Expr.and(
                                Expr.eq("uuid", request.uuid),
                                Expr.eq("type", request.type)
                        ),
                        Expr.eq("owner", user)
                ).findUnique();

        if(device == null){
            device = new Device();
            device.setOwner(user);
            device.setType(request.type);
            device.setUuid(request.uuid);
            device.setFirstRegistered(new Date());
            device.setLastRegistered(new Date());

            AWSNotificationService service = new AWSNotificationService();
            CreatePlatformEndpointResult arn = service.createEndpoint(device.getUuid());

            device.setArn(arn.getEndpointArn());
            device.save();

            return ok(toJson(device));
        }
        else{
            device.setLastRegistered(new Date());
            device.save();
        }

        return ok(toJson(device));

    }

    @Security.Authenticated(AuthenticateUser.class)
    public static Result check(){

        User user = (User) Http.Context.current().args.get("userObject");

        return ok(toJson(user));
    }

}
