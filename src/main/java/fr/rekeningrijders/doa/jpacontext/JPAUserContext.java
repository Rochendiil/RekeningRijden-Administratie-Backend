package fr.rekeningrijders.doa.jpacontext;

import fr.rekeningrijders.doa.context.IUserStorage;
import fr.rekeningrijders.models.pojo.LoginInfo;
import fr.rekeningrijders.models.pojo.User;
import fr.rekeningrijders.util.SystemUtil;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Default
@Stateless
public class JPAUserContext implements IUserStorage {

    @PersistenceContext(unitName = "RekAdmPU")
    private EntityManager entityManager;

    public JPAUserContext() {
    }

    public JPAUserContext(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public User read(long id) {
        return entityManager.find(User.class,id);
    }

    @Override
    public void create(User user) {
        entityManager.persist(user);
    }

    @Override
    public void update(User user) {
        entityManager.persist(user);
    }

    @Override
    public void delete(User user){
        entityManager.remove(user);
    }

    @Override
    public User login(LoginInfo loginInfo) {
        Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username AND u.passwordHash= :passwordhash")
                .setParameter("username", loginInfo.getUsername())
                .setParameter("passwordhash", SystemUtil.sha256Hash(loginInfo.getPassword()));
        if(!query.getResultList().isEmpty()){
            return (User)query.getSingleResult();
        }
        return null;
    }
}
