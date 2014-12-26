package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.UUID;

/**
 * Created by steffen on 22/12/14.
 */
@Entity
public class SecurityContext extends Model {

    public SecurityContext(){
        this.hash = UUID.randomUUID().toString();
    }

    @Id
    public Long id;
    public String hash;
    public String attempt = "";
    @JsonIgnore
    @ManyToOne
    private User autherizedUser;
    @JsonIgnore
    private String pinCode = "8888";
    @JsonIgnore
    private String cookie;


    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public Long getId() {
        return id;
    }

    public String getCookie() {
        return cookie;
    }

    public String getAttempt() {
        return attempt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public User getAutherizedUser() {
        return autherizedUser;
    }

    public void generateCookie(){
        this.cookie = this.autherizedUser.getId().toString() + UUID.randomUUID().toString();
    }

    public void setAutherizedUser(User autherizedUser) {
        this.autherizedUser = autherizedUser;
    }

    public static Finder<Long,SecurityContext> find = new Finder<Long,SecurityContext>(
            Long.class, SecurityContext.class
    );

}
