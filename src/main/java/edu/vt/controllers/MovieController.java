/*
 * Created by Osman Balci on 2021.7.20
 * Copyright © 2021 Osman Balci. All rights reserved.
 */
package edu.vt.controllers;

import edu.vt.EntityBeans.Movie;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.controllers.util.JsfUtil.PersistAction;
import edu.vt.FacadeBeans.MovieFacade;
import edu.vt.globals.Methods;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

@Named("movieController")
@SessionScoped
public class MovieController implements Serializable {
    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */
    private List<Movie> listOfMovies = null;
    private Movie selected;
    private String idOfVideoToPlay;

    /*
    The @EJB annotation directs the EJB Container Manager to inject (store) the object reference of the
    MovieFacade bean into the instance variable 'movieFacade' after it is instantiated at runtime.
     */
    @EJB
    private MovieFacade movieFacade;

    /*
    =========================
    Getter and Setter Methods
    =========================
     */
    public List<Movie> getListOfMovies() {
        if (listOfMovies == null) {
            listOfMovies = movieFacade.findAll();
        }
        return listOfMovies;
    }

    public Movie getSelected() {
        return selected;
    }

    public void setSelected(Movie selected) {
        this.selected = selected;
    }

    public String getIdOfVideoToPlay() {
        return idOfVideoToPlay;
    }

    public void setIdOfVideoToPlay(String idOfVideoToPlay) {
        this.idOfVideoToPlay = idOfVideoToPlay;
    }

    /*
    ================
    Instance Methods
    ================
    */

    /*
     **************************************
     *   Unselect Selected Movie Object   *
     **************************************
     */
    public void unselect() {
        selected = null;
    }

    /*
     *************************************
     *   Cancel and Display List.xhtml   *
     *************************************
     */
    public String cancel() {
        // Unselect previously selected movie object if any
        selected = null;
        return "/movieFavorites/List?faces-redirect=true";
    }

    /*
    *****************************
    Prepare to Create a New Movie
    *****************************
    */
    public void prepareCreate() {
        /*
        Instantiate a new Movie object and store its object reference into
        instance variable 'selected'. The Movie class is defined in Movie.java
         */
        selected = new Movie();
    }

    /*
    An enum is a special Java type used to define a group of constants.
    The constants CREATE, DELETE and UPDATE are defined as follows in JsfUtil.java

            public enum PersistAction {
                CREATE,
                DELETE,
                UPDATE
            }
     */

    /*
    **********************************
    CREATE a New Movie in the Database
    **********************************
     */
    public void create() {
        Methods.preserveMessages();

        persist(PersistAction.CREATE,"Movie was Successfully Created!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The CREATE operation is successfully performed.
            selected = null;        // Remove selection
            listOfMovies = null;    // Invalidate listOfMovies to trigger re-query.
        }
    }

    /*
    *************************************
    UPDATE Selected Movie in the Database
    *************************************
     */
    public void update() {
        Methods.preserveMessages();

        persist(PersistAction.UPDATE,"Movie was Successfully Updated!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The UPDATE operation is successfully performed.
            selected = null;        // Remove selection
            listOfMovies = null;    // Invalidate listOfMovies to trigger re-query.
        }
    }

    /*
    ***************************************
    DELETE Selected Movie from the Database
    ***************************************
     */
    public void destroy() {
        Methods.preserveMessages();

        persist(PersistAction.DELETE,"Movie was Successfully Deleted!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The DELETE operation is successfully performed.
            selected = null;        // Remove selection
            listOfMovies = null;    // Invalidate listOfMovies to trigger re-query.
        }
    }

    /*
     **********************************************************************************************
     *   Perform CREATE, UPDATE (EDIT), and DELETE (DESTROY, REMOVE) Operations in the Database   *
     **********************************************************************************************
     */
    /**
     * @param persistAction refers to CREATE, UPDATE (Edit) or DELETE action
     * @param successMessage displayed to inform the user about the result
     */
    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            try {
                if (persistAction != PersistAction.DELETE) {
                    /*
                     -------------------------------------------------
                     Perform CREATE or EDIT operation in the database.
                     -------------------------------------------------
                     The edit(selected) method performs the SAVE (STORE) operation of the "selected"
                     object in the database regardless of whether the object is a newly
                     created object (CREATE) or an edited (updated) object (EDIT or UPDATE).
                    
                     MovieFacade inherits the edit(selected) method from the AbstractFacade class.
                     */
                    movieFacade.edit(selected);
                } else {
                    /*
                     -----------------------------------------
                     Perform DELETE operation in the database.
                     -----------------------------------------
                     The remove(selected) method performs the DELETE operation of the "selected"
                     object in the database.
                    
                     MovieFacade inherits the remove(selected) method from the AbstractFacade class.
                     */
                    movieFacade.remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex,"A persistence error occurred.");
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex,"A persistence error occurred.");
            }
        }
    }

}
