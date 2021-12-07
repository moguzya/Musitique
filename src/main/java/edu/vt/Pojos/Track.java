package edu.vt.Pojos;

import edu.vt.controllers.util.JsfUtil;
import edu.vt.globals.Methods;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static edu.vt.globals.Constants.*;
import static edu.vt.globals.Constants.CLIENT;

public class Track {

    private String id;
    private String name;
    private Album album;
    private List<Artist> artists;
    private Integer durationMs;
    private Boolean explicit;
    private String imageUrl;

    public Track() {
    }

    public Track(String json) {
        JSONObject body = new JSONObject(json);
        this.id = body.optString("id", "");
        this.name = body.optString("name", "");

        JSONObject album = body.getJSONObject("album");
        this.album = new Album(album);


        artists = new ArrayList<>();
        JSONArray artistsArray = body.getJSONArray("artists");
        for (int i = 0; i < artistsArray.length() - 1; i++) {
            Artist artist = new Artist();
            artist.setId(artistsArray.getJSONObject(i).optString("id"));
            artist.setName(artistsArray.getJSONObject(i).optString("name"));
            artists.add(artist);
        }

        this.durationMs = body.optInt("duration_ms", 0);
        this.explicit = body.optBoolean("explicit", false);
        if (album.getJSONArray("images").length() > 0)
            this.imageUrl = album.getJSONArray("images").getJSONObject(0).optString("url", "");
        else
            this.imageUrl = "https://i.scdn.co/image/ab6761610000e5eb2dc40ac263ef07c16a95af4e";
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

    public String getDurationAsString() {

        DateFormat simple = new SimpleDateFormat("mm:ss");
        Date result = new Date(durationMs);
        return simple.format(result);
//        Integer minutes = durationMs / 60000;
//        Integer seconds = (durationMs / 1000)%60;
//        return minutes.toString() + ":" + seconds;
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
        return EMBED_URI + "track/" + id;
    }
}