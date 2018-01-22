package com.taxisurfr.rest;

import com.redfin.sitemapgenerator.WebSitemapGenerator;
import com.taxisurfr.domain.*;
import com.taxisurfr.manager.*;
import com.taxisurfr.rest.js.*;
import com.taxisurfr.util.Mailer;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@Path("/api")
@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaxisurfrImpl {

    @Inject
    private BookingManager bookingManager;

    @Inject
    private PricesManager pricesManager;

    @Inject
    RouteManager routeManager;
    @Inject
    AgentManager agentManager;

    @Inject
    ContractorManager contractorManager;

    @Inject
    StatManager statManager;

    @Inject
    FinanceManager financeManager;

    @Inject
    Mailer mailer;

    @Inject
    ProfileManager profileManager;

    @Inject
    Logger logger;

    @GET
    @Path("/daily")
    public void daily() throws IllegalArgumentException {
        logger.info("daily");
        statManager.sendDaily(profileManager.getProfile());
    }

    @POST
    @Path("/session")
    public NewSessionResultJS newSession(SessionJS session) throws IllegalArgumentException {

        SessionStat sessionStat = statManager.addSession();
        sessionStat.setReferer(session.hostname);
        sessionStat.setRoute("" + session.routeId);
        sessionStat.setSrc("announcement:" + session.shareAnnouncement);

        return new NewSessionResultJS();
    }

    String[] getLink(String url) {
        String link = null;
        String country = null;
        try {
            String s = url.split("#")[0];
            s = s.split("params=")[1];
            String[] link_country = s.split("--");
            link = link_country[0];
            country = link_country.length > 1 ? link_country[1] : null;
        } catch (Exception ex) {
        }
        return new String[]{link, country};
    }


    @POST
    @Path("/routefromlink")
    public RouteAndSharingsJS getRouteFromLink(Query query) throws IllegalArgumentException {
        RouteAndSharingsJS routeAndSharingsJS = new RouteAndSharingsJS();
        routeAndSharingsJS.route = routeManager.getRouteFromLink(query.link);
        routeAndSharingsJS.prices = pricesManager.getPrices(routeAndSharingsJS.route);
        routeAndSharingsJS.sharingList = bookingManager.getSharingsForRoute(routeAndSharingsJS.route);
        routeAndSharingsJS.stripeKey = profileManager.getProfile().getStripePublishable();
        routeAndSharingsJS.showNoRouteMessage=false;
        return routeAndSharingsJS;
    }

    @POST
    @Path("/route")
    public Route getRoute(Query query) throws IllegalArgumentException {
        logger.info("start:" + query.start + "end:" + query.end);
        Route route = routeManager.getRoute(query.start, query.end);
        logger.info("route:" + route);
        return route;
    }

    @POST
    @Path("/routeandsharings")
    public RouteAndSharingsJS getRouteAndBookings(@Context HttpHeaders headers, PickupDropoff query) throws IllegalArgumentException {
        logger.info("pickup:" + query.pickup + "dropoff:" + query.dropoff);
        RouteAndSharingsJS routeAndSharingsJS = new RouteAndSharingsJS();
        routeAndSharingsJS.route = routeManager.getRoute(query.pickup, query.dropoff);
        routeAndSharingsJS.prices = pricesManager.getPrices(routeAndSharingsJS.route);
        routeAndSharingsJS.sharingList = bookingManager.getSharingsForRoute(routeAndSharingsJS.route);
        routeAndSharingsJS.stripeKey = profileManager.getProfile().getStripePublishable();
        routeAndSharingsJS.showNoRouteMessage=true;

        logger.info("route:" + routeAndSharingsJS.route);
        if (routeAndSharingsJS.route == null) {
            mailer.sendMissingRoute(query, profileManager.getProfile());
        }
/*
        createSessionStat(headers,"routeandsharings"+query.pickup+"_"+query.dropoff);
*/
        return routeAndSharingsJS;
    }

    @POST
    @Path("/sessiononserver")
    public SessionJS getRouteAndBookingsSession(@Context HttpHeaders headers, PickupDropoff query) throws IllegalArgumentException {
        SessionJS sessionJS = new SessionJS();
        if ("base".equals(query.link)) {
            sessionJS.country = createSessionStat(headers, query.src, "base");
        } else {
            if (query.link != null) {
                Route routeFromLink = routeManager.getRouteFromLink(query.link);
                if (routeFromLink != null) {
                    createSessionStat(headers, query.src, "routeandsharings" + routeFromLink.getStartroute() + "_" + routeFromLink.getEndroute());
                }
            } else if (query.pickup != null) {
                createSessionStat(headers, query.src, "routeandsharings" + query.pickup + "_" + query.dropoff);
            }
        }
        return sessionJS;
    }


    @POST
    @Path("/createcontact")
    public Boolean createContactMessage(ContactMessageJS contact) throws IllegalArgumentException {
        mailer.sendContactMessage(contact.contactname, contact.contactemail, contact.contactmessage, profileManager.getProfile());
        return true;
    }

    @GET
    @Path("/lka_/{link}")
    public Route getResortForServer(@PathParam("link") String link) throws IllegalArgumentException {
        return routeManager.getRouteFromLink(link);
    }

    @GET
    @Path("/lka/{link}")
    public Route getResortForClient(@PathParam("link") String link) throws IllegalArgumentException {
        return routeManager.getRouteFromLink(link);
    }

    @POST
    @Path("/resort")
    public RouteAndSharingsJS getResortSharings(@Context HttpHeaders headers, RouteLinkJS routeLinkJS) throws IllegalArgumentException {
        RouteAndSharingsJS routeAndSharingsJS = new RouteAndSharingsJS();
        routeAndSharingsJS.route = routeManager.getRouteFromLink(routeLinkJS.link);
        routeAndSharingsJS.prices = pricesManager.getPrices(routeAndSharingsJS.route);
        routeAndSharingsJS.sharingList = bookingManager.getSharingsForRoute(routeAndSharingsJS.route);
        routeAndSharingsJS.stripeKey = profileManager.getProfile().getStripePublishable();
        logger.info("route:" + routeAndSharingsJS.route);
/*
        createSessionStat(headers,"resort:"+routeAndSharingsJS.route.startroute+"_"+routeAndSharingsJS.route.endroute);
*/
        return routeAndSharingsJS;
    }

    @POST
    @Path("/doshare")
    public DoShareResponseJS doShareCmd(DoShareJS routeLinkJS) throws IllegalArgumentException {
        DoShareResponseJS doShareResponse = new DoShareResponseJS();
        BookingStatus bookingStatus = null;
        doShareResponse.booking = bookingManager.shareRequestResponse(routeLinkJS.cmd, routeLinkJS.id);
        doShareResponse.stripeKey = profileManager.getProfile().getStripePublishable();
        return doShareResponse;
    }

    @POST
    @Path("/requestshare")
    public ShareJS requestShareOfExistingBooking(ShareRequest shareRequest) throws IllegalArgumentException {
        logger.info("share request for bookingId:" + shareRequest.bookingId);
        return bookingManager.share(shareRequest);
    }

    @POST
    @Path("/missingroute")
    public Route missingRoute(Query query) throws IllegalArgumentException {
        logger.severe(" missing start:" + query.start + "end:" + query.end);
        return null;
    }

    @POST
    @Path("/newbooking")
    public Booking addBooking(@Context HttpHeaders headers, NewBookingJS booking) throws IllegalArgumentException {
        logger.info("addBooking");
        Route route = routeManager.find(booking.routeId);
        Contractor contractor = contractorManager.find(route.getContractorId());
        Agent agent = agentManager.find(contractor.getAgentId());
        Price price = pricesManager.find(booking.priceId);
        createSessionStat(headers, "", "newbooking:" + route.startroute + "_" + route.endroute);
        return bookingManager.createBooking(booking, price,agent, contractor);
    }

    @POST
    @Path("/payment")
    public PaymentResultJS payment(StripePaymentJS stripePaymentJS)
            throws IllegalArgumentException {
        logger.info("payment");
        Booking booking = bookingManager.find(stripePaymentJS.bookingId);
        String paymentError = financeManager.payment(stripePaymentJS.token, booking, stripePaymentJS.share);
        PaymentResultJS paymentResultJS = new PaymentResultJS();
        paymentResultJS.ok = paymentError == null;
        paymentResultJS.error = paymentError;
        return paymentResultJS;
    }

    private Long ipAsNumeric(String ipAsString) {
        String[] segments = ipAsString.split("\\.");
        return (long) (Long.parseLong(segments[0]) * 16777216L
                + Long.parseLong(segments[1]) * 65536L
                + Long.parseLong(segments[2]) * 256L +
                Long.parseLong(segments[3]));
    }

    public String createSessionStat(HttpHeaders headers, String src, String pickupdropoff) {
        logger.info("createSessionStat" + LocalDateTime.now());
        String userAgent = headers.getRequestHeader("user-agent").get(0);
        for (String agent : new String[]{"Chrome", "Firefox", "iPhone", "iPad"}) {
            if (userAgent != null && userAgent.contains(agent)) {
                userAgent = agent;
                break;
            }
        }
        List<String> requestHeader = headers.getRequestHeader("X-Forwarded-For");
        String ipaddress = requestHeader.size() > 0 ? requestHeader.get(0) : null;

        Long inet_aton = ipaddress != null ? ipAsNumeric(ipaddress) : -1L;
        //String country = statManager.newSession(userAgent, inet_aton, pickupdropoff, src);
        logger.info("createSessionStat" + LocalDateTime.now());
        return "no country";
    }

    @POST
    @Path("/formx")
    @Produces("application/pdf")
    public FormJS gettest(FormJS formJS) {

        formJS.form = bookingManager.find(1L).getPdf();
        return formJS;
    }

    @GET
    @Path("/form")
    public Response confirmationForm(@Context HttpServletRequest request, @Context
            HttpServletResponse response, @QueryParam("id") Long id) throws IllegalArgumentException {
        logger.info("id:" + id);
        Booking booking = bookingManager.find(id);
        if (booking == null) {
            return null;
        }
        byte[] pdf = bookingManager.find(id).getPdf();
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment;filename=confirmation.pdf");
        response.setContentLength(pdf.length);
        try {
            ServletOutputStream out = response.getOutputStream();
            out.write(pdf);

            out.flush();
            out.close();

        } catch (Exception e) {
            logger.severe(e.getMessage());
        }

        return null;
    }

    @GET
    @Path("/sitemap")
    public Response sitemap(@Context HttpServletRequest request, @Context
            HttpServletResponse response) throws IllegalArgumentException {

        try {
            WebSitemapGenerator wsg = new WebSitemapGenerator("https://taxisurfr.com", new File("."));
            for (Route route : routeManager.getRoutes()) {
                if (route.getPickupType()==PickupType.AIRPORT || route.getPickupType()==PickupType.HOTEL)
                wsg.addUrl("https://taxisurfr.com" + route.getDescription());
            }
            File sitemap = wsg.write().get(0);
            BufferedReader br = new BufferedReader(new FileReader(sitemap));
            String line = null;

            response.setContentType("application/text");
            response.setHeader("Content-Disposition", "attachment;filename=sitemap");
            response.setContentLength((int) sitemap.length());
            try {
                PrintWriter printWriter = new PrintWriter("sitemap.txt");
                ServletOutputStream out = response.getOutputStream();
                while ((line = br.readLine()) != null) {
                    out.write(line.getBytes());
                    System.out.println(line.getBytes());
                    printWriter.println(line.getBytes());
                }

                out.flush();
                out.close();

            } catch (Exception e) {
                logger.severe(e.getMessage());
            }

        } catch (IOException ioexception) {

        }
        return null;
    }

}
