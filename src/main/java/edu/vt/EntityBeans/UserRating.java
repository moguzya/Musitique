/*
 * Copyright (c) 2021. Musitique App was developed by Mehmet Oguz Yardimci, Vibhavi Peiris, and Joseph Conwell as CS5704 Software Engineering course assignment.
 *
 * https://www.linkedin.com/in/oguzyardimci/
 * https://www.linkedin.com/in/vibhavipeiris/?originalSubdomain=ca
 * https://conwell.info/
 */

package edu.vt.EntityBeans;

import edu.vt.Pojos.EntityType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

/*
The @Entity annotation designates this class as a JPA Entity POJO class
representing the UserRating table in the database.
 */

@Entity

@Table(name = "UserRating")

@NamedQueries({
    /*
    private User userId;    --> userId is the object reference of the User object.
    userId.id               --> User object's database primary key
     */
        @NamedQuery(name = "UserRating.findByUserId", query = "SELECT r FROM UserRating r WHERE r.userId.id = :userId")
})

public class UserRating implements Serializable {

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

    // entity_type VARCHAR(256) NOT NULL
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "entity_type")
    private String entityType;

    @Basic(optional = false)
    @NotNull
    @Column(name = "rating")
    private Integer rating;

    @Basic(optional = false)
    @NotNull
    @Column(name = "date")
    private Date date;

    public UserRating() {
        rating = 0;
    }

    public UserRating(User userId, String entityId, Integer rating, EntityType entityType) {
        this.userId = userId;
        this.entityId = entityId;
        this.rating = rating;
        this.date = new Date().from(Instant.now());
        this.entityType = entityType.toString();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof UserRating)) {
            return false;
        }
        UserRating other = (UserRating) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return id.toString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }


}