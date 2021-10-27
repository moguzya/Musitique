/*
 * Created by Osman Balci on 2021.7.17
 * Copyright © 2021 Osman Balci. All rights reserved.
 */
package edu.vt.MoviesNowPlaying;

import java.text.DateFormatSymbols;
import java.util.List;

public class CurrentMovie {
    /*
    ==========================================================
    Instance variables representing the attributes of a movie.
    ==========================================================
     */
    private String id;
    private String title;
    private String mpaaRating;
    private String runtime;
    private String releaseDate;
    private String posterFileName;
    private String overview;
    private String cast;
    private String imdbRating;
    private String criticsRating;
    private String audienceRating;
    private String criticsRatingImage;
    private String audienceRatingImage;
    private String imdbID;
    private String youTubeMovieTrailerVideoID;
    private List<CurrentMovieCastMember> castMembers;

    /*
    ===================================================
    Class constructors for instantiating a CurrentMovie
    object to represent a particular movie.
    ===================================================
     */
    public CurrentMovie(String id, String title, String mpaaRating, String runtime,
            String releaseDate, String posterFileName, String overview, String cast,
            String imdbRating, String criticsRating, String audienceRating, String criticsRatingImage,
            String audienceRatingImage, String imdbID, String youTubeMovieTrailerVideoID, 
            List<CurrentMovieCastMember> castMembers) 
    {
        this.id = id;
        this.title = title;
        this.mpaaRating = mpaaRating;
        this.runtime = runtime;
        this.releaseDate = releaseDate;
        this.posterFileName = posterFileName;
        this.overview = overview;
        this.cast = cast;
        this.imdbRating = imdbRating;
        this.criticsRating = criticsRating;
        this.audienceRating = audienceRating;
        this.criticsRatingImage = criticsRatingImage;
        this.audienceRatingImage = audienceRatingImage;
        this.imdbID = imdbID;
        this.youTubeMovieTrailerVideoID = youTubeMovieTrailerVideoID;
        this.castMembers = castMembers;
    }

    /*
    ========================================================
    Getter and Setter methods for the attributes of a movie.
    ========================================================
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMpaaRating() {
        return mpaaRating;
    }

    public void setMpaaRating(String mpaaRating) {
        this.mpaaRating = mpaaRating;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterFileName() {
        return posterFileName;
    }

    public void setPosterFileName(String posterFileName) {
        this.posterFileName = posterFileName;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    public String getCriticsRating() {
        return criticsRating;
    }

    public void setCriticsRating(String criticsRating) {
        this.criticsRating = criticsRating;
    }

    public String getAudienceRating() {
        return audienceRating;
    }

    public void setAudienceRating(String audienceRating) {
        this.audienceRating = audienceRating;
    }

    public String getCriticsRatingImage() {
        return criticsRatingImage;
    }

    public void setCriticsRatingImage(String criticsRatingImage) {
        this.criticsRatingImage = criticsRatingImage;
    }

    public String getAudienceRatingImage() {
        return audienceRatingImage;
    }

    public void setAudienceRatingImage(String audienceRatingImage) {
        this.audienceRatingImage = audienceRatingImage;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getYouTubeMovieTrailerVideoID() {
        return youTubeMovieTrailerVideoID;
    }

    public void setYouTubeMovieTrailerVideoID(String youTubeMovieTrailerVideoID) {
        this.youTubeMovieTrailerVideoID = youTubeMovieTrailerVideoID;
    }

    public List<CurrentMovieCastMember> getCastMembers() {
        return castMembers;
    }

    public void setCastMembers(List<CurrentMovieCastMember> castMembers) {
        this.castMembers = castMembers;
    }

    /*
    ================
    Instance Methods
    ================

    --------------------------------------------
    Converts the date from the 2017-12-03 format
    to December 3, 2017 format.
    --------------------------------------------
     */
    public String getReleaseDateLong() {

        int monthNumber;
        String year, day, dayCleaned, monthString;

        if (releaseDate.equals("N/A")) {
            releaseDate = "none";
            return releaseDate;
        }

        year = releaseDate.substring(0, 4);
        monthNumber = Integer.parseInt(releaseDate.substring(5, 7));
        day = releaseDate.substring(8, 10);

        // Remove the leading zero in day value, e.g., 03 --> 3
        dayCleaned = day.replaceAll("^0+(?!$)", "");

        // DateFormatSymbols is a public class for encapsulating localizable date-time formatting data
        DateFormatSymbols dfs = new DateFormatSymbols();

        // getMonths() returns month names in a String array from 0 to 11
        String[] months0to11 = dfs.getMonths();

        // Obtain the month name from month number from 1 to 12
        monthString = months0to11[monthNumber - 1];

        return monthString + " " + dayCleaned + ", " + year;
    }

    /*
    --------------------------------------------------------
    Returns the names of the first two cast members (actors)
    --------------------------------------------------------
     */
    public String getCastShort() {

        String[] actors = cast.split(", ");

        if (actors.length > 1) {
            return actors[0] + ", " + actors[1];

        } else if (actors.length == 1) {
            return actors[0];

        } else {
            return "No cast members are available!";
        }
    }

}