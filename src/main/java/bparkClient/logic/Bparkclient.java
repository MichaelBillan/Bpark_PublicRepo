/**
 * This code was written with inspiration from lecturer Ilya Zeldner.
 */
package bparkClient.logic;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

import common.Request;
import common.RequestType;

/**

 * Client-side communication handler for Bpark system.
 * Manages socket connection, request sending, synchronous/asynchronous response handling,
 * and cleanup.

 */
public class Bparkclient {

    /** TCP socket for client-server communication. */
    private final Socket socket;

    /** Output stream to send serialized requests to server. */
    private final ObjectOutputStream out;

    /** Input stream to receive serialized responses from server. */
    private final ObjectInputStream in;

    /** Queue for storing incoming async responses. */
    private final BlockingQueue<Object> incomingResponses = new LinkedBlockingQueue<>();

    /** Single-threaded executor for sending requests asynchronously. */
    private final ExecutorService threadPool = Executors.newSingleThreadExecutor();

    /** Singleton instance of the client. */
    private static Bparkclient instance;

    /**

     * Returns the singleton instance of the client.
     *
     * @return the current client instance

     */
    public static Bparkclient getInstance() {
        return instance;
    }

    /**

     * Establishes a socket connection with the server at the given host and port.
     * Initializes input/output streams.
     *
     * @param host the server hostname or IP

     * @param port the server port
     * @throws IOException if connection or stream setup fails
     */
    public Bparkclient(String host, int port) throws IOException {
        Socket s = new Socket();
        s.connect(new InetSocketAddress(host, port), 5000); // Connect timeout
        s.setSoTimeout(5000); // Read timeout
        this.socket = s;

        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        instance = this;
        System.out.println("Connected to server at " + host + ":" + port);
    }

    /**

     * Sends a request to the server asynchronously using the internal thread pool.
     *
     * @param type    the request type
     * @param payload the request payload

     */
    public void sendRequest(RequestType type, Object payload) {
        threadPool.execute(() -> {
            try {
                Request req = new Request(type, payload);
                out.writeObject(req);
                out.flush();
                System.out.println("Sent: " + req);
            } catch (IOException e) {
                System.err.println("Failed to send request: " + e.getMessage());
            }
        });
    }

    /**

     * Listens for incoming asynchronous responses from the server.
     * Responses are placed into a blocking queue for further handling.
r
     */
    public void listenForResponses() {
        Thread listenerThread = new Thread(() -> {
            try {
                Object response;
                while ((response = in.readObject()) != null) {
                    System.out.println("Received async: " + response);
                    incomingResponses.put(response);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error in listener: " + e.getMessage());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); 
            }
        }, "AsyncListenerThread");

        listenerThread.setDaemon(true); 
        listenerThread.start();
    }

    /**
     * Sends a request to the server and waits synchronously for a response.
     *
     * @param type    the request type
     * @param payload the request payload

     * @return the response object, or null if an error occurred
     */
    public Object sendAndWait(RequestType type, Object payload) {
        try {
            Request req = new Request(type, payload);
            out.writeObject(req);
            out.flush();
            System.out.println("Sent (sync): " + req);

            Object response = in.readObject();
            System.out.println("Received (sync): " + response);
            return response;

        } catch (Exception e) {
            System.err.println("Error in sendAndWait: " + e.getMessage());
            return null;
        }
    }

    /**
     * Closes the client connection, streams, and thread pool.
     * Also clears any session data stored locally.

     */
    public void close() {
        try {
            sendAndWait(RequestType.DISCONNECT, null);
            in.close();
            out.close();
            socket.close();
            threadPool.shutdown();
            AppSessionData.getInstance().clear();
            System.out.println("Connection closed.");
        } catch (IOException e) {
            System.err.println("Error closing client: " + e.getMessage());
        }
    }
}
