package edu.vt.Pojos;

import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static edu.vt.globals.Constants.API_CONTROLLER;
import static edu.vt.globals.Constants.EMBED_URI;

public class Track {

    private String id;
    private String name;
    private Album album;
    private List<Artist> artists;
    private Integer durationMs;
    private Boolean explicit;
    private String imageUrl;

    public Track(String json, Boolean subcall) {
        JSONObject body = new JSONObject(json);
        this.id = body.optString("id", "");
        this.name = body.optString("name", "");

        JSONObject album = body.getJSONObject("album");
        this.album = new Album(album);

        JSONArray artistsArray = body.getJSONArray("artists");

        StringBuilder artistsAsString = new StringBuilder();
        for (int i = 0; i < artistsArray.length() - 1; i++) {
            artistsAsString.append(artistsArray.getJSONObject(i).optString("id")).append(",");
        }
        artistsAsString.append(artistsArray.getJSONObject(artistsArray.length() - 1).optString("id"));

        this.artists = new ArrayList();
        if (!subcall)
            this.artists = API_CONTROLLER.requestSeveralArtists(artistsAsString.toString());

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