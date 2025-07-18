package common.entities;

import java.io.Serializable;

/**
 * Represents an usher in the Bpark system.
 * Extends GeneralUser and includes additional identifying and contact information.
 */
public class Usher implements Serializable{
    private final String usherId;
    private String phoneNumber;
    private String email;
    private String name;
    
    
    public Usher(String name, String phoneNumber, String email, String usherId) {
        this.name = name;
        this.usherId = usherId;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getUsherId() {
        return usherId;
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
    
	private String getName() {
		return name;
	}

    @Override
    public String toString() {
        return "Usher [usherId=" + usherId +
                ", name=" + getName() +
                ", phoneNumber=" + phoneNumber +
                ", email=" + email + "]";
    }


}
