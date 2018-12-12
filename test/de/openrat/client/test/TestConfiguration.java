package de.openrat.client.test;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Locale;

public class TestConfiguration {

    public static final String HOST = "demo.openrat.de";
    public static final String PATH = "/";
    public static final int PORT = 80;
    public static final String USER = "admin";
    public static final String PASS = "admin";
    public static final String DB = "db1";
    public static final String PROXY_HOST = null;
    public static final int PROXY_PORT = 8080;

    public static final PrintWriter WRITER = new PrintWriter(System.out, true);
    public static final Locale LOCALE = Locale.ENGLISH;
}
