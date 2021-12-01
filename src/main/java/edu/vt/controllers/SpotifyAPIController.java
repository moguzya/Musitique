/*
 * Created by Joe Conwell
 * Copyright © 2021 Joe Conwell. All rights reserved.
 */
package edu.vt.controllers;

import edu.vt.EntityBeans.User;
import edu.vt.EntityBeans.UserComment;
import edu.vt.EntityBeans.UserRating;
import edu.vt.EntityType;
import edu.vt.FacadeBeans.CommentFacade;
import edu.vt.FacadeBeans.RatingFacade;
import edu.vt.Pojos.Album;
import edu.vt.Pojos.Artist;
import edu.vt.Pojos.Track;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.controllers.util.JsfUtil.PersistAction;
import edu.vt.globals.Methods;
import org.primefaces.event.RateEvent;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
//    List<Album> getNewReleases()
//    Track getTrackInfo(String Id)
//    Album getAlbum Info(String Id)
//    Artist getArtist Info(String Id)
//
//    Void getRecommendations(List<String> favoriteArtistIds)
//    set:
//    List<Album> searchedAlbum;
//    List<Track> searchedTrack;
//    List<Artist> searchedArtist;

    private List<Track> searchedTracks;
    private List<Artist> searchedArtist;
    private List<Album> searchedAlbums;


    /*
    ================
    Instance Methods
    ================
    */


    /*
    ******************
    Getter and Setters
    ******************
     */
}
