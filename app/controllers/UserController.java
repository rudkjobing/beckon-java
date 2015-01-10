package controllers;

import classes.SearchQuery;
import models.AuthenticateUser;
import models.User;
import play.mvc.Controller;
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
        String searchString = "%" + query.searchString + "%";
        List users = User.find.where().ilike("email", searchString).findList();

        return ok(toJson(users));

    }

}
