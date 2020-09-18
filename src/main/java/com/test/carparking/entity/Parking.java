package com.test.carparking.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author NIsaev on 20.08.2020
 */

@Entity
@Table(name = "parking")
public class Parking {
    //private static final String DATE_FORMAT = "dd.MM.yyyy";
    private static final String DATE_TIME_FORMAT = "dd.MM.yyyy HH:mm";

    @Id
    @SequenceGenerator(name = "parking_gen", sequenceName = "parking_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "parking_gen")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "is_free")
    private Boolean isFree;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    @Column(name = "reg_date")
    private LocalDateTime regDate;

    public Parking() {
    }

    public Parking(Integer id, String name, Boolean isFree, LocalDateTime regDate) {
        this.id = id;
        this.name = name;
        this.isFree = isFree;
        this.regDate = regDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getFree() {
        return isFree;
    }

    public void setFree(Boolean free) {
        isFree = free;
    }

    public LocalDateTime getRegDate() {
        return regDate;
    }

    public void setRegDate(LocalDateTime regDate) {
        this.regDate = regDate;
    }
}
