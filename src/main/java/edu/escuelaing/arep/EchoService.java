package edu.escuelaing.arep;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The EchoService class implements the RestService interface
 * and provides a simple echo service that extracts a "text"
 * field from a JSON string and returns it as a plain text response.
 */
public class EchoService implements RestService {

    /**
     * Processes the incoming message, which is expected to be a JSON string,
     * and extracts the value of the "text" field. If the "text" field is found,
     * it returns a response in the format "Echo: <text>". If the field is not found,
     * it returns an error message indicating that the field was not found.
     *
     * @param message the JSON string containing the "text" field to be echoed back.
     * @return a plain text response containing the value of the "text" field
     * or an error message if the field is not found.
     */
    @Override
    public String response(String message) {
        Pattern pattern = Pattern.compile("\"text\"\\s*:\\s*\"(.*?)\"");
        Matcher matcher = pattern.matcher(message);

        String text = "";
        if (matcher.find()) {
            text = matcher.group(1);
        } else {
            text = "Error: Campo 'text' no encontrado";
        }
        String plainTextResponse = "Echo: " + text;
        System.out.println("Respuesta texto plano: " + plainTextResponse);
        return plainTextResponse;
    }
}


