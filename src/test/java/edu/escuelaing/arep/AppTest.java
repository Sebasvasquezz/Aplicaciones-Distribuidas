// package edu.escuelaing.arep;

// import junit.framework.TestCase;
// import java.net.HttpURLConnection;
// import java.net.URL;
// import java.io.BufferedReader;
// import java.io.InputStreamReader;

// public class AppTest extends TestCase {

//     private static final String SERVER_URL = "http://localhost:8080/";
//     private Thread serverThread;

//     public void setUp() throws Exception {
//         serverThread = new Thread(() -> {
//             try {
//                 SimpleWebServer.main(null);
//             } catch (Exception e) {
//                 e.printStackTrace();
//             }
//         });
//         serverThread.start();
//         Thread.sleep(2000); // Esperar un poco para que el servidor se inicie
//     }

//     public void tearDown() throws Exception {
//         SimpleWebServer.stop(); // Detener el servidor
//         serverThread.join();
//     }

//     public void testHelloServiceResponse() throws Exception {
//         for (int i = 0; i < 5; i++) { 
//             URL url = new URL(SERVER_URL + "hello");
//             HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//             conn.setRequestMethod("GET");
//             BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//             String inputLine;
//             StringBuilder content = new StringBuilder();
//             while ((inputLine = in.readLine()) != null) {
//                 content.append(inputLine);
//             }
//             in.close();
//             conn.disconnect();
//             assertEquals("Hello, World!", content.toString());
//         }
//     }

//     public void testRestServiceResponse() throws Exception {
//         for (int i = 0; i < 5; i++) { 
//             URL url = new URL(SERVER_URL + "rest/test");
//             HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//             conn.setRequestMethod("GET");
//             BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//             String inputLine;
//             StringBuilder content = new StringBuilder();
//             while ((inputLine = in.readLine()) != null) {
//                 content.append(inputLine);
//             }
//             in.close();
//             conn.disconnect();
//             assertEquals("Response for test", content.toString());
//         }
//     }

//     public void testLoadStaticFile() throws Exception {
//         for (int i = 0; i < 5; i++) { 
//             URL url = new URL(SERVER_URL + "index.html");
//             HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//             conn.setRequestMethod("GET");
//             int responseCode = conn.getResponseCode();
//             assertEquals(200, responseCode);
//             conn.disconnect();
//         }
//     }

//     public void testInvalidRequest() throws Exception {
//         for (int i = 0; i < 5; i++) { 
//             URL url = new URL(SERVER_URL + "nonexistentfile.html");
//             HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//             conn.setRequestMethod("GET");
//             int responseCode = conn.getResponseCode();
//             assertEquals(404, responseCode);
//             conn.disconnect();
//         }
//     }

//     public void testMultipleConnections() throws Exception {
//         for (int i = 0; i < 10; i++) { 
//             URL url = new URL(SERVER_URL + "index.html");
//             HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//             conn.setRequestMethod("GET");
//             int responseCode = conn.getResponseCode();
//             assertEquals(200, responseCode);
//             conn.disconnect();
//         }
//     }
// }
