package fr.rekeningrijders.doa.context;

import fr.rekeningrijders.models.pojo.LoginInfo;
import fr.rekeningrijders.models.pojo.User;

public interface IUserStorage {

    User read(long id);

    void create(User user);

    void update(User user);

    void delete(User user);

    User login(LoginInfo loginInfo);
}
