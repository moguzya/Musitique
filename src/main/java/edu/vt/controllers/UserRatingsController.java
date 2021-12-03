/*
 * Created by Osman Balci on 2021.7.15
 * Copyright © 2021 Osman Balci. All rights reserved.
 */
package edu.vt.controllers;

import edu.vt.EntityBeans.User;
import edu.vt.EntityBeans.UserComment;
import edu.vt.EntityBeans.UserRating;
import edu.vt.FacadeBeans.RatingFacade;
import edu.vt.FacadeBeans.UserFacade;
import edu.vt.Pojos.Album;
import edu.vt.Pojos.Artist;
import edu.vt.Pojos.Track;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.globals.Methods;

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

@Named("userRatingsController")
@SessionScoped
public class UserRatingsController implements Serializable {
    List<Album> listofAlbums;
    List<Track> listofTracks;
    List<Artist> listofArtists;
    SpotifyAPIController spotifyAPIController = new SpotifyAPIController();
    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */
    private UserRating selected;
    private List<UserRating> listofUserRatings = null;
    private EntityController entityController;
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

    public UserRating getSelected() {
        return selected;
    }

    /*
    =========================
    Getter and Setter Methods
    =========================
     */
    public void setSelected(UserRating selected) {
        this.selected = selected;
    }

    public void unselect() {
        selected = null;
    }

    /*
    ***************************************************************
    Return the List of User Comments that Belong to the Signed-In User
    ***************************************************************
     */
    public List<UserRating> getListOfUserRatings() {

        if (listofUserRatings == null) {
            /*
            'user', the object reference of the signed-in user, was put into the SessionMap
            in the initializeSessionMap() method in LoginManager upon user's sign in.
             */
            Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
            User signedInUser = (User) sessionMap.get("user");

            // Obtain the database primary key of the signedInUser object
            Integer primaryKey = signedInUser.getId();

            listofUserRatings = ratingFacade.findUserRatingByUserPrimaryKey(primaryKey);

            List<String> AlbumsIds = new ArrayList<>();
            List<String> TracksIds = new ArrayList<>();
            List<String> ArtistsIds = new ArrayList<>();

            //move each entity type into seperate lists to that we can bulk get the data
            for (UserRating listofUserRating : listofUserRatings) {
                String currEntityType = listofUserRating.getEntityType();
                switch (currEntityType) {
                    case "ALBUM":
                        AlbumsIds.add(listofUserRating.getEntityId());
                    case "TRACK":
                        TracksIds.add(listofUserRating.getEntityId());
                    case "ARTIST":
                        ArtistsIds.add(listofUserRating.getEntityId());
                }
            }

            listofAlbums = spotifyAPIController.requestSeveralAlbums(String.join(",", AlbumsIds), false);
            listofTracks = spotifyAPIController.requestSeveralTracks(String.join(",", TracksIds), false);
            listofArtists = spotifyAPIController.requestSeveralArtists(String.join(",", ArtistsIds));

        }

        return listofUserRatings;
    }

    public void setListofUserRatings(List<UserRating> listofUserRatings) {
        this.listofUserRatings = listofUserRatings;
    }

    public Album findAlbum(String entityId) {
        if (listofAlbums != null) {
            for (Album album : listofAlbums) {
                if (album != null) {
                    if (Objects.equals(entityId, album.getId())) {
                        return album;
                    }
                }
            }

        }
        return null;
    }

    public Track findTrack(String entityId) {
        if (listofTracks != null) {
            for (Track track : listofTracks) {
                if (track != null) {
                    if (Objects.equals(entityId, track.getId())) {
                        return track;
                    }
                }
            }
        }
        return null;
    }

    public Artist findArtist(String entityId) {
        if (listofArtists != null) {

            for (Artist artist : listofArtists) {
                if (artist != null) {
                    if (Objects.equals(entityId, artist.getId())) {
                        return artist;
                    }
                }
            }
        }
        return null;
    }

