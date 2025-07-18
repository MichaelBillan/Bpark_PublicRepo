package common;

import java.io.Serializable;

/**
 * Request is a data container used for multi-threaded operations.
 * It holds relevant fields and provides getters/setters.
 */
public class Request implements Serializable {
    private final RequestType type;
    private final Object payload;

    public Request(RequestType type, Object payload) {
        this.type = type;
        this.payload = payload;
    }

    public RequestType getType() {
        return type;
    }

    public Object getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "Request{" + "type=" + type + ", payload=" + payload + '}';
    }
}
