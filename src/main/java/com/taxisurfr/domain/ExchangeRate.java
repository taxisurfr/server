package com.taxisurfr.domain;


import javax.persistence.*;


@Entity
@NamedQueries({
        @NamedQuery(name = "ExchangeRate.getByCurrency", query = "SELECT s FROM ExchangeRate s WHERE s.currency = :currency")
})
public class ExchangeRate implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "idGeneratorSeq", sequenceName = "idSequence")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "idGeneratorSeq")
    @Column private Long id;
    @Column private String date;
    @Column private Currency currency;
    @Column private Double value;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}