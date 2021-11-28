package edu.vt.Pojos;

import edu.vt.EntityBeans.UserComment;
import edu.vt.EntityBeans.UserRating;
import edu.vt.globals.Constants;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

import static edu.vt.globals.Constants.EMBED_URI;

//TODO
public class Album {

    private String id;
    private String albumType;
    private Integer totalTracks;
    private String imageUrl;
    private String name;
    private String releaseDate;
    private List<Artist> artists;
    private List<Track> tracks;
    private List<UserComment> comments;
    private List<UserRating> ratings;

    public Album(String json) {
//        parseid()
//        parsealbumtype()
//
//        for
//            artists.add(new Artist(stringpart))
//        for
//            tracks.add(new Track(stringpart))
    }

    public Album() {

    }

    public Album(String id, String name, String imageUrl, String releaseDate, Integer totalTracks) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.releaseDate = releaseDate;
        this.totalTracks = totalTracks;
    }
    public Album(String id, String name, String imageUrl, String releaseDate, Integer totalTracks,List<Track> tracks) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.releaseDate = releaseDate;
        this.totalTracks = totalTracks;
        this.tracks = tracks;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlbumType() {
        return albumType;
    }

    public void setAlbumType(String albumType) {
        this.albumType = albumType;
    }

    public Integer getTotalTracks() {
        return totalTracks;
    }

    public void setTotalTracks(Integer totalTracks) {
        this.totalTracks = totalTracks;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public String getArtistsListAsString() {
        if(artists == null)
            return "Artist list cannot be found";
        String artistsAsString = "";
        Iterator<Artist> iterator = artists.iterator();
        while (iterator.hasNext()) {
            Artist artist = iterator.next();
            artistsAsString += artist.getName();
            if (iterator.hasNext()) {
                artistsAsString += ", ";
            }
        }
        return artistsAsString == "" ? "Artist list cannot be found" : artistsAsString;
    }

    public String getEmbedUri() {
        return EMBED_URI + "album/" + id;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }
}