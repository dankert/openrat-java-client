package de.openrat.client.action;

import de.openrat.client.dto.Project;
import de.openrat.client.util.CMSConnection;
import de.openrat.client.util.CMSResponse;
import de.openrat.client.util.HttpRequest.HttpMethod;
import de.openrat.client.util.Id;

/**
 * This class is NOT threadsafe and should be used by one thread simultaneously.
 * 
 * @author dankert
 */
public class ProjectAction extends Action
{

	public ProjectAction(CMSConnection request)
	{
		super(request, "project");
	}

	public Project getInfo(Id id)
	{

		CMSResponse response = execute("info", HttpMethod.GET);
		// TODO

		return new Project();
	}

}
