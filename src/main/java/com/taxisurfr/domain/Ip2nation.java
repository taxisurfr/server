package com.taxisurfr.domain;

import javax.persistence.*;

@Entity
@Table
@NamedQueries({
    @NamedQuery(name = "Ip2nation.get", query = "SELECT c.country FROM  Ip2nationCountries c, Ip2nation i WHERE i.ip < :INET_ATON AND c.code = i.country ORDER BY i.ip DESC"),
})
public class Ip2nation implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "idGeneratorSeq", sequenceName = "idSequence")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "idGeneratorSeq")
    private long id;

    @Column
    private Long ip;

    @Column
    private String country;

    public static Ip2nation create(long i, String country) {
        Ip2nation ip2nation = new Ip2nation();
        ip2nation.ip = i;
        ip2nation.country = country;
        return ip2nation;
    }

    public Long getIp() {
        return ip;
    }

    public void setIp(Long ip) {
        this.ip = ip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
