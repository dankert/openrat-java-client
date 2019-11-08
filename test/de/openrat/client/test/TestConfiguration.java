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
	public static final String DB = "demo";
	public static String PROXY_HOST = null;
	public static int PROXY_PORT = 8080;

	public static final PrintWriter WRITER = new PrintWriter(System.out, true);
	public static final Locale LOCALE = Locale.ENGLISH;

	static {
		final String proxyHost = System.getenv("PROXY_HOST");
		if (proxyHost != null)
			PROXY_HOST = proxyHost;

		final String proxyPort = System.getenv("PROXY_PORT");
		if (proxyPort != null)
			PROXY_PORT = Integer.parseInt(proxyPort);
	}
}
