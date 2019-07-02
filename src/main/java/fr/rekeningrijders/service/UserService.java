package fr.rekeningrijders.service;

import fr.rekeningrijders.doa.context.IUserStorage;
import fr.rekeningrijders.models.pojo.LoginInfo;
import fr.rekeningrijders.models.pojo.Role;
import fr.rekeningrijders.models.pojo.User;
import fr.rekeningrijders.security.JwtManager;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class UserService {
    @Inject
    private JwtManager jwtManager;
    @Inject
    private IUserStorage userStorage;

    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

    public UserService() {
    }

    public UserService(IUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user){
        userStorage.create(user);
        return user;
    }
    public User find(long id){
        return userStorage.read(id);
    }

    public void update(User user) {
        userStorage.update(user);
    }

    public void delete(User user){
        userStorage.delete(user);
    }

    public User login(LoginInfo loginInfo) {
        User user = userStorage.login(loginInfo);
        //todo add roles
        String token = null;
        try {
            token = jwtManager.createJwt(user.getUuid(), Role.EMPLOYEE.toString());
            user.setToken(token);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
        return user;
    }

}
