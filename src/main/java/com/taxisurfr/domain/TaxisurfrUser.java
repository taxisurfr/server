package com.taxisurfr.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM TaxisurfrUser u"),
    @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM TaxisurfrUser u WHERE u.email = :email"),
    @NamedQuery(name = "User.findByGoogle", query = "SELECT u FROM TaxisurfrUser u WHERE u.google = :google") }
)
@XmlRootElement
public class TaxisurfrUser implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "idGeneratorSeq", sequenceName = "idSequence")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "idGeneratorSeq")
    private long id;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String displayName;

    @Column
    private String facebook;

    @Column
    private String google;

    @Column private boolean admin;
    @Column private Long agentId;
    @Column private Long contractorId;
    @Column private Long dispatchId;

    public enum Provider {
        FACEBOOK("facebook"), GOOGLE("google"), LINKEDIN("linkedin"), GITHUB(
            "github"), FOURSQUARE(
            "foursquare"), TWITTER("twitter");

        String name;

        Provider(final String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

    }

    public enum UserType {
        AGENT("agent"), DISPATCH("dispatch"), CONTRACTOR("linkedin");

        String name;

        UserType(final String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getGoogle() {
        return google;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public void setDisplayName(final String name) {
        this.displayName = name;
    }

    public void setProviderId(final Provider provider, final String value) {
        switch (provider) {
            case FACEBOOK:
                this.facebook = value;
                break;
            case GOOGLE:
                this.google = value;
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public void setUserType(final UserType userType, final Long value) {
        switch (userType) {
            case AGENT:
                this.agentId = value;
                break;
            case CONTRACTOR:
                this.contractorId = value;
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    @JsonIgnore
    public int getSignInMethodCount() throws IllegalArgumentException, IllegalAccessException,
        NoSuchFieldException, SecurityException {
        int count = 0;

        if (this.getPassword() != null) {
            count++;
        }

        for (final Provider p : Provider.values()) {
            if (this.getClass().getDeclaredField(p.name).get(this) != null) {
                count++;
            }
        }

        return count;
    }

}

