package edu.escuelaing.arep;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * The HelloService class implements the RestService interface
 * and provides a service that responds with a personalized greeting.
 * It extracts a name parameter from a URL-encoded string and returns
 * a greeting message in plain text.
 */
public class HelloService implements RestService {

    /**
     * Processes the incoming request, which is expected to be a URL-encoded string,
     * and extracts the value associated with the "name" parameter. If the "name"
     * parameter is found, it returns a greeting message in the format "Hola, <name>".
     * If the parameter is not found or an error occurs during decoding, it returns an
     * appropriate error message.
     *
     * @param request the URL-encoded string containing the "name" parameter.
     * @return a plain text response containing a greeting message with the provided name,
     * or an error message if the parameter is missing or cannot be decoded.
     */
    @Override
    public String response(String request) {
        String[] requestParams = request.split("=");
        if (requestParams.length > 1) {
            try {
                String decodedName = URLDecoder.decode(requestParams[1], StandardCharsets.UTF_8.name());

                String plainTextResponse = "Hola, " + decodedName;
                System.out.println("Respuesta texto plano: " + plainTextResponse);
                
                return plainTextResponse;
            } catch (Exception e) {
                return "Error al decodificar el nombre.";
            }
        } else {
            return "Error: No se proporcionó ningún nombre.";
        }
    }

}
