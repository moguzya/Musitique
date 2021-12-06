package edu.vt.Pojos;

import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Artist {

    private String id;
    private String name;
    private List<String> genres;
    private Integer followers;
    private String imageUrl;
    private List<Track> artistTopTracks;

    public Artist() {
    }

    public Artist(String json) {
        JSONObject body = new JSONObject(json);
        this.id = body.optString("id", "");
        this.name = body.optString("name", "");


        if (body.has("genres")) {
            JSONArray genresArray = body.getJSONArray("genres");
            this.genres = new ArrayList();

            for (int i = 0; i < genresArray.length(); i++) {
                this.genres.add(genresArray.optString(i, ""));
            }

            this.followers = body.getJSONObject("followers").optInt("total", 0);

            if (body.getJSONArray("images").length() > 0)
                this.imageUrl = body.getJSONArray("images").getJSONObject(0).optString("url", "");
            else
                this.imageUrl = "https://i.imgur.com/7dUML1G.png";
        } else {
            this.imageUrl = "https://i.imgur.com/7dUML1G.png";
        }
    }

    public Artist(String id, String name, Integer followers, String imageUrl, List<String> genres) {
        this.id = id;
        this.name = name;
        this.followers = followers;
        this.imageUrl = imageUrl;
        this.genres = genres;
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

    public List<Track> getArtistTopTracks() {
        return artistTopTracks;
    }

    public void setArtistTopTracks(List<Track> artistTopTracks) {
        this.artistTopTracks = artistTopTracks;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public Integer getFollowers() {
        return followers;
    }

    public String getFollowersAsString() {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(followers);
    }
    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getGenresListAsString() {
        String genresAsString = "";
        Iterator<String> iterator = genres.iterator();
        while (iterator.hasNext()) {
            String genre = iterator.next();
            genresAsString += genre;
            if (iterator.hasNext()) {
                genresAsString += ", ";
            }
        }
        return genresAsString;
    }
}