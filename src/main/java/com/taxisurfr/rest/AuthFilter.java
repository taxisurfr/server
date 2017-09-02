package com.taxisurfr.rest;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.taxisurfr.manager.RouteManager;
import com.taxisurfr.util.TaxisurfrSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.plugins.server.servlet.ServletSecurityContext;
import org.joda.time.DateTime;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Principal;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Logger;

//@WebFilter(filterName = "AuthFilter", urlPatterns = { "/rest/admin/*" })
@Provider
@Authenicate
public class AuthFilter implements ContainerRequestFilter {

    @Inject Logger logger;

    private static final String AUTH_ERROR_MSG =
        "Please make sure your request has an Authorization header",
        EXPIRE_ERROR_MSG = "Token has expired",
        JWT_ERROR_MSG = "Unable to parse JWT",
        JWT_INVALID_MSG = "Invalid JWT token";

    //    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    //    @Override
    public void destroy() {

    }

    //    @Override
    public void doFilter(ServletRequest request, ServletResponse response,

        FilterChain chain) throws IOException, ServletException {

        ContainerRequestContext containerRequestContext = null;
        containerRequestContext.setSecurityContext(new TaxisurfrSecurityContext(""));

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if ("OPTIONS".equals(httpRequest.getMethod())) {
            chain.doFilter(request, response);
            return;
        }
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String authHeader = httpRequest.getHeader(AuthUtils.AUTH_HEADER_KEY);
        String email = null;
        logger.info("authHeader:" + authHeader);
        if (StringUtils.isBlank(authHeader) || authHeader.split(" ").length != 2) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, AUTH_ERROR_MSG);
        } else {
            JWTClaimsSet claimSet = null;
            try {
                email = GoogleVerifier.getEmail(authHeader.split(" ")[1]);
                containerRequestContext.setSecurityContext(new TaxisurfrSecurityContext(email));

                //                securityContext.getAuthenticationScheme();

                //                claimSet = (JWTClaimsSet) AuthUtils.decodeToken(authHeader);
                //            } catch (ParseException e) {
                //                httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, JWT_ERROR_MSG);
                //                return;
            } catch (GeneralSecurityException e) {
                httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, JWT_INVALID_MSG);
                return;
            }

            chain.doFilter(request, response);
        }
    }

    @Override public void filter(ContainerRequestContext containerRequestContext)
        throws IOException {
        try {
            MultivaluedMap<String, String> headers = containerRequestContext.getHeaders();
            String token = headers.get(AuthUtils.AUTH_HEADER_KEY).get(0).split(" ")[1];
            String email = GoogleVerifier.getEmail(token);
            containerRequestContext.setSecurityContext(new TaxisurfrSecurityContext(email));

        } catch (GeneralSecurityException e) {
            logger.severe("no login");
        }

    }
}
