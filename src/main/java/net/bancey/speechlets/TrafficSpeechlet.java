package net.bancey.speechlets;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import net.bancey.intents.AlexaTrafficIntent;
import net.bancey.intents.DrivingTimeIntent;

import java.util.Map;

/**
 * AlexaTraffic
 * Created by abance on 28/12/2016.
 */
public class TrafficSpeechlet implements Speechlet {

    private static final String DEST_KEY = "Destination";
    private static final String ORIGIN_KEY = "Origin";
    private AlexaTrafficIntent[] intents = {new DrivingTimeIntent("GetDrivingETAIntent")};

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
        if ("AMAZON.StopIntent".equals(intent.getName())) {
            return onExitResponse();
        } else if ("AMAZON.CancelIntent".equals(intent.getName())) {
            return onExitResponse();
        } else if ("AMAZON.HelpIntent".equals(intent.getName())) {
            return onLaunchResponse();
        } else {
            for (AlexaTrafficIntent alexaTrafficIntent : intents) {
                if (alexaTrafficIntent.getName().equals(intent.getName())) {
                    Map<String, Slot> slots = intent.getSlots();
                    Slot destinationSlot = slots.get(DEST_KEY);
                    Slot originSlot = slots.get(ORIGIN_KEY);
                    return alexaTrafficIntent.handle(originSlot.getValue(), destinationSlot.getValue());
                }
            }
        }
        return onErrorResponse();
    }

    @Override
    public void onSessionEnded(SessionEndedRequest sessionEndedRequest, Session session) throws SpeechletException {
        //Shutdown logic
    }

    private SpeechletResponse onLaunchResponse() {
        String speechText = "Welcome to the Alexa Traffic Skill, you can say 'driving time to destination'.";
        String repromptText = "What would you like to do?";

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
        repromptSpeech.setText(repromptText);

        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(repromptSpeech);

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
