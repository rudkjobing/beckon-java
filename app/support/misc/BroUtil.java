package support.misc;

import com.avaje.ebean.Expr;
import models.Friendship;
import models.ShoutMembership;
import models.User;

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
        return ShoutMembership.find.where()
                .and(
                        Expr.eq("user", user),
                        Expr.eq("status", ShoutMembership.Status.INVITED)
                ).findRowCount();
    }
}
