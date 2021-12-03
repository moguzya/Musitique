package edu.vt.FacadeBeans;

import edu.vt.EntityBeans.User;
import edu.vt.EntityBeans.UserComment;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

// @Stateless annotation implies that the conversational state with the client shall not be maintained.
@Stateless
public class CommentFacade extends AbstractFacade<UserComment> {
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
    public CommentFacade() {
        super(UserComment.class);
    }

    // Obtain the object reference of the EntityManager instance in charge of
    // managing the entities in the persistence context identified above.
    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    public List<UserComment> findCommentsByEntityId(String entityId) {
        return getEntityManager().createQuery("SELECT c FROM UserComment c WHERE c.entityId = :entityId")
                .setParameter("entityId", entityId)
                .getResultList();
    }

    // Deletes the UserComment entity object whose primary key is id
    public void deleteComment(int id) {

        // The find method is inherited from the parent AbstractFacade class
        UserComment userComment = getEntityManager().find(UserComment.class, id);

        // The remove method is inherited from the parent AbstractFacade class
        getEntityManager().remove(userComment);
    }

    // Returns a list of object references of UserVideo objects that belong to
    // the User object whose database Primary Key = primaryKey
    public List<UserComment> findUserCommentByUserPrimaryKey(Integer primaryKey) {
        /*
        The following @NamedQuery definition is given in UserVideo entity class file:
        @NamedQuery(name = "UserVideo.findByUserId", query = "SELECT u FROM UserVideo u WHERE u.userId.id = :userId")

        The following statement obtains the results from the named database query.
         */
        return entityManager.createNamedQuery("UserComment.findByUserId")
                .setParameter("userId", primaryKey)
                .getResultList();
    }
}
