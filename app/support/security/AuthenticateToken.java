package support.security;

import models.Session;
import models.User;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Created by Play on 5/3/2015.
 */
public class AuthenticateToken extends Security.Authenticator {

    @Override
    public String getUsername(Http.Context ctx) {
        if(ctx.request().getQueryString("token").isEmpty()){
            return null;
        }

        String token = ctx.request().getQueryString("token");
        Session s = Session.find.where().eq("uuid", token).findUnique();
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