package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Play on 6/2/2015.
 */
@Entity
public class SupportRequest extends Model{

    @Id
    private Long id;
    private String email;

    @Lob
    private String message;
    private Boolean handled = false;

    @Temporal(TemporalType.TIMESTAMP)
    private Date posted;

    @ManyToOne
    private User user;

    public Boolean isHandled() {
        return handled;
    }

    public void setHandled(Boolean handled) {
        this.handled = handled;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getPosted() {
        return posted;
    }

    public void setPosted(Date posted) {
        this.posted = posted;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
