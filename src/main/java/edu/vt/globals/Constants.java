
/*
 * Copyright (c) 2021. Musitique App was developed by Mehmet Oguz Yardimci, Vibhavi Peiris, and Joseph Conwell as CS5704 Software Engineering course assignment.
 *
 * https://www.linkedin.com/in/oguzyardimci/
 * https://www.linkedin.com/in/vibhavipeiris/?originalSubdomain=ca
 * https://conwell.info/
 */

package edu.vt.globals;


import java.net.http.HttpClient;
import java.util.Arrays;
import java.util.List;

public final class Constants {
    /*
    ==================================================
    |   Use of Class Variables as Global Constants   |
    ==================================================
    All of the variables in this class are Class Variables (typed with the "static" keyword)
    with Constant values, which can be accessed in any class in the project by specifying
    Constants.CONSTANTNAME, i.e., ClassName.ClassVariableNameInCaps

    Constants are specified in capital letters.
    
    =====================================================
    |   Our Design Decision for Use of External Files   |
    =====================================================
    We decided to use directories external to our application for the storage and retrieval of user's files.
    We do not want to store/retrieve external files into/from our database for the following reasons:
    
        (a) Database storage and retrieval of large files as BLOB (Binary Large OBject) degrades performance.
        (b) BLOBs increase the database complexity.
        (c) The operating system handles the external files instead of the database management system.
    
    WildFly provides an internal web server, named Undertow, to access and display external files.
    See https://docs.wildfly.org/24/Admin_Guide.html#Undertow
     */

    //---------------
    // To run locally
    //---------------

    //    public static final String PHOTOS_ABSOLUTE_PATH = "C:/Users/jmcon/DocRoot/MusitiqueUserPhotoStorage/";

    //--------------------------------
    // To run on your AWS EC2 instance
    //--------------------------------
    public static final String PHOTOS_ABSOLUTE_PATH = "/opt/wildfly/DocRoot/CS5704-Team9-FileStorage";


    /*
    RUN THE FOLLOWING TO ADD PATH TO WILDFLY
    /subsystem=undertow/configuration=handler/file=MusitiquePhotosDirHandler/:add(cache-buffer-size=1024,cache-buffers=1024,directory-listing=true,follow-symlink=true,path=/opt/wildfly/DocRoot/CS5704-Team9-FileStorage/)

    /subsystem=undertow/server=default-server/host=default-host/location=\/musitiquePhotos/:add(handler=MusitiquePhotosDirHandler)
    */


    /*
    =================================================================================================
    |   For displaying external files to the user in an XHTML page, we use the Undertow subsystem.  |
    =================================================================================================
     We configured WildFly Undertow subsystem so that
     http://localhost:8080/musitiquePhotos/p displays file p from /DocRoot/CS5704-Team9-FileStorage/
     */

    //---------------
    // To run locally
    //---------------
//    public static final String PHOTOS_URI = "http://localhost:8080/musitiquePhotos/";

    //-----------------------------------------------------
    // To run on your AWS EC2 instance with your IP address
    //-----------------------------------------------------
        public static final String PHOTOS_URI = "http://34.203.199.88:8080/musitiquePhotos/";

    /* 
    =============================================
    |   Our Design Decision for Profile Photo   |
    =============================================
    We do not want to use the uploaded user profile photo as is, which may be very large
    degrading performance. We scale it down to size 200x200 called the Thumbnail photo size.
     */
    public static final Integer THUMBNAIL_SIZE = 200;

    /* 
     United States postal state abbreviations (codes)
     */
    public static final String[] STATES = {"AK", "AL", "AR", "AZ", "CA", "CO", "CT",
        "DC", "DE", "FL", "GA", "GU", "HI", "IA", "ID", "IL", "IN", "KS", "KY", "LA", "MA",
        "MD", "ME", "MH", "MI", "MN", "MO", "MS", "MT", "NC", "ND", "NE", "NH", "NJ", "NM",
        "NV", "NY", "OH", "OK", "OR", "PA", "PR", "PW", "RI", "SC", "SD", "TN", "TX", "UT",
        "VA", "VI", "VT", "WA", "WI", "WV", "WY"};

    /* 
     A security question is selected and answered by the user at the time of account creation.
     The selected question/answer is used as a second level of authentication for
     (a) resetting user's password, and (b) deleting user's account.
     */
    public static final String[] QUESTIONS = {
        "In what city or town did your mother and father meet?",
        "In what city or town were you born?",
        "What did you want to be when you grew up?",
        "What do you remember most from your childhood?",
        "What is the name of the boy or girl that you first kissed?",
        "What is the name of the first school you attended?",
        "What is the name of your favorite childhood friend?",
        "What is the name of your first pet?",
        "What is your mother's maiden name?",
        "What was your favorite place to visit as a child?"
    };
    public static final HttpClient CLIENT = HttpClient.newHttpClient();
    public static String ACCESS_TOKEN;
    public static final String EMBED_URI = "https://open.spotify.com/embed/";

    public static final String CLIENT_AUTH = "NzM5YjE5NTU2OGJmNGZkNTg5MzBiMmUxZjU4YjU4NDA6MmI0ZTk4MWI0OTFkNGRhZmJiOGE3OGY2NTU3MjA0MjM=";

    public static final List<String> GENRES = Arrays.asList("acoustic",
            "afrobeat",
            "alt-rock",
            "alternative",
            "ambient",
            "anime",
            "black-metal",
            "bluegrass",
            "blues",
            "bossanova",
            "brazil",
            "breakbeat",
            "british",
            "cantopop",
            "chicago-house",
            "children",
            "chill",
            "classical",
            "club",
            "comedy",
            "country",
            "dance",
            "dancehall",
            "death-metal",
            "deep-house",
            "detroit-techno",
            "disco",
            "disney",
            "drum-and-bass",
            "dub",
            "dubstep",
            "edm",
            "electro",
            "electronic",
            "emo",
            "folk",
            "forro",
            "french",
            "funk",
            "garage",
            "german",
            "gospel",
            "goth",
            "grindcore",
            "groove",
            "grunge",
            "guitar",
            "happy",
            "hard-rock",
            "hardcore",
            "hardstyle",
            "heavy-metal",
            "hip-hop",
            "holidays",
            "honky-tonk",
            "house",
            "idm",
            "indian",
            "indie",
            "indie-pop",
            "industrial",
            "iranian",
            "j-dance",
            "j-idol",
            "j-pop",
            "j-rock",
            "jazz",
            "k-pop",
            "kids",
            "latin",
            "latino",
            "malay",
            "mandopop",
            "metal",
            "metal-misc",
            "metalcore",
            "minimal-techno",
            "movies",
            "mpb",
            "new-age",
            "new-release",
            "opera",
            "pagode",
            "party",
            "philippines-opm",
            "piano",
            "pop",
            "pop-film",
            "post-dubstep",
            "power-pop",
            "progressive-house",
            "psych-rock",
            "punk",
            "punk-rock",
            "r-n-b",
            "rainy-day",
            "reggae",
            "reggaeton",
            "road-trip",
            "rock",
            "rock-n-roll",
            "rockabilly",
            "romance",
            "sad",
            "salsa",
            "samba",
            "sertanejo",
            "show-tunes",
            "singer-songwriter",
            "ska",
            "sleep",
            "songwriter",
            "soul",
            "soundtracks",
            "spanish",
            "study",
            "summer",
            "swedish",
            "synth-pop",
            "tango",
            "techno",
            "trance",
            "trip-hop",
            "turkish",
            "work-out",
            "world-music");
}
