package edu.vt.NewReleases;

import edu.vt.Pojos.Album;
import edu.vt.Pojos.Artist;
import edu.vt.Pojos.Track;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static edu.vt.globals.Constants.API_CONTROLLER;

@Named("exploreController")
@SessionScoped
public class ExploreController implements Serializable {
    private List<Album> albums;
    private List<Artist> artists;
    private List<Track> tracks;

    @PostConstruct
    public void init() {
        API_CONTROLLER.requestRecommendations();
        albums = getAlbums();
        artists = getArtists();
        tracks = getTracks();
    }

    public String explore() {
//        init();
        return "/NewReleases/Explore.xhtml";
    }

    public List<Album> getAlbums() {
        API_CONTROLLER.requestRecommendations();
        albums = API_CONTROLLER.getRecommendedAlbums();
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public List<Artist> getArtists() {
        API_CONTROLLER.requestRecommendations();
        artists = API_CONTROLLER.getRecommendedArtists();
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public List<Track> getTracks() {
        API_CONTROLLER.requestRecommendations();
        tracks = API_CONTROLLER.getRecommendedTracks();
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }
}
