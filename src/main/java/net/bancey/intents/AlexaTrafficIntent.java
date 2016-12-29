package net.bancey.intents;

import com.amazon.speech.speechlet.SpeechletResponse;
import com.google.maps.model.TravelMode;

/**
 * AlexaTraffic
 * Created by abance on 28/12/2016.
 */
public abstract class AlexaTrafficIntent {

    private String name;

    AlexaTrafficIntent(String name) {
        this.name = name;
    }

    public abstract SpeechletResponse handle(String origin, String destination, TravelMode travelMode);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
