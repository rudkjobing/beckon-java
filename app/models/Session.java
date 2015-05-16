package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Steffen Rudkjøbing on 03/01/15.
 * © 2014 Steffen Rudkjøbing
 */
@Entity
public class Session extends Model{

    public Session(User user){
        this.user = user;
        this.uuid = UUID.randomUUID() + "$" + user.getEmail();
    }

    @Id
    public Long id;
    @Column(unique=true,length = 191)
    public String uuid;
    @ManyToOne
    public User user;
    @Temporal(TemporalType.TIMESTAMP)
    public Date expires;

    public Long getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public User getUser() {
        return user;
    }

    public Date getExpires() {
        return expires;
    }

    public static Model.Finder<Long, Session> find = new Model.Finder<Long, Session>(
            Long.class, Session.class
    );

}
