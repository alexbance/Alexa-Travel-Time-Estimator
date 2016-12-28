package net.bancey;

import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.servlet.SpeechletServlet;
import net.bancey.speechlets.TrafficSpeechlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * AlexaTraffic
 * Created by abance on 28/12/2016.
 */
@Controller
@SpringBootApplication
@EnableAutoConfiguration
public class AlexaTraffic extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(AlexaTraffic.class, args);

    }

    @RequestMapping("/")
    @ResponseBody
    public String home() {
        return "Alexa-Traffic is running!";
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AlexaTraffic.class);
    }

    /*
     * Method that allows us to map our Speechlet that handles the Alexa requests to the url /alexa
     */
    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        return new ServletRegistrationBean(createServlet(new TrafficSpeechlet()), "/alexa");
    }

    /*
     * Method that wraps a Speechlet in a SpeechletServlet which we can publish using spring
     */
    private SpeechletServlet createServlet(final Speechlet speechlet) {
        SpeechletServlet servlet = new SpeechletServlet();
        servlet.setSpeechlet(speechlet);
        return servlet;
    }
}