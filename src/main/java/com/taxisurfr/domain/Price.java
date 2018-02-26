package com.taxisurfr.domain;


import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@NamedQueries({
        @NamedQuery(name = "Price.getAll", query = "SELECT s FROM Price s" ),
        @NamedQuery(name = "Price.getByContractor", query = "SELECT s FROM Price s WHERE s.contractor = :contractor" ),
        @NamedQuery(name = "Price.getByLocation", query = "SELECT s FROM Price s WHERE s.startroute = :start and s.endroute = :end" ),
        @NamedQuery(name = "Price.getByLocationAndContractor", query = "SELECT s FROM Price s WHERE s.startroute = :start and s.endroute = :end and s.contractor= :contractor" )
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
    private Location startroute;

    @OneToOne(fetch = FetchType.EAGER)
    private Location endroute;

    @Column
    private Long cents;

    @Column
    private String link;
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }


    public Long getId() {
        return id;
    }

    public Location getStartroute() {
        return startroute;
    }

    public void setStartroute(Location startroute) {
        this.startroute = startroute;
    }

    public Location getEndroute() {
        return endroute;
    }

    public void setEndroute(Location endroute) {
        this.endroute = endroute;
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

    public Long getCents() {
        return cents;
    }

    public void setCents(Long cents) {
        this.cents = cents;
    }



}