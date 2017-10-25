package com.taxisurfr.rest;

import com.taxisurfr.domain.Agent;
import com.taxisurfr.domain.Route;
import com.taxisurfr.domain.SessionStat;
import com.taxisurfr.manager.*;
import com.taxisurfr.rest.js.*;
import com.taxisurfr.util.Mailer;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.logging.Logger;

@Path("/admin")
@Stateless
@Produces(MediaType.APPLICATION_JSON)
//@Consumes("application/json")
public class AdminEndpoint {

    private static final String ADMIN = "dispatch@taxisurfr.com";

    @Inject
    private BookingManager bookingManager;

    @Inject
    FinanceManager financeManager;
    @Inject
    RouteManager routeManager;
    @Inject
    AgentManager agentManager;

    @Inject
    ContractorManager contractorManager;

    @Inject
    StatManager statManager;

    @Inject
    StripePayment stripePayment;

    @Inject
    ProfileManager profileManager;

    @Inject
    Logger logger;

    @Inject
    Mailer mailer;

    @GET
    @Path("/films")
    @Authenicate
    public String films(@Context SecurityContext sc) throws IllegalArgumentException {
        String name = sc.getUserPrincipal().getName();
        logger.info(name);
        String result = "{\n"
                + "  \"title\": \"The Basics - Networking\",\n"
                + "  \"description\": \"Your app fetched this from a remote endpoint!\",\n"
                + "  \"movies\": [\n"
                + "    { \"title\": \"Star Wars\", \"releaseYear\": \"19\"},\n"
                + "    { \"title\": \"Back to the Future\", \"releaseYear\": \"1985\"},\n"
                + "    { \"title\": \"The Matrix\", \"releaseYear\": \"1999\"},\n"
                + "    { \"title\": \"Inception\", \"releaseYear\": \"2010\"},\n"
                + "    { \"title\": \"Inception\", \"releaseYear\": \"2010\"},\n"
                + "    { \"title\": \"Inception\", \"releaseYear\": \"2010\"},\n"
                + "    { \"title\": \"xxxxxxxxxxx\", \"releaseYear\": \"2010\"},\n"
                + "    { \"title\": \"Inception\", \"releaseYear\": \"2010\"},\n"
                + "    { \"title\": \"Interstellar\", \"releaseYear\": \"2014\"}\n"
                + "  ]\n"
                + "}";
        return result;
    }

    @GET
    @Path("/films_no")
    public String films_no() throws IllegalArgumentException {
        logger.info("");
        String result = "{\n"
                + "  \"title\": \"The new version\",\n"
                + "  \"description\": \"Your app fetched this from a remote endpoint!\",\n"
                + "  \"movies\": [\n"
                + "    { \"title\": \"Star Wars\", \"releaseYear\": \"1977\"},\n"
                + "    { \"title\": \"Back to the Future\", \"releaseYear\": \"1985\"},\n"
                + "    { \"title\": \"The Matrix\", \"releaseYear\": \"1999\"},\n"
                + "    { \"title\": \"Inception\", \"releaseYear\": \"2010\"},\n"
                + "    { \"title\": \"Inception\", \"releaseYear\": \"2010\"},\n"
                + "    { \"title\": \"Inception\", \"releaseYear\": \"2010\"},\n"
                + "    { \"title\": \"xxxxxxxxxxx\", \"releaseYear\": \"2010\"},\n"
                + "    { \"title\": \"Inception\", \"releaseYear\": \"2010\"},\n"
                + "    { \"title\": \"Interstellar\", \"releaseYear\": \"2014\"}\n"
                + "  ]\n"
                + "}";
        return result;
    }

    @POST
    @Path("/finance")
    @Authenicate
    public FinanceModel finance(@Context SecurityContext sc) throws IllegalArgumentException {
        logger.info("");
        String email = sc.getUserPrincipal() != null ? sc.getUserPrincipal().getName() : null;
        email = peek(email);
        boolean admin = isAdmin(email);
        if (email != null) {
            FinanceModel financeModel =financeManager.getFinances(email,admin);
            return financeModel;
        } else {
            return null;
        }
    }

    private String peek(String contractorEmail){
        if ("taxisurfrcontractor@gmail.com".equals(contractorEmail)){
            return "newsanjumobile@gmail.com";
        }
        return contractorEmail;
    }

