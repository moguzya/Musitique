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

@Named("searchController")
@SessionScoped
public class SearchController implements Serializable {
    private List<Album> albums;
    private List<Artist> artists;
    private List<Track> tracks;
    private String searchedText;

    public String search()
    {
        API_CONTROLLER.requestSearch(searchedText);
        return "/NewReleases/SearchResults?faces-redirect=true";
    }

    public List<Album> getAlbums() {
        albums = API_CONTROLLER.getSearchedAlbums();
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public List<Artist> getArtists() {
        artists = API_CONTROLLER.getSearchedArtists();
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public List<Track> getTracks() {
        tracks = API_CONTROLLER.getSearchedTracks();
        return tracks;
    }

    public String getSearchedText() {
        return searchedText;
    }

    public void setSearchedText(String searchedText) {
        this.searchedText = searchedText;
    }
}
