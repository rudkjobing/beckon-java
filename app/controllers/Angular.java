package controllers;

import models.User;
import models.SecurityContext;
import play.mvc.*;
import views.html.*;

import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;

/**
 * Created by steffen on 22/12/14.
 */
public class Angular extends Controller {

//    public static Result signIn() {
////        Person autherizedPerson =
//        SecurityContext securityContext = fromJson(request().body().asJson(), SecurityContext.class);
//
//        SecurityContext storedSecurityContext = SecurityContext.find.where().eq("hash", securityContext.getHash()).findUnique();
//
//        if(securityContext.getAttempt().equals(storedSecurityContext.getPinCode())){
//            storedSecurityContext.generateCookie();
//            storedSecurityContext.save();
//            response().setCookie("cookie", storedSecurityContext.getCookie(), 3600, "/");
//            return ok(storedSecurityContext.getHash());
//        }
//        return badRequest();
//    }
//
    public static Result index() {
        return ok(index.render());
    }
//
//    public static Result getSecurityContext(){
//
//        String phoneNumber = request().body().asJson().get("phoneNumber").textValue();
//        User autherizedUser = User.find.where().eq("phoneNumber", phoneNumber).findUnique();
//        SecurityContext s = new SecurityContext();
//        s.setAutherizedUser(autherizedUser);
//        s.setPinCode("4942");
//        s.save();
//
//        return ok(toJson(s));
//
//    }
//
//    public static Result signUp(){
//
//        try{
//            User newUser = fromJson(request().body().asJson(), User.class);
//            newUser.save();
//
//            SecurityContext securityContext = new SecurityContext();
//            securityContext.setAutherizedUser(newUser);
//            securityContext.setPinCode("1234");
//            securityContext.save();
//            return ok(toJson(securityContext));
//        }
//        catch(Exception e){
//            return badRequest(e.getMessage());
//        }
//
//    }

}
