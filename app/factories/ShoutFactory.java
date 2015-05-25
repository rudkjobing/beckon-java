package factories;

import classes.ShoutAddRequest;
import models.Friendship;
import models.Shout;
import models.ShoutMembership;
import models.User;
import org.apache.commons.lang3.time.DateUtils;
import play.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Steffen H. Rudkjøbing on 5/25/2015.
 */
public class ShoutFactory {

    public static Shout getShout(User user, ShoutAddRequest request) throws Exception{

        if(request.title.equals("")){
            throw new Exception("Title can not be empty");
        }

        Shout shout = new Shout();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

        Date date = formatter.parse(request.begins);
        if (date.before(DateUtils.addHours(new Date(), -2))){
            throw new Exception("Date is too far in the past");
        }
        shout.setBegins(date);

        shout.setTitle(request.title.trim());

        if (request.location.name == null){
            request.location.name = "";
        }
        else{
            request.location.name = request.location.name.trim();
        }

        shout.setLocation(request.location);
        shout.save();

        ShoutMembership member = new ShoutMembership();
        member.setUser(user);
        member.setShout(shout);
        shout.getMembers().add(member);
        member.setRole(ShoutMembership.Role.CREATOR);
        member.setStatus(ShoutMembership.Status.ACCEPTED);
        user.getBeckons().add(member);
        member.save();

        for(Friendship friend : request.members) {
            friend.refresh();
            Logger.debug(friend.getNickname());
            Logger.debug(friend.getFriend().getEmail());
            member = new ShoutMembership();
            member.setUser(friend.getFriend());
            member.setShout(shout);
            shout.getMembers().add(member);
            member.setRole(ShoutMembership.Role.MEMBER);
            member.setStatus(ShoutMembership.Status.INVITED);
            friend.getOwner().getBeckons().add(member);
            member.save();
        }

        shout.save();

        return shout;
    }

}
