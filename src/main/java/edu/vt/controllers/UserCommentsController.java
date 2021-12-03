/*
 * Created by Osman Balci on 2021.7.15
 * Copyright © 2021 Osman Balci. All rights reserved.
 */
package edu.vt.controllers;

import edu.vt.EntityBeans.User;
import edu.vt.EntityBeans.UserComment;
import edu.vt.FacadeBeans.CommentFacade;
import edu.vt.FacadeBeans.UserFacade;
import edu.vt.Pojos.Album;
import edu.vt.Pojos.Artist;
import edu.vt.Pojos.Track;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.globals.Methods;
import edu.vt.controllers.util.JsfUtil.PersistAction;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

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


    List<Album> listofAlbums;
    List<Track> listofTracks;
    List<Artist> listofArtists;

    SpotifyAPIController spotifyAPIController = new SpotifyAPIController();


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
            Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
            User signedInUser = (User) sessionMap.get("user");

            // Obtain the database primary key of the signedInUser object
            Integer primaryKey = signedInUser.getId();


            // Obtain only those videos from the database that belong to the signed-in user
            listofUserComments = commentFacade.findUserCommentByUserPrimaryKey(primaryKey);

            List<String> AlbumsIds = new ArrayList<>();
            List<String> TracksIds = new ArrayList<>();
            List<String> ArtistsIds = new ArrayList<>();

            //move each entity type into seperate lists to that we can bulk get the data
            for (UserComment listofUserComment : listofUserComments) {
                String currEntityType = listofUserComment.getEntityType();
                switch (currEntityType) {
                    case "ALBUM":
                        AlbumsIds.add(listofUserComment.getEntityId());
                    case "TRACK":
                        TracksIds.add(listofUserComment.getEntityId());
                    case "ARTIST":
                        ArtistsIds.add(listofUserComment.getEntityId());
                }
            }

            listofAlbums = spotifyAPIController.requestSeveralAlbums(String.join(",", AlbumsIds));
            System.out.println(listofAlbums);
            listofTracks = spotifyAPIController.requestSeveralTracks(String.join(",", TracksIds));
            listofArtists = spotifyAPIController.requestSeveralArtists(String.join(",", ArtistsIds));

        }

        return listofUserComments;
    }

    public void setListofUserComments(List<UserComment> listofUserComments) {
        this.listofUserComments = listofUserComments;
    }

    public Album findAlbum(String entityId) {
        if (listofAlbums !=null) {
            for (Album album : listofAlbums) {
                if (album != null){
                    if (Objects.equals(entityId, album.getId())) {
                        return album;
                    }
                }
            }

        }
        return null;
    }

    public Track findTrack(String entityId) {
        if (listofTracks !=null) {
            for (Track track : listofTracks) {
                if (track != null){
                    if (Objects.equals(entityId, track.getId())) {
                        return track;
                    }
                }
            }
        }
        return null;
    }

    public Artist findArtist(String entityId) {
        if (listofArtists !=null) {

            for (Artist artist : listofArtists) {
                if (artist != null){
                    if (Objects.equals(entityId, artist.getId())) {
                        return artist;
                    }
                }
            }
        }
        return null;
    }

    public String getEntityName(UserComment userComment) {
        switch (userComment.getEntityType()) {
            case "ALBUM":
                Album album = findAlbum(userComment.getEntityId());
                if (album != null) {
                    return album.getName();
                }
            case "TRACK":
                Track track = findTrack(userComment.getEntityId());
                if (track != null) {
                    return track.getName();
                }
            case "ARTIST":
                Artist artist = findArtist(userComment.getEntityId());
                if (artist != null) {
                    return artist.getName();
                }
        }
        return "qwe";
    }


    public String getImageUrl(UserComment userComment) {
        switch (userComment.getEntityType()) {
            case "ALBUM":
                Album album = findAlbum(userComment.getEntityId());
                if (album != null) {
                    return album.getImageUrl();
                }
            case "TRACK":
                Track track = findTrack(userComment.getEntityId());
                if (track != null) {
                    return track.getImageUrl();
                }
            case "ARTIST":
                Artist artist = findArtist(userComment.getEntityId());
                if (artist != null) {
                    return artist.getImageUrl();
                }
        }
        return "";
    }


    public String getArtists(UserComment userComment) {
        switch (userComment.getEntityType()) {
            case "ALBUM":
                Album album = findAlbum(userComment.getEntityId());
                if (album != null) {
                    return album.getArtistsListAsString();
                }
            case "TRACK":
                Track track = findTrack(userComment.getEntityId());
                if (track != null) {
                    return track.getArtistsListAsString();
                }
        }
        return "";
    }

    /*
        *************************************
        UPDATE Selected Movie in the Database
        *************************************
         */
    public void update() {
        Methods.preserveMessages();

        persist(PersistAction.UPDATE,"Comment was Successfully Updated!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The UPDATE operation is successfully performed.
            selected = null;        // Remove selection
            listofUserComments = null;    // Invalidate listOfMovies to trigger re-query.
        }
    }

    /*
    ***************************************
    DELETE Selected Movie from the Database
    ***************************************
     */
    public void destroy() {
        Methods.preserveMessages();

        persist(PersistAction.DELETE,"Comment was Successfully Deleted!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The DELETE operation is successfully performed.
            selected = null;        // Remove selection
            listofUserComments = null;    // Invalidate listOfMovies to trigger re-query.
        }
    }

    /*
     **********************************************************************************************
     *   Perform CREATE, UPDATE (EDIT), and DELETE (DESTROY, REMOVE) Operations in the Database   *
     **********************************************************************************************
     */
    /**
     * @param persistAction refers to CREATE, UPDATE (Edit) or DELETE action
     * @param successMessage displayed to inform the user about the result
     */
    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            try {
                if (persistAction != PersistAction.DELETE) {
                    /*
                     -------------------------------------------------
                     Perform CREATE or EDIT operation in the database.
                     -------------------------------------------------
                     The edit(selected) method performs the SAVE (STORE) operation of the "selected"
                     object in the database regardless of whether the object is a newly
                     created object (CREATE) or an edited (updated) object (EDIT or UPDATE).

                     MovieFacade inherits the edit(selected) method from the AbstractFacade class.
                     */
                    commentFacade.edit(selected);
                } else {
                    /*
                     -----------------------------------------
                     Perform DELETE operation in the database.
                     -----------------------------------------
                     The remove(selected) method performs the DELETE operation of the "selected"
                     object in the database.

                     MovieFacade inherits the remove(selected) method from the AbstractFacade class.
                     */
                    commentFacade.remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex,"A persistence error occurred.");
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex,"A persistence error occurred.");
            }
        }
    }

}
