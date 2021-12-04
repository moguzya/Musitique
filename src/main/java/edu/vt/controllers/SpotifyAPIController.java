/*
 * Created by Joe Conwell
 * Copyright © 2021 Joe Conwell. All rights reserved.
 */
package edu.vt.controllers;

import edu.vt.EntityBeans.UserFavoriteArtist;
import edu.vt.EntityBeans.UserGenre;
import edu.vt.Pojos.Album;
import edu.vt.Pojos.Artist;
import edu.vt.Pojos.Results;
import edu.vt.Pojos.Track;
import edu.vt.globals.Constants;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/*
---------------------------------------------------------------------------
The @Named (javax.inject.Named) annotation indicates that the objects
instantiated from this class will be managed by the Contexts and Dependency
Injection (CDI) container. The name "spotifyAPIController" is used within
Expression Language (EL) expressions in JSF (XHTML) facelets pages to
access the properties and invoke methods of this class.
---------------------------------------------------------------------------
*/
@Named("spotifyAPIController")

/*
The @SessionScoped annotation preserves the values of the SpotifyAPIController
object's instance variables across multiple HTTP request-response cycles
as long as the user's established HTTP session is alive.
*/
@SessionScoped

/*
-----------------------------------------------------------------------------
Marking the SpotifyAPIController class as "implements Serializable" implies that
instances of the class can be automatically serialized and deserialized. 

Serialization is the process of converting a class instance (object)
from memory into a suitable format for storage in a file or memory buffer, 
or for transmission across a network connection link.

Deserialization is the process of recreating a class instance (object)
in memory from the format under which it was stored.
-----------------------------------------------------------------------------
*/
public class SpotifyAPIController implements Serializable {
    /*
    ===============================
    Instance Variables (Properties)
    ===============================
    */
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    HttpRequest lastNewReleasesRequest;
    List<Album> lastNewReleasesResult;
    Results lastRecommendationsResult;
    HttpRequest lastRecommendationsRequest;
    private String accessToken;
    private List<Album> recommendedAlbums;
    private List<Artist> recommendedArtists;
    private List<Track> recommendedTracks;
    private List<Album> searchedAlbums;
    private List<Artist> searchedArtists;
    private List<Track> searchedTracks;
    private String searchedText;

