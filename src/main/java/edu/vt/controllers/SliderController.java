/*
 * Created by Osman Balci on 2021.7.17
 * Copyright © 2021 Osman Balci. All rights reserved.
 */
package edu.vt.controllers;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named("sliderController")
@RequestScoped
public class SliderController {

    // The List contains image filenames, e.g., photo1.png, photo2.png, etc.
    private List<String> listOfSliderImageFilenames;

    /*
    The PostConstruct annotation is used on a method that needs to be executed after
    dependency injection is done to perform any initialization. The initialization 
    method init() is the first method invoked before this class is put into service. 
    */
    @PostConstruct
    public void init() {
        listOfSliderImageFilenames = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            listOfSliderImageFilenames.add("photo" + i + ".png");
        }
    }

    /*
    =============
    Getter Method
    =============
     */
    public List<String> getListOfSliderImageFilenames() {
        return listOfSliderImageFilenames;
    }

    /*
    ===============
    Instance Method
    ===============
    */
    public String description(String imageFilename) {

        String imageDescription = "";

        switch (imageFilename) {
            case "photo1.png":
                imageDescription = "Star Wars: Episode VII - The Force Awakens";
                break;
            case "photo2.png":
                imageDescription = "X-Men: Apocalypse";
                break;
            case "photo3.png":
                imageDescription = "The Hunger Games: Catching Fire";
                break;
            case "photo4.png":
                imageDescription = "Captain America: Civil War";
                break;
            case "photo5.png":
                imageDescription = "Batman v Superman: Dawn of Justice";
                break;
            case "photo6.png":
                imageDescription = "Iron Man 3";
                break;
            case "photo7.png":
                imageDescription = "Dawn of the Planet of the Apes";
                break;
            case "photo8.png":
                imageDescription = "Live Die Repeat: Edge of Tomorrow";
                break;
            case "photo9.png":
                imageDescription = "The Revenant";
                break;
            case "photo10.png":
                imageDescription = "The Martian";
                break;
            case "photo11.png":
                imageDescription = "Mad Max: Fury Road";
                break;
            case "photo12.png":
                imageDescription = "The Jungle Book";
                break;
        }

        return imageDescription;
    }
}
