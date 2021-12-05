package edu.vt.controllers;

import edu.vt.EntityBeans.User;

import edu.vt.EntityBeans.UserFavoriteArtist;
import edu.vt.FacadeBeans.UserFavoriteArtistFacade;
import edu.vt.Pojos.Artist;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.controllers.util.JsfUtil.PersistAction;
import edu.vt.globals.Constants;
import edu.vt.globals.Methods;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.el.MethodExpression;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static edu.vt.globals.Constants.ACCESS_TOKEN;
import static edu.vt.globals.Constants.CLIENT;

@Named("userFavoriteArtistController")
@SessionScoped

public class UserFavoriteArtistController implements Serializable {
    private List<Artist> favoriteArtists;
    private List<UserFavoriteArtist> listOfUserFavoriteArtists = null;
    private UserFavoriteArtist selected;

    @EJB
    private UserFavoriteArtistFacade userFavoriteArtistFacade;


    /*
    ***************************************************************
    Return the List of UserFavoriteArtist that Belong to the Signed-In User
    ***************************************************************
     */
    public List<UserFavoriteArtist> getListOfUserFavoriteArtists() {

        if (listOfUserFavoriteArtists == null) {

            Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
            User signedInUser = (User) sessionMap.get("user");

            // Obtain the database primary key of the signedInUser object
            Integer primaryKey = signedInUser.getId();

            listOfUserFavoriteArtists = userFavoriteArtistFacade.findUserFavoriteArtistsByUserPrimaryKey(primaryKey);
        }
        return listOfUserFavoriteArtists;
    }

    public void requestFavoriteArtists() {
        String queryArtist = getListOfUserFavoriteArtists().stream().
                map(i -> String.valueOf(i.getEntityId())).
                collect(Collectors.joining(","));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.spotify.com/v1/artists?ids=" + queryArtist))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .GET()
                .build();
        try {
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<Artist> artists = new ArrayList<>();
                JSONArray artistArray = new JSONObject(response.body()).getJSONArray("artists");

                for (int i = 0; i < artistArray.length(); i++) {
                    if (!Objects.equals(artistArray.get(i).toString(), "null")) {
                        Artist a = new Artist(artistArray.getJSONObject(i).toString());
                        artists.add(a);
                    }
                }
                favoriteArtists = artists;
                return;
            } else if (response.statusCode() == 401) {
                Methods.requestToken();
                requestFavoriteArtists();
                return;
            } else if (response.statusCode() == 429) {
                JsfUtil.addErrorMessage("Api rate limit exceeded!");
                favoriteArtists = new ArrayList<>();
                return;
            }
        } catch (IOException | InterruptedException e) {
            JsfUtil.addErrorMessage(e.toString());
            favoriteArtists = new ArrayList<>();
            return;
        }
        favoriteArtists = new ArrayList<>();
        return;
    }

    public List<Artist> getFavoriteArtists() {
        if (favoriteArtists == null)
            requestFavoriteArtists();
        return favoriteArtists;
    }

    public void setFavoriteArtists(List<Artist> favoriteArtists) {
        this.favoriteArtists = favoriteArtists;
    }

    public void setListOfUserFavoriteArtists(List<UserFavoriteArtist> listOfUserFavoriteArtists) {
        this.listOfUserFavoriteArtists = listOfUserFavoriteArtists;
    }

    public UserFavoriteArtist getSelected() {
        return selected;
    }

    public void setSelected(UserFavoriteArtist selected) {
        this.selected = selected;
    }

    public UserFavoriteArtistFacade getUserFavoriteArtistFacade() {
        return userFavoriteArtistFacade;
    }

    public void setUserFavoriteArtistFacade(UserFavoriteArtistFacade userFavoriteArtistFacade) {
        this.userFavoriteArtistFacade = userFavoriteArtistFacade;
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
                getListOfUserFavoriteArtists()) {
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
            List<UserFavoriteArtist> favs = getListOfUserFavoriteArtists();
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
