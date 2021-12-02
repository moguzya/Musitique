/*
 * Created by Joe Conwell
 * Copyright © 2021 Joe Conwell. All rights reserved.
 */
package edu.vt.controllers;


import edu.vt.EntityBeans.UserFavoriteArtist;
import edu.vt.EntityBeans.UserGenre;
import edu.vt.Pojos.Album;
import edu.vt.Pojos.Artist;
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
    private String accessToken;
    private List<Album> recommendedAlbums;
    private List<Artist> recommendedArtists;
    private List<Track> recommendedTracks;

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
                return new Album(response.body());
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

    public List<Album> requestSeveralAlbums(String albumIds) {
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
                    Album a = new Album(albumArray.getJSONObject(0).toString());
                    albums.add(a);
                }
                return albums;
            } else if (response.statusCode() == 401) {
                requestToken();
                return requestSeveralAlbums(albumIds);
            } else if (response.statusCode() == 429) {
                System.out.println("rate limit");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println(e);
        }

        return null;
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
                    Artist a = new Artist(artistArray.getJSONObject(0).toString());
                    artists.add(a);
                }
                return artists;
            } else if (response.statusCode() == 401) {
                requestToken();
                return requestSeveralArtists(artistIds);
            } else if (response.statusCode() == 429) {
                System.out.println("rate limit");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println(e);
        }

        return null;
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
                return new Track(response.body());
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

    public List<Track> requestSeveralTracks(String trackIds) {
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
                    Track a = new Track(trackArray.getJSONObject(0).toString());
                    tracks.add(a);
                }
                return tracks;
            } else if (response.statusCode() == 401) {
                requestToken();
                return requestSeveralTracks(trackIds);
            } else if (response.statusCode() == 429) {
                System.out.println("rate limit");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println(e);
        }

        return null;
    }

    public List<Album> requestNewReleases() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.spotify.com/v1/browse/new-releases?country=US&limit=50&offset=0"))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();

        try {
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<Album> albums = new ArrayList();
                JSONArray albumArray = new JSONObject(response.body()).getJSONObject("albums").getJSONArray("items");

                for (int i = 0; i < albumArray.length(); i++) {
                    Album a = new Album(albumArray.getJSONObject(0).toString());
                    albums.add(a);
                }
                return albums;
            } else if (response.statusCode() == 401) {
                requestToken();
                return requestNewReleases();
            } else if (response.statusCode() == 429) {
                System.out.println("rate limit");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println(e);
        }

        return null;
    }

    public List<Track> requestRecommendations() {
        List<UserGenre> UserGenresController.favoriteGenres = getListOfUserGenres();
        List<UserFavoriteArtist> favoriteArtists = getListOfFavoriteArtists();

    }
    
     public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public List<Album> getRecommendedAlbums() {
        return recommendedAlbums;
    }

    public void setRecommendedAlbums(List<Album> recommendedAlbums) {
        this.recommendedAlbums = recommendedAlbums;
    }
}