    public String getEntityName(UserRating UserRating) {
        if (UserRating == null)
            return "";
        switch (UserRating.getEntityType()) {
            case "ALBUM":
                Album album = findAlbum(UserRating.getEntityId());
                if (album != null) {
                    return album.getName();
                }
            case "TRACK":
                Track track = findTrack(UserRating.getEntityId());
                if (track != null) {
                    return track.getName();
                }
            case "ARTIST":
                Artist artist = findArtist(UserRating.getEntityId());
                if (artist != null) {
                    return artist.getName();
                }
        }
        return "";
    }


    public String getImageUrl(UserRating UserRating) {
        switch (UserRating.getEntityType()) {
            case "ALBUM":
                Album album = findAlbum(UserRating.getEntityId());
                if (album != null) {
                    return album.getImageUrl();
                }
            case "TRACK":
                Track track = findTrack(UserRating.getEntityId());
                if (track != null) {
                    return track.getImageUrl();
                }
            case "ARTIST":
                Artist artist = findArtist(UserRating.getEntityId());
                if (artist != null) {
                    return artist.getImageUrl();
                }
        }
        return "";
    }


    public String getArtists(UserRating UserRating) {
        switch (UserRating.getEntityType()) {
            case "ALBUM":
                Album album = findAlbum(UserRating.getEntityId());
                if (album != null) {
                    return album.getArtistsListAsString();
                }
            case "TRACK":
                Track track = findTrack(UserRating.getEntityId());
                if (track != null) {
                    return track.getArtistsListAsString();
                }
        }
        return "";
    }


    /*
    *************************************
    UPDATE Selected Rating in the Database
    *************************************
     */
    public void update() {
        Methods.preserveMessages();

        persist(JsfUtil.PersistAction.UPDATE, "Rating was Successfully Updated!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The UPDATE operation is successfully performed.
            selected = null;        // Remove selection
            listofUserRatings = null;    // Invalidate listOfRatings to trigger re-query.
        }
    }

    /*
    ***************************************
    DELETE Selected Rating from the Database
    ***************************************
    */
    public void destroy() {
        JsfUtil.addSuccessMessage("here");
        JsfUtil.addSuccessMessage(selected.getEntityId());

        Methods.preserveMessages();

        persist(JsfUtil.PersistAction.DELETE, "Rating was Successfully Deleted!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The DELETE operation is successfully performed.
            selected = null;        // Remove selection
            listofUserRatings = null;    // Invalidate listOfRatings to trigger re-query.
        }
    }

    /*
     **********************************************************************************************
     *   Perform CREATE, UPDATE (EDIT), and DELETE (DESTROY, REMOVE) Operations in the Database   *
     **********************************************************************************************
     */

    /**
     * @param persistAction  refers to CREATE, UPDATE (Edit) or DELETE action
     * @param successMessage displayed to inform the user about the result
     */
    private void persist(JsfUtil.PersistAction persistAction, String successMessage) {
        if (selected != null) {
            try {
                if (persistAction != JsfUtil.PersistAction.DELETE) {
                    /*
                     -------------------------------------------------
                     Perform CREATE or EDIT operation in the database.
                     -------------------------------------------------
                     The edit(selected) method performs the SAVE (STORE) operation of the "selected"
                     object in the database regardless of whether the object is a newly
                     created object (CREATE) or an edited (updated) object (EDIT or UPDATE).

                     RatingFacade inherits the edit(selected) method from the AbstractFacade class.
                     */
                    ratingFacade.edit(selected);
                } else {
                    /*
                     -----------------------------------------
                     Perform DELETE operation in the database.
                     -----------------------------------------
                     The remove(selected) method performs the DELETE operation of the "selected"
                     object in the database.

                     RatingFacade inherits the remove(selected) method from the AbstractFacade class.
                     */
                    ratingFacade.remove(selected);
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
                    JsfUtil.addErrorMessage(ex, "A persistence error occurred.");
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, "A persistence error occurred.");
            }
        }
    }

}
