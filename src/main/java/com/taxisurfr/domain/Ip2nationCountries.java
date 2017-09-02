package com.taxisurfr.domain;

import javax.persistence.*;

@Entity
@Table
public class Ip2nationCountries implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    public static Ip2nationCountries create(String code, String iso_code_2, String iso_code_3, String iso_country, String country, double lat, double lon) {
        Ip2nationCountries ip2nationCountries = new Ip2nationCountries();
        ip2nationCountries.code = code;
        ip2nationCountries.iso_code_2 = iso_code_2;
        ip2nationCountries.iso_code_3 = iso_code_3;
        ip2nationCountries.country = country;
        ip2nationCountries.iso_country = iso_country;
        ip2nationCountries.lat = (float)lat;
        ip2nationCountries.lon = (float)lon;
        return ip2nationCountries;
    }

    @Id
    private String code;
    @Column
    private String iso_code_2;
    @Column
    private String iso_code_3;
    @Column
    private String iso_country;
    @Column
    private String country;
    @Column
    private Float lat;
    @Column
    private Float lon;

}
