package bparkServer.db;

import ocsf.server.*;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Singleton class that manages a pool of database connections.
 * Uses a blocking queue to allow concurrent access in a thread-safe way.
 */
public class DBConnection {

    // Singleton instance
    private static DBConnection instance;

    /** Properties loaded from db.properties file. */
    static Properties props = new Properties();

    // Static initialisation block - properly syntax
    static {
        try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new RuntimeException("Cannot find db.properties file in classpath");
            }
            props.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load db.properties", e);
        }
    }

    // DB config
    private static final String DB_URL = props.getProperty("db.url");
    private static final String DB_USER = props.getProperty("db.username");
    private static final String DB_PASSWORD = props.getProperty("db.password");
    private static final int MAX_POOL_SIZE = 20; // Max number of connections in the pool
    private static final int INITIAL_POOL_SIZE = 10; // Initial connections to create

    /** The pool of database connections */
    private BlockingQueue<Connection> connectionPool;

    /**
     * Private constructor to enforce Singleton pattern.
     * Initializes the connection pool.
     */
    private DBConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load MySQL JDBC Driver
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
            throw new RuntimeException("Failed to load JDBC driver", e);
        }

        connectionPool = new ArrayBlockingQueue<>(MAX_POOL_SIZE);
        initializePool();
    }

    /**
     * Returns the singleton instance of DBConnection.
     * 
     * @return DBConnection instance
     */
    public static synchronized DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    /**
     * Initializes the connection pool with a fixed number of initial connections.
     */
    private void initializePool() {
        System.out.println("Initializing database connection pool with " + INITIAL_POOL_SIZE + " connections...");
        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            try {
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                connectionPool.offer(connection); // Add connection to the pool
                System.out.println("Connection " + (i + 1) + " created and added to pool.");
            } catch (SQLException e) {
                System.err.println("Error initializing connection pool: " + e.getMessage());
            }
        }
        System.out.println("Database connection pool initialized. Current size: " + connectionPool.size());
    }

    /**
     * Retrieves a valid database connection from the pool, or creates a new one if needed.
     * Waits up to 5 seconds if no connection is immediately available.
     * 
     * @return a valid {@link Connection} object
     * @throws SQLException if no valid connection is available
     * @throws InterruptedException if thread is interrupted while waiting
     */
    public Connection getConnection() throws SQLException, InterruptedException {
        Connection connection = connectionPool.poll(5, TimeUnit.SECONDS); // Wait up to 5 seconds

        if (connection == null) {
            synchronized (this) {
                if (connectionPool.size() < MAX_POOL_SIZE) {
                    try {
                        System.out.println("Pool exhausted, attempting to create a new connection...");
                        connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                        System.out.println("New connection created and provided.");
                    } catch (SQLException e) {
                        System.err.println("Failed to create new connection: " + e.getMessage());
                        throw new SQLException("Failed to get a connection from the pool or create a new one.", e);
                    }
                } else {
                    throw new SQLException("Database connection pool exhausted. Max connections reached.");
                }
            }
        } else {
            System.out.println("Connection retrieved from pool. Remaining: " + connectionPool.size());
        }

        if (connection != null && !connection.isValid(2)) {
            System.out.println("Invalid connection detected, attempting to replace it.");
            connectionPool.remove(connection);
            try {
                Connection newConnection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                return newConnection;
            } catch (SQLException e) {
                System.err.println("Failed to replace invalid connection: " + e.getMessage());
                throw new SQLException("Failed to replace invalid connection.", e);
            }
        }

        return connection;
    }

    /**
     * Releases a connection back to the pool if it is valid.
     * Otherwise, closes the connection.
     * 
     * @param connection the connection to release
     */
    public void releaseConnection(Connection connection) {
        if (connection != null) {
            try {
                if (connection.isValid(1) && connectionPool.offer(connection)) {
                    System.out.println("Connection released back to pool. Current size: " + connectionPool.size());
                } else {
                    System.err.println("Connection invalid or could not be added to pool. Closing it.");
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error validating or closing connection: " + e.getMessage());
            }
        }
    }

    /**
     * Closes all connections currently in the pool.
     */
    public void closeAllConnections() {
        System.out.println("Closing all connections in the pool...");
        while (!connectionPool.isEmpty()) {
            Connection connection = connectionPool.poll();
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("Connection closed.");
                } catch (SQLException e) {
                    System.err.println("Error closing pooled connection: " + e.getMessage());
                }
            }
        }
        System.out.println("All pooled connections closed.");
    }

    /**
     * Gets the current number of available connections in the pool.
     * 
     * @return current pool size
     */
    public int getCurrentPoolSize() {
        return connectionPool.size();
    }

    /**
     * Gets the maximum number of connections allowed in the pool.
     * 
     * @return max pool size
     */
    public int getMaxPoolSize() {
        return MAX_POOL_SIZE;
    }

}
