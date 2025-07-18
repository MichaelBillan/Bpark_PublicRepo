package common;

import java.util.ArrayList;
import java.util.List;

/**
 * A basic implementation of the Observable design pattern.
 * Maintains a list of observers and notifies them of changes.
 */
public class Observable {
    private final List<Observer> observers = new ArrayList<>();

    /**
     * Adds an observer to the list.
     *
     * @param obs the observer to be added
     */
    public void addObserver(Observer obs) {
        observers.add(obs);
    }

    /**
     * Removes an observer from the list.
     *
     * @param obs the observer to be removed
     */
    public void removeObserver(Observer obs) {
        observers.remove(obs);
    }

    /**
     * Notifies all registered observers with the given data.
     *
     * @param data the data to pass to each observer
     */
    protected void notifyObservers(Object data) {
        for (Observer obs : observers) {
            obs.update(data);
        }
    }
}
