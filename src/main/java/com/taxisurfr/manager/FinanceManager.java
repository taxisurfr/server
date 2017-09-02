package com.taxisurfr.manager;

import com.taxisurfr.domain.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Stateless
public class FinanceManager extends AbstractDao<Finance> {
    @Inject
    Logger logger;

    @Inject
    StripePayment stripePayment;
    @Inject
    BookingManager bookingManager;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public FinanceModel getFinances(String email, boolean admin) {
        FinanceModel financeModel = new FinanceModel();
        financeModel.admin = admin;
        try {
            List<Finance> payments = !admin ?
                    getEntityManager().createNamedQuery("Finance.getByAgent")
                            .setParameter("agentEmail", email)
                            .setParameter("financeType", FinanceType.PAYMENT)
                            .getResultList()
                    :
                    getEntityManager().createNamedQuery("Finance.getByAllAgents")
                            .setParameter("financeType", FinanceType.PAYMENT)
                            .getResultList();

            List<Finance> transfers = !admin ?
                    getEntityManager().createNamedQuery("Finance.getByAgent")
                            .setParameter("agentEmail", email)
                            .setParameter("financeType", FinanceType.TRANSFER)
                            .getResultList()
                    :
                    getEntityManager().createNamedQuery("Finance.getByAllAgents")
                            .setParameter("financeType", FinanceType.TRANSFER)
                            .getResultList();

            int totalTransfers = 0;

            int carriedOutOrders = 0;
            int openOrders = 0;
            LocalDateTime now = LocalDateTime.now();
            for (Finance finance : payments) {
                Booking booking = bookingManager.find(finance.getBookingId());

                if (booking==null || booking.getDate().isBefore(now)) {
                    carriedOutOrders +=finance.getAmount();
                } else {
                    openOrders += finance.getAmount();
                }
            }
            for (Finance finance : transfers) {
                totalTransfers+=finance.getAmount();
            }
            int currentBalance = totalTransfers - carriedOutOrders;

            financeModel.summaryList = new ArrayList<>();

            FinanceDetail financeDetail = new FinanceDetail();
            financeDetail.name = "Current Balance";
            financeDetail.currency = "USD";
            financeDetail.amount = currentBalance;
            financeModel.summaryList.add(financeDetail);

            financeDetail = new FinanceDetail();
            financeDetail.name = "Total Transfers";
            financeDetail.currency = "USD";
            financeDetail.amount = totalTransfers;
            financeModel.summaryList.add(financeDetail);

            financeDetail = new FinanceDetail();
            financeDetail.name = "Carried Out Order";
            financeDetail.currency = "USD";
            financeDetail.amount = carriedOutOrders;
            financeModel.summaryList.add(financeDetail);

            financeDetail = new FinanceDetail();
            financeDetail.name = "Open Orders";
            financeDetail.currency = "USD";
            financeDetail.amount = openOrders;
            financeModel.summaryList.add(financeDetail);

            financeModel.paymentList = payments.stream().map(new Function<Finance, Object>() {
                @Override
                public FinanceDetail apply(Finance finance) {
                    FinanceDetail financeDetail = new FinanceDetail();
                    Booking booking = bookingManager.find(finance.getBookingId());
                    financeDetail.name = booking != null ? booking.getOrderRef() : "deleted";
                    financeDetail.currency = finance.getCurrency().symbol;
                    financeDetail.amount = (float) (finance.getAmount());
                    financeDetail.datetime = finance.getDate().toInstant(ZoneOffset.UTC).getEpochSecond() * 1000;
                    financeDetail.dateText = finance.getDate().format(formatter);
                    financeDetail.bookingId = finance.getBookingId();
                    return financeDetail;
                }
            }).collect(Collectors.toList());

            financeModel.transferList = transfers.stream().map(new Function<Finance, Object>() {
                @Override
                public Object apply(Finance finance) {
                    FinanceDetail financeDetail = new FinanceDetail();
                    financeDetail.name = finance.getName();

                    financeDetail.currency = finance.getCurrency().symbol;
                    financeDetail.dateText = finance.getDate().format(formatter);
                    financeDetail.amount = (float) (finance.getAmount());
                    return financeDetail;
                }
            }).collect(Collectors.toList());
            ;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return financeModel;
    }

    public FinanceModel addTransfer(String email, String name,boolean admin) {
        Agent agent = (Agent) getEntityManager().createNamedQuery("Agent.getByEmail")
                .setParameter("email", email)
                .getResultList().get(0);

        Finance finance = new Finance();
        finance.setAgentEmail(agent.getEmail());
        finance.setType(FinanceType.TRANSFER);
        finance.setDate(LocalDateTime.now());

        getEntityManager().persist(finance);

        return getFinances(email,admin);
    }

    public FinanceModel cancelBooking(String email, Long bookingId,boolean admin) {
        FinanceModel financeModel = null;
        Finance finance = getByBookingId(bookingId);
        if (finance != null) {

            stripePayment.refund(finance);

            getEntityManager().persist(finance);

        }
        return getFinances(email,admin);
    }

    private Finance getByBookingId(Long bookingId) {
        List<Finance> finance = getEntityManager().createNamedQuery("Finance.getByBookingId")
                .setParameter("bookingId", bookingId)
                .setParameter("financeType", FinanceType.PAYMENT)
                .getResultList();
        return finance.size() > 0 ? finance.get(0) : null;
    }

    public String payment(String token, Booking booking, boolean share) {
        String error = stripePayment.payment(token, booking, share);
        if (error != null) {
            return error;
        }
        if (booking.getOrderType().equals(OrderType.SHARE_REQUEST) && booking.getStatus().equals(BookingStatus.PAID)) {
            Booking booker = booking.getBooker();
            Route route = booker.getRoute();
            //TODO refund everything?
            Finance finance = getByBookingId(booker.getId());

            int centsToJoin = route.getCentsToJoin();
            error = stripePayment.refundBooker(booking.getBooker(), centsToJoin, finance);
            if (error == null) {
                booking.setStatus(BookingStatus.SHARE_PAID);
                bookingManager.persist(booking);
            }
        }
        return error;

    }

    public void saveTransfer(String agentEmail, boolean admin, int cents, String description) {
        Finance finance = new Finance();
        finance.setAmount(cents);
        finance.setFinanceType(FinanceType.TRANSFER);
        finance.setDate(LocalDateTime.now());
        finance.setAgentEmail(agentEmail);
        finance.setName(description);
        persist(finance);
    }
}
