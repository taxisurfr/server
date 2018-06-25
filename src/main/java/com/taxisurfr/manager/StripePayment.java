package com.taxisurfr.manager;

import com.stripe.Stripe;
import com.stripe.model.Charge;
import com.stripe.model.Refund;
import com.taxisurfr.domain.*;
import com.taxisurfr.util.Mailer;
import com.taxisurfr.util.PdfUtil;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class StripePayment {
    private static final Logger logger = Logger.getLogger(StripePayment.class.getName());

    @Inject
    private BookingManager bookingManager;

    @Inject
    RouteManager routeManager;
    @Inject
    AgentManager agentManager;

    @Inject
    ContractorManager contractorManager;
    @Inject
    FinanceDao financeDao;
    @Inject
    Mailer mailer;
    //
    //    @Inject
    //    StatManager statManager;
    //
    @Inject
    ProfileManager profileManager;

    public String payment(String token, Booking booking, boolean share)
            throws IllegalArgumentException {

        String error = null;
        Profile profile = profileManager.getProfile();
        try {
            Contractor contractor = booking.getPrice().getContractor();

            Finance finance = new Finance();
            finance.setType(FinanceType.PAYMENT);
            finance.setCurrency(booking.getCurrency());
            finance.setName(booking.getOrderRef());
            finance.setDate(new Timestamp(new Date().getTime()));
            finance.setAgentEmail(contractor.getEmail());

            long centsToPay = booking.getPaidPrice();

            finance.setAmount(booking.getPrice().getCents().intValue());
            finance.setBookingId(booking.getId());

            error = charge(token, booking, finance, (int)centsToPay);
            if (error==null) {
                booking.setPaidPrice((int)centsToPay);
                booking.setRef(contractor.getOrderCount() + "_" + booking.getName());
                byte[] pdfData = new PdfUtil()
                        .generateTaxiOrder("template/order_with_feedback.pdf", booking, contractor);
                booking.setPdf(pdfData);
                bookingManager.edit(booking);

                mailer.sendConfirmation(booking, profileManager.getProfile(), contractor, share);
                contractor.getOrderCount();

                financeDao.persist(finance);

            }
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
            error =
                    "Sorry about that. We had an internal error. Please contact dispatch@taxisurf.com";
            ex.printStackTrace();
            mailer.sendError(ex, profile);
        }

        return error;
    }

    public String charge(String card, Booking booking , Finance finance, int cents) {

        String error = null;
        try {
            Stripe.apiKey = profileManager.getProfile().getStripeSecret();

            Map<String, Object> chargeParams = new HashMap<String, Object>();

            chargeParams.put("amount", cents);
            chargeParams.put("currency", booking.getCurrency().name().toLowerCase());
            chargeParams.put("card", card); // obtained with Stripe.js
            chargeParams.put("description",
                    "Taxi Charges Sri Lanka - " + RouteFormatter.asRoute(booking.getPrice()) + " - Thank you!");
            logger.info("receipt to " + booking.getEmail());
            chargeParams.put("receipt_email", booking.getEmail());

            logger.info("charging cents " + booking.getCurrency().name().toLowerCase() + cents);
            Charge charge = Charge.create(chargeParams);

            finance.setCharge(charge.getId());
            booking.setStatus(BookingStatus.PAID);
            booking.setPaidPrice(cents);
            logger.info("charging successful");
        } catch (Exception exception) {
            error = exception.getMessage();
            logger.log(Level.SEVERE, exception.getMessage(), exception);
            exception.printStackTrace();
        }
        return error;
    }

    public void refund(Finance finance) {
        Stripe.apiKey = profileManager.getProfile().getStripeSecret();

        Map<String, Object> refundParams = new HashMap<String, Object>();
        refundParams.put("charge", finance.getCharge());

        try {
            Refund.create(refundParams);
            finance.setFinanceType(FinanceType.REFUNDED);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public String refundBooker(Booking booker, int refundedAmount, Finance finance) {
        Stripe.apiKey = profileManager.getProfile().getStripeSecret();
        Map<String, Object> refundParams = new HashMap<String, Object>();
        refundParams.put("charge", finance.getCharge());
        refundParams.put("amount", refundedAmount);
/*
        refundParams.put("card", card); // obtained with Stripe.js
*/
        try {
            Refund.create(refundParams);
            Finance refundedFinance = new Finance();
            refundedFinance.setFinanceType(FinanceType.SHARE_REFUNDED);
            refundedFinance.setAmount(refundedAmount);
            refundedFinance.setBookingId(booker.getId());
        } catch (Exception e) {
            mailer.sendError(e,profileManager.getProfile());
            return e.getMessage();
        }
        return null;

    }
}
