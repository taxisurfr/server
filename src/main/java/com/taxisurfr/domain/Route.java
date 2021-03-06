package com.taxisurfr.domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table
@NamedQueries({
        @NamedQuery(name = "Route.getAll", query = "SELECT s FROM Route s"),
        @NamedQuery(name = "Route.getByContractor", query = "SELECT s FROM Route s WHERE s.contractorId = :contractorId "),
        @NamedQuery(name = "Route.getByQuery", query = "SELECT s FROM Route s WHERE s.startroute like  :query "),
        @NamedQuery(name = "Route.getByLink", query = "SELECT s FROM Route s WHERE s.link =  :link "),
        @NamedQuery(name = "Route.getByDescLink", query = "SELECT s FROM Route s WHERE s.description =  :link "),
        @NamedQuery(name = "Route.getByStartEnd", query = "SELECT s FROM Route s WHERE s.startroute =  :startroute AND s.endroute = :endroute "),
        @NamedQuery(name = "Route.getByStart", query = "SELECT s FROM Route s WHERE s.startroute =  :startroute ")
})
@XmlRootElement
public class Route implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    public static final long NO_ASSOCIATED = 0L;

    @Column
    public String startroute;
    @Column
    public String endroute;

    @Column(length = 1337)
    private String description;

    public Long getContractorId() {
        return contractorId;
    }

    public void setContractorId(Long contractorId) {
        this.contractorId = contractorId;
    }

    @Column
    private Long contractorId;


    @Column
    private PickupType pickupType = PickupType.AIRPORT;
    @Column
    private int centsToJoin;
    @Column
    private Currency agentCurrency = Currency.USD;
    private Long agentCents;
    private Long image;

    private Long associatedRoute = NO_ASSOCIATED;
    @Column
    private String link;



    @Id
    @SequenceGenerator(name = "idGeneratorSeq", sequenceName = "idSequence")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "idGeneratorSeq")
    private long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStartroute() {
        return startroute;
    }

    public void setStartroute(String startroute) {
        this.startroute = startroute;
    }

    public String getEndroute() {
        return endroute;
    }

    public void setEndroute(String endroute) {
        this.endroute = endroute;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PickupType getPickupType() {
        return pickupType;
    }

    public void setPickupType(PickupType pickupType) {
        this.pickupType = pickupType;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
