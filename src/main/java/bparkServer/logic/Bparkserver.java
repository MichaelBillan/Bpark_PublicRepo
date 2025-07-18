package bparkServer.logic;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.concurrent.*;

import bparkServer.db.DBConnection;
import bparkServer.db.DataBaseController;
import common.*;
import common.entities.Subscriber;
import ocsf.server.*;

import bparkServer.notifications.NotificationController;
import bparkServer.scheduler.SchedulerController;

/**
 * Main server class for the BPARK application.
 * Extends AbstractServer from OCSF and handles incoming client connections and requests.
 */
public class Bparkserver extends AbstractServer {

    private DBConnection dbPool;
    private final ExecutorService queryExecutor; // To handle queries concurrently
    private java.util.function.Consumer<ConnectionToClient> clientConnectedCallback;
    private java.util.function.Consumer<ConnectionToClient> clientDisconnectedCallback;

    /**
     * Constructs a new Bparkserver instance on the specified port.
     * Initializes the database connection pool and thread pool for handling queries.
     *
     * @param port the port number the server will listen on
     */
    public Bparkserver(int port) {
        super(port);

        dbPool = DBConnection.getInstance();
        System.out.println("Server initialized. Current DB pool size: " + dbPool.getCurrentPoolSize() + " / " + dbPool.getMaxPoolSize());
        queryExecutor = Executors.newFixedThreadPool(dbPool.getMaxPoolSize() + 2);
    }

    /**
     * Handles incoming messages from clients.
     * Executes database queries asynchronously to avoid blocking client threads.
     *
     * @param msg    the message object received
     * @param client the client who sent the message
     */
    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        if (msg instanceof Request) {
            Request req = (Request) msg;
            RequestType type = req.getType();
            Object payload = req.getPayload();
            if (type == RequestType.DISCONNECT) {
                System.out.println("Received DISCONNECT from " + client.getInetAddress());
                try {
                    client.close();
                } catch (IOException e) {
                    System.err.println("Failed to close client: " + e.getMessage());
                }
                return;
            }
            queryExecutor.submit(() -> {
                Object response = DataBaseController.handleRequest(type, payload);
                try {
                    System.out.println("Sending response: " + response + " (type=" + response.getClass().getName() + ")");
                    client.sendToClient(response);
                } catch (IOException e) {
                    System.err.println("Failed to send response: " + e.getMessage());
                }
            });
        } else {
            System.out.println("Received unknown message type: " + msg.getClass().getName());
        }
    }

    /**
     * Invoked when a new client connects to the server.
     *
     * @param client the client that connected
     */
    @Override
    protected void clientConnected(ConnectionToClient client) {
        System.out.println("Client connected: " + client.getInetAddress() +
            ". Active clients: " + getNumberOfClients());
        if (clientConnectedCallback != null) {
            clientConnectedCallback.accept(client);
        }
    }

    /**
     * Invoked when a client disconnects from the server.
     *
     * @param client the client that disconnected
     */
    @Override
    protected void clientDisconnected(ConnectionToClient client) {
        if (clientDisconnectedCallback != null) {
            clientDisconnectedCallback.accept(client);
        }
    }

    /**
     * Handles exceptions thrown by a connected client.
     *
     * @param client    the client that caused the exception
     * @param exception the exception thrown
     */
    @Override
    protected void clientException(ConnectionToClient client, Throwable exception) {
        System.err.println("Client " + client.getInetAddress() + " exception: " + exception.getMessage());
    }

    /**
     * Called when the server successfully starts listening on its port.
     */
    @Override
    protected void serverStarted() {
        System.out.println("Query Server started. Listening on port " + getPort());
    }

    /**
     * Called when the server stops listening and initiates shutdown of resources.
     */
    @Override
    protected void serverStopped() {
        System.out.println("Server stopped. Attempting to close DB pool...");
        queryExecutor.shutdown();
        try {
            if (!queryExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
                System.err.println("Query executor did not terminate in time, forcing shutdown...");
                queryExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            System.err.println("Server shutdown interrupted: " + e.getMessage());
            queryExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        dbPool.closeAllConnections();
        System.out.println("Server fully stopped and DB pool closed.");
    }

    /**
     * Called after serverStopped() if stopListening() was invoked.
     */
    @Override
    protected void serverClosed() {
        System.out.println("Server closed gracefully.");
    }

    /**
     * Main method to launch the server.
     * Initializes scheduler and sets up shutdown hook.
     *
     * @param args optional command line arguments (first can be port number)
     */
    public static void main(String[] args) {
        int port = 5555; // Default port
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number. Using default port " + port);
            }
        }

        Bparkserver server = new Bparkserver(port);
        DataBaseController.testConnection();
        try {
            server.listen();
        } catch (IOException e) {
            System.err.println("Could not start server: " + e.getMessage());
            e.printStackTrace();
        }

        NotificationController notificationController = new NotificationController();
        SchedulerController scheduler = new SchedulerController(notificationController);
        scheduler.startNotificationScheduler(1_800_000); // Every 30 minutes
        scheduler.startDailySpotStatusRefresh(); // Every day at 00:01

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nShutting down server via shutdown hook...");
            server.stopListening();
        }));
    }

    /**
     * Sets a callback function to execute when a client connects.
     *
     * @param callback a consumer that receives the client connection
     */
    public void setClientConnectedCallback(java.util.function.Consumer<ConnectionToClient> callback) {
        this.clientConnectedCallback = callback;
    }

    /**
     * Sets a callback function to execute when a client disconnects.
     *
     * @param callback a consumer that receives the client disconnection
     */
    public void setClientDisconnectedCallback(java.util.function.Consumer<ConnectionToClient> callback) {
        this.clientDisconnectedCallback = callback;
    }

}
