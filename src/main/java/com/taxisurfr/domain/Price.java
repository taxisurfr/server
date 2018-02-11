package com.taxisurfr.domain;


import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@NamedQueries({
        @NamedQuery(name = "Price.getAll", query = "SELECT s FROM Price s" ),
        @NamedQuery(name = "Price.getByContractorAndRoute", query = "SELECT s FROM Price s WHERE s.contractor = :contractor and s.route = :route" ),
        @NamedQuery(name = "Price.getByContractor", query = "SELECT s FROM Price s WHERE s.contractor = :contractor" ),
        @NamedQuery(name = "Price.getByRoute", query = "SELECT s FROM Price s WHERE s.route = :route" )
})
@XmlRootElement
public class Price implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    private Contractor contractor;

    @OneToOne(fetch = FetchType.EAGER)
    private Route route;

    @Column
    private Long cents;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Contractor getContractor() {
        return contractor;
    }

    public void setContractor(Contractor contractor) {
        this.contractor = contractor;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Long getCents() {
        return cents;
    }

    public void setCents(Long cents) {
        this.cents = cents;
    }



}