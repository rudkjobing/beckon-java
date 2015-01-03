package models;

import com.avaje.ebean.annotation.EnumMapping;
import play.db.ebean.Model;

import javax.persistence.*;

/**
 * Created by steffen on 26/12/14.
 */

@Entity
public class BeckonMembership extends Model{

    public enum Status {INVITED, ACCEPTED, DECLINED};
    public enum Role {CREATOR, MEMBER, ADMIN}

    @Id
    public Long id;
    @ManyToOne(cascade=CascadeType.ALL)
    public Beckon beckon;
    @ManyToOne(cascade=CascadeType.ALL)
    public User user;
    @Enumerated(EnumType.STRING)
    public Status status;
    @Enumerated(EnumType.STRING)
    public Role role;

    public Beckon getBeckon() {
        return beckon;
    }

    public void setBeckon(Beckon beckon) {
        this.beckon = beckon;
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
}

