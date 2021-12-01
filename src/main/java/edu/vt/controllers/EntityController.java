/*
 * Created by Mehmet Oguz Yardimci
 * Copyright © 2021 Mehmet Oguz Yardimci. All rights reserved.
 */
package edu.vt.controllers;

import edu.vt.EntityBeans.User;
import edu.vt.EntityBeans.UserComment;
import edu.vt.EntityBeans.UserRating;
import edu.vt.EntityType;
import edu.vt.Pojos.Album;
import edu.vt.FacadeBeans.CommentFacade;
import edu.vt.FacadeBeans.RatingFacade;
import edu.vt.Pojos.Artist;
import edu.vt.Pojos.Track;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.controllers.util.JsfUtil.PersistAction;
import edu.vt.globals.Methods;
import org.primefaces.event.RateEvent;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
---------------------------------------------------------------------------
The @Named (javax.inject.Named) annotation indicates that the objects
instantiated from this class will be managed by the Contexts and Dependency
Injection (CDI) container. The name "entityController" is used within
Expression Language (EL) expressions in JSF (XHTML) facelets pages to
access the properties and invoke methods of this class.
---------------------------------------------------------------------------
*/
@Named("entityController")

/*
The @SessionScoped annotation preserves the values of the VideoController
object's instance variables across multiple HTTP request-response cycles
as long as the user's established HTTP session is alive.
*/
@SessionScoped

/*
-----------------------------------------------------------------------------
Marking the VideoController class as "implements Serializable" implies that
instances of the class can be automatically serialized and deserialized. 

Serialization is the process of converting a class instance (object)
from memory into a suitable format for storage in a file or memory buffer, 
or for transmission across a network connection link.

Deserialization is the process of recreating a class instance (object)
in memory from the format under which it was stored.
-----------------------------------------------------------------------------
*/
public class EntityController implements Serializable {
    /*
    ===============================
    Instance Variables (Properties)
    ===============================
    */
    @EJB
    private CommentFacade commentFacade;

    @EJB
    private RatingFacade ratingFacade;

    private Track selectedTrack = new Track(
            "Test track name",
            123456,
            Boolean.TRUE,
            "https://i.scdn.co/image/ab67616d00004851efdaf87d5ea59307b4d530a3");

    private Album selectedAlbum = new Album(
            "4OEnpg5ubhg6OQ4M2ZjtsL",
            "Most People (with Lukas Graham)",
            "https://i.scdn.co/image/ab67616d00004851efdaf87d5ea59307b4d530a3",
            "2026-06-19",
            25,
            Arrays.asList(selectedTrack, selectedTrack, selectedTrack, selectedTrack, selectedTrack, selectedTrack)
    );
    private Artist selectedArtist;

    private UserComment selectedComment;
    private UserRating userRating;

    private List<UserComment> listOfComments;
    private EntityType selectedEntityType = EntityType.ALBUM;
    private String newCommentText;
    private Double averageEntityRating;
    /*
    ================
    Instance Methods
    ================
    */

    public String toAlbumPage(Album selectedAlbum) {
        setSelectedAlbum(selectedAlbum);
        return "/standalonePages/Album?faces-redirect=true";
    }

    public String toArtistPage(Artist selectedArtist) {
        setSelectedArtist(selectedArtist);
        return "/standalonePages/Artist?faces-redirect=true";
    }

    public String toTrackPage(Track selectedTrack) {
        setSelectedTrack(selectedTrack);
        return "/standalonePages/Track?faces-redirect=true";
    }

    public void unselectAlbum() {
        selectedAlbum = null;
    }

    public void unselectArtist() {
        selectedArtist = null;
    }

    public void unselectTrack() {
        selectedTrack = null;
    }

    /*
    An enum is a special Java type used to define a group of constants.
    The constants CREATE, DELETE and UPDATE are defined as follows in JsfUtil.java

            public enum PersistAction {
                CREATE,
                DELETE,
                UPDATE
            }
     */

