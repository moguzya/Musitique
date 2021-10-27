/*
 * Created by Osman Balci on 2021.9.10
 * Copyright © 2021 Osman Balci. All rights reserved.
 */
package edu.vt.globals;

public final class Constants {
    /*
    ==================================================
    |   Use of Class Variables as Global Constants   |
    ==================================================
    All of the variables in this class are Class Variables (typed with the "static" keyword)
    with Constant values, which can be accessed in any class in the project by specifying
    Constants.CONSTANTNAME, i.e., ClassName.ClassVariableNameInCaps

    Constants are specified in capital letters.
    */

    /*
    ***********************************************************************************
    You are required to register at https://developers.themoviedb.org/3/getting-started
    and obtain your own API key. TMDb limits 4 accesses per second. Therefore, every
    student must use his/her own API key so that the limit is not exceeded.
    ***********************************************************************************
     */
    public static final String TMDB_API_KEY                     = "USE-YOUR-OWN-API-KEY";

    // You are allowed to use Dr. Balci's OMDb API key below since he pays $1 for it every month.
    public static final String OMDB_API_KEY                     = "9f67dd7a";

    public static final String TMDB_API_SEARCH_MOVIE_BASE_URL   = "https://api.themoviedb.org/3/search/movie";
    public static final String TMDB_API_MOVIE_BASE_URL          = "https://api.themoviedb.org/3/movie/";
    public static final String OMDB_API_MOVIE_BASE_URL          = "https://www.omdbapi.com/";

}
