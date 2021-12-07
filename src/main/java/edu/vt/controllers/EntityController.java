
package edu.vt.controllers;


import edu.vt.EntityBeans.User;
import edu.vt.EntityBeans.UserComment;
import edu.vt.EntityBeans.UserRating;
import edu.vt.Pojos.EntityType;
import edu.vt.Pojos.Album;
import edu.vt.FacadeBeans.CommentFacade;
import edu.vt.FacadeBeans.RatingFacade;
import edu.vt.Pojos.Artist;
import edu.vt.Pojos.Track;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.controllers.util.JsfUtil.PersistAction;
import edu.vt.globals.Methods;
import org.primefaces.event.RateEvent;
import edu.vt.EntityBeans.UserFavoriteArtist;
import edu.vt.FacadeBeans.UserFavoriteArtistFacade;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static edu.vt.globals.Constants.ACCESS_TOKEN;
import static edu.vt.globals.Constants.CLIENT;


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
The @SessionScoped annotation preserves the values of the entityController
object's instance variables across multiple HTTP request-response cycles
as long as the user's established HTTP session is alive.
*/
@SessionScoped

/*
-----------------------------------------------------------------------------
Marking the EntityController class as "implements Serializable" implies that
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

    @EJB
    private UserFavoriteArtistFacade userFavoriteArtistFacade;

    private Track selectedTrack;
    private Album selectedAlbum;
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

    public String toSelectedEntityPage(String entityId, String entityType) {

        switch (entityType) {
            case "ALBUM": {
                Album album = new Album();
                album.setId(entityId);
                return toAlbumPage(album);
            }
            case "ARTIST": {
                Artist artist = new Artist();
                artist.setId(entityId);
                return toArtistPage(artist);
            }
            case "TRACK": {
                Track track = new Track();
                track.setId(entityId);
                return toTrackPage(track);
            }
        }
        return "";
    }

    public String toAlbumPage(Album selectedAlbum) {
        unselectArtist();
        unselectTrack();
        selectedEntityType = EntityType.ALBUM;

        selectedComment = null;
        listOfComments = null;
        userRating = null;
        newCommentText = null;

        this.selectedAlbum = requestAlbum(selectedAlbum.getId());

        List<String> artistIds = new ArrayList<>();
        for (Artist artist : this.selectedAlbum.getArtists()) {
            artistIds.add(artist.getId());
        }

        if (!artistIds.isEmpty()) {
            List<Artist> artists = new ArrayList<>();
            for (int i = 0; i < artistIds.size(); i = i + 20) {
                artists.addAll(requestSeveralArtists(artistIds.stream().skip(i).limit(20).collect(Collectors.joining(","))));
            }
            this.selectedAlbum.setArtists(artists);
        }

        List<String> trackIds = new ArrayList<>();
        for (Track track : this.selectedAlbum.getTracks()) {
            trackIds.add(track.getId());
        }

        if (!trackIds.isEmpty()) {
            List<Track> tracks = new ArrayList<>();
            for (int i = 0; i < trackIds.size(); i = i + 20) {
                tracks.addAll(requestSeveralTracks(trackIds.stream().skip(i).limit(20).collect(Collectors.joining(","))));
            }
            this.selectedAlbum.setTracks(tracks);
        }

        userRating = getUserRating();
        listOfComments = getListOfComments();

        return "/standalonePages/Album?faces-redirect=true";
    }

    public String toArtistPage(Artist selectedArtist) {
        unselectAlbum();
        unselectTrack();
        selectedEntityType = EntityType.ARTIST;

        selectedComment = null;
        listOfComments = null;
        userRating = null;
        newCommentText = null;

        this.selectedArtist = requestArtist(selectedArtist.getId());
        this.selectedArtist.setArtistTopTracks(requestTopTracksFromArtist(this.selectedArtist.getId()));

        userRating = getUserRating();
        listOfComments = getListOfComments();
        return "/standalonePages/Artist?faces-redirect=true";
    }

    public String toTrackPage(Track selectedTrack) {
        unselectAlbum();
        unselectArtist();
        selectedEntityType = EntityType.TRACK;
        this.selectedTrack = requestTrack(selectedTrack.getId());

        selectedComment = null;
        listOfComments = null;
        userRating = null;
        newCommentText = null;

        this.selectedTrack.setAlbum(requestAlbum(this.selectedTrack.getAlbum().getId()));

        List<String> artistIds = new ArrayList<>();
        for (Artist artist : this.selectedTrack.getArtists()) {
            artistIds.add(artist.getId());
        }

        if (!artistIds.isEmpty()) {
            List<Artist> artists = new ArrayList<>();
            for (int i = 0; i < artistIds.size(); i = i + 20) {
                artists.addAll(requestSeveralArtists(artistIds.stream().skip(i).limit(20).collect(Collectors.joining(","))));
            }
            this.selectedTrack.setArtists(artists);
        }

        userRating = getUserRating();
        listOfComments = getListOfComments();
        return "/standalonePages/Track?faces-redirect=true";
    }

    public List<Artist> requestSeveralArtists(String artistIds) {
        System.out.println("I called requestSeveralArtists in ratings");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.spotify.com/v1/artists?ids=" + artistIds))
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
                return artists;
            } else if (response.statusCode() == 401) {
                Methods.requestToken();
                return requestSeveralArtists(artistIds);
            } else if (response.statusCode() == 429) {
                JsfUtil.addErrorMessage("Api rate limit exceeded!");
                return new ArrayList<>();

            }
        } catch (IOException | InterruptedException e) {
            JsfUtil.addErrorMessage(e.toString());
            return new ArrayList<>();

        }

        return new ArrayList<>();
    }

    public List<Track> requestSeveralTracks(String trackIds) {
        System.out.println("I called requestSeveralTracks in ratings");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.spotify.com/v1/tracks?ids=" + trackIds))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .GET()
                .build();

        try {
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<Track> tracks = new ArrayList<>();
                JSONArray trackArray = new JSONObject(response.body()).getJSONArray("tracks");

                for (int i = 0; i < trackArray.length(); i++) {
                    if (!Objects.equals(trackArray.get(i).toString(), "null")) {
                        Track a = new Track(trackArray.getJSONObject(i).toString());
                        tracks.add(a);
                    }
                }
                return tracks;
            } else if (response.statusCode() == 401) {
                Methods.requestToken();
                return requestSeveralTracks(trackIds);
            } else if (response.statusCode() == 429) {
                JsfUtil.addErrorMessage("Api rate limit exceeded!");
                return new ArrayList<>();

            }
        } catch (IOException | InterruptedException e) {
            JsfUtil.addErrorMessage(e.toString());
            return new ArrayList<>();

        }

        return new ArrayList<>();
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

    public List<Track> requestTopTracksFromArtist(String artistId) {
        System.out.println("I called requestTopTracksFromArtist");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.spotify.com/v1/artists/" + artistId + "/top-tracks?market=US"))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .GET()
                .build();

        try {
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<Track> tracks = new ArrayList<>();
                JSONArray tracksArray = new JSONObject(response.body()).getJSONArray("tracks");

                int length = Math.min(tracksArray.length(), 6);

                for (int i = 0; i < length; i++) {
                    Track t = new Track(tracksArray.getJSONObject(i).toString());
                    tracks.add(t);
                }

                return tracks;
            } else if (response.statusCode() == 401) {
                Methods.requestToken();
                return requestTopTracksFromArtist(artistId);
            } else if (response.statusCode() == 429) {
                JsfUtil.addErrorMessage("Api rate limit exceeded!");
                return new ArrayList<>();
            }
        } catch (IOException | InterruptedException e) {
            JsfUtil.addErrorMessage(e.getMessage());
            return new ArrayList<>();
        }
        JsfUtil.addErrorMessage("Unexpected error occurred!");
        return new ArrayList<>();
    }

    public UserFavoriteArtistFacade getUserFavoriteArtistFacade() {
        return userFavoriteArtistFacade;
    }

    public void setUserFavoriteArtistFacade(UserFavoriteArtistFacade userFavoriteArtistFacade) {
        this.userFavoriteArtistFacade = userFavoriteArtistFacade;
    }

    public void createComment(UserComment comment) {
        selectedComment = comment;
        Methods.preserveMessages();
        persistComment(PersistAction.CREATE, "Comment was Successfully Created!");
        if (!JsfUtil.isValidationFailed()) {
            listOfComments = null;  // Invalidate listOfComments to trigger re-query.
            selectedComment = null;
            newCommentText = null;
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

    /*
    ************************************
    CREATE a New Comment in the Database
    ************************************
     */

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
        UserComment userComment = new UserComment(getUser(), getSelectedEntityId(), getSelectedEntityType(), newCommentText);
        createComment(userComment);
        newCommentText = "";
    }

    public void onRate(RateEvent<Integer> rateEvent) {
        userRating = ratingFacade.findUserRatingByEntityId(getSelectedEntityId(), getUser(), selectedEntityType);
        if (userRating.getRating() == -1) {
            userRating = new UserRating(getUser(), getSelectedEntityId(), rateEvent.getRating(), selectedEntityType);
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

    public Album requestAlbum(String albumId) {
        System.out.println("I called requestAlbum");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.spotify.com/v1/albums/" + albumId))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .GET()
                .build();

        try {
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return new Album(response.body());
            } else if (response.statusCode() == 401) {
                Methods.requestToken();
                return requestAlbum(albumId);
            } else if (response.statusCode() == 429) {
                JsfUtil.addErrorMessage("Api rate limit exceeded!");
            }
        } catch (IOException | InterruptedException e) {
            JsfUtil.addErrorMessage(e.toString());
        }

        return null;
    }

    public Artist requestArtist(String artistId) {
        System.out.println("I called requestArtist");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.spotify.com/v1/artists/" + artistId))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .GET()
                .build();

        try {
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return new Artist(response.body());
            } else if (response.statusCode() == 401) {
                Methods.requestToken();
                return requestArtist(artistId);
            } else if (response.statusCode() == 429) {
                JsfUtil.addErrorMessage("Api rate limit exceeded!");
            }
        } catch (IOException | InterruptedException e) {
            JsfUtil.addErrorMessage(e.toString());
        }

        return null;
    }

    public Track requestTrack(String trackId) {
        System.out.println("I called requestTrack");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.spotify.com/v1/tracks/" + trackId))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .GET()
                .build();

        try {
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return new Track(response.body());
            } else if (response.statusCode() == 401) {
                Methods.requestToken();
                return requestTrack(trackId);
            } else if (response.statusCode() == 429) {
                JsfUtil.addErrorMessage("Api rate limit exceeded!");
            }
        } catch (IOException | InterruptedException e) {
            JsfUtil.addErrorMessage(e.toString());
        }

        return null;
    }

    public List<UserFavoriteArtist> getListOfFavoriteArtists() {
        /*
        'user', the object reference of the signed-in user, was put into the SessionMap
        in the initializeSessionMap() method in LoginManager upon user's sign in.
         */
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        User signedInUser = (User) sessionMap.get("user");

        // Obtain the database primary key of the signedInUser object
        Integer primaryKey = signedInUser.getId();

        return userFavoriteArtistFacade.findUserFavoriteArtistsByUserPrimaryKey(primaryKey);
    }

    public Boolean isFavoriteArtist() {
        for (UserFavoriteArtist favoriteArtist :
                getListOfFavoriteArtists()) {
            if (favoriteArtist.getEntityId().equals(selectedArtist.getId())) {
                return true;
            }
        }
        return false;
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
    }

    public Artist getSelectedArtist() {
        return selectedArtist;
    }

    public void setSelectedArtist(Artist selectedArtist) {
        this.selectedArtist = selectedArtist;
    }

    public Track getSelectedTrack() {
        return selectedTrack;
    }

    public void setSelectedTrack(Track selectedTrack) {
        this.selectedTrack = selectedTrack;
    }

    public String getNewCommentText() {
        if (newCommentText == null)
            newCommentText = "";
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
            userRating = ratingFacade.findUserRatingByEntityId(getSelectedEntityId(), getUser(), selectedEntityType);
        }
        return userRating;
    }

    public void setUserRating(UserRating userRating) {
        this.userRating = userRating;
    }

    public long getAverageRating() {
        return Math.round(ratingFacade.findAverageRatingByEntityId(getSelectedEntityId()));
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

    public Object getEntity(String entityId, EntityType entityType) {
        switch (entityType) {
            case ALBUM:
                return selectedAlbum;
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
