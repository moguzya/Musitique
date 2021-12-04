package edu.vt.Pojos;

import java.util.ArrayList;
import java.util.List;

public class Results {
    List<Album> albums;
    List<Artist> artists;
    List<Track> tracks;

    public Results(List<Album> albums, List<Artist> artists, List<Track> tracks) {
        this.albums = albums;
        this.artists = artists;
        this.tracks = tracks;
    }

    public Results() {
        this.albums = new ArrayList<>();
        this.artists = new ArrayList<>();
        this.tracks = new ArrayList<>();
    }

    public Results(List<Track> tracks) {
        this.tracks = tracks;
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

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }
}
