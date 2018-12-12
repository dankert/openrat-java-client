package de.openrat.client.action;

import de.openrat.client.util.CMSConnection;
import de.openrat.client.util.CMSException;
import de.openrat.client.util.CMSNode;
import de.openrat.client.util.CMSResponse;
import de.openrat.client.util.HttpRequest.HttpMethod;

import javax.security.auth.login.LoginException;
import java.util.Map;

/**
 * This class is NOT threadsafe and should be used by one thread simultaneously.
 *
 * @author dankert
 */
public class CMSAction extends Action {

    public CMSAction(CMSConnection connection) {

        super(connection);
    }

    /**
     * Custom method call.
     *
     * @return CMSNode
     */
    public CMSNode executeView(String action, String method, Map<String, String> parameter) {

        return execute(action,method,parameter,HttpMethod.GET);
    }
    /**
     * Custom method call.
     *
     * @return CMSNode
     */
    public CMSNode executePost(String action, String method, Map<String, String> parameter) {

        return execute(action,method,parameter,HttpMethod.POST);
    }


    /**
     * Custom method call.
     *
     * @return CMSNode
     */
    private CMSNode execute(String action, String method, Map<String, String> parameter,HttpMethod httpMethod) {

        clear();

        for (Map.Entry<String, String> entry : parameter.entrySet())
            setParameter(entry.getKey(), entry.getValue());

        try {
            CMSResponse response;
            if( httpMethod.equals(HttpMethod.GET))
                response = executeView(action, method);
            else
                response = executePost(action, method);
            return response.getOutput();
        } catch (CMSException e) {
            throw e;
        }
    }
}
