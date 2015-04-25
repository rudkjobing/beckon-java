package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

/**
 * Created by steffen on 26/12/14.
 */

@Entity
public class Location {

    @Id
    public Long id;
    public String name;
    public double latitude;
    public double longitude;

}