    /*
    ************************************
    CREATE a New Comment in the Database
    ************************************
     */
    public void createComment(UserComment comment) {
        selectedComment = comment;
        Methods.preserveMessages();
        persistComment(PersistAction.CREATE, "Comment was Successfully Created!");
        if (!JsfUtil.isValidationFailed()) {
            listOfComments = null;  // Invalidate listOfComments to trigger re-query.
        }
    }

    public void createRating(UserRating rating) {
        userRating = rating;
        Methods.preserveMessages();
        persistRating(PersistAction.CREATE, "Rating was Successfully Created!");
    }

    public void updateRating(UserRating rating) {
        userRating = rating;
        Methods.preserveMessages();
        persistRating(PersistAction.UPDATE, "Rating was Successfully Updated!");
    }

    public void destroyComment(UserComment comment) {
        selectedComment = comment;
        Methods.preserveMessages();

        persistComment(PersistAction.DELETE, "Comment was Successfully Deleted!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The DELETE operation is successfully performed.
            listOfComments = null;    // Invalidate listOfComments to trigger re-query.
        }
    }

    public void destroyRating(UserRating rating) {
        userRating = rating;
        Methods.preserveMessages();

        persistRating(PersistAction.DELETE, "Rating was Successfully Deleted!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The DELETE operation is successfully performed.
            userRating = null;
        }
    }

    public void postComment() {
        UserComment userComment = new UserComment(getUser(), getSelectedEntityId(), newCommentText);
        createComment(userComment);
        newCommentText = "";
    }

    public void onRate(RateEvent<Integer> rateEvent) {
        userRating = ratingFacade.findUserRatingByEntityId(getSelectedEntityId(), getUser(), selectedEntityType.toString());
        if (userRating.getRating() == -1) {
            userRating = new UserRating(getUser(), getSelectedEntityId(), rateEvent.getRating(), selectedEntityType.toString());
            createRating(userRating);
        } else {
            userRating.setRating(rateEvent.getRating());
            updateRating(userRating);
        }
    }

    public void onUnrate() {
        destroyRating(userRating);
    }

    private User getUser() {
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        return (User) sessionMap.get("user");
    }

    /*
    ******************
    Getter and Setters
    ******************
     */
    public Album getSelectedAlbum() {
        return selectedAlbum;
    }

    public void setSelectedAlbum(Album selectedAlbum) {
        this.selectedAlbum = selectedAlbum;
        selectedEntityType = EntityType.ALBUM;
        unselectArtist();
        unselectTrack();
    }

    public Artist getSelectedArtist() {
        return selectedArtist;
    }

    public void setSelectedArtist(Artist selectedArtist) {
        this.selectedArtist = selectedArtist;
        selectedEntityType = EntityType.ARTIST;
        unselectAlbum();
        unselectTrack();
    }

    public Track getSelectedTrack() {
        //TODO fix this
        selectedTrack.setAlbum(selectedAlbum);
        return selectedTrack;
    }

    public void setSelectedTrack(Track selectedTrack) {
        this.selectedTrack = selectedTrack;
        selectedEntityType = EntityType.TRACK;
        unselectAlbum();
        unselectArtist();
    }

    public String getNewCommentText() {
        return newCommentText;
    }

    public void setNewCommentText(String newCommentText) {
        this.newCommentText = newCommentText;
    }

    public CommentFacade getCommentFacade() {
        return commentFacade;
    }

    public void setCommentFacade(CommentFacade commentFacade) {
        this.commentFacade = commentFacade;
    }

    public RatingFacade getRatingFacade() {
        return ratingFacade;
    }

    public void setRatingFacade(RatingFacade ratingFacade) {
        this.ratingFacade = ratingFacade;
    }

    public UserComment getSelectedComment() {
        return selectedComment;
    }

    public void setSelectedComment(UserComment selectedComment) {
        this.selectedComment = selectedComment;
    }

