package com.taxisurfr.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.taxisurfr.domain.TaxisurfrUser;
import com.taxisurfr.domain.TaxisurfrUser.Provider;
import com.taxisurfr.domain.Token;
import com.taxisurfr.manager.UserDAO;
//import io.dropwizard.hibernate.UnitOfWork;
//import io.dropwizard.jersey.errors.ErrorMessage;
import java.util.Optional;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import javax.xml.registry.infomodel.User;

import com.taxisurfr.rest.js.LoginJS;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    Client client = ClientBuilder.newClient();

    @Inject
    private UserDAO dao;
    private final ClientSecretsConfiguration secrets;

    public static final String CLIENT_ID_KEY = "client_id", REDIRECT_URI_KEY = "redirect_uri",
        CLIENT_SECRET = "client_secret", CODE_KEY = "code", GRANT_TYPE_KEY = "grant_type",
        AUTH_CODE = "authorization_code";


    public static final String CONFLICT_MSG = "There is already a %s account that belongs to you",
        NOT_FOUND_MSG = "TaxisurfrUser not found", LOGING_ERROR_MSG = "Wrong email and/or password",
        UNLINK_ERROR_MSG = "Could not unlink %s account because it is your only sign-in method";

    public static final ObjectMapper MAPPER = new ObjectMapper();

    public AuthResource() {
        this.secrets = new ClientSecretsConfiguration();
    }

//    @POST
//    @Path("login")
//    //  @UnitOfWork
//    public Response login(@Valid final TaxisurfrUser user, @Context final HttpServletRequest request)
//        throws Exception {
//        final Optional<TaxisurfrUser> foundUser = dao.findByEmail(user.getEmail());
//        if (foundUser.isPresent()
//            && PasswordService.checkPassword(user.getPassword(), foundUser.get().getPassword())) {
//            final Token token =
//                AuthUtils.createToken(request.getRemoteHost(), foundUser.get().getId());
//            return Response.ok().entity(token).build();
//        }
//        return Response.status(Status.UNAUTHORIZED).entity(new ErrorMessage(LOGING_ERROR_MSG))
//            .build();
//    }
//
//    @POST
//    @Path("signup")
////    @UnitOfWork
//    public Response signup(@Valid final TaxisurfrUser user, @Context final HttpServletRequest request)
//        throws Exception {
//        user.setPassword(PasswordService.hashPassword(user.getPassword()));
//        final TaxisurfrUser savedUser = dao.save(user);
//        final Token token = AuthUtils.createToken(request.getRemoteHost(), savedUser.getId());
//        return Response.status(Status.CREATED).entity(token).build();
//    }

    @POST
    @Path("google")
//    @UnitOfWork
    public LoginJS loginGoogle(@Valid final Payload payload,
        @Context final HttpServletRequest request) throws Exception, ParseException,
        JsonParseException, JsonMappingException, IOException {
        final String accessTokenUrl = "https://accounts.google.com/o/oauth2/token";
        final String peopleApiUrl = "https://www.googleapis.com/plus/v1/people/me/openIdConnect";
        Response response;


        // Step 1. Exchange authorization code for access token.
        final MultivaluedMap<String, String> accessData = new MultivaluedHashMap<String, String>();
        accessData.add(CLIENT_ID_KEY, payload.getClientId());
        accessData.add(REDIRECT_URI_KEY, payload.getRedirectUri());
        accessData.add(CLIENT_SECRET, secrets.getGoogle());
        accessData.add(CODE_KEY, payload.getCode());
        accessData.add(GRANT_TYPE_KEY, AUTH_CODE);
//        response = client.target(accessTokenUrl).request().post(Entity.form(accessData));
        WebTarget target = client.target(accessTokenUrl);
        Invocation.Builder request1 = target.request();
        response = request1.post(Entity.form(accessData));
        accessData.clear();

        // Step 2. Retrieve profile information about the current user.
        final String accessToken = (String) getResponseEntity(response).get("access_token");

        response =
            client.target(peopleApiUrl).request("text/plain")
                .header(AuthUtils.AUTH_HEADER_KEY, String.format("Bearer %s", accessToken)).get();
        final Map<String, Object> userInfo = getResponseEntity(response);

        // Step 3. Process the authenticated the user.
        return processUser(request, Provider.GOOGLE, userInfo.get("email").toString(),
            userInfo.get("name").toString());
    }

    public static class Payload {
        @NotEmpty
        String clientId;

        @NotEmpty
        String redirectUri;

        @NotEmpty
        String code;

        @NotEmpty
        String state;

        public String getClientId() {
            return clientId;
        }

        public String getRedirectUri() {
            return redirectUri;
        }

        public String getCode() {
            return code;
        }
        public String getState() {
            return state;
        }
    }

    /*
     * Helper methods
     */
    private Map<String, Object> getResponseEntity(final Response response)
        throws JsonParseException,
        JsonMappingException, IOException {
        return MAPPER.readValue(response.readEntity(String.class),
            new TypeReference<Map<String, Object>>() {
            });
    }

    private LoginJS processUser(final HttpServletRequest request, final Provider provider,
        final String email, final String displayName) throws Exception, ParseException {
        final Optional<TaxisurfrUser> user = dao.findByEmail(email);
        if (user.isPresent()){
            LoginJS loginJS = new LoginJS();
            loginJS.taxisurfrUser = user.get();
            Token token = AuthUtils.createToken(request.getRemoteHost(), loginJS.taxisurfrUser.getId());

            loginJS.token = token.getToken();
            return loginJS;
        }else{
            return null;
        }
    }

    public static class ClientSecretsConfiguration {

        @NotEmpty
        @JsonProperty
        String facebook;

        @NotEmpty
        @JsonProperty
        String google = "6Nn05IlQQK9-Kk2PTyNZHViR";

        @NotEmpty
        @JsonProperty
        String linkedin;

        @NotEmpty
        @JsonProperty
        String github;

        @NotEmpty
        @JsonProperty
        String foursquare;

        @NotEmpty
        @JsonProperty
        String twitter;

        public String getFacebook() {
            return facebook;
        }

        public String getGoogle() {
            return google;
        }

        public String getLinkedin() {
            return linkedin;
        }

        public String getFoursquare() {
            return foursquare;
        }

        public String getTwitter() {
            return twitter;
        }
    }

}
