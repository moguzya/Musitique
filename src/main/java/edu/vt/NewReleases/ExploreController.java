package edu.vt.NewReleases;

import edu.vt.EntityBeans.User;
import edu.vt.EntityBeans.UserFavoriteArtist;
import edu.vt.EntityBeans.UserGenre;
import edu.vt.FacadeBeans.UserFavoriteArtistFacade;
import edu.vt.FacadeBeans.UserGenreFacade;
import edu.vt.Pojos.Album;
import edu.vt.Pojos.Artist;
import edu.vt.Pojos.Track;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.globals.Methods;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static edu.vt.globals.Constants.ACCESS_TOKEN;
import static edu.vt.globals.Constants.CLIENT;


@Named("exploreController")
@SessionScoped
public class ExploreController implements Serializable {
    @EJB
    private UserGenreFacade userGenreFacade;

    @EJB
    private UserFavoriteArtistFacade userFavoriteArtistFacade;

    List<UserGenre> favoriteGenres;
    List<UserFavoriteArtist> favoriteArtists;
    private List<Track> tracks;

    public List<Track> getTracks() {
        if (tracks == null)
            tracks = requestRecommendations();
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public List<UserGenre> getFavoriteGenres() {

        if (favoriteGenres == null) {
            /*
            'user', the object reference of the signed-in user, was put into the SessionMap
            in the initializeSessionMap() method in LoginManager upon user's sign in.
             */
            Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
            User signedInUser = (User) sessionMap.get("user");

            // Obtain the database primary key of the signedInUser object
            Integer primaryKey = signedInUser.getId();

            favoriteGenres = userGenreFacade.findUserGenresByUserPrimaryKey(primaryKey);

        }
        return favoriteGenres;
    }

    public void setFavoriteGenres(List<UserGenre> favoriteGenres) {
        this.favoriteGenres = favoriteGenres;
    }

    public List<UserFavoriteArtist> getFavoriteArtists() {
        if (favoriteArtists == null) {

            Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
            User signedInUser = (User) sessionMap.get("user");

            // Obtain the database primary key of the signedInUser object
            Integer primaryKey = signedInUser.getId();

            favoriteArtists = userFavoriteArtistFacade.findUserFavoriteArtistsByUserPrimaryKey(primaryKey);
        }
        return favoriteArtists;
    }

    public void setFavoriteArtists(List<UserFavoriteArtist> favoriteArtists) {
        this.favoriteArtists = favoriteArtists;
    }

    public List<Track> requestRecommendations() {
        System.out.println("I called requestRecommendations");

        String queryGenre = getFavoriteGenres().stream().
                map(i -> String.valueOf(i.getGenre())).
                collect(Collectors.joining(","));
        String queryArtist = getFavoriteArtists().stream().
                map(i -> String.valueOf(i.getEntityId())).
                collect(Collectors.joining(","));


        if (queryGenre.length() == 0 && queryArtist.length() == 0)
            queryGenre = "rock";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.spotify.com/v1/recommendations?limit=24&seed_artists=" + queryArtist + "&seed_genres=" + queryGenre))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .GET()
                .build();

        try {
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONArray recommendationArray = new JSONObject(response.body()).getJSONArray("tracks");
                List<Track> tracks = new ArrayList<>();

                for (int i = 0; i < recommendationArray.length(); i++) {
                    tracks.add(new Track(recommendationArray.getJSONObject(i).toString()));
                }

                return tracks;
            } else if (response.statusCode() == 401) {
                Methods.requestToken();
                return requestRecommendations();
            } else if (response.statusCode() == 429) {
                JsfUtil.addErrorMessage("Api rate limit exceeded!");
                return new ArrayList<>();
            }

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
            return new ArrayList<>();
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage(ex, "A Persistence Error Occurred!");
            return new ArrayList<>();
        }
        return new ArrayList<>();
    }
}
