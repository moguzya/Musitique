
package edu.vt.NewReleases;

import edu.vt.Pojos.Album;

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

    @PostConstruct
    public void init() {

        selected = null;
        //TODO change the following
        listOfNewReleases = tempReadData();
    }


    private List<Album> tempReadData() {
        List<Album> tempListOfNewReleases;
        tempListOfNewReleases = new ArrayList<>();
        tempListOfNewReleases.add(new Album("4aawyAB9vmqN3uQ7FjRGTy", "Global Warming", "https://i.scdn.co/image/ab67616d0000b2732c5b24ecfa39523a75c993c4", "2006-06-19", 5));
        tempListOfNewReleases.add(new Album("2ZGACajeINbPfwLPbEuzwr", "Änglar", "https://i.scdn.co/image/ab67616d0000b273f54e06014d92f89dfe5baaf3", "2016-06-19", 15));
        tempListOfNewReleases.add(new Album("1VhOdgOjIARBn6SoNyeQDa", "Always Been You", "https://i.scdn.co/image/ab67616d0000b273f54e06014d92f89dfe5baaf3", "2026-06-19", 25));
        tempListOfNewReleases.add(new Album("3KrkQ77DF9OUB0aOzKFYOF", "Donda (Deluxe)", "https://i.scdn.co/image/ab67616d0000b273df9a35baaa98675256b35177", "2036-06-19", 35));
        return tempListOfNewReleases;
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
