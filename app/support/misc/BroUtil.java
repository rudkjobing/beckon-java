package support.misc;

import com.avaje.ebean.Expr;
import models.Friendship;
import models.ShoutMembership;
import models.User;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

/**
 * Created by Play on 5/9/2015.
 */
public class BroUtil {

    public static int getPendingFriendships(User user){
        return Friendship.find.where()
                .and(
                        Expr.eq("owner", user),
                        Expr.eq("status", Friendship.Status.PENDING)
                ).findRowCount();
    }

    public static int getPendingShouts(User user) {

        Date nowMinusHours = DateUtils.addHours(new Date(), -2);

        return ShoutMembership.find.where()
                .and(
                        Expr.eq("user", user),
                        Expr.and(
                                Expr.gt("shout.begins", nowMinusHours),
                                Expr.eq("status", ShoutMembership.Status.INVITED)
                        )
                ).findRowCount();
    }
}
