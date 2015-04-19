package classes;

/**
 * Created by Steffen Rudkjøbing on 12/01/15.
 * © 2014 Steffen Rudkjøbing
 */
public class FriendshipAddRequest {

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
