/*
 * Created by Osman Balci on 2021.9.10
 * Copyright © 2021 Osman Balci. All rights reserved.
 */
package edu.vt.NewReleases;

import edu.vt.globals.Constants;
import edu.vt.globals.Methods;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

@Named("ReleaseController")
@SessionScoped
public class ReleaseController implements Serializable {
    private List<Track> listOfNewReleases;
    private Track selected;

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


    private List<Track> tempReadData() {
        List<Track> tempListOfNewReleases;
        tempListOfNewReleases = new ArrayList<>();
        tempListOfNewReleases.add(
                new Track("1", "title1", "Gustaf & Viktor Norén", "12Chz98pHFMPJEknJQMWvI", "album name", "link",
                        "2006-06-19", "https://i.scdn.co/image/ab67616d00004851efdaf87d5ea59307b4d530a3", "https://open.spotify.com/embed/album/3Gg0rgnLUn8kdctvVyAR3X?utm_source=generator", 304840));
        tempListOfNewReleases.add(
                new Track("2", "title2", "Så mycket bättre 2021 - Tolkningarna", "12Chz98pHFMPJEknJQMWvI", "album name", "link",
                        "2006-06-19", "https://i.scdn.co/image/ab67616d00001e024a94b3678908c6524ec7c2ad", "https://open.spotify.com/embed/album/2SZ5OldmAx9zijGjLbSfor?utm_source=generator", 304840));
        tempListOfNewReleases.add(
                new Track("3", "title3", "Most People (with Lukas Graham)", "12Chz98pHFMPJEknJQMWvI", "album name", "link",
                        "2006-06-19", "https://i.scdn.co/image/ab67616d00004851cb70ba7ab3fe1343b5b842ef", "https://open.spotify.com/embed/album/2rU6ujofsaPM3D2HyDdcKt?utm_source=generator", 304840));
        tempListOfNewReleases.add(
                new Track("4", "title4", "Donda (Deluxe)", "12Chz98pHFMPJEknJQMWvI", "album name", "link",
                        "2006-06-19", "https://i.scdn.co/image/ab67616d0000b273c5663e50de353981ed2b1a37", "https://open.spotify.com/embed/album/2Wiyo7LzdeBCsVZiRA6vVZ?utm_source=generator", 304840));

        return tempListOfNewReleases;
    }

    public List<Track> getListOfNewReleases() {
        return listOfNewReleases;
    }

    public void setListOfNewReleases(List<Track> listOfNewReleases) {
        this.listOfNewReleases = listOfNewReleases;
    }

    public Track getSelected() {
        return selected;
    }

    public void setSelected(Track selected) {
        this.selected = selected;
    }

}
