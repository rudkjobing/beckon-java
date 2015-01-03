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
    @OneToOne
    public Friendship peer;
    @Enumerated(EnumType.STRING)
    public Status status;

    /**
     * Getters and Setters
     */

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Friendship getPeer() {
        return peer;
    }

    public void setPeer(Friendship peer) {
        this.peer = peer;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

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
