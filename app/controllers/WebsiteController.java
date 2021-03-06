package controllers;

import com.avaje.ebean.Expr;
import models.SupportRequest;
import models.User;
import org.apache.commons.validator.routines.EmailValidator;
import play.mvc.Controller;
import play.mvc.Result;
import support.mail.AWSMail;
import support.mail.AWSMailService;
import support.mail.Mail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static play.data.Form.form;

/**
 * Created by Play on 5/31/2015.
 */
public class WebsiteController extends Controller{

    public static Result index(){
        String text = "Submit any questions you may have below, and we will get back to you.";
        String errorText = "";
        String email = form().bindFromRequest().get("email") == null ? "" : form().bindFromRequest().get("email");
        String message = form().bindFromRequest().get("message") == null ? "" : form().bindFromRequest().get("message");



        if(email.equals("") && message.equals("")){
            return ok(views.html.web.index.render(text, errorText, email, message, ""));
        }
        else if((email.equals("") ^ message.equals("")) || !EmailValidator.getInstance().isValid(email)){
            errorText = email.equals("") ? "Please provide a valid email"
                    : message.equals("") ? "Please provide a message"
                    : "Please provide a valid email";
            return badRequest(views.html.web.index.render(text, errorText, email, message, ""));
        }

        SupportRequest request = new SupportRequest();
        request.setEmail(email);
        request.setMessage(message);
        request.setPosted(new Date());
        request.setHandled(false);

        String userName = "";

        User user = User.find.where(Expr.eq("email", email)).findUnique();
        if(user != null){
            request.setUser(user);
            userName = "Dear " + user.getFirstName() + ". ";
        }

        request.save();

        Mail mail = new AWSMail();

        List<String> to = new ArrayList<>();
        to.add("steffen@broshout.net");
        mail.setFrom("SupportBot@broshout.net");
        mail.setTo(to);
        mail.setSubject(email);
        mail.setHtmlBody(message);
        mail.setTextBody(message);

        AWSMailService service = new AWSMailService();
        service.sendMail(mail);

        text = userName + "Thanks for your message, we will email you asap :-)";
        email = "";
        message = "";
        return ok(views.html.web.index.render(text, errorText, email, message, "disabled"));
    }

}
