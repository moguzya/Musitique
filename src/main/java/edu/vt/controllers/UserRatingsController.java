/*
 * Created by Osman Balci on 2021.7.15
 * Copyright © 2021 Osman Balci. All rights reserved.
 */
package edu.vt.controllers;

import edu.vt.EntityBeans.UserRating;
import edu.vt.FacadeBeans.RatingFacade;
import edu.vt.FacadeBeans.UserFacade;
import edu.vt.Pojos.Album;
import edu.vt.Pojos.Artist;
import edu.vt.Pojos.Track;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("userRatingsController")
@SessionScoped
public class UserRatingsController implements Serializable {
    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */
    private UserRating selected;
    private List<UserRating> listofUserRatings = null;
    private EntityController entityController;

    private Album album;
    private Track track;
    private Artist artist;



    /*
    The @EJB annotation directs the EJB Container Manager to inject (store) the object reference of the
    UserFacade bean into the instance variable 'userFacade' after it is instantiated at runtime.
     */
    @EJB
    private UserFacade userFacade;

    /*
    The @EJB annotation directs the EJB Container Manager to inject (store) the object reference of the
    CommentFacade bean into the instance variable 'commentFacade' after it is instantiated at runtime.
     */
    @EJB
    private RatingFacade ratingFacade;

    /*
    =========================
    Getter and Setter Methods
    =========================
     */
    public void setSelected(UserRating selected) {
        this.selected = selected;
    }

    public UserRating getSelected() {
        return selected;
    }

    public void unselect() {
        selected = null;
    }

    /*
    ***************************************************************
    Return the List of User Comments that Belong to the Signed-In User
    ***************************************************************
     */
    public List<UserRating> getListofUserRatings() {

        if (listofUserRatings == null) {
            /*
            'user', the object reference of the signed-in user, was put into the SessionMap
            in the initializeSessionMap() method in LoginManager upon user's sign in.
             */
//            Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
//            User signedInUser = (User) sessionMap.get("user");
//
//            // Obtain the database primary key of the signedInUser object
//            Integer primaryKey = signedInUser.getId();
            Integer primaryKey = 1;
            // Obtain only those videos from the database that belong to the signed-in user
            listofUserRatings = ratingFacade.findUserRatingByUserPrimaryKey(primaryKey);
        }
        return listofUserRatings;
    }

    public void setListofUserComments(List<UserRating> listofUserComments) {
        this.listofUserRatings = listofUserComments;
    }



}
