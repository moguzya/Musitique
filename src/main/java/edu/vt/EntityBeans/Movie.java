/*
 * Created by Osman Balci on 2021.7.17
 * Copyright © 2021 Osman Balci. All rights reserved.
 */
package edu.vt.EntityBeans;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/*
The @Entity annotation designates this class as a JPA Entity POJO class
representing the Movie table in the MoviesDB database.
 */
@Entity

// Name of the database table represented
@Table(name = "Movie")

@NamedQueries({
    @NamedQuery(name = "Movie.findAll", query = "SELECT m FROM Movie m")
    , @NamedQuery(name = "Movie.findById", query = "SELECT m FROM Movie m WHERE m.id = :id")
    , @NamedQuery(name = "Movie.findByTitle", query = "SELECT m FROM Movie m WHERE m.title = :title")
    , @NamedQuery(name = "Movie.findByYoutubeTrailerId", query = "SELECT m FROM Movie m WHERE m.youtubeTrailerId = :youtubeTrailerId")
    , @NamedQuery(name = "Movie.findByGenres", query = "SELECT m FROM Movie m WHERE m.genres = :genres")
    , @NamedQuery(name = "Movie.findByReleaseDate", query = "SELECT m FROM Movie m WHERE m.releaseDate = :releaseDate")
    , @NamedQuery(name = "Movie.findByDirector", query = "SELECT m FROM Movie m WHERE m.director = :director")
    , @NamedQuery(name = "Movie.findByStars", query = "SELECT m FROM Movie m WHERE m.stars = :stars")
    , @NamedQuery(name = "Movie.findByFilmRating", query = "SELECT m FROM Movie m WHERE m.filmRating = :filmRating")
    , @NamedQuery(name = "Movie.findByPercentLiked", query = "SELECT m FROM Movie m WHERE m.percentLiked = :percentLiked")
})

public class Movie implements Serializable {
    /*
    ========================================================
    Instance variables representing the attributes (columns)
    of the Movie table in the MoviesDB database.

    CREATE TABLE Movie
    (
        id INT UNSIGNED NOT NULL AUTO_INCREMENT,
        title VARCHAR(255) NOT NULL,
        youtube_trailer_id VARCHAR(32) NOT NULL,
        genres VARCHAR(128) NOT NULL,
        release_date DATE NOT NULL,
        director VARCHAR(128) NOT NULL,
        stars VARCHAR(255) NOT NULL,
        film_rating VARCHAR(8) NOT NULL,
        percent_liked VARCHAR(8) NOT NULL,
        PRIMARY KEY (id)
    );

    ========================================================
     */
    private static final long serialVersionUID = 1L;

    // id INT UNSIGNED NOT NULL AUTO_INCREMENT
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    // Movie Title
    // title VARCHAR(255) NOT NULL
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "title")
    private String title;

    // youtube_trailer_id VARCHAR(32) NOT NULL
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "youtube_trailer_id")
    private String youtubeTrailerId;

    // genres VARCHAR(128) NOT NULL
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "genres")
    private String genres;

    // release_date DATE NOT NULL
    // Release date is recorded in the database as YYYY-MM-DD so that it is sortable
    @Basic(optional = false)
    @NotNull
    @Column(name = "release_date")
    @Temporal(TemporalType.DATE)
    private Date releaseDate;

    // director VARCHAR(128) NOT NULL
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "director")
    private String director;

    // stars VARCHAR(255) NOT NULL
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "stars")
    private String stars;

    // film_rating VARCHAR(8) NOT NULL
    // G, PG, PG-13, R, NC-17
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "film_rating")
    private String filmRating;

    // percent_liked VARCHAR(8) NOT NULL
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "percent_liked")
    private String percentLiked;

    /*
    =============================================================
    Class constructors for instantiating a Movie entity object to
    represent a row in the User table in the MoviesDB database.
    =============================================================
     */
    // Used in PrepareCreate method in MovieController
    public Movie() {
    }

    // Not used but kept for potential future use
    public Movie(Integer id) {
        this.id = id;
    }

    // Not used but kept for potential future use
    public Movie(Integer id, String title, String youtubeTrailerId, String genres, Date releaseDate, String director, String stars, String filmRating, String percentLiked) {
        this.id = id;
        this.title = title;
        this.youtubeTrailerId = youtubeTrailerId;
        this.genres = genres;
        this.releaseDate = releaseDate;
        this.director = director;
        this.stars = stars;
        this.filmRating = filmRating;
        this.percentLiked = percentLiked;
    }

    /*
    ======================================================
    Getter and Setter methods for the attributes (columns)
    of the Movie table in the MoviesDB database.
    ======================================================
     */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYoutubeTrailerId() {
        return youtubeTrailerId;
    }

    public void setYoutubeTrailerId(String youtubeTrailerId) {
        this.youtubeTrailerId = youtubeTrailerId;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public String getFilmRating() {
        return filmRating;
    }

    public void setFilmRating(String filmRating) {
        this.filmRating = filmRating;
    }

    public String getPercentLiked() {
        return percentLiked;
    }

    public void setPercentLiked(String percentLiked) {
        this.percentLiked = percentLiked;
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
     Checks if the Movie object identified by 'object' is the same as the Movie object identified by 'id'
     Parameter object = Movie object identified by 'object'
     Returns True if the Movie 'object' and 'id' are the same; otherwise, return False
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Movie)) {
            return false;
        }
        Movie other = (Movie) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    // Return String representation of database primary key id
    @Override
    public String toString() {
        return id.toString();
    }

}
