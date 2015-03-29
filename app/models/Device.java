package models;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by steffen on 26/12/14.
 */

@Entity
public class Device {

    public enum Type {APPLE, ANDROID, WINDOWS};

    @Id
    public Long id;
    public String arn;
    @ManyToOne
    public User owner;
    @Enumerated(EnumType.STRING)
    public Type type;
    @Temporal(TemporalType.TIMESTAMP)
    public Date firstRegistered;
    @Temporal(TemporalType.TIMESTAMP)
    public Date lastRegistered;

    public Long getId() {
        return id;
    }

    public String getArn() {
        return arn;
    }

    public void setArn(String arn) {
        this.arn = arn;
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
}
