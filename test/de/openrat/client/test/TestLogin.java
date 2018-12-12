package de.openrat.client.test;

import de.openrat.client.CMSClient;
import de.openrat.client.action.LoginAction;
import org.junit.Test;

import javax.security.auth.login.LoginException;

import static de.openrat.client.test.TestConfiguration.*;
import static org.junit.Assert.fail;

public class TestLogin {

    /**
     * simple example for using the client.
     */
    @Test
    public void test() {
        final CMSClient client = new CMSClient(TestConfiguration.HOST, PATH, PORT);
        client.setLogWriter(WRITER);
        client.setProxy(PROXY_HOST, PROXY_PORT);
        client.setLocale(LOCALE);
        client.setKeepAlive(false);
        client.setTimeout(15000);

        LoginAction loginAction = client.createAction(LoginAction.class);

        try {
            loginAction.login(USER, PASS, DB);
        } catch (LoginException e) {
            fail("Login failed" + e.getLocalizedMessage());
        }

        client.close();
    }

}
