package com.taxisurfr.domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;

@Entity
@Table
@NamedQueries({
    @NamedQuery(name = "Finance.getByAgent", query = "SELECT s FROM Finance s WHERE s.agentEmail = :agentEmail and financeType=:financeType order by date"),
    @NamedQuery(name = "Finance.getByAllAgents", query = "SELECT s FROM Finance s WHERE financeType=:financeType order by date"),
    @NamedQuery(name = "Finance.getByBookingId", query = "SELECT s FROM Finance s WHERE s.bookingId = :bookingId and financeType=:financeType"),
})
@XmlRootElement
public class Finance implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "idGeneratorSeq", sequenceName = "idSequence")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "idGeneratorSeq")
    private long id;

    @Column private FinanceType financeType;
    @Column private LocalDateTime date;
    @Column private String name;
    @Column private Long bookingId;
    @Column private String charge;
    @Column private int amount;
    @Column private Currency currency = Currency.USD;
    @Column private String agentEmail;

    public String getAgentEmail() {
        return agentEmail;
    }

    public void setAgentEmail(String agentEmail) {
        this.agentEmail = agentEmail;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int compareTo(Finance other) {
        // compareTo should return < 0 if this is supposed to be
        // less than other, > 0 if this is supposed to be greater than
        // other and 0 if they are supposed to be equal
        return this.date.isAfter(date) ? -1 : -1;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setName(String name) {
        this.name = name;

    }
    public String getName() {
        return name;
    }

    public FinanceType getFinanceType() {
        return financeType;
    }

    public void setFinanceType(FinanceType financeType) {
        this.financeType = financeType;
    }

    public void setType(FinanceType financeType) {
        this.financeType = financeType;
    }

    public void setBookingId(long bookingId) {
        this.bookingId = bookingId;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public String getCharge() {
        return this.charge;
    }
    public void setCharge(String id) {
        this.charge = id;
    }
}
