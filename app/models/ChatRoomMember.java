package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by Play on 5/25/2015.
 */
@Entity
public class ChatRoomMember {

    @Id
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private ChatRoom chatRoom;

}
