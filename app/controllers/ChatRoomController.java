package controllers;

import classes.ChatRoomCreateRequest;
import classes.ChatRoomSendMessageRequest;
import com.avaje.ebean.Expr;
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

import java.util.Date;
import java.util.List;

import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;

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

    @Security.Authenticated(AuthenticateCookie.class)
    public static Result sendChatRoomMessage(Long id){

        ChatRoomSendMessageRequest chatRoomSendMessageRequest = fromJson(request().body().asJson(), ChatRoomSendMessageRequest.class);
        User user = (User) Http.Context.current().args.get("userObject");

        ChatRoomMember chatRoomMember = ChatRoomMember.find.where()
                .and(
                        Expr.eq("user", user),
                        Expr.eq("id", id)
                ).findUnique();

        if(chatRoomMember == null){
            return badRequest("Chatroom not found");
        }

        ChatRoom chatRoom = chatRoomMember.getChatRoom();

        chatRoom.refresh();

        ChatRoomMessage message = new ChatRoomMessage();
        message.setChatRoom(chatRoom);
        message.setMessage(chatRoomSendMessageRequest.message);
        message.setPosted(new Date());
        message.setPoster(chatRoomMember);
        message.save();

        NotificationService service = new AWSNotificationService();
        for(ChatRoomMember member : chatRoom.getMembers()){
            if(member.getUser().equals(user)){
                continue;
            }

            member.setUnreadMessages(member.getUnreadMessages() + 1);
            member.save();

            Notification notification = new AWSNotification()
                    .setEndpoints(member.getUser().getDevices())
                    .setMessage(user.getFirstName() + " " + user.getLastName() + ": " + message.getMessage())
                    .setBadge(BroUtil.getPendingFriendships(member.getUser()) + BroUtil.getPendingShouts(member.getUser()));
            service.addNotification(notification);
        }
        service.publish();

        return ok();
    }

    @Security.Authenticated(AuthenticateCookie.class)
    public static Result getChatRoomMessages(Long id){

        User user = (User) Http.Context.current().args.get("userObject");

        ChatRoomMember chatRoom = ChatRoomMember.find.where()
                .and(
                        Expr.eq("user", user),
                        Expr.eq("id", id)
                ).findUnique();

        if(chatRoom == null){
            return badRequest("Chatroom not found");
        }

        return ok(toJson(chatRoom.getChatRoom().getMessages().toArray()));
    }

}
