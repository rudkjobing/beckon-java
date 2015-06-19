package models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by Play on 5/25/2015.
 */
@Entity
public class ChatRoomMember extends Model{

    @Id
    @JsonIgnore
    private Long id;

    @ManyToOne
    private User user;

    @JsonIgnore
    @ManyToOne
    private ChatRoom chatRoom;

    private int unreadMessages;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    public int getUnreadMessages() {
        return unreadMessages;
    }

    public void setUnreadMessages(int unreadMessages) {
        this.unreadMessages = unreadMessages;
    }

    public static Model.Finder<Long,ChatRoomMember> find = new Model.Finder<Long,ChatRoomMember>(
            Long.class, ChatRoomMember.class
    );
}
