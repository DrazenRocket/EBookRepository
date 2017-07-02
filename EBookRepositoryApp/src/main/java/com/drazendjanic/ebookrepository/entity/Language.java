package com.drazendjanic.ebookrepository.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "language")
public class Language implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "language_id")
    private Long id;

    @Column(name = "language_name", nullable = false)
    private String name;

    public Language() {

    }

    public Language(String name) {
        this.name = name;
    }

    public Language(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
