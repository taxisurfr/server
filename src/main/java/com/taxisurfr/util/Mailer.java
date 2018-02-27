package com.taxisurfr.util;

import com.taxisurfr.domain.*;
import com.taxisurfr.manager.ProfileManager;
import com.taxisurfr.rest.js.PickupDropoff;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Stateless
public class Mailer {
    @Inject
    Logger logger;

    private Map<String, File> templateMap = new HashMap<>();

    public static final String SHARE = "/share/";
    public static final String AGREE = "agree";
    public static final String REFUSE = "refuse";
    public static final String BOOK = "book";
    public static final String REQUEST = "request";

    public static final String BOOKSHARE = SHARE+BOOK;
    public static final String AGGREESHARE = SHARE+AGREE;
    public static final String REFUSESHARE = SHARE+REFUSE;

    private final String DISPATCHER = "dispatch@taxisurfr.com";
    private final String EMAILIT = "peter24lasagna@emailitin.com";
    private static final String ROUTE = "Route:";
    private static final String DATE = "Date:";
    private static final String FLIGHTNO = "Flight No:";
    private static final String HOTEL = "Hotel:";
    private static final String LANDING_TIME = "Landing Time:";
    private static final String PICKUP_TIME = "Pickup Time:";
    private static final String NAME = "Name:";
    private static final String EMAIL = "Email:";
    private static final String NUM_PAX = "Passengers:";
    private static final String NUM_SURFBOARDS = "Surfboards:";
    private static final String REQS = "Other requirements:";
    private static DateTimeFormatter sdf = DateTimeFormatter.ISO_DATE;

    private static final String TITLE_ERROR = "Error";
    private static final String TITLE_CONFIRMATION = "Confirmation";
    private static final String TITLE_CONTACT_MESSAGE = "Contact Message";
    private static final String TITLE_MISSING_ROUTE = "Missing Route";
    private static final String TITLE_PRICE_CREATED = "Price created";
    private static final String TITLE_SHARE_ANNOUNCEMENT = "Share Announcement";
    private static final String TITLE_SHARE_REQUEST = "Share Request";
    private static final String TITLE_SHARE_REQUEST_AGREED = "Share Request Agreed";
    private static final String TITLE_SHARE_REQUEST_REFUSED = "Share Request Refused";
    private static final String TITLE_DAILY = "Daily";
    @Inject
    private SendGridSender sender;

    @Inject
    ProfileManager profileManager;

    public void sendDaily(String report, Profile profil) {
        sender.send(TITLE_DAILY, "dispatch@taxisurfr.com", report, profil);
    }

    public void sendContactMessage(String contactname, String contactemail, String contactmessage, Profile profile) {
        String message = String.format("name:%s email:%s message:%s",contactname,contactemail,contactmessage );
        sender.sendWithFrom(TITLE_CONTACT_MESSAGE, contactemail, "dispatch@taxisurfr.com",message, profile);
        new SendGridSender().addRecepient(profile,contactemail);
    }

    public void sendMissingRoute(PickupDropoff query, Profile profile) {
        String message = query.pickup + " to " + query.dropoff;
        sender.send(TITLE_MISSING_ROUTE, "dispatch@taxisurfr.com", message, profile);
    }

    public void sendPriceCreated(String pickup, String dropoff) {
        String message = pickup + " to " + dropoff;
        sender.send(TITLE_PRICE_CREATED, "dispatch@taxisurfr.com", message, profileManager.getProfile());
    }

    public void sendShareAnnouncementNotificationToDispatch(Booking booking, Profile profile) {
        String message = booking.getName();
        message += "\n"+booking.getPrice().getStartroute().getName() + " to " + booking.getPrice().getEndroute().getName();
        sender.send(TITLE_SHARE_ANNOUNCEMENT, "dispatch@taxisurfr.com", message, profile);
    }

    static class Pair<S, S1> {
        String s1;
        String s2;

        static Pair of(String s1, String s2) {
            Pair pair = new Pair();
            pair.s1 = s1;
            pair.s2 = s2;
            return pair;

        }
    }

    private File getFile(String path) {
        File file = templateMap.get(path);
        if (file == null) {
            templateMap.put(path, new File(path));
        }
        return templateMap.get(path);
    }

    public static final String SHARE_REQUEST = "template/abay_share_request.html";
    public static final String SHARE_ANNOUNCEMENT_REQUEST = "template/abay_share_announcement_request.html";
    public static final String FEEDBACK_REQUEST = "template/feedbackRequest.html";
    public static final String SHARE_ACCEPTED = "template/abay_share_request_accepted.html";
    public static final String SHARE_REFUSED = "template/abay_share_request_refused.html";
    public static final String SHARE_OFFER_ACCEPTED = "template/abay_share_offer_accepted.html";
    public static final String CONFIRMATION = "template/confirmation.html";

