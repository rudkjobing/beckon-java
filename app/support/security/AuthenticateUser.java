package support.security;

import models.Session;
import models.User;
import play.libs.F;
import play.mvc.*;

/**
 * Created by Steffen Rudkjøbing on 03/01/15.
 * © 2014 Steffen Harbom Rudkjøbing
 */
public class AuthenticateUser extends Security.Authenticator {

    @Override
    public String getUsername(Http.Context ctx) {
        if(ctx.request().cookie("uuid") == null){
            return null;
        }

        String userUUID = ctx.request().cookie("uuid").value();
        Session s = Session.find.where().eq("uuid", userUUID).findUnique();
        if(s != null){
            User u = s.getUser();
            ctx.args.put("userObject", u);
            return u.getEmail();
        }
        return null;
    }

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        return unauthorized();
    }

}
