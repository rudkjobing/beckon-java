package models;

import play.db.ebean.Model;

import javax.persistence.*;

/**
 * Created by steffen on 26/12/14.
 */

@Entity
public class ShoutMembership extends Model{

    public enum Status {INVITED, ACCEPTED, MAYBE, DECLINED, DELETED};
    public enum Role {CREATOR, MEMBER, ADMIN}

    @Id
    public Long id;
    @ManyToOne
    public Shout shout;
    @ManyToOne
    public User user;
    @Enumerated(EnumType.STRING)
    public Status status;
    @Enumerated(EnumType.STRING)
    public Role role;

    public Shout getShout() {
        return shout;
    }

    public void setShout(Shout shout) {
        this.shout = shout;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public static Model.Finder<Long,ShoutMembership> find = new Model.Finder<Long,ShoutMembership>(
            Long.class, ShoutMembership.class
    );
}
