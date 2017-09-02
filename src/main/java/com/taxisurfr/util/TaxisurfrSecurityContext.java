package com.taxisurfr.util;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

public class TaxisurfrSecurityContext implements SecurityContext {

    private Principal principal;
    public TaxisurfrSecurityContext(String email){
        principal = new Principal() {
            @Override public String getName() {
                return email;
            }
        };
    }
    @Override public Principal getUserPrincipal() {
        return principal;
    }

    @Override public boolean isUserInRole(String s) {
        return false;
    }

    @Override public boolean isSecure() {
        return principal != null;
    }

    @Override public String getAuthenticationScheme() {
        return null;
    }
}
