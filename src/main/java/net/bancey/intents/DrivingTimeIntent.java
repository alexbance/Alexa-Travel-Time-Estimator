package net.bancey.intents;

import com.amazon.speech.speechlet.SpeechletResponse;
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
    public SpeechletResponse handle(String destination) {
        GeoApiContext context = new GMapsApp().getContext();
        DistanceMatrixApiRequest request = new DistanceMatrixApiRequest(context)
                .origins("1A South Road, Bisley, Woking, GU24 9ES")
                .destinations(destination)
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
        if (matrix != null) {
            for (DistanceMatrixRow row : matrix.rows) {
                System.out.println(row);
                for(DistanceMatrixElement element: row.elements) {
                    System.out.println("Distance: " + element.distance + " Duration: " + element.duration + " Duration in traffic: " + element.durationInTraffic);
                }
            }
        }
        return null;
    }
}
