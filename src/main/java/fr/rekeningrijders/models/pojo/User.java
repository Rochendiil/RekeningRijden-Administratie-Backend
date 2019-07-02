package fr.rekeningrijders.models.pojo;

import fr.rekeningrijders.util.SystemUtil;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.UUID;

/**
 * @author Ken
 */
@Entity
@Getter
@Setter
public class User
{
    @Id
    @GeneratedValue
    private long id;
    private String uuid;
    private String username;
    private String email;
    private String passwordHash;
    @Transient
    private String token;
    public User() {
    }

    public User(String username, String email, String password) {
        uuid = UUID.randomUUID().toString();
        this.username = username;
        this.email = email;
        this.passwordHash = SystemUtil.sha256Hash(password);
    }


}
