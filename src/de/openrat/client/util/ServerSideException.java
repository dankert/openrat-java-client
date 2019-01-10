package de.openrat.client.util;

public class ServerSideException extends Exception {

    private String name;

    public ServerSideException() {

    }

    public ServerSideException(String message, String name) {
        super(message);
        this.name = name;
    }


    public ServerSideException(String message) {
        super(message);
    }


    public String toString() {
        // if name is set, use it and not the class name.
        String s = (name != null) ? name : getClass().getName();

        String message = getLocalizedMessage();
        return (message != null) ? (s + ": " + message) : s;
    }
}
