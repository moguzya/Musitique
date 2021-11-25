/*
 * Created by Osman Balci on 2021.7.14
 * Copyright © 2021 Osman Balci. All rights reserved.
 */
package edu.vt.FacadeBeans;

import edu.vt.EntityBeans.UserPhoto;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

// @Stateless annotation implies that the conversational state with the client shall NOT be maintained.
@Stateless
public class UserPhotoFacade extends AbstractFacade<UserPhoto> {
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

    // Obtain the object reference of the EntityManager instance in charge of
    // managing the entities in the persistence context identified above.
    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    /* 
    This constructor method invokes its parent AbstractFacade's constructor method,
    which in turn initializes its entity class type T and entityClass instance variable.
     */
    public UserPhotoFacade() {
        super(UserPhoto.class);
    }

    /*
     *********************
     *   Other Methods   *
     *********************
     */

    // Returns a list of object references of UserPhoto objects that belong to the
    // User object whose primary key is primaryKey
    public List<UserPhoto> findPhotosByUserPrimaryKey(Integer primaryKey) {
        /*
        The following @NamedQuery definition is given in UserPhoto entity class file:
            @NamedQuery(name = "UserPhoto.findPhotosByUserDatabasePrimaryKey", query = "SELECT p FROM UserPhoto p WHERE p.userId.id = :primaryKey")

        userId.id --> User object's database primary key
        The following statement obtains the results from the named database query.
         */

        return (List<UserPhoto>) entityManager.createNamedQuery("UserPhoto.findPhotosByUserDatabasePrimaryKey")
                .setParameter("primaryKey", primaryKey)
                .getResultList();
    }
    
}
