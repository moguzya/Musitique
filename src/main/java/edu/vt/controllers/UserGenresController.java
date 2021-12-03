
package edu.vt.controllers;

import edu.vt.EntityBeans.User;
import edu.vt.EntityBeans.UserGenre;
import edu.vt.FacadeBeans.UserGenreFacade;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.controllers.util.JsfUtil.PersistAction;
import edu.vt.globals.Constants;
import edu.vt.globals.Methods;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named("userGenresController")
@SessionScoped

public class UserGenresController implements Serializable {
    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */
    private List<UserGenre> listOfUserGenres = null;
    private List<String> listOfUserGenreNames = null;

    private UserGenre selected;

    /*
    The @EJB annotation directs the EJB Container Manager to inject (store) the object reference of the
    UserVideoFacade bean into the instance variable 'userVideoFacade' after it is instantiated at runtime.
     */
    @EJB
    private UserGenreFacade userGenreFacade;


    /*
    =========================
    Getter and Setter Methods
    =========================
     */

    public List<String> getListOfUserGenreNames() {
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        User signedInUser = (User) sessionMap.get("user");
        if (listOfUserGenres==null){
            listOfUserGenres = userGenreFacade.findUserGenresByUserPrimaryKey(signedInUser.getId());
        }
        if (listOfUserGenreNames==null){
            listOfUserGenreNames = new ArrayList<String>();
            for (UserGenre genre: listOfUserGenres )
            {
                listOfUserGenreNames.add(genre.getGenre());
            }
        }
        return listOfUserGenreNames;
    }

    public void setListOfUserGenreNames(List<String> listOfUserGenreNames) {
        this.listOfUserGenreNames = listOfUserGenreNames;
    }

    public UserGenre getSelected() {
        return selected;
    }

    public void setSelected(UserGenre selected) {
        this.selected = selected;
    }

    /*
            ***************************************************************
            Return the List of User Videos that Belong to the Signed-In User
            ***************************************************************
             */
    public List<UserGenre> getListOfUserGenres() {

        if (listOfUserGenres == null) {
            /*
            'user', the object reference of the signed-in user, was put into the SessionMap
            in the initializeSessionMap() method in LoginManager upon user's sign in.
             */
            Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
            User signedInUser = (User) sessionMap.get("user");

            // Obtain the database primary key of the signedInUser object
            Integer primaryKey = signedInUser.getId();

            // Obtain only those videos from the database that belong to the signed-in user
            listOfUserGenres = userGenreFacade.findUserGenresByUserPrimaryKey(primaryKey);

        }
        return listOfUserGenres;
    }

    public void setListOfUserGenres(List<UserGenre> listOfUserGenres) {
        this.listOfUserGenres = listOfUserGenres;
    }


/*
    ================
    Instance Methods
    ================
    */

    // Return list of categories
    public List<UserGenre> getGenres() {
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        User signedInUser = (User) sessionMap.get("user");

        List<UserGenre> genres = new ArrayList<>();
        for (int i = 0; i < Constants.GENRES.size(); i++) {
            genres.add(new UserGenre(Constants.GENRES.get(i),signedInUser));
        }
        System.out.printf("-------------%s",genres);
        return genres;
    }


    /*
     **************************************
     *   Unselect Selected UserVideo Object   *
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
        // Unselect previously selected userVideo object if any
        selected = null;
        return "/userVideo/List?faces-redirect=true";
    }

    /*
    *****************************
    Prepare to Create a New UserVideo
    *****************************
    */
    public void prepareCreate() {
        /*
        Instantiate a new UserVideo object and store its object reference into
        instance variable 'selected'. The UserVideo class is defined in UserVideo.java
         */
        selected = new UserGenre();
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        User signedInUser = (User) sessionMap.get("user");
        if(signedInUser == null){
            JsfUtil.addSuccessMessage("NOT LOGGED IN");
        }
        else {
            selected.setUserId(signedInUser);
        }
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

    // The constants CREATE, DELETE and UPDATE are defined in JsfUtil.java

    /*
    **********************
    Create a New User Video
    **********************
     */
    public void create() {

        if (!JsfUtil.isValidationFailed()) {
            selected = null;            // Remove selection
            listOfUserGenres = null;     // Invalidate listOfUserVideos to trigger re-query.
        }
    }

    /*
     *************************************
     UPDATE Selected UserVideo in the Database
     *************************************
      */
    public void update(List<String> listOfUserGenres2) {
        System.out.printf("-------------- %s %s\n",selected, listOfUserGenres2);
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        User signedInUser = (User) sessionMap.get("user");

        persist(PersistAction.CREATE,"Favorite genres are set.");
        for (String genreName: listOfUserGenres2 )
        {
            UserGenre userGenreNew = new UserGenre(genreName,signedInUser);
            userGenreFacade.edit(userGenreNew);
        }

        //DELETE Ones the user unselected
        for (UserGenre genre: listOfUserGenres )
        {
            if ( !listOfUserGenres2.contains(genre)) {
                userGenreFacade.remove(genre);
            }
        }

        Methods.preserveMessages();

        persist(PersistAction.UPDATE,"User Video was successfully updated.");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The UPDATE operation is successfully performed.
            selected = null;        // Remove selection
            listOfUserGenres = null;    // Invalidate listOfUserVideos to trigger re-query.
        }
    }

    /*
     ***************************************
     DELETE Selected UserVideo from the Database
     ***************************************
      */
    public void destroy() {
        Methods.preserveMessages();

        persist(PersistAction.DELETE,"User Video was successfully deleted.");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The DELETE operation is successfully performed.
            selected = null;        // Remove selection
            listOfUserGenres = null;    // Invalidate listOfUserVideos to trigger re-query.
        }
    }

    /*
     ****************************************************************************
     *   Perform CREATE, EDIT (UPDATE), and DELETE Operations in the Database   *
     ****************************************************************************
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

                     userVideoFacade inherits the edit(selected) method from the AbstractFacade class.
                     */
                    userGenreFacade.edit(selected);
                } else {
                    /*
                     -----------------------------------------
                     Perform DELETE operation in the database.
                     -----------------------------------------
                     The remove method performs the DELETE operation in the database.

                     userVideoFacade inherits the remove(selected) method from the AbstractFacade class.
                     */
                    userGenreFacade.remove(selected);
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
                    JsfUtil.addErrorMessage(ex,"A Persistence Error Occurred!");
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex,"A Persistence Error Occurred!");
            }
        }
    }
}