    public void sendShareRequest(Booking sharer, Booking booker, Profile profile, String message) {
        if (booker.getOrderType().equals(OrderType.BOOKING)) {
            String html = "error";
            html = toConfirmationEmailHtml(sharer, SHARE_REQUEST, profile, toPairList(sharer,true));

            html = html.replace("____INSERT___DETAILS___", getSharingDetails(sharer));
            html = html.replace("___SHARE_MESSAGE__", message != null ? message : "");
            html = html.replace("AGREE_SHARE_LINK", profile.getTaxisurfUrlClient() + AGGREESHARE + "/" + sharer.getId());
            html = html.replace("REFUSE_SHARE_LINK", profile.getTaxisurfUrlClient() + REFUSESHARE + "/" + sharer.getId());

            sender.send(TITLE_SHARE_REQUEST, booker.getEmail(), html, profile);
            sender.send(TITLE_SHARE_REQUEST, "dispatch@taxisurfr.com", html, profile);
        }
        if (booker.getOrderType().equals(OrderType.SHARE_ANNOUNCEMENT)) {
            String html = "error";
            html = toConfirmationEmailHtml(sharer, SHARE_ANNOUNCEMENT_REQUEST, profile, toPairListForAnnouncement(sharer));

            html = html.replace("____INSERT___DETAILS___", getSharingDetailsForAnnouncement(sharer));
            html = html.replace("___SHARE_MESSAGE__", message != null ? message : "");

            sender.send(TITLE_SHARE_REQUEST, booker.getEmail(), html, profile);
            sender.send(TITLE_SHARE_REQUEST, "dispatch@taxisurfr.com", html, profile);
        }

    }

    public void sendShareRequestAccepted(BookingStatus bookingStatus, Booking sharer, Profile profile) {
        String html = null;
        switch (bookingStatus) {
            case SHARE_ACCEPTED:
                html = toConfirmationEmailHtml(sharer, SHARE_ACCEPTED, profile,toPairList(sharer.getBooker(),true));

                html = html.replace("____INSERT___DETAILS___", getSharingDetails(sharer.getBooker()));
                html = html.replace("___BOOK_SHARE_LINK___", profile.getTaxisurfUrlClient() + BOOKSHARE + "/" + sharer.getId());

                sender.send(TITLE_SHARE_REQUEST_AGREED, sharer.getEmail(), html, profile);
                sender.send(TITLE_SHARE_REQUEST_AGREED, "dispatch@taxisurfr.com", html, profile);
                break;

            case SHARE_REFUSED:
                html = toConfirmationEmailHtml(sharer, SHARE_REFUSED, profile,toPairList(sharer,  true));
                sender.send(TITLE_SHARE_REQUEST_REFUSED, sharer.getEmail(), html, profile);
                sender.send(TITLE_SHARE_REQUEST_REFUSED, "dispatch@taxisurfr.com", html, profile);
                break;
            case SHARE_OFFER_ACCEPTED:
                //FIXME
                break;
        }
    }

    private String getSharingDetails(Booking booking) {
        String details = booking.getPrice().getStartroute().getName() + " to " + booking.getPrice().getEndroute().getName() + "\n";
        details += booking.getDateText();
        details += booking.getLandingTime();
        return details;

    }
    private String getSharingDetailsForAnnouncement(Booking booking) {
        String details = booking.getPrice().getStartroute().getName() + " to " + booking.getPrice().getEndroute().getName() + "\n";
        details += booking.getDateText();
        details += booking.getName();
        details += booking.getEmail();
        return details;

    }
    public void sendConfirmation(Booking booking, Profile profile, Agent agent, Contractor contractor,boolean share) {
        String html = "error";
        html = toConfirmationEmailHtml(booking, CONFIRMATION, profile,toPairList(booking,true));
        String confirmationString = profile.getTaxisurfUrl().contains("localhost")?"TEST Confirmation":"Booking Confirmation";
        html = html.replace("__CONFIMATION__", confirmationString);

        String email = booking.getEmail();
        sender.send(TITLE_CONFIRMATION, booking.getEmail(), html, profile);
        sender.send(TITLE_CONFIRMATION, "dispatch@taxisurfr.com", html, profile);
        sender.send(TITLE_CONFIRMATION, contractor.getEmail(), html, profile);
        sender.send(TITLE_CONFIRMATION, EMAILIT, html, profile);
    }

