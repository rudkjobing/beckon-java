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

        ShoutResult resultObject = new ShoutResult();

        resultObject.status = membership.getStatus();
        resultObject.begins = membership.getShout().getBegins();
        resultObject.id = membership.getShout().id;
        resultObject.memberId = membership.id;
        resultObject.location = membership.getShout().getLocation();
        resultObject.begins = membership.getShout().getBegins();
        resultObject.title = membership.getShout().getTitle();

        resultObject.createMembers(membership.getShout());

        return resultObject;

    }



}
