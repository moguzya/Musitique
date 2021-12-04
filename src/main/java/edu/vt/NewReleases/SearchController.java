package edu.vt.NewReleases;

import edu.vt.Pojos.Album;
import edu.vt.Pojos.Artist;
import edu.vt.Pojos.Results;
import edu.vt.Pojos.Track;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Named("searchController")
@SessionScoped
public class SearchController implements Serializable {
    private List<Album> albums;
    private List<Artist> artists;
    private List<Track> tracks;

    public String search(Results results) {
        this.albums = results.getAlbums();
        this.artists = results.getArtists();
        this.tracks = results.getTracks();
        return "/newReleases/SearchResults?faces-redirect=true";
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public List<Artist> getArtists() {

        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public List<Track> getTracks() {

        return tracks;
    }

}
