
/*
 * Copyright (c) 2021. Musitique App was developed by Mehmet Oguz Yardimci, Vibhavi Peiris, and Joseph Conwell as CS5704 Software Engineering course assignment.
 *
 * https://www.linkedin.com/in/oguzyardimci/
 * https://www.linkedin.com/in/vibhavipeiris/?originalSubdomain=ca
 * https://conwell.info/
 */

package edu.vt.controllers;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named("spotifyPlayerController")
@SessionScoped
public class SpotifyPlayerController implements Serializable {
    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */
    private String embedURL = "https://open.spotify.com/embed/album/3Gg0rgnLUn8kdctvVyAR3X?utm_source=generator";

    public String getEmbedURL() {
        return embedURL;
    }

    public void setEmbedURL(String embedURL) {
        this.embedURL = embedURL;
    }
    public void updateURL(String newUrl){
        this.embedURL = newUrl;
    }


}
