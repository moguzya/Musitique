/*
 * Created by Mehmet Oguz Yardimci
 * Copyright © 2021 Mehmet Oguz Yardimci. All rights reserved.
 */
package edu.vt.controllers;

import edu.vt.Pojos.Album;
import edu.vt.Pojos.Track;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

/*
---------------------------------------------------------------------------
The @Named (javax.inject.Named) annotation indicates that the objects
instantiated from this class will be managed by the Contexts and Dependency
Injection (CDI) container. The name "videoController" is used within
Expression Language (EL) expressions in JSF (XHTML) facelets pages to
access the properties and invoke methods of this class.
---------------------------------------------------------------------------
*/
@Named("trackController")

/*
The @SessionScoped annotation preserves the values of the VideoController
object's instance variables across multiple HTTP request-response cycles
as long as the user's established HTTP session is alive.
*/
@SessionScoped

/*
-----------------------------------------------------------------------------
Marking the VideoController class as "implements Serializable" implies that
instances of the class can be automatically serialized and deserialized. 

Serialization is the process of converting a class instance (object)
from memory into a suitable format for storage in a file or memory buffer, 
or for transmission across a network connection link.

Deserialization is the process of recreating a class instance (object)
in memory from the format under which it was stored.
-----------------------------------------------------------------------------
*/
public class TrackController implements Serializable {
    /*
    ===============================
    Instance Variables (Properties)
    ===============================
    */

    private Track selected;

    /*
    ================
    Instance Methods
    ================
    */

    public void unselect() {
        selected = null;
    }

    public String cancel() {
        selected = null;
        return "/index?faces-redirect=true";
    }

    public Track getSelected() {
        return selected;
    }

    public void setSelected(Track selected) {
        this.selected = selected;
    }
}
