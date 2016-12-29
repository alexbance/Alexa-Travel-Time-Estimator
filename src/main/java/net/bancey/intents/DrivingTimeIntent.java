package net.bancey.intents;

import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.*;
import net.bancey.gmaps.GMapsApp;
import org.joda.time.DateTime;

/**
 * AlexaTraffic
 * Created by abance on 28/12/2016.
 */
public class DrivingTimeIntent extends AlexaTrafficIntent {

    public DrivingTimeIntent(String name) {
        super(name);
    }

    @Override
    public SpeechletResponse handle(String origin, String destination) {
        GeoApiContext context = new GMapsApp().getContext();
        DistanceMatrixApiRequest request = new DistanceMatrixApiRequest(context)
                .origins(origin + ", GB")
                .destinations(destination + ", GB")
                .mode(TravelMode.DRIVING)
                .trafficModel(TrafficModel.PESSIMISTIC)
                .departureTime(new DateTime(System.currentTimeMillis()));

        DistanceMatrix matrix;
        try {
            matrix = request.await();
        } catch (Exception ex) {
            matrix = null;
            ex.printStackTrace();
        }
        String speechText = "Sorry, I couldn't calculate the ETA.";
        if (matrix != null) {
            for (DistanceMatrixRow row : matrix.rows) {
                for(DistanceMatrixElement element: row.elements) {
                    DateTime eta = new DateTime(System.currentTimeMillis());
                    eta.plus(element.durationInTraffic.inSeconds);
                    speechText = "There is " + element.distance + " between " + origin + " and " + destination + ". It will take you approximately " + element.durationInTraffic + " to reach " + destination + ". Your ETA is " + eta.toLocalTime();
                    System.out.println("Distance: " + element.distance + " Duration: " + element.duration + " Duration in traffic: " + element.durationInTraffic);
                }
            }
        }
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        SimpleCard card = new SimpleCard();
        card.setTitle("Travel between " + origin + " and " + destination + ".");
        card.setContent(speechText);

        return SpeechletResponse.newTellResponse(speech, card);
    }
}
