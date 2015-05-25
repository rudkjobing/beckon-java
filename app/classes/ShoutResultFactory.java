package classes;

import models.ShoutMembership;
import models.Location;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Play on 19-04-2015.
 */
public class ShoutResultFactory {

    public static ShoutResult getShoutResult(ShoutMembership membership){

        ShoutResult result = new ShoutResult();

        result.role = membership.getRole();
        result.status = membership.getStatus();
        result.begins = membership.getShout().getBegins();
        result.id = membership.getShout().id;
        result.memberId = membership.id;
        result.location = membership.getShout().getLocation();
        result.begins = membership.getShout().getBegins();
        result.title = membership.getShout().getTitle();

        result.createMembers(membership.getShout());

        return result;

    }



}
