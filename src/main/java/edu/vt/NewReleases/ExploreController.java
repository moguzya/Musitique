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

//    @PostConstruct
//    public void init() {
//
//        //TODO change the following
//        //listOfNewReleases = new ArrayList<>();
//        albums = readAlbums();
//        artists = readArtists();
//        tracks = readTracks();
//    }
    public String explore(){
        //TODO do explore
        return "/NewReleases/Explore.xhtml";
    }

    public List<Album> getAlbums() {
        if (albums == null) {
            albums = API_CONTROLLER.requestAlbumRecommendations();
        }
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public List<Artist> getArtists() {
        if (artists == null) {
            artists = API_CONTROLLER.requestArtistRecommendations();
        }
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public List<Track> getTracks() {
        if (tracks == null) {
            tracks = API_CONTROLLER.requestTrackRecommendations();
        }
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }
}
