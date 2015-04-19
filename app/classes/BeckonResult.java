package classes;

import models.BeckonMembership;
import models.Location;
import models.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Play on 19-04-2015.
 */
public class BeckonResult{

    public ArrayList<ShoutResultObject> beckons = new ArrayList<ShoutResultObject>();

    public void addBeckon(BeckonMembership m){

        ShoutResultObject s = new ShoutResultObject();

        s.accepted = m.getStatus() == BeckonMembership.Status.ACCEPTED;
        s.begins = m.getBeckon().getBegins();
        s.id = m.getBeckon().id;
        s.location = m.getBeckon().getLocation();
        s.begins = m.getBeckon().getBegins();
        s.title = m.getBeckon().getTitle();

        for(BeckonMembership u : m.getBeckon().getMembers()){
            s.memberCount++;
            if(u.getStatus() == BeckonMembership.Status.ACCEPTED){
                s.acceptedCount ++;
                if(s.acceptedMemberList.length() < 40 && s.acceptedMemberList.length() > 0){
                    s.acceptedMemberList = s.acceptedMemberList + ", " + u.getUser().getFirstName();
                }
                else if(s.acceptedMemberList.length() < 40){
                    s.acceptedMemberList = s.acceptedMemberList + u.getUser().getFirstName();
                }
                else{
                    s.acceptedMemberList = s.acceptedMemberList + "...";
                }
            }
            else if(u.getStatus() == BeckonMembership.Status.DECLINED){
                s.declinedCount++;
            }
            else if(u.getStatus() == BeckonMembership.Status.MAYBE){
                s.maybeCount++;
            }
        }

        this.beckons.add(s);

    }

    private class ShoutResultObject{

        public boolean accepted;
        public Long id;
        public String title;
        public Location location;
        public Date begins;
        public int memberCount = 0;
        public int acceptedCount = 0;
        public int declinedCount = 0;
        public int maybeCount = 0;
        public String acceptedMemberList = "";

    }

}
