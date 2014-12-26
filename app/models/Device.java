package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * Created by steffen on 26/12/14.
 */

@Entity
public class Device {

    @Id
    public Long id;
    public String arn;
    @ManyToOne
    public User owner;


}
