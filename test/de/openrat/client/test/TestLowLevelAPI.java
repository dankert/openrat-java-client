package de.openrat.client.test;

import de.openrat.client.CMSClient;
import de.openrat.client.action.CMSAction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static de.openrat.client.test.TestConfiguration.*;

public class TestLowLevelAPI {

    private CMSClient client;

    /**
     *
     */
    @Before
    public void connect() {

        client = new CMSClient(TestConfiguration.HOST, PATH, PORT);
        client.setLogWriter(WRITER);
        client.setProxy(PROXY_HOST, PROXY_PORT);
        client.setLocale(LOCALE);
        client.setKeepAlive(false);
        client.setTimeout(15000);
    }


    /**
     * simple example for using the client.
     */
    @Test
    public void test() {
        final CMSAction action = client.createAction();

        action.executeView("login", "login", new HashMap<>());

        final Map<String, String> logindata = new HashMap<>();
        logindata.put("login_username", USER);
        logindata.put("login_password", PASS);
        logindata.put("dbid", DB);

        action.executePost("login", "login", logindata);
    }

    @After
    public void close() {

        client.close();
    }

}
