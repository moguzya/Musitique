/*
 * Created by Osman Balci on 2021.9.10
 * Copyright © 2021 Osman Balci. All rights reserved.
 */
package edu.vt.NewReleases;

import edu.vt.Pojos.Album;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named("releaseController")
@SessionScoped
public class ReleaseController implements Serializable {
    private List<Album> listOfNewReleases;
    private Album selected;

    @PostConstruct
    public void init() {

        selected = null;
        //TODO change the following
        //listOfNewReleases = new ArrayList<>();
        listOfNewReleases = tempReadData();
//        try {
//            for (int tmdbPageNumber = 1; tmdbPageNumber < 2; tmdbPageNumber++) {
//
//                String tmdbNowPlayingWebServiceUrl = Constants.TMDB_API_MOVIE_BASE_URL + "now_playing?api_key=" + Constants.TMDB_API_KEY + "&page=" + tmdbPageNumber;
//
//                String nowPlayingResultsJsonData = Methods.readUrlContent(tmdbNowPlayingWebServiceUrl);
//
//                JSONObject resultsJsonObject = new JSONObject(nowPlayingResultsJsonData);
//
//                JSONArray jsonArrayMovies = resultsJsonObject.getJSONArray("results");
//
//                int index = 0;
//
//                if (jsonArrayMovies.length() > index) {
//
//                    while (jsonArrayMovies.length() > index) {
//
//                        Song song = new Song(id, title, artist, album, description, link, releaseDate, imageUrl, duration_ms);
//                        listOfNewReleases.add(song);
//                        index++;
//                    }
//
//                } else {
//                    Methods.showMessage("Information", "No API response!",
//                            "Spotify New Releases API is unreachable!");
//                    return;
//                }
//            }
//
//        } catch (Exception ex) {
//            // Take no action
//        }
    }


    private List<Album> tempReadData() {
        List<Album> tempListOfNewReleases;
        tempListOfNewReleases = new ArrayList<>();
        tempListOfNewReleases.add(new Album("4tZwfgrHOc3mvqYlEYSvVi", "Gustaf & Viktor Norén", "https://i.scdn.co/image/ab67616d00004851efdaf87d5ea59307b4d530a3", "2006-06-19", 5));
        tempListOfNewReleases.add(new Album("3a9qv6NLHnsVxJUtKOMHvD", "Så mycket bättre 2021 - Tolkningarna", "https://i.scdn.co/image/ab67616d00001e024a94b3678908c6524ec7c2ad", "2016-06-19", 15));
        tempListOfNewReleases.add(new Album("4OEnpg5ubhg6OQ4M2ZjtsL", "Most People (with Lukas Graham)", "https://i.scdn.co/image/ab67616d00004851efdaf87d5ea59307b4d530a3", "2026-06-19", 25));
        tempListOfNewReleases.add(new Album("1TT6gRprLQ5vSWgoWpyKfR", "Donda (Deluxe)", "https://i.scdn.co/image/ab67616d00004851efdaf87d5ea59307b4d530a3", "2036-06-19", 35));
        return tempListOfNewReleases;
    }

    public List<Album> getListOfNewReleases() {
        return listOfNewReleases;
    }

    public void setListOfNewReleases(List<Album> listOfNewReleases) {
        this.listOfNewReleases = listOfNewReleases;
    }

    public Album getSelected() {
        return selected;
    }

    public void setSelected(Album selected) {
        this.selected = selected;
    }

}
