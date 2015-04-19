package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by steffen on 26/12/14.
 */

@Entity
public class Shout extends Model{

    @Id
    public Long id;
    public String title;
    public String description;
    @OneToMany(cascade=CascadeType.ALL)
    @JsonIgnore
    public List<ShoutMembership> members = new ArrayList<ShoutMembership>();
    @Temporal(TemporalType.TIMESTAMP)
    public Date begins;
    @OneToOne(cascade=CascadeType.ALL,fetch = FetchType.EAGER)
    public Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ShoutMembership> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<ShoutMembership> members) {
        this.members = members;
    }

    public Date getBegins() {
        return begins;
    }

    public void setBegins(Date begins) {
        this.begins = begins;
    }
}
