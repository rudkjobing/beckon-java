package classes;

import models.ShoutMembership;
import models.Location;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Play on 19-04-2015.
 */
public class ShoutList {

    public ArrayList<ShoutResultObject> beckons = new ArrayList<ShoutResultObject>();

    public void addBeckon(ShoutMembership m){

        ShoutResultObject s = new ShoutResultObject();

        s.status = m.getStatus();
        s.begins = m.getShout().getBegins();
        s.id = m.getShout().id;
        s.memberId = m.id;
        s.location = m.getShout().getLocation();
        s.begins = m.getShout().getBegins();
        s.title = m.getShout().getTitle();

        for(ShoutMembership u : m.getShout().getMembers()){
            s.memberCount++;
            if(u.getStatus() == ShoutMembership.Status.ACCEPTED){
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
            else if(u.getStatus() == ShoutMembership.Status.DECLINED){
                s.declinedCount++;
            }
            else if(u.getStatus() == ShoutMembership.Status.MAYBE){
                s.maybeCount++;
            }
            if(u.getRole() == ShoutMembership.Role.CREATOR){
                s.createrName = u.getUser().getFirstName();
            }
        }

        this.beckons.add(s);

    }

    private class ShoutResultObject{

        public ShoutMembership.Status status;
        public String createrName;
        public Long id;
        public Long memberId;
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
