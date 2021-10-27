/*
 * Created by Osman Balci on 2021.9.10
 * Copyright © 2021 Osman Balci. All rights reserved.
 */
package edu.vt.MovieSearch;

import edu.vt.globals.Constants;
import edu.vt.globals.Methods;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;
import org.primefaces.util.LangUtils;

@Named("searchedMovieController")
@SessionScoped
public class SearchedMovieController implements Serializable {
    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */
    private List<SearchedMovie> listOfSearchedMovies;

    private String searchQuery;
    private SearchedMovie selected;

    /*
    =========================
    Getter and Setter Methods
    =========================
     */
    public List<SearchedMovie> getListOfSearchedMovies() {
        return listOfSearchedMovies;
    }

    public void setListOfSearchedMovies(List<SearchedMovie> listOfSearchedMovies) {
        this.listOfSearchedMovies = listOfSearchedMovies;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public SearchedMovie getSelected() {
        return selected;
    }

    public void setSelected(SearchedMovie selected) {
        this.selected = selected;
    }

    /*
    ================
    Instance Methods
    ================

    ---------------------------------------------------------
    Search for movies for the searchQuery entered by the user
    ---------------------------------------------------------
     */
    public String performSearch() {

        selected = null;
        listOfSearchedMovies = new ArrayList<>();
        List<SearchedMovieCastMember> castMembers = new ArrayList<>();
        List<String> idList = new ArrayList<>();

        // Spaces in search query must be replaced with "+"
        searchQuery = searchQuery.replaceAll(" ", "+");

        try {
            for (int tmdbPageNumber = 1; tmdbPageNumber < 2; tmdbPageNumber++) {
                /*
                We obtain the first page of The Movie Database (TMDb) movie search query results
                in JSON by specifying the page number.
                
                https://api.themoviedb.org/3/search/movie?api_key=YOUR-API-KEY&query=Star+Wars&page=2
                
                JSON data use the following notation:
                { }    represents a JavaScript object as a Dictionary with Key:Value pairs
                [ ]    represents Array
                [{ }]  represents an Array of JavaScript objects (dictionaries)
                  :    separates Key from the Value
                 */

                String tmdbMovieSearchWebServiceUrl = Constants.TMDB_API_SEARCH_MOVIE_BASE_URL + "?api_key=" + Constants.TMDB_API_KEY
                        + "&query=" + searchQuery + "&page=" + Integer.toString(tmdbPageNumber);

                // Obtain the JSON file containing the movie search results at the given page number
                String movieSearchResultsJsonData = Methods.readUrlContent(tmdbMovieSearchWebServiceUrl);

                // Instantiate a JSON object from the JSON data obtained
                JSONObject resultsJsonObject = new JSONObject(movieSearchResultsJsonData);

                // Obtain a JSONArray of movie objects (Each movie is represented as a JSONObject)
                JSONArray jsonArrayFoundMovies = resultsJsonObject.getJSONArray("results");

                int index = 0;

                if (jsonArrayFoundMovies.length() > index) {

                    while (jsonArrayFoundMovies.length() > index) {

                        // Get the movie JSONObject at index
                        JSONObject jsonObject = jsonArrayFoundMovies.getJSONObject(index);

                        /*
                        ======== JSON Data Optional Processing ======== 
                        
                        optBoolean(String key, boolean defaultValue) 
                            Obtain the Boolean value for the given "key" if a value exists; otherwise, use the defaultValue.
                        
                        optDouble(String key, double defaultValue) 
                            Obtain the Double value for the given "key", or use the defaultValue if there is no such key or if its value is not a number.

                        optInt(String key, int defaultValue) 
                            Obtain the Int value for the given "key", or use the defaultValue if there is no such key or if its value is not a number.
          
                        optLong(String key, long defaultValue) 
                            Obtain the Long value for the given "key", or use the defaultValue if there is no such key or if its value is not a number.
                        
                        optString(String key, String defaultValue) 
                            Obtain the String value for the given "key" if a value exists; otherwise, use the defaultValue.
                        
                        ============================
                        Movie Poster Image File Name
                        ============================
                         */
                        String posterFileName = jsonObject.optString("poster_path", "");
                        if (posterFileName.equals("")) {
                            // Skip the movie with no poster image provided
                            index++;
                            continue;
                        }

                        /*
                        =========================
                        Movie Overview (Synopsis)
                        =========================
                         */
                        String overview = jsonObject.optString("overview", "");
                        if (overview.equals("")) {
                            overview = "No movie overview is provided!";
                        }

                        /*
                        ==================
                        Movie Release Date
                        ==================
                         */
                        String releaseDate = jsonObject.optString("release_date", "");
                        if (releaseDate.equals("")) {
                            releaseDate = "No Release Date";
                        }

                        /*
                        =====================================
                        Movie TMDb Identification Number (id)
                        =====================================
                         */
                        long idNumber = jsonObject.optLong("id", 0);
                        if (idNumber == 0) {
                            // Skip the movie with no id number
                            index++;
                            continue;
                        }

                        String id = Long.toString(idNumber);

                        // Remove duplicated movies created due to TMDb update
                        if (index > 0) {
                            String[] idTest = new String[idList.size()];
                            idTest = idList.toArray(idTest);

                            if (LangUtils.contains(idTest, id)) {
                                // Skip the movie which already exists in the list
                                index++;
                                continue;
                            }
                        }

                        /*
                        ===========
                        Movie Title
                        ===========
                         */
                        String title = jsonObject.optString("title", "");
                        if (title.equals("")) {
                            // Skip the movie with no title
                            index++;
                            continue;
                        }

                        /*
                        ========================================================================================
                        Obtain data specific to the movie with the given "id" under the VIDEOS option                       
                        https://api.themoviedb.org/3/movie/284052?api_key=YOUR-API-KEY&append_to_response=videos
                        ========================================================================================
                         */
                        String tmdbIdVideosWebServiceUrl = Constants.TMDB_API_MOVIE_BASE_URL + id + "?api_key=" + Constants.TMDB_API_KEY + "&append_to_response=videos";

                        // Obtain the JSON data specific to the movie with the given "id" under the Videos option
                        String jsonDataForVideoId = Methods.readUrlContent(tmdbIdVideosWebServiceUrl);

                        /*
                        jsonDataForVideoId:
                        ---------------------------------------------
                            {
                                    :
                                    :
                                "imdb_id":"tt1211837",
                                    :
                                    :
                                "runtime":115,
                                    :
                                    :
                                "videos":
                                    {"results":
                                        [
                                            {
                                                :
                                                :
                                                "key":"9Golt91teTQ",  <-- youTubeMovieTrailerVideoID
                                                :
                                                :
                                            }
                                        ]
                                    }
                            }
                        ---------------------------------------------
                         */
                        // Convert the JSON data into a JSONObject
                        JSONObject jsonObjectForVideoId = new JSONObject(jsonDataForVideoId);

                        /*
                        ====================================
                        YouTube Trailer Video Identification
                        ====================================
                         */
                        String youTubeMovieTrailerVideoID;

                        JSONObject jsonObjectForVideos = jsonObjectForVideoId.getJSONObject("videos");
                        JSONArray jsonArrayForResults = jsonObjectForVideos.getJSONArray("results");

                        if (jsonArrayForResults.isNull(0)) {
                            youTubeMovieTrailerVideoID = "";
                        } else {
                            JSONObject jsonObjectAtIndexZero = jsonArrayForResults.getJSONObject(0);
                            youTubeMovieTrailerVideoID = jsonObjectAtIndexZero.optString("key", "");
                        }

                        /*
                        =========================
                        Movie IMDb Identification
                        =========================
                         */
                        String imdbID = jsonObjectForVideoId.optString("imdb_id", "");
                        if (imdbID.equals("")) {
                            // Skip the movie with no IMDb identification
                            index++;
                            continue;
                        }

                        /*
                        ========================
                        Movie Runtime (Duration)
                        ========================
                         */
                        String runtime;

                        // The "runtime" value in the JSON file is given as integer in minutes.
                        int runtimeInt = jsonObjectForVideoId.optInt("runtime", 0);

                        if (runtimeInt == 0) {
                            runtime = "No runtime";
                        } else {
                            // Convert runtime from minutes to: 1 hour M mins, or N hours M mins.
                            int hours = runtimeInt / 60;
                            int minutes = runtimeInt % 60;

                            switch (hours) {
                                case 0:
                                    runtime = String.valueOf(minutes) + " mins";
                                    break;
                                case 1:
                                    runtime = "1 hour " + String.valueOf(minutes) + " mins";
                                    break;
                                default:
                                    runtime = String.valueOf(hours) + " hours " + String.valueOf(minutes) + " mins";
                                    break;
                            }
                        }

                        /*
                        =============================================================================
                        Obtain JSON data from the OMDb API for the given IMDb movie ID tt3393786
                        https://www.omdbapi.com/?type=movie&tomatoes=true&i=tt3393786&apikey=9f67dd7a
                        You are allowed to use Dr. Balci's key since he pays $1 for it every month.
                        =============================================================================
                         */
                        String omdbWebServiceUrl = Constants.OMDB_API_MOVIE_BASE_URL + "?type=movie&tomatoes=true&i=" + imdbID + "&apikey=" + Constants.OMDB_API_KEY;

                        // Obtain the JSON data specific to the movie with the given imdbID number
                        String omdbJsonData = Methods.readUrlContent(omdbWebServiceUrl);

                        // getJSONObject(0), object at index 0, gives the dictionary returned in the JSON file.
                        JSONObject jsonObjectOmdb = new JSONObject(omdbJsonData);

                        /*
                        ==============================
                        Check if Response is Available
                        ==============================
                         */
                        String response = jsonObjectOmdb.optString("Response", "");
                        if (response.equals("") || response.equals("False")) {
                            // Skip the movie since no Response is available
                            index++;
                            continue;
                        }

                        /*
                        ===========
                        MPAA Rating
                        ===========
                         */
                        String mpaaRating = jsonObjectOmdb.optString("Rated", "");
                        if (mpaaRating.equals("") || mpaaRating.equals("N/A")) {
                            mpaaRating = "none";
                        }

                        /*
                        ===================
                        Movie Cast (Actors)
                        ===================
                         */
                        String cast = jsonObjectOmdb.optString("Actors", "");
                        if (cast.equals("") || cast.equals("N/A")) {
                            cast = "No cast (actors) list is available!";
                        }

                        /*
                        ===========
                        IMDb Rating
                        ===========
                         */
                        String imdbRating = jsonObjectOmdb.optString("imdbRating", "");
                        if (imdbRating.equals("") || imdbRating.equals("N/A")) {
                            imdbRating = "none";
                        }

                        /*
                        ============================
                        Critics (tomatoMeter) Rating
                        ============================
                         */
                        String criticsRating = jsonObjectOmdb.optString("tomatoMeter", "");
                        if (criticsRating.equals("") || criticsRating.equals("N/A")) {
                            criticsRating = "none";
                        } else {
                            criticsRating = criticsRating + "%";
                        }

                        /*
                        =================================
                        Audience (tomatoUserMeter) Rating
                        =================================
                         */
                        String audienceRating = jsonObjectOmdb.optString("tomatoUserMeter", "");

                        if (audienceRating.equals("") || audienceRating.equals("N/A")) {
                            audienceRating = "none";
                        } else {
                            audienceRating = audienceRating + "%";
                        }

                        /*
                        ==================================
                        Critics Rating Image (tomatoImage)
                        ==================================
                         */
                        String criticsRatingImage = jsonObjectOmdb.optString("tomatoImage", "");

                        if (criticsRatingImage.equals("") || criticsRatingImage.equals("N/A")) {
                            criticsRatingImage = "/images/ratingIcons/notRated.png";
                        } else {
                            switch (criticsRatingImage) {
                                case "certified":
                                    criticsRatingImage = "/images/ratingIcons/certified.png";
                                    break;
                                case "fresh":
                                    criticsRatingImage = "/images/ratingIcons/tomato.png";
                                    break;
                                default:
                                    criticsRatingImage = "/images/ratingIcons/splat.png";
                                    break;
                            }
                        }

                        /*
                        =============================================
                        Audience Rating Image (tomatoUserMeter image)
                        =============================================
                         */
                        String audienceRatingImage;

                        String tomatoUserMeter = jsonObjectOmdb.optString("tomatoUserMeter", "");
                        if (tomatoUserMeter.equals("") || tomatoUserMeter.equals("N/A")) {
                            audienceRatingImage = "/images/ratingIcons/notRated.png";
                        } else {
                            if (Integer.parseInt(tomatoUserMeter) >= 60) {
                                audienceRatingImage = "/images/ratingIcons/popcorn.png";
                            } else {
                                audienceRatingImage = "/images/ratingIcons/popcornBad.png";
                            }
                        }

                        /*
                        =========================================================================================
                        Obtain data specific to the movie with the given "id" 343611 under the CREDITS option                       
                        https://api.themoviedb.org/3/movie/343611?api_key=YOUR-API-KEY&append_to_response=credits
                        =========================================================================================
                         */
                        String tmdbIdCreditsWebServiceUrl = Constants.TMDB_API_MOVIE_BASE_URL + id + "?api_key=" + Constants.TMDB_API_KEY + "&append_to_response=credits";

                        // Obtain the JSON data specific to the movie with the given "id" under the Credits option
                        String idCreditsResultsJsonData = Methods.readUrlContent(tmdbIdCreditsWebServiceUrl);

                        // Convert JSON data into a JSON object
                        JSONObject jsonObjectIdCredits = new JSONObject(idCreditsResultsJsonData);

                        JSONObject credits = jsonObjectIdCredits.getJSONObject("credits");
                        JSONArray jsonArrayCastMembers = credits.getJSONArray("cast");

                        /*
                        ========================================================================
                        Create a List of Cast Member objects from the jsonArrayCastMembers Array
                        ========================================================================
                         */
                        int castMemberIndex = 0;
                        castMembers = new ArrayList<>();

                        if (jsonArrayCastMembers.length() > castMemberIndex) {

                            while (jsonArrayCastMembers.length() > castMemberIndex) {

                                // Get the JSONObject at castMemberIndex
                                JSONObject jsonObjectCastMember = jsonArrayCastMembers.getJSONObject(castMemberIndex);

                                // Actor / Actress Name
                                String actorName = jsonObjectCastMember.optString("name", "");
                                if (actorName.equals("")) {
                                    // Skip actor with no name
                                    castMemberIndex++;
                                    continue;
                                }

                                // Name of Character Played by Actor
                                String characterName = jsonObjectCastMember.optString("character", "");
                                if (characterName.equals("")) {
                                    characterName = "Not Available";
                                }

                                // Actor / Actress Photo File Name
                                String actorPhotoFileName = jsonObjectCastMember.optString("profile_path", "");
                                if (actorPhotoFileName.equals("")) {
                                    // Skip actor with no photo
                                    castMemberIndex++;
                                    continue;
                                }
                                /*
                                System.out.println("actorName = " + actorName + " | " +
                                        "characterName = " + characterName + " | " +
                                        "actorPhotoFileName = " + actorPhotoFileName + "\n");
                                 */
                                SearchedMovieCastMember newCastMember = new SearchedMovieCastMember(actorName, characterName, actorPhotoFileName);

                                castMembers.add(newCastMember);
                                castMemberIndex++;
                            }

                        }

                        /*
                        ================================================================
                        Create a new SearchedMovie object dressed up with its Attributes
                        ================================================================
                         */
                        SearchedMovie movie = new SearchedMovie(id, title, mpaaRating, runtime,
                                releaseDate, posterFileName, overview, cast, imdbRating,
                                criticsRating, audienceRating, criticsRatingImage,
                                audienceRatingImage, imdbID, youTubeMovieTrailerVideoID, castMembers);

                        // Add the newly created SearchedMovie object to the list of searchedMovies
                        listOfSearchedMovies.add(movie);
                        idList.add(id);
                        index++;
                    }

                } else {
                    // Take no action since there are no search results at this page number
                }
            }

        } catch (Exception ex) {
            Methods.showMessage("Fatal Error", "The Movie Database (TMDb) API limit of 4 accesses per second has been exceeded!",
                    "See: " + ex.getMessage());
            searchQuery = "";
            return "";
        }

        searchQuery = "";
        return "/movieSearch/SearchResults?faces-redirect=true";
    }

}