    @POST
    @Path("/createTransfer")
    @Authenicate
    public FinanceModel createTransfer(@Context SecurityContext sc, TransferJS transferJS) throws IllegalArgumentException {
        logger.info("");
        String email = sc.getUserPrincipal() != null ? sc.getUserPrincipal().getName() : null;
        boolean admin = isAdmin(email);
        if (email != null) {
            Agent agent = agentManager.find(transferJS.agentId);
            financeManager.saveTransfer(agent.getEmail(),admin,transferJS.cents,transferJS.description);
            FinanceModel financeModel =financeManager.getFinances(email,admin);
            return financeModel;
        } else {
            return null;
        }
    }


    @POST
    @Path("/booking")
    @Authenicate
    public BookingModel booking(@Context SecurityContext sc) throws IllegalArgumentException {
        logger.info("");
        String email = sc.getUserPrincipal() != null ? sc.getUserPrincipal().getName() : null;
        email = peek(email);
        return getBookingModel(email);
    }

    @POST
    @Path("/routes")
    @Authenicate
    public RoutesModel routes(@Context SecurityContext sc) throws IllegalArgumentException {
        logger.info("");
        String email = sc.getUserPrincipal() != null ? sc.getUserPrincipal().getName() : null;
        email = peek(email);
        RoutesModel routesModel = getRoutesModel(email);
        return routesModel;
    }

    private boolean isAdmin(String email){
        return  ADMIN.equals(email);
    }

    private BookingModel getBookingModel(String email) {
        BookingModel bookingModel=null;
        boolean admin = isAdmin(email);
        if (email != null) {
            Agent agent = !admin ? agentManager.getAgent(email) : null;
            bookingModel = bookingManager.getBookings(agent,admin);
        }
        return bookingModel;
    }

    private RoutesModel getRoutesModel(String email) {
        RoutesModel routesModel=null;
        boolean admin = isAdmin(email);
        if (email != null) {
            Agent agent = !admin ? agentManager.getAgent(email) : null;
            routesModel = routeManager.getRoutes(agent, admin);
        }
        return routesModel;
    }

    @POST
    @Path("/cancelBooking")
    @Consumes("application/json")
    @Authenicate
    public BookingModel cancelBooking(@Context SecurityContext sc, CancelBookingJS cancelBookingJS) throws
            IllegalArgumentException {

        FinanceModel financeModel = null;
        logger.info("");
        String email = sc.getUserPrincipal() != null ? sc.getUserPrincipal().getName() : null;
        if (email != null) {
            financeModel = financeManager.cancelBooking(email, cancelBookingJS.bookingId,isAdmin(email));
            bookingManager.cancelBooking(cancelBookingJS.bookingId);
        }
        return getBookingModel(email);
    }

    @POST
    @Path("/editRoute")
    @Consumes("application/json")
    @Authenicate
    public RoutesModel editRoute(@Context SecurityContext sc, EditRouteJS editBookingJS) throws
            IllegalArgumentException {

        FinanceModel financeModel = null;
        logger.info("");
        String email = sc.getUserPrincipal() != null ? sc.getUserPrincipal().getName() : null;
        if (email != null) {
            routeManager.editRoute(editBookingJS.id,editBookingJS.cents);
        }
        return getRoutesModel(email);
    }
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/routes")
    public List<Route> getRoutes() throws IllegalArgumentException {
        return routeManager.getRoutes();
    }

    @GET
    @Path("/generate")
    public void generate() throws IllegalArgumentException {
        routeManager.createLinkInDescription();
    }

    @POST
    @Path("/session.get")
    public NewSessionResultJS addSession(NewSessionJS session) throws IllegalArgumentException {
        logger.info("session.new" + session.start + " to " + session.end);
        SessionStat sessionStat = statManager.addSession(session);

        NewSessionResultJS newSessionResultJS = new NewSessionResultJS();
        newSessionResultJS.stripePublishable = profileManager.getProfile().getStripePublishable();

        //http://localhost:3000/unawatuna#/form/transport
        //http://localhost:3000/#/form/transport
        newSessionResultJS.route = routeManager.getRouteFromLink(getLink(session.url));

        return newSessionResultJS;
    }

    String getLink(String link) {
        try {
            link = link.split("#")[0];
            link = link.split("//")[1];
            link = link.split("/")[1];
        } catch (Exception ex) {
            return null;
        }
        return link;

    }

    @POST
    @Path("/missingroute")
    public Route missingRoute(Query query) throws IllegalArgumentException {
        logger.severe(" missing start:" + query.start + "end:" + query.end);
        return null;
    }
}
