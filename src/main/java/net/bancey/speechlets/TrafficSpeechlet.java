package net.bancey.speechlets;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.google.maps.model.TravelMode;
import net.bancey.intents.AlexaTrafficIntent;
import net.bancey.intents.TravelTimeIntent;

import java.util.Map;

/**
 * AlexaTraffic
 * Created by abance on 28/12/2016.
 */
public class TrafficSpeechlet implements Speechlet {

    private static final String DEST_KEY = "Destination";
    private static final String ORIGIN_KEY = "Origin";
    private static final String TRAVEL_MODE_KEY = "TravelMode";
    private AlexaTrafficIntent[] intents = {new TravelTimeIntent("TravelETAIntent")};

    @Override
    public void onSessionStarted(SessionStartedRequest sessionStartedRequest, Session session) throws SpeechletException {
        //Startup logic
    }

    @Override
    public SpeechletResponse onLaunch(LaunchRequest launchRequest, Session session) throws SpeechletException {
        return onLaunchResponse();
    }

    @Override
    public SpeechletResponse onIntent(IntentRequest intentRequest, Session session) throws SpeechletException {
        Intent intent = intentRequest.getIntent();
        System.out.println(intent.getName());
        if ("AMAZON.StopIntent".equals(intent.getName())) {
            return onExitResponse();
        } else if ("AMAZON.CancelIntent".equals(intent.getName())) {
            return onExitResponse();
        } else if ("AMAZON.HelpIntent".equals(intent.getName())) {
            return onHelpResponse();
        } else {
            for (AlexaTrafficIntent alexaTrafficIntent : intents) {
                if (alexaTrafficIntent.getName().equals(intent.getName())) {
                    Map<String, Slot> slots = intent.getSlots();
                    Slot destinationSlot = slots.get(DEST_KEY);
                    Slot originSlot = slots.get(ORIGIN_KEY);
                    Slot travelMode = slots.get(TRAVEL_MODE_KEY);
                    if(travelMode.getValue() != null) {
                        switch(travelMode.getValue()) {
                            case "walking":
                                return alexaTrafficIntent.handle(originSlot.getValue(), destinationSlot.getValue(), TravelMode.WALKING);
                            case "driving":
                                return alexaTrafficIntent.handle(originSlot.getValue(), destinationSlot.getValue(), TravelMode.DRIVING);
                            case "bicycling":
                                return alexaTrafficIntent.handle(originSlot.getValue(), destinationSlot.getValue(), TravelMode.BICYCLING);
                            default:
                                return alexaTrafficIntent.handle(originSlot.getValue(), destinationSlot.getValue(), TravelMode.DRIVING);
                        }
                    }
                    return alexaTrafficIntent.handle(originSlot.getValue(), destinationSlot.getValue(), TravelMode.DRIVING);
                }
            }
        }
        return onErrorResponse();
    }

    @Override
    public void onSessionEnded(SessionEndedRequest sessionEndedRequest, Session session) throws SpeechletException {
        //Shutdown logic
    }

    private SpeechletResponse onHelpResponse() {
        String speechText = "Welcome! This skill allows you to get an ETA to travel from point A to point B. Example usage: 'Alexa, ask traffic for driving eta from Bisley to Woking Surrey'. You can substitute driving for walking or cycling, if you don't specify or the specified value is invalid driving will be used. Note this skill only works with GB locations.";
        String repromptText = "What would you like to do?";

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
        repromptSpeech.setText(repromptText);

        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(repromptSpeech);

        return SpeechletResponse.newAskResponse(speech, reprompt);
    }

    private SpeechletResponse onLaunchResponse() {
        String speechTest = "Hello, this is the travel time estimator skill! What would you like to do?";

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechTest);

        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt);
    }

    private SpeechletResponse onErrorResponse() {
        String speechText = "I encountered an error while processing your request.";
        String repromptText = "Please try again.";

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
        repromptSpeech.setText(repromptText);

        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(repromptSpeech);

        return SpeechletResponse.newAskResponse(speech, reprompt);
    }

    private SpeechletResponse onExitResponse() {
        String speechText = "Goodbye!";

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech);
    }
}
