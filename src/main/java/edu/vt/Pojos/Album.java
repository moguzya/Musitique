package edu.vt.Pojos;

import edu.vt.EntityBeans.UserComment;
import edu.vt.EntityBeans.UserRating;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.globals.Methods;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static edu.vt.globals.Constants.*;
import static edu.vt.globals.Constants.CLIENT;

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

        this.artists = new ArrayList();
        for (int i = 0; i < artistsArray.length(); i++) {
            Artist artist = new Artist();
            artist.setId(artistsArray.getJSONObject(i).optString("id"));
            artist.setName(artistsArray.getJSONObject(i).optString("name"));
            artists.add(artist);
        }
        if (body.has("tracks")) {
            JSONArray tracksArray = body.getJSONObject("tracks").getJSONArray("items");
            this.tracks = new ArrayList();

            for (int i = 0; i < tracksArray.length() ; i++) {
                Track track = new Track();
                track.setId(tracksArray.getJSONObject(i).optString("id"));
                track.setName(tracksArray.getJSONObject(i).optString("name"));
                tracks.add(track);
            }
        }
    }

    public Album(JSONObject object) {
        this.id = object.optString("id", "");
        this.albumType = object.optString("album_type", "");
        this.totalTracks = object.optInt("total_tracks", 0);
        this.imageUrl = object.getJSONArray("images").getJSONObject(0).optString("url", "");
        this.name = object.optString("name", "");
        this.releaseDate = object.optString("release_date", "");
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

    public String getArtistNames()
    {
        return artists.stream().map(p -> String.valueOf(p.getName()))
                .collect(Collectors.joining(", "));
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

    public String getTracksCountAsString() {
        if (totalTracks > 1)
            return totalTracks + " Tracks in Album";
        return "Only " + totalTracks + " Track in Album";
    }
}