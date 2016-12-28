package net.bancey.gmaps;

import com.google.maps.GeoApiContext;

/**
 * AlexaTraffic
 * Created by abance on 28/12/2016.
 */
public class GMapsApp {

    private GeoApiContext context;

    public GMapsApp() {
        context = new GeoApiContext().setApiKey("AIzaSyBhTQFb8hJIzXkcr_npqiowyw9v3MUD_44");
    }

    public GeoApiContext getContext() {
        return context;
    }
}
