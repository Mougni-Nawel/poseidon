package com.nnk.springboot.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;


import java.sql.Timestamp;
import java.util.Date;


@Setter
@Getter
@Entity
@Table(name = "curvepoint")
public class CurvePoint {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="curveId")
    private int curveId;

    @Column(name="asOfDate")
    private Date asOfDate;

    @Column(name="term")
    private double term;

    @Column(name="value")
    private double value;

    @Column(name="creationDate")
    private Date creationDate;

    public CurvePoint(int curveId, double term, double value) {
        this.curveId = curveId;
        this.term = term;
        this.value = value;
    }

    public CurvePoint() {

    }


}
