package com.taxisurfr.domain;

import javax.enterprise.inject.Model;
import javax.persistence.*;

@Entity
@Table
@NamedQueries({
    @NamedQuery(name = "Agent.getByEmail", query = "SELECT s FROM Agent s WHERE s.email =  :email "),
})
public class Agent implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "idGeneratorSeq", sequenceName = "idSequence")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "idGeneratorSeq")
    private long id;

    @Column private String email;
    @Column private Long mobile;
    @Column private Long orderCount;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @SuppressWarnings("JpaAttributeMemberSignatureInspection")
    public Long getOrderCount() {
        if (orderCount == null) {
            orderCount = 0L;
        }
        return orderCount++;
    }

    public void setMobile(Long mobile) {
        this.mobile = mobile;
    }

    public Long getMobile() {
        return mobile;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
