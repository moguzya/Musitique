/*
 * Created by Osman Balci on 2021.7.20
 * Copyright © 2021 Osman Balci. All rights reserved.
 */
package edu.vt.controllers;

import edu.vt.EntityBeans.Movie;
import edu.vt.FacadeBeans.MovieFacade;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.controllers.util.JsfUtil.PersistAction;
import edu.vt.globals.Methods;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named("spotifyPlayerController")
@SessionScoped
public class SpotifyPlayerController implements Serializable {
    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */
    private String embededURL = "https://open.spotify.com/embed/track/0VjIjW4GlUZAMYd2vXMi3b?utm_source=generator";

    public String getEmbededURL() {
        return embededURL;
    }

    public void setEmbededURL(String embededURL) {
        this.embededURL = embededURL;
    }
    public void updateURL(String newUrl){
        this.embededURL = newUrl;
    }


}
