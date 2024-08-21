package edu.escuelaing.arep;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class SimpleWebServer {
    private static final int PORT = 8080;
    public static final String WEB_ROOT = "src/main/java/edu/escuelaing/arep/resources/";
    public static Map<String, RestService> services = new HashMap<>();
    private static boolean running = true;


    public static void main(String[] args) throws IOException {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Ready to receive on port " + PORT + "...");
        addServices();
        while (running) {
            Socket clientSocket = serverSocket.accept();
            threadPool.submit(new ClientHandler(clientSocket));
        }
        serverSocket.close();
        threadPool.shutdown();
    }

    private static void addServices() {
        services.put("hello", new HelloService());
    }

    public static void stop() {
        running = false;
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedOutputStream dataOut = new BufferedOutputStream(clientSocket.getOutputStream())) {

            String requestLine = in.readLine();
            if (requestLine == null)
                return;
            String[] tokens = requestLine.split(" ");
            String method = tokens[0];
            String fileRequested = tokens[1];

            printRequestLine(requestLine, in);
            if (fileRequested.startsWith("/app")) {
                handleAppRequest(method, fileRequested, out);
                System.out.println("entro");
            } else {
                if (method.equals("GET")) {
                    handleGetRequest(fileRequested, out, dataOut);
                } else if (method.equals("POST")) {
                    handlePostRequest(fileRequested, out, dataOut);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close(); // Cerrando el socket aquí, después de procesar la solicitud
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void printRequestLine(String requestLine, BufferedReader in) {
        System.out.println("Request line: " + requestLine);
        String inputLine;
        try {
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Header: " + inputLine);
                if (in.ready()) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleGetRequest(String fileRequested, PrintWriter out, BufferedOutputStream dataOut)
            throws IOException {
        File file = new File(SimpleWebServer.WEB_ROOT, fileRequested);
        int fileLength = (int) file.length();
        String content = getContentType(fileRequested);

        if (file.exists()) {
            byte[] fileData = readFileData(file, fileLength);
            out.println("HTTP/1.1 200 OK");
            out.println("Content-type: " + content);
            out.println("Content-length: " + fileLength);
            out.println();
            out.flush();
            dataOut.write(fileData, 0, fileLength);
            dataOut.flush();
        } else {
            out.println("HTTP/1.1 404 Not Found");
            out.println("Content-type: text/html");
            out.println();
            out.flush();
            out.println("<html><body><h1>File Not Found</h1></body></html>");
            out.flush();
        }
    }

    private void handlePostRequest(String fileRequested, PrintWriter out, BufferedOutputStream dataOut) throws IOException {
        StringBuilder payload = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                payload.append(line);
            }
        }

        out.println("HTTP/1.1 200 OK");
        out.println("Content-type: text/html");
        out.println();
        out.println("<html><body><h1>POST data received:</h1>");
        out.println("<p>" + payload.toString() + "</p>");
        out.println("</body></html>");
        out.flush();
    }

    private void handleAppRequest(String method, String fileRequested, PrintWriter out) {
        System.out.println("Entró en handleAppRequest");
        
        // Log para mostrar el método HTTP y la solicitud recibida
        System.out.println("Método HTTP: " + method);
        System.out.println("Archivo solicitado: " + fileRequested);
        
        // Enviar la cabecera de la respuesta
        out.println("HTTP/1.1 200 OK");
        out.println("Content-type: text/plain");
        out.println();
        
        // Log para verificar el servicio que se obtiene
        RestService service = SimpleWebServer.services.get("hello");
        if (service != null) {
            System.out.println("Servicio obtenido: " + service.getClass().getName());
        } else {
            System.out.println("Error: El servicio 'hello' no se encontró en 'SimpleWebServer.services'");
        }
        
        // Generación de la respuesta y log para mostrar el contenido
        String response = service != null ? service.response(fileRequested) : "Error: Servicio no disponible";
        System.out.println("La respuesta es: " + response);
        
        // Enviar la respuesta al cliente
        out.println(response);
        out.flush();
        
        System.out.println("Respuesta enviada y conexión flush");
    }
    

    private String getContentType(String fileRequested) {
        if (fileRequested.endsWith(".html"))
            return "text/html";
        else if (fileRequested.endsWith(".css"))
            return "text/css";
        else if (fileRequested.endsWith(".js"))
            return "application/javascript";
        else if (fileRequested.endsWith(".png"))
            return "image/png";
        else if (fileRequested.endsWith(".jpg"))
            return "image/jpeg";
        return "text/plain";
    }

    private byte[] readFileData(File file, int fileLength) throws IOException {
        FileInputStream fileIn = null;
        byte[] fileData = new byte[fileLength];
        try {
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        } finally {
            if (fileIn != null)
                fileIn.close();
        }
        return fileData;
    }
}
