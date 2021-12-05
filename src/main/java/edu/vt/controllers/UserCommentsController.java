/*
 * Created by Osman Balci on 2021.7.15
 * Copyright © 2021 Osman Balci. All rights reserved.
 */
package edu.vt.controllers;

import edu.vt.EntityBeans.User;
import edu.vt.EntityBeans.UserComment;
import edu.vt.FacadeBeans.CommentFacade;
import edu.vt.Pojos.Album;
import edu.vt.Pojos.Artist;
import edu.vt.Pojos.Track;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.globals.Methods;
import edu.vt.controllers.util.JsfUtil.PersistAction;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import static edu.vt.globals.Constants.ACCESS_TOKEN;
import static edu.vt.globals.Constants.CLIENT;

@Named("userCommentsController")
@SessionScoped
public class UserCommentsController implements Serializable {
    List<Album> listOfAlbums;
    List<Track> listOfTracks;
    List<Artist> listOfArtists;
    
    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */
    private UserComment selected;
    private List<UserComment> listOfUserComments = null;

    /*
    The @EJB annotation directs the EJB Container Manager to inject (store) the object reference of the
    CommentFacade bean into the instance variable 'commentFacade' after it is instantiated at runtime.
     */
    @EJB
    private CommentFacade commentFacade;

    public UserComment getSelected() {
        return selected;
    }

    /*
    =========================
    Getter and Setter Methods
    =========================
     */
    public void setSelected(UserComment selected) {
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
    public List<UserComment> getListOfUserComments() {

        if (listOfUserComments == null) {
            Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
            User signedInUser = (User) sessionMap.get("user");

            // Obtain the database primary key of the signedInUser object
            Integer primaryKey = signedInUser.getId();

            // Obtain only those videos from the database that belong to the signed-in user
            listOfUserComments = commentFacade.findUserCommentByUserPrimaryKey(primaryKey);

            List<String> AlbumsIds = new ArrayList<>();
            List<String> ArtistsIds = new ArrayList<>();
            List<String> TracksIds = new ArrayList<>();

            //move each entity type into seperate lists to that we can bulk get the data
            for (UserComment listOfUserComment : listOfUserComments) {
                String currEntityType = listOfUserComment.getEntityType();
                switch (currEntityType) {
                    case "ALBUM":
                        AlbumsIds.add(listOfUserComment.getEntityId());
                    case "ARTIST":
                        ArtistsIds.add(listOfUserComment.getEntityId());
                    case "TRACK":
                        TracksIds.add(listOfUserComment.getEntityId());
                }
            }
            if (!(String.join(",", AlbumsIds).equals("")))
                listOfAlbums = requestSeveralAlbums(String.join(",", AlbumsIds));
            if (!(String.join(",", ArtistsIds).equals("")))
                listOfArtists = requestSeveralArtists(String.join(",", ArtistsIds));
            if (!(String.join(",", TracksIds).equals("")))
                listOfTracks = requestSeveralTracks(String.join(",", TracksIds));
        }
        return listOfUserComments;
    }

    public void setListOfUserComments(List<UserComment> listOfUserComments) {
        this.listOfUserComments = listOfUserComments;
    }

    public List<Album> requestSeveralAlbums(String albumIds) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.spotify.com/v1/albums?ids=" + albumIds))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .GET()
                .build();
        try {
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                List<Album> albums = new ArrayList<>();
                JSONArray albumArray = new JSONObject(response.body()).getJSONArray("albums");

                for (int i = 0; i < albumArray.length(); i++) {
                    if (!Objects.equals(albumArray.get(i).toString(), "null")) {
                        Album a = new Album(albumArray.getJSONObject(i).toString());
                        albums.add(a);
                    }
                }
                return albums;
            } else if (response.statusCode() == 401) {
                Methods.requestToken();
                return requestSeveralAlbums(albumIds);
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

    public List<Artist> requestSeveralArtists(String artistIds) {
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

    public void clearListOfUserComments() {
        this.listOfUserComments = null;
    }

    public Album findAlbum(String entityId) {
        if (listOfAlbums != null) {
            for (Album album : listOfAlbums) {
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
        if (listOfTracks != null) {
            for (Track track : listOfTracks) {
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
        if (listOfArtists != null) {

            for (Artist artist : listOfArtists) {
                if (artist != null) {
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
            case "ARTIST":
                Artist artist = findArtist(userComment.getEntityId());
                if (artist != null) {
                    return artist.getName();
                }
        }
        return "";
    }

    /*
    *************************************
    UPDATE Selected Comment in the Database
    *************************************
     */
    public void update() {
        Methods.preserveMessages();

        persist(PersistAction.UPDATE, "Comment was Successfully Updated!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The UPDATE operation is successfully performed.
            selected = null;        // Remove selection
            listOfUserComments = null;    // Invalidate listOfMovies to trigger re-query.
        }
    }

    /*
    ***************************************
    DELETE Selected Comment from the Database
    ***************************************
     */
    public void destroy() {
        Methods.preserveMessages();

        persist(PersistAction.DELETE, "Comment was Successfully Deleted!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The DELETE operation is successfully performed.
            selected = null;        // Remove selection
            listOfUserComments = null;    // Invalidate listOfMovies to trigger re-query.
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
                    JsfUtil.addErrorMessage(ex, "A persistence error occurred.");
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, "A persistence error occurred.");
            }
        }
    }

}
