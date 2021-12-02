package edu.vt.Pojos;

import edu.vt.EntityBeans.UserComment;
import edu.vt.EntityBeans.UserRating;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static edu.vt.globals.Constants.API_CONTROLLER;
import static edu.vt.globals.Constants.EMBED_URI;

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
        JSONObject body = new JSONObject(json);
        this.id = body.optString("id", "");
        this.albumType = body.optString("album_type", "");
        this.totalTracks = body.optInt("total_tracks", 0);
        if (body.getJSONArray("images").length() > 0)
            this.imageUrl = body.getJSONArray("images").getJSONObject(0).optString("url", "");
        else
            this.imageUrl = "https://i.scdn.co/image/ab6761610000e5eb2dc40ac263ef07c16a95af4e";
        this.name = body.optString("name", "");
        this.releaseDate = body.optString("release_date", "");

        JSONArray artistsArray = body.getJSONArray("artists");
        JSONArray tracksArray = body.getJSONObject("tracks").getJSONArray("items");
        this.artists = new ArrayList();
        this.tracks = new ArrayList();

        String artistsAsString = "";
        for (int i = 0; i < artistsArray.length()-1; i++) {
            artistsAsString+=artistsArray.getJSONObject(i).optString("id") + ",";
        }
        artistsAsString+=artistsArray.getJSONObject(artistsArray.length()-1).optString("id") ;
        this.artists = API_CONTROLLER.requestSeveralArtists(artistsAsString);

        String tracksAsString = "";
        for (int i = 0; i < tracksArray.length()-1; i++) {
            tracksAsString+=tracksArray.getJSONObject(i).optString("id") + ",";
        }
        tracksAsString+=tracksArray.getJSONObject(tracksArray.length()-1).optString("id") ;
        this.tracks = API_CONTROLLER.requestSeveralTracks(tracksAsString);
    }

    public Album(JSONObject object) {
        this.id = object.optString("id", "");
        this.albumType = object.optString("album_type", "");
        this.totalTracks = object.optInt("total_tracks", 0);
        this.imageUrl = object.getJSONArray("images").getJSONObject(0).optString("url", "");
        this.name = object.optString("name", "");
        this.releaseDate = object.optString("release_date", "");
    }

    public Album(String id, String name, String imageUrl, String releaseDate, Integer totalTracks) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.releaseDate = releaseDate;
        this.totalTracks = totalTracks;
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
        if (artists == null)
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