    /*
    ================
    Instance Methods
    ================
    */
    public void requestToken() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://accounts.spotify.com/api/token"))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Basic " + Constants.CLIENT_AUTH)
                .POST(HttpRequest.BodyPublishers.ofString("grant_type=client_credentials&json=true"))
                .build();
        try {
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JSONObject body = new JSONObject(response.body());
                accessToken = body.optString("access_token", "");
                if (accessToken.equals(""))
                    throw new NoSuchFieldException();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Album requestAlbum(String albumId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.spotify.com/v1/albums/" + albumId))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();

        try {
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return new Album(response.body(), false);
            } else if (response.statusCode() == 401) {
                requestToken();
                return requestAlbum(albumId);
            } else if (response.statusCode() == 429) {
                System.out.println("rate limit");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println(e);
        }

        return null;
    }

    public List<Album> requestSeveralAlbums(String albumIds, Boolean subcall) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.spotify.com/v1/albums?ids=" + albumIds))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();
        try {
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                List<Album> albums = new ArrayList();
                JSONArray albumArray = new JSONObject(response.body()).getJSONArray("albums");

                for (int i = 0; i < albumArray.length(); i++) {
                    if (albumArray.get(i).toString() != "null") {
                        Album a = new Album(albumArray.getJSONObject(i).toString(), subcall);
                        albums.add(a);
                    }
                }
                return albums;
            } else if (response.statusCode() == 401) {
                requestToken();
                return requestSeveralAlbums(albumIds, subcall);
            } else if (response.statusCode() == 429) {
                System.out.println("rate limit");
                return new ArrayList();
            }
        } catch (IOException | InterruptedException e) {
            System.out.println(e);
            return new ArrayList();
        }
        return new ArrayList();
    }

    public Artist requestArtist(String artistId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.spotify.com/v1/artists/" + artistId))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();

        try {
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return new Artist(response.body());
            } else if (response.statusCode() == 401) {
                requestToken();
                return requestArtist(artistId);
            } else if (response.statusCode() == 429) {
                System.out.println("rate limit");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println(e);
        }

        return null;
    }

    public List<Artist> requestSeveralArtists(String artistIds) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.spotify.com/v1/artists?ids=" + artistIds))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();
        try {
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<Artist> artists = new ArrayList();
                JSONArray artistArray = new JSONObject(response.body()).getJSONArray("artists");

                for (int i = 0; i < artistArray.length(); i++) {
                    if (artistArray.get(i).toString() != "null") {
                        Artist a = new Artist(artistArray.getJSONObject(i).toString());
                        artists.add(a);
                    }
                }
                return artists;
            } else if (response.statusCode() == 401) {
                requestToken();
                return requestSeveralArtists(artistIds);
            } else if (response.statusCode() == 429) {
                System.out.println("rate limit");
                return new ArrayList();

            }
        } catch (IOException | InterruptedException e) {
            System.out.println(e);
            return new ArrayList();

        }

        return new ArrayList();
    }

    public Track requestTrack(String trackId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.spotify.com/v1/tracks/" + trackId))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();

        try {
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return new Track(response.body(), false);
            } else if (response.statusCode() == 401) {
                requestToken();
                return requestTrack(trackId);
            } else if (response.statusCode() == 429) {
                System.out.println("rate limit");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println(e);
        }

        return null;
    }

    public List<Track> requestSeveralTracks(String trackIds, Boolean subcall) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.spotify.com/v1/tracks?ids=" + trackIds))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();

        try {
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<Track> tracks = new ArrayList();
                JSONArray trackArray = new JSONObject(response.body()).getJSONArray("tracks");

                for (int i = 0; i < trackArray.length(); i++) {
                    if (trackArray.get(i).toString() != "null") {
                        Track a = new Track(trackArray.getJSONObject(i).toString(), subcall);
                        tracks.add(a);
                    }
                }
                return tracks;
            } else if (response.statusCode() == 401) {
                requestToken();
                return requestSeveralTracks(trackIds, subcall);
            } else if (response.statusCode() == 429) {
                System.out.println("rate limit");
                return new ArrayList();

            }
        } catch (IOException | InterruptedException e) {
            System.out.println(e);
            return new ArrayList();

        }

        return new ArrayList();
    }

    public List<Album> requestNewReleases() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.spotify.com/v1/browse/new-releases?country=US&limit=24&offset=0"))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();
        if (request == null)
            return new ArrayList<>();
        if (request.equals(lastNewReleasesRequest)) {
            return lastNewReleasesResult;
        }
        lastNewReleasesRequest = request;
        try {
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<Album> albums = new ArrayList();
                JSONArray albumArray = new JSONObject(response.body()).getJSONObject("albums").getJSONArray("items");

                for (int i = 0; i < albumArray.length(); i++) {
                    Album a = new Album(albumArray.getJSONObject(i).toString(), false);
                    albums.add(a);
                }
                lastNewReleasesResult = albums;
                return lastNewReleasesResult;
            } else if (response.statusCode() == 401) {
                requestToken();
                return requestNewReleases();
            } else if (response.statusCode() == 429) {
                System.out.println("rate limit");
                return new ArrayList();
            }
        } catch (IOException | InterruptedException e) {
            System.out.println(e);
            return new ArrayList();
        }
        return new ArrayList();
    }

    public Results requestRecommendations(List<UserGenre> favoriteGenres, List<UserFavoriteArtist> favoriteArtists) {
        String queryGenre = favoriteGenres.stream().
                map(i -> String.valueOf(i.getGenre())).
                collect(Collectors.joining(","));
        String queryArtist = favoriteArtists.stream().
                map(i -> String.valueOf(i.getEntityId())).
                collect(Collectors.joining(","));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.spotify.com/v1/recommendations?limit=24&seed_artists=" + queryArtist + "&seed_genres=" + queryGenre))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();

        if (request.equals(lastRecommendationsRequest)) {
            return lastRecommendationsResult;
        }
        lastRecommendationsRequest = request;

        try {
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONArray recommendationArray = new JSONObject(response.body()).getJSONArray("tracks");
                List<Album> albums = new ArrayList<>();
                List<Artist> artists = new ArrayList<>();
                List<Track> tracks = new ArrayList<>();

                for (int i = 0; i < recommendationArray.length(); i++) {
                    albums.add(new Album(recommendationArray.getJSONObject(i).getJSONObject("album").toString(), false));
                    artists.add(new Artist(recommendationArray.getJSONObject(i).getJSONArray("artists").get(0).toString()));
                    tracks.add(new Track(recommendationArray.getJSONObject(i).toString(), false));
                }
                setRecommendedAlbums(albums);
                setRecommendedArtists(artists);
                setRecommendedTracks(tracks);
                lastRecommendationsResult = new Results(albums, artists, tracks);
                return lastRecommendationsResult;
            } else if (response.statusCode() == 401) {
                requestToken();
                return requestRecommendations(favoriteGenres, favoriteArtists);
            } else if (response.statusCode() == 429) {
                System.out.println("rate limit");
                return new Results();
            }
        } catch (IOException | InterruptedException e) {
            System.out.println(e);
            return new Results();
        }
        return new Results();
    }

    public Results requestSearch() {
        if (searchedText.length() == 0) {
            return new Results();
        } else {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.spotify.com/v1/search?q=" + searchedText + "&type=track,artist,album&limit=24&market=US"))
                    .timeout(Duration.ofMinutes(1))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + accessToken)
                    .GET()
                    .build();

            try {
                HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    JSONArray tracksArray = new JSONObject(response.body()).getJSONObject("tracks").getJSONArray("items");
                    JSONArray albumsArray = new JSONObject(response.body()).getJSONObject("albums").getJSONArray("items");
                    JSONArray artistsArray = new JSONObject(response.body()).getJSONObject("artists").getJSONArray("items");

                    List<Album> albums = new ArrayList<>();
                    List<Artist> artists = new ArrayList<>();
                    List<Track> tracks = new ArrayList<>();

                    for (int i = 0; i < tracksArray.length(); i++) {
                        tracks.add(new Track(tracksArray.getJSONObject(i).toString(), false));
                    }

                    for (int i = 0; i < albumsArray.length(); i++) {
                        albums.add(new Album(albumsArray.getJSONObject(i).toString(), false));
                    }

                    for (int i = 0; i < artistsArray.length(); i++) {
                        artists.add(new Artist(artistsArray.getJSONObject(i).toString()));
                    }
                    setSearchedAlbums(albums);
                    setSearchedArtists(artists);
                    setSearchedTracks(tracks);
                    return new Results(albums, artists, tracks);
                } else if (response.statusCode() == 401) {
                    requestToken();
                    return requestSearch();
                } else if (response.statusCode() == 429) {
                    System.out.println("rate limit");
                    return new Results();
                }
            } catch (IOException | InterruptedException e) {
                System.out.println(e);
                return new Results();
            }
            return new Results();
        }
    }


    /*
        Getters & Setters
     */

    public String getSearchedText() {
        return searchedText;
    }

    public void setSearchedText(String searchedText) {
        this.searchedText = searchedText;
    }

    public List<Album> getRecommendedAlbums() {
        return recommendedAlbums;
    }

    public void setRecommendedAlbums(List<Album> recommendedAlbums) {
        this.recommendedAlbums = recommendedAlbums;
    }

    public List<Artist> getRecommendedArtists() {
        return recommendedArtists;
    }

    public void setRecommendedArtists(List<Artist> recommendedArtists) {
        this.recommendedArtists = recommendedArtists;
    }

    public List<Track> getRecommendedTracks() {
        return recommendedTracks;
    }

    public void setRecommendedTracks(List<Track> recommendedTracks) {
        this.recommendedTracks = recommendedTracks;
    }

    public List<Album> getSearchedAlbums() {
        return searchedAlbums;
    }

    public void setSearchedAlbums(List<Album> searchedAlbums) {
        this.searchedAlbums = searchedAlbums;
    }

    public List<Artist> getSearchedArtists() {
        return searchedArtists;
    }

    public void setSearchedArtists(List<Artist> searchedArtists) {
        this.searchedArtists = searchedArtists;
    }

    public List<Track> getSearchedTracks() {
        return searchedTracks;
    }

    public void setSearchedTracks(List<Track> searchedTracks) {
        this.searchedTracks = searchedTracks;
    }
}
