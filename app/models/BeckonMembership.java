package models;

import com.avaje.ebean.annotation.EnumMapping;

import javax.persistence.*;

/**
 * Created by steffen on 26/12/14.
 */

@Entity
public class BeckonMembership {

    public enum Status {INVITED, ACCEPTED, DECLINED};
    public enum Role {CREATOR, MEMBER, ADMIN}

    @Id
    public Long id;
    @ManyToOne
    public Beckon beckon;
    @ManyToOne
    public User user;
    @Enumerated(EnumType.STRING)
    public Status status;
    @Enumerated(EnumType.STRING)
    public Role role;

}

