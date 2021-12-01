/*
 * Created by Osman Balci on 2021.7.15
 * Copyright © 2021 Osman Balci. All rights reserved.
 */
package edu.vt.controllers;

import edu.vt.EntityBeans.User;
import edu.vt.EntityBeans.UserComment;
import edu.vt.EntityBeans.UserPhoto;
import edu.vt.EntityBeans.UserRating;
import edu.vt.EntityType;
import edu.vt.FacadeBeans.CommentFacade;
import edu.vt.FacadeBeans.RatingFacade;
import edu.vt.FacadeBeans.UserFacade;
import edu.vt.FacadeBeans.UserPhotoFacade;
import edu.vt.Pojos.Album;
import edu.vt.Pojos.Artist;
import edu.vt.Pojos.Track;
import edu.vt.globals.Constants;
import edu.vt.globals.Methods;
import edu.vt.globals.Password;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Named("userCommentsController")
@SessionScoped
public class UserCommentsController implements Serializable {
    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */
    private UserComment selected;
    private List<UserComment> listofUserComments = null;
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
    private CommentFacade commentFacade;

    /*
    =========================
    Getter and Setter Methods
    =========================
     */
    public void setSelected(UserComment selected) {
        this.selected = selected;
    }

    public UserComment getSelected() {
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
    public List<UserComment> getListofUserComments() {

        if (listofUserComments == null) {
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
            listofUserComments = commentFacade.findUserCommentByUserPrimaryKey(primaryKey);
        }
        return listofUserComments;
    }

    public void setListofUserComments(List<UserComment> listofUserComments) {
        this.listofUserComments = listofUserComments;
    }



}
