package de.openrat.client.test;

import static org.junit.Assert.fail;

import java.io.PrintWriter;
import java.util.Locale;

import javax.security.auth.login.LoginException;

import org.junit.Test;

import de.openrat.client.CMSClient;
import de.openrat.client.action.LoginAction;

public class TestLogin
{

	/**
	 * simple example for using the client.
	 */
	@Test
	public void test()
	{

		CMSClient client = new CMSClient("demo.openrat.de", "/latest-snapshot/openrat/dispatcher.php", 80);
		client.setLogWriter(new PrintWriter(System.out, true));
//		client.setProxy("proxy.mycompany.exmaple", 8080, "user", "pass");
		client.setLocale(Locale.GERMAN);
		client.setKeepAlive(false);
		client.setTimeout(15000);
		LoginAction loginAction = client.getLoginAction();

		try
		{
			loginAction.login("admin", "admin", "db1");
		}
		catch (LoginException e)
		{
			fail("Login failed" + e.getLocalizedMessage());
		}
	}

}
