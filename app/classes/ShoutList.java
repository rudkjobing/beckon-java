package classes;

import models.ShoutMembership;
import models.Location;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Play on 19-04-2015.
 */
public class ShoutList {

    public ArrayList<ShoutResultObject> shouts = new ArrayList<ShoutResultObject>();

    public void addShout(ShoutMembership membership){

        ShoutResultObject resultObject = new ShoutResultObject();

        resultObject.status = membership.getStatus();
        resultObject.begins = membership.getShout().getBegins();
        resultObject.id = membership.getShout().id;
        resultObject.memberId = membership.id;
        resultObject.location = membership.getShout().getLocation();
        resultObject.begins = membership.getShout().getBegins();
        resultObject.title = membership.getShout().getTitle();

        for(ShoutMembership otherMember : membership.getShout().getMembers()){
            resultObject.memberList.add(
                    new Member(
                            otherMember.getUser().getFirstName(),
                            otherMember.getStatus(),
                            otherMember.getRole()
                    )
            );
        }

        this.shouts.add(resultObject);

    }

    private class ShoutResultObject{

        public ShoutMembership.Status status;
        public String creatorName;
        public Long id;
        public Long memberId;
        public String title;
        public Location location;
        public Date begins;
        public List<Member> memberList = new ArrayList<>();

    }

    private class Member{

        public Member(String name, ShoutMembership.Status status, ShoutMembership.Role role){
            this.name = name;
            this.status = status;
            this.role = role;
        }

        public String name;
        public ShoutMembership.Status status;
        public ShoutMembership.Role role;
    }

}
