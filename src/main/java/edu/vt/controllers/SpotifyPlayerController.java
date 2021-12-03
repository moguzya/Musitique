/*
 * Created by Osman Balci on 2021.7.20
 * Copyright © 2021 Osman Balci. All rights reserved.
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
