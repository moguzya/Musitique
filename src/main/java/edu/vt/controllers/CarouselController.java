/*
 * Copyright (c) 2021. Musitique App was developed by Mehmet Oguz Yardimci, Vibhavi Peiris, and Joseph Conwell as CS5704 Software Engineering course assignment.
 *
 * https://www.linkedin.com/in/oguzyardimci/
 * https://www.linkedin.com/in/vibhavipeiris/?originalSubdomain=ca
 * https://conwell.info/
 */

package edu.vt.controllers;

import edu.vt.Pojos.Album;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.globals.Methods;
import org.primefaces.model.ResponsiveOption;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static edu.vt.globals.Constants.ACCESS_TOKEN;
import static edu.vt.globals.Constants.CLIENT;

@Named
@ViewScoped
public class CarouselController implements Serializable {

    List<Album> listOfNewReleases;

    public List<Album> getListOfNewReleases() {
        if(listOfNewReleases==null)
            requestNewReleases();
        return listOfNewReleases;
    }

    public void requestNewReleases() {
        System.out.println("I called requestNewReleases");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.spotify.com/v1/browse/new-releases?country=US&limit=24&offset=0"))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .GET()
                .build();

        try {
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<Album> albums = new ArrayList<>();
                JSONArray albumArray = new JSONObject(response.body()).getJSONObject("albums").getJSONArray("items");

                for (int i = 0; i < albumArray.length(); i++) {
                    Album a = new Album(albumArray.getJSONObject(i).toString());
                    albums.add(a);
                }
                listOfNewReleases = albums;
                return;
            } else if (response.statusCode() == 401) {
                Methods.requestToken();
                requestNewReleases();
                return;
            } else if (response.statusCode() == 429) {
                JsfUtil.addErrorMessage("Api rate limit exceeded!");
                listOfNewReleases =  new ArrayList<>();
                return;
            }
        } catch (IOException | InterruptedException e) {
            JsfUtil.addErrorMessage(e.toString());
            listOfNewReleases =  new ArrayList<>();
            return;
        }
        JsfUtil.addErrorMessage("Unexpected error occurred!");
        listOfNewReleases =  new ArrayList<>();
        return;
    }

    public void setListOfNewReleases(List<Album> listOfNewReleases) {
        this.listOfNewReleases = listOfNewReleases;
    }
}