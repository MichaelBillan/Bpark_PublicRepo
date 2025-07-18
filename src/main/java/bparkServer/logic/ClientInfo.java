package bparkServer.logic;

import javafx.beans.property.*;

public class ClientInfo {
    private final StringProperty hostName = new SimpleStringProperty();
    private final StringProperty ip = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty();

    public ClientInfo(String hostName, String ip, String status) {
        this.hostName.set(hostName);
        this.ip.set(ip);
        this.status.set(status);
    }


    public StringProperty hostNameProperty() { return hostName; }
    public StringProperty ipProperty() { return ip; }
    public StringProperty statusProperty() { return status; }


    public String getHostName() { return hostName.get(); }
    public String getIp() { return ip.get(); }
    public String getStatus() { return status.get(); }


    public void setStatus(String newStatus) { status.set(newStatus); }
}
