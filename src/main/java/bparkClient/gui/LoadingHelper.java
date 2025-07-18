package bparkClient.gui;

import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Utility class to show a loading overlay and blur effect during long-running tasks.
 * Provides multiple overloaded methods to support different task handling scenarios.
 */
public class LoadingHelper {

    /** Default blur effect applied to main content during loading. */
    private static final GaussianBlur blurEffect = new GaussianBlur(10);

    /**
     * Runs a background task while showing a loading overlay and applying blur.
     * The UI is restored once the task completes.
     *
     * @param loadingOverlay the node to display during loading (e.g., VBox)
     * @param mainContent    the node to apply the blur effect to
     * @param runnable       the background task to execute
     */
    public static void runWithLoading(VBox loadingOverlay, VBox mainContent, Runnable runnable) {
        loadingOverlay.setVisible(true);
        mainContent.setEffect(blurEffect);

        new Thread(() -> {
            try {
                runnable.run();
            } finally {
                Platform.runLater(() -> {
                    loadingOverlay.setVisible(false);
                    mainContent.setEffect(null);
                });
            }
        }).start();
    }

    /**
     * Runs a background task and a follow-up UI task while showing loading overlay and blur.
     *
     * @param loadingOverlay the node to display during loading
     * @param mainContent    the node to apply the blur effect to
     * @param backgroundTask the background task to run off the UI thread
     * @param onUiThread     the task to run on the JavaFX application thread after completion
     */
    public static void runWithLoading(VBox loadingOverlay, VBox mainContent, Runnable backgroundTask, Runnable onUiThread) {
        loadingOverlay.setVisible(true);
        mainContent.setEffect(new GaussianBlur(10));

        new Thread(() -> {
            backgroundTask.run();
            Platform.runLater(() -> {
                loadingOverlay.setVisible(false);
                mainContent.setEffect(null);
                onUiThread.run();
            });
        }).start();
    }

    /**
     * Runs a task with blur and overlay for a generic Parent and Node.
     *
     * @param mainContent the parent node to blur
     * @param overlay     the node to show during loading
     * @param task        the task to execute in the background
     */
    public static void runWithLoading(Parent mainContent, Node overlay, Runnable task) {
        Platform.runLater(() -> {
            overlay.setVisible(true);
            mainContent.setEffect(new GaussianBlur(10));
        });

        new Thread(() -> {
            task.run();
            Platform.runLater(() -> {
                overlay.setVisible(false);
                mainContent.setEffect(null);
            });
        }).start();
    }

    /**
     * Runs a background task and handles success/failure cases with optional error handling.
     *
     * @param loadingOverlay the node to display during loading
     * @param mainContent    the node to blur
     * @param backgroundTask the background task to execute
     * @param uiTask         the task to run on UI thread after success
     * @param errorHandler   optional consumer to handle exceptions thrown during background task
     */
    public static void runWithLoading(
            VBox loadingOverlay,
            VBox mainContent,
            Runnable backgroundTask,
            Runnable uiTask,
            Consumer<Throwable> errorHandler
    ) {
        Platform.runLater(() -> {
            loadingOverlay.setVisible(true);
            mainContent.setEffect(new BoxBlur(5, 5, 3));
        });

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                try {
                    backgroundTask.run();
                } catch (Throwable e) {
                    if (errorHandler != null) {
                        errorHandler.accept(e);
                    } else {
                        e.printStackTrace();
                    }
                    throw new RuntimeException(e); // triggers failed()
                }
                return null;
            }

            @Override
            protected void succeeded() {
                Platform.runLater(() -> {
                    uiTask.run();
                    loadingOverlay.setVisible(false);
                    mainContent.setEffect(null);
                });
            }

            @Override
            protected void failed() {
                Platform.runLater(() -> {
                    loadingOverlay.setVisible(false);
                    mainContent.setEffect(null);
                });
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
}
