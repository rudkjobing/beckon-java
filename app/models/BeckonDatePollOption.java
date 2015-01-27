package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Steffen Rudkjøbing on 27/01/15.
 * © 2014 Steffen Rudkjøbing
 */
@Entity
public class BeckonDatePollOption extends Model{

    @Id
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOption;

    @OneToMany(cascade=CascadeType.ALL)
    private List<Vote> voters = new ArrayList<Vote>();

    public Long getId() {
        return id;
    }

    public Date getDateOption() {
        return dateOption;
    }

    public void setDateOption(Date dateOption) {
        this.dateOption = dateOption;
    }

    public List<Vote> getVoters() {
        return voters;
    }

    public void setVoters(List<Vote> voters) {
        this.voters = voters;
    }
}
