package com.taxisurfr.domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table
@NamedQueries({
    @NamedQuery(name = "Partner.getByCode", query = "SELECT s FROM Partner s WHERE s.code =  :code ")
})
@XmlRootElement
public class Partner implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    public static final long NO_ASSOCIATED = 0L;

    @Column
    public String code;
    @Column
    public String name;
    @Column
    public String email;

    @Column
    private String bankAccount1;
    @Column
    private String bankAccount2;
    @Column
    private String bankAccount3;
    @Column
    private String bankAccount4;

    @Id
    @Column
    private long id;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBankAccount1() {
        return bankAccount1;
    }

    public void setBankAccount1(String bankAccount1) {
        this.bankAccount1 = bankAccount1;
    }

    public String getBankAccount2() {
        return bankAccount2;
    }

    public void setBankAccount2(String bankAccount2) {
        this.bankAccount2 = bankAccount2;
    }

    public String getBankAccount3() {
        return bankAccount3;
    }

    public void setBankAccount3(String bankAccount3) {
        this.bankAccount3 = bankAccount3;
    }

    public String getBankAccount4() {
        return bankAccount4;
    }

    public void setBankAccount4(String bankAccount4) {
        this.bankAccount4 = bankAccount4;
    }
}
