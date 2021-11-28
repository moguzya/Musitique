package edu.vt.Pojos;

import java.util.Iterator;
import java.util.List;

import static edu.vt.globals.Constants.EMBED_URI;

//TODO
public class Artist {

    private String id;
    private String name;
    private List<String> genres;
    private Integer followers;
    private String imageUrl;

    public Artist(String json) {
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

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public Integer getFollowers() {
        return followers;
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