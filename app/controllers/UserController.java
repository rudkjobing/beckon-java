package controllers;

import classes.SearchQuery;
import com.avaje.ebean.Expr;
import models.AuthenticateUser;
import models.User;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

import java.util.List;

import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;

/**
 * Created by Steffen Rudkjøbing on 10/01/15.
 * © 2014 Steffen Rudkjøbing
 */
public class UserController extends Controller{

    @Security.Authenticated(AuthenticateUser.class)
    public static Result search(){

        SearchQuery query = fromJson(request().body().asJson(), SearchQuery.class);
        User me = (User) Http.Context.current().args.get("userObject");
        String searchString = "%" + query.queryString + "%";
        List users = User.find.where().ilike("email", searchString).not(Expr.eq("id", me.getId())).findPagingList(10).getAsList();

        return ok(toJson(users));

    }

}
