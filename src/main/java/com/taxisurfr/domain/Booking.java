package com.taxisurfr.domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Booking.getByContractor", query = "SELECT s FROM Booking s WHERE s.contractor = :contractor and (bookingStatus=:bookingStatus1 or bookingStatus=:bookingStatus2 )order by instanziated"),
    @NamedQuery(name = "Booking.getAllWithStatus", query = "SELECT s FROM Booking s WHERE (bookingStatus=:bookingStatus1 or bookingStatus=:bookingStatus2 )order by instanziated"),
    @NamedQuery(name = "Booking.getByRoute", query = "SELECT s FROM Booking s WHERE (s.price.startroute = :start and s.price.endroute= :end) and (s.orderType=:orderType1 OR s.orderType=:orderType2)order by date"),
    @NamedQuery(name = "Booking.getByRouteX", query = "SELECT s FROM Booking s order by date"),
})public class Booking implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    public Booking()
    {
        bookingStatus = BookingStatus.BOOKED;
        orderType = OrderType.BOOKING;
        instanziated = new Timestamp(new Date().getTime());

        rated = false;
    }

    @Id
    @SequenceGenerator(name = "idGeneratorSeq", sequenceName = "idSequence")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "idGeneratorSeq")
    private long id;

    @ManyToOne private Booking booker;
    @Column private String name;
    @Column private String email;
    @Column private String dateText;
    @Column private String flightNo;
    @Column private String landingTime;
    @Column private int pax;
    @Column private int surfboards;
    @Column private BookingStatus bookingStatus;
    @Column private String requirements;
    @Lob private byte[] pdf;
    @Column private String ref;
    @Column private Boolean shareWanted;
    @Column private Timestamp instanziated;
    @Column private OrderType orderType;
    private Long parentId;
    @ManyToOne private Route route;
    @Column private Long agent;
    @Column private Long contractor;
    private Boolean rated;
    private String stripeRefusal;
    @Column private Timestamp date;
    @Column private Currency currency = Currency.USD;

    public Timestamp getDate()
    {
        return date;
    }
    public void setDate(Timestamp date)
    {
        this.date = date;
    }
    @Column private int paidPrice;
    @Column private int sharePrice;

    @ManyToOne(fetch = FetchType.EAGER)
    private Price price;

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPaidPrice()
    {
        return paidPrice;
    }

    public void setPaidPrice(int paidPrice)
    {
        this.paidPrice = paidPrice;
    }

    @Deprecated
    public Route getRoute()
    {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Boolean getRated()
    {
        return rated;
    }
    public void setRated(Boolean rated)
    {
        this.rated = rated;
    }

    public void setRated(boolean rated)
    {
        this.rated = rated;
    }

    public Boolean getShareWanted()
    {
        return shareWanted;
    }

    public void setShareWanted(Boolean shareWanted)
    {
        this.shareWanted = shareWanted;
    }

    public Long getParentId()
    {
        return parentId;
    }

    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public OrderType getOrderType()
    {
        return orderType;
    }

    public void setOrderType(OrderType orderType)
    {
        this.orderType = orderType;
    }

    public Timestamp getInstanziated()
    {
        return instanziated;
    }

    public void setInstanziated(Timestamp instanziated)
    {
        this.instanziated = instanziated;
    }

    public void setStatus(BookingStatus status)
    {
        this.bookingStatus = status;
    }

    public BookingStatus getStatus()
    {
        return bookingStatus;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public int getPax()
    {
        return pax;
    }

    public void setPax(int pax)
    {
        this.pax = pax;
    }

    public String getRequirements()
    {
        return requirements;
    }

    public void setRequirements(String requirements)
    {
        this.requirements = requirements;
    }

    public int getSurfboards()
    {
        return surfboards;
    }

    public void setSurfboards(int surfboards)
    {
        this.surfboards = surfboards;
    }


    public String getDateText()
    {
        return dateText;
    }

    public void setDateText(String dateText)
    {
        this.dateText = dateText;
    }

    public String getFlightNo()
    {
        return flightNo;
    }

    public void setFlightNo(String flightNo)
    {
        this.flightNo = flightNo;
    }

    public String getLandingTime()
    {
        return landingTime;
    }

    public void setLandingTime(String landingTime)
    {
        this.landingTime = landingTime;
    }

    public byte[] getPdf()
    {
        return pdf;
    }

    public void setPdf(byte[] pdf)
    {
        this.pdf =pdf;
    }

    public String getRef()
    {
        return ref;
    }

    public void setRef(String ref)
    {
        this.ref = ref;
    }

    public int compareTo(Booking other)
    {
        // compareTo should return < 0 if this is supposed to be
        // less than other, > 0 if this is supposed to be greater than
        // other and 0 if they are supposed to be equal
        return this.instanziated.toLocalDateTime().isAfter(instanziated.toLocalDateTime()) ? -1 : -1;
    }


    private void setCurrency(Currency currency)
    {
        this.currency = currency;
    }
    public Currency getCurrency() {
        return currency;
    }

    public void setStripeRefusal(String stripeRefusal) {
        this.stripeRefusal = stripeRefusal;
    }
    public String getStripeRefusal(){
        return stripeRefusal;
    }

    public String getOrderRef() {
        return ref;
    }

    public Long getAgent() {
        return agent;
    }

    public void setAgent(Long agent) {
        this.agent = agent;
    }
    public Long getContractor() {
        return contractor;
    }

    public void setContractor(Long contractor) {
        this.contractor = contractor;
    }

    public Booking getBooker() {
        return booker;
    }

    public void setBooker(Booking booker) {
        this.booker = booker;
    }

    public int getSharePrice() {
        return sharePrice;
    }

    public void setSharePrice(int sharePrice) {
        this.sharePrice = sharePrice;
    }
}
