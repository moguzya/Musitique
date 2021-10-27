/*
 * Created by Osman Balci on 2021.7.17
 * Copyright © 2021 Osman Balci. All rights reserved.
 */
package edu.vt.MovieSearch;

public class SearchedMovieCastMember {
    /*
    =====================================================================
    Instance variables representing the attributes of a movie cast member
    =====================================================================
     */
    private String actorName;
    private String characterName;   // Name of character played by the actor
    private String actorPhotoFileName;

    /*
    =============================================================
    Class constructor for instantiating a SearchedMovieCastMember
    object to represent a particular movie cast member.
    =============================================================
     */
    public SearchedMovieCastMember(String actorName, String characterName, String actorPhotoFileName) {
        this.actorName = actorName;
        this.characterName = characterName;
        this.actorPhotoFileName = actorPhotoFileName;
    }

    /*
    ===================================================================
    Getter and Setter methods for the attributes of a movie cast member
    ===================================================================
     */
    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public String getActorPhotoFileName() {
        return actorPhotoFileName;
    }

    public void setActorPhotoFileName(String actorPhotoFileName) {
        this.actorPhotoFileName = actorPhotoFileName;
    }
}
