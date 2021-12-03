package edu.vt.EntityBeans;

import edu.vt.EntityType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import edu.vt.EntityType;
import edu.vt.Pojos.Album;
import edu.vt.Pojos.Artist;
import edu.vt.Pojos.Track;
import edu.vt.controllers.EntityController;

/*
The @Entity annotation designates this class as a JPA Entity POJO class
representing the Video table in the UsersVideosDB database.
 */

@Entity

@Table(name = "UserComment")


@NamedQueries({
    /*
    private User userId;    --> userId is the object reference of the User object.
    userId.id               --> User object's database primary key
     */
        @NamedQuery(name = "UserComment.findByUserId", query = "SELECT c FROM UserComment c WHERE c.userId.id = :userId")
})
public class UserComment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    // user_id INT UNSIGNED
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
    @Size(min = 1, max = 256)
    @Column(name = "comment")
    private String comment;

    @Basic(optional = false)
    @NotNull
    @Column(name = "date")
    private LocalDateTime date;





    public UserComment() {
    }

    public UserComment(User userId, String entityId, EntityType entityType, String comment) {
        this.userId = userId;
        this.entityId = entityId;
        this.entityType = entityType.toString();
        this.comment = comment;
        this.date = LocalDateTime.now();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof UserComment)) {
            return false;
        }
        UserComment other = (UserComment) object;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }


    //Fields used for display purposes only
//
//    private Track track;
//    private Album album;
//    private Artist artist;
//
//    public Track getTrack() {
//        return track;
//    }
//
//    public void setTrack(Track track) {
//        this.track = track;
//    }
//
//    public Album getAlbum() {
//        return album;
//    }
//
//    public void setAlbum(Album album) {
//        this.album = album;
//    }
//
//    public Artist getArtist() {
//        return artist;
//    }
//
//    public void setArtist(Artist artist) {
//        this.artist = artist;
//    }
//
//    public String getEntityName(){
//        EntityController entityController = new EntityController();
//
//        switch (entityType) {
//            case "ALBUM":
//                return album.getName();
//            case "TRACK":
//                return track.getName();
//            case "ARTIST":
//                return artist.getName();
//        }
//        return "NOT FOUND";
//    }
//
//    public String getEntityArtists(){
//        EntityController entityController = new EntityController();
//
//        switch (entityType) {
//            case "ALBUM":
//                return album.getArtistsListAsString();
//            case "TRACK":
//                return track.getArtistsListAsString();
//            case "ARTIST":
//                return "N/A";
//        }
//        return "NOT FOUND";
//    }
//
//    public String getEntityImageUrl(){
//        EntityController entityController = new EntityController();
//
//        switch (entityType) {
//            case "ALBUM":
//                return album.getImageUrl();
//            case "TRACK":
//                return track.getImageUrl();
//            case "ARTIST":
//                return artist.getImageUrl();
//        }
//        return "NOT FOUND";
//    }
}