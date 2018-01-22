package com.taxisurfr.manager;

import com.taxisurfr.domain.*;
import com.taxisurfr.rest.js.NewBookingJS;
import com.taxisurfr.rest.js.RouteAndSharingsJS;
import com.taxisurfr.rest.js.ShareJS;
import com.taxisurfr.rest.js.ShareRequest;
import com.taxisurfr.util.Mailer;
import com.taxisurfr.util.SendGridSender;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Column;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.taxisurfr.domain.BookingStatus.SHARE_ACCEPTED;
import static com.taxisurfr.domain.BookingStatus.SHARE_OFFER_ACCEPTED;
import static com.taxisurfr.domain.BookingStatus.SHARE_REFUSED;
import static com.taxisurfr.util.Mailer.*;

@Stateless
public class BookingManager extends AbstractDao<Booking> {
    private static final Logger logger = Logger.getLogger(BookingManager.class.getName());
    //    static final DateTimeFormatter fmt = DateTimeFormat.forPattern("dd.MM.yyyy");

    public BookingManager() {
        super(Booking.class);
        ;
    }

    @Inject
    Mailer mailer;
    @Inject
    ProfileManager profileManager;
    @Inject
    RouteManager routeManager;
    @Inject
    private SendGridSender sender;

    public Booking createBooking(NewBookingJS newBooking, Price price, Agent agent, Contractor contractor) {
        OrderType orderType = newBooking.announceShare ? OrderType.SHARE_ANNOUNCEMENT : OrderType.BOOKING;
        Booking booking = new Booking();
        Route route = routeManager.find(newBooking.routeId);
        booking.setRoute(route);
        booking.setPrice(price);
        booking.setAgent(agent.getId());
        booking.setContractor(contractor.getId());
        booking.setDate(new Timestamp(newBooking.date.getTime()));
        booking.setName(newBooking.name);
        booking.setEmail(newBooking.email);
        sender.addRecepient(profileManager.getProfile(), newBooking.email);
        booking.setDateText(newBooking.dateText);
        booking.setFlightNo(newBooking.flightNo);
        booking.setLandingTime(newBooking.landingTime);
        booking.setPax(newBooking.pax);
        booking.setSurfboards(newBooking.surfboards);
        booking.setRequirements(newBooking.requirements);
        booking.setOrderType(orderType);
        booking.setStatus(BookingStatus.BOOKED);
        booking.setShareWanted(newBooking.shareWanted);
        persist(booking);
        if (orderType==OrderType.SHARE_ANNOUNCEMENT){
            mailer.sendShareAnnouncementNotificationToDispatch(booking,profileManager.getProfile());
        }
        return booking;
    }

    @Column
    private Currency currency = Currency.USD;

    public List<RouteAndSharingsJS.Share> getSharingsForRoute(Route route) {
        List<RouteAndSharingsJS.Share> shares = new ArrayList<>();

        Route routeAlternative = null;

        if (route != null) {
            if (route.getId()==104){
                routeAlternative = routeManager.find(112L);
            }
            List<Booking> resultList = getEntityManager().createNamedQuery("Booking.getByRoute")
                    .setParameter("route", route)
                    .setParameter("routeAlternative", routeAlternative)
                    .setParameter("orderType1", OrderType.BOOKING)
                    .setParameter("orderType2", OrderType.SHARE_ANNOUNCEMENT)
                    .getResultList();
            for (Booking booker : resultList) {
                if (booker.getDate().toLocalDateTime().isAfter(LocalDateTime.now())) {
                    if (
                            (
                                    booker.getStatus().equals(BookingStatus.PAID) && booker.getOrderType().equals(OrderType.BOOKING))
                                    ||
                                    booker.getOrderType().equals(OrderType.SHARE_ANNOUNCEMENT)
                            ) {
                        RouteAndSharingsJS.Share share = new RouteAndSharingsJS.Share();
                        share.bookingId = booker.getId();
                        share.dateText = booker.getDateText();
                        share.date = LocalDate.now();
                        share.time = booker.getLandingTime();
                        share.amountToShare = booker.getSharePrice();
                        share.orderType = booker.getOrderType().ordinal();
                        shares.add(share);
                    }
                }
            }
        }
        return shares;
    }

    public BookingModel getBookings(Agent agent, boolean admin) {
        BookingModel bookingModel = new BookingModel();
        bookingModel.admin = admin;
        try {

            bookingModel.bookingList = !admin ?
                    getEntityManager().createNamedQuery("Booking.getByAgent")
                            .setParameter("agent", agent.getId())
                            .setParameter("bookingStatus1", BookingStatus.PAID)
                            .setParameter("bookingStatus2", BookingStatus.CANCELED)
                            .getResultList() :

                    getEntityManager().createNamedQuery("Booking.getAllWithStatus")
                            .setParameter("bookingStatus1", BookingStatus.PAID)
                            .setParameter("bookingStatus2", BookingStatus.CANCELED)
                            .getResultList();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bookingModel;
    }

    public ShareJS share(ShareRequest shareRequest) {
        Booking booker = find(shareRequest.bookingId);
        Booking sharer = new Booking();
        sharer.setRoute(booker.getRoute());
        sharer.setOrderType(OrderType.SHARE_REQUEST);
        sharer.setStatus(BookingStatus.SHARE_REQUEST_SENT);
        sharer.setBooker(booker);
        sharer.setName(shareRequest.sharingName);
        sharer.setLandingTime(shareRequest.sharingLandingTime);
        sharer.setFlightNo(shareRequest.sharingFlightNo);
        sharer.setEmail(shareRequest.sharingEmail);
        sharer.setDateText(booker.getDateText());
        sharer.setPax(shareRequest.sharingPax);
        sharer.setSurfboards(shareRequest.sharingSurfboards);
        sharer.setRequirements(shareRequest.requirements);
        persist(sharer);

        mailer.sendShareRequest(sharer, booker, profileManager.getProfile(), shareRequest.requirements);

        ShareJS shareJS = new ShareJS();
        shareJS.orderType = booker.getOrderType().ordinal();
        shareJS.confirmed = true;

        return shareJS;

    }

    public Booking shareRequestResponse(String cmd, Long bookingId) {
        if (AGREE.equals(cmd) || REFUSE.equals(cmd) || BOOK.equals(cmd)) {
            Booking sharer = find(bookingId);
            if (sharer != null) {

                BookingStatus bookingStatus = null;
                if (AGREE.equals(cmd))
                    bookingStatus = SHARE_ACCEPTED;
                if (REFUSE.equals(cmd))
                    bookingStatus = SHARE_REFUSED;
                if (BOOK.equals(cmd))
                    bookingStatus = SHARE_OFFER_ACCEPTED;
                if (bookingStatus != null) {

                    sharer.setStatus(bookingStatus);
                    persist(sharer);
                    if (SHARE_ACCEPTED.equals(bookingStatus) || SHARE_REFUSED.equals(bookingStatus)) {
                        mailer.sendShareRequestAccepted(bookingStatus, sharer, profileManager.getProfile());
                    }
                }
            }
            return sharer;
        }
        if (REQUEST.equals(cmd)) {
            return find(bookingId);
        }
        return null;
    }

    public void cancelBooking(Long bookingId) {
        Booking booking = find(bookingId);
        if (booking != null) {
            booking.setStatus(BookingStatus.CANCELED);
        }
    }
}
