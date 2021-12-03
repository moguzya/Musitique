package edu.vt.NewReleases;

import edu.vt.Pojos.Album;
import edu.vt.Pojos.Artist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;


@Named("releaseController")
@SessionScoped
public class ReleaseController implements Serializable {
    private List<Album> listOfNewReleases;
    private Album selected;

    public String newReleases(List<Album> listOfNewReleases) {
        this.listOfNewReleases = listOfNewReleases;
        return "/newReleases/NewReleases.xhtml";
    }

    public void unselect() {
        selected = null;
    }

    public List<Album> getListOfNewReleases() {
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
