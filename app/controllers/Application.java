package controllers;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import models.User;
import models.SecurityContext;
import play.data.Form;
import play.mvc.*;
import views.html.*;
import java.util.List;

import static play.libs.Json.toJson;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render());
    }

    public static Result addPerson() {
//        String phoneNumber = Form.form().bindFromRequest().get("phoneNumber");
//        User p = new User();
//
//        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
//        try {
//            p.setRegion("DK");
//            p.setPhoneNumber(phoneNumber);
//        } catch (Exception e) {
//            return badRequest(error.render(e.getMessage()));
//        }
//        SecurityContext token = new SecurityContext();
//        token.setAutherizedUser(p);
//        p.getSecurityContexts().add(token);
//        p.save();
//        token.save();
        return redirect(routes.Application.people());

    }

    public static Result people(){
        List<User> people = User.find.all();
        return ok(toJson(people));
    }

}
