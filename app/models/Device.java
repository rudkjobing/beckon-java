package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by steffen on 26/12/14.
 */

@Entity
public class Device extends Model{

    public enum Type {APPLE, ANDROID, WINDOWS};

    @Id
    private Long id;
    private String arn;
    private String uuid;

    @ManyToOne
    private User owner;
    @Enumerated(EnumType.STRING)
    private Type type;
    @Temporal(TemporalType.TIMESTAMP)
    private Date firstRegistered;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastRegistered;

    public Long getId() {
        return id;
    }

    public String getArn() {
        return arn;
    }

    public void setArn(String arn) {
        this.arn = arn;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Date getFirstRegistered() {
        return firstRegistered;
    }

    public void setFirstRegistered(Date firstRegistered) {
        this.firstRegistered = firstRegistered;
    }

    public Date getLastRegistered() {
        return lastRegistered;
    }

    public void setLastRegistered(Date lastRegistered) {
        this.lastRegistered = lastRegistered;
    }

    public static Model.Finder<Long,Device> find = new Model.Finder<Long,Device>(
            Long.class, Device.class
    );
}
