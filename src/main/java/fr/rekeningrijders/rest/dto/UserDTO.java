package fr.rekeningrijders.rest.dto;


import fr.rekeningrijders.models.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO implements Serializable
{
    private static final long serialVersionUID = -2567776740331153388L;
    
    long id;
    private String uuid;
    private String username;
    String token;

    public UserDTO(long id, String uuid, String username) {
        this.id = id;
        this.uuid = uuid;
        this.username = username;
    }

    public static UserDTO transform(User user)
    {
        return user == null
            ? null 
            : new UserDTO(user.getId(), user.getUuid(), user.getUsername(),user.getToken());
    }
}
