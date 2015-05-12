package controllers;

import classes.*;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;
import com.avaje.ebean.Expr;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.*;
import org.apache.commons.validator.routines.EmailValidator;
import org.hibernate.validator.constraints.Email;
import play.Logger;
import play.libs.Json;
import play.mvc.*;
import support.mail.AWSMail;
import support.mail.AWSMailService;
import support.mail.Mail;
import support.misc.BroUtil;
import support.notification.AWSNotificationService;
import support.security.AuthenticateCookie;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;

/**
 * Created by steffen on 02/01/15.
 */
public class AccountController extends Controller{

    public static Result signIn(){
        /* Get the request body and deserialize it */
        SignInRequest request = fromJson(request().body().asJson(), SignInRequest.class);
        ObjectNode result = Json.newObject();

        if(!EmailValidator.getInstance().isValid(request.email)){
            result.put("success", false);
            result.put("message", "Invalid email address.");
            return badRequest(result);
        }

        User user = User.find.where().eq("email", request.email.toLowerCase()).findUnique();

        if(user == null){
            result.put("success", false);
            result.put("message", "Invalid email address.");
            return badRequest(result);
        }

        /* Check that the supplied password matches the hash from the user object */
        try{
            if(request.getPassword().equals("") || user.getHash().equals("")){
                throw new Exception("Need a PIN code? just press Request PIN");
            }
            else if(user.getStatus() == User.Status.BANNED){
                throw new Exception("Sorry, you have been banned.");
            }

            Password.check(request.password, user.getHash());
            user.setStatus(User.Status.ACTIVE);
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
        user.setHash("");
        user.save();
        return ok(result);
    }

    public static Result requestPIN(){
        NewPINRequest request = fromJson(request().body().asJson(), NewPINRequest.class);
        ObjectNode result = Json.newObject();
        if(!EmailValidator.getInstance().isValid(request.email)){
            result.put("success", false);
            result.put("message", "Invalid email address.");
            return badRequest(result);
        }
        User user = User.find.where().eq("email", request.email.toLowerCase()).findUnique();
        return ok();
    }

    public static Result signUp(){
        /* Get the request body and deserialize it */
        SignUpRequest request = fromJson(request().body().asJson(), SignUpRequest.class);
        ObjectNode result = Json.newObject();

        /* Create a new user and fill in the data */
        User newUser = new User();

        try {
            if(!EmailValidator.getInstance().isValid(request.getEmail())){
                throw new Exception("Please enter a valid email");
            }
            if(request.firstName.equals("") || request.lastName.equals("")){
                throw new Exception("Please enter first and last name");
            }

            String pinCode = String.valueOf(String.valueOf((int) (Math.random() * 9000) + 1000));

            newUser.setEmail(request.email.toLowerCase().trim());
            newUser.setFirstName(request.firstName.trim());
            newUser.setLastName(request.lastName.trim());
            newUser.setHash(Password.getSaltedHash(pinCode));
            newUser.setStatus(User.Status.INACTIVE);

            Session emailSession = new Session(newUser);

            Mail welcomeMail = new AWSMail();
            List<String> to = new ArrayList<String>();
            to.add("steffen@beckon.dk");
            welcomeMail.setFrom("steffen@broshout.net");
            welcomeMail.setTo(to);
            welcomeMail.setSubject("Welcome!");
            welcomeMail.setHtmlBody(views.html.mail.welcome_html.render(newUser.getFirstName(), pinCode).body());
            welcomeMail.setTextBody(views.html.mail.welcome_text.render(newUser.getFirstName(), pinCode).body());

            AWSMailService service = new AWSMailService();
            service.sendMail(welcomeMail);

            newUser.save();
            emailSession.save();

            result.put("success", true);
            result.put("message", "User created");

            return ok(result);
        }
        catch(Exception e){
            Logger.debug(e.getMessage());
            result.put("success", false);
            result.put("message", e.getMessage());
            return badRequest(result);
        }
    }

    @Security.Authenticated(AuthenticateCookie.class)
    public static Result registerDevice(){

        DeviceRegisterRequest request = fromJson(request().body().asJson(), DeviceRegisterRequest.class);
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

    @Security.Authenticated(AuthenticateCookie.class)
    public static Result check(){

        User user = (User) Http.Context.current().args.get("userObject");

        return ok(toJson(user));

    }

    @Security.Authenticated(AuthenticateCookie.class)
    public static Result getBadge(){

        User user = (User) Http.Context.current().args.get("userObject");

        ObjectNode result = Json.newObject();
        result.put("badge", BroUtil.getPendingFriendships(user) + BroUtil.getPendingShouts(user));

        return ok(result);

    }

}
