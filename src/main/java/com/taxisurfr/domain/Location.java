package com.taxisurfr.domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@NamedQueries({
        @NamedQuery(name = "Location.get", query = "SELECT s FROM Location s WHERE s.name = :name" ),
        @NamedQuery(name = "Location.getLike", query = "SELECT s FROM Location s WHERE s.name like :name" )
})
@XmlRootElement
@Table
public class Location implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "idGeneratorSeq", sequenceName = "idSequence")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "idGeneratorSeq")
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column private String name;
}
