package de.openrat.client.test;

import de.openrat.client.CMSClient;
import org.junit.Test;

import static de.openrat.client.test.TestConfiguration.*;

public class LoginAndInfoTest {

    /**
     * simple example for using the client.
     */
    @Test
    public void testUserInfo() throws Exception {

        final CMSClient client = new CMSClient(TestConfiguration.HOST, PATH, PORT);
        client.setLogWriter(WRITER);
        client.setProxy(PROXY_HOST, PROXY_PORT);
        client.setLocale(LOCALE);
        client.setKeepAlive(false);
        client.setTimeout(15000);

		ExampleCMSTestAPI api = new ExampleCMSTestAPI(client);

		// let's login
		api.login( DB,USER, PASS);

		// print user info
		System.out.println(api.info());

		client.close();
    }

}
