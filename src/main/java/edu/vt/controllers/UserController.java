
/*
 * Copyright (c) 2021. Musitique App was developed by Mehmet Oguz Yardimci, Vibhavi Peiris, and Joseph Conwell as CS5704 Software Engineering course assignment.
 *
 * https://www.linkedin.com/in/oguzyardimci/
 * https://www.linkedin.com/in/vibhavipeiris/?originalSubdomain=ca
 * https://conwell.info/
 */

package edu.vt.controllers;

import edu.vt.EntityBeans.User;
import edu.vt.EntityBeans.UserComment;
import edu.vt.EntityBeans.UserPhoto;
import edu.vt.EntityBeans.UserRating;
import edu.vt.FacadeBeans.CommentFacade;
import edu.vt.FacadeBeans.RatingFacade;
import edu.vt.FacadeBeans.UserFacade;
import edu.vt.FacadeBeans.UserPhotoFacade;
import edu.vt.globals.Constants;
import edu.vt.globals.Methods;
import edu.vt.globals.Password;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import java.io.IOException;
import java.util.Objects;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named("userController")
@SessionScoped
public class UserController implements Serializable {
    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */
    private String username;
    private String password;
    private String confirmPassword;

    private String firstName;
    private String middleName;
    private String lastName;

    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zipcode;

    private int securityQuestionNumber;
    private String answerToSecurityQuestion;

    private String email;

    private Map<String, Object> security_questions;

    private User selected;

    private List<UserComment> listOfUserComments = null;
    private List<UserRating> listOfUserRatings = null;


    /*
    The @EJB annotation directs the EJB Container Manager to inject (store) the object reference of the
    UserFacade bean into the instance variable 'userFacade' after it is instantiated at runtime.
     */
    @EJB
    private UserFacade userFacade;

    /*
    The @EJB annotation directs the EJB Container Manager to inject (store) the object reference of the
    UserPhotoFacade bean into the instance variable 'userPhotoFacade' after it is instantiated at runtime.
     */
    @EJB
    private UserPhotoFacade userPhotoFacade;

    /*
    The @EJB annotation directs the EJB Container Manager to inject (store) the object reference of the
    CommentFacade bean into the instance variable 'commentFacade' after it is instantiated at runtime.
     */
    @EJB
    private CommentFacade commentFacade;

    /*
    The @EJB annotation directs the EJB Container Manager to inject (store) the object reference of the
    CommentFacade bean into the instance variable 'commentFacade' after it is instantiated at runtime.
     */
    @EJB
    private RatingFacade ratingFacade;

    /*
    =========================
    Getter and Setter Methods
    =========================
     */
    /*
    ***************************************************************
    Return the List of User Comments that Belong to the Signed-In User
    ***************************************************************
     */
    public List<UserComment> getListOfUserComments() {

        if (listOfUserComments == null) {
            /*
            'user', the object reference of the signed-in user, was put into the SessionMap
            in the initializeSessionMap() method in LoginManager upon user's sign in.
             */
            Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
            User signedInUser = (User) sessionMap.get("user");

            // Obtain the database primary key of the signedInUser object
            Integer primaryKey = signedInUser.getId();

            // Obtain only those videos from the database that belong to the signed-in user
            listOfUserComments = commentFacade.findUserCommentByUserPrimaryKey(primaryKey);
        }
        return listOfUserComments;
    }

    public void setlistOfUserComments(List<UserComment> listOfUserComments) {
        this.listOfUserComments = listOfUserComments;
    }

    public List<UserRating> getListOfUserRatings() {

        if (listOfUserRatings == null) {
            /*
            'user', the object reference of the signed-in user, was put into the SessionMap
            in the initializeSessionMap() method in LoginManager upon user's sign in.
             */
            Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
            User signedInUser = (User) sessionMap.get("user");

            // Obtain the database primary key of the signedInUser object
            Integer primaryKey = signedInUser.getId();

            // Obtain only those videos from the database that belong to the signed-in user
            listOfUserRatings = ratingFacade.findUserRatingByUserPrimaryKey(primaryKey);
        }
        return listOfUserRatings;
    }

