package bparkClient.logic;

/**
 * Singleton class that holds session-related data for the application.
 * Stores information such as the current subscriber's code and the origin screen for details view.
 */
public class AppSessionData {

    private static AppSessionData instance;

    private String subscriberCode;
    private String detailsOrigin;

    /**
     * Private constructor to prevent instantiation.
     */
    private AppSessionData() {
        // private constructor
    }

    /**
     * Returns the singleton instance of AppSessionData.
     *
     * @return the single instance of AppSessionData
     */
    public static AppSessionData getInstance() {
        if (instance == null) {
            instance = new AppSessionData();
        }
        return instance;
    }

    /**
     * Gets the current subscriber code stored in the session.
     *
     * @return the subscriber code
     */
    public String getSubscriberCode() {
        return subscriberCode;
    }

    /**
     * Sets the subscriber code for the current session.
     *
     * @param subscriberCode the subscriber code to set
     */
    public void setSubscriberCode(String subscriberCode) {
        this.subscriberCode = subscriberCode;
    }

    /**
     * Gets the origin identifier indicating from where the details page was accessed.
     *
     * @return the details origin string
     */
    public String getDetailsOrigin() {
        return detailsOrigin;
    }

    /**
     * Sets the origin identifier for the details view.
     *
     * @param detailsOrigin the origin string to set
     */
    public void setDetailsOrigin(String detailsOrigin) {
        this.detailsOrigin = detailsOrigin;
    }

    /**
     * Clears the subscriber code from the session.
     */
    public void clear() {
        subscriberCode = null;
    }

    /**
     * Clears the origin information for the details view.
     */
    public void clearDetailsOrigin() {
        this.detailsOrigin = null;
    }
}
