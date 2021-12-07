/*
 * Copyright (c) 2021. Musitique App was developed by Mehmet Oguz Yardimci, Vibhavi Peiris, and Joseph Conwell as CS5704 Software Engineering course assignment.
 *
 * https://www.linkedin.com/in/oguzyardimci/
 * https://www.linkedin.com/in/vibhavipeiris/?originalSubdomain=ca
 * https://conwell.info/
 */

package edu.vt.EntityBeans;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/*
The @Entity annotation designates this class as a JPA Entity POJO class
representing the UserFavoriteArtist table in the database.
 */

@Entity

@Table(name = "UserFavoriteArtist")

@NamedQueries({
    /*
    private User userId;    --> userId is the object reference of the User object.
    userId.id               --> User object's database primary key
     */
        @NamedQuery(name = "UserFavoriteArtists.findByUserId", query = "SELECT r FROM UserFavoriteArtist r WHERE r.userId.id = :userId")
})

public class UserFavoriteArtist implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private User userId;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "entity_id")
    private String entityId;

    public UserFavoriteArtist() {
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }
}