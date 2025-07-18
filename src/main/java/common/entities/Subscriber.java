package common.entities;

import java.io.Serializable;

/**
 * Represents a subscriber to the Bpark system.
 * Includes subscriber ID, contact information, and a tagId that simulates a physical tag.
 */
public class Subscriber implements Serializable {
    private String subId;
    private String name;
    private String phoneNumber;
    private String email;
    private String tagId;

    
    public Subscriber() {
    }

    public Subscriber(String subId, String name, String phoneNumber, String email) {
        this.subId = subId;
        this.name = name; 
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getSubId() {
        return subId;
    }

    public String getTagId() {
        return subId;
    }
    
    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Subscriber [subId=" + subId + ", tagId=" + tagId +
                ", name=" + getName() +
                ", phoneNumber=" + phoneNumber +
                ", email=" + email + "]";
    }
}
