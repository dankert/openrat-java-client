package de.openrat.client.action;

import javax.security.auth.login.LoginException;

import de.openrat.client.dto.User;
import de.openrat.client.util.CMSConnection;
import de.openrat.client.util.CMSException;
import de.openrat.client.util.CMSResponse;
import de.openrat.client.util.HttpRequest.HttpMethod;

/**
 * This class is NOT threadsafe and should be used by one thread simultaneously.
 * 
 * @author dankert
 */
public class LoginAction extends Action
{

	public LoginAction(CMSConnection connection)
	{
		super(connection, "login");
	}

	/**
	 * Login.
	 * 
	 * @param username
	 *            username
	 * @param password
	 *            password
	 * @param databaseId
	 *            database-id
	 * @return {@link User}
	 * @throws LoginException
	 *             if credentials are wrong
	 */
	public User login(String username, String password, String databaseId) throws LoginException
	{

		execute("login", HttpMethod.GET);

		clear();
		setParameter("login_name", username);
		setParameter("login_password", password);
		setParameter("dbid", databaseId);
		try
		{
			CMSResponse response = execute("login", HttpMethod.POST);

			User user = new User();
			return user;
		}
		catch (CMSException e)
		{
			if ("LOGIN_FAILED".equals(e.getStatus()))
				// wrong credentials - throw checked exception
				throw new LoginException(e.getLocalizedMessage());
			else
				// otherwise it's a technical exception
				throw e;
		}

	}

	public void logout()
	{

		execute("logout", HttpMethod.POST);
	}
}