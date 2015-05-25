package classes;

import models.Location;
import models.Shout;
import models.ShoutMembership;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Play on 5/25/2015.
 */
public class ShoutResult {

    public ShoutMembership.Status status;
    public ShoutMembership.Role role;
    public Long id;
    public Long memberId;
    public String title;
    public Location location;
    public Date begins;
    public List<Member> memberList = new ArrayList<>();

    public void createMembers(Shout shout){
        for(ShoutMembership otherMember : shout.getMembers()){
            this.memberList.add(
                    new ShoutResult.Member(
                            otherMember.id,
                            otherMember.getUser().getFirstName(),
                            otherMember.getUser().getLastName(),
                            otherMember.getStatus(),
                            otherMember.getRole()
                    )
            );
        }
    }

    public class Member{

        public Member(Long id, String firstName, String lastName, ShoutMembership.Status status, ShoutMembership.Role role){
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.status = status;
            this.role = role;
        }

        public Long id;
        public String firstName;
        public String lastName;
        public ShoutMembership.Status status;
        public ShoutMembership.Role role;
    }

}
