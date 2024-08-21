package edu.escuelaing.arep;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class HelloService implements RestService {

    @Override
    public String response(String request) {
        String[] requestParams = request.split("=");
        System.out.println("Parámetros divididos: " + Arrays.toString(requestParams));

        if (requestParams.length > 1) {
            try {
                // Decodifica el nombre
                String decodedName = URLDecoder.decode(requestParams[1], StandardCharsets.UTF_8.name());
                System.out.println(requestParams.length);

                // Texto plano
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
