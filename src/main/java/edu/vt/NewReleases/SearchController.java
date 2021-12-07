/*
 * Copyright (c) 2021. Musitique App was developed by Mehmet Oguz Yardimci, Vibhavi Peiris, and Joseph Conwell as CS5704 Software Engineering course assignment.
 *
 * https://www.linkedin.com/in/oguzyardimci/
 * https://www.linkedin.com/in/vibhavipeiris/?originalSubdomain=ca
 * https://conwell.info/
 */

package edu.vt.NewReleases;

import edu.vt.Pojos.Album;
import edu.vt.Pojos.Artist;
import edu.vt.Pojos.Track;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.globals.Methods;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static edu.vt.globals.Constants.ACCESS_TOKEN;
import static edu.vt.globals.Constants.CLIENT;


@Named("searchController")
@SessionScoped
public class SearchController implements Serializable {
    private List<Album> albums;
    private List<Artist> artists;
    private List<Track> tracks;
    private String searchedText = "";

    public void requestSearch() {
        System.out.println("I called requestSearch");
        if ((searchedText!= null) && (searchedText.length() != 0)) {
            tracks = new ArrayList<>();
            artists = new ArrayList<>();
            albums = new ArrayList<>();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.spotify.com/v1/search?q=" + searchedText.replaceAll(" ", "%20") + "&type=track,artist,album&limit=24&market=US"))
                    .timeout(Duration.ofMinutes(1))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + ACCESS_TOKEN)
                    .GET()
                    .build();
            try {
                HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    JSONArray tracksArray = new JSONObject(response.body()).getJSONObject("tracks").getJSONArray("items");
                    JSONArray albumsArray = new JSONObject(response.body()).getJSONObject("albums").getJSONArray("items");
                    JSONArray artistsArray = new JSONObject(response.body()).getJSONObject("artists").getJSONArray("items");

                    for (int i = 0; i < tracksArray.length(); i++) {
                        tracks.add(new Track(tracksArray.getJSONObject(i).toString()));
                    }

                    for (int i = 0; i < albumsArray.length(); i++) {
                        albums.add(new Album(albumsArray.getJSONObject(i).toString()));
                    }

                    for (int i = 0; i < artistsArray.length(); i++) {
                        artists.add(new Artist(artistsArray.getJSONObject(i).toString()));
                    }

                    return;
                } else if (response.statusCode() == 401) {
                    Methods.requestToken();
                    requestSearch();
                    return;
                } else if (response.statusCode() == 429) {
                    JsfUtil.addErrorMessage("Api rate limit exceeded!");
                    return;
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
                return;
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, "A Persistence Error Occurred!");
                return;
            }
        }
        else
        {
            tracks = new ArrayList<>();
            artists = new ArrayList<>();
            albums = new ArrayList<>();
            return;
        }
        JsfUtil.addErrorMessage("Unexpected error occurred!");
        return;
    }

    public void clearSearch() {
        albums = null;
        artists = null;
        tracks = null;
    }

    public List<Album> getAlbums() {
        if (albums == null)
            requestSearch();
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public List<Artist> getArtists() {
        if (artists == null)
            requestSearch();
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public List<Track> getTracks() {
        if (tracks == null)
            requestSearch();
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public String getSearchedText() {
        return searchedText;
    }

    public void setSearchedText(String searchedText) {
        this.searchedText = searchedText;
    }
}