    public String displayErrorForWeb(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        String stackTrace = sw.toString();
        return stackTrace.replace(System.getProperty("line.separator"), "<br/>\n");
    }

    public void sendError(Throwable t, Profile profile) {
        String email = displayErrorForWeb(t);
        sender.send(TITLE_ERROR, "dispatch@taxisurfr.com", displayErrorForWeb(t), profile);
    }

    private static String FACEBOOK_PAGE = "https://www.facebook.com/taxisurfr";

    public String toConfirmationEmailHtml(Booking booking, String path, Profile profile,List<Pair<String, String>> pairs) {
        try {
            String html = getTemplate(path);

            String insertion = "";
            for (Pair<String, String> pair : pairs) {
/*
            for (Pair<String, String> pair : toPairList(booking, route,sharing)) {
*/
                insertion += pair.s1 + " " + pair.s2 + "<br>";
            }
            html = html.replace("____INSERT___DETAILS___", insertion);

            String bookingLink = profile.getTaxisurfUrl()+"/rest/api/form?id=" + booking.getId();
            html = html.replace("___BOOKING_LINK___", bookingLink);

            String taxisurfrRouteLink = profile.getTaxisurfUrl() + "/"+booking.getPrice().getLink();
            taxisurfrRouteLink = FACEBOOK_PAGE;

            html = html.replace("__TAXISURFR_ROUTE_LINK__", taxisurfrRouteLink);
            if (booking.getShareWanted() != null && booking.getShareWanted()) {
                html = html.replace("___SHARE_MESSAGE__", "Spread the word about your shared taxi using this share link.");

            } else {
                html = html.replace("___SHARE_MESSAGE__", "");
            }

            return html;
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
            ex.printStackTrace();
        }

        return null;
    }

    public static List<Pair<String, String>> toPairList(Booking booking, boolean sharing) {
        List<Pair<String, String>> list = new ArrayList<Pair<String, String>>();
        PickupType pickupType = booking.getPrice().getStartroute().getName().equals("COLOMBO AIRPORT") ? PickupType.AIRPORT : PickupType.HOTEL;

        Location start = booking.getPrice().getStartroute();
        Location end = booking.getPrice().getEndroute();
        list.add(Pair.of(ROUTE, start.getName() + " to " + end.getName()));
        list.add(Pair.of(DATE, booking.getDateText()));
        list.add(Pair.of(pickupType.getLocationType(), booking.getFlightNo()));
        list.add(Pair.of(pickupType.getTimeType(), booking.getLandingTime()));
        if (!sharing)list.add(Pair.of(NAME, booking.getName()));
        if (!sharing)list.add(Pair.of(EMAIL, booking.getEmail()));
        list.add(Pair.of(NUM_SURFBOARDS, Integer.toString(booking.getSurfboards())));
        list.add(Pair.of(NUM_PAX, Integer.toString(booking.getPax())));
        if (!sharing)list.add(Pair.of(REQS, booking.getRequirements()));

        return list;
    }

    public static List<Pair<String, String>> toPairListSharing(Booking booking, Route route) {
        List<Pair<String, String>> list = new ArrayList<Pair<String, String>>();

        list.add(Pair.of(ROUTE, route.getStartroute() + " to " + route.getEndroute()));
        list.add(Pair.of(DATE, booking.getDateText()));
        PickupType pickupType = route.getPickupType();
        list.add(Pair.of(pickupType.getLocationType(), booking.getFlightNo()));
        list.add(Pair.of(pickupType.getTimeType(), booking.getLandingTime()));
        list.add(Pair.of(NUM_SURFBOARDS, Integer.toString(booking.getSurfboards())));
        list.add(Pair.of(NUM_PAX, Integer.toString(booking.getPax())));

        return list;
    }

    public static List<Pair<String, String>> toPairListForAnnouncement(Booking booking) {
        List<Pair<String, String>> list = new ArrayList<Pair<String, String>>();

        list.add(Pair.of(ROUTE, booking.getPrice().getStartroute().getName() + " to " + booking.getPrice().getEndroute().getName()));
        list.add(Pair.of(DATE, booking.getDateText()));
        list.add(Pair.of(NAME, booking.getName()));
        list.add(Pair.of(EMAIL, booking.getEmail()));
        list.add(Pair.of(REQS, booking.getRequirements()));

        return list;
    }

    public String getTemplate(String path) {
        logger.info("path:" + path);
        InputStream inputStream = getClass().getClassLoader()
            .getResourceAsStream(path);

        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String ls = System.getProperty("line.separator");

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return stringBuilder.toString();

    }

}