    public UserRating getUserRating() {
        if (userRating == null) {
            userRating = ratingFacade.findUserRatingByEntityId(getSelectedEntityId(), getUser(), selectedEntityType.toString());
        }
        return userRating;
    }

    public void setUserRating(UserRating userRating) {
        this.userRating = userRating;
    }

    public List<UserComment> getListOfComments() {
        if (listOfComments == null) {
            listOfComments = commentFacade.findCommentsByEntityId(getSelectedEntityId());
        }
        return listOfComments;
    }

    public void setListOfComments(List<UserComment> listOfComments) {
        this.listOfComments = listOfComments;
    }

    private String getSelectedEntityId() {
        switch (selectedEntityType) {
            case ALBUM:
                return selectedAlbum.getId();
            case ARTIST:
                return selectedArtist.getId();
            case TRACK:
                return selectedTrack.getId();
        }
        return "";
    }

    public EntityType getSelectedEntityType() {
        return selectedEntityType;
    }

    public void setSelectedEntityType(EntityType selectedEntityType) {
        this.selectedEntityType = selectedEntityType;
    }

    public Double getAverageEntityRating() {
        listOfComments = commentFacade.findCommentsByEntityId(getSelectedEntityId());
        return averageEntityRating;
    }

    public void setAverageEntityRating(Double averageEntityRating) {
        this.averageEntityRating = averageEntityRating;
    }

//    public String getEntityArtists(String entityId, EntityType entityType){
//        switch (entityType) {
//            case ALBUM:
//                return selectedAlbum.getArtistsListAsString();
//            case TRACK:
//                return selectedTrack.getArtistsListAsString();
//        }
//        return "";
//    }

    public Object getEntity(String entityId, EntityType entityType) {
        switch (entityType) {
            case ALBUM:
                return selectedTrack;
            case ARTIST:
                return selectedArtist;
            case TRACK:
                return selectedTrack;
        }
        return null;
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
    private void persistComment(PersistAction persistAction, String successMessage) {
        if (selectedComment != null) {
            try {
                if (persistAction != PersistAction.DELETE) {
                    /*
                     -------------------------------------------------
                     Perform CREATE or EDIT operation in the database.
                     -------------------------------------------------
                     The edit(selected) method performs the SAVE (STORE) operation of the "selectedComment"
                     object in the database regardless of whether the object is a newly
                     created object (CREATE) or an edited (updated) object (EDIT or UPDATE).

                     CommentFacade inherits the edit(selected) method from the AbstractFacade class.
                     */
                    commentFacade.edit(selectedComment);
                } else {
                    /*
                     -----------------------------------------
                     Perform DELETE operation in the database.
                     -----------------------------------------
                     The remove(selected) method performs the DELETE operation of the "selectedComment"
                     object in the database.

                     CommentFacade inherits the remove(selected) method from the AbstractFacade class.
                     */
                    commentFacade.remove(selectedComment);
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

    private void persistRating(PersistAction persistAction, String successMessage) {
        if (userRating != null) {
            try {
                if (persistAction != PersistAction.DELETE) {
                    /*
                     -------------------------------------------------
                     Perform CREATE or EDIT operation in the database.
                     -------------------------------------------------
                     The edit(selected) method performs the SAVE (STORE) operation of the "userRating"
                     object in the database regardless of whether the object is a newly
                     created object (CREATE) or an edited (updated) object (EDIT or UPDATE).

                     ratingFacade inherits the edit(selected) method from the AbstractFacade class.
                     */
                    ratingFacade.edit(userRating);
                } else {
                    /*
                     -----------------------------------------
                     Perform DELETE operation in the database.
                     -----------------------------------------
                     The remove(selected) method performs the DELETE operation of the "userRating"
                     object in the database.

                     RatingFacade inherits the remove(selected) method from the AbstractFacade class.
                     */
                    ratingFacade.remove(userRating);
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
