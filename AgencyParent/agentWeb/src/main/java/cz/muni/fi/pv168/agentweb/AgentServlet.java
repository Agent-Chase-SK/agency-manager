/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv168.agentweb;

import cz.muni.fi.pv168.agencymanager.common.ValidationException;
import cz.muni.fi.pv168.agencymanager.entity.Agent;
import cz.muni.fi.pv168.agencymanager.manager.AgentManager;
import cz.muni.fi.pv168.agencymanager.manager.AgentManagerImpl;
import cz.muni.fi.pv168.agencymanager.status.AgentStatus;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jakub
 */
@WebServlet(name = "AgentServlet", urlPatterns = {AgentServlet.URL_MAPPING,AgentServlet.URL_MAPPING+"/*"})
public class AgentServlet extends HttpServlet {
    private static final String LIST_JSP = "/list.jsp";
    public static final String URL_MAPPING = "/AgentServlet";
    
    private final static Logger LOG = LoggerFactory.getLogger(AgentServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        String action = req.getPathInfo();
        LOG.debug("POST ... {}",action);
        Agent agent;
        
        switch (action) {
            case "/create":
                agent = getAgentFromReq(req);
                if (agent.getCodeName() == null || agent.getCodeName().length() == 0 || agent.getStatus() == null) {
                    req.setAttribute("formError", "Fill all fields!");
                    LOG.debug("form data invalid");
                    showAgentsList(req, resp);
                    return;
                }
                try {
                    getAgentManager().createAgent(agent);
                    LOG.debug("redirecting after POST");
                    resp.sendRedirect(req.getContextPath()+URL_MAPPING);
                    return;
                } catch (ValidationException e) {
                    LOG.error("Cannot add agent", e);
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                    return;
                }
            case "/retire":
                try {
                    Long id = Long.valueOf(req.getParameter("id"));
                    agent = getAgentManager().findAgentById(id);
                    agent.setStatus(AgentStatus.RETIRED);
                    getAgentManager().updateAgent(agent);
                    LOG.debug("redirecting after POST");
                    resp.sendRedirect(req.getContextPath()+URL_MAPPING);
                    return;
                } catch (ValidationException ex) {
                    LOG.error("Cannot retire agent", ex);
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
                    return;
                }
            case "/update":
                //TODO
                return;
            default:
                LOG.error("Unknown action " + action);
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown action " + action);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.debug("GET ...");
        showAgentsList(req, resp);
    }

    private void showAgentsList(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try {
            LOG.debug("showing table of agents");
            AgentManager man = getAgentManager();
            List<Agent> list = man.findAllAgents();
            req.setAttribute("agents", list);
            req.getRequestDispatcher(LIST_JSP).forward(req, resp);
        } catch (ValidationException ex) {
            LOG.error("Cannot show agents", ex);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    private AgentManager getAgentManager() {
        return (AgentManager) getServletContext().getAttribute("AgentManager");
    }
    
    private Agent getAgentFromReq(HttpServletRequest req) {
        Agent agent = new Agent();

        if (req.getParameter("id") != null && !req.getParameter("id").isEmpty()) {
            agent.setId(Long.valueOf(req.getParameter("id")));
        } else {
            agent.setId(null);
        }
        
        if (req.getParameter("codeName") != null && !req.getParameter("codeName").isEmpty()) {
            agent.setCodeName(req.getParameter("codeName"));
        } else {
            agent.setCodeName(null);
        }
        
        if (req.getParameter("status") != null && !req.getParameter("status").isEmpty()) {
            agent.setStatus(AgentManagerImpl.toAgentStatus(req.getParameter("status")));
        } else {
            agent.setStatus(null);
        }
        return agent;
    }
}
