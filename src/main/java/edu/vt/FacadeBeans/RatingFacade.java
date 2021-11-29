package edu.vt.FacadeBeans;

import edu.vt.EntityBeans.UserComment;
import edu.vt.EntityBeans.UserRating;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
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
        Double sum = 0.0;
        for (UserRating rating : ratings) {
            sum += rating.getRating();
        }
        return sum / ratings.size();
    }

    public UserRating findUserRatingByEntityId(String entityId, Integer userId) {
        return (UserRating) getEntityManager()
                .createQuery("SELECT r FROM UserRating r WHERE r.entityId = :entityId AND r.userId = :userId")
                .setParameter("entityId", entityId)
                .setParameter("userId", userId)
                .getSingleResult();
    }

    // Deletes the UserRating entity object whose primary key is id
    public void deleteComment(int id) {

        // The find method is inherited from the parent AbstractFacade class
        UserRating userRating = getEntityManager().find(UserRating.class, id);

        // The remove method is inherited from the parent AbstractFacade class
        getEntityManager().remove(userRating);
    }
}
