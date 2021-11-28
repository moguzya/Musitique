package edu.vt.Pojos;

import java.util.Iterator;
import java.util.List;

import static edu.vt.globals.Constants.EMBED_URI;

//TODO
public class Track {

    private String id;
    private String name;
    private Album album;
    private List<Artist> artists;
    private Integer durationMs;
    private Boolean explicit;
    private String imageUrl;

    public Track(String json) {
    }


    public Track(String name,Integer durationMs,Boolean explicit,String imageUrl) {
        this.name = name;
        this.durationMs = durationMs;
        this.explicit = explicit;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public Integer getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(Integer durationMs) {
        this.durationMs = durationMs;
    }

    public Boolean getExplicit() {
        return explicit;
    }

    public void setExplicit(Boolean explicit) {
        this.explicit = explicit;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public String getEmbedUri(){
        return EMBED_URI + "track/" + id;
    }
}