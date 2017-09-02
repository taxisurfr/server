package com.taxisurfr.rest;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.joda.time.DateTime;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Locale;
import java.util.logging.Logger;

//@WebFilter(filterName = "LinkFilter", urlPatterns = { "/resort/*" })
public class LinkFilter implements Filter {

    @Inject Logger logger;

    @Override public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
        FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if ("GET".equals(httpRequest.getMethod())) {
            Locale userlocale = request.getLocale();
            String country = userlocale != null ? userlocale.getCountry() : "unknown";

            String resort = httpRequest.getRequestURI().split("/resort/")[1];

            ((HttpServletResponse)response).sendRedirect("../client/index.html?params="+resort+"--"+country);
            return;
        }
        chain.doFilter(request, response);
    }

}
