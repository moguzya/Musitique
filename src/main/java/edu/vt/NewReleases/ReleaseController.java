package edu.vt.NewReleases;

import edu.vt.Pojos.Album;
import edu.vt.Pojos.Artist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import static edu.vt.globals.Constants.API_CONTROLLER;

@Named("releaseController")
@SessionScoped
public class ReleaseController implements Serializable {
    private List<Album> listOfNewReleases;
    private Album selected;

    public void unselect() {
        selected = null;
    }

    public List<Album> getListOfNewReleases() {
        if (listOfNewReleases == null) {
            listOfNewReleases = API_CONTROLLER.requestNewReleases();
        }
        return listOfNewReleases;
    }

    public void setListOfNewReleases(List<Album> listOfNewReleases) {
        this.listOfNewReleases = listOfNewReleases;
    }

    public Album getSelected() {
        return selected;
    }

    public void setSelected(Album selected) {
        this.selected = selected;
    }

}
