package classes;

import models.User;


/**
 * Created by Steffen Rudkjøbing on 07/01/15.
 * © 2014 Steffen Rudkjøbing
 */
public class AddFriendshipRequest {

    public Long userId;
    public String nickname;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
