package models;

import play.libs.F;
import play.mvc.*;

/**
 * Created by Steffen Rudkjøbing on 03/01/15.
 * © 2014 Steffen Rudkjøbing
 */
public class AuthenticateUser extends Security.Authenticator {

    @Override
    public String getUsername(Http.Context ctx) {
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
        return unauthorized(ctx.request().cookie("uuid").value());
    }

}
