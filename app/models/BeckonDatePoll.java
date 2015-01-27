package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Steffen Rudkjøbing on 26/01/15.
 * © 2014 Steffen Rudkjøbing
 */
@Entity
public class BeckonDatePoll extends Model{

    @Id
    private Long id;

    @OneToOne
    private Beckon beckon;

    @OneToMany(cascade=CascadeType.ALL)
    private List<BeckonDatePollOption> options = new ArrayList<BeckonDatePollOption>();

    public Long getId() {
        return id;
    }

    public Beckon getBeckon() {
        return beckon;
    }

    public void setBeckon(Beckon beckon) {
        this.beckon = beckon;
    }
}