    public void setListOfUserRatings(List<UserRating> listOfUserRatings) {
        this.listOfUserRatings = listOfUserRatings;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public int getSecurityQuestionNumber() {
        return securityQuestionNumber;
    }

    public void setSecurityQuestionNumber(int securityQuestionNumber) {
        this.securityQuestionNumber = securityQuestionNumber;
    }

    public String getAnswerToSecurityQuestion() {
        return answerToSecurityQuestion;
    }

    public void setAnswerToSecurityQuestion(String answerToSecurityQuestion) {
        this.answerToSecurityQuestion = answerToSecurityQuestion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String myCritiques()
    {
        return "/userAccount/Lists.xhtml";
    }
    /*
    --------------------------------------------------------------------------------
    private Map<String, Object> security_questions;
        String      int
        ---------   ---
        question1,  0
        question2,  1
        question3,  2
            :
    When the user selects a security question, its number (int) is stored; 
    not its String value. Later, given the number (int), the security question 
    String is obtained from the constant array QUESTIONS given in Constants.java.
    
    DESIGN STRATEGY:
        Typically, software quality characteristics Performance and Maintainability
        conflict with each other. Here, we choose to improve Performance at the 
        expense of degrading Maintainability.
    
        Storage and retrieval of an integer number into/from database is much faster
        than doing it for the security question as a String. However, this degrades
        maintainability. In the future, changing the order, deleting or adding 
        security questions will invalidate the number representation. Therefore,
        we can only add new security questions, but not delete or change order.
    --------------------------------------------------------------------------------
     */
    public Map<String, Object> getSecurity_questions() {

        if (security_questions == null) {
            /*
            Difference between HashMap and LinkedHashMap:
            HashMap stores key-value pairings in no particular order. 
                Values are retrieved based on their corresponding Keys.
            LinkedHashMap stores and retrieves key-value pairings
                in the order they were put into the map.
             */
            security_questions = new LinkedHashMap<>();

            for (int i = 0; i < Constants.QUESTIONS.length; i++) {
                security_questions.put(Constants.QUESTIONS[i], i);
            }
        }
        return security_questions;
    }

    public void setSecurity_questions(Map<String, Object> security_questions) {
        this.security_questions = security_questions;
    }

    public User getSelected() {
        if (selected == null) {
            // Store the object reference of the signed-in User into the instance variable selected.
            Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
            selected = (User) sessionMap.get("user");
        }
        // Return the object reference of the selected (i.e., signed-in) User object
        return selected;
    }

    public void setSelected(User selected) {
        this.selected = selected;
    }

    /*
    ================
    Instance Methods
    ================

    **********************************
    Return True if a User is Signed In
    **********************************
     */
    public boolean isLoggedIn() {
        /*
        The username of a signed-in user is put into the SessionMap in the
        initializeSessionMap() method in LoginManager upon user's sign in.
        If there is a username, that means, there is a signed-in user.
         */
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        return sessionMap.get("username") != null;
    }

    /*
    **************************************
    Return List of U.S. State Postal Codes
    **************************************
     */
    public String[] listOfStates() {
        return Constants.STATES;
    }

    /*
    ****************************************
    Return the Security Question Selected by
    the User at the Time of Account Creation
    ****************************************
     */
    public String getSelectedSecurityQuestion() {
        /*
        'user', the object reference of the signed-in user, was put into the SessionMap
        in the initializeSessionMap() method in LoginManager upon user's sign in.
         */
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        User signedInUser = (User) sessionMap.get("user");

        // Obtain the number of the security question selected by the user
        int questionNumber = signedInUser.getSecurityQuestionNumber();

        // Return the security question corresponding to the question number
        return Constants.QUESTIONS[questionNumber];
    }

    /*
    *********************************************
    Process Submitted Answer to Security Question
    *********************************************
     */
    public void securityAnswerSubmit() {
        /*
        'user', the object reference of the signed-in user, was put into the SessionMap
        in the initializeSessionMap() method in LoginManager upon user's sign in.
         */
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        User signedInUser = (User) sessionMap.get("user");

        String actualSecurityAnswer = signedInUser.getSecurityAnswer();

        // getAnswerToSecurityQuestion() is the Getter method for the property 'answerToSecurityQuestion'
        String enteredSecurityAnswer = getAnswerToSecurityQuestion();

        if (actualSecurityAnswer.equals(enteredSecurityAnswer)) {
            // Answer to the security question is correct; Delete the user's account.
            // deleteAccount() method is given below.
            deleteAccount();
        } else {
            Methods.showMessage("Error",
                    "Answer to the Security Question is Incorrect!", "");
        }
    }

    /*
    **********************************************************
    Create User's Account and Redirect to Show the SignIn Page
    **********************************************************
     */
    public String createAccount() {
        /*
        ----------------------------------------------------------------
        Password and Confirm Password are validated under 3 tests:
        
        <1> Non-empty (tested with required="true" in JSF page)
        <2> Correct composition satisfying the regex rule.
            (tested with <f:validator validatorId="passwordValidator" /> in the JSF page)
        <3> Password and Confirm Password must match (tested below)
        ----------------------------------------------------------------
         */
        if (!password.equals(confirmPassword)) {
            Methods.showMessage("Fatal Error", "Unmatched Passwords!",
                    "Password and Confirm Password must Match!");
            return "";
        }

        //--------------------------------------------
        // Password and Confirm Password are Validated
        //--------------------------------------------

        /*
        Redirecting to show a JSF page involves more than one subsequent requests and
        the messages would die from one request to another if not kept in the Flash scope.
        Since we will redirect to show the SignIn page, we invoke preserveMessages().
         */
        Methods.preserveMessages();

        //-----------------------------------------------------------
        // First, check if the entered username is already being used
        //-----------------------------------------------------------
        // Obtain the object reference of a User object with the username entered by the user
        User aUser = userFacade.findByUsername(username);

        if (aUser != null) {
            // A user already exists with the username entered by the user
            username = "";
            Methods.showMessage("Fatal Error",
                    "Username Already Exists!",
                    "Please Select a Different One!");
            return "";
        }

        //----------------------------------
        // The entered username is available
        //----------------------------------
        try {
            // Instantiate a new User object
            User newUser = new User();

            /*
             Set the properties of the newly created newUser object with the values
             entered by the user in the AccountCreationForm in CreateAccount.xhtml             
             */
            newUser.setFirstName(firstName);
            newUser.setMiddleName(middleName);
            newUser.setLastName(lastName);
            newUser.setAddress1(address1);
            newUser.setAddress2(address2);
            newUser.setCity(city);
            newUser.setState(state);
            newUser.setZipcode(zipcode);
            newUser.setSecurityQuestionNumber(securityQuestionNumber);
            newUser.setSecurityAnswer(answerToSecurityQuestion);
            newUser.setEmail(email);
            newUser.setUsername(username);

            /*
            Invoke class Password's createHash() method to convert the user-entered String
            password to a String containing the following parts
                  "algorithmName":"PBKDF2_ITERATIONS":"hashSize":"salt":"hash"
            for secure storage and retrieval with Key Stretching to prevent brute-force attacks.
             */
            String parts = Password.createHash(password);
            newUser.setPassword(parts);

            // Create the user in the database
            userFacade.create(newUser);

        } catch (EJBException | Password.CannotPerformOperationException ex) {
            username = "";
            Methods.showMessage("Fatal Error",
                    "Something went wrong while creating user's account!",
                    "See: " + ex.getMessage());
            return "";
        }

        Methods.showMessage("Information", "Success!",
                "User Account is Successfully Created!");

        /*
         The Profile page cannot be shown since the new User has not signed in yet.
         Therefore, we show the Sign In page for the new User to sign in first.
         */
        return "/SignIn?faces-redirect=true";
    }

    /*
    ***********************************************************
    Update User's Account and Redirect to Show the Profile Page
    ***********************************************************
     */
    public String updateAccount() {

        // Since we will redirect to show the Profile page, invoke preserveMessages()
        Methods.preserveMessages();

        /*
        'user', the object reference of the signed-in user, was put into the SessionMap
        in the initializeSessionMap() method in LoginManager upon user's sign in.
         */
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        User editUser = (User) sessionMap.get("user");

        try {
            /*
             Set the signed-in user's properties to the values entered by
             the user in the EditAccountProfileForm in EditAccount.xhtml.
             */
            editUser.setFirstName(this.selected.getFirstName());
            editUser.setMiddleName(this.selected.getMiddleName());
            editUser.setLastName(this.selected.getLastName());

            editUser.setAddress1(this.selected.getAddress1());
            editUser.setAddress2(this.selected.getAddress2());
            editUser.setCity(this.selected.getCity());
            editUser.setState(this.selected.getState());
            editUser.setZipcode(this.selected.getZipcode());
            editUser.setEmail(this.selected.getEmail());

            // Store the changes in the database
            userFacade.edit(editUser);

            Methods.showMessage("Information", "Success!",
                    "User's Account is Successfully Updated!");

        } catch (EJBException ex) {
            username = "";
            Methods.showMessage("Fatal Error",
                    "Something went wrong while updating user's profile!",
                    "See: " + ex.getMessage());
            return "";
        }

        // Account update is completed, redirect to show the Profile page.
        return "/userAccount/Profile?faces-redirect=true";
    }

    /*
    *****************************************************************
    Delete User's Account, Logout, and Redirect to Show the Home Page
    *****************************************************************
     */
    public void deleteAccount() {
        Methods.preserveMessages();
        /*
        The database primary key of the signed-in User object was put into the SessionMap
        in the initializeSessionMap() method in LoginManager upon user's sign in.
         */
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        int userPrimaryKey = (int) sessionMap.get("user_id");

        try {
            // Delete all of the photo files associated with the signed-in user whose primary key is userPrimaryKey
            deleteAllUserPhotos(userPrimaryKey);

            // Delete the User entity from the database
            userFacade.deleteUser(userPrimaryKey);

            Methods.showMessage("Information", "Success!",
                    "Your Account is Successfully Deleted!");

        } catch (EJBException ex) {
            username = "";
            Methods.showMessage("Fatal Error",
                    "Something went wrong while deleting user's account!",
                    "See: " + ex.getMessage());
            return;
        }

        // Execute the logout() method given below
        logout("/index.xhtml");
    }

    /*
    **********************************************
    Logout User and Redirect to Show the Home Page
    **********************************************
     */
    public void logout(String subPath) {

        // Clear the signed-in User's session map
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        sessionMap.clear();

        // Reset the signed-in User's properties
        username = password = confirmPassword = "";
        firstName = middleName = lastName = "";
        address1 = address2 = city = state = zipcode = "";
        securityQuestionNumber = 0;
        answerToSecurityQuestion = email = "";
        selected = null;

        // Since we will redirect to show the home page, invoke preserveMessages()
        Methods.preserveMessages();

        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

            // Invalidate the signed-in User's session
            externalContext.invalidateSession();

            /*
            getRequestContextPath() returns the URI of the webapp directory of the application.
            Obtain the URI of the index (home) page to redirect to.
             */
            String redirectPageURI = externalContext.getRequestContextPath() + subPath;

            externalContext.redirect(redirectPageURI);

            /*
            NOTE: We cannot use: return "/index?faces-redirect=true"; here because the user's session is invalidated.
             */
        } catch (IOException ex) {
            Methods.showMessage("Fatal Error",
                    "Unable to redirect to the index (home) page!",
                    "See: " + ex.getMessage());
        }
    }

    /*
    ***************************************
    Return Signed-In User's Thumbnail Photo
    ***************************************
     */
    public String userPhoto() {
        /*
        The database primary key of the signed-in User object was put into the SessionMap
        in the initializeSessionMap() method in LoginManager upon user's sign in.
         */
        if (!isLoggedIn()){
            return Constants.PHOTOS_URI + "loginImage.png";
        }
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        Integer primaryKey = (Integer) sessionMap.get("user_id");

        List<UserPhoto> photoList = userPhotoFacade.findPhotosByUserPrimaryKey(primaryKey);


        if (photoList.isEmpty()) {
            // No user photo exists. Return defaultUserPhoto.png.
            return Constants.PHOTOS_URI + "defaultUserPhoto.png";
        }

        /*
        photoList.get(0) returns the object reference of the first Photo object in the list.
        getThumbnailFileName() message is sent to that Photo object to retrieve its
        thumbnail image file name, e.g., 5_thumbnail.jpeg
         */
        String thumbnailFileName = photoList.get(0).getThumbnailFileName();

        return Constants.PHOTOS_URI + thumbnailFileName;
    }

    /*
    ********************************************************
    Delete the photo, thumbnail, and tempFile that belong to
    the User object whose database primary key is primaryKey
    ********************************************************
     */
    public void deleteAllUserPhotos(int primaryKey) {
        /*
        Obtain the list of Photo objects that belong to the User whose
        database primary key is userId.
         */
        List<UserPhoto> photoList = userPhotoFacade.findPhotosByUserPrimaryKey(primaryKey);

        // photoList.isEmpty implies that the user has never uploaded a photo file
        if (!photoList.isEmpty()) {

            // Obtain the object reference of the first Photo object in the list.
            UserPhoto photo = photoList.get(0);
            try {
                /*
                NOTE: Files and Paths are imported as
                        import java.nio.file.Files;
                        import java.nio.file.Paths;

                 Delete the user's photo if it exists.
                 getFilePath() is given in UserPhoto.java file.
                 */
                Files.deleteIfExists(Paths.get(photo.getPhotoFilePath()));

                /*
                 Delete the user's thumbnail image if it exists.
                 getThumbnailFilePath() is given in UserPhoto.java file.
                 */
                Files.deleteIfExists(Paths.get(photo.getThumbnailFilePath()));

                // Delete the photo file record from the database.
                // UserPhotoFacade inherits the remove() method from AbstractFacade.
                userPhotoFacade.remove(photo);

                /*
                 Delete the user's captured photo file if it exists.
                 The file is named "user's primary key_tempFile".
                 */
                String capturedPhotoFilepath = Constants.PHOTOS_ABSOLUTE_PATH + primaryKey + "_tempFile";
                Files.deleteIfExists(Paths.get(capturedPhotoFilepath));

            } catch (IOException ex) {
                Methods.showMessage("Fatal Error",
                        "Something went wrong while deleting user's photo!",
                        "See: " + ex.getMessage());
            }
        }
    }

    public Boolean isOwner(UserComment commentToCheck) {
        if (commentToCheck == null)
            return Boolean.FALSE;
        User userToCheck = commentToCheck.getUserId();
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        User signedInUser = (User) sessionMap.get("user");
        Integer primaryKey = signedInUser.getId();
        return userToCheck.getId() == primaryKey;
    }

}
