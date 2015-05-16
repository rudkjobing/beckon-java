package models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.apache.commons.validator.routines.EmailValidator;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by steffen on 20/12/14.
 */

@Entity
public class User extends Model {

    /**
     * Properties
     */

    public enum Status {INACTIVE, ACTIVE, BANNED};

    @Id
    public Long id;
    private String firstName;
    private String lastName;
    private String region = "DK";
//    @Column(unique=true)
//    private String phoneNumber;
    @Column(unique=true,length = 191)
    private String email;
    @OneToMany(mappedBy = "owner",cascade=CascadeType.ALL)
    @JsonIgnore
    private List<Friendship> friendships = new ArrayList<Friendship>();
    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ShoutMembership> beckons = new ArrayList<ShoutMembership>();
    @OneToMany(mappedBy = "owner", cascade=CascadeType.ALL)
    @JsonIgnore
    private List<Device> devices = new ArrayList<Device>();
    @JsonIgnore
    private String hash;
    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private Status status;

    /**
     * Getters And Setters
     */

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws Exception{
        if(!EmailValidator.getInstance().isValid(email)){
            throw new Exception("Sorry invalid email :(");
        }
        else{
            this.email = email;
        }
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

//    public String getPhoneNumber() {
//        return this.phoneNumber;
//    }
//
//    public void setPhoneNumber(String phoneNumber) throws Exception{
//        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
//        try{
//            Phonenumber.PhoneNumber p = phoneUtil.parse(phoneNumber, this.region);
//            this.phoneNumber = phoneUtil.format(p, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
//        }
//        catch(Exception e){
//            throw new Exception("Sorry invalid phonenumber :(");
//        }
//    }

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

    public List<ShoutMembership> getBeckons() {
        return beckons;
    }

    public void setBeckons(List<ShoutMembership> beckons) {
        this.beckons = beckons;
    }

    /**
     * Ebean
     */

    public static Finder<Long, User> find = new Finder<Long, User>(
            Long.class, User.class
    );

 }
