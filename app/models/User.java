package models;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by steffen on 20/12/14.
 */

@Entity
public class User extends Model {

    /**
     * Properties
     */

    @Id
    public Long id;
    public String firstName;
    public String lastName;
    public String region = "DK";
    @Column(unique=true)
    private String phoneNumber;
    @Column(unique=true)
    private String email;
    @OneToMany(mappedBy = "owner")
    public List<Friendship> friendships = new ArrayList<Friendship>();
    @ManyToMany(cascade = CascadeType.ALL)
    public List<BeckonMembership> beckons = new ArrayList<BeckonMembership>();
    @OneToMany(mappedBy = "owner")
    public List<Device> devices = new ArrayList<Device>();
    public String hash;

    /**
     * Getters And Setters
     */

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Friendship> getFriendships() {
        return friendships;
    }

    public void setFriendships(List<Friendship> friendships) {
        this.friendships = friendships;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) throws Exception{
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try{
            Phonenumber.PhoneNumber p = phoneUtil.parse(phoneNumber, this.region);
            this.phoneNumber = phoneUtil.format(p, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
        }
        catch(Exception e){
            throw e;
        }
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public List<BeckonMembership> getBeckons() {
        return beckons;
    }

    public void setBeckons(List<BeckonMembership> beckons) {
        this.beckons = beckons;
    }

    /**
     * Ebean
     */

    public static Finder<Long, User> find = new Finder<Long, User>(
            Long.class, User.class
    );

 }
