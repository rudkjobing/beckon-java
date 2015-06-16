package controllers;

import classes.ChatRoomCreateRequest;
import classes.ShoutCreateRequest;
import com.fasterxml.jackson.databind.node.ObjectNode;
import factories.ChatRoomFactory;
import models.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import support.misc.BroUtil;
import support.notification.AWSNotification;
import support.notification.AWSNotificationService;
import support.notification.Notification;
import support.notification.NotificationService;
import support.security.AuthenticateCookie;

import java.util.List;

import static play.libs.Json.fromJson;

/**
 * Created by Play on 6/16/2015.
 */
public class ChatRoomController extends Controller {

    @Security.Authenticated(AuthenticateCookie.class)
    public static Result createChatRoom(){

        ChatRoomCreateRequest chatRoomCreateRequest = fromJson(request().body().asJson(), ChatRoomCreateRequest.class);
        User user = (User) Http.Context.current().args.get("userObject");

        ObjectNode result = Json.newObject();

        try{
            List<Friendship> members = chatRoomCreateRequest.members;
            for(Friendship friend : members) {
                friend.refresh();
            }
            ChatRoomFactory.getChatRoom(user, members);
        }
        catch(Exception e){
            result.put("success", false);
            result.put("message", e.getMessage());
            return badRequest(result);
        }

        return ok();

    }

}
