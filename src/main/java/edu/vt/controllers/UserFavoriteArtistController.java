package edu.vt.controllers;

import edu.vt.EntityBeans.User;

import edu.vt.EntityBeans.UserFavoriteArtist;
import edu.vt.FacadeBeans.UserFavoriteArtistFacade;
import edu.vt.Pojos.Artist;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.controllers.util.JsfUtil.PersistAction;
import edu.vt.globals.Constants;
import edu.vt.globals.Methods;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.el.MethodExpression;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named("userFavoriteArtistController")
@SessionScoped

public class UserFavoriteArtistController implements Serializable {
    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */
    private List<UserFavoriteArtist> listOfUserFavoriteArtists = null;
    private UserFavoriteArtist selected;

    @EJB
    private UserFavoriteArtistFacade userFavoriteArtistFacade;

    public UserFavoriteArtistController() {
    }
/*
    =========================
    Getter and Setter Methods
    =========================
     */

    /*
    ***************************************************************
    Return the List of UserFavoriteArtist that Belong to the Signed-In User
    ***************************************************************
     */
    public List<UserFavoriteArtist> getListOfFavoriteArtists() {

        if (listOfUserFavoriteArtists == null) {
            /*
            'user', the object reference of the signed-in user, was put into the SessionMap
            in the initializeSessionMap() method in LoginManager upon user's sign in.
             */
            Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
            User signedInUser = (User) sessionMap.get("user");

            // Obtain the database primary key of the signedInUser object
            Integer primaryKey = signedInUser.getId();

            listOfUserFavoriteArtists = userFavoriteArtistFacade.findUserFavoriteArtistsByUserPrimaryKey(primaryKey);
        }
        return listOfUserFavoriteArtists;
    }




    /*
    ================
    Instance Methods
    ================
    */

    // Return list of categories
    public List<String> getGenres() {
        return Constants.GENRES;
    }

    public Boolean isFavoriteArtist(Artist artist) {
        for (UserFavoriteArtist favoriteArtist :
                getListOfFavoriteArtists()) {
            if (favoriteArtist.getEntityId() == artist.getId()) {
                selected = favoriteArtist;
                return true;
            }
        }
        selected = null;
        return false;
    }

    public void addFavorite(Artist artist) {
        selected = new UserFavoriteArtist();
        selected.setEntityId(artist.getId());
        selected.setUserId(getUser());
        create();
    }

    private User getUser() {
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        return (User) sessionMap.get("user");
    }

    public void unselect() {
        selected = null;
    }

    /*
    *****************
    Prepare to Create
    *****************
    */
    public void prepareCreate() {

        selected = new UserFavoriteArtist();
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        User signedInUser = (User) sessionMap.get("user");
        if (signedInUser == null) {
            JsfUtil.addSuccessMessage("NOT LOGGED IN");
        } else {
            selected.setUserId(signedInUser);
        }
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

    // The constants CREATE, DELETE and UPDATE are defined in JsfUtil.java

    public void create() {
        persist(PersistAction.CREATE, "Artist added to favorites.");

        if (!JsfUtil.isValidationFailed()) {
            selected = null;            // Remove selection
            listOfUserFavoriteArtists = null;     // Invalidate listOfUserFavoriteArtists to trigger re-query.
        }
    }


    public void update() {
        Methods.preserveMessages();

        persist(PersistAction.UPDATE, "Favorıte was successfully updated.");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The UPDATE operation is successfully performed.
            selected = null;        // Remove selection
            listOfUserFavoriteArtists = null;    // Invalidate to trigger re-query.
        }
    }


    public void destroy(Artist artist) {
        if (selected == null) {
            List<UserFavoriteArtist> favs = getListOfFavoriteArtists();
            for (UserFavoriteArtist fav : favs) {
                if (fav.getEntityId().equals(artist.getId())) {
                    selected = fav;
                    break;
                }
            }
        }
        Methods.preserveMessages();

        persist(PersistAction.DELETE, "Artist removed from favorites.");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The DELETE operation is successfully performed.
            selected = null;        // Remove selection
            listOfUserFavoriteArtists = null;    // Invalidate listOfUserFavoriteArtists to trigger re-query.
        }
    }

    /*
     ****************************************************************************
     *   Perform CREATE, EDIT (UPDATE), and DELETE Operations in the Database   *
     ****************************************************************************
     */

    /**
     * @param persistAction  refers to CREATE, UPDATE (Edit) or DELETE action
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

                     userFavoriteArtistFacade inherits the edit(selected) method from the AbstractFacade class.
                     */
                    userFavoriteArtistFacade.edit(selected);
                } else {
                    /*
                     -----------------------------------------
                     Perform DELETE operation in the database.
                     -----------------------------------------
                     The remove method performs the DELETE operation in the database.

                     userFavoriteArtistFacade inherits the remove(selected) method from the AbstractFacade class.
                     */
                    userFavoriteArtistFacade.remove(selected);
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
                    JsfUtil.addErrorMessage(ex, "A Persistence Error Occurred!");
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, "A Persistence Error Occurred!");
            }
        }
    }


}
