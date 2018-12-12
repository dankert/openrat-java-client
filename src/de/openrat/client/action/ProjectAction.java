package de.openrat.client.action;

import de.openrat.client.dto.Project;
import de.openrat.client.util.CMSConnection;
import de.openrat.client.util.CMSResponse;
import de.openrat.client.util.Id;

/**
 * This class is NOT threadsafe and should be used by one thread simultaneously.
 * 
 * @author dankert
 */
public class ProjectAction extends Action
{
	private final static String PROJECT = "project";

	public ProjectAction(CMSConnection connection)
	{
		super(connection);
	}

	public Project getInfo(Id id)
	{

		CMSResponse response = executeView(PROJECT, "info");
		// TODO

		return new Project();
	}

}
