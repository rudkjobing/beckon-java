package models;

import play.db.ebean.Model;

import javax.persistence.*;

/**
 * Created by steffen on 20/12/14.
 */

@Entity
public class Friendship extends Model{

    public enum Status {INVITED, ACCEPTED, DECLINED, BLOCKED, DELETED};

    /**
     * Properties
     */

    @Id
    public Long id;
    public String nickname;
    @ManyToOne
    public User owner;
    @ManyToOne
    public User friend;
    @Enumerated(EnumType.STRING)
    public Status status;

    /**
     * Getters and Setters
     */

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static Finder<Long,Friendship> find = new Finder<Long,Friendship>(
            Long.class, Friendship.class
    );

}
