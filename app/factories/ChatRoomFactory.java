package factories;

import classes.ChatRoomCreateRequest;
import com.avaje.ebean.Expr;
import models.ChatRoom;
import models.ChatRoomMember;
import models.Friendship;
import models.User;

import java.util.List;

/**
 * Created by Play on 6/16/2015.
 */
public class ChatRoomFactory{

    public static ChatRoom getChatRoom(User user, List<Friendship> friendships) throws Exception{

        ChatRoom chatRoom = new ChatRoom();

        chatRoom.save();

        ChatRoomMember member = new ChatRoomMember();
        member.setUser(user);
        member.setChatRoom(chatRoom);
        member.save();

        for(Friendship friend : friendships) {
            member = new ChatRoomMember();
            member.setUser(friend.getFriend());
            member.save();
        }

        chatRoom.save();

        return chatRoom;

    }

}
