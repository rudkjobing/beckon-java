package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Play on 5/25/2015.
 */
@Entity
public class ChatRoom {

    @Id
    private Long id;
    @OneToMany
    private List<ChatRoomMessage> messages = new ArrayList<>();
    @OneToMany
    private List<ChatRoomMember> members = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public List<ChatRoomMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatRoomMessage> messages) {
        this.messages = messages;
    }

    public List<ChatRoomMember> getMembers() {
        return members;
    }

    public void setMembers(List<ChatRoomMember> members) {
        this.members = members;
    }
}
