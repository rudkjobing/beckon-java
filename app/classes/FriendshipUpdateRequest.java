package classes;

import models.User;


/**
 * Created by Steffen Rudkjøbing on 07/01/15.
 * © 2014 Steffen Rudkjøbing
 */
public class FriendshipUpdateRequest {

    public Long id;
    public String nickname;

    public Long getFriendId() {
        return id;
    }

    public void setFriendId(Long friendId) {
        this.id = friendId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
