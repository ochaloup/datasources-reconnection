package org.jboss.qa.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;
import org.jboss.qa.cdi.CDIBean;


@WebServlet(name="CDITestServlet", urlPatterns={"/cdi"})
public class CDITestServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private  static final Logger LOGGER = Logger.getLogger(CDITestServlet.class);
    
    @Inject
    CDIBean bean;
        
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        if(request.getParameter("notx") != null) {
            LOGGER.info("Going 'goWithoutTxn'");
            bean.goWithoutTxn();
        } else {
            LOGGER.info("Going 'goWithMe'");
            bean.goWithMe();
        }
        out.println("OK");
    }
}

