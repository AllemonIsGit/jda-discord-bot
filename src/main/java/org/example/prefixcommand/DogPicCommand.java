package org.example.prefixcommand;

import com.jayway.jsonpath.JsonPath;
import lombok.Getter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DogPicCommand implements TextCommand {

    private static final String URL = "https://dog.ceo/api/breeds/image/random";

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Getter
    private final String invokePhrase = "dog";

    @Override
    public void execute(MessageReceivedEvent event) {
        try {

            final HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(new URI(URL))
                    .GET()
                    .build();

            final HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            final String link = JsonPath.parse(response.body()).read("$['message']");

            event.getChannel().sendMessage(link).queue();

        } catch (URISyntaxException e) {
            System.err.println("Masz chujowy link!" + e);
        } catch (IOException e) {
            System.err.println("Coś się spierdoliło z http!" + e);
        } catch (InterruptedException e) {
            System.err.println("Ktoś mi się tu wpierdala!" + e);
        }
    }
}
