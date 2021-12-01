package edu.vt.FacadeBeans;

import edu.vt.EntityBeans.User;
import edu.vt.EntityBeans.UserComment;
import edu.vt.EntityBeans.UserRating;
import edu.vt.EntityType;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

// @Stateless annotation implies that the conversational state with the client shall not be maintained.
@Stateless
public class RatingFacade extends AbstractFacade<UserRating> {
    /*
    ---------------------------------------------------------------------------------------------
    The EntityManager is an API that enables database CRUD (Create Read Update Delete) operations
    and complex database searches. An EntityManager instance is created to manage entities
    that are defined by a persistence unit. The @PersistenceContext annotation below associates
    the entityManager instance with the persistence unitName identified below.
    ---------------------------------------------------------------------------------------------
     */
    @PersistenceContext(unitName = "MusitiquePU")
    private EntityManager entityManager;

    /*
    This constructor method invokes its parent AbstractFacade's constructor method,
    which in turn initializes its entity class type T and entityClass instance variable.
     */
    public RatingFacade() {
        super(UserRating.class);
    }

    // Obtain the object reference of the EntityManager instance in charge of
    // managing the entities in the persistence context identified above.
    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    public Double findAverageRatingByEntityId(String entityId) {
        List<UserRating> ratings = getEntityManager()
                .createQuery("SELECT r FROM UserRating r WHERE r.entityId = :entityId")
                .setParameter("entityId", entityId)
                .getResultList();
        if (ratings.size() == 0)
            return 0.0;
        Double sum = 0.0;
        for (UserRating rating : ratings) {
            sum += rating.getRating();
        }
        return sum / ratings.size();
    }

    public UserRating findUserRatingByEntityId(String entityId, User user, EntityType entityType) {
        UserRating userRating;
        try {
            userRating = (UserRating) getEntityManager()
                    .createQuery("SELECT r FROM UserRating r WHERE r.entityId = :entityId AND r.userId = :userId")
                    .setParameter("entityId", entityId)
                    .setParameter("userId", user)
                    .getSingleResult();
        } catch (NoResultException nre) {
            userRating = new UserRating(user, entityId, -1, entityType);
        }
        return userRating;
    }

    // Deletes the UserRating entity object whose primary key is id
    public void deleteComment(int id) {

        // The find method is inherited from the parent AbstractFacade class
        UserRating userRating = getEntityManager().find(UserRating.class, id);

        // The remove method is inherited from the parent AbstractFacade class
        getEntityManager().remove(userRating);
    }

    // Returns a list of object references of UserVideo objects that belong to
    // the User object whose database Primary Key = primaryKey
    public List<UserRating> findUserRatingByUserPrimaryKey(Integer primaryKey) {
        /*
        The following @NamedQuery definition is given in UserVideo entity class file:
        @NamedQuery(name = "UserVideo.findByUserId", query = "SELECT u FROM UserVideo u WHERE u.userId.id = :userId")

        The following statement obtains the results from the named database query.
         */
        return entityManager.createNamedQuery("UserRating.findByUserId")
                .setParameter("userId", primaryKey)
                .getResultList();
    }
}
