package models;

import play.db.ebean.Model;

import javax.persistence.*;

/**
 * Created by Steffen Rudkjøbing on 27/01/15.
 * © 2014 Steffen Rudkjøbing
 */
@Entity
public class Vote extends Model {

    public enum Choice{YES, NO, MAYBE, BLANK, UNDECIDED};

    @Id
    private Long id;

    @ManyToOne(cascade=CascadeType.ALL)
    private User voter;

    @Enumerated(EnumType.STRING)
    private Choice vote = Choice.UNDECIDED;

    public Long getId() {
        return id;
    }

    public User getVoter() {
        return voter;
    }

    public void setVoter(User voter) {
        this.voter = voter;
    }

    public Choice getVote() {
        return vote;
    }

    public void setVote(Choice vote) {
        this.vote = vote;
    }
}
