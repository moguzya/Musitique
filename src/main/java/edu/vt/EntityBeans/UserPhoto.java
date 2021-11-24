/*
 * Created by Osman Balci on 2021.7.14
 * Copyright © 2021 Osman Balci. All rights reserved.
 */
package edu.vt.EntityBeans;

import edu.vt.globals.Constants;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/*
The @Entity annotation designates this class as a JPA Entity POJO class
representing the UserPhoto table in the CloudDriveDB database.
 */
@Entity

// Name of the database table represented
@Table(name = "UserPhoto")

@NamedQueries({
    /*
    private User userId;    --> userId is the object reference of the User object.
    userId.id               --> User object's database primary key
     */
    @NamedQuery(name = "UserPhoto.findPhotosByUserDatabasePrimaryKey", query = "SELECT p FROM UserPhoto p WHERE p.userId.id = :primaryKey")
    , @NamedQuery(name = "UserPhoto.findAll", query = "SELECT u FROM UserPhoto u")
    , @NamedQuery(name = "UserPhoto.findById", query = "SELECT u FROM UserPhoto u WHERE u.id = :id")
    , @NamedQuery(name = "UserPhoto.findByExtension", query = "SELECT u FROM UserPhoto u WHERE u.extension = :extension")
})

public class UserPhoto implements Serializable {
    /*
    ========================================================
    Instance variables representing the attributes (columns)
    of the UserPhoto table in the CloudDriveDB database.

    CREATE TABLE UserPhoto
    (
           id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL,
           extension ENUM('jpeg', 'jpg', 'png', 'gif') NOT NULL,
           user_id INT UNSIGNED,
           FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
    );
    ========================================================
     */
    private static final long serialVersionUID = 1L;

    // id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    // extension ENUM('jpeg', 'jpg', 'png', 'gif') NOT NULL
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "extension")
    private String extension;

    // user_id INT UNSIGNED
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private User userId;

    /*
    =================================================================
    Class constructors for instantiating a UserPhoto entity object to
    represent a row in the UserPhoto table in the CloudDriveDB database.
    =================================================================
     */
    public UserPhoto() {
    }

    // Called from UserPhotoController
    public UserPhoto(String fileExtension, User id) {
        this.extension = fileExtension;
        userId = id;
    }

    /*
    ======================================================
    Getter and Setter methods for the attributes (columns)
    of the UserPhoto table in the CloudDriveDB database.
    ======================================================
     */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExtension() {
        return extension;
    }

    public User getUserId() {
        return userId;
    }

    /*
    ================================
    Instance Methods Used Internally
    ================================
     */

    // Generate and return a hash code value for the object with database primary key id
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /*
     Checks if the UserPhoto object identified by 'object' is the same as the UserPhoto object identified by 'id'
     Parameter object = UserPhoto object identified by 'object'
     Returns True if the UserPhoto 'object' and 'id' are the same; otherwise, return False
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof UserPhoto)) {
            return false;
        }
        UserPhoto other = (UserPhoto) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    // Return String representation of database primary key id
    @Override
    public String toString() {
        return id.toString();
    }

    /*
    =============
    Other Methods
    =============
     */

    /*
    User's photo image file is named as "userId.fileExtension", e.g., 5.jpg for user with id 5.
    Since the user can have only one photo, this makes sense.
     */
    public String getPhotoFilename() {
        return getUserId() + "." + getExtension();
    }

    /*
    --------------------------------------------------------------------------------
    The thumbnail photo image size is set to 200x200px in Constants.java as follows:
    public static final Integer THUMBNAIL_SIZE = 200;
    
    If the user uploads a large photo file, we will scale it down to THUMBNAIL_SIZE
    and use it so that the size is reasonable for performance reasons.
    
    The photo image scaling is properly done by using the imgscalr-lib-4.2.jar file.
    
    The thumbnail file is named as "userId_thumbnail.fileExtension", 
    e.g., 5_thumbnail.jpg for user with id 5.
    --------------------------------------------------------------------------------
     */
    public String getThumbnailFileName() {
        return getUserId() + "_thumbnail." + getExtension();
    }

    public String getPhotoFilePath() {
        return Constants.PHOTOS_ABSOLUTE_PATH + getPhotoFilename();
    }

    public String getThumbnailFilePath() {
        return Constants.PHOTOS_ABSOLUTE_PATH + getThumbnailFileName();
    }

}
