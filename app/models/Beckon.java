package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by steffen on 26/12/14.
 */

@Entity
public class Beckon {

    @Id
    public Long id;
    public String title;
    public String description;
    @OneToMany
    public List<BeckonMembership> members = new ArrayList<BeckonMembership>();
    @Temporal(TemporalType.TIMESTAMP)
    public Date starts;
    @Temporal(TemporalType.TIMESTAMP)
    public Date ends;
    @ManyToOne
    public Location location;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<BeckonMembership> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<BeckonMembership> members) {
        this.members = members;
    }

    public Date getStarts() {
        return starts;
    }

    public void setStarts(Date starts) {
        this.starts = starts;
    }

    public Date getEnds() {
        return ends;
    }

    public void setEnds(Date ends) {
        this.ends = ends;
    }

    public static Model.Finder<Long, Beckon> find = new Model.Finder<Long, Beckon>(
            Long.class, Beckon.class
    );

}
