package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Play on 5/25/2015.
 */
@Entity
public class ChatRoomMessage extends Model {

    @Id
    private Long id;
    private String message;
    @Temporal(TemporalType.TIMESTAMP)
    public Date posted;
    @ManyToOne
    private ChatRoom chatRoom;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getPosted() {
        return posted;
    }

    public void setPosted(Date posted) {
        this.posted = posted;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }
}
