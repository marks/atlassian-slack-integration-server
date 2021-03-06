package com.atlassian.plugins.slack.admin;

import com.atlassian.sal.api.user.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AbstractPermissionCheckingServlet extends HttpServlet {
    private final Logger LOGGER = LoggerFactory.getLogger(AbstractPermissionCheckingServlet.class);

    protected final UserManager userManager;
    protected final LoginRedirectionManager loginRedirectionManager;

    public AbstractPermissionCheckingServlet(final UserManager userManager,
                                             final LoginRedirectionManager loginRedirectionManager) {
        this.loginRedirectionManager = loginRedirectionManager;
        this.userManager = userManager;
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        if (checkAccess(request)) {
            super.service(request, response);
        } else {
            onPermissionError(request, response);
            LOGGER.debug("User does not have permission to access this page, redirecting to login page.");
            loginRedirectionManager.redirectToLogin(request, response);
        }
    }

    protected abstract void onPermissionError(HttpServletRequest request, HttpServletResponse response);

    protected abstract boolean checkAccess(HttpServletRequest request);
}
