# Simple Concurrent Web Server

This project implements a concurrent HTTP server in Java that serves static files from a specified directory and handles multiple client requests concurrently using a fixed-size thread pool.

![Demo GIF](https://github.com/alexandrac1420/Aplicaciones_Distribuidas/blob/master/out/diagrama/Dise%C3%B1o%20sin%20t%C3%ADtulo.gif)


## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

You need to install the following tools and configure their dependencies:

1. **Java** (versions 7 or 8)
    ```sh
    java -version
    ```
    Should return something like:
    ```sh
    java version "1.8.0"
    Java(TM) SE Runtime Environment (build 1.8.0-b132)
    Java HotSpot(TM) 64-Bit Server VM (build 25.0-b70, mixed mode)
    ```

2. **Maven**
    - Download Maven from [here](http://maven.apache.org/download.html)
    - Follow the installation instructions [here](http://maven.apache.org/download.html#Installation)

    Verify the installation:
    ```sh
    mvn -version
    ```
    Should return something like:
    ```sh
    Apache Maven 3.2.5 (12a6b3acb947671f09b81f49094c53f426d8cea1; 2014-12-14T12:29:23-05:00)
    Maven home: /Users/dnielben/Applications/apache-maven-3.2.5
    Java version: 1.8.0, vendor: Oracle Corporation
    Java home: /Library/Java/JavaVirtualMachines/jdk1.8.0.jdk/Contents/Home/jre
    Default locale: es_ES, platform encoding: UTF-8
    OS name: "mac os x", version: "10.10.1", arch: "x86_64", family: "mac"
    ```

3. **Git**
    - Install Git by following the instructions [here](http://git-scm.com/book/en/v2/Getting-Started-Installing-Git)

    Verify the installation:
    ```sh
    git --version
    ```
    Should return something like:
    ```sh
    git version 2.2.1
    ```

### Installing

1. Clone the repository and navigate into the project directory:
    ```sh
    git clone https://github.com/alexandrac1420/Aplicaciones_Distribuidas.git

    cd Aplicaciones_Distribuidas
    ```

2. Build the project:
    ```sh
    mvn package
    ```

    Should display output similar to:
    ```sh
    [INFO] --- jar:3.3.0:jar (default-jar) @ AplicacionesDistriuidas ---
    [INFO] Building jar: C:\Users\alexa\OneDrive\Escritorio\Aplicaciones_Distribuidas\target\AplicacionesDistriuidas-1.0-SNAPSHOT.jar
    [INFO] BUILD SUCCESS
    ```

3. Run the application:
    ```sh
    java -cp target/AplicacionesDistriuidas-1.0-SNAPSHOT.jar edu.escuelaing.arep.SimpleWebServer
    ```
    When running the application, the following message should appear

    ```
    Ready to receive on port 8080...
    ```

    And now you can access `index.html` and other static files.

    
## Architecture

![Architecture Diagram](https://github.com/alexandrac1420/Aplicaciones_Distribuidas/blob/master/out/diagrama/diagrama.png)

### Overview

The Simple Concurrent Web Server is designed to handle multiple HTTP client requests concurrently using a thread pool. The server is capable of serving static files and processing simple REST-like API requests.

### Components

#### 1. **SimpleWebServer**
   - **Role**: This is the main class of the server. It initializes a `ServerSocket` on a specified port and listens for incoming client connections. The server uses a fixed-size thread pool (`ExecutorService`) to handle each client request concurrently.
   - **Responsibilities**:
     - Accept incoming client connections.
     - Delegate the handling of each client connection to a `ClientHandler`.
     - Manage the lifecycle of the server, including startup and shutdown.

#### 2. **ClientHandler**
   - **Role**: This class is responsible for processing individual client requests. It implements the `Runnable` interface, allowing it to be executed in a separate thread by the thread pool.
   - **Responsibilities**:
     - Read and parse the HTTP request from the client.
     - Serve static files from the specified directory or handle REST-like API requests.
     - Send appropriate HTTP responses back to the client, including handling errors such as "404 Not Found".
     - Close the client socket after processing the request to ensure no resource leaks.

### Interaction Flow

1. **Server Initialization**: The `SimpleWebServer` starts and initializes a `ServerSocket` on port 8080. It also sets up a thread pool with a fixed number of threads.

2. **Request Handling**:
   - When a client sends an HTTP request, the server accepts the connection and creates a new `ClientHandler` instance.
   - The `ClientHandler` reads the request, determines whether it’s a request for a static file or a REST-like API request, and processes it accordingly.
   - The appropriate response (file content, JSON response, or an error message) is sent back to the client.

3. **Concurrency**: Multiple client requests are handled concurrently by the thread pool, allowing the server to efficiently manage several connections at the same time.

4. **Shutdown**: When the server needs to be stopped, it gracefully shuts down the thread pool and closes the server socket.



## Class Diagram

![Class Diagram](https://github.com/alexandrac1420/Aplicaciones_Distribuidas/blob/master/out/diagrama/diagramaClases.png)


### Overview

The class diagram for the Simple Concurrent Web Server project provides a detailed view of the components involved in handling HTTP requests and managing server operations. The primary classes include `SimpleWebServer`, `ClientHandler`, `RestService`, and its implementation `HelloService`.

### Class Descriptions

#### 1. **SimpleWebServer**
   - **Role**: The `SimpleWebServer` class acts as the central controller of the server. It listens for incoming HTTP requests on a specific port (typically port 8080) and delegates the handling of these requests to `ClientHandler` instances.
   - **Key Responsibilities**:
     - **ServerSocket Management**: Maintains a `ServerSocket` that listens for client connections.
     - **Concurrency Handling**: Utilizes a `Thread Pool (ExecutorService)` to manage multiple client connections concurrently.
     - **Routing**: Routes incoming requests to the appropriate handler, which could be static file serving or REST API processing.

#### 2. **ClientHandler**
   - **Role**: The `ClientHandler` class is responsible for processing individual HTTP requests. It reads the request, determines the appropriate action (such as serving a static file or processing a REST API request), and returns the response to the client.
   - **Key Responsibilities**:
     - **Request Handling**: Parses HTTP requests and determines the type of response needed.
     - **Static File Serving**: Serves static files like HTML, CSS, and images from the server’s resources.
     - **REST API Processing**: Handles API requests by interacting with various services, such as the `HelloService`.

#### 3. **RestService**
   - **Role**: `RestService` is an abstract class or interface that defines the structure for handling RESTful API requests.
   - **Key Responsibilities**:
     - **Response Generation**: Provides a method to generate responses based on the requests received.

#### 4. **HelloService**
   - **Role**: The `HelloService` class implements the `RestService` interface and is specifically designed to handle requests directed at the `/hello` endpoint.
   - **Key Responsibilities**:
     - **Handling Specific Requests**: Responds to `/hello` requests with a pre-defined message or behavior.

### Relationships Between Classes

- **`SimpleWebServer` and `ClientHandler`**:
  - The `SimpleWebServer` class uses the `ClientHandler` to process each incoming client request. The `ClientHandler` is instantiated and managed by the server, allowing it to handle requests in separate threads provided by the thread pool.
  
- **`SimpleWebServer` and `RestService`**:
  - The `SimpleWebServer` class manages instances of `RestService`, routing requests that match certain patterns (like `/hello`) to the appropriate service. This allows for scalable and modular handling of different API endpoints.

- **`RestService` and `HelloService`**:
  - `HelloService` is a concrete implementation of `RestService`. It defines the specific behavior for the `/hello` endpoint, returning a greeting or other relevant information.


## Test Report - Simple Concurrent Web Server

### Author
Name: Alexandra Cortes Tovar

### Date
Date: 21/08/2024

### Summary
This report outlines the unit tests conducted for the Simple Concurrent Web Server project. Each test aimed to validate specific functionalities and behaviors of the server under various conditions.

### Tests Conducted

1. **Test `testHelloServiceResponse`**
   - **Description**: Validates the server's ability to handle requests to the REST-like hello service.
   - **Objective**: Ensure the server responds correctly with a greeting message when receiving GET requests at `/hello`.
   - **Testing Scenario**: Clients simulate HTTP GET requests to `/hello`.
   - **Expected Behavior**: The server should respond with `HTTP/1.1 200 OK` and the message "Hello, World!".
   - **Verification**: Confirms that all clients receive the correct response with the expected content.

2. **Test `testStaticFileRequest`**
   - **Description**: Checks the server's capability to serve static files.
   - **Objective**: Ensure that the server correctly serves files like `index.html` and images from the designated directory.
   - **Testing Scenario**: Multiple clients request static files simultaneously.
   - **Expected Behavior**: The server should respond with `HTTP/1.1 200 OK`, the correct `Content-Type`, and the file content.
   - **Verification**: Validates that the file is served correctly to all requesting clients.

3. **Test `test404NotFound`**
   - **Description**: Tests the server's behavior when a requested file does not exist.
   - **Objective**: Ensure the server responds with a `404 Not Found` status for non-existent resources.
   - **Testing Scenario**: A client requests a file that is not present in the server's root directory.
   - **Expected Behavior**: The server should respond with `HTTP/1.1 404 Not Found` and an appropriate error message.
   - **Verification**: Confirms that the server handles missing resources correctly.

## Built With


* [Maven](https://maven.apache.org/) - Dependency Management
* [Git](http://git-scm.com/) - Version Control System

## Versioning

I use [GitHub](https://github.com/) for versioning. For the versions available, see the [tags on this repository](https://github.com/alexandrac1420/Aplicaciones_Distribuidas.git).

## Authors

* **Alexandra Cortes Tovar** - [alexandrac1420](https://github.com/alexandrac1420)

## License

This project is licensed under the GNU
