package de.openrat.client.test;

import de.openrat.client.CMSClient;
import de.openrat.client.util.CMSNode;

import java.io.IOException;

/**
 * this is an example API facade for calling CMS functions.
 */
public class ExampleCMSTestAPI {

	private CMSClient client;

	public ExampleCMSTestAPI(CMSClient client) {
		this.client = client;
	}

	public boolean login( String dbid, String user, String password ) throws IOException {
		client.request("login","login").execute(); // Read Session and Token.

		return client.request("login","login").forWriting().addParameter("dbid",dbid).addParameter("login_name",user).addParameter("login_password",password).execute().getValidationErrors().isEmpty();
	}

	public CMSNode info() throws IOException {
		return client.request("login","license").execute().getOutput();
	}

	// ... add another methods here ...
}