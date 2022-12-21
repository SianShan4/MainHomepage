package welcomecording.mainHomepage.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class SessionUser implements Serializable {
    private String username;
    private String email;
    private String picture;

    public SessionUser(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }

    public SessionUser() {
    }

}
