package edu.vt.NewReleases;

import edu.vt.Pojos.Album;
import edu.vt.Pojos.Artist;
import edu.vt.Pojos.Track;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("exploreController")
@SessionScoped
public class ExploreController implements Serializable {
    private List<Album> albums;
    private List<Artist> artists;
    private List<Track> tracks;

    @PostConstruct
    public void init() {

        //TODO change the following
        //listOfNewReleases = new ArrayList<>();
        albums = readAlbums();
        artists = readArtists();
        tracks = readTracks();
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
    public String explore(){
        //TODO do explore
        return "/NewReleases/Explore.xhtml";
    }
    private List<Artist> readArtists() {
        List<String> genres = new ArrayList<>();
        genres.add("dance pop");
        genres.add("latin");
        genres.add("miami hip hop");
        genres.add("pop");
        genres.add("pop rap");

        Artist artist = new Artist("0TnOYISbd1XYRBk9myaseg", "Pitbull", 8874690, "https://i.scdn.co/image/ab6761610000e5eb2dc40ac263ef07c16a95af4e", genres);
        List<Artist> tempArtists = new ArrayList<>();
        tempArtists.add(artist);
        tempArtists.add(artist);
        tempArtists.add(artist);

        List<Album> tempListOfNewReleases;
        tempListOfNewReleases = new ArrayList<>();
        tempListOfNewReleases.add(new Album("4aawyAB9vmqN3uQ7FjRGTy", "Global Warming", "https://i.scdn.co/image/ab67616d0000b2732c5b24ecfa39523a75c993c4", "2006-06-19", tempArtists, 5));
        tempListOfNewReleases.add(new Album("2ZGACajeINbPfwLPbEuzwr", "Änglar", "https://i.scdn.co/image/ab67616d0000b273f54e06014d92f89dfe5baaf3", "2016-06-19", tempArtists, 15));
        tempListOfNewReleases.add(new Album("1VhOdgOjIARBn6SoNyeQDa", "Always Been You", "https://i.scdn.co/image/ab67616d0000b273f54e06014d92f89dfe5baaf3", "2026-06-19", tempArtists, 25));
        tempListOfNewReleases.add(new Album("3KrkQ77DF9OUB0aOzKFYOF", "Donda (Deluxe)", "https://i.scdn.co/image/ab67616d0000b273df9a35baaa98675256b35177", "2036-06-19", tempArtists, 35));
        return tempArtists;
    }

    private List<Album> readAlbums() {
        List<String> genres = new ArrayList<>();
        genres.add("dance pop");
        genres.add("latin");
        genres.add("miami hip hop");
        genres.add("pop");
        genres.add("pop rap");

        Artist artist = new Artist("0TnOYISbd1XYRBk9myaseg", "Pitbull", 8874690, "https://i.scdn.co/image/ab6761610000e5eb2dc40ac263ef07c16a95af4e", genres);
        List<Artist> tempArtists = new ArrayList<>();
        tempArtists.add(artist);
        tempArtists.add(artist);
        tempArtists.add(artist);

        List<Album> tempListOfNewReleases;
        tempListOfNewReleases = new ArrayList<>();
        tempListOfNewReleases.add(new Album("4aawyAB9vmqN3uQ7FjRGTy", "Global Warming", "https://i.scdn.co/image/ab67616d0000b2732c5b24ecfa39523a75c993c4", "2006-06-19", tempArtists, 5));
        tempListOfNewReleases.add(new Album("2ZGACajeINbPfwLPbEuzwr", "Änglar", "https://i.scdn.co/image/ab67616d0000b273f54e06014d92f89dfe5baaf3", "2016-06-19", tempArtists, 15));
        tempListOfNewReleases.add(new Album("1VhOdgOjIARBn6SoNyeQDa", "Always Been You", "https://i.scdn.co/image/ab67616d0000b273f54e06014d92f89dfe5baaf3", "2026-06-19", tempArtists, 25));
        tempListOfNewReleases.add(new Album("3KrkQ77DF9OUB0aOzKFYOF", "Donda (Deluxe)", "https://i.scdn.co/image/ab67616d0000b273df9a35baaa98675256b35177", "2036-06-19", tempArtists, 35));
        return  tempListOfNewReleases;
    }

    private List<Track> readTracks() {
        List<String> genres = new ArrayList<>();
        genres.add("dance pop");
        genres.add("latin");
        genres.add("miami hip hop");
        genres.add("pop");
        genres.add("pop rap");

        Artist artist = new Artist("0TnOYISbd1XYRBk9myaseg", "Pitbull", 8874690, "https://i.scdn.co/image/ab6761610000e5eb2dc40ac263ef07c16a95af4e", genres);
        List<Artist> tempArtists = new ArrayList<>();
        tempArtists.add(artist);
        tempArtists.add(artist);
        tempArtists.add(artist);

        List<Album> tempListOfNewReleases;
        tempListOfNewReleases = new ArrayList<>();
        tempListOfNewReleases.add(new Album("4aawyAB9vmqN3uQ7FjRGTy", "Global Warming", "https://i.scdn.co/image/ab67616d0000b2732c5b24ecfa39523a75c993c4", "2006-06-19", tempArtists, 5));
        tempListOfNewReleases.add(new Album("2ZGACajeINbPfwLPbEuzwr", "Änglar", "https://i.scdn.co/image/ab67616d0000b273f54e06014d92f89dfe5baaf3", "2016-06-19", tempArtists, 15));
        tempListOfNewReleases.add(new Album("1VhOdgOjIARBn6SoNyeQDa", "Always Been You", "https://i.scdn.co/image/ab67616d0000b273f54e06014d92f89dfe5baaf3", "2026-06-19", tempArtists, 25));
        tempListOfNewReleases.add(new Album("3KrkQ77DF9OUB0aOzKFYOF", "Donda (Deluxe)", "https://i.scdn.co/image/ab67616d0000b273df9a35baaa98675256b35177", "2036-06-19", tempArtists, 35));
        return new ArrayList<>();
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }
}